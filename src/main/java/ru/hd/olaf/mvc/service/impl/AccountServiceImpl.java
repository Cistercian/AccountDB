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

import java.util.List;

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

        for (int i = 0; i < 10000; i ++){
            Account account = new Account(String.valueOf(i));
            accountRepository.save(account);

            Client client = new Client(String.format("client_%d", i), account);
            clientRepository.save(client);
        }

    }

    public JsonResponse findEntity(String query){
        logger.debug(LogUtil.getMethodName());

        if (query == null)
            throw new IllegalArgumentException();

        EntityData entityData = clientRepository.findEntity(query);

        JsonResponse jsonResponse;
        String message;
        if (entityData != null) {
            message = "Данные найдены";
            jsonResponse = new JsonResponse(JsonResponseType.SUCCESS, message, entityData);
        } else {
            message = "Данные по искомой строке не найдены";
            jsonResponse = new JsonResponse(JsonResponseType.INFO, message);
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
