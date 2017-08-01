package ru.hd.olaf.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.hd.olaf.entities.Account;
import ru.hd.olaf.util.JsonResponse;

/**
 * Created by Olaf on 31.07.2017.
 *
 *
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {

    /**
     * Функция возвращает текущее кол-во записей в таблице accounts (для отображения на index.jsp)
     * @return Integer
     */
    @Query("SELECT COUNT(a) FROM Account a")
    Integer getTotalCount();

    /**
     * Функция возвращает последний id (primare key таблицы account) или 0, если таблицы пуста (используется для
     * генерации уникального поля accounts.acct
     * @return Long
     */
    @Query("SELECT COALESCE(MAX(a.id), 0) FROM Account a")
    Long getCurrentMaxId();

    @Modifying
    @Query("DELETE FROM Account ")
    void deleteData();
}
