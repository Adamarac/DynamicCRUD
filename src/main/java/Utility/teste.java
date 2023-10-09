package Utility;

import Dao.DynaDao;
import java.sql.SQLException;

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
        
        Conexao conn = new Conexao("clinica");
        
        DynaDao dao = new DynaDao(conn.obterConexao(),"medico");
        Object[][] matriz = dao.listar();
        
        for (Object[] matriz1 : matriz) {
            for (Object valor : matriz1) {
                System.out.print(valor + "\t"); // Use \t para separar os valores por guias
            }
            System.out.println(); // Pule para a próxima linha após imprimir uma linha completa
        }
        
    }
    
}
