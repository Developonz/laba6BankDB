package org.example.View;

import org.example.Data.BankAccs.BankAccount;
import org.example.Data.Client;
import org.example.Data.MyTableModel;
import org.example.Data.ClientsRepository;
import org.example.Exceptions.ErrorMoneyException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransferMoneyWindow extends Window {
    private ClientsRepository[] repo;
    private JTable table;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JLabel label;
    private JButton exitButton;
    private JButton confirm;

    public TransferMoneyWindow(BankAccount acc) {
        setTitle("Перевод");
        repo = new ClientsRepository[] {new ClientsRepository("alfa"), new ClientsRepository("tinkoff"), new ClientsRepository("sber")};
        table = new JTable(new MyTableModel(repo, acc));
        panel = new JPanel();
        scrollPane = new JScrollPane(table);
        label = new JLabel(acc.getOwner().getData() + " - " + acc.getTitle() + ": " + acc.getMoney() + " руб");
        exitButton = new JButton("Назад");
        confirm  = new JButton("Выбрать");
        panel.add(confirm);
        panel.add(exitButton);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ActionsWindow(acc);
                dispose();
            }
        });

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    long id = (long) table.getValueAt(selectedRow, 1);
                    Client client = new ClientsRepository((String) table.getValueAt(selectedRow, 0)).getClient((int) id);
                    if (acc.getBank().equals(client.getBank()) && acc.getOwner().getId() == client.getId()) {
                        client = acc.getOwner();
                    }
                    BankAccount account = client.getAccInTitle((String) table.getValueAt(selectedRow, 4));
                    if (account != null) {
                        String result = JOptionPane.showInputDialog(getContentPane(), "Введите значение:", "Ввод", JOptionPane.PLAIN_MESSAGE);
                        double money;
                        try {
                            money = Double.parseDouble(result);
                            acc.transactMoney(account, money);
                            new ClientsRepository(acc.getBank()).updateAcc(acc);
                            new ClientsRepository(account.getBank()).updateAcc(account);
                            new TransferMoneyWindow(acc).setLocation(getLocation());
                            dispose();
                            JOptionPane.showMessageDialog(getContentPane(), "Перевод прошёл успешно", "Уведомление", JOptionPane.INFORMATION_MESSAGE);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(getContentPane(), "Неверный ввод", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        } catch (ErrorMoneyException ex) {
                            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(getContentPane(), "Ошибочка вышла", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(getContentPane(), "Ничего не выбрано", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        exitButton.setPreferredSize(confirm.getPreferredSize());

        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);
        add(scrollPane);
        add(panel, BorderLayout.SOUTH);
        setLocation(630, 300);
        setSize(600, 350);
        setVisible(true);
    }
}
