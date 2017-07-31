package ru.hd.olaf.mvc.service;

import ru.hd.olaf.entities.Account;
import ru.hd.olaf.util.JsonResponse;

import java.util.List;

/**
 * Created by Olaf on 31.07.2017.
 */
public interface AccountService {

    Integer getTotalCount();

    List<Account> findAll();

    void deleteData();

    void mockData();

    JsonResponse findEntity(String query);

    JsonResponse createEntity(String name, String acct);

    JsonResponse deleteEntity(Integer id);


}
