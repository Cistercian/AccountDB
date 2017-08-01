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
import java.util.concurrent.FutureTask;

/**
 * Created by Olaf on 31.07.2017.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


    private static final byte COUNT_THREAD = 10;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(COUNT_THREAD);

    private static final int COUNT_CREATED_ENTITIES = 1000000;

    public List<Account> findAll() {
        return Lists.newArrayList(accountRepository.findAll());
    }

    public void deleteData() {
        logger.debug(LogUtil.getMethodName());

        for (Account account : accountRepository.findAll()){
            accountRepository.delete(account);
        }

    }

    public void mockData() {
        logger.debug(LogUtil.getMethodName());

        long currentId = accountRepository.getCurrentMaxId();
/*
        if (COUNT_CREATED_ENTITIES > COUNT_THREAD) {
            List<Boolean> futureListCreators = new ArrayList<Boolean>();
            for (int i = 0; i < COUNT_THREAD; i++){
                int distance = COUNT_CREATED_ENTITIES / COUNT_THREAD;

                FutureTask futureTask = executorService.submit(new CreatorEntities(
                        distance * i,
                        distance * (i + 1)));
            }
        } else {*/
            createEntities(0, COUNT_CREATED_ENTITIES, currentId);
        /*}*/
    }

    class CreatorEntities implements Callable<Boolean>{
        private int startCount;
        private int endCount;

        public CreatorEntities(int startCount, int endCount) {
            this.startCount = startCount;
            this.endCount = endCount;
        }

        public Boolean call() throws Exception {
            try {
                createEntities(startCount, endCount, 0);
            } catch (Exception e) {
                return false;
            }

            return true;
        }
    }

    private void createEntities(int startCount, int endCount, long currentId) {
        for (int i = startCount; i <= endCount; i++) {
            Account account = new Account(String.valueOf(++currentId));
            accountRepository.save(account);

            Client client = new Client(String.format("client_%d", currentId), account);
            clientRepository.save(client);
        }
    }

    public JsonResponse findEntity(String query){
        logger.debug(LogUtil.getMethodName());

        if (query == null)
            throw new IllegalArgumentException();

        List<EntityData> entityData = clientRepository.findEntity(query);

        JsonResponse jsonResponse;
        String message;
        if (entityData != null && entityData.size() > 0) {
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

    public JsonResponse createEntity(String name, String acct) {
        logger.debug(LogUtil.getMethodName());

        JsonResponse jsonResponse;

        try {
            Account account = new Account(acct);
            accountRepository.save(account);

            Client client = new Client(name, account);
            clientRepository.save(client);

            jsonResponse = new JsonResponse(JsonResponseType.SUCCESS, "Запись успешно создана");
        } catch (Exception e) {
            jsonResponse = new JsonResponse(JsonResponseType.ERROR, ExceptionUtils.getRootCause(e).getMessage());
        }

        logger.debug(jsonResponse.toString());
        return jsonResponse;
    }

    public JsonResponse deleteEntity(Integer id) {
        logger.debug(LogUtil.getMethodName());

        JsonResponse jsonResponse;
        try {
            accountRepository.delete(id);
            jsonResponse = new JsonResponse(JsonResponseType.SUCCESS, "Запись успешно удалена");
        } catch (Exception e) {
            jsonResponse = new JsonResponse(JsonResponseType.ERROR, ExceptionUtils.getRootCause(e).getMessage());
        }

        logger.debug(jsonResponse.toString());
        return jsonResponse;
    }

    public Integer getTotalCount() {
        logger.debug(LogUtil.getMethodName());
        return accountRepository.getTotalCount();
    }
}
