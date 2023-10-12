/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import Beans.DynaBeans;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

public class CreateRegController implements Initializable {

    private DynaBeans bean;
    private Stage stage; 
    private CRUDDatabaseController crudController;

    public void setBean(DynaBeans bean) {
        this.bean = bean;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCRUDController(CRUDDatabaseController crudController) {
        this.crudController = crudController;
    }
    
    
    
    
    @FXML
    private Text text;
    
    @FXML
    private VBox VBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        
    }   
    
    public void Campos(){
    
    if (bean != null) {
        
            String texto = "Insira seu registro de " + bean.getName() + " :";
            text.setText(texto);
        
            DynaBean campos = bean.getBean();
            DynaProperty[] dynaProperties = campos.getDynaClass().getDynaProperties();
            

            for (int i = 0; i < dynaProperties.length; i++) {
                DynaProperty dynaProperty = dynaProperties[i];
                String propertyName = dynaProperty.getName();

                Text text = new Text(propertyName + ":");
                TextField textField = new TextField();
                
                VBox.getChildren().addAll(text, textField);
            }
        }
    
    
    }
    
    
    
}
