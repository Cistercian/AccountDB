package ru.hd.olaf.mvc.service.impl;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hd.olaf.entities.Account;
import ru.hd.olaf.entities.Client;
import ru.hd.olaf.mvc.repository.AccountRepository;
import ru.hd.olaf.mvc.repository.ClientRepository;
import ru.hd.olaf.mvc.service.AccountService;
import ru.hd.olaf.util.EntityData;
import ru.hd.olaf.util.JsonResponse;
import ru.hd.olaf.util.JsonResponseType;
import ru.hd.olaf.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Olaf on 31.07.2017.
 *
 * Единственный сервисный уровень...
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;        //связывание с JPA репозитарием таблицы accounts
    @Autowired
    private ClientRepository clientRepository;          //связывание с JPA репозитарием таблицы clients
//    @PersistenceContext
//    private EntityManager entityManager;              //тест пакетного создания записец БД

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class); //логгер

    private static final byte COUNT_THREAD = 10;        //кол-во потоков, запускаемых для автоматического создания
                                                        // записей БД
    //Пул потоков для автоматического заполнения БД
    private static final ExecutorService executorService = Executors.newFixedThreadPool(COUNT_THREAD);
    //кол-во создаваемых записей (по одной на каждужю из таблиц)
    private static final int COUNT_CREATED_ENTITIES = 1000000;

    /**
     * Функция возвращает список всех записей таблицы accounts
     * @return список
     */
    @Deprecated
    public List<Account> findAll() {
        return Lists.newArrayList(accountRepository.findAll());
    }

    /**
     * Функция служебного удаления данных (связанное удаление записец accounts -> clients)
     */
    public void deleteData() {
        logger.debug(LogUtil.getMethodName());

        for (Account account : accountRepository.findAll()) {
            accountRepository.delete(account);
        }

    }

    /**
     * Функция многопоточного создания записец БД (служебная функция на странице /admin/admin.jsp)
     */
    public void mockData() {
        logger.debug(LogUtil.getMethodName());

        long currentId = accountRepository.getCurrentMaxId();

        //для теста оставляет однопоточный ввод
        if (COUNT_CREATED_ENTITIES > COUNT_THREAD) {
            logger.debug("Инициализация запуска потоков");

            //разбиваем кол-во задач на текущее кол-во потоков
            List<Future<Boolean>> futureListCreators = new ArrayList<Future<Boolean>>();
            for (int i = 1; i <= COUNT_THREAD; i++) {
                //кол-во создаваемых пар записей на один поток
                int distance = COUNT_CREATED_ENTITIES / COUNT_THREAD;

                //все потоки генерируют distance пар записей кроме последнего - тот все оставшиеся после деления
                Future future = executorService.submit(new CreatorEntities(
                        distance * (i - 1),
                        (i != COUNT_THREAD ? distance * i - 1 : COUNT_CREATED_ENTITIES - 1),
                        currentId + distance * (i - 1) + 1));

                futureListCreators.add(future);
            }
            logger.debug("Пул потоков заполнен. Ожидание завершения их работы.");
            //не выходим из метода до полного завершения обработки для корректного замера длительности всего процесса
            for (Future<Boolean> future : futureListCreators) {
                while (!future.isDone()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        logger.debug("Ошибка: попытка остановить работу.");
                        e.printStackTrace();
                    }
                }
            }
            logger.debug("Все потоки завершены.");
        } else {
            logger.debug("Инициализация однопоточного импорта.");
            createEntities(0, COUNT_CREATED_ENTITIES, currentId);
        }
    }

    /**
     * Внутренний класс для реализации многопоточности
     */
    class CreatorEntities implements Callable<Boolean> {
        private int startCount;     //начальный номер создаваемой пары записей (по факту используется только для логов)
        private int endCount;       //конечный номер создаваемый пары записей
        private long currentId;     //уникальный идентификатор (используется для генерации уникального accounts.acct)

        String threadName = Thread.currentThread().getName();

        public CreatorEntities(int startCount, int endCount, long currentId) {
            this.startCount = startCount;
            this.endCount = endCount;
            this.currentId = currentId;
        }

        public Boolean call() throws Exception {
            try {
                logger.debug(String.format("%s: инициализация импорта данных. Параметры: startCount=%d, endCount=%d, currentId=%d",
                        threadName, startCount, endCount, currentId));
                createEntities(startCount, endCount, currentId);
            } catch (Exception e) {
                logger.debug(String.format("%s: Ошибка. Принудительная остановка потока", threadName));
                return false;
            }

            logger.debug(String.format("%s: Поток успешно завершил работу", threadName));
            return true;
        }
    }

    /**
     * Служебная функция создания пар записей в таблицах accounts и clients
     * кол-во создаваемых пар = endCount - startCount
     *
     * @param startCount    //начальный номер пары (используется для наглядности результативности многопоточности)
     * @param endCount      //конечный номер создаваемой пары
     * @param currentId     //начальный уникальный номер (для уникального поля accounts.acct)
     */
    private void createEntities(int startCount, int endCount, long currentId) {
        int count = 0;
        for (int i = startCount; i <= endCount; i++) {
            Account account = new Account(String.valueOf(++currentId));
            //При пакетном создании записей исключаем поедание памяти и превышения кол-ва одновременного подключения к БД
            //(значение hibernate.jdbc.batch_size в persistence.xml)
            // - при каждом 20 вызове функции сохранения применяем и очищаем кеш гибернейта.
            if (++count % 20 == 0)
                accountRepository.saveAndFlush(account);
            else
                accountRepository.save(account);

            //Создаем подчиненную запись в таблице clients
            Client client = new Client(String.format("client_%d", currentId), account);
            if (count % 20 == 0)
                clientRepository.saveAndFlush(client);
            else
                clientRepository.save(client);
        }
    }

    /**
     * Функция поиска данных в БД. Переданная строка сравнивается с accounts.acct ИЛИ clients.name.
     * Возвращается объект JsonResponse, содержащий соощение о результате поиска и первый элемент из найденного списка
     *
     * @param query   Искомая строка
     * @return  объект JsonResponse (для передаче данных в виде JSON на страницу index.jsp)
     */
    public JsonResponse findEntity(String query) {
        logger.debug(LogUtil.getMethodName());

        if (query == null)
            throw new IllegalArgumentException();

        List<EntityData> entityData = clientRepository.findEntity(query);

        JsonResponse jsonResponse;
        String message;

        if (entityData != null && entityData.size() > 0) {
            //на всякий случай предупреждаем о нескольких совпадениях
            if (entityData.size() > 1) {
                message = String.format("Найдено больше 1 записи (совпадений: %d)", entityData.size());
                jsonResponse = new JsonResponse(JsonResponseType.INFO, message, entityData.get(0));
            } else {
                message = "Данные найдены";
                jsonResponse = new JsonResponse(JsonResponseType.SUCCESS, message, entityData.get(0));
            }
        } else {
            message = "Данные по искомой строке не найдены";
            jsonResponse = new JsonResponse(JsonResponseType.ERROR, message);
        }

        logger.debug(jsonResponse.toString());
        return jsonResponse;
    }

    /**
     * Функция ручного создания пары записей accounts и clients
     *
     * @param name ФИО абонента
     * @param acct Лицевой счет
     * @return объект JsonResponse с результатом сохранения (при ошибке транслируем ответ СУДБ)
     */
    public JsonResponse createEntity(String name, String acct) {
        logger.debug(LogUtil.getMethodName());

        JsonResponse jsonResponse;
        //TODO: transaction
        try {
            Account account = new Account(acct);
            accountRepository.save(account);

            Client client = new Client(name, account);
            clientRepository.save(client);

            jsonResponse = new JsonResponse(JsonResponseType.SUCCESS, "Запись успешно создана");
        } catch (Exception e) {
            //СУБД возвратила ошибку - передаем ее в составе объекта JsonResponse
            jsonResponse = new JsonResponse(JsonResponseType.ERROR, ExceptionUtils.getRootCause(e).getMessage());
        }

        logger.debug(jsonResponse.toString());
        return jsonResponse;
    }

    /**
     * Функция ручного удаления пары записей таблиц
     *
     * @param id primary key удаляемой записи таблицы accounts (clients удаляется по связи)
     * @return  Объект JsonResponse с результатом операции
     */
    public JsonResponse deleteEntity(Integer id) {
        logger.debug(LogUtil.getMethodName());

        JsonResponse jsonResponse;
        //TODO: transaction
        try {
            accountRepository.delete(id);
            jsonResponse = new JsonResponse(JsonResponseType.SUCCESS, "Запись успешно удалена");
        } catch (Exception e) {
            //СУБД возвратила ошибку - передаем ее в составе объекта JsonResponse
            jsonResponse = new JsonResponse(JsonResponseType.ERROR, ExceptionUtils.getRootCause(e).getMessage());
        }

        logger.debug(jsonResponse.toString());
        return jsonResponse;
    }

    /**
     * Функция получения общего кол-ва записец в БД для отображения на странице index.jsp
     * @return Integer
     */
    public Integer getTotalCount() {
        logger.debug(LogUtil.getMethodName());
        return accountRepository.getTotalCount();
    }
}
