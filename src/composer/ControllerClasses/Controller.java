/*
    Description:    Root-Controller Class including abstract Message-Handling in UI.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ControllerClasses;

import composer.DataClasses.Chordcomplexity;
import composer.DataClasses.MusicStructure;
import composer.DataClasses.MusicStructureGroup;
import composer.DataClasses.Settings;
import composer.Interfaces.MessageTypes;
import composer.Interfaces.Regex;
import composer.DataClasses.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import jm.JMC;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Controller implements MessageTypes, JMC, Regex {

    protected Response r;
    protected Settings settings;
    protected ObservableList<MusicStructure> allChords;
    protected ObservableList<MusicStructure> allScales;
    protected ObservableList<MusicStructureGroup> allChordgroups;
    protected ObservableList<MusicStructureGroup> allScalegroups;
    protected ObservableList<String> allChordgroupsAsString;
    protected ObservableList<String> allScalegroupsAsString;
    protected ObservableList<Chordcomplexity> allChordcomplexities;
    protected ObservableList<String> allChordcomplexitiesAsString;

    public abstract void msg(String message, int type);

    public String getUsageAsString(ArrayList<Integer> usage){
        String usageAsString = "";
        int length = usage.size();
        for(int i=0; i<length-1; i++){
            usageAsString += usage.get(i).toString() + "-";
        }
        usageAsString += usage.get(length).toString();
        return usageAsString;
    }

    public ArrayList<Integer> getUsageAsArray(String usage){
        ArrayList<Integer> usageAsArray = new ArrayList<Integer>();
        while(usage.indexOf("-")>-1){
            usageAsArray.add(Integer.parseInt(usage.substring(0,usage.indexOf("-"))));
            usage = usage.substring(usage.indexOf("-") + 1);
        }
        usageAsArray.add(Integer.parseInt(usage));
        return usageAsArray;
    }

    public void changeScene(String sceneKey, ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../../FXML/" + sceneKey + ".fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public File midiFileChooser(String title, ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(new Settings().getDefault_location()));
        fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("MIDI Files", "*.mid"));
        Stage stg = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        return fileChooser.showOpenDialog(stg);
    }

    /*********************************************** GET LISTS ********************************************************/

    public ObservableList<MusicStructure> getAllChords() {
        ObservableList<MusicStructure> allChords = FXCollections.observableArrayList();
        int lengthChordgroups = settings.getChordgroups().size();
        for (int i = 0; i < lengthChordgroups; i++) {
            int lengthChords = settings.getChordgroups().get(i).getMusicStructures().size();
            for (int j = 0; j < lengthChords; j++) {
                allChords.add(settings.getChordgroups().get(i).getMusicStructures().get(j));
            }
        }
        return allChords;
    }

    public ObservableList<String> getAllChordgroupsAsString() {
        ObservableList<String> allGroups = FXCollections.observableArrayList();
        int lengthChordgroups = settings.getChordgroups().size();
        for(int i=0; i<lengthChordgroups; i++){
            allGroups.add(settings.getChordgroups().get(i).getName());
        }
        return allGroups;
    }

    public ObservableList<MusicStructureGroup> getAllChordgroups(){
        ObservableList<MusicStructureGroup> allGroups = FXCollections.observableArrayList();
        int lengthChordgroups = settings.getChordgroups().size();
        for (int i = 0; i < lengthChordgroups; i++) {
            allGroups.add(settings.getChordgroups().get(i));
        }
        return allGroups;
    }

    public ObservableList<Chordcomplexity> getAllChordcomplexities() {
        ObservableList<Chordcomplexity> allComplexities = FXCollections.observableArrayList();
        int length = settings.getChordcomplexities().size();
        for(int i=0; i<length; i++){
            allComplexities.add(settings.getChordcomplexities().get(i));
        }
        return allComplexities;
    }

    public ObservableList<MusicStructure> getAllScales() {
        ObservableList<MusicStructure> allScales = FXCollections.observableArrayList();
        int lengthScalegoups = settings.getScalegroups().size();
        for (int i = 0; i < lengthScalegoups; i++) {
            int lengthScales = settings.getScalegroups().get(i).getMusicStructures().size();
            for (int j = 0; j < lengthScales; j++) {
                allScales.add(settings.getScalegroups().get(i).getMusicStructures().get(j));
            }
        }
        return allScales;
    }

    public ObservableList<String> getAllScalegroupsAsString() {
        ObservableList<String> allGroups = FXCollections.observableArrayList();
        int lengthScalegroups = settings.getScalegroups().size();
        for(int i=0; i<lengthScalegroups; i++){
            allGroups.add(settings.getScalegroups().get(i).getName());
        }
        return allGroups;
    }

    public ObservableList<MusicStructureGroup> getAllScalegroups(){
        ObservableList<MusicStructureGroup> allGroups = FXCollections.observableArrayList();
        int lengthScalegroups = settings.getScalegroups().size();
        for (int i = 0; i < lengthScalegroups; i++) {
            allGroups.add(settings.getScalegroups().get(i));
        }
        return allGroups;
    }

    public ObservableList<String> getAllChordcomplexitiesAsString() {
        ObservableList<String> allComplexities = FXCollections.observableArrayList();
        int lengthComplexities = settings.getChordcomplexities().size();
        for(int i=0; i<lengthComplexities; i++){
            allComplexities.add(settings.getChordcomplexities().get(i).getTerm());
        }
        return allComplexities;
    }
}
