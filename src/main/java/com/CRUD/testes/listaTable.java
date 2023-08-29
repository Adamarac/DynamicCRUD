package com.CRUD.testes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class listaTable {
    
    public static List<String> nomesTable(Connection connection) throws SQLException {
        List<String> tableNames = new ArrayList<>();
        Statement statement = connection.createStatement();

        String query = "SHOW TABLES";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            String tableName = resultSet.getString(1);
            tableNames.add(tableName);
        }

        return tableNames;
    }
    
    public static int countColumns(Connection connection, String tableName) throws SQLException{
        Statement statement = connection.createStatement();
         
            String query = "SELECT * FROM " + tableName + " WHERE 1 = 0";
            ResultSet resultSet = statement.executeQuery(query);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            return columnCount;
    }
}
