/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

public class CreateRegController implements Initializable {

    private DynaBeans bean;
    private Stage stage; 
    private String database;
    private CRUDDatabaseController crudController;

    public void setBean(DynaBeans bean) {
        this.bean = bean;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setDatabase(String database) {
        this.database = database;
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
    
    @FXML
    public void create(ActionEvent event) throws IOException, SQLException, IllegalAccessException, InstantiationException {
    
        DynaBean reg = preencherDynaBean();
        Conexao connection = new Conexao(database);
        Connection conn = connection.obterConexao();    
        DynaDao dao = new DynaDao(conn, bean);
        
        String upres = dao.adicionar(reg);
        message(upres,"Atualizar");
        
    
    }
    
    public void exit(ActionEvent event) {   
        stage.close();     
    }
    
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
                textField.setId("textField_" + propertyName);
                
                VBox.getChildren().addAll(text, textField);
            }
        }
    
    
    }
    
    public DynaBean preencherDynaBean() {
        
    DynaBean campos = null;
    if (bean != null) {
        campos = bean.getBean();
        DynaProperty[] dynaProperties = campos.getDynaClass().getDynaProperties();

        for (DynaProperty dynaProperty : dynaProperties) {
            String propertyName = dynaProperty.getName();
            String textFieldId = "textField_" + propertyName;

            Node node = VBox.lookup("#" + textFieldId);

            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                String valor = textField.getText();

                campos.set(propertyName, valor);
            }
        }
    }
    
    return campos;
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
