/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class CreateTableController implements Initializable {

    private Stage stage;
    String base;
    private CRUDDatabaseController crudController;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCRUDController(CRUDDatabaseController crudController) {
        this.crudController = crudController;
    }

    public void setBase(String base) {
        this.base = base;
    }
    
    
    
     @FXML
     private TextArea content;
     
     @FXML
     private TextField name;
    
    @FXML
    public void create(ActionEvent event) throws IOException, SQLException {
          
    String text = "CREATE TABLE " + name.getText() + " ( " + content.getText() + " );";
    String sql = text.split(";")[0] + ";";
            
        SQLDao dao = new SQLDao();
        String retorno = dao.createTable(base, sql);
        
        String title = "Resultado";
        message(retorno,title); 
  
    }
    
    public void handleButtonClick(ActionEvent event) {   
        stage.close();     
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
        controller.setType(2);
        controller.setBase(base);
        controller.setCRUDController(crudController);
        
        
    }
    
}
