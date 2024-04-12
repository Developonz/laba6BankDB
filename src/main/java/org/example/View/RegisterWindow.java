package org.example.View;

import org.example.Data.ClientsRepository;
import org.example.Exceptions.ErrorMoneyException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RegisterWindow extends Window{
    private JPanel panel;
    private JPanel gridBagPan;
    private JLabel name;
    private JLabel surname;
    private JLabel typeAcc;
    private JComboBox<String> typesAccs;
    private JTextField nameField;
    private JTextField surnameField;
    private JButton register;
    private JButton exitButton;

    public RegisterWindow(String bank) {
        setTitle("Регистрация");

        panel = new JPanel();
        name = new JLabel("Имя:");
        surname = new JLabel("Фамилия:");
        typeAcc = new JLabel("Тип счёта:");
        String[] options = {"дебетовый", "кредитный"};
        typesAccs = new JComboBox<>(options);
        nameField = new JTextField(10);
        surnameField = new JTextField(10);
        register = new JButton("Подтвердить");
        exitButton = new JButton("Назад");
        exitButton.setPreferredSize(register.getPreferredSize());

        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new ProfileWindow(new ClientsRepository(bank).createClient(nameField.getText(), surnameField.getText(), bank, (String) typesAccs.getSelectedItem()));
                } catch (ErrorMoneyException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EntryToBankWindow(bank);
                dispose();
            }
        });

        gridBagPan = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gridBagPan.add(surname, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gridBagPan.add(name, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gridBagPan.add(typeAcc, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gridBagPan.add(surnameField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gridBagPan.add(nameField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gridBagPan.add(typesAccs, gbc);

        panel.add(register);
        panel.add(exitButton);

        add(gridBagPan);
        add(panel, BorderLayout.SOUTH);

        setLocation(630, 300);
        setSize(255, 235);
        setVisible(true);
    }
}
