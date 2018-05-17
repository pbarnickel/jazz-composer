/*
    Description:    Controller for Menu-UI.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ControllerClasses.sub;

import composer.ControllerClasses.Controller;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import java.io.IOException;

public class MenuController extends Controller {

    public void gotoSettings(ActionEvent actionEvent) throws IOException {changeScene("settings", actionEvent);}

    public void gotoComposeJazz(ActionEvent actionEvent) throws IOException {changeScene("composer", actionEvent);}

    public void gotoAbout(ActionEvent actionEvent) throws IOException {changeScene("about", actionEvent);}

    @Override
    public void msg(String message, int type) {}

    @Override
    public void defaultInputs(){}
}
