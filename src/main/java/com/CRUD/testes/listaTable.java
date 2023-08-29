package com.CRUD.testes;

import java.sql.Connection;
import java.sql.ResultSet;
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
}
