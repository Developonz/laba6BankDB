package org.example.Data;

import org.example.Data.BankAccs.BankAccount;
import org.example.Data.BankAccs.CreditAcc;
import org.example.Data.BankAccs.DebitAcc;
import org.example.Exceptions.ErrorMoneyException;
import org.example.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class ClientsRepository implements Repository {
    private ArrayList<Client> clients;
    private String bank;
    private IdCounter counter;

    public ClientsRepository(String bank) {
        clients = new ArrayList<>();
        this.bank = bank;
        uploadData();
        uploadCounter();
    }

    @Override
    public void uploadData() {
        String hql = "FROM Client c WHERE c.key.bank = :bankName";
        try (Session session = HibernateUtil.getSession()) {
            Query<Client> query = session.createQuery(hql, Client.class);
            query.setParameter("bankName", bank);
            this.clients = (ArrayList<Client>) query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeClient(Client client) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.delete(client);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateAcc(BankAccount account) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.update(account);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addAcc(BankAccount account, Client client) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(account);
                client.addAcc(account);
                session.update(client);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeAcc(BankAccount account) {
        Client client = account.getOwner();
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.delete(account);
                account.removeAccount();
                session.update(client);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getCount() {
        uploadData();
        return clients.size();
    }

    @Override
    public Client createClient(String name, String surname, String bank, String typeAcc) throws ErrorMoneyException {
        Client client;
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            client = new Client(name, surname, bank, generateClientId());
            try {
                session.save(client);
                BankAccount acc;
                if (typeAcc.equalsIgnoreCase("дебетовый")) {
                    acc = new DebitAcc(client, generateAccId());
                    client.addAccount(acc);
                } else if (typeAcc.equalsIgnoreCase("кредитный")) {
                    acc = new CreditAcc(client, generateAccId());
                    client.addAccount(acc);
                } else {
                    throw new ErrorMoneyException("неверный тип счёта");
                }
                session.save(acc);
                session.update(client);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
        return client;
    }

    @Override
    public Client getClient(int index) {
        for (Client cl : clients) {
            if (cl.getId() == index) {
                return cl;
            }
        }
        return null;
    }

    @Override
    public List<Client> getClients() {
        uploadData();
        return clients;
    }

    public long generateClientId() {
        long id = this.counter.getCountClient();
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            this.counter.setCountClient(++id);
            session.update(counter);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public long generateAccId() {
        long id = this.counter.getCountAcc();
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            this.counter.setCountAcc(++id);
            session.update(counter);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    private void uploadCounter() {
        String hql = "FROM IdCounter i WHERE i.bank = :bankName";
        try (Session session = HibernateUtil.getSession()) {
            Query<IdCounter> query = session.createQuery(hql, IdCounter.class);
            query.setParameter("bankName", bank);
            List<IdCounter> counters = query.getResultList();
            if (!counters.isEmpty()) {
                this.counter = counters.get(0);
            } else {
                Transaction transaction = session.beginTransaction();
                this.counter = new IdCounter(this.bank);
                session.save(counter);
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
