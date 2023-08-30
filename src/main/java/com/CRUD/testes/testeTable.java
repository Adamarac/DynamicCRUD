package com.CRUD.testes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class testeTable extends JFrame {
    private Connection connection;

    public testeTable(int numColumns, listaTable dados) throws SQLException {
        setTitle("Dados da tabela");

        String[] names = dados.nameColumns();
        String[][] registros = dados.registrosTable();

        DefaultTableModel tableModel = new DefaultTableModel(registros, names) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);

        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Alinhamento à direita
        
        JButton button1 = new JButton("Deletar");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedId = (String) table.getValueAt(selectedRow, 0);
                    String firstColumnName = table.getColumnName(0);
                    try {
                        dados.deleteRecord(selectedId, firstColumnName);
                    } catch (SQLException ex) {
                        Logger.getLogger(testeTable.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione uma linha a ser removida.");
                }
            }
        });
        buttonPanel.add(button1);

        JButton button2 = new JButton("Adicionar");
        buttonPanel.add(button2);
        
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testeForm form = new testeForm(connection, names);
                form.setVisible(true);
            
            }
        });
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        JButton button3 = new JButton("Atualizar");
        buttonPanel.add(button3);
        
        button3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String[] selectedValues = new String[names.length];
                        for (int i = 0; i < names.length; i++) {
                            selectedValues[i] = table.getValueAt(selectedRow, i).toString();
                        }
                        testeUpdate formUp = new testeUpdate(connection, names, selectedValues);
                        formUp.setVisible(true);
                    }
                }
            });
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
    }
}
