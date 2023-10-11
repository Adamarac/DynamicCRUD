/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;



/**
 * FXML Controller class
 *
 * @author User
 */
public class MessagePaneController implements Initializable {

    
private Stage stage; 
private CRUDDatabaseController crudController;
private int type;
private String base;

  public void setStage(Stage stage) {
        this.stage = stage;
  } 
  
  public void setCRUDController(CRUDDatabaseController controller) {
        this.crudController = controller;
  }

    public void setType(int type) {
        this.type = type;
    }

    public void setBase(String base) {
        this.base = base;
    }
    
    
  
  

@FXML
private Text texto;

    @FXML
    private void onButtonClick(ActionEvent event) throws SQLException, IllegalAccessException, InstantiationException, IOException{   
        
       switch(type){
           
           case 1:
            crudController.attComboItems();
            break;
            
           case 2:
            crudController.atualizarListView(base);
            break;
            
           case 4:
            crudController.listViewMouseClicked(null);
            break;
            
           default:
            break;
            
        }
                
        stage.close();       
    }
       
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void conteudoText(String resultExc){  
    
        texto.setText(resultExc);
    
    }
    
}
