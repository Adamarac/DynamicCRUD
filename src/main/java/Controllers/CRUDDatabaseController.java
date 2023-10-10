package Controllers;

import Beans.DynaBeans;
import Dao.DynaDao;
import Dao.SQLDao;
import Utility.App;
import Utility.Conexao;
import java.io.IOException;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.apache.commons.beanutils.DynaBean;

/**
 * FXML Controller class
 *
 * @author User
 */
public class CRUDDatabaseController implements Initializable {
      
    private String selectedBD;
    private String selectedTable;

    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private ListView<String> listView;
    @FXML
    private TableView<DynaBean> tableView;

    
    @FXML
    private void onButtonClick(ActionEvent event) throws IOException, SQLException, IllegalAccessException, InstantiationException {
        
        TableViewSelectionModel<DynaBean> selectionModel = tableView.getSelectionModel();
        
        int selectedIndex = selectionModel.getSelectedIndex();
        
        selectedTable = listView.getSelectionModel().getSelectedItem();
        
        DynaDao dao = CreateDao();
        DynaBeans bean = dao.getDynaBean();
        
        String nullSelection = "";
        
        if (selectedIndex >= 0) {
        DynaBean selectedRow = tableView.getItems().get(selectedIndex);
                     
            for (TableColumn<DynaBean, ?> coluna : tableView.getColumns()) {
                String nomeColuna = coluna.getText(); 
                Object valorColuna = selectedRow.get(nomeColuna); 
                bean.getBean().set(nomeColuna, valorColuna);      
            }      
            
            confirmaExclusao(selectedBD, bean);
            
        } else {
               nullSelection = "Não há registros selecionados";  
               message(nullSelection);  
        }  
               
        
        
    }
    
    
    @FXML
    public void listViewMouseClicked(MouseEvent event) throws SQLException, IllegalAccessException, InstantiationException {
    
    selectedBD = comboBox.getValue();
    selectedTable = listView.getSelectionModel().getSelectedItem();

    if (selectedBD != null && selectedTable != null) {
              
        DynaDao dao = CreateDao();

        List<String> colunas = dao.getDynaBean().attributeToList();
        List<DynaBean> matriz = dao.listar();

        tableView.getItems().clear();
        tableView.getColumns().clear();
        
        criarColunas(colunas);
        preencherTabela(matriz);
    }
}
        
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            
            List<String> listaDeItens = SQLDao.listarBases();
            ObservableList<String> items = FXCollections.observableArrayList(listaDeItens);
            comboBox.setItems(items);
            comboBox.setValue("Selecione");
        } catch (SQLException ex) {
            Logger.getLogger(CRUDDatabaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    atualizarListView(newValue);
                } catch (SQLException ex) {
                    Logger.getLogger(CRUDDatabaseController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });  
        
    }    
    
    public void atualizarListView(String selecao) throws SQLException {
       
        ObservableList<String> listViewItems = FXCollections.observableArrayList();
        
        SQLDao dao = new SQLDao();
        
        List<String> tabelas = dao.tabelas(selecao);
        
        for (String tabela : tabelas) {
            listViewItems.add(tabela);
        }
        
        listView.setItems(listViewItems);
    }
        
   private void criarColunas(List<String> nomesColunas) {
    for (String nomeColuna : nomesColunas) {
        TableColumn<DynaBean, String> coluna = new TableColumn<>(nomeColuna);
        coluna.setCellValueFactory(param -> {
            DynaBean dynaBean = param.getValue();
            if (dynaBean != null) {
                Object valorColuna = dynaBean.get(nomeColuna);
                if (valorColuna != null) {
                    return new SimpleObjectProperty<>(valorColuna.toString());
                }
            }
            return new SimpleObjectProperty<>();
        }); 
        tableView.getColumns().add(coluna);
    }
}

        
    public void preencherTabela(List<DynaBean> dados) {
       ObservableList<DynaBean> listaDeRegistros;
       listaDeRegistros = FXCollections.observableList(dados);
       tableView.setItems(listaDeRegistros);
    }
     
    public DynaDao CreateDao() throws SQLException, IllegalAccessException, InstantiationException{
    
        Conexao connection = new Conexao(selectedBD);
        Connection conn = connection.obterConexao();
        
        DynaDao dao = new DynaDao(conn, selectedTable);
        
        return dao;
    }
    
    private Stage setData(Stage stage){
        Image iconImage = new Image(getClass().getResourceAsStream("/icons/icon.png"));   
        stage.setTitle("CRUD");
        stage.getIcons().add(iconImage);
        stage.setResizable(false);    
        return stage;
    }
    
    private void confirmaExclusao(String DatabaseTable, DynaBeans registro) throws IOException{
    
        Stage stage = new Stage(); 
        FXMLLoader fxml = App.loadFXML("ConfirmaExclusao");
        
        Parent root = fxml.load();       
        Scene scene = new Scene(root);
        stage.setScene(scene);  
        
        stage = setData(stage);
               
        ConfirmaExclusaoController controller = fxml.getController();
        controller.setStage(stage);
        controller.setDatabaseTable(DatabaseTable);
        controller.setRegistro(registro);
        controller.conteudoText();
        controller.setCRUDController(this);
        
   
        stage.show();
    }
    
    private void message(String resultExc) throws IOException{
    
        FXMLLoader fxml = App.loadFXML("MessagePane");
        
        Stage stage = new Stage();
        Parent root = fxml.load();       
        Scene scene = new Scene(root);
        stage.setScene(scene);     
        stage = setData(stage);
    
        MessagePaneController controller = fxml.getController();
        controller.setStage(stage);
        controller.conteudoText(resultExc);
        controller.setCRUDController(this);
        
        stage.show();
        
    }
    
}
