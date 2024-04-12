package org.example.View;

import org.example.Data.BankAccs.BankAccount;
import org.example.Data.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChoseAccWindow  extends Window {
    private JLabel label;
    private JPanel panel;
    private JButton exitButton;
    private List<BankAccount> list;
    private JButton[] buttons;

    public ChoseAccWindow(Client client) {
        setTitle("Счета");
        label = new JLabel("Выберите счёт");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        panel = new JPanel(new GridLayout(0, 1));
        exitButton = new JButton("Назад");

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProfileWindow(client);
                dispose();
            }
        });

        list = client.getAccs();
        buttons = new JButton[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            buttons[i] = new JButton(list.get(i).getTitle());
            panel.add(buttons[i]);
            BankAccount acc = list.get(i);
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new ActionsWindow(acc);
                    dispose();
                }
            });
        }

        add(label, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(panel);
        add(scroll);
        add(exitButton, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(250, 70));
        setMaximumSize(new Dimension(250, 350));
        pack();
        setLocation(630, 300);
        setVisible(true);
    }
}
