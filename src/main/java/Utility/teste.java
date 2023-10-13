package Utility;

import Beans.Bean;
import Beans.DynaBeans;
import Dao.DynaDao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

/**
 *
 * @author User
 */
public class teste {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.InstantiationException
     */
    public static void main(String[] args) throws SQLException, IllegalAccessException, InstantiationException {
        
        Connection connection = new Conexao("webii").obterConexao();
        
        DynaBeans bean = Bean.createBean(connection, "cliente");
        
        DynaBean naeb = bean.getBean();
        
        DynaDao dao = new DynaDao(connection, bean);
        
        List<DynaBean> listar = dao.listar();
        
        for(DynaBean item : listar){
        
        DynaProperty[] properties = item.getDynaClass().getDynaProperties();
        
        for (DynaProperty property : properties) {
            System.out.println("Nome do atributo: " + property.getName());
            Object valor = item.get(property.getName());
            System.out.println("Valor: " + valor); 
            
        }
        
        
        
        }
        
        
        DynaProperty[] properties = naeb.getDynaClass().getDynaProperties();
        
        for (DynaProperty property : properties) {
            System.out.println("Nome do atributo: " + property.getName());
        }
    }
    
}
