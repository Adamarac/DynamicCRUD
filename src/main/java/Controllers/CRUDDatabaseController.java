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
    DynaDao dao;

    public String getSelectedBD() {
        return selectedBD;
    }

    public String getSelectedTable() {
        return selectedTable;
    }

    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private ListView<String> listView;
    @FXML
    private TableView<DynaBean> tableView;
 
    @FXML
    private void onButtonClick(ActionEvent event) throws IOException, SQLException, IllegalAccessException, InstantiationException {
    
    TableViewSelectionModel<DynaBean> selectionModel = tableView.getSelectionModel();
    selectedTable = listView.getSelectionModel().getSelectedItem();
    
    DynaDao dao = CreateDao();
    
    if (dao == null) {
        return;
    }

    int selectedIndex = selectionModel.getSelectedIndex();

    if (selectedIndex < 0) {
        message("Não há registros selecionados");
        return;
    }

    DynaBean selectedRow = tableView.getItems().get(selectedIndex);
    
    if (nullBean(selectedRow)) {
        message("Não há registros selecionados");
        return;
    }
    
    DynaBeans bean = dao.getDynaBean();
    
    for (TableColumn<DynaBean, ?> coluna : tableView.getColumns()) {
        String nomeColuna = coluna.getText();
        Object valorColuna = selectedRow.get(nomeColuna);
        bean.getBean().set(nomeColuna, valorColuna);
    }
    
    confirmaExclusao(selectedBD, bean);
}

    
    @FXML
    private void newBD(ActionEvent event) throws IOException, SQLException, IllegalAccessException, InstantiationException {
    
        Stage stage = new Stage(); 
        FXMLLoader fxml = App.loadFXML("CreateDatabase");
        
        Parent root = fxml.load();       
        Scene scene = new Scene(root);
        stage.setScene(scene);  
        
        String title = "Cria base de dados";
        stage = setData(stage,title);
               
        CreateDatabaseController controller = fxml.getController();
        controller.setStage(stage);
        controller.setCRUDController(this);
        
   
        stage.show();
        
    }
    
    @FXML
    private void newTB(ActionEvent event) throws IOException, SQLException, IllegalAccessException, InstantiationException {
    
        String title = "Criar tabela";
        if ("Selecione".equals(comboBox.getValue())) {
            
            message("Selecione uma base de dados para poder realizar a criação de tabelas");
            
        }else{
            
            Stage stage = new Stage();
            FXMLLoader fxml = App.loadFXML("CreateTable");
            
            Parent root = fxml.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            
            stage = setData(stage,title);
            
            CreateTableController controller = fxml.getController();
            controller.setStage(stage);
            controller.setBase(comboBox.getValue());
            controller.setCRUDController(this);
            
            
            stage.show();
            
        }
    
    }
    
    @FXML
    private void CreateReg(ActionEvent event) throws IOException, SQLException, IllegalAccessException, InstantiationException {
    
        if (selectedBD != null && selectedTable != null) {
        
        DynaDao est = CreateDao();
        DynaBeans bean = est.getDynaBean();

            
        String title = "Adicionar " + selectedTable;
        newReg(title,bean);
        
        }else{
        
            String err = "Não há uma base de dados ou tabela selecionada";
            message(err);
            
        }
        
    }
    
    
    @FXML
    private void UpdateReg(ActionEvent event) throws IOException, SQLException, IllegalAccessException, InstantiationException {
    
        if (selectedBD != null && selectedTable != null) {
        
        DynaDao reg = CreateDao();
                
        TableViewSelectionModel<DynaBean> selectionModel = tableView.getSelectionModel();
        int selectedIndex = selectionModel.getSelectedIndex();    
        
        if (selectedIndex < 0) {
        message("Não há registros selecionados");
        return;
    }

    DynaBean selectedRow = tableView.getItems().get(selectedIndex);
    
    if (nullBean(selectedRow)) {
        message("Não há registros selecionados");
        return;
    }
        
        DynaBeans bean = reg.getDynaBean();
    
        for (TableColumn<DynaBean, ?> coluna : tableView.getColumns()) {
        String nomeColuna = coluna.getText();
        Object valorColuna = selectedRow.get(nomeColuna);
        bean.getBean().set(nomeColuna, valorColuna);
        }
    
    
        String title = "Atualizar " + selectedTable;
        UpReg(title,bean);
        
        }else{
        
            String err = "Não há uma base de dados ou tabela selecionada";
            message(err);
            
        }
        
        
    }
    
    
    
    @FXML
    public void listViewMouseClicked(MouseEvent event) throws SQLException, IllegalAccessException, InstantiationException, IOException {
    
    selectedBD = comboBox.getValue();
    selectedTable = listView.getSelectionModel().getSelectedItem();

    if (selectedBD != null && selectedTable != null) {
              
        this.dao = CreateDao();

        List<String> colunas = dao.getDynaBean().attributeToList();
        List<DynaBean> matriz = dao.listar();

        tableView.getItems().clear();
        tableView.getColumns().clear();
        
        criarColunas(colunas);
        preencherTabela(matriz);
    }
}
        
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        attComboItems();
        comboBox.setValue("Selecione");
        
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
        
    public void preencherTabela(List<DynaBean> dados) {
       ObservableList<DynaBean> listaDeRegistros;

    if (dados == null || dados.isEmpty()) {
       
        DynaBean dynaBeanVazio = this.dao.getDynaBean().getBean();
        listaDeRegistros = FXCollections.observableArrayList(dynaBeanVazio);
    } else {
        listaDeRegistros = FXCollections.observableList(dados);
    }

       tableView.setItems(listaDeRegistros);
    }
    
    public void attComboItems(){
    
        try {
            
            List<String> listaDeItens = SQLDao.listarBases();
            ObservableList<String> items = FXCollections.observableArrayList(listaDeItens);
            comboBox.setItems(items);
        } catch (SQLException ex) {
            Logger.getLogger(CRUDDatabaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public DynaDao CreateDao() throws SQLException, IllegalAccessException, InstantiationException, IOException{
       
        if(selectedBD != null && selectedTable != null){
        
        Conexao connection = new Conexao(selectedBD);
        Connection conn = connection.obterConexao();
        
        DynaDao dao = new DynaDao(conn, selectedTable);
        
        return dao;
        
        }else{
        
            String err = "Não há uma base de dados ou tabela selecionada";
            message(err);
        }
        
       return null; 
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
    
    private void confirmaExclusao(String DatabaseTable, DynaBeans registro) throws IOException{
    
        Stage stage = new Stage(); 
        FXMLLoader fxml = App.loadFXML("ConfirmaExclusao");
        
        Parent root = fxml.load();       
        Scene scene = new Scene(root);
        stage.setScene(scene);  
        
        String title = "Excluir";
        stage = setData(stage,title);
               
        ConfirmaExclusaoController controller = fxml.getController();
        controller.setStage(stage);
        controller.setDatabaseTable(DatabaseTable);
        controller.setRegistro(registro);
        controller.conteudoText();
        controller.setCRUDController(this);
        
   
        stage.show();
    }
    
    private void newReg(String title, DynaBeans bean) throws IOException, SQLException, IllegalAccessException, InstantiationException{
    
        Stage stage = new Stage(); 
        FXMLLoader fxml = App.loadFXML("CreateReg");
        
        Parent root = fxml.load();       
        Scene scene = new Scene(root);
        stage.setScene(scene);  
        
        stage = setData(stage,title);
        
               
        CreateRegController controller = fxml.getController();
        controller.setStage(stage);
        controller.setBean(bean);
        controller.setCRUDController(this);
        controller.Campos();
   
        stage.show();
        
    
    }
    
    private void UpReg(String title, DynaBeans bean) throws IOException, SQLException, IllegalAccessException, InstantiationException{
    
        Stage stage = new Stage(); 
        FXMLLoader fxml = App.loadFXML("UpdateReg");
        
        Parent root = fxml.load();       
        Scene scene = new Scene(root);
        stage.setScene(scene);  
        
        stage = setData(stage,title);
        
               
        UpdateRegController controller = fxml.getController();
        controller.setStage(stage);
        controller.setBean(bean);
        controller.setCRUDController(this);
        controller.Campos();
   
        stage.show();
        
    
    }
    
    
    
    
    private void message(String resultExc) throws IOException{
    
        FXMLLoader fxml = App.loadFXML("MessagePane");
        
        Stage stage = new Stage();
        Parent root = fxml.load();       
        Scene scene = new Scene(root);
        stage.setScene(scene);   
        
        String title = "Resultado";
        stage = setData(stage,title);
    
        MessagePaneController controller = fxml.getController();
        controller.setStage(stage);
        controller.conteudoText(resultExc);
        controller.setCRUDController(this);
        controller.setType(0);
        
        stage.show();
        
    }
    
    private boolean nullBean(DynaBean selectedRow){
    
    boolean allNull = true;
    for (TableColumn<DynaBean, ?> coluna : tableView.getColumns()) {
        String nomeColuna = coluna.getText();
        Object valorColuna = selectedRow.get(nomeColuna);
        if (valorColuna != null) {
            allNull = false;
            break;
        }
    }
    
     return allNull;
    }
     
    private Stage setData(Stage stage, String title){
        Image iconImage = new Image(getClass().getResourceAsStream("/icons/icon.png"));   
        stage.setTitle(title);
        stage.getIcons().add(iconImage);
        stage.setResizable(false);    
        return stage;
    }
    
   
}
