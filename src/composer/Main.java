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
import javafx.stage.Stage;

public final class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("FXML/menu.fxml"));
        primaryStage.setTitle("Barnickel Jazz Composer");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        p("Jazz Composer started.");
        launch(args);
    }

    public static void p(String str){
        System.out.println(str);
    }
}
