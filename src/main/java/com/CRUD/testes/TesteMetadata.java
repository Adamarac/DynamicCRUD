package com.CRUD.testes;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class TesteMetadata {
    
        public static void Metadata(String ler, Connection connection) throws SQLException{
            
            DatabaseMetaData metaData = connection.getMetaData();
    
            ResultSet columns = metaData.getColumns(null, null, ler, null);

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
    
}
        
}
