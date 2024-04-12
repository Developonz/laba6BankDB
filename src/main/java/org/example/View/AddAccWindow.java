package org.example.View;

import org.example.Data.BankAccs.CreditAcc;
import org.example.Data.BankAccs.DebitAcc;
import org.example.Data.BankAccs.DepositAcc;
import org.example.Data.Client;
import org.example.Data.ClientsRepository;
import org.example.HibernateUtil;
import org.hibernate.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddAccWindow extends Window {
    private JButton debit;
    private JButton credit;
    private JButton deposit;
    private JButton exitButton;
    private JPanel panel;

    public AddAccWindow(Client client) {
        setTitle("Создание счёта");

        debit = new JButton("Дебетовый");
        debit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientsRepository repo = new ClientsRepository(client.getBank());
                repo.addAcc(new DebitAcc(client, repo.generateAccId()), client);
                JOptionPane.showMessageDialog(getContentPane(), "Успешно добавлено", "Уведомление", JOptionPane.INFORMATION_MESSAGE);
                new ProfileWindow(client);
                dispose();
            }
        });

        credit = new JButton("Кредитный");
        credit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientsRepository repo = new ClientsRepository(client.getBank());
                repo.addAcc(new CreditAcc(client, repo.generateAccId()), client);
                JOptionPane.showMessageDialog(getContentPane(), "Успешно добавлено", "Уведомление", JOptionPane.INFORMATION_MESSAGE);
                new ProfileWindow(client);
                dispose();
            }
        });

        deposit = new JButton("Сберегательный");
        deposit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientsRepository repo = new ClientsRepository(client.getBank());
                repo.addAcc(new DepositAcc(client, repo.generateAccId()), client);
                JOptionPane.showMessageDialog(getContentPane(), "Успешно добавлено", "Уведомление", JOptionPane.INFORMATION_MESSAGE);
                new ProfileWindow(client);
                dispose();
            }
        });

        exitButton = new JButton("Назад");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProfileWindow(client);
                dispose();
            }
        });

        debit.setPreferredSize(deposit.getPreferredSize());
        credit.setPreferredSize(deposit.getPreferredSize());

        panel = new JPanel();
        panel.add(debit);
        panel.add(credit);
        panel.add(deposit);

        add(panel);
        add(exitButton, BorderLayout.SOUTH);
        pack();
        setLocation(630, 300);
        setVisible(true);
    }
}
