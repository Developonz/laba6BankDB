package org.example.Data;

import javax.persistence.*;

@Entity
@Table(name = "IdCounter")
public class IdCounter {
    @Id
    private String bank;

    @Column
    private long countClient;

    @Column
    private long countAcc;

    public IdCounter() {};

    public IdCounter(String bank) {
        this.bank = bank;
        countClient = 0;
        countAcc = 0;
    }

    public String getBank() {
        return bank;
    }

    public long getCountClient() {
        return countClient;
    }

    public void setCountClient(long countClient) {
        this.countClient = countClient;
    }

    public long getCountAcc() {
        return countAcc;
    }

    public void setCountAcc(long countAcc) {
        this.countAcc = countAcc;
    }
}
