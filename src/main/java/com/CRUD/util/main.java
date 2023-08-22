package com.CRUD.util;

import java.sql.Connection;
import java.sql.SQLException;

public class main {

    public static void main(String[] args) {
        try {
            Connection connection = DataBaseConnection.Connect();

            if (connection != null) {
                System.out.println("Conexão bem-sucedida com o banco de dados!");
                connection.close();
            } else {
                System.out.println("Falha ao conectar ao banco de dados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
