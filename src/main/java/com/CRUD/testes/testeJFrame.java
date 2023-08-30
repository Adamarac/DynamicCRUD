package com.CRUD.testes;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class testeJFrame extends JFrame {
       
    public  testeJFrame(Connection connection, List<String> options) {
  
        setTitle("Tabelas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JComboBox<String> comboBox = new JComboBox<>(options.toArray(String[]::new));
        getContentPane().add(comboBox, BorderLayout.NORTH);
        
        JButton button = new JButton("Selecionar");
        getContentPane().add(button, BorderLayout.SOUTH);
        
        button.addActionListener(e -> {
            String selectedOption = (String) comboBox.getSelectedItem();
            
            listaTable dados = new listaTable(connection,selectedOption);
            testeTable table;
            
            try {
                ResultSetMetaData metaData = dados.countColumns(); 
                int columnCount = metaData.getColumnCount();
                dados.nameColumns();
                table = new testeTable(columnCount, dados);
                table.setVisible(true);
            }    
            catch (SQLException ex) {
                Logger.getLogger(testeJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }); 
         
                
    }
}
