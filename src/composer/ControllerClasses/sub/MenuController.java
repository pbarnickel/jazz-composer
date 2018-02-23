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

    @FXML
    public void gotoAbout(ActionEvent actionEvent) throws IOException {changeScene("about", actionEvent);}

    @FXML
    public void gotoBackingTrack(ActionEvent actionEvent) throws IOException {changeScene("backingtrack", actionEvent);}

    @FXML
    public void gotoTest(ActionEvent actionEvent) throws IOException {changeScene("test", actionEvent);}

    @FXML
    public void gotoSettings(ActionEvent actionEvent) throws IOException {changeScene("settings", actionEvent);}

    @Override
    public void msg(String message, int type) {}

    @Override
    public void defaultInputs(){}
}
