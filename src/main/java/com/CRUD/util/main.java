package com.CRUD.util;

import com.CRUD.testes.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class main {

    public static void main(String[] args) {
                        
        try {
            Connection connection = DataBaseConnection.Connect();
            listaTable nomes = new listaTable(connection,"");

            if (connection != null) {
                System.out.println("Conexão bem-sucedida com o banco de dados!");
            } else {
                System.out.println("Falha ao conectar ao banco de dados.");
            }
            
            List<String> comboboxOptions = nomes.nomesTable();
            testeJFrame frame = new testeJFrame(connection, comboboxOptions);
            frame.setVisible(true);
            
        } catch (SQLException e) {
        }
    }
}
