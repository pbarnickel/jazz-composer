/*
    Description:    Data-model-class for MusicStructureGroups like Chordgroups or Scalegroups.
                    musicStructures is a list of all containing MusicStructures.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           22.02.2018
 */

package composer.Model;

import java.util.ArrayList;

public class MusicStructureGroup extends MusicElement {

    private ArrayList<MusicStructure> musicStructures;

    public MusicStructureGroup(){
        this.name = "";
        this.musicStructures = new ArrayList<MusicStructure>();
    }

    public MusicStructureGroup(String name){
        this.name = name;
        this.musicStructures = new ArrayList<MusicStructure>();
    }

    public String getName(){
        return name;
    }

    public ArrayList<MusicStructure> getMusicStructures() {
        return musicStructures;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addMusicStructure(MusicStructure musicStructure){
        musicStructures.add(musicStructure);
    }

    public void delMusicStructure(MusicStructure musicStructure){
        this.musicStructures.remove(musicStructure);
    }

    //Getter for dynamically UI-outputs
    public int getNrOfMusicStructures(){
        return musicStructures.size();
    }

    //Changes all group-attributes in enclosed MusicStructures
    public void changeMusicStructureGroupInMusicStructures(){
        int length = musicStructures.size();
        for(int i=0; i<length; i++){
            musicStructures.get(i).setGroup(this.name);
        }
    }
}
