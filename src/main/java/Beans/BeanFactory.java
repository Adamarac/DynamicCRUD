package Beans;

import java.util.List;

/**
 *
 * @author User
 */
public class BeanFactory {

    private BeanFactory() {}
    
    /**
     *
     * @param name
     * @param columnNames
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static DynaBeans newInstance(String name, List<String> columnNames) throws IllegalAccessException, InstantiationException {
        
    DynaBeanAttribute atributos = new DynaBeanAttribute(columnNames);
    DynaBeans retorno = new DynaBeans(name,atributos);
    
    return retorno;
    }
    
    
    
}
