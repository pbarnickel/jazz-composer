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
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsController extends Controller {

    @FXML private Label lblOut;
    @FXML private TextField edtDefaultLocation;
    @FXML private TabPane tbpSettings;
    @FXML private Tab tabChords;
    @FXML private Tab tabChordgroups;
    @FXML private Tab tabChordComplexity;
    @FXML private Tab tabScales;
    @FXML private Tab tabScalegroups;
    @FXML private TableView<MusicStructure> tblChords;
    @FXML private TableView<MusicStructureGroup> tblChordgroups;
    @FXML private TableView<Chordcomplexity> tblChordcomplexity;
    @FXML private TableView<MusicStructure> tblScales;
    @FXML private TableView<MusicStructureGroup> tblScalegroups;
    @FXML private TableColumn<MusicStructure, String> colChordsName;
    @FXML private TableColumn<MusicStructure, String> colChordsUsage;
    @FXML private TableColumn<MusicStructure, String> colChordsGroup;
    @FXML private TableColumn<MusicStructureGroup, String> colChordgroupsName;
    @FXML private TableColumn<MusicStructureGroup, Integer> colChordgroupsNr;
    @FXML private TableColumn<Chordcomplexity, String> colChordcomplexityTerm;
    @FXML private TableColumn<Chordcomplexity, String> colChordcomplexityMin;
    @FXML private TableColumn<Chordcomplexity, String> colChordcomplexityMax;
    @FXML private TableColumn<MusicStructure, String> colScalesName;
    @FXML private TableColumn<MusicStructure, String> colScalesUsage;
    @FXML private TableColumn<MusicStructure, String> colScalesGroup;
    @FXML private TableColumn<MusicStructureGroup, String> colScalegroupsName;
    @FXML private TableColumn<MusicStructureGroup, Integer> colScalegroupsNr;
    @FXML private ChoiceBox<String> chbChordsGroup;
    @FXML private ChoiceBox<String> chbScalesGroup;
    @FXML private TextField edtChordsName;
    @FXML private TextField edtChordsUsage;
    @FXML private TextField edtScalesName;
    @FXML private TextField edtScalesUsage;
    @FXML private TextField edtChordgroupsName;
    @FXML private TextField edtScalegroupsName;
    @FXML private TextField edtChordcomplexityTerm;
    @FXML private TextField edtChordcomplexityMin;
    @FXML private TextField edtChordcomplexityMax;

    private String callSave = " Please save the settings.";

    public void initialize() {
        //init settings
        settings = new Settings();

        //set up columns in table
        colChordsName.setCellValueFactory(new PropertyValueFactory<MusicStructure, String>("name"));
        colChordsUsage.setCellValueFactory(new PropertyValueFactory<MusicStructure, String>("usageAsString"));
        colChordsGroup.setCellValueFactory(new PropertyValueFactory<MusicStructure, String>("group"));
        colChordgroupsName.setCellValueFactory(new PropertyValueFactory<MusicStructureGroup, String>("name"));
        colChordgroupsNr.setCellValueFactory(new PropertyValueFactory<MusicStructureGroup, Integer>("nrOfMusicStructures"));
        colChordcomplexityTerm.setCellValueFactory(new PropertyValueFactory<Chordcomplexity, String>("term"));
        colChordcomplexityMin.setCellValueFactory(new PropertyValueFactory<Chordcomplexity, String>("minAsString"));
        colChordcomplexityMax.setCellValueFactory(new PropertyValueFactory<Chordcomplexity, String>("maxAsString"));
        colScalesName.setCellValueFactory(new PropertyValueFactory<MusicStructure, String>("name"));
        colScalesUsage.setCellValueFactory(new PropertyValueFactory<MusicStructure, String>("usageAsString"));
        colScalesGroup.setCellValueFactory(new PropertyValueFactory<MusicStructure, String>("group"));
        colScalegroupsName.setCellValueFactory(new PropertyValueFactory<MusicStructureGroup, String>("name"));
        colScalegroupsNr.setCellValueFactory(new PropertyValueFactory<MusicStructureGroup, Integer>("nrOfMusicStructures"));

        //load data
        edtDefaultLocation.setText(settings.getDefault_location());
        allChords = FXCollections.observableArrayList(getAllChords());
        allChordgroupsAsString = FXCollections.observableArrayList(getAllChordgroupsAsString());
        allChordgroups = FXCollections.observableArrayList(getAllChordgroups());
        allChordcomplexities = FXCollections.observableArrayList(getAllChordcomplexities());
        allScales = FXCollections.observableArrayList(getAllScales());
        allScalegroupsAsString = FXCollections.observableArrayList(getAllScalegroupsAsString());
        allScalegroups = FXCollections.observableArrayList(getAllScalegroups());
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
        colChordcomplexityTerm.setCellFactory(TextFieldTableCell.forTableColumn());
        colChordcomplexityMin.setCellFactory(TextFieldTableCell.forTableColumn());
        colChordcomplexityMax.setCellFactory(TextFieldTableCell.forTableColumn());
        tblScales.setEditable(true);
        colScalesName.setCellFactory(TextFieldTableCell.forTableColumn());
        colScalesUsage.setCellFactory(TextFieldTableCell.forTableColumn());
        colScalesGroup.setCellFactory(ChoiceBoxTableCell.forTableColumn(allScalegroupsAsString));
        tblScalegroups.setEditable(true);
        colScalegroupsName.setCellFactory(TextFieldTableCell.forTableColumn());
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
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose default location for project files");
        chooser.setInitialDirectory(new File(settings.getDefault_location()));
        Stage stg = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        File dir = chooser.showDialog(stg);
        if (dir != null) {
            settings.setDefault_location(dir.getPath());
            edtDefaultLocation.setText(dir.getPath());
            msg(dir.getPath() + " selected." + callSave, MSG_W);
        } else {
            msg("No directory selected.", MSG_E);
        }
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

    /********************************************CHORDS****************************************************************/

    @FXML
    public void changeChordsNameCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newName = cellEdited.getNewValue().toString();
        MusicStructure chordSelected = tblChords.getSelectionModel().getSelectedItem();
        int indexOfGroup = settings.getIndexOfGroup(settings.getChordgroups(), chordSelected.getGroup());
        if(settings.isStructureNameUnique(settings.getChordgroups().get(indexOfGroup).getMusicStructures(), newName)){
            if (newName.matches(REG_CHORD_NAME)) {
                chordSelected.setName(newName);
                msg("Value changed." + callSave, MSG_W);
            } else {
                tblChords.refresh();
                msg("Value not valid.", MSG_E);
            }
        } else {
            tblChords.refresh();
            msg(newName + " exists already in " + chordSelected.getGroup() + ".", MSG_E);
        }
    }

    @FXML
    public void changeChordsUsageCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newUsage = cellEdited.getNewValue().toString();
        MusicStructure chordSelected = tblChords.getSelectionModel().getSelectedItem();
        if(newUsage.matches(REG_CHORD_USAGE)) {
            chordSelected.setUsage(getUsageAsArray(newUsage));
            msg("Value changed." + callSave, MSG_W);
        } else {
            tblChords.refresh();
            msg("Value not valid.", MSG_E);
        }
    }

    @FXML
    public void changeChordsGroupCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newGroup = cellEdited.getNewValue().toString();
        int indexOldGroup = settings.getIndexOfGroup(settings.getChordgroups(), cellEdited.getOldValue().toString());
        int indexNewGroup = settings.getIndexOfGroup(settings.getChordgroups(), newGroup);
        MusicStructure chordSelected = tblChords.getSelectionModel().getSelectedItem();
        if(settings.isStructureNameUnique(settings.getChordgroups().get(indexNewGroup).getMusicStructures(), chordSelected.getName())) {
            //remove
            allChords.remove(chordSelected);
            settings.getChordgroups().get(indexOldGroup).delMusicStructure(chordSelected);
            //add
            chordSelected.setGroup(newGroup);
            allChords.add(chordSelected);
            settings.getChordgroups().get(indexNewGroup).addMusicStructure(chordSelected);
            msg("Value changed." + callSave, MSG_W);
        } else {
            tblChords.refresh();
            msg(chordSelected.getName() + " exists already in " + newGroup + ".",MSG_E);
        }
    }

    @FXML
    public void onChordsAdd(ActionEvent actionEvent) {
        String error = "Chord could not be added. ";
        String name = edtChordsName.getText();
        String usageText = edtChordsUsage.getText();
        String group = chbChordsGroup.getValue();
        int indexOfGroup = settings.getIndexOfGroup(settings.getChordgroups(), group);
        if(name.matches(REG_CHORD_NAME)){
            if(settings.isStructureNameUnique(settings.getChordgroups().get(indexOfGroup).getMusicStructures(), name)) {
                if (usageText.matches(REG_CHORD_USAGE)) {
                    ArrayList<Integer> usage = getUsageAsArray(usageText);
                    if (group != null) {
                        int lengthChordgroups = settings.getChordgroups().size();
                        for (int i = 0; i < lengthChordgroups; i++) {
                            if (group.equals(settings.getChordgroups().get(i).getName())) {
                                MusicStructure chord = new MusicStructure(name, usage, group);
                                settings.getChordgroups().get(i).addMusicStructure(chord);
                                tblChords.getItems().add(chord);
                                edtChordsName.clear();
                                edtChordsUsage.clear();
                                msg("Chord " + name + " added." + callSave, MSG_W);
                            }
                        }
                    } else {msg(error + "No group selected.", MSG_E);}
                } else {msg(error + "Usage is empty or not in the right format (Example: 0-2-7).", MSG_E);}
            } else {msg(error + name + " exists already in " + group + ".", MSG_E);}
        } else {msg(error + "Name is not valid.", MSG_E);}
    }

    @FXML
    public void onChordsDelete(ActionEvent actionEvent) {
        MusicStructure chord = tblChords.getSelectionModel().getSelectedItem();
        int indexChordgroup = settings.getIndexOfGroup(settings.getChordgroups(), chord.getGroup());
        if(chord != null){
            allChords.remove(chord);
            settings.getChordgroups().get(indexChordgroup).delMusicStructure(chord);
            msg("Chord deleted." + callSave, MSG_W);
        } else {
            msg("No chord selected.", MSG_E);
        }
    }

    /********************************************CHORDGROUPS***********************************************************/

    @FXML
    public void changeChordgroupsNameCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newName = cellEdited.getNewValue().toString();
        MusicStructureGroup chordgroupSelected = tblChordgroups.getSelectionModel().getSelectedItem();
        if(settings.isGroupNameUnique(settings.getChordgroups(), newName)) {
            if (newName.matches(REG_CHORD_NAME)) {
                chordgroupSelected.setName(newName);
                chordgroupSelected.changeMusicStructureGroupInMusicStructures();
                msg("Value changed." + callSave, MSG_W);
            } else {
                tblScalegroups.refresh();
                msg("Value not valid.", MSG_E);
            }
        } else {
            tblScalegroups.refresh();
            msg("There is already a chordgroup named " + newName + ".", MSG_E);
        }
    }

    @FXML
    public void onChordgroupsAdd(ActionEvent actionEvent) {
        String error = "Chordgroup could not be added. ";
        String name = edtChordgroupsName.getText();
        if(settings.isGroupNameUnique(settings.getChordgroups(), name)){
            if(name.matches(REG_NAME)){
                MusicStructureGroup chordgroup = new MusicStructureGroup();
                chordgroup.setName(name);
                settings.getChordgroups().add(chordgroup);
                allChordgroups.add(chordgroup);
                allChordgroupsAsString.add(chordgroup.getName());
                edtChordgroupsName.clear();
                msg("Chordgroup added." + callSave, MSG_W);
            } else {msg(error + name + " is not a valid name.",MSG_E);}
        } else {msg(error + "There is already a chordgroup named " + name + ".",MSG_E);}
    }

    @FXML
    public void onChordgroupsDelete(ActionEvent actionEvent) {
        MusicStructureGroup chordgroup = tblChordgroups.getSelectionModel().getSelectedItem();
        if(chordgroup != null){
            allChordgroups.remove(chordgroup);
            allChordgroupsAsString.remove(chordgroup.getName());
            settings.delChordgroup(chordgroup);
            msg("Chordgroup deleted." + callSave, MSG_W);
        } else {
            msg("No chordgroup selected.",MSG_E);
        }
    }

    /********************************************CHORDCOMPLEXITY*******************************************************/

    @FXML
    public void changeChordcomplexityTermCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newTerm = cellEdited.getNewValue().toString();
        Chordcomplexity chordcomplexitySelected = tblChordcomplexity.getSelectionModel().getSelectedItem();
        if(settings.isComplexityUnique(newTerm)) {
            if (newTerm.matches(REG_NAME)) {
                chordcomplexitySelected.setTerm(newTerm);
                msg("Value changed." + callSave, MSG_W);
            } else {
                tblChordcomplexity.refresh();
                msg("Value not valid.", MSG_E);
            }
        } else {
            tblChordcomplexity.refresh();
            msg(newTerm + " exists already.", MSG_E);
        }
    }

    @FXML
    public void changeChordcomplexityMinCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newMin = cellEdited.getNewValue().toString();
        Chordcomplexity chordcomplexitySelected = tblChordcomplexity.getSelectionModel().getSelectedItem();
        if (newMin.matches(REG_NUMBER)) {
            chordcomplexitySelected.setMin(Integer.parseInt(newMin));
            msg("Value changed." + callSave, MSG_W);
        } else {
            tblChordcomplexity.refresh();
            msg("Value not valid.", MSG_E);
        }
    }

    @FXML
    public void changeChordcomplexityMaxCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newMax = cellEdited.getNewValue().toString();
        Chordcomplexity chordcomplexitySelected = tblChordcomplexity.getSelectionModel().getSelectedItem();
        if (newMax.matches(REG_NUMBER)) {
            chordcomplexitySelected.setMax(Integer.parseInt(newMax));
            msg("Value changed." + callSave, MSG_W);
        } else {
            tblChordcomplexity.refresh();
            msg("Value not valid.", MSG_E);
        }
    }

    @FXML
    public void onChordcomplexityAdd(ActionEvent actionEvent) {
        String error = "Chordcomplexity could not be added. ";
        String term = edtChordcomplexityTerm.getText();
        String min = edtChordcomplexityMin.getText();
        String max = edtChordcomplexityMax.getText();
        if(term.matches(REG_NAME)){
            if(settings.isComplexityUnique(term)){
                if(min.matches(REG_NUMBER)){
                    if(max.matches(REG_NUMBER)){
                        Chordcomplexity chordcomplexity = new Chordcomplexity(term, Integer.parseInt(min), Integer.parseInt(max));
                        settings.getChordcomplexities().add(chordcomplexity);
                        allChordcomplexities.add(chordcomplexity);
                        edtChordcomplexityTerm.clear();
                        edtChordcomplexityMin.clear();
                        edtChordcomplexityMax.clear();
                        msg("Chordcomplexity added." + callSave, MSG_W);
                    } else {msg(error + max + "is not a valid number.",MSG_E);}
                } else {msg(error + min + "is not a valid number.",MSG_E);}
            } else {msg(error + term + " exists already.", MSG_E);}
        } else {msg(error + term + " is not a valid term.",MSG_E);}
    }

    @FXML
    public void onChordcomplexityDelete(ActionEvent actionEvent) {
        Chordcomplexity chordcomplexity = tblChordcomplexity.getSelectionModel().getSelectedItem();
        if(chordcomplexity != null){
            allChordcomplexities.remove(chordcomplexity);
            settings.delChordcomplexity(chordcomplexity);
            msg("Complexity deleted." + callSave, MSG_W);
        } else {
            msg("No complexity selected.",MSG_E);
        }
    }

    /********************************************SCALES****************************************************************/

    @FXML
    public void changeScalesNameCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newName = cellEdited.getNewValue().toString();
        MusicStructure scaleSelected = tblScales.getSelectionModel().getSelectedItem();
        int indexOfGroup = settings.getIndexOfGroup(settings.getScalegroups(), scaleSelected.getGroup());
        if(settings.isStructureNameUnique(settings.getScalegroups().get(indexOfGroup).getMusicStructures(), newName)){
            if (newName.matches(REG_CHORD_NAME)) {
                scaleSelected.setName(newName);
                msg("Value changed." + callSave, MSG_W);
            } else {
                tblScales.refresh();
                msg("Value not valid.", MSG_E);
            }
        } else {
            tblScales.refresh();
            msg(newName + " exists already in " + scaleSelected.getGroup() + ".", MSG_E);
        }
    }

    @FXML
    public void changeScalesUsageCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newUsage = cellEdited.getNewValue().toString();
        MusicStructure scaleSelected = tblScales.getSelectionModel().getSelectedItem();
        if(newUsage.matches(REG_CHORD_USAGE)) {
            scaleSelected.setUsage(getUsageAsArray(newUsage));
            msg("Value changed." + callSave, MSG_W);
        } else {
            tblScales.refresh();
            msg("Value not valid.", MSG_E);
        }
    }

    @FXML
    public void changeScalesGroupCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newGroup = cellEdited.getNewValue().toString();
        int indexOldGroup = settings.getIndexOfGroup(settings.getScalegroups(), cellEdited.getOldValue().toString());
        int indexNewGroup = settings.getIndexOfGroup(settings.getScalegroups(), newGroup);
        MusicStructure scaleSelected = tblScales.getSelectionModel().getSelectedItem();
        if(settings.isStructureNameUnique(settings.getScalegroups().get(indexNewGroup).getMusicStructures(), scaleSelected.getName())) {
            //remove
            allChords.remove(scaleSelected);
            settings.getScalegroups().get(indexOldGroup).delMusicStructure(scaleSelected);
            //add
            scaleSelected.setGroup(newGroup);
            allChords.add(scaleSelected);
            settings.getScalegroups().get(indexNewGroup).addMusicStructure(scaleSelected);
            msg("Value changed." + callSave, MSG_W);
        } else {
            tblScales.refresh();
            msg(scaleSelected.getName() + " exists already in " + newGroup + ".",MSG_E);
        }
    }

    @FXML
    public void onScalesAdd(ActionEvent actionEvent) {
        String error = "Scale could not be added. ";
        String name = edtScalesName.getText();
        String usageText = edtScalesUsage.getText();
        String group = chbScalesGroup.getValue();
        int indexOfGroup = settings.getIndexOfGroup(settings.getScalegroups(), group);
        if(name.matches(REG_CHORD_NAME)){
            if(settings.isStructureNameUnique(settings.getScalegroups().get(indexOfGroup).getMusicStructures(), name)) {
                if (usageText.matches(REG_CHORD_USAGE)) {
                    ArrayList<Integer> usage = getUsageAsArray(usageText);
                    if (group != null) {
                        int lengthScalegroups = settings.getScalegroups().size();
                        for (int i = 0; i < lengthScalegroups; i++) {
                            if (group.equals(settings.getScalegroups().get(i).getName())) {
                                MusicStructure scale = new MusicStructure(name, usage, group);
                                settings.getScalegroups().get(i).addMusicStructure(scale);
                                tblScales.getItems().add(scale);
                                edtScalesName.clear();
                                edtScalesUsage.clear();
                                msg("Scale " + name + " added." + callSave, MSG_W);
                                continue;
                            }
                        }
                    } else {msg(error + "No group selected.", MSG_E);}
                } else {msg(error + "Usage is empty or not in the right format (Example: 0-2-7).", MSG_E);}
            } else {msg(error + name + " exists already.", MSG_E);}
        } else {msg(error + "Name is not valid.", MSG_E);}
    }

    @FXML
    public void onScalesDelete(ActionEvent actionEvent) {
        MusicStructure scale = tblScales.getSelectionModel().getSelectedItem();
        int indexScalegroup = settings.getIndexOfGroup(settings.getScalegroups(), scale.getGroup());
        if(scale != null){
            allScales.remove(scale);
            settings.getScalegroups().get(indexScalegroup).delMusicStructure(scale);
            msg("Scale deleted." + callSave, MSG_W);
        } else {
            msg("No scale selected.", MSG_E);
        }
    }

    /********************************************SCALEGROUPS***********************************************************/

    @FXML
    public void changeScalegroupsNameCellEvent(TableColumn.CellEditEvent cellEdited) {
        String newName = cellEdited.getNewValue().toString();
        MusicStructureGroup scalegroupSelected = tblScalegroups.getSelectionModel().getSelectedItem();
        if(settings.isGroupNameUnique(settings.getScalegroups(), newName)) {
            if (newName.matches(REG_NAME)) {
                scalegroupSelected.setName(newName);
                scalegroupSelected.changeMusicStructureGroupInMusicStructures();
                msg("Value changed." + callSave, MSG_W);
            } else {
                tblScalegroups.refresh();
                msg("Value not valid.", MSG_E);
            }
        } else {
            tblScalegroups.refresh();
            msg("There is already a scalegroup named " + newName + ".", MSG_E);
        }
    }

    @FXML
    public void onScalegroupsAdd(ActionEvent actionEvent) {
        String error = "Scalegroup could not be added. ";
        String name = edtScalegroupsName.getText();
        if(settings.isGroupNameUnique(settings.getScalegroups(), name)){
            if(name.matches(REG_NAME)){
                MusicStructureGroup scalegroup = new MusicStructureGroup();
                scalegroup.setName(name);
                settings.getScalegroups().add(scalegroup);
                allScalegroups.add(scalegroup);
                allScalegroupsAsString.add(scalegroup.getName());
                edtScalegroupsName.clear();
                msg("Scalegroup added." + callSave, MSG_W);
            } else {msg(error + name + " is not a valid name.",MSG_E);}
        } else {msg(error + "There is already a scalegroup named " + name + ".",MSG_E);}
    }

    @FXML
    public void onScalegroupsDelete(ActionEvent actionEvent) {
        MusicStructureGroup scalegroup= tblScalegroups.getSelectionModel().getSelectedItem();
        if(scalegroup != null){
            allScalegroups.remove(scalegroup);
            allScalegroupsAsString.remove(scalegroup.getName());
            settings.delScalegroup(scalegroup);
            msg("Scalegroup deleted." + callSave, MSG_W);
        } else {
            msg("No scalegroup selected.",MSG_E);
        }
    }
}