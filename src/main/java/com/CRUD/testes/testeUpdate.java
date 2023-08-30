package com.CRUD.testes;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class testeUpdate extends JFrame{
    
    private String[] fieldNames;
    private JTextField[] textFields;

    public testeUpdate(Connection connection, String[] infoNames, String[] values) {
        this.fieldNames = infoNames;
        this.textFields = new JTextField[fieldNames.length];

        setTitle("Atualizar");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        for (int i = 0; i < fieldNames.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JLabel label = new JLabel(fieldNames[i] + ":");
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.gridy = i;
            JTextField textField = new JTextField(20);
            textField.setText(values[i]);
            textFields[i] = textField;
            panel.add(textField, gbc);
        }

        JButton submitButton = new JButton("Atualizar");
        gbc.gridx = 0;
        gbc.gridy = fieldNames.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(submitButton, gbc);
        
        submitButton.addActionListener(e -> {
            try { 
                String nameColumnId = fieldNames[0];
                listaTable.updateSQL(fieldNames, textFields, nameColumnId);
            } catch (SQLException ex) {
                Logger.getLogger(testeForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 

        getContentPane().add(panel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }
}
