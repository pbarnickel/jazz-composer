/*
    Description:    Controller for About-UI.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ControllerClasses.sub;

import composer.ControllerClasses.Controller;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class AboutController extends Controller{

    @FXML private Hyperlink hypGitHub;

    public void gotoMenu(ActionEvent actionEvent) throws IOException {changeScene("menu", actionEvent);}

    @Override
    public void msg(String message, int type) {}

    @Override
    public void defaultInputs() {}

    public void initialize(){
        //Activate hyperlink to GitHub
        hypGitHub.setOnAction(e -> {
            if(Desktop.isDesktopSupported())
            {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/pbarnickel/jazz-composer"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
