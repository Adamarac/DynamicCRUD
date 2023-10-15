package Dao;

import Beans.BeanFactory;
import Beans.DynaBeans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private final DynaBeans table;

    /**
     *
     * @param connection
     * @param tabela
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public DynaDao(Connection connection, DynaBeans tabela) throws SQLException, IllegalAccessException, InstantiationException {
        this.connection = connection;
        this.table = tabela;          
    }

    /**
     *
     * @return
     */
    public DynaBeans getDynaBean() {
        return table;
    }
        
    /**
     *
     * @param registro
     * @throws SQLException
     */
    public String adicionar(DynaBean registro) throws SQLException{ 
         
        String sql = createInsert(registro);
        
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        
        List<String> colunas = table.attributeToList();
        
        int index = 1;
        for(String column : colunas){
        preparedStatement.setString(index, (String) registro.get(column));
        index++;
        }
        
        int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                return "Inserção bem-sucedida!";
            } else {
                return "A inserção falhou.";
            }
 
    }
    
    /**
     *
     * @return 
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public List<DynaBean>  listar() throws SQLException, IllegalAccessException, InstantiationException{ 
        
        String tabela = table.getName();
        String sqlQuery = "SELECT * FROM " + tabela;
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlQuery);  
             
        List<String> colunas = table.attributeToList();
        List<DynaBean> registros = new ArrayList<>();
          
        
        while (rs.next()) {
            DynaBeans reg; 
            reg = BeanFactory.newInstance(tabela,table.attributeToList());
            for (String coluna : colunas) {
                 Object columnValue = rs.getObject(coluna);
                 reg.getBean().set(coluna, columnValue);                
            }
            registros.add(reg.getBean());
            }
        
        return registros;
    }
     
    /**
     *
     * @param id
     * @throws SQLException
     */
    public String excluir(Object id, String pk) throws SQLException{ 
    String tabela = table.getName();    
    String sql_del = "DELETE FROM " + tabela + " WHERE " + pk + " = ?";
    
    try {
        PreparedStatement ps = connection.prepareStatement(sql_del);
        ps.setObject(1, id);
        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            return "Registro(s) deletado(s) com sucesso.";
        } else {
            return "Nenhum registro foi deletado.";
        }
    } catch (SQLException e) {
        if (e.getSQLState().startsWith("23")) {
            return "Erro: Esta operação viola uma chave estrangeira.";
        } else {
            
            throw e; 
        }
    }
}

      
    /**
     *
     * @param atualiza
     * @param id
     * @throws SQLException
     */
    public String atualizar(DynaBean atualiza, Object id) throws SQLException{ 
               
        String sql = createUpdate(atualiza);
        
        PreparedStatement ps = connection.prepareStatement(sql);
        
        List<String> colunas = table.attributeToList();
        
        int index = 1;
        for(String column : colunas){
            ps.setObject(index, atualiza.get(column));
            index++;
        }
        
        
        ps.setObject(index, id);
        
        int rowsAffected = ps.executeUpdate();
        
        if (rowsAffected > 0) {
               return "Registro(s) alterado(s) com sucesso.";
            } else {
               return "Nenhum registro foi alterado.";
            }
 
    }
      
    /**
     *
     * @return
     * @throws SQLException
     */
    public String pk() throws SQLException{
            
        Statement statement = connection.createStatement();   
        String sql = "SHOW KEYS FROM " + table.getName() + " WHERE Key_name = 'PRIMARY'";
        ResultSet resultSet = statement.executeQuery(sql);
        
        String pk = null;
        
        if (resultSet.next()) {
                pk = resultSet.getString("Column_name");
              
            } 
        
        return pk;
    }
    
    /**
     *
     * @param registro
     * @return
     */
    public String createInsert(DynaBean registro){
    
        String tabela = table.getName();
        List<String> colunas = table.attributeToList();
        
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
    
        String tabela = table.getName();
        String pk = pk();

        List<String> colunas = table.attributeToList();
        
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
