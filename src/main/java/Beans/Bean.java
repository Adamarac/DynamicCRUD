package Beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author João Gabriel
 */
public class Bean {

    private Bean() {
    }
    
    public static DynaBeans createBean(Connection connection, String tabela) throws SQLException, IllegalAccessException, InstantiationException{
        
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
        
        DynaBeans create = BeanFactory.newInstance(tabela,columnNames);
        
        return create;
    }
        
}
