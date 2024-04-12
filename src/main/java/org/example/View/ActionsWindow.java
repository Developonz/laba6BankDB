package org.example.View;

import org.example.Data.BankAccs.BankAccount;
import org.example.Data.Client;
import org.example.Data.ClientsRepository;
import org.example.Exceptions.ErrorMoneyException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionsWindow extends Window {
    private JButton moneyTransfer;
    private JButton moneyDismount;
    private JButton moneyReplenish;
    private JButton accRename;
    private JButton accRemove;
    private JButton exitButton;
    private JPanel mainPanel;
    private JLabel label;

    public ActionsWindow(BankAccount acc) {
        setTitle("��������");

        moneyTransfer = new JButton("���������");
        moneyDismount = new JButton("�����");
        moneyReplenish = new JButton("���������");
        accRename = new JButton("������������� ����");
        accRemove = new JButton("������� ����");
        exitButton = new JButton("�����");
        label = new JLabel(acc.getTitle() + ": " + acc.getMoney() + " ���");

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProfileWindow(acc.getOwner());
                dispose();
            }
        });

        moneyDismount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = JOptionPane.showInputDialog(null, "������� ��������:", "����", JOptionPane.PLAIN_MESSAGE);
                double money;
                if (result != null && !result.isEmpty()) {
                    try {
                        try {
                            money = Double.parseDouble(result);
                            acc.topDownAcc(money);
                            ClientsRepository repo = new ClientsRepository(acc.getBank());
                            repo.updateAcc(acc);
                            label.setText(acc.getTitle() + ": " + acc.getMoney() + " ���");
                            JOptionPane.showMessageDialog(getContentPane(), "����� ������� �����", "�����������", JOptionPane.INFORMATION_MESSAGE);
                        } catch (ErrorMoneyException ex) {
                            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage(), "������", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(getContentPane(), "������������ ���� �����", "������", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        moneyReplenish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = JOptionPane.showInputDialog(null, "������� ��������:", "����", JOptionPane.PLAIN_MESSAGE);
                if (result != null && !result.isEmpty()) {
                    try {
                        double money = Double.parseDouble(result);
                        acc.topUpAcc(money);
                        ClientsRepository repo = new ClientsRepository(acc.getBank());
                        repo.updateAcc(acc);
                        label.setText(acc.getTitle() + ": " + acc.getMoney() + " ���");
                        JOptionPane.showMessageDialog(getContentPane(), "���� ������� ��������", "�����������", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(getContentPane(), "������������ ���� �����", "������", JOptionPane.ERROR_MESSAGE);
                    } catch (ErrorMoneyException ex) {
                        JOptionPane.showMessageDialog(getContentPane(), ex.getMessage(), "������", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        accRename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = JOptionPane.showInputDialog(null, "������� ��������:", "����", JOptionPane.PLAIN_MESSAGE);
                if (result != null && !result.isEmpty()) {
                    try {
                        acc.renameTitle(result);
                        BankAccount newAcc = acc;
                        ClientsRepository repo = new ClientsRepository(acc.getBank());
                        repo.updateAcc(acc);
                        label.setText(acc.getTitle() + ": " + acc.getMoney() + " ���");
                        JOptionPane.showMessageDialog(getContentPane(), "���� ������� ������������", "�����������", JOptionPane.INFORMATION_MESSAGE);
                    } catch (ErrorMoneyException ex) {
                        JOptionPane.showMessageDialog(getContentPane(), ex.getMessage(), "������", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        accRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientsRepository repo = new ClientsRepository(acc.getBank());
                if (acc.getOwner().getAccs().size() == 1) {
                    repo.removeClient(acc.getOwner());
                    new EntryToBankWindow(acc.getBank());
                    dispose();
                } else {
                    Client client = acc.getOwner();
                    repo.removeAcc(acc);
                    new ProfileWindow(client);
                    dispose();
                }
            }
        });

        moneyTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TransferMoneyWindow(acc);
                dispose();
            }
        });

        mainPanel = new JPanel(new GridLayout(0, 1));
        mainPanel.add(moneyTransfer);
        mainPanel.add(moneyDismount);
        mainPanel.add(moneyReplenish);
        mainPanel.add(accRename);
        mainPanel.add(accRemove);


        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);
        add(mainPanel);
        add(exitButton, BorderLayout.SOUTH);

        setSize(250, 300);
        setLocation(660, 300);
        setVisible(true);

    }
}
