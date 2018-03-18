/*
    Description:    Data-model-class for all runtime-data.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           22.02.2018
 */

package composer.DataClasses;

import jm.JMC;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static jm.constants.Pitches.C4;

public class Settings implements JMC{

    private String default_location;
    private ArrayList<MusicStructureGroup> chordgroups;
    private ArrayList<Chordcomplexity> chordcomplexities;
    private ArrayList<MusicStructureGroup> scalegroups;
    private ArrayList<Tone> tones;

    public Settings(){loadSettings();}

    public String getDefault_location(){ return this.default_location; }

    public ArrayList<MusicStructureGroup> getChordgroups(){
        return chordgroups;
    }

    public ArrayList<Chordcomplexity> getChordcomplexities() { return chordcomplexities; }

    public ArrayList<MusicStructureGroup> getScalegroups() {return scalegroups;}

    public ArrayList<Tone> getTones() {return this.tones;}

    public void setDefault_location(String default_location){this.default_location = default_location;}

    public void delChordgroup(MusicStructureGroup chordgroup){
        this.chordgroups.remove(chordgroup);
    }

    public void delScalegroup(MusicStructureGroup scalegroup) {this.scalegroups.remove(scalegroup);}

    public void delChordcomplexity(Chordcomplexity chordcomplexity) { this.chordcomplexities.remove(chordcomplexity); }

    public void delTone(Tone tone) { this.tones.remove(tone);}

    //Returns paths for JSON-Settings-File and JSON-Default-Settings-File---------------------------------------------------------
    public String getPathForSettings(){
        return new File("").getAbsolutePath() +  "/src/composer/JSON/settings.json";
    }
    public String getPathForDefaultSettings(){return new File("").getAbsolutePath() + "/src/composer/JSON/default.json";}
    //----------------------------------------------------------------------------------------------------------------------------

    //Returns index of MusicElement-Name in MusicElement-List
    public int getIndexOfMusicElement(ArrayList<? extends MusicElement> musicElements, String element){
        int length = musicElements.size();
        for(int i=0; i<length; i++){
            if(musicElements.get(i).getName().equals(element))return i;
        }
        return -1;
    }

    //Checkes if MusicElement-Name is unique in MusicElement-List
    public boolean isNameUnique(ArrayList<? extends MusicElement> musicElements, String name){
        int length = musicElements.size();
        for(int i=0; i<length; i++){
            if(musicElements.get(i).getName().equals(name))return false;
        }
        return true;
    }

    //Returns Tone-Object by String of Tone-Name
    public Tone getToneByString(String toneString){
        int length = tones.size();
        int pitch = 60;
        for(int i=0; i<length; i++){
            if(tones.get(i).getName().equals(toneString.substring(0,1))){
                pitch = tones.get(i).getPitch();
                if(toneString.length()>1){
                    if(toneString.substring(1,2).equals("b"))pitch--;
                    else if(toneString.substring(1,2).equals("#"))pitch++;
                }
            }
        }
        return new Tone(toneString, pitch);
    }

    //Loads default-settings
    public void loadDefaultSettings() throws IOException {
        Files.copy(new File(getPathForDefaultSettings()).toPath(), new File(getPathForSettings()).toPath(), REPLACE_EXISTING);
    }

    /************************************************ JSON ***********************************************************/

    //loads settings by JSON-settings-file
    public void loadSettings(){
        JSONParser parser = new JSONParser();
        try {
            //read file
            FileReader reader = new FileReader(getPathForSettings());
            Object obj = parser.parse(reader);
            JSONObject jsonObjectRoot = (JSONObject) obj;

            //read default_location
            this.default_location = (String) jsonObjectRoot.get("default_location");

            //read chordgroups
            chordgroups = loadMusicStructureGroup(jsonObjectRoot, "chordgroups", "chords");

            //read scalegroups
            scalegroups = loadMusicStructureGroup(jsonObjectRoot,"scalegroups", "scales");

            //read chordcomplexities
            chordcomplexities = new ArrayList<Chordcomplexity>();
            JSONArray jsonArrayChordcomplexities = (JSONArray) jsonObjectRoot.get("chordcomplexities");
            int lengthJSONArrayChordcomplexities = jsonArrayChordcomplexities.size();
            for(int i=0; i<lengthJSONArrayChordcomplexities; i++){
                JSONObject jsonObjectChordcomplexity = (JSONObject) jsonArrayChordcomplexities.get(i);
                String chordcomplexityName = jsonObjectChordcomplexity.get("name").toString();
                int chordcomplexityMin = Integer.parseInt(jsonObjectChordcomplexity.get("min").toString());
                int chordcomplexityMax = Integer.parseInt(jsonObjectChordcomplexity.get("max").toString());
                Chordcomplexity chordcomplexity = new Chordcomplexity(chordcomplexityName, chordcomplexityMin, chordcomplexityMax);
                chordcomplexities.add(chordcomplexity);
            }

            //read tones
            tones = new ArrayList<Tone>();
            JSONArray jsonArrayTones = (JSONArray) jsonObjectRoot.get("tones");
            int lengthJSONArrayTones = jsonArrayTones.size();
            for(int i=0; i<lengthJSONArrayTones; i++){
                JSONObject jsonObjectTone = (JSONObject) jsonArrayTones.get(i);
                String toneName = jsonObjectTone.get("name").toString();
                Integer tonePitch = Integer.parseInt(jsonObjectTone.get("pitch").toString());
                Tone tone = new Tone(toneName, tonePitch);
                tones.add(tone);
            }

            reader.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    //Returns a MusicStructureGroup-list including there MusicStructures
    public ArrayList<MusicStructureGroup> loadMusicStructureGroup(JSONObject jsonObjectRoot, String jsonRootKey, String jsonMusicStructuresKey){
        ArrayList<MusicStructureGroup> groups = new ArrayList<MusicStructureGroup>();
        JSONArray jsonArrayMusicStructureGroups = (JSONArray) jsonObjectRoot.get(jsonRootKey);
        int lengthJSONArrayMusicStructureGroups  = jsonArrayMusicStructureGroups.size();
        for(int i=0; i<lengthJSONArrayMusicStructureGroups; i++){
            MusicStructureGroup musicStructureGroup = new MusicStructureGroup();
            JSONObject jsonObjectMusicStructureGroup = (JSONObject) jsonArrayMusicStructureGroups.get(i);
            String musicStructureGroupName = (String) jsonObjectMusicStructureGroup.get("name");
            musicStructureGroup.setName(musicStructureGroupName);
            JSONArray jsonArrayMusicStructures = (JSONArray) jsonObjectMusicStructureGroup.get(jsonMusicStructuresKey);
            int lengthJSONArrayMusicStructures = jsonArrayMusicStructures.size();
            for(int j=0; j<lengthJSONArrayMusicStructures; j++){
                MusicStructure musicStructure = new MusicStructure();
                JSONObject jsonObjectMusicStructure = (JSONObject) jsonArrayMusicStructures.get(j);
                String musicStructureName = (String) jsonObjectMusicStructure.get("name");
                musicStructure.setName(musicStructureName);
                String musicStructureMusicStructureGroup = (String) jsonObjectMusicStructureGroup.get("name");
                musicStructure.setGroup(musicStructureMusicStructureGroup);
                ArrayList<Integer> musicStructureUsage = new ArrayList<Integer>();
                JSONArray jsonArrayMusicStructureUsage = (JSONArray) jsonObjectMusicStructure.get("usage");
                int lengthJSONArrayMusicStructureLength = jsonArrayMusicStructureUsage.size();
                for(int k=0; k<lengthJSONArrayMusicStructureLength; k++){
                    int usagePart = (int)(long) jsonArrayMusicStructureUsage.get(k);
                    musicStructureUsage.add(usagePart);
                }//usage
                musicStructure.setUsage(musicStructureUsage);
                musicStructure.setMode(musicStructure.calcMode());
                musicStructureGroup.addMusicStructure(musicStructure);
            }//structure
            groups.add(musicStructureGroup);
        }//group
        return groups;
    }

    //Saves runtime-settings in JSON-settings-file
    public void saveSettings(){
        JSONObject jsonObjectRoot = new JSONObject();

        //default_location
        jsonObjectRoot.put("default_location", default_location);

        //chordgroups
        jsonObjectRoot.put("chordgroups", generateJSONMusicStructureGroup(chordgroups, "chords"));

        //scalegroups
        jsonObjectRoot.put("scalegroups", generateJSONMusicStructureGroup(scalegroups, "scales"));

        //chordcomplexities
        JSONArray jsonArrayChordcomplexities = new JSONArray();
        int lengthChordcomplexities = chordcomplexities.size();
        for(int i=0; i<lengthChordcomplexities; i++){
            JSONObject jsonObjectChordcomplexity = new JSONObject();
            jsonObjectChordcomplexity.put("name", chordcomplexities.get(i).getName());
            jsonObjectChordcomplexity.put("min", chordcomplexities.get(i).getMin());
            jsonObjectChordcomplexity.put("max", chordcomplexities.get(i).getMax());
            jsonArrayChordcomplexities.add(jsonObjectChordcomplexity);
        }
        jsonObjectRoot.put("chordcomplexities", jsonArrayChordcomplexities);

        //tones
        JSONArray jsonArrayTones = new JSONArray();
        int lengthTones = tones.size();
        for(int i=0; i<lengthTones; i++){
            JSONObject jsonObjectTone = new JSONObject();
            jsonObjectTone.put("name", tones.get(i).getName());
            jsonObjectTone.put("pitch", tones.get(i).getPitch());
            jsonArrayTones.add(jsonObjectTone);
        }
        jsonObjectRoot.put("tones", jsonArrayTones);

        //write JSON
        try (FileWriter writer = new FileWriter(getPathForSettings())){
            writer.write(jsonObjectRoot.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Returns a JSON-array with all MusicStructureGroups including there MusicStructures
    public JSONArray generateJSONMusicStructureGroup(ArrayList<MusicStructureGroup> groups, String jsonMusicStructuresKey){
        JSONArray jsonArrayGroups = new JSONArray();
        int lengthGroups = groups.size();
        for(int i=0; i<lengthGroups; i++){
            JSONObject jsonObjectGroup = new JSONObject();
            jsonObjectGroup.put("name", groups.get(i).getName());
            JSONArray jsonArrayStructures = new JSONArray();
            int lengthStructures = groups.get(i).getMusicStructures().size();
            for(int j=0; j<lengthStructures; j++){
                JSONObject jsonObjectStructure= new JSONObject();
                jsonObjectStructure.put("name", groups.get(i).getMusicStructures().get(j).getName());
                JSONArray jsonArrayUsage = new JSONArray();
                int lengthUsage = groups.get(i).getMusicStructures().get(j).getUsage().size();
                for(int k=0; k<lengthUsage; k++){
                    jsonArrayUsage.add(groups.get(i).getMusicStructures().get(j).getUsage().get(k).intValue());
                }//usage
                jsonObjectStructure.put("usage", jsonArrayUsage);
                jsonArrayStructures.add(jsonObjectStructure);
            }//structures
            jsonObjectGroup.put(jsonMusicStructuresKey, jsonArrayStructures);
            jsonArrayGroups.add(jsonObjectGroup);
        }//group
        return jsonArrayGroups;
    }
}
