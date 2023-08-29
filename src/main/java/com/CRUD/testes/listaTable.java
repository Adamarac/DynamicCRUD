package com.CRUD.testes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class listaTable {
    
    private static Connection connection;
    private static String tableName; 

    public listaTable(Connection c, String tn) {
        connection = c;
        tableName = tn;
    }
    
    
    
    public List<String> nomesTable() throws SQLException {
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
    
    public ResultSetMetaData countColumns() throws SQLException{
        Statement statement = connection.createStatement();
         
            String query = "SELECT * FROM " + tableName + " WHERE 1 = 0";
            ResultSet resultSet = statement.executeQuery(query);

            ResultSetMetaData metaData = resultSet.getMetaData();
            
            
            return metaData;
    }
    
    public String[] nameColumns() throws SQLException{
        
    ResultSetMetaData metaData = countColumns();
    int columnCount = metaData.getColumnCount();
    String[] columnNames = new String[columnCount];
    

            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            return columnNames;
    
    
    }
    
    public String[][] registrosTable() throws SQLException{
    
    Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            int rowCount = 0;
            int columnCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                rowCount++;
            }

            String[][] matrix = new String[rowCount][columnCount];

            resultSet.beforeFirst();

            int row = 0;
            while (resultSet.next()) {
                for (int col = 1; col <= columnCount; col++) {
                    matrix[row][col - 1] = resultSet.getString(col);
                }
                row++;
            }
    
            return matrix;
    }
    
}
