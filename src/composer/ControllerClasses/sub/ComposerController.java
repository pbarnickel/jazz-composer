/*
    Description:    Controller for Composer-UI.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ControllerClasses.sub;

import composer.DataClasses.Composer;
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
import java.util.*;

import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class ComposerController extends Controller {

    private Composer composer;
    private String error = "Composition unsuccessful. ";
    private boolean tactProp;

    //Common
    @FXML private Label lblOut;

    //General
    @FXML private TextField edtGeneralTempo;
    @FXML private TextField edtGeneralTone;
    @FXML private TextField edtGeneralRepeat;
    @FXML private Slider sldGeneralHumanizer;
    @FXML private Slider sldGeneralDynamic;

    //Pattern
    @FXML private TableView<Patternelement> tblPattern;
    @FXML private TableColumn<Patternelement, String> colPatternOrder;
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

    //Backingtrack
    @FXML private ToggleButton tglBackingtrackPiano;
    @FXML private ToggleButton tglBackingtrackBass;
    @FXML private ToggleButton tglBackingtrackDrums;
    @FXML private Slider sldBackingtrackDeviation;
    @FXML private Slider sldBackingtrackWalkingBass;

    //Melody
    @FXML private ToggleButton tglMelodyMelody;
    @FXML private Slider sldMelodyInversion;
    @FXML private Slider sldMelodySortOfPitches;
    @FXML private Slider sldMelodyJumper;
    @FXML private Slider sldMelodyBebop;
    @FXML private ChoiceBox chbMelodyMajorScalegroup;
    @FXML private ChoiceBox chbMelodyMajorScale;
    @FXML private ChoiceBox chbMelodyMinorScalegroup;
    @FXML private ChoiceBox chbMelodyMinorScale;

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
        scales = FXCollections.observableArrayList();
        chordsAsString = FXCollections.observableArrayList();
        allChords = FXCollections.observableArrayList(getAllItems(settings.getChordgroups()));
        allChordgroups = FXCollections.observableArrayList(settings.getChordgroups());
        allScalegroups = FXCollections.observableArrayList(settings.getScalegroups());
        allChordgroupsAsString = FXCollections.observableArrayList(getStrings(settings.getChordgroups()));
        allScalegroupsAsString = FXCollections.observableArrayList(getStrings(settings.getScalegroups()));
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
        chbMelodyMajorScalegroup.setItems(allScalegroupsAsString);
        chbMelodyMinorScalegroup.setItems(allScalegroupsAsString);
        chbMelodyMajorScalegroup.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                int indexOfGroup = settings.getIndexOfMusicElement(settings.getScalegroups(), newValue.toString());
                scales = FXCollections.observableArrayList(settings.getScalegroups().get(indexOfGroup).getMusicStructures());
                scalesAsString = FXCollections.observableArrayList(getStrings(settings.getScalegroups().get(indexOfGroup).getMusicStructures()));
                chbMelodyMajorScale.setItems(scalesAsString);
            }
        });
        chbMelodyMinorScalegroup.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                int indexOfGroup = settings.getIndexOfMusicElement(settings.getScalegroups(), newValue.toString());
                scales = FXCollections.observableArrayList(settings.getScalegroups().get(indexOfGroup).getMusicStructures());
                scalesAsString = FXCollections.observableArrayList(getStrings(settings.getScalegroups().get(indexOfGroup).getMusicStructures()));
                chbMelodyMinorScale.setItems(scalesAsString);
            }
        });
        tglBtnPatternTactPropFull.setUserData("Full");
        tglBtnPatternTactPropSemi.setUserData("Semi");

        //set up columns in table
        colPatternOrder.setCellValueFactory(new PropertyValueFactory<Patternelement, String>("orderAsString"));
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
        colPatternOrder.setCellFactory(TextFieldTableCell.forTableColumn());
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
        tglBackingtrackPiano.setSelected(true);
        tglBackingtrackBass.setSelected(true);
        tglBackingtrackDrums.setSelected(true);
        edtGeneralTempo.setText("140");
        edtGeneralTone.setText("C");
        edtGeneralRepeat.setText("3");
        allPatternelements.add(
                new Patternelement(
                        0,
                        0,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(4),
                        settings.getChordcomplexities().get(3),
                        "Full"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        1,
                        2,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(5),
                        settings.getChordcomplexities().get(3),
                        "Full"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        2,
                        4,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(5),
                        settings.getChordcomplexities().get(3),
                        "Full"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        3,
                        -3,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(32),
                        settings.getChordcomplexities().get(3),
                        "Full"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        4,
                        2,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(12),
                        settings.getChordcomplexities().get(3),
                        "Full"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        5,
                        -5,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(15),
                        settings.getChordcomplexities().get(3),
                        "Full"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        6,
                        0,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(4),
                        settings.getChordcomplexities().get(3),
                        "Full"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        7,
                        2,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(5),
                        settings.getChordcomplexities().get(3),
                        "Semi"
                )
        );
        allPatternelements.add(
                new Patternelement(
                        8,
                        -5,
                        settings.getChordgroups().get(0),
                        settings.getChordgroups().get(0).getMusicStructures().get(15),
                        settings.getChordcomplexities().get(3),
                        "Semi"
                )
        );
        double[] val = new double[]{100,0,0,0,0,0,0,0};
        for(int i=0; i<8; i++)sldSwing.get(i).setValue(val[i]);
        sldGeneralHumanizer.setValue(20);
        sldGeneralDynamic.setValue(40);
        tglMelodyMelody.setSelected(true);
        chbMelodyMajorScalegroup.setValue(settings.getScalegroups().get(0).getName());
        chbMelodyMinorScalegroup.setValue(settings.getScalegroups().get(0).getName());
        chbMelodyMajorScale.setValue(settings.getScalegroups().get(0).getMusicStructures().get(0).getName());
        chbMelodyMinorScale.setValue(settings.getScalegroups().get(0).getMusicStructures().get(0).getName());
        //for(int i=0; i<8; i++)sldSwing.get(i).setValue(new Random().nextInt(100));
    }

    /*********************************** Common methods ****************************************/

    @Override
    //Writes a message in output-label
    public void msg(String message, int type) {
        this.r = new Response(message, type, lblOut);
    }

    //Returns to menu
    public void gotoMenu(ActionEvent actionEvent) throws IOException {
        if(composer != null && composer.getSequencer().isRunning()){
            composer.getSequencer().stop();
            composer.getSequencer().close();
        }
        changeScene("menu", actionEvent);
    }

    //Updates Composer-UI after reading a BJC-Project-File
    public void updateComposerUI(){
        //General
        edtGeneralTempo.setText(Integer.toString(composer.getGeneral().getTempo()));
        edtGeneralTone.setText(settings.getToneByPitch(composer.getGeneral().getTone().getPitch()));
    }

    /****************************** File methods ************************************************/

    //Exports a composition as MIDI-File
    public void onExport(ActionEvent actionEvent){
        if(composer != null) {
            if(new File(settings.getDefault_location()).exists()) {
                File selectedFile = chooseFile("Choose place for saving File", actionEvent, false, "MIDI-File", "*.mid");
                if (selectedFile != null) {
                    composer.writeScoreinMIDI(selectedFile.getPath());
                    msg(selectedFile.getName() + " saved.", MSG_S);
                } else {msg("File could not be saved.", MSG_E);}
            } else {msg("Default path not valid. Change this in the settings.",MSG_E);}
        } else msg("No Composition to export.", MSG_E);
    }

    //Opens BJC-Project
    public void onOpen(ActionEvent actionEvent) throws MidiUnavailableException {
        if(new File(settings.getDefault_location()).exists()) {
            File selectedFile = chooseFile("Choose the BJC-File to open", actionEvent, true, "BJC-Project-File", "*.bjc");
            if (selectedFile != null) {
                composer = new Composer();
                composer.readBJCProjectFile(selectedFile.getPath());
                msg(selectedFile.getName() + " is not a valid BJC-Project-File.",MSG_E);
            } else {msg("File could not be loaded.", MSG_E);}
        } else {msg("Default path not valid. Change this in the settings.",MSG_E);}
    }

    //Saves a composition as BJC-Project
    public void onSave(ActionEvent actionEvent){
        if(composer != null){
            if(new File(settings.getDefault_location()).exists()) {
                File selectedFile = chooseFile("Choose place for saving BJC-Project-File", actionEvent, false, "BJC-Project-File", "*.bjc");
                if(selectedFile != null){
                    composer.writeBJCProjectFile(selectedFile.getPath());
                    msg(selectedFile.getName() + " saved.", MSG_S);
                } else msg("File could not be saved.", MSG_E);
            } else msg("Default path not valid. Change this in the settings.", MSG_E);
        } else msg("No composition to save.", MSG_E);
    }

    /****************************** Audio-Player, Statistics, ... *********************************/

    //Plays composition
    public void onPlay(ActionEvent actionEvent) throws MidiUnavailableException, InvalidMidiDataException, IOException {
        if(composer != null && composer.getScore().getSize() > 0){
            composer.playScore();
        } else msg("No composition to play.",MSG_E);
    }

    //Pauses playing composition
    public void onPause(ActionEvent actionEvent) {
        if(composer.getSequencer().isRunning()){
            composer.pauseScore();
        } else msg("No composition playing.",MSG_E);
    }

    //Stops playing composition
    public void onStop(ActionEvent actionEvent) {
        if(composer.getSequencer().isOpen()){
            composer.stopScore();
        } else msg("No composition to stop.",MSG_E);
    }

    //Shows statistics
    public void onStatistics(ActionEvent actionEvent) {
        if(composer != null){
            composer.showStatistics();
        } else msg("No composition-statistics.",MSG_E);
    }

    //Views JMusic composition-overview
    public void onView(ActionEvent actionEvent) {
        if(composer != null)composer.showScore();
        else msg("No composition to view.",MSG_E);
    }

    //Clears composition
    public void onClear(ActionEvent actionEvent){
        composer.initScore();
        msg("Composition cleared.",MSG_I);
    }

    /******************************* COMPOSE ******************************************/

    //Composes a Jazz-composition by user input
    public void onCompose(ActionEvent actionEvent) throws MidiUnavailableException {

        if(validateGeneral() && validatePattern() && validateSwing() && (validateMelody() || validateBackingtrack())){
            msg("Composition successful.",MSG_S);

            if(composer != null && composer.getSequencer().isRunning())
                composer.stopScore();

            //General
            int tempo = Integer.parseInt(edtGeneralTempo.getText());
            Tone tone = settings.getToneByString(edtGeneralTone.getText());
            int repeat = Integer.parseInt(edtGeneralRepeat.getText());
            double humanizerTolerance = sldGeneralHumanizer.getValue();
            double dynamic = sldGeneralDynamic.getValue();
            General general = new General(tempo, repeat, humanizerTolerance, dynamic, tone);

            //Pattern
            ArrayList<Patternelement> patternelements = new ArrayList<Patternelement>(tblPattern.getItems());
            Pattern pattern = new Pattern(patternelements);

            //Backingtrack
            boolean piano = tglBackingtrackPiano.isSelected();
            boolean bass = tglBackingtrackBass.isSelected();
            boolean drums = tglBackingtrackDrums.isSelected();
            int deviation = (int) (sldBackingtrackDeviation.getValue()/100 * 12);
            double walkingBass = sldBackingtrackWalkingBass.getValue();
            Backingtrack backingtrack = new Backingtrack(piano, bass, drums, deviation, walkingBass);

            //Melody
            boolean state = tglMelodyMelody.isSelected();
            Melody melody;
            if(state) {
                double inversion = sldMelodyInversion.getValue();
                double sortOfPitches = sldMelodySortOfPitches.getValue();
                double jumper = sldMelodyJumper.getValue();
                double bebop = sldMelodyBebop.getValue();
                int indexGroup = settings.getIndexOfMusicElement(settings.getScalegroups(), chbMelodyMajorScalegroup.getValue().toString());
                int indexScale = settings.getIndexOfMusicElement(settings.getScalegroups().get(indexGroup).getMusicStructures(), chbMelodyMajorScale.getValue().toString());
                MusicStructure majorScale = settings.getScalegroups().get(indexGroup).getMusicStructures().get(indexScale);
                indexGroup = settings.getIndexOfMusicElement(settings.getScalegroups(), chbMelodyMajorScalegroup.getValue().toString());
                indexScale = settings.getIndexOfMusicElement(settings.getScalegroups().get(indexGroup).getMusicStructures(), chbMelodyMajorScale.getValue().toString());
                MusicStructure minorScale = settings.getScalegroups().get(indexGroup).getMusicStructures().get(indexScale);
                melody = new Melody(state, inversion, sortOfPitches, jumper, bebop, majorScale, minorScale);
            } else melody = new Melody(state);

            //Swing
            ArrayList<Eighth> eighths = getEighthsProbabilities();
            Swing swing = new Swing(eighths);

            composer = new Composer(general, pattern, backingtrack, melody, swing);

        } else msg(error + "Configuration incomplete or faulty",MSG_E);
    }

    /*************************************** GENERAL ******************************************************************/

    //Validates general configuration of create backingtrack
    public boolean validateGeneral(){
        if(edtGeneralTempo.getText().matches(REG_TEMPO) && edtGeneralTone.getText().matches(REG_TONE_EXTENDED)
           && edtGeneralRepeat.getText().matches(REG_NUMBER)) return true;
        else msg(error + "Tempo [0..n], Tone [C-B] or Repeat [1..n] not valid.",MSG_E);
        return false;
    }

    /********************************* PATTERN ************************************************************************/

    //Checks if elements are included in the pattern
    public boolean validatePattern(){
        if(allPatternelements.size()>0)return true;
        else msg(error + "No pattern configuration.", MSG_E);
        return false;
    }

    //If 'Semi' patternelement is added -> the next patternelement has to be 'Semi' too. This is implemented by disabling
    //the ToggleButton for 'Full'
    public void setTactProp(){
        tactProp = !tactProp;
        tglBtnPatternTactPropFull.setDisable(!tactProp);
    }

    //Changes the order attribute of patternelement
    public void changePatternOrderCellEvent(TableColumn.CellEditEvent<Patternelement, String> patternelementStringCellEditEvent) {
        String newOrder = patternelementStringCellEditEvent.getNewValue().toString();
        Patternelement patternelementSelected = tblPattern.getSelectionModel().getSelectedItem();
        if(newOrder.matches(REG_NUMBER_0)){
            int order = Integer.parseInt(newOrder);
            if(order < allPatternelements.size()){
                if(order > patternelementSelected.getOrder()){
                    allPatternelements.get(order).setOrder(order - 1);
                    patternelementSelected.setOrder(order);
                } else if(order < patternelementSelected.getOrder()){
                    allPatternelements.get(order).setOrder(order + 1);
                    patternelementSelected.setOrder(order);
                }
                tblPattern.refresh();
                msg("Order changed.",MSG_S);
            } else msg("Order could not be changed. Either not a valid number or not a possible order-number.",MSG_E);
        }
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
                int order = allPatternelements.size();
                int transpose = Integer.parseInt(edtPatternTranspose.getText());
                int indexGroup = settings.getIndexOfMusicElement(settings.getChordgroups(), chbPatternChordgroups.getValue().toString());
                int indexComplexity = settings.getIndexOfMusicElement(settings.getChordcomplexities(), chbPatternChordcomplexity.getValue().toString());
                int indexChord = settings.getIndexOfMusicElement(settings.getChordgroups().get(indexGroup).getMusicStructures(), chbPatternChord.getValue().toString());
                MusicStructureGroup chordgroup = settings.getChordgroups().get(indexGroup);
                MusicStructure chord = chordgroup.getMusicStructures().get(indexChord);
                Chordcomplexity chordcomplexity = settings.getChordcomplexities().get(indexComplexity);
                String tactProportion = tglGrpPatternTactProp.getSelectedToggle().getUserData().toString();
                if(tactProportion.equals("Semi"))setTactProp();
                Patternelement patternelement = new Patternelement(order, transpose, chordgroup, chord, chordcomplexity, tactProportion);
                allPatternelements.add(patternelement);
                edtPatternTranspose.clear();
            } else msg(error + "All fields are required.",MSG_E);
        } else {msg(error + "Transpose is not valid.",MSG_E);}
    }

    //Deletes a patternelement from pattern-list
    public void onPatternDelete(ActionEvent actionEvent) {
        Patternelement patternelement = tblPattern.getSelectionModel().getSelectedItem();
        if(patternelement != null){
            int order = patternelement.getOrder();
            int length = allPatternelements.size();
            for(int i=order+1; i<length; i++) allPatternelements.get(i).setOrder(i - 1);
            allPatternelements.remove(patternelement);
            tblPattern.refresh();
            msg("Patternelement deleted.", MSG_W);
        } else {msg("No Patternelement selected.", MSG_E);}
    }

    //Plays the chord of the patternelement
    public void onPatternPlay(ActionEvent actionEvent) {
        Patternelement patternelement = tblPattern.getSelectionModel().getSelectedItem();
        int rootPitch = (int) settings.getToneByString(edtGeneralTone.getText()).getPitch() + patternelement.getTranspose();
        if(patternelement != null)patternelement.getChord().play(rootPitch, true);
    }

    /*************************************** BACKINGTRACK ******************************************************************/

    //Validates backintrack configuration
    public boolean validateBackingtrack(){
        if(tglBackingtrackPiano.isSelected() || tglBackingtrackBass.isSelected() || tglBackingtrackDrums.isSelected()) return true;
        else msg(error + "No instruments active.", MSG_E);
        return false;
    }

    /******************************************* Melody ****************************************************************/

    //Validates melody configuration
    public boolean validateMelody(){
        if(tglMelodyMelody.isSelected()){
            if(chbMelodyMajorScalegroup.getValue() != null && chbMelodyMajorScale.getValue() != null
               && chbMelodyMinorScalegroup.getValue() != null && chbMelodyMinorScale.getValue() != null) {
                return true;
            }
        }
        return false;
    }

    /******************************************* SWING ****************************************************************/

    //Validates swing configuration
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