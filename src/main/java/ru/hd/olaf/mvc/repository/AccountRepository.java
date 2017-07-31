package ru.hd.olaf.mvc.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.hd.olaf.entities.Account;
import ru.hd.olaf.util.JsonResponse;

/**
 * Created by Olaf on 31.07.2017.
 */
public interface AccountRepository extends CrudRepository<Account, Integer> {

    @Query("SELECT COUNT(a) FROM Account a")
    Integer getTotalCount();

    @Query("SELECT MAX(a.id) FROM Account a")
    Long getCurrentMaxId();
}
