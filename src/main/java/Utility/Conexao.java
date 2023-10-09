package Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author User
 */
public class Conexao {
    
    private String bd = null;
    private final String URL;
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    /**
     *
     */
    public Conexao() { 
        this.URL = "jdbc:mysql://localhost:3306/";
    }
    
    /**
     *
     * @param base
     */
    public Conexao(String base) { 
        this.bd = base;
        this.URL = "jdbc:mysql://localhost:3306/" + bd;
    }
      
    /**
     *
     * @return
     */
    public Connection obterConexao() {
        try {
            Connection conexao = null;
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            return conexao;
        } catch (ClassNotFoundException e) {
            System.err.println("Erro ao carregar o driver do MySQL: " + e.getMessage());
            throw new RuntimeException("Erro ao carregar o driver do MySQL.", e);
        } catch (SQLException e) {
            System.err.println("Erro ao obter a conexão com o MySQL: " + e.getMessage());
            throw new RuntimeException("Erro ao obter a conexão com o MySQL.", e);
        }
    }
    
    
    
    
    
}

