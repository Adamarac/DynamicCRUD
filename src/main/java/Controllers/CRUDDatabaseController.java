package Controllers;

import Dao.DynaDao;
import Dao.SQLDao;
import Utility.Conexao;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author User
 */
public class CRUDDatabaseController implements Initializable {

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private ListView<String> listView;
    
    @FXML
    private TableView<Object[]> tableView;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            
            List<String> listaDeItens = SQLDao.listar();
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
        
        
        
        
        TableColumn<Object[], Object> column1 = new TableColumn<>("Coluna 1");
        
        
    }   
    
    
    private void atualizarListView(String selecao) throws SQLException {
       
        ObservableList<String> listViewItems = FXCollections.observableArrayList();
        
        SQLDao dao = new SQLDao();
        
        List<String> tabelas = dao.tabelas(selecao);
        
        for (String tabela : tabelas) {
            listViewItems.add(tabela);
        }
        
        listView.setItems(listViewItems);
    }
    
    
    @FXML
    private void listViewMouseClicked(MouseEvent event) throws SQLException, IllegalAccessException, InstantiationException {
    
    String selectedBD = comboBox.getValue();
    String selectedTable = listView.getSelectionModel().getSelectedItem();

    if (selectedBD != null && selectedTable != null) {
        
        Conexao conn = new Conexao(selectedBD);
        DynaDao dao = new DynaDao(conn.obterConexao(), selectedTable);

        List<String> colunas = dao.getDynaBean().attributeToList();
        Object[][] matriz = dao.listar();

        tableView.getItems().clear();
        tableView.getColumns().clear();
        

        criarColunas(colunas);
        preencherTabela(matriz);
    }
}
    
    
    private void criarColunas(List<String> nomesColunas) {
        for (int i = 0; i < nomesColunas.size(); i++) {
            final int colunaIndex = i;
            String nomeColuna = nomesColunas.get(i);

            TableColumn<Object[], Object> coluna = new TableColumn<>(nomeColuna);
            coluna.setCellValueFactory(cellData -> {
                Object[] linha = cellData.getValue();
                return new SimpleObjectProperty<>(linha[colunaIndex]);
            });

            tableView.getColumns().add(coluna);
        }
    }
    
    public void preencherTabela(Object[][] dados) {
        tableView.getItems().addAll(dados);
    }
    
}
