package Controllers;

import Beans.Bean;
import Beans.DynaBeans;
import Dao.DynaDao;
import Utility.App;
import Utility.Conexao;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;



/**
 * FXML Controller class
 *
 * @author User
 */
public class ConfirmaExclusaoController implements Initializable {
  
    private DynaBeans registro;
    private String Database;
    private CRUDDatabaseController crudController;
    
    private Stage stage; 

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setCRUDController(CRUDDatabaseController controller) {
        this.crudController = controller;
    }
    
    public void setRegistro(DynaBeans registro) {
        this.registro = registro;
    }

    public void setDatabaseTable(String Database) {
        this.Database = Database;
    }
    
    
    
    @FXML
    private Text texto;
            
    @FXML
    private void onButtonClick(ActionEvent event) throws IOException, SQLException, IllegalAccessException, InstantiationException {
    
        Conexao connection = new Conexao(Database);
        Connection conn = connection.obterConexao();    
        String table = registro.getName();
        DynaBeans bean = Bean.createBean(conn, table);
        DynaDao dao = new DynaDao(conn, bean);
        
        String pk = dao.pk();
        
        DynaBean atributos = registro.getBean();
        Object pkVal = atributos.get(pk);
        
        String deletedRows = dao.excluir(pkVal,pk);
        
        String title = "Resultado";
        message(deletedRows,title);
               
        
    }
    
        /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
          
               
    }    
        
    public void handleButtonClick(ActionEvent event) {   
        stage.close();     
    }
    
    public void conteudoText(){  
        
        texto.setText("Para realizar a exclusão verifique os dados abaixo \n e clique em excluir\n\n");
        
        StringBuilder result = new StringBuilder();
    
        DynaBean atributos = registro.getBean();
        DynaProperty[] properties = atributos.getDynaClass().getDynaProperties();
    
        for (DynaProperty property : properties) {
        String propertyName = property.getName();
        Object propertyValue = atributos.get(propertyName);
        
        
        result.append(propertyName).append(": ").append(propertyValue).append("\n");
        }
        
        texto.setText(texto.getText() + result.toString());
        
    }
    
    private void message(String resultExc, String title) throws IOException{
    
        FXMLLoader fxml = App.loadFXML("MessagePane");
        
        Parent root = fxml.load();       
        Scene scene = new Scene(root);
        stage.setScene(scene);     
        stage.setTitle(title);
        Image iconImage = new Image(getClass().getResourceAsStream("/icons/icon.png"));
        stage.getIcons().add(iconImage);
        stage.setResizable(false);
    
        MessagePaneController controller = fxml.getController();
        controller.setStage(stage);
        controller.conteudoText(resultExc);
        controller.setType(4);
        controller.setCRUDController(crudController);
        
        
    }
    
}
