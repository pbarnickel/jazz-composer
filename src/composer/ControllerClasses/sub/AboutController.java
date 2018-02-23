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
import java.io.IOException;

public class AboutController extends Controller{

    @FXML
    public void gotoMenu(ActionEvent actionEvent) throws IOException {changeScene("menu", actionEvent);}

    @Override
    public void msg(String message, int type) {}

    @Override
    public void defaultInputs() {}
}
