package Dao;

import Utility.Conexao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class SQLDao {
     
    public static List<String> listar() throws SQLException{
    
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
    
    
}
