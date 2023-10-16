package Controllers;

import Dao.SQLDao;
import Utility.App;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class CreateDatabaseController implements Initializable {

    private Stage stage;
    private CRUDDatabaseController crudController;
    
    @FXML
    private TextField textField;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void setCRUDController(CRUDDatabaseController controller) {
        this.crudController = controller;
    }
    
        /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    @FXML
    public void exit(ActionEvent event) throws IOException, SQLException {
        stage.close();     
    }
    
    @FXML
    public void create(ActionEvent event) throws IOException, SQLException {
    
        SQLDao dao = new SQLDao();
        String retorno = dao.createDatabase(textField.getText());
        
        String title = "Resultado";
        
        String verify = "Banco de dados criado com sucesso.";
        
        if(!retorno.equals(verify)){
        message(retorno,title,4);}else{
        message(retorno,title,5);    
        }
        
    }
     
    private void message(String resultExc, String title, int type) throws IOException{
    
        Stage message = new Stage();
        FXMLLoader fxml = App.loadFXML("MessagePane");
        
        Parent root = fxml.load();       
        Scene scene = new Scene(root);
        message.setScene(scene);     
        message.setTitle(title);
        Image iconImage = new Image(getClass().getResourceAsStream("/icons/icon.png"));
        message.getIcons().add(iconImage);
        message.setResizable(false);
    
        MessagePaneController controller = fxml.getController();
        controller.setStage(stage);
        controller.setMe(message);
        controller.conteudoText(resultExc);
        controller.setType(type);
        controller.setCRUDController(crudController);
        
        stage.hide();
        message.show();
        
        
    }
    
    
         
}
