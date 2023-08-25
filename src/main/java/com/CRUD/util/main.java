package com.CRUD.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
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
            
            DatabaseMetaData metaData = connection.getMetaData();
            
            String ler = JOptionPane.showInputDialog("Digite a tabela que deseja consultar");

            String tableName = ler;
            ResultSet columns = metaData.getColumns(null, null, tableName, null);

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                String isNullable = columns.getString("IS_NULLABLE");
                boolean isAutoIncrement = columns.getBoolean("IS_AUTOINCREMENT");

                System.out.println("Nome da Coluna: " + columnName);
                System.out.println("Tipo de Dado: " + dataType);
                System.out.println("Tamanho: " + columnSize);
                System.out.println("É Nulo: " + isNullable);
                System.out.println("Auto Incremento: " + isAutoIncrement);
                System.out.println("---------------------");
            }
            
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
