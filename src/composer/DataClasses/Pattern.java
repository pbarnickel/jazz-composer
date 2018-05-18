/*
    Description:    Data-model-class for Pattern-user-input.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           16.05.2018
 */

package composer.DataClasses;

import java.util.ArrayList;

public class Pattern {

    private ArrayList<Patternelement> patternelements;

    public Pattern(ArrayList patternelements){
        this.patternelements = patternelements;
    }

    public int getSize(){
        return patternelements.size();
    }

    public ArrayList<Patternelement> getPatternelements(){
        return patternelements;
    }

    public void addPatternelement(Patternelement patternelement){
        patternelements.add(patternelement);
    }

    public void delPatternelement(int i){
        patternelements.remove(i);
    }

    public Patternelement getPatternelement(int i){
        return patternelements.get(i);
    }

    public ArrayList<Patternelement> getPattern(){
        return patternelements;
    }

}
