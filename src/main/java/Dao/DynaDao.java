package Dao;

import Beans.BeanFactory;
import Beans.DynaBeans;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

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
    public String excluir(Object id, DynaBean bean) throws SQLException{ 
    String tabela = table.getName();    
    String pk = pk();
    
    
    if(pk != null){
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
    }}else{
    
        String sql_del = "DELETE FROM " + tabela;
        DynaProperty[] properties = bean.getDynaClass().getDynaProperties();
        boolean first = true;

        for (DynaProperty property : properties) {
        String propertyName = property.getName();
        Object propertyValue = bean.get(propertyName);

   
        if (propertyValue != null) {
            if (first) {
                sql_del += " WHERE " + propertyName + " = ?";
                first = false;
            } else {
                sql_del += " AND " + propertyName + " = ?";
            }
        }
}

    if (first) {

        throw new IllegalStateException("Nenhuma condição definida para a exclusão.");
    }

    try {
        PreparedStatement ps = connection.prepareStatement(sql_del);
        
        int parameterIndex = 1;
        for (DynaProperty property : properties) {
            String propertyName = property.getName();
            Object propertyValue = bean.get(propertyName);

                if (propertyValue != null) {
                    ps.setObject(parameterIndex, propertyValue);
                    parameterIndex++;
                }
}
        
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
    }catch (IllegalStateException e) {
     return e.toString();
    
    }
    
    }
       
    }

      
    /**
     *
     * @param old
     * @param atualiza
     * @param id
     * @return 
     * @throws SQLException
     */
    public String atualizar(List<String> old, DynaBean atualiza, Object id) throws SQLException{ 
               
        PreparedStatement ps = createUpdate(atualiza, old);
               
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
     * @param values
     * @return
     * @throws SQLException
     */
    public PreparedStatement createUpdate(DynaBean atualizar, List<String> values) throws SQLException {
    String tabela = table.getName();
    String pk = pk();
    PreparedStatement ps = null;

    List<String> colunas = table.attributeToList();
    
    StringBuilder sql = new StringBuilder("UPDATE " + tabela + " SET ");
    DynaProperty[] properties = atualizar.getDynaClass().getDynaProperties();
    List<String> attributeNames = new ArrayList<>();

    for (DynaProperty property : properties) {
        attributeNames.add(property.getName());
    }

    for (int i = 1; i <= colunas.size(); i++) {
        if (i > 1) {
            sql.append(", ");
        }
        sql.append(colunas.get(i - 1)).append(" = ?");
    }
   
    if (pk == null) {
        sql.append(" WHERE ");
        for (int i = 1; i <= colunas.size(); i++) {
            if (i > 1) {
                sql.append(" AND ");
            }
            sql.append(colunas.get(i - 1)).append(" = ?");
        }
        
        ps = connection.prepareStatement(sql.toString());
        
        int i = 1;
        while (i <= colunas.size()) {
             ps.setObject(i, atualizar.get(attributeNames.get(i - 1)));     
             i++;
        }
        
        int j = i;
        while (i < j + colunas.size()) {
             ps.setObject(i, values.get(i - j));
             i++;
        }    
    }
    else {
        sql.append(" WHERE ").append(pk).append(" = ?");
        
        ps = connection.prepareStatement(sql.toString());
        
        for (int i = 1; i <= colunas.size(); i++) {
             ps.setObject(i, atualizar.get(attributeNames.get(i - 1)));     
        }
        ps.setObject(colunas.size() + 1, atualizar.get(pk));
    }
    
    return ps;
}

    
    
    public List<String> getAutoIncrementColumns() {
        List<String> autoIncrementColumns = new ArrayList<>();
        
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, table.getName(), null);
            
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
                if (isAutoIncrement != null && isAutoIncrement.equalsIgnoreCase("YES")) {
                    autoIncrementColumns.add(columnName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return autoIncrementColumns;
    }
    
    
}
