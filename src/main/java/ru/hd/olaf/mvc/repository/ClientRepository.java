package ru.hd.olaf.mvc.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.hd.olaf.entities.Client;
import ru.hd.olaf.util.EntityData;

/**
 * Created by Olaf on 31.07.2017.
 */
public interface ClientRepository extends CrudRepository<Client, Integer> {

    @Query("SELECT new ru.hd.olaf.util.EntityData(a.id, c.name, a.acct) " +
            "FROM Client c LEFT JOIN c.account a " +
            "WHERE ?1 != NULL AND (c.name = ?1 OR a.acct = ?1) ")
    EntityData findEntity(String query);

}
