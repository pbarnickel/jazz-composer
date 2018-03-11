/*
    Description:    Root-Controller Class including abstract Message-Handling in UI.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ControllerClasses;

import composer.DataClasses.*;
import composer.Interfaces.MessageTypes;
import composer.Interfaces.Regex;
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

import static composer.Main.p;

public abstract class Controller implements MessageTypes, JMC, Regex {

    protected Response r;
    protected Settings settings;
    protected ObservableList<MusicStructure> chords;
    protected ObservableList<MusicStructure> allChords;
    protected ObservableList<MusicStructure> allScales;
    protected ObservableList<MusicStructureGroup> allChordgroups;
    protected ObservableList<MusicStructureGroup> allScalegroups;
    protected ObservableList<Chordcomplexity> allChordcomplexities;
    protected ObservableList<String> chordsAsString;
    protected ObservableList<String> allChordgroupsAsString;
    protected ObservableList<String> allScalegroupsAsString;
    protected ObservableList<String> allChordcomplexitiesAsString;
    protected ObservableList<Patternelement> allPatternelements;

    public abstract void msg(String message, int type);

    public abstract void defaultInputs();

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

    public ObservableList<MusicStructure> getAllItems(ArrayList<MusicStructureGroup> musicStructureGroups){
        ObservableList<MusicStructure> allItems = FXCollections.observableArrayList();
        int lengthGroups = musicStructureGroups.size();
        for(int i=0; i<lengthGroups; i++)allItems.addAll(musicStructureGroups.get(i).getMusicStructures());
        return allItems;
    }

    public ObservableList<String> getStrings(ArrayList<? extends MusicElement> musicElements){
        ObservableList<String> strings = FXCollections.observableArrayList();
        int length = musicElements.size();
        for(int i=0; i<length; i++){
            strings.add(musicElements.get(i).getName());
        }
        return strings;
    }
}
