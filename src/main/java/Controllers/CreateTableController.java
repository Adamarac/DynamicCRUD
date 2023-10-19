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
                
        SQLDao dao = new SQLDao();
        String retorno = dao.createTable(base,name.getText(),content.getText());
        
        String title = "Resultado";
        
        String verify = "Tabela criada com sucesso!";
        String verify2 = "Tabela criada com sucesso!\n\n As demais querys foram ignoradas";
        
        if(verify.equals(retorno) || verify2.equals(retorno)){
        message(retorno,title,6);}else{
        message(retorno,title,4);
        } 
  
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
        controller.setBase(base);
        controller.setType(type);
        controller.setCRUDController(crudController);
        
        stage.hide();
        message.show();
        
        
    }
    
}
