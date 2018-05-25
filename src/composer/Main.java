/*
    Description:    Main-Class for starting the application.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;

public final class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        String settings = new File("").getAbsoluteFile() + "/src/composer/JSON/settings.json";
        String default_settings = new File("").getAbsoluteFile() + "/src/composer/JSON/default.json";
        if(new File(settings).exists() && new File(default_settings).exists()) {
            Parent root = FXMLLoader.load(getClass().getResource("FXML/menu.fxml"));
            primaryStage.setTitle("Jazz Composer");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error loading the settings files!");
            alert.setContentText("Settings files could not be loaded. The files 'settings.json' and 'default.json' should be in the same directory as the Jazz Composer.");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void p(String str){
        System.out.println(str);
    }
}
