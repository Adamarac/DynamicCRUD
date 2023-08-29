package com.CRUD.testes;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class testeJFrame extends JFrame {
    
    private JComboBox<String> comboBox;
    private List<String> comboOptions;
    
    public testeJFrame(List<String> options, Connection connection) {
        setTitle("Meu JFrame");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         comboOptions = options;
        comboBox = new JComboBox<>(comboOptions.toArray(new String[0]));
        
        getContentPane().add(comboBox, BorderLayout.NORTH);
        
        JButton button = new JButton("Click Me!");
        button.addActionListener(e -> {
            String selectedOption = (String) comboBox.getSelectedItem();
            testeTable table;
            try {
                table = new testeTable(listaTable.countColumns(connection, selectedOption));
                table.setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(testeJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
         getContentPane().add(button, BorderLayout.SOUTH);
    }
}
