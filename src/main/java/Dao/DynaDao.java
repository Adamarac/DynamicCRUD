package Dao;

import Beans.BeanFactory;
import Beans.DynaBeans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

/**
 *
 * @author User
 */
public class DynaDao {
    
    private final Connection connection;
    private final DynaBeans bean;

    /**
     *
     * @param connection
     * @param tabela
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public DynaDao(Connection connection, String tabela) throws SQLException, IllegalAccessException, InstantiationException {
        this.connection = connection;
        
        String sqlQuery = "SELECT * FROM " + tabela + " WHERE 1 = 0";
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlQuery);
        
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        List<String> columnNames = new ArrayList<>();
        
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            columnNames.add(columnName);
        }
        
        this.bean  = BeanFactory.newInstance(tabela,columnNames);
 
    }

    /**
     *
     * @return
     */
    public DynaBeans getDynaBean() {
        return bean;
    }
        
    /**
     *
     * @param registro
     * @throws SQLException
     */
    public void adicionar(DynaBean registro) throws SQLException{ 
        
        String sql = createInsert(registro);
        
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        
        List<String> colunas = bean.attributeToList();
        
        int index = 1;
        for(String column : colunas){
        preparedStatement.setString(index, (String) registro.get(column));
        index++;
        }
        
        int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Inserção bem-sucedida!");
            } else {
                System.out.println("A inserção falhou.");
            }
 
    }
    
    /**
     *
     * @return 
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Object[][] listar() throws SQLException, IllegalAccessException, InstantiationException{ 
        
        String tabela = bean.getName();
        String sqlQuery = "SELECT * FROM " + tabela;
        
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt.executeQuery(sqlQuery);  
             
        List<String> colunas = bean.attributeToList();
        int numColunas = colunas.size();
        
        int numLinhas = 0;
        while (rs.next()) {
            numLinhas++;
        }
        
        rs.beforeFirst();
        Object[][] matriz = new Object[numLinhas][numColunas];
          
        int linha = 0;
        
        while (rs.next()) {
            for (int coluna = 0; coluna < numColunas; coluna++) {
                 Object columnValue = rs.getObject(colunas.get(coluna));
                 matriz[linha][coluna] = columnValue;
            }
            linha++;
            }
        
        return matriz;
    }
     
    /**
     *
     * @param id
     * @throws SQLException
     */
    public void excluir(String id) throws SQLException{ 
          
        String tabela = bean.getName();    
        String pk = pk(tabela);
            
        String sql_del = "DELETE FROM " + tabela + " WHERE " + pk + " = " + id;
        
        PreparedStatement ps = connection.prepareStatement(sql_del);
        int rowsAffected = ps.executeUpdate();
        
        if (rowsAffected > 0) {
                System.out.println("Registro(s) deletado(s) com sucesso.");
            } else {
                System.out.println("Nenhum registro foi deletado.");
            }
        
    }
      
    /**
     *
     * @param atualiza
     * @param id
     * @throws SQLException
     */
    public void atualizar(DynaBean atualiza, String id) throws SQLException{ 
               
        String sql = createUpdate(atualiza);
        
        PreparedStatement ps = connection.prepareStatement(sql);
        
        List<String> colunas = bean.attributeToList();
        
        int index = 1;
        for(String column : colunas){
            ps.setString(index, (String) atualiza.get(column));
            index++;
        }
        
        
        ps.setString(index, id);
        
        int rowsAffected = ps.executeUpdate();
        
        if (rowsAffected > 0) {
                System.out.println("Registro(s) alterado(s) com sucesso.");
            } else {
                System.out.println("Nenhum registro foi alterado.");
            }
 
    }
      
    /**
     *
     * @param table
     * @return
     * @throws SQLException
     */
    public String pk(String table) throws SQLException{
            
        Statement statement = connection.createStatement();   
        String sql = "SHOW KEYS FROM " + table + " WHERE Key_name = 'PRIMARY'";
        ResultSet resultSet = statement.executeQuery(sql);
        
        String pk = null;
        
        if (resultSet.next()) {
                pk = resultSet.getString("Column_name");
                System.out.println("Nome da coluna da chave primária: " + pk);
            } else {
                System.out.println("A tabela não possui chave primária definida.");
            }
        
        return pk;
    }
    
    /**
     *
     * @param registro
     * @return
     */
    public String createInsert(DynaBean registro){
    
        String tabela = bean.getName();
        List<String> colunas = bean.attributeToList();
        
        StringBuilder string = new StringBuilder("INSERT INTO " + tabela + " (");
        string.append(String.join(", ", colunas));
        string.append(") VALUES (");

        for (int i = 0; i < colunas.size(); i++) {
            if (i > 0) {
                string.append(", ");
            }
            string.append("?");
        }

        string.append(")");
        String sql = string.toString();
    
        return sql;
    }
    
    /**
     *
     * @param atualizar
     * @return
     * @throws SQLException
     */
    public String createUpdate(DynaBean atualizar) throws SQLException{
    
        String tabela = bean.getName();
        String pk = pk(tabela);

        List<String> colunas = bean.attributeToList();
        
        StringBuilder sql = new StringBuilder("UPDATE " + tabela + " SET ");
        for (int i = 0; i < colunas.size(); i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append(colunas.get(i)).append(" = ?");
        }
        sql.append(" WHERE ").append(pk).append(" = ?");
        
        return sql.toString();
    }
}
