package com.CRUD.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/db_cinema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    public static Connection Connect() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    
}
