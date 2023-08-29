package com.CRUD.testes;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class testeTable extends JFrame {
    private int numColumns; // Número de colunas na tabela

    public testeTable(int numColumns) {
        this.numColumns = numColumns;

        setTitle("Table Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        DefaultTableModel tableModel = new DefaultTableModel();

        // Adiciona colunas ao modelo de tabela
        for (int i = 1; i <= numColumns; i++) {
            tableModel.addColumn("Column " + i);
        }

        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        pack(); // Ajusta o tamanho do JFrame automaticamente
        setLocationRelativeTo(null); // Centraliza o JFrame na tela
    }

    public static void main(String[] args) {
        int numColumns = 5; // Número de colunas desejado
        SwingUtilities.invokeLater(() -> new testeTable(numColumns).setVisible(true));
    }
}
