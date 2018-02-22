/*
    Description:    Data-model-class for MusicStructure like Chords or Scales.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           22.02.2018
 */

package composer.DataClasses;

import java.util.ArrayList;

public class MusicStructure {

    private String name;
    private ArrayList<Integer> usage;
    private String group;

    public MusicStructure(){
        this.name = "";
        this.usage = new ArrayList<Integer>();
    }

    public MusicStructure(String name, ArrayList<Integer> usage, String group){
        this.name = name;
        this.usage = usage;
        this.group = group;
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
        for(int i=0; i<length-1; i++){
            usageAsString += usage.get(i).toString() + "-";
        }
        usageAsString += usage.get(length-1).toString();
        return usageAsString;
    }

    public String getGroup() {
        return group;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setUsage(ArrayList<Integer> usage){
        this.usage = usage;
    }

    public void setGroup(String group){
        this.group = group;
    }

}
