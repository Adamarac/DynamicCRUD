package com.CRUD.testes;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class testeTable extends JFrame {
    private Connection connection;

    public testeTable(int numColumns, listaTable dados) throws SQLException {

        setTitle("Dados da tabela");
        
        String[] names = dados.nameColumns();
        String[][] registros = dados.registrosTable();
        
        DefaultTableModel tableModel = new DefaultTableModel(registros,names);


        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
    }

}
