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
import java.util.ArrayList;

import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import jm.music.data.*;

public class BackingtrackController extends Controller {

    private Backingtrack bt = new Backingtrack();
    private String error = "Composition unsuccessful. ";

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
    @FXML private TableView<Patternelement> tblPattern;
    @FXML private TableColumn<Patternelement, String> colPatternTranspose;
    @FXML private TableColumn<Patternelement, String> colPatternMode;
    @FXML private TableColumn<Patternelement, String> colPatternChordgroup;
    @FXML private TableColumn<Patternelement, String> colPatternChord;
    @FXML private TableColumn<Patternelement, String> colPatternChordcomplexity;
    @FXML private TableColumn<Patternelement, String> colPatternUsage;
    @FXML private TableColumn<Patternelement, String> colPatternTactProp;
    @FXML private TextField edtPatternTranspose;
    @FXML private ChoiceBox chbPatternChordgroups;
    @FXML private ChoiceBox chbPatternChord;
    @FXML private ChoiceBox chbPatternChordcomplexity;
    @FXML private ToggleGroup tglGrpPatternTactProp;
    @FXML private ToggleButton tglBtnPatternTactPropSemi;
    @FXML private ToggleButton tglBtnPatternTactPropFull;

    public void initialize(){
        settings = new Settings();
        defaultInputs();
        update();
    }

    public void update(){
        //load lists
        chords = FXCollections.observableArrayList();
        chordsAsString = FXCollections.observableArrayList();
        allChords = FXCollections.observableArrayList(getAllItems(settings.getChordgroups()));
        allChordgroups = FXCollections.observableArrayList(settings.getChordgroups());
        allChordgroupsAsString = FXCollections.observableArrayList(getStrings(settings.getChordgroups()));
        allChordcomplexities = FXCollections.observableArrayList(settings.getChordcomplexities());
        allChordcomplexitiesAsString = FXCollections.observableArrayList(getStrings(settings.getChordcomplexities()));
        allPatternelements = FXCollections.observableArrayList();
        allPatternelements.add(new Patternelement(
                0,
                allChordgroups.get(0),
                allChordgroups.get(0).getMusicStructures().get(0),
                allChordcomplexities.get(0),
                "Semi")
        );
        allPatternelements.add(new Patternelement(
                1,
                allChordgroups.get(1),
                allChordgroups.get(1).getMusicStructures().get(0),
                allChordcomplexities.get(1),
                "Full")
        );
        allPatternelements.add(new Patternelement(
                2,
                allChordgroups.get(2),
                allChordgroups.get(2).getMusicStructures().get(0),
                allChordcomplexities.get(2),
                "Semi")
        );

        //init choiceboxes and togglegroups
        chbPatternChordgroups.setItems(allChordgroupsAsString);
        chbPatternChordcomplexity.setItems(allChordcomplexitiesAsString);
        chbPatternChordgroups.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                int indexOfGroup = settings.getIndexOfMusicElement(settings.getChordgroups(), newValue.toString());
                chords = FXCollections.observableArrayList(settings.getChordgroups().get(indexOfGroup).getMusicStructures());
                chordsAsString = FXCollections.observableArrayList(getStrings(settings.getChordgroups().get(indexOfGroup).getMusicStructures()));
                chbPatternChord.setItems(chordsAsString);
            }
        });
        tglBtnPatternTactPropFull.setUserData("Full");
        tglBtnPatternTactPropSemi.setUserData("Semi");

        //set up columns in table
        colPatternTranspose.setCellValueFactory(new PropertyValueFactory<Patternelement, String>("transposeAsString"));
        colPatternChordgroup.setCellValueFactory(new PropertyValueFactory<Patternelement, String>("groupName"));
        colPatternChord.setCellValueFactory(new PropertyValueFactory<Patternelement, String>("chordName"));
        colPatternChordcomplexity.setCellValueFactory(new PropertyValueFactory<Patternelement, String>("chordcomplexityName"));
        colPatternMode.setCellValueFactory(new PropertyValueFactory<Patternelement, String>("mode"));
        colPatternUsage.setCellValueFactory(new PropertyValueFactory<Patternelement, String>("usage"));
        colPatternTactProp.setCellValueFactory(new PropertyValueFactory<Patternelement, String>("tactProportion"));

        //load table
        tblPattern.setItems(allPatternelements);
        tblPattern.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Patternelement patternelement = tblPattern.getSelectionModel().getSelectedItem();
                chords = FXCollections.observableArrayList(patternelement.getChordgroup().getMusicStructures());
                chordsAsString = getStrings(patternelement.getChordgroup().getMusicStructures());
                colPatternChord.setCellFactory(ChoiceBoxTableCell.forTableColumn(chordsAsString));
            }
        });

        //editable table
        tblPattern.setEditable(true);
        colPatternTranspose.setCellFactory(TextFieldTableCell.forTableColumn());
        colPatternChordgroup.setCellFactory(ChoiceBoxTableCell.forTableColumn(allChordgroupsAsString));
        colPatternChordcomplexity.setCellFactory(ChoiceBoxTableCell.forTableColumn(allChordcomplexitiesAsString));
        colPatternTactProp.setCellFactory(TextFieldTableCell.forTableColumn());
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
        if(validateGeneral() && validatePattern()){
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
        } else {msg("Composition not successful. Configuration not completed.",MSG_E);}
    }

    /*************************************** GENERAL ******************************************************************/

    public boolean validateGeneral(){
        if(tglGeneralPiano.isSelected() || tglGeneralBass.isSelected() || tglGeneralDrums.isSelected()){
            if(edtGeneralTempo.getText().matches(REG_TEMPO) && edtGeneralTone.getText().matches(REG_TONE)
                    && edtGeneralRepeat.getText().matches(REG_NUMBER))return true;
            else msg(error + "Tempo [0..n], Tone [C-B] or Repeat [1..n] not valid.",MSG_E);
        } else msg(error + "No instruments active.", MSG_E);
        return false;
    }

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

    public boolean validatePattern(){
        if(allPatternelements.size()>0)return true;
        else return false;
    }

    @FXML
    public void changePatternTransposeCellEvent(TableColumn.CellEditEvent patternelementIntegerCellEditEvent) {
        String newTranspose = patternelementIntegerCellEditEvent.getNewValue().toString();
        Patternelement patternelementSelected = tblPattern.getSelectionModel().getSelectedItem();
        if(newTranspose.matches(REG_TRANSPOSE)) {
            patternelementSelected.setTranspose(Integer.parseInt(newTranspose));
            msg("Transpose changed.", MSG_S);
        } else {msg("Value not valid.", MSG_E);}
    }

    @FXML
    public void changePatternChordgroupCellEvent(TableColumn.CellEditEvent<Patternelement, String> patternelementStringCellEditEvent) {
        String newGroup = patternelementStringCellEditEvent.getNewValue().toString();
        int index = settings.getIndexOfMusicElement(settings.getChordgroups(), newGroup);
        Patternelement patternelement = tblPattern.getSelectionModel().getSelectedItem();
        patternelement.setChordgroup(settings.getChordgroups().get(index));
        chords = FXCollections.observableArrayList(patternelement.getChordgroup().getMusicStructures());
        chordsAsString = FXCollections.observableArrayList(getStrings(patternelement.getChordgroup().getMusicStructures()));
        colPatternChord.setCellFactory(ChoiceBoxTableCell.forTableColumn(chordsAsString));
        tblPattern.refresh();
        msg("Chordgroup changed.",MSG_S);
    }

    @FXML
    public void changePatternChordCellEvent(TableColumn.CellEditEvent<Patternelement, String> patternelementStringCellEditEvent) {
        String newChord = patternelementStringCellEditEvent.getNewValue().toString();
        Patternelement patternelement = tblPattern.getSelectionModel().getSelectedItem();
        int index = settings.getIndexOfMusicElement(patternelement.getChordgroup().getMusicStructures(), newChord);
        patternelement.setChord(patternelement.getChordgroup().getMusicStructures().get(index));
        tblPattern.refresh();
        msg("Chord changed.",MSG_S);
    }

    @FXML
    public void changePatternChordcomplexityCellEvent(TableColumn.CellEditEvent<Patternelement, String> patternelementStringCellEditEvent) {
        String newComplexity = patternelementStringCellEditEvent.getNewValue().toString();
        int index = settings.getIndexOfMusicElement(settings.getChordcomplexities(), newComplexity);
        Patternelement patternelement = tblPattern.getSelectionModel().getSelectedItem();
        patternelement.setChordcomplexity(settings.getChordcomplexities().get(index));
        msg("Chordcomplexity changed.",MSG_S);
    }

    @FXML
    public void changePatternTactPropCellEvent(TableColumn.CellEditEvent<Patternelement, String> patternelementStringCellEditEvent) {
        String newTactProp = patternelementStringCellEditEvent.getNewValue();
        if(newTactProp.matches(REG_TACT_PROPORTION)){
            Patternelement patternelement = tblPattern.getSelectionModel().getSelectedItem();
            patternelement.setTactProportion(newTactProp);
            msg("Tact-Proportion changed.",MSG_S);
        } else {msg("Value not valid",MSG_E);}
    }

    @FXML
    public void onPatternAdd(ActionEvent actionEvent) {
        String error = "Adding Patternelement unsuccessful. ";
        if(edtPatternTranspose.getText().matches(REG_TRANSPOSE)){
            if(chbPatternChordgroups.getValue() != null && chbPatternChord.getValue() != null
                && chbPatternChordcomplexity.getValue() != null && tglGrpPatternTactProp.getSelectedToggle() != null){
                int transpose = Integer.parseInt(edtPatternTranspose.getText());
                int indexGroup = settings.getIndexOfMusicElement(settings.getChordgroups(), chbPatternChordgroups.getValue().toString());
                int indexComplexity = settings.getIndexOfMusicElement(settings.getChordcomplexities(), chbPatternChordcomplexity.getValue().toString());
                int indexChord = settings.getIndexOfMusicElement(settings.getChordgroups().get(indexGroup).getMusicStructures(), chbPatternChord.getValue().toString());
                MusicStructureGroup chordgroup = settings.getChordgroups().get(indexGroup);
                MusicStructure chord = chordgroup.getMusicStructures().get(indexChord);
                Chordcomplexity chordcomplexity = settings.getChordcomplexities().get(indexComplexity);
                String tactProportion = tglGrpPatternTactProp.getSelectedToggle().getUserData().toString();
                Patternelement patternelement = new Patternelement(transpose, chordgroup, chord, chordcomplexity, tactProportion);
                allPatternelements.add(patternelement);
                edtPatternTranspose.clear();
            } else msg(error + "All fields are required.",MSG_E);
        } else {msg(error + "Transpose is not valid.",MSG_E);}
    }

    @FXML
    public void onPatternDelete(ActionEvent actionEvent) {
        Patternelement patternelement = tblPattern.getSelectionModel().getSelectedItem();
        if(patternelement != null){
            allPatternelements.remove(patternelement);
            msg("Patternelement deleted.", MSG_W);
        } else {msg("No Patternelement selected.", MSG_E);}
    }

    @FXML
    public void onPatternPlay(ActionEvent actionEvent) {
        Patternelement patternelement = tblPattern.getSelectionModel().getSelectedItem();
        if(patternelement != null)patternelement.getChord().play(true);
    }
}
