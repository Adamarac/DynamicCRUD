package Beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

/**
 *
 * @author User
 */
public class DynaBeans {
    
    private String name;
    private DynaBean bean;

    /**
     *
     * @param table
     * @param attributes
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public DynaBeans(String table, DynaBeanAttribute attributes) throws IllegalAccessException, InstantiationException {
        this.name = table;
         
        DynaClass dynaClass = new BasicDynaClass(table, null, attributes.getProperties());
        bean = dynaClass.newInstance();
        
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public DynaBean getBean() {
        return bean;
    }

    /**
     *
     * @param bean
     */
    public void setBean(DynaBean bean) {
        this.bean = bean;
    }
    
    /**
     *
     * @return
     */
    public List<String> attributeToList(){
    
    List<String> colunas = new ArrayList<>(); 
     DynaProperty[] dynaProperties = bean.getDynaClass().getDynaProperties(); 
    
    for (DynaProperty property : dynaProperties) {
            colunas.add(property.getName());
        }
    
    return colunas;    
    }

   
    
    
    
    
}
