package ru.hd.olaf.entities;

import javax.persistence.*;

/**
 * Created by Olaf on 31.07.2017.
 */
@Entity
@Table(name = "accounts", schema = "account_db")
public class Account {
    private Integer id;
    private String acct;

    private Client client;

    public Account() {
    }

    public Account(String acct) {
        this.acct = acct;
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
    @Column(name = "acct")
    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
