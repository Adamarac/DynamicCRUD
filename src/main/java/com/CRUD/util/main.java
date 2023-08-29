package com.CRUD.util;

import com.CRUD.testes.tableMetadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class main {

    public static void main(String[] args) {
                        
        try {
            Connection connection = DataBaseConnection.Connect();

            if (connection != null) {
                System.out.println("Conexão bem-sucedida com o banco de dados!");
            } else {
                System.out.println("Falha ao conectar ao banco de dados.");
            }
            
            
            String ler = JOptionPane.showInputDialog("Digite a tabela que deseja consultar");
            
            tableMetadata.Metadata(ler, connection);

            
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
