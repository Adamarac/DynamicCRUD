package com.CRUD.testes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

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
    
    public void deleteRecord(String id, String nameColumnId) throws SQLException {

            String sql = "DELETE FROM " + tableName + " WHERE " + nameColumnId + " =  ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            statement.executeUpdate();
    }
    
    public static void generateSQL(String[] fieldNames, JTextField[] textFields) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");

        for (int i = 0; i < fieldNames.length; i++) {
            sql.append(fieldNames[i]);
            if (i < fieldNames.length - 1) {
                sql.append(", ");
            }
        }

        sql.append(") VALUES (");

        for (int i = 0; i < textFields.length; i++) {
            String value = textFields[i].getText();
            sql.append("'").append(value).append("'");
            if (i < textFields.length - 1) {
                sql.append(", ");
            }
        }

        sql.append(");");
        connection.createStatement().executeUpdate(sql.toString());
    }
    public static void updateSQL(String[] fieldNames, JTextField[] textFields, String nameColumnId) throws SQLException {
    StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");

    for (int i = 0; i < fieldNames.length; i++) {
        String fieldName = fieldNames[i];
        String value = textFields[i].getText();
        
        sql.append(fieldName).append(" = ");
        
        if (isNumeric(value)) {
            sql.append(value);
        } else {
            sql.append("'").append(value).append("'");
        }

        if (i < fieldNames.length - 1) {
            sql.append(", ");
        }
    }
    
    sql.append(" WHERE " + nameColumnId + " = ").append(textFields[0].getText());

    connection.createStatement().executeUpdate(sql.toString());
}

public static boolean isNumeric(String str) {
    return str.matches("-?\\d+(\\.\\d+)?");
}

    
}
