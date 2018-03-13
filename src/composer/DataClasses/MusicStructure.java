/*
    Description:    Data-model-class for MusicStructure like Chords or Scales.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           22.02.2018
 */

package composer.DataClasses;

import javafx.scene.transform.Scale;
import jm.JMC;
import jm.music.data.*;
import jm.util.Play;

import java.util.ArrayList;

public class MusicStructure extends MusicElement implements JMC {

    private ArrayList<Integer> usage;
    private String group;
    private String mode;

    public MusicStructure(){
        this.name = "";
        this.usage = new ArrayList<Integer>();
        this.mode = "";
    }

    public MusicStructure(String name, ArrayList<Integer> usage, String group){
        this.name = name;
        this.usage = usage;
        this.group = group;
        this.mode = calcMode();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getUsage() {
        return usage;
    }

    public String getUsageAsString() {
        String usageAsString = "";
        int length = usage.size();
        if(length>0){
            for(int i=0; i<length-1; i++){
                usageAsString += usage.get(i).toString() + "-";
            }
            usageAsString += usage.get(length-1).toString();
        }
        return usageAsString;
    }

    public String getGroup() {
        return group;
    }

    public String getMode() {return mode;}

    public void setName(String name){
        this.name = name;
    }

    public void setUsage(ArrayList<Integer> usage){
        this.usage = usage;
        this.mode = calcMode();
    }

    public void setGroup(String group){
        this.group = group;
    }

    public void setMode(String mode) {this.mode = mode;}

    public String calcMode(){
        int length = usage.size();
        for(int i=0; i<length; i++){
            if(usage.get(i).intValue()==3){
                return "Minor";
            }
        }
        return "Major";
    }

    public void play(boolean asChord){
        Part piano = new Part("Piano", PIANO, 0);
        CPhrase chord = new CPhrase();
        Phrase scale = new Phrase();
        int length = usage.size();
        int notes[] = new int[length];
        for(int i=0; i<length; i++){
            if(asChord){
                notes[i] = C4 + usage.get(i).intValue();
            } else {
                Note note = new Note(C4 + usage.get(i),SIXTEENTH_NOTE);
                scale.addNote(note);
            }
        }
        if(asChord){
            chord.addChord(notes,CROTCHET);
            piano.addCPhrase(chord);
        } else {
            piano.addPhrase(scale);
        }
        Play.midi(piano);
    }
}
