package ru.hd.olaf.entities;

import javax.persistence.*;

/**
 * Created by Olaf on 31.07.2017.
 */
@Entity
@Table(name = "clients", schema = "account_db")
public class Client {
    private Integer id;
    private String name;

    private Account account;

    public Client() {
    }

    public Client(String name, Account account) {
        this.name = name;
        this.account = account;
    }

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account")
    //@PrimaryKeyJoinColumn
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
