package org.example.Data.BankAccs;

import org.example.Data.Client;
import org.example.Exceptions.ErrorMoneyException;
import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "accountType")
public abstract class BankAccount {
    @EmbeddedId
    private AccountKey key;

    @Column
    private double money;

    @Column
    private String title;

    @ManyToOne()
    @JoinColumns({
            @JoinColumn(name = "owner_id", referencedColumnName = "client_id"),
            @JoinColumn(name = "owner_bank", referencedColumnName = "bank")
    })
    private Client owner;


    public BankAccount() {};

    public BankAccount(Client client, long id) {
        this.key = new AccountKey(client.getBank(), id);
        this.money = 0;
        this.owner = client;
    }


    public abstract void transactMoney(BankAccount account, double money) throws ErrorMoneyException;

    public void topUpAcc(double money) throws ErrorMoneyException {
        if (money > 0) {
            this.money += money;
        } else {
            throw new ErrorMoneyException("Сумма должна быть больше 0.");
        }
    }

    public void topDownAcc(double money) throws ErrorMoneyException {
        if (money > 0) {
            if (this instanceof org.example.Data.BankAccs.CreditAcc) {
                if (this.money - money >= -10000) {
                    this.money -= money;
                } else {
                    throw new ErrorMoneyException("У вас недостаточно средств");
                }
            } else {
                if (this.money >= money) {
                    this.money -= money;
                } else {
                    throw new ErrorMoneyException("У вас недостаточно средств");
                }
            }
        } else {
            throw new ErrorMoneyException("Сумма должна быть больше 0.");
        }
    }

    public void renameTitle(String title) throws ErrorMoneyException {
        boolean coincid = false;
        for (BankAccount acc : this.owner.getAccs()) {
            if (acc.getTitle().equals(title) && acc != this) {
                coincid = true;
                break;
            }
        }
        if (coincid) {
            throw new ErrorMoneyException("У вас уже есть счёт с таким названием");
        } else {
            this.title = title;
        }
    }

    public void removeAccount() throws ErrorMoneyException {
        if (this.money != 0) {
            throw new ErrorMoneyException("Вы не можете закрыть счёт");
        }
        this.owner.getAccs().remove(this);
    }


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getMoney() {
        return money;
    }

    public Client getOwner() {
        return this.owner;
    }

    public String getBank() {
        return key.getBank();
    }

}
