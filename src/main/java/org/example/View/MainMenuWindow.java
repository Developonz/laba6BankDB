package org.example.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuWindow extends Window {
    private JButton alfaBut;
    private JButton tinkBut;
    private JButton sberBut;
    private JButton exitButton;
    private JPanel panel;

    public MainMenuWindow() {
        setTitle("Банк");

        alfaBut = new JButton("alfa");
        alfaBut.addActionListener(new MyListener(alfaBut.getText()));

        tinkBut = new JButton("tinkoff");
        tinkBut.addActionListener(new MyListener(tinkBut.getText()));

        sberBut = new JButton("sber");
        sberBut.addActionListener(new MyListener(sberBut.getText()));

        exitButton = new JButton("Выход");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Действительно ли вы хотите выйти из приложения?",
                        "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (confirmed == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        alfaBut.setPreferredSize(tinkBut.getPreferredSize());
        sberBut.setPreferredSize(tinkBut.getPreferredSize());

        panel = new JPanel();
        panel.add(alfaBut);
        panel.add(tinkBut);
        panel.add(sberBut);

        add(panel);
        add(exitButton, BorderLayout.SOUTH);
        setSize(300, 100);
        setLocation(630, 300);
        setVisible(true);
    }

    public class MyListener implements ActionListener {
        private String str;
        public MyListener(String str) {
            this.str = str;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new EntryToBankWindow(this.str);
            dispose();
        }
    }
}
