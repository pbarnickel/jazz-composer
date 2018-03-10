/*
    Description:    Controller for Backingtrack-UI.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ControllerClasses.sub;

import composer.ComposerClasses.sub.Backingtrack;
import composer.ControllerClasses.Controller;
import composer.DataClasses.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import javafx.scene.shape.Path;
import jm.music.data.*;

public class BackingtrackController extends Controller {

    private Backingtrack bt = new Backingtrack();

    //Common
    @FXML private Label lblOut;

    //General
    @FXML private ToggleButton tglGeneralPiano;
    @FXML private ToggleButton tglGeneralBass;
    @FXML private ToggleButton tglGeneralDrums;
    @FXML private TextField edtGeneralTempo;
    @FXML private TextField edtGeneralTone;
    @FXML private TextField edtGeneralRepeat;

    //Pattern
    @FXML private TableView tblPattern;
    @FXML private TableColumn<Patternelement, Integer> colPatternTranspose;
    @FXML private TableColumn<MusicStructure, String> colPatternMode;
    @FXML private TableColumn<Patternelement, String> colPatternChordgroup;
    @FXML private TableColumn<Patternelement, String> colPatternChord;
    @FXML private TableColumn<Patternelement, String> colPatternChordcomplexity;
    @FXML private TableColumn<MusicStructure, String> colPatternUsage;
    @FXML private TextField edtPatternTranspose;
    @FXML private ChoiceBox chbPatternChordgroups;
    @FXML private ChoiceBox chbPatternChord;
    @FXML private ChoiceBox chbPatternChordcomplexity;

    public void initialize(){
        settings = new Settings();
        update();
        defaultInputs();
    }

    public void update(){
        //load lists
        allChords = FXCollections.observableArrayList(getAllMusicStructureItems(settings.getChordgroups()));
        allChordgroups = FXCollections.observableArrayList(getMusicStructureGroupsItems(settings.getChordgroups()));
        allChordgroupsAsString = FXCollections.observableArrayList(getMusicStructureGroupsAsString(settings.getChordgroups()));
        allChordcomplexities = FXCollections.observableArrayList(getChordcomplexityItems(settings.getChordcomplexities()));
        allChordcomplexitiesAsString = FXCollections.observableArrayList(getChordcomplexitiesAsString(settings.getChordcomplexities()));
        chbPatternChordgroups.setItems(allChordgroupsAsString);
        chbPatternChordcomplexity.setItems(allChordcomplexitiesAsString);
        chbPatternChordgroups.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                int indexOfGroup = settings.getIndexOfGroup(settings.getChordgroups(), newValue.toString());
                chords = FXCollections.observableArrayList(getMusicStructureItems(settings.getChordgroups().get(indexOfGroup)));
                chordsAsString = FXCollections.observableArrayList(getMusicStructuresAsString(settings.getChordgroups().get(indexOfGroup).getMusicStructures()));
                chbPatternChord.setItems(chordsAsString);
            }
        });
    }

    @Override
    public void defaultInputs(){
        tglGeneralPiano.setSelected(true);
        edtGeneralTempo.setText("120");
        edtGeneralTone.setText("C");
        edtGeneralRepeat.setText("3");
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
        if(new File(settings.getDefault_location()).exists()) {
            File selectedFile = midiFileChooser("Choose the MIDI-File to open", actionEvent);
            if (selectedFile != null) {
                bt.readMIDIinScore(selectedFile.getPath());
                msg(selectedFile.getName() + " loaded.", MSG_S);
            } else {msg("File could not be loaded.", MSG_E);}
        } else {msg("Default path not valid. Change this in the settings.",MSG_E);}
    }

    @FXML
    public void onSave(ActionEvent actionEvent){
        if(new File(settings.getDefault_location()).exists()) {
            File selectedFile = midiFileChooser("Choose place for saving File", actionEvent);
            if (selectedFile != null) {
                bt.writeScoreinMIDI(selectedFile.getPath());
                msg(selectedFile.getName() + " saved.",MSG_S);
            } else {msg("File could not be saved.", MSG_E);}
        } else {msg("Default path not valid. Change this in the settings.",MSG_E);}
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
        if(tglGeneralPiano.isSelected() || tglGeneralBass.isSelected() || tglGeneralDrums.isSelected()){
            if(edtGeneralTempo.getText().matches(REG_TEMPO)){
                if(edtGeneralTone.getText().matches(REG_TONE)){
                    if(edtGeneralRepeat.getText().matches(REG_NUMBER)){
                        if(chbPatternChordcomplexity.getValue() != null) {
                            if(chbPatternChordgroups.getValue() != null) {
                                Boolean instruments[] = new Boolean[3];
                                int tempo = Integer.parseInt(edtGeneralTempo.getText());
                                int repeat = Integer.parseInt((edtGeneralRepeat.getText()));
                                String tone = edtGeneralTone.getText();
                                instruments[0] = tglGeneralPiano.isSelected();
                                instruments[1] = tglGeneralBass.isSelected();
                                instruments[2] = tglGeneralDrums.isSelected();
                                ArrayList<Patternelement> pattern = new ArrayList<Patternelement>();
                                bt.createBackingtrack(instruments, tempo, tone, repeat, pattern);
                                msg("Composition created successfully.", MSG_S);
                            } else {msg(error + "No chordgroup selected.", MSG_E);}
                        } else {msg(error + "No chordcomplexity selected.", MSG_E);}
                    } else {msg(error + "Repeat is not a number greater than zero.", MSG_E);}
                } else {msg(error + "Tone is not valid.", MSG_E);}
            } else {msg(error + "Tempo is not a number.", MSG_E);}
        } else {msg(error + "No instruments active.", MSG_E);}
    }

    /*************************************** GENERAL ******************************************************************/

    @FXML
    public void onGeneralPiano(ActionEvent actionEvent) {
        if(tglGeneralPiano.isSelected()){
            msg("Piano activated.", MSG_I);
        } else {
            msg("Piano disabled.", MSG_W);
        }
    }

    @FXML
    public void onGeneralBass(ActionEvent actionEvent) {
        if(tglGeneralBass.isSelected()){
            msg("Bass activated.", MSG_I);
        } else {
            msg("Bass disabled.", MSG_W);
        }
    }

    @FXML
    public void onGeneralDrums(ActionEvent actionEvent) {
        if(tglGeneralDrums.isSelected()){
            msg("Drums activated.", MSG_I);
        } else {
            msg("Drums disabled.", MSG_W);
        }
    }

    @FXML
    public void onGeneralTempo(ActionEvent actionEvent) {
        msg("Tempo set to " + edtGeneralTempo.getText() + ".", MSG_I);
    }

    @FXML
    public void onGeneralTone(ActionEvent actionEvent) {
        msg("Tone set to " + edtGeneralTone.getText() + ".", MSG_I);
    }

    @FXML
    public void onGeneralRepeat(ActionEvent actionEvent) {
        msg("Repeat set to " + edtGeneralRepeat.getText() + " times.", MSG_I);
    }

    /********************************* PATTERN ************************************************************************/

    @FXML
    public void changePatternTransposeCellEvent(TableColumn.CellEditEvent<Patternelement, Integer> patternelementIntegerCellEditEvent) {
    }

    @FXML
    public void changePatternModeCellEvent(TableColumn.CellEditEvent<MusicStructure, String> musicStructureStringCellEditEvent) {
    }

    @FXML
    public void changePatternChordgroupCellEvent(TableColumn.CellEditEvent<Patternelement, String> patternelementStringCellEditEvent) {
    }

    @FXML
    public void changePatternChordCellEvent(TableColumn.CellEditEvent<Patternelement, String> patternelementStringCellEditEvent) {
    }

    @FXML
    public void changePatternChordcomplexityCellEvent(TableColumn.CellEditEvent<Patternelement, String> patternelementStringCellEditEvent) {
    }

    @FXML
    public void onPatternAdd(ActionEvent actionEvent) {
        String error = "Adding Patternelement unsuccessful: ";
        if(edtPatternTranspose.getText().matches(REG_TRANSPOSE)){
            if(chbPatternChordgroups != null && chbPatternChord != null && chbPatternChordcomplexity != null){
                int transpose = Integer.parseInt(edtPatternTranspose.getText());
                //Patternelement patternelement = new Patternelement();
                // TODO Chords müssen nach Chordgroup geladen werden, sowohl nach Change- als auch bei Add-Formular
                // TODO Chordgroup muss nicht in Patternelement gespeichert werden → Redundant, da Chord.groupname
                // TODO Fehlerhandling Add
                // TODO Erzeugen und hinzufügen eines Patternelements
            }
        }
    }

    @FXML
    public void onPatternDelete(ActionEvent actionEvent) {
    }

    @FXML
    public void onPatternPlay(ActionEvent actionEvent) {
    }

}
