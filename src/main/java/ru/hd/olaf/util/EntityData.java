package ru.hd.olaf.util;

/**
 * Created by Olaf on 31.07.2017.
 * Служебный класс для передачи на фронт данных о найденной паре записец accounts-clients
 */
public class EntityData {
    private Integer id;         //accounts.id
    private String name;        //ФИО абонента
    private String acct;        //Лицевой счет

    public EntityData() {
    }

    public EntityData(Integer id, String name, String acct) {
        this.id = id;
        this.name = name;
        this.acct = acct;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    @Override
    public String toString() {
        return "EntityData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", acct='" + acct + '\'' +
                '}';
    }
}
