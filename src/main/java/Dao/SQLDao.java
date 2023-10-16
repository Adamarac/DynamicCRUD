package Dao;

import Utility.Conexao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class SQLDao {
     
    public static List<String> listarBases() throws SQLException{
    
            Conexao conn = new Conexao();
            Connection connect = conn.obterConexao();
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery("SHOW DATABASES WHERE `Database` NOT IN ('mysql', 'information_schema', 'performance_schema', 'sys','phpmyadmin')");
            List<String> bases = new ArrayList<>();
            
            while (resultSet.next()) {
                String nomeDoBanco = resultSet.getString(1);
                bases.add(nomeDoBanco);
            }   
     return bases;       
    }
    
    
    public List<String> tabelas(String base) throws SQLException{
    
        List<String> tabelas = new ArrayList<>();
        
        Conexao conn = new Conexao(base);
        Connection connect = conn.obterConexao();
        
        Statement statement = connect.createStatement();
        String sql = "SHOW TABLES";
        ResultSet resultSet = statement.executeQuery(sql);
        
        while (resultSet.next()) {
                String nomeTabela = resultSet.getString(1);
                tabelas.add(nomeTabela);
            }
    
    return tabelas;
    }


public String createDatabase(String name) throws SQLException {
    Conexao conn = new Conexao();
    Connection connect = null;
    Statement statement = null;

       
    try {
        connect = conn.obterConexao();
        statement = connect.createStatement();
        
        if (databaseExists(connect, name)) {
            return "Erro na criação do banco de dados: A base de dados já existe.";
        }
        
        String sql = "CREATE DATABASE " + name;
        statement.executeUpdate(sql);
        return "Banco de dados criado com sucesso.";
    } catch (SQLException e) {
        String errorMessage;        
        if (e.getMessage().contains("right syntax")) {
            errorMessage = "Erro de sintaxe SQL: Verifique o nome da sua tabela.";
        } else {
            errorMessage = "Erro desconhecido ao criar o banco de dados.";
        }
        
        return errorMessage;
    }
}

    
    public String dropDatabase(String database){
    Conexao conn = new Conexao();
    Connection connect = null;
    Statement statement = null;
        
        
        try {

                connect = conn.obterConexao();
                statement = connect.createStatement();

                String sql = "DROP DATABASE " + database;
                statement.executeUpdate(sql);

                return "Banco de dados '" + database + "' excluído com sucesso.";
                
            } catch (SQLException e) {
                
                e.printStackTrace();
                
            return "Erro ao excluir o banco de dados '" + database + "': " + e.getMessage();
            
            } finally {
            
                if (connect != null) {
                    try {
                        connect.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
    
    }


    public String createTable(String base, String name, String content) throws SQLException{
    
        Conexao conn = new Conexao(base);
        Connection connect = conn.obterConexao();
        Statement statement = connect.createStatement();
 
        String text = "CREATE TABLE " + name + " ( " + content + " );";
        String sql = text.split(";")[0] + ";";
        
        try{
        statement.executeUpdate(sql);       
        return "Tabela criada com sucesso!";
        
        } catch (SQLException e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return "Erro de restrição: Integridade de query de criação de tabela ";
        } else if (e instanceof SQLSyntaxErrorException) {
            return "Erro SQL: A query de criação inserida apresenta erros de sintaxe";
        } else if (e instanceof SQLNonTransientConnectionException) {
            return "Erro de conexão não transitória: Houve algum problemação com a sua conexão com a base de dados";
        } else {
            return "Erro SQL não especificado: Houve uma exceção sem solução identificada";
        }
    
    
    }
    }
    
    public String dropTable(String base, String name) throws SQLException{
    
        Conexao conn = new Conexao(base);
        Connection connect = conn.obterConexao();
        Statement statement = connect.createStatement();
 
        String sql = "DROP TABLE " + name;
        
            try{
                
                statement.executeUpdate(sql);       
                return "Tabela deletada com sucesso!";

            } catch (SQLException e) {
                
                e.printStackTrace();
                return "Erro ao excluir a tabela '" + name + "': " + e.getMessage();
                
            } finally {
                
                if (connect != null) {
                    
                    try {
                        connect.close();
                        
                    } catch (SQLException e) {
                        
                        e.printStackTrace();
                        
                    }
                }
            }
    }

    private boolean databaseExists(Connection connect, String dbName) throws SQLException {

    ResultSet resultSet = connect.getMetaData().getCatalogs();
    while (resultSet.next()) {
        String databaseName = resultSet.getString(1);
        if (databaseName.equalsIgnoreCase(dbName)) {
            resultSet.close();
            return true;
        }
    }
    resultSet.close();
    return false;
    
    }

    
}
