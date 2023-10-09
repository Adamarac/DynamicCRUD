package Beans;

import java.util.List;
import org.apache.commons.beanutils.DynaProperty;

/**
 *
 * @author User
 */
public class DynaBeanAttribute {
    
    private DynaProperty[] properties;

    /**
     *
     * @param columnNames
     */
    public DynaBeanAttribute(List<String> columnNames) {
        
      properties = new DynaProperty[columnNames.size()];
      
      int index = 0;
        
        for (String columnName : columnNames) {
            properties[index++] = new DynaProperty(columnName, Object.class);
        }
        
    }  

    /**
     *
     * @return
     */
    public DynaProperty[] getProperties() {
        return properties;
    }

    /**
     *
     * @param properties
     */
    public void setProperties(DynaProperty[] properties) {
        this.properties = properties;
    }
    
      
    
}
