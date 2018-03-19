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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.DragEvent;
import jm.music.data.*;

public class BackingtrackController extends Controller {

    private Backingtrack backingtrack;
    private String error = "Composition unsuccessful. ";
    private boolean tactProp;

    //Common
    @FXML private Label lblOut;

    //General
    @FXML private ToggleButton tglGeneralPiano;
    @FXML private ToggleButton tglGeneralBass;
    @FXML private ToggleButton tglGeneralDrums;
    @FXML private TextField edtGeneralTempo;
    @FXML private TextField edtGeneralTone;
    @FXML private TextField edtGeneralRepeat;
    @FXML private Slider sldGeneralHumanizer;
    @FXML private Slider sldGeneralDeviation;

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

    //Swing
    private ArrayList<Slider> sldSwing;
    @FXML private Slider sldSwing1;
    @FXML private Slider sldSwing2;
    @FXML private Slider sldSwing3;
    @FXML private Slider sldSwing4;
    @FXML private Slider sldSwing5;
    @FXML private Slider sldSwing6;
    @FXML private Slider sldSwing7;
    @FXML private Slider sldSwing8;

    public void initialize(){
        //general
        settings = new Settings();
        tactProp = true;

        //load lists
        chords = FXCollections.observableArrayList();
        chordsAsString = FXCollections.observableArrayList();
        allChords = FXCollections.observableArrayList(getAllItems(settings.getChordgroups()));
        allChordgroups = FXCollections.observableArrayList(settings.getChordgroups());
        allChordgroupsAsString = FXCollections.observableArrayList(getStrings(settings.getChordgroups()));
        allChordcomplexities = FXCollections.observableArrayList(settings.getChordcomplexities());
        allChordcomplexitiesAsString = FXCollections.observableArrayList(getStrings(settings.getChordcomplexities()));
        allPatternelements = FXCollections.observableArrayList();

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

        //add sliders to slider array
        sldSwing = new ArrayList<Slider>(Arrays.asList(sldSwing1, sldSwing2, sldSwing3, sldSwing4, sldSwing5, sldSwing6, sldSwing7, sldSwing8));

        //load default user inputs
        defaultInputs();
    }

    //Default user inputs for faster testing
    @Override
    public void defaultInputs(){
        tglGeneralPiano.setSelected(true);
        edtGeneralTempo.setText("120");
        edtGeneralTone.setText("C");
        edtGeneralRepeat.setText("3");
        allPatternelements.add(
                new Patternelement(
                        0,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(4),
                        settings.getChordcomplexities().get(3),
                        "Full"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        2,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(5),
                        settings.getChordcomplexities().get(3),
                        "Full"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        4,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(5),
                        settings.getChordcomplexities().get(3),
                        "Full"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        -3,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(32),
                        settings.getChordcomplexities().get(3),
                        "Full"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        2,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(12),
                        settings.getChordcomplexities().get(3),
                        "Full"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        -5,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(15),
                        settings.getChordcomplexities().get(3),
                        "Full"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        0,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(4),
                        settings.getChordcomplexities().get(3),
                        "Full"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        2,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(5),
                        settings.getChordcomplexities().get(3),
                        "Semi"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        -5,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(15),
                        settings.getChordcomplexities().get(3),
                        "Semi"
                )
        );
        //double[] val = new double[]{100,100,0,0,0,0,0,0};
        for(int i=0; i<8; i++)sldSwing.get(i).setValue(new Random().nextInt(100));
        //for(int i=0; i<8; i++)sldSwing.get(i).setValue(val[i]);
    }

    @Override
    //Writes a message in output-label
    public void msg(String message, int type) {
        this.r = new Response(message, type, lblOut);
    }

    //Returns to menu
    public void gotoMenu(ActionEvent actionEvent) throws IOException {
        changeScene("menu", actionEvent);
    }

    //Views JMusic composition-overview
    public void onView(ActionEvent actionEvent) {
        if(backingtrack != null)backingtrack.showScore();
        else msg("No composition to view.",MSG_E);
    }

    //Clears composition
    public void onClear(ActionEvent actionEvent){
        msg("Composition cleared.",MSG_I);
    }

    //Opens a MIDI-composition
    public void onOpen(ActionEvent actionEvent){
        if(new File(settings.getDefault_location()).exists()) {
            File selectedFile = midiFileChooser("Choose the MIDI-File to open", actionEvent, true);
            if (selectedFile != null) {
                backingtrack = new Backingtrack();
                backingtrack.readMIDIinScore(selectedFile.getPath());
                msg(selectedFile.getName() + " loaded.", MSG_S);
            } else {msg("File could not be loaded.", MSG_E);}
        } else {msg("Default path not valid. Change this in the settings.",MSG_E);}
    }

    //Saves composed backingtrack as MIDI-File
    public void onSave(ActionEvent actionEvent){
        if(backingtrack != null) {
            if(new File(settings.getDefault_location()).exists()) {
                File selectedFile = midiFileChooser("Choose place for saving File", actionEvent, false);
                if (selectedFile != null) {
                    backingtrack.writeScoreinMIDI(selectedFile.getPath());
                    msg(selectedFile.getName() + " saved.", MSG_S);
                } else {msg("File could not be saved.", MSG_E);}
            } else {msg("Default path not valid. Change this in the settings.",MSG_E);}
        } else msg("No Backingtrack composed.", MSG_E);
    }

    //Plays composed backingtrack
    public void onPlay(ActionEvent actionEvent) {
        if(backingtrack != null && backingtrack.getScore().getSize() > 0){
            backingtrack.playScore();
            msg("Backing Track is played.",MSG_I);
        } else msg("No Backing Track to play.",MSG_E);
    }

    //Composes a backingtrack by user input
    public void onCompose(ActionEvent actionEvent) {
        if(validateGeneral() && validatePattern() && validateSwing()){
            Boolean instruments[] = new Boolean[3];
            int tempo = Integer.parseInt(edtGeneralTempo.getText());
            int repeat = Integer.parseInt((edtGeneralRepeat.getText()));
            double humanizerTolerance = sldGeneralHumanizer.getValue();
            int deviation = (int) (sldGeneralDeviation.getValue()/100 * 12);
            Tone tone = settings.getToneByString(edtGeneralTone.getText());
            instruments[0] = tglGeneralPiano.isSelected();
            instruments[1] = tglGeneralBass.isSelected();
            instruments[2] = tglGeneralDrums.isSelected();
            ArrayList<Patternelement> pattern = new ArrayList<Patternelement>(tblPattern.getItems());
            ArrayList<Eighth> eighths = getEighthsProbabilities();
            backingtrack = new Backingtrack(instruments, tempo, tone, repeat, pattern, humanizerTolerance, eighths, deviation);
            msg("Composition created successfully.", MSG_S);
        } else {msg("Composition not successful. Configuration not completed.",MSG_E);}
    }

    /*************************************** GENERAL ******************************************************************/

    //Validates general configuration of create backingtrack
    public boolean validateGeneral(){
        if(tglGeneralPiano.isSelected() || tglGeneralBass.isSelected() || tglGeneralDrums.isSelected()){
            if(edtGeneralTempo.getText().matches(REG_TEMPO) && edtGeneralTone.getText().matches(REG_TONE_EXTENDED)
                    && edtGeneralRepeat.getText().matches(REG_NUMBER))return true;
            else msg(error + "Tempo [0..n], Tone [C-B] or Repeat [1..n] not valid.",MSG_E);
        } else msg(error + "No instruments active.", MSG_E);
        return false;
    }

    /********************************* PATTERN ************************************************************************/

    //Checks if elements are included in the pattern
    public boolean validatePattern(){
        if(allPatternelements.size()>0)return true;
        else return false;
    }

    //If 'Semi' patternelement is added -> the next patternelement has to be 'Semi' too. This is implemented by disabling
    //the ToggleButton for 'Full'
    public void setTactProp(){
        tactProp = !tactProp;
        tglBtnPatternTactPropFull.setDisable(!tactProp);
    }

    //Changes the transpose attribute of patternelement
    public void changePatternTransposeCellEvent(TableColumn.CellEditEvent patternelementIntegerCellEditEvent) {
        String newTranspose = patternelementIntegerCellEditEvent.getNewValue().toString();
        Patternelement patternelementSelected = tblPattern.getSelectionModel().getSelectedItem();
        if(newTranspose.matches(REG_TRANSPOSE)) {
            patternelementSelected.setTranspose(Integer.parseInt(newTranspose));
            msg("Transpose changed.", MSG_S);
        } else {msg("Value not valid.", MSG_E);}
    }

    //Changes the chordgroup attribute of patternelement
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

    //Changes the chord attribute of patternelement
    public void changePatternChordCellEvent(TableColumn.CellEditEvent<Patternelement, String> patternelementStringCellEditEvent) {
        String newChord = patternelementStringCellEditEvent.getNewValue().toString();
        Patternelement patternelement = tblPattern.getSelectionModel().getSelectedItem();
        int index = settings.getIndexOfMusicElement(patternelement.getChordgroup().getMusicStructures(), newChord);
        patternelement.setChord(patternelement.getChordgroup().getMusicStructures().get(index));
        tblPattern.refresh();
        msg("Chord changed.",MSG_S);
    }

    //Changes the chordcomplexity attribute of patternelement
    public void changePatternChordcomplexityCellEvent(TableColumn.CellEditEvent<Patternelement, String> patternelementStringCellEditEvent) {
        String newComplexity = patternelementStringCellEditEvent.getNewValue().toString();
        int index = settings.getIndexOfMusicElement(settings.getChordcomplexities(), newComplexity);
        Patternelement patternelement = tblPattern.getSelectionModel().getSelectedItem();
        patternelement.setChordcomplexity(settings.getChordcomplexities().get(index));
        msg("Chordcomplexity changed.",MSG_S);
    }

    //Changes the tact-proportion attribute of patternelement
    public void changePatternTactPropCellEvent(TableColumn.CellEditEvent<Patternelement, String> patternelementStringCellEditEvent) {
        String newTactProp = patternelementStringCellEditEvent.getNewValue();
        if(newTactProp.matches(REG_TACT_PROPORTION)){
            Patternelement patternelement = tblPattern.getSelectionModel().getSelectedItem();
            patternelement.setTactProportion(newTactProp);
            msg("Tact-Proportion changed.",MSG_S);
        } else {msg("Value not valid",MSG_E);}
    }

    //Adds a patternelement to pattern-list
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
                if(tactProportion.equals("Semi"))setTactProp();
                Patternelement patternelement = new Patternelement(transpose, chordgroup, chord, chordcomplexity, tactProportion);
                allPatternelements.add(patternelement);
                edtPatternTranspose.clear();
            } else msg(error + "All fields are required.",MSG_E);
        } else {msg(error + "Transpose is not valid.",MSG_E);}
    }

    //Deletes a patternelement from pattern-list
    public void onPatternDelete(ActionEvent actionEvent) {
        Patternelement patternelement = tblPattern.getSelectionModel().getSelectedItem();
        if(patternelement != null){
            allPatternelements.remove(patternelement);
            msg("Patternelement deleted.", MSG_W);
        } else {msg("No Patternelement selected.", MSG_E);}
    }

    //Plays the chord of the patternelement
    public void onPatternPlay(ActionEvent actionEvent) {
        Patternelement patternelement = tblPattern.getSelectionModel().getSelectedItem();
        int rootPitch = (int) settings.getToneByString(edtGeneralTone.getText()).getPitch() + patternelement.getTranspose();
        if(patternelement != null)patternelement.getChord().play(rootPitch, true);
    }

    /******************************************* SWING ****************************************************************/

    //Validates general configuration of create backingtrack
    public boolean validateSwing(){
        int length = sldSwing.size();
        for (int i=0; i<length; i++){
            if(sldSwing.get(i).getValue()>0)return true;
        }
        return false;
    }

    //Reads EighthsProbabilities and adds them to probability-list
    public ArrayList<Eighth> getEighthsProbabilities(){
        ArrayList<Eighth> eighths = new ArrayList<Eighth>();
        int length = sldSwing.size();
        double sum = 0;
        double start = 0;
        double percentage;
        for (int i=0; i<length; i++)sum += sldSwing.get(i).getValue();
        for (int i=0; i<length; i++){
            //Checks if probability is > 0%
            if(sldSwing.get(i).getValue() > 0) {
                percentage = sldSwing.get(i).getValue() / sum * 100;
                Range range = new Range((int) start, (int) (start + percentage - 1));
                Eighth eighth = new Eighth(i, range);
                eighths.add(eighth);
                start += percentage;
            }
        }
        return eighths;
    }
}