package com.CRUD.testes;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class testeJFrame extends JFrame {
    
    private JComboBox<String> comboBox;
    private List<String> comboOptions;
    
    public testeJFrame(List<String> options) {
        setTitle("Meu JFrame");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         comboOptions = options;
        comboBox = new JComboBox<>(comboOptions.toArray(new String[0]));

        getContentPane().add(comboBox, BorderLayout.NORTH);
        
    }
}
