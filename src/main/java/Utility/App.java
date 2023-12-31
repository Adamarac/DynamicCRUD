package Utility;

import Controllers.CRUDDatabaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.image.Image;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    /**
     *
     * @param stage
     * @throws IOException
     * @throws SQLException
     */
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader xml = loadFXML("CRUDDatabase");
        scene = new Scene(xml.load());  
        
        stage.setScene(scene);
        stage.setTitle("CRUD");
        Image iconImage = new Image(getClass().getResourceAsStream("/icons/icon.png"));
        stage.getIcons().add(iconImage);
        stage.setResizable(false);
        
        stage.show();
    }


    public static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/FXML/" + fxml + ".fxml"));
        return fxmlLoader;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        launch();
       
    }

}