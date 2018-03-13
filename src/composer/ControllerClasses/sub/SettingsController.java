/*
    Description:    Controller for Settings-UI.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           22.02.2018
 */

package composer.ControllerClasses.sub;

import composer.ControllerClasses.Controller;
import composer.DataClasses.*;
import composer.DataClasses.Response;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsController extends Controller {

    public static final KeyCombination kcCtrlS = new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN);

    //Common
    @FXML private VBox vboxSettings;
    @FXML private Label lblOut;
    @FXML private TextField edtDefaultLocation;

    //Chords
    @FXML private TableView<MusicStructure> tblChords;
    @FXML private TableColumn<MusicStructure, String> colChordsName;
    @FXML private TableColumn<MusicStructure, String> colChordsUsage;
    @FXML private TableColumn<MusicStructure, String> colChordsGroup;
    @FXML private TableColumn<MusicStructure, String> colChordsMode;
    @FXML private TextField edtChordsName;
    @FXML private TextField edtChordsUsage;
    @FXML private ChoiceBox chbChordsGroup;

    //Chordgroups
    @FXML private TableView<MusicStructureGroup> tblChordgroups;
    @FXML private TableColumn<MusicStructureGroup, String> colChordgroupsName;
    @FXML private TableColumn<MusicStructureGroup, Integer> colChordgroupsNr;
    @FXML private TextField edtChordgroupsName;

    //Chordcomplexity
    @FXML private TableView<Chordcomplexity> tblChordcomplexity;
    @FXML private TableColumn<Chordcomplexity, String> colChordcomplexityName;
    @FXML private TableColumn<Chordcomplexity, String> colChordcomplexityMin;
    @FXML private TableColumn<Chordcomplexity, String> colChordcomplexityMax;
    @FXML private TextField edtChordcomplexityName;
    @FXML private TextField edtChordcomplexityMin;
    @FXML private TextField edtChordcomplexityMax;

    //Scales
    @FXML private TableView<MusicStructure> tblScales;
    @FXML private TableColumn<MusicStructure, String> colScalesName;
    @FXML private TableColumn<MusicStructure, String> colScalesUsage;
    @FXML private TableColumn<MusicStructure, String> colScalesGroup;
    @FXML private TableColumn<MusicStructure, String> colScalesMode;
    @FXML private TextField edtScalesName;
    @FXML private TextField edtScalesUsage;
    @FXML private ChoiceBox chbScalesGroup;

    //Scalegroups
    @FXML private TableView<MusicStructureGroup> tblScalegroups;
    @FXML private TableColumn<MusicStructureGroup, String> colScalegroupsName;
    @FXML private TableColumn<MusicStructureGroup, Integer> colScalegroupsNr;
    @FXML private TextField edtScalegroupsName;

    private String callSave = " Please save the settings.";

    public void initialize() {
        vboxSettings.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(kcCtrlS.match(event)){onSave(new ActionEvent());}
            }
        });
        settings = new Settings();
        update();
    }

    @Override
    public void msg(String message, int type) {
        this.r = new Response(message, type, lblOut);
    }

    @Override
    public void defaultInputs(){}

    //Back to Menu
    @FXML
    public void gotoMenu(ActionEvent actionEvent) throws IOException {
        changeScene("menu", actionEvent);
    }

    @FXML
    public void onSave(ActionEvent actionEvent) {
        settings.saveSettings();
        msg("Settings saved.", MSG_S);
    }

    @FXML
    public void onDefault(ActionEvent actionEvent) throws IOException {
        try {
            settings.loadDefaultSettings();
            initialize();
            msg("Default settings loaded and saved.", MSG_S);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onFiles(ActionEvent actionEvent) {
        if(new File(settings.getDefault_location()).exists()) {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose default location for project files");
            chooser.setInitialDirectory(new File(settings.getDefault_location()));
            Stage stg = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            File dir = chooser.showDialog(stg);
            if (dir != null) {
                settings.setDefault_location(dir.getPath());
                edtDefaultLocation.setText(dir.getPath());
                msg(dir.getPath() + " selected." + callSave, MSG_W);
            } else {msg("No directory selected.", MSG_E);}
        } else { msg("Default directory not valid. Change it manually in the edit-field.",MSG_E);}
    }

    @FXML
    public void onFilesEdt(ActionEvent actionEvent) {
        String path = edtDefaultLocation.getText();
        if (new File(path).exists()) {
            settings.setDefault_location(path);
            msg(path + " selected." + callSave, MSG_W);
        } else {
            edtDefaultLocation.setText(settings.getDefault_location());
            msg("Path is not valid.", MSG_E);
        }
    }

    public void update(){
        //load lists
        allChords = FXCollections.observableArrayList(getAllItems(settings.getChordgroups()));
        allScales = FXCollections.observableArrayList(getAllItems(settings.getScalegroups()));
        allChordgroups = FXCollections.observableArrayList(settings.getChordgroups());
        allScalegroups = FXCollections.observableArrayList(settings.getScalegroups());
        allChordcomplexities = FXCollections.observableArrayList(settings.getChordcomplexities());
        allChordgroupsAsString = FXCollections.observableArrayList(getStrings(settings.getChordgroups()));
        allScalegroupsAsString = FXCollections.observableArrayList(getStrings(settings.getScalegroups()));
        allChordcomplexitiesAsString = FXCollections.observableArrayList(getStrings(settings.getChordcomplexities()));

        //set up columns in table
        colChordsName.setCellValueFactory(new PropertyValueFactory<MusicStructure, String>("name"));
        colChordsUsage.setCellValueFactory(new PropertyValueFactory<MusicStructure, String>("usageAsString"));
        colChordsGroup.setCellValueFactory(new PropertyValueFactory<MusicStructure, String>("group"));
        colChordsMode.setCellValueFactory(new PropertyValueFactory<MusicStructure, String>("mode"));
        colChordgroupsName.setCellValueFactory(new PropertyValueFactory<MusicStructureGroup, String>("name"));
        colChordgroupsNr.setCellValueFactory(new PropertyValueFactory<MusicStructureGroup, Integer>("nrOfMusicStructures"));
        colChordcomplexityName.setCellValueFactory(new PropertyValueFactory<Chordcomplexity, String>("name"));
        colChordcomplexityMin.setCellValueFactory(new PropertyValueFactory<Chordcomplexity, String>("minAsString"));
        colChordcomplexityMax.setCellValueFactory(new PropertyValueFactory<Chordcomplexity, String>("maxAsString"));
        colScalesName.setCellValueFactory(new PropertyValueFactory<MusicStructure, String>("name"));
        colScalesUsage.setCellValueFactory(new PropertyValueFactory<MusicStructure, String>("usageAsString"));
        colScalesGroup.setCellValueFactory(new PropertyValueFactory<MusicStructure, String>("group"));
        colScalesMode.setCellValueFactory(new PropertyValueFactory<MusicStructure, String>("mode"));
        colScalegroupsName.setCellValueFactory(new PropertyValueFactory<MusicStructureGroup, String>("name"));
        colScalegroupsNr.setCellValueFactory(new PropertyValueFactory<MusicStructureGroup, Integer>("nrOfMusicStructures"));

        //load data
        edtDefaultLocation.setText(settings.getDefault_location());
        tblChords.setItems(allChords);
        tblScales.setItems(allScales);
        chbChordsGroup.setItems(allChordgroupsAsString);
        chbScalesGroup.setItems(allScalegroupsAsString);
        tblChordgroups.setItems(allChordgroups);
        tblScalegroups.setItems(allScalegroups);
        tblChordcomplexity.setItems(allChordcomplexities);

        //editable table
        tblChords.setEditable(true);
        colChordsName.setCellFactory(TextFieldTableCell.forTableColumn());
        colChordsUsage.setCellFactory(TextFieldTableCell.forTableColumn());
        colChordsGroup.setCellFactory(ChoiceBoxTableCell.forTableColumn(allChordgroupsAsString));
        tblChordgroups.setEditable(true);
        colChordgroupsName.setCellFactory(TextFieldTableCell.forTableColumn());
        tblChordcomplexity.setEditable(true);
        colChordcomplexityName.setCellFactory(TextFieldTableCell.forTableColumn());
        colChordcomplexityMin.setCellFactory(TextFieldTableCell.forTableColumn());
        colChordcomplexityMax.setCellFactory(TextFieldTableCell.forTableColumn());
        tblScales.setEditable(true);
        colScalesName.setCellFactory(TextFieldTableCell.forTableColumn());
        colScalesUsage.setCellFactory(TextFieldTableCell.forTableColumn());
        colScalesGroup.setCellFactory(ChoiceBoxTableCell.forTableColumn(allScalegroupsAsString));
        tblScalegroups.setEditable(true);
        colScalegroupsName.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    /********************************************CHORDS****************************************************************/

    @FXML
    public void changeChordsNameCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newName = cellEdited.getNewValue().toString();
        MusicStructure chordSelected = tblChords.getSelectionModel().getSelectedItem();
        int indexOfGroup = settings.getIndexOfMusicElement(settings.getChordgroups(), chordSelected.getGroup());
        if(newName.matches(REG_CHORD_NAME) && settings.isNameUnique(settings.getChordgroups().get(indexOfGroup).getMusicStructures(), newName)){
            chordSelected.setName(newName);
            msg("Value changed." + callSave, MSG_W);
        } else {msg("Name not valid or already used.", MSG_E);}
    }

    @FXML
    public void changeChordsUsageCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newUsage = cellEdited.getNewValue().toString();
        MusicStructure chordSelected = tblChords.getSelectionModel().getSelectedItem();
        if(newUsage.matches(REG_CHORD_USAGE)) {
            chordSelected.setUsage(getUsageAsArray(newUsage));
            tblChords.refresh();
            msg("Value changed." + callSave, MSG_W);
        } else {msg("Value not valid.", MSG_E);}
    }

    @FXML
    public void changeChordsGroupCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newGroup = cellEdited.getNewValue().toString();
        int indexOldGroup = settings.getIndexOfMusicElement(settings.getChordgroups(), cellEdited.getOldValue().toString());
        int indexNewGroup = settings.getIndexOfMusicElement(settings.getChordgroups(), newGroup);
        MusicStructure chordSelected = tblChords.getSelectionModel().getSelectedItem();
        if(settings.isNameUnique(settings.getChordgroups().get(indexNewGroup).getMusicStructures(), chordSelected.getName())) {
            //remove
            allChords.remove(chordSelected);
            settings.getChordgroups().get(indexOldGroup).delMusicStructure(chordSelected);
            //add
            chordSelected.setGroup(newGroup);
            allChords.add(chordSelected);
            settings.getChordgroups().get(indexNewGroup).addMusicStructure(chordSelected);
            tblChordgroups.refresh();
            msg("Value changed." + callSave, MSG_W);
        } else {msg(chordSelected.getName() + " exists already in " + newGroup + ".",MSG_E);}
    }

    @FXML
    public void onChordsAdd(ActionEvent actionEvent) {
        String error = "Chord could not be added. ";
        if(!edtChordsName.getText().isEmpty() && !edtChordsUsage.getText().isEmpty() && chbChordsGroup.getValue()!=null){
            String name = edtChordsName.getText();
            String usageText = edtChordsUsage.getText();
            String group = chbChordsGroup.getValue().toString();
            int indexOfGroup = settings.getIndexOfMusicElement(settings.getChordgroups(), group);
            if(name.matches(REG_CHORD_NAME) && settings.isNameUnique(settings.getChordgroups().get(indexOfGroup).getMusicStructures(), name)){
                if(usageText.matches(REG_CHORD_USAGE)){
                    ArrayList<Integer> usage = getUsageAsArray(usageText);
                    MusicStructure chord = new MusicStructure(name, usage, group);
                    settings.getChordgroups().get(indexOfGroup).addMusicStructure(chord);
                    tblChords.getItems().add(chord);
                    edtChordsName.clear();
                    edtChordsUsage.clear();
                    tblChordgroups.refresh();
                    msg("Chord " + name + " added." + callSave, MSG_W);
                } else {msg(error + "Usage not valid.",MSG_E);}
            } else {msg(error + "Name not valid or already used.",MSG_E);}
        } else {msg(error + "All fields are required.",MSG_E);}
    }

    @FXML
    public void onChordsDelete(ActionEvent actionEvent) {
        MusicStructure chord = tblChords.getSelectionModel().getSelectedItem();
        if(chord != null){
            int indexChordgroup = settings.getIndexOfMusicElement(settings.getChordgroups(), chord.getGroup());
            allChords.remove(chord);
            tblChordgroups.refresh();
            settings.getChordgroups().get(indexChordgroup).delMusicStructure(chord);
            msg("Chord deleted." + callSave, MSG_W);
        } else {msg("No chord selected.", MSG_E);}
    }

    public void onChordsPlay(ActionEvent actionEvent) {
        MusicStructure chord = tblChords.getSelectionModel().getSelectedItem();
        if(chord != null)chord.play(true);
        else msg("No chord selected.",MSG_E);
    }

    /********************************************CHORDGROUPS***********************************************************/

    @FXML
    public void changeChordgroupsNameCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newName = cellEdited.getNewValue().toString();
        MusicStructureGroup chordgroupSelected = tblChordgroups.getSelectionModel().getSelectedItem();
        if(newName.matches(REG_CHORD_NAME) && settings.isNameUnique(settings.getChordgroups(), newName)) {
            chordgroupSelected.setName(newName);
            chordgroupSelected.changeMusicStructureGroupInMusicStructures();
            msg("Value changed." + callSave, MSG_W);
        } else {msg("Name not valid or already used.", MSG_E);}
    }

    @FXML
    public void onChordgroupsAdd(ActionEvent actionEvent) {
        String error = "Chordgroup could not be added. ";
        String name = edtChordgroupsName.getText();
        if(name.matches(REG_NAME) && settings.isNameUnique(settings.getChordgroups(), name)){
            MusicStructureGroup chordgroup = new MusicStructureGroup(name);
            settings.getChordgroups().add(chordgroup);
            allChordgroups.add(chordgroup);
            allChordgroupsAsString.add(chordgroup.getName());
            edtChordgroupsName.clear();
            msg("Chordgroup " + name + " added." + callSave, MSG_W);
        } else {msg(error + "Name not valid or already used.",MSG_E);}
    }

    @FXML
    public void onChordgroupsDelete(ActionEvent actionEvent) {
        MusicStructureGroup chordgroup = tblChordgroups.getSelectionModel().getSelectedItem();
        if(chordgroup != null){
            allChordgroups.remove(chordgroup);
            allChordgroupsAsString.remove(chordgroup.getName());
            settings.delChordgroup(chordgroup);
            allChords = FXCollections.observableArrayList(getAllItems(settings.getChordgroups()));
            tblChords.setItems(allChords);
            msg("Chordgroup deleted." + callSave, MSG_W);
        } else {msg("No chordgroup selected.",MSG_E);}
    }

    /********************************************CHORDCOMPLEXITY*******************************************************/

    @FXML
    public void changeChordcomplexityNameCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newName = cellEdited.getNewValue().toString();
        Chordcomplexity chordcomplexitySelected = tblChordcomplexity.getSelectionModel().getSelectedItem();
        if(newName.matches(REG_NAME) && settings.isNameUnique(settings.getChordcomplexities(), newName)) {
            chordcomplexitySelected.setName(newName);
            msg("Value changed." + callSave, MSG_W);
        } else {msg("Name not valid or already used.", MSG_E);}
    }

    @FXML
    public void changeChordcomplexityMinCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newMin = cellEdited.getNewValue().toString();
        Chordcomplexity chordcomplexitySelected = tblChordcomplexity.getSelectionModel().getSelectedItem();
        if (newMin.matches(REG_NUMBER)) {
            chordcomplexitySelected.setMin(Integer.parseInt(newMin));
            msg("Value changed." + callSave, MSG_W);
        } else {msg("Value not valid.", MSG_E);}
    }

    @FXML
    public void changeChordcomplexityMaxCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newMax = cellEdited.getNewValue().toString();
        Chordcomplexity chordcomplexitySelected = tblChordcomplexity.getSelectionModel().getSelectedItem();
        if (newMax.matches(REG_NUMBER)) {
            chordcomplexitySelected.setMax(Integer.parseInt(newMax));
            msg("Value changed." + callSave, MSG_W);
        } else {msg("Value not valid.", MSG_E);}
    }

    @FXML
    public void onChordcomplexityAdd(ActionEvent actionEvent) {
        String error = "Chordcomplexity could not be added. ";
        String name = edtChordcomplexityName.getText();
        String min = edtChordcomplexityMin.getText();
        String max = edtChordcomplexityMax.getText();
        if(name.matches(REG_NAME) && settings.isNameUnique(settings.getChordcomplexities(), name)){
            if(min.matches(REG_NUMBER) && max.matches(REG_NUMBER)){
                Chordcomplexity chordcomplexity = new Chordcomplexity(name, Integer.parseInt(min), Integer.parseInt(max));
                settings.getChordcomplexities().add(chordcomplexity);
                allChordcomplexities.add(chordcomplexity);
                edtChordcomplexityName.clear();
                edtChordcomplexityMin.clear();
                edtChordcomplexityMax.clear();
                msg("Chordcomplexity added." + callSave, MSG_W);
            } else {msg("Min or Max are not valid.",MSG_E);}
        } else {msg(error + name + " is not valid or already used.",MSG_E);}
    }

    @FXML
    public void onChordcomplexityDelete(ActionEvent actionEvent) {
        Chordcomplexity chordcomplexity = tblChordcomplexity.getSelectionModel().getSelectedItem();
        if(chordcomplexity != null){
            allChordcomplexities.remove(chordcomplexity);
            settings.delChordcomplexity(chordcomplexity);
            msg("Complexity deleted." + callSave, MSG_W);
        } else {msg("No complexity selected.",MSG_E);}
    }

    /********************************************SCALES****************************************************************/

    @FXML
    public void changeScalesNameCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newName = cellEdited.getNewValue().toString();
        MusicStructure scaleSelected = tblScales.getSelectionModel().getSelectedItem();
        int indexOfGroup = settings.getIndexOfMusicElement(settings.getScalegroups(), scaleSelected.getGroup());
        if(newName.matches(REG_CHORD_NAME) && settings.isNameUnique(settings.getScalegroups().get(indexOfGroup).getMusicStructures(), newName)){
            scaleSelected.setName(newName);
            msg("Value changed." + callSave, MSG_W);
        } else {msg("Name not valid or already used.", MSG_E);}
    }

    @FXML
    public void changeScalesUsageCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newUsage = cellEdited.getNewValue().toString();
        MusicStructure scaleSelected = tblScales.getSelectionModel().getSelectedItem();
        if(newUsage.matches(REG_CHORD_USAGE)) {
            scaleSelected.setUsage(getUsageAsArray(newUsage));
            tblScales.refresh();
            msg("Value changed." + callSave, MSG_W);
        } else {msg("Value not valid.", MSG_E);}
    }

    @FXML
    public void changeScalesGroupCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newGroup = cellEdited.getNewValue().toString();
        int indexOldGroup = settings.getIndexOfMusicElement(settings.getScalegroups(), cellEdited.getOldValue().toString());
        int indexNewGroup = settings.getIndexOfMusicElement(settings.getScalegroups(), newGroup);
        MusicStructure scaleSelected = tblScales.getSelectionModel().getSelectedItem();
        if(settings.isNameUnique(settings.getScalegroups().get(indexNewGroup).getMusicStructures(), scaleSelected.getName())) {
            //remove
            allScales.remove(scaleSelected);
            settings.getScalegroups().get(indexOldGroup).delMusicStructure(scaleSelected);
            //add
            scaleSelected.setGroup(newGroup);
            allScales.add(scaleSelected);
            settings.getScalegroups().get(indexNewGroup).addMusicStructure(scaleSelected);
            tblScalegroups.refresh();
            msg("Value changed." + callSave, MSG_W);
        } else {msg(scaleSelected.getName() + " exists already in " + newGroup + ".",MSG_E);}
    }

    @FXML
    public void onScalesAdd(ActionEvent actionEvent) {
        String error = "Scale could not be added. ";
        if(!edtScalesName.getText().isEmpty() && !edtScalesUsage.getText().isEmpty() && chbScalesGroup.getValue()!=null){
            String name = edtScalesName.getText();
            String usageText = edtScalesUsage.getText();
            String group = chbScalesGroup.getValue().toString();
            int indexOfGroup = settings.getIndexOfMusicElement(settings.getScalegroups(), group);
            if(name.matches(REG_CHORD_NAME) && settings.isNameUnique(settings.getScalegroups().get(indexOfGroup).getMusicStructures(), name)){
                if(usageText.matches(REG_CHORD_USAGE)){
                    ArrayList<Integer> usage = getUsageAsArray(usageText);
                    MusicStructure scale = new MusicStructure(name, usage, group);
                    settings.getScalegroups().get(indexOfGroup).addMusicStructure(scale);
                    tblScales.getItems().add(scale);
                    edtScalesName.clear();
                    edtScalesUsage.clear();
                    tblScalegroups.refresh();
                    msg("Scale " + name + " added." + callSave, MSG_W);
                } else {msg(error + "Usage not valid.",MSG_E);}
            } else {msg(error + "Name not valid or already used.",MSG_E);}
        } else {msg(error + "All fields are required.",MSG_E);}
    }

    @FXML
    public void onScalesDelete(ActionEvent actionEvent) {
        MusicStructure scale = tblScales.getSelectionModel().getSelectedItem();
        if(scale != null){
            int indexScalegroup = settings.getIndexOfMusicElement(settings.getScalegroups(), scale.getGroup());
            allScales.remove(scale);
            settings.getScalegroups().get(indexScalegroup).delMusicStructure(scale);
            tblScalegroups.refresh();
            msg("Scale deleted." + callSave, MSG_W);
        } else {msg("No scale selected.", MSG_E);}
    }

    public void onScalesPlay(ActionEvent actionEvent) {
        MusicStructure scale = tblScales.getSelectionModel().getSelectedItem();
        if(scale != null)scale.play(false);
        else msg("No scale selected",MSG_E);
    }

    /********************************************SCALEGROUPS***********************************************************/

    @FXML
    public void changeScalegroupsNameCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newName = cellEdited.getNewValue().toString();
        MusicStructureGroup scalegroupSelected = tblScalegroups.getSelectionModel().getSelectedItem();
        if(newName.matches(REG_NAME) && settings.isNameUnique(settings.getScalegroups(), newName)) {
            scalegroupSelected.setName(newName);
            scalegroupSelected.changeMusicStructureGroupInMusicStructures();
            msg("Value changed." + callSave, MSG_W);
        } else {msg("Name not valid or already used.", MSG_E);}
    }

    @FXML
    public void onScalegroupsAdd(ActionEvent actionEvent) {
        String error = "Scalegroup could not be added. ";
        String name = edtScalegroupsName.getText();
        if(name.matches(REG_NAME) && settings.isNameUnique(settings.getScalegroups(), name)){
            MusicStructureGroup scalegroup = new MusicStructureGroup(name);
            settings.getScalegroups().add(scalegroup);
            allScalegroups.add(scalegroup);
            allScalegroupsAsString.add(scalegroup.getName());
            edtScalegroupsName.clear();
            msg("Scalegroup added." + callSave, MSG_W);
        } else {msg(error + name + " is not valid or is already used.",MSG_E);}
    }

    @FXML
    public void onScalegroupsDelete(ActionEvent actionEvent) {
        MusicStructureGroup scalegroup= tblScalegroups.getSelectionModel().getSelectedItem();
        if(scalegroup != null){
            allScalegroups.remove(scalegroup);
            allScalegroupsAsString.remove(scalegroup.getName());
            settings.delScalegroup(scalegroup);
            allScales = FXCollections.observableArrayList(getAllItems(settings.getScalegroups()));
            tblScales.setItems(allScales);
            msg("Scalegroup deleted." + callSave, MSG_W);
        } else {msg("No scalegroup selected.",MSG_E);}
    }
}