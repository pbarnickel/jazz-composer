/*
    Description:    Controller for Backingtrack-UI.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ControllerClasses.sub;

import composer.ComposerClasses.sub.Backingtrack;
import composer.ControllerClasses.Controller;
import composer.DataClasses.Chordcomplexity;
import composer.DataClasses.MusicStructureGroup;
import composer.DataClasses.Settings;
import composer.DataClasses.Response;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;

import jm.music.data.*;

public class BackingtrackController extends Controller {

    private Backingtrack bt = new Backingtrack();

    @FXML private Label lblOut;
    @FXML private ToggleButton tglPiano;
    @FXML private ToggleButton tglBass;
    @FXML private ToggleButton tglDrums;
    @FXML private TextField edtTempo;
    @FXML private TextField edtTone;
    @FXML private TextField edtPattern;
    @FXML private TextField edtRepeat;
    @FXML private ToggleGroup tglGrpMode;
    @FXML private ChoiceBox chbChordcomplexity;
    @FXML private ChoiceBox chbChordgroups;

    public void initialize(){
        //init settings
        settings = new Settings();

        //init choice-boxes
        allChordgroupsAsString = FXCollections.observableArrayList(getAllChordgroupsAsString());
        allChordcomplexitiesAsString = FXCollections.observableArrayList(getAllChordcomplexitiesAsString());
        chbChordgroups.setItems(getAllChordgroupsAsString());
        chbChordcomplexity.setItems(getAllChordcomplexitiesAsString());
    }

    @Override
    public void msg(String message, int type) {
        this.r = new Response(message, type, lblOut);
    }

    @FXML
    public void gotoMenu(ActionEvent actionEvent) throws IOException {
        changeScene("menu", actionEvent);
    }

    @FXML
    public void onView(ActionEvent actionEvent) {
        bt.showScore();
    }

    @FXML
    public void onClear(ActionEvent actionEvent){
        bt = new Backingtrack();
        msg("Composition cleared.",MSG_I);
    }

    @FXML
    public void onOpen(ActionEvent actionEvent){
        File selectedFile = midiFileChooser("Choose the MIDI-File to open", actionEvent);
        if (selectedFile != null) {
            bt.readMIDIinScore(selectedFile.getPath());
            msg(selectedFile.getName() + " loaded.",MSG_S);
        } else {
            msg("File could not be loaded.",MSG_E);
        }
    }

    @FXML
    public void onSave(ActionEvent actionEvent){
        File selectedFile = midiFileChooser("Choose place for saving File", actionEvent);
        if (selectedFile != null) {
            bt.writeScoreinMIDI(selectedFile.getPath());
            msg(selectedFile.getName() + " saved.",MSG_S);
        } else {
            msg("File could not be saved.",MSG_E);
        }
    }

    @FXML
    public void onTest(ActionEvent actionEvent) {
        //Testing Module Backingtrack
        bt.initScore();
        int rootPitch = C4;
        Score s = new Score();
        s = bt.getBackingTrack(rootPitch, "Backingtrack in C", 2);
        msg("Test Backing Track loaded.",MSG_I);
    }

    @FXML
    public void onPlay(ActionEvent actionEvent) {
        if(bt.getScore().getSize() > 0){
            bt.playScore();
            msg("Backing Track is played.",MSG_I);
        } else {
            msg("No Backing Track to play.",MSG_E);
        }
    }

    @FXML
    public void onCompose(ActionEvent actionEvent) {
        String error = "Composition unsuccessful: ";
        if(tglPiano.isSelected() || tglBass.isSelected() || tglDrums.isSelected()){
            if(edtTempo.getText().matches(REG_TEMPO)){
                if(edtTone.getText().matches(REG_TONE)){
                    if(edtPattern.getText().matches(REG_CHORD_USAGE)){
                        if(edtRepeat.getText().matches(REG_NUMBER)){
                            if(tglGrpMode.getSelectedToggle() != null) {
                                if(chbChordcomplexity.getValue() != null) {
                                    if(chbChordgroups.getValue() != null) {
                                        Boolean instruments[] = new Boolean[3];
                                        int tempo = Integer.parseInt(edtTempo.getText());
                                        int repeat = Integer.parseInt((edtRepeat.getText()));
                                        String tone = edtTone.getText();
                                        String pattern = edtPattern.getText();
                                        instruments[0] = tglPiano.isSelected();
                                        instruments[1] = tglBass.isSelected();
                                        instruments[2] = tglDrums.isSelected();
                                        int mode;
                                        if (tglGrpMode.getSelectedToggle().equals("Major")) {
                                            mode = 4;
                                        } else {
                                            mode = 3;
                                        }
                                        int indexOfGroup = settings.getIndexOfGroup(settings.getChordgroups(), chbChordgroups.getValue().toString());
                                        int indexOfComplexity = settings.getIndexOfComplexity((chbChordcomplexity.getValue().toString()));
                                        MusicStructureGroup chordgroup = settings.getChordgroups().get(indexOfGroup);
                                        Chordcomplexity chordcomplexity = settings.getChordcomplexities().get(indexOfComplexity);
                                        bt.createBackingtrack(instruments, tempo, tone, pattern, repeat, mode, chordgroup, chordcomplexity);
                                        msg("Composition created successfully.", MSG_S);
                                    } else {msg(error + "No chordgroup selected.", MSG_E);}
                                } else {msg(error + "No chordcomplexity selected.", MSG_E);}
                            } else {msg(error + "No mode selected.", MSG_E);}
                        } else {msg(error + "Repeat is not a number greater than zero.", MSG_E);}
                    } else {msg(error + "Pattern is not valid. Example: 0-2-7", MSG_E);}
                } else {msg(error + "Tone is not valid.", MSG_E);}
            } else {msg(error + "Tempo is not a number.", MSG_E);}
        } else {msg(error + "No instruments active.", MSG_E);}
    }

    @FXML
    public void onPiano(ActionEvent actionEvent) {
        if(tglPiano.isSelected()){
            msg("Piano activated.", MSG_I);
        } else {
            msg("Piano disabled.", MSG_W);
        }
    }

    @FXML
    public void onBass(ActionEvent actionEvent) {
        if(tglBass.isSelected()){
            msg("Bass activated.", MSG_I);
        } else {
            msg("Bass disabled.", MSG_W);
        }
    }

    @FXML
    public void onDrums(ActionEvent actionEvent) {
        if(tglDrums.isSelected()){
            msg("Drums activated.", MSG_I);
        } else {
            msg("Drums disabled.", MSG_W);
        }
    }

    @FXML
    public void onTempo(ActionEvent actionEvent) {
        msg("Tempo set to " + edtTempo.getText() + ".", MSG_I);
    }

    @FXML
    public void onTone(ActionEvent actionEvent) {
        msg("Tone set to " + edtTone.getText() + ".", MSG_I);
    }

    @FXML
    public void onPattern(ActionEvent actionEvent) {
        msg("Pattern set to " + edtPattern.getText() + ".", MSG_I);
    }

    @FXML
    public void onRepeat(ActionEvent actionEvent) {
        msg("Repeat set to " + edtRepeat.getText() + " times.", MSG_I);
    }
}
