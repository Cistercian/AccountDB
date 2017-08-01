package ru.hd.olaf.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.hd.olaf.entities.Client;
import ru.hd.olaf.util.EntityData;

import java.util.List;

/**
 * Created by Olaf on 31.07.2017.
 */
public interface ClientRepository extends JpaRepository<Client, Integer> {

    /**
     * Ищет объекты БД по переданной строке, сравнивая ее с полями accounts.acct и clients.name,
     * и возвращает список объектов EntityData для их последующей передачи на страницу index.jsp
     *
     * @param query Искомая строка String
     * @return  список EntityData
     */
    @Query("SELECT new ru.hd.olaf.util.EntityData(a.id, c.name, a.acct) " +
            "FROM Client c LEFT JOIN c.account a " +
            "WHERE ?1 != NULL AND (c.name = ?1 OR a.acct = ?1) ")
    List<EntityData> findEntity(String query);

}
