/*
    Description:    Controller for Test-Composer-UI.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ControllerClasses.sub;

import composer.ControllerClasses.Controller;
import composer.DataClasses.Response;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import java.io.IOException;
import composer.ComposerClasses.sub.Test;

public class TestController extends Controller {

    private Test t = new Test();

    @FXML Label lblOut;

    //Returns to menu
    public void gotoMenu(ActionEvent actionEvent) throws IOException {
        changeScene("menu", actionEvent);
    }

    //Functions in module
    public void onDotsAndDashes(ActionEvent actionEvent) {
        t.dots_and_dashes();
        msg("Simple Note generated.",MSG_I);
    }

    public void onArpeggio(ActionEvent actionEvent) {
        t.arpeggio();
        msg("Simple Arpeggio-Composition generated.",MSG_I);
    }

    public void onCreateDrumBeat(ActionEvent actionEvent) {
        t.createDrumBeat();
        msg("Drum Beats generated.",MSG_I);
    }

    public void onBasicGame(ActionEvent actionEvent) {
        t.basicGame();
        msg("Composition generated.",MSG_I);
    }

    public void onStochastic(ActionEvent actionEvent) {
        t.stochastic();
        msg("Random-Composition generated.",MSG_I);
    }

    public void onCleanComposing(ActionEvent actionEvent) {
        t.cleanComposing();
        msg("New MIDI-File generated.",MSG_I);
    }

    @Override
    public void msg(String message, int type) {
        this.r = new Response(message, type, lblOut);
    }

    @Override
    public void defaultInputs(){}
}
