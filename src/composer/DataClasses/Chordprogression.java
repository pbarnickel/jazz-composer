/*
    Description:    Data-model-class for Chordprogression-user-input.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           16.05.2018
 */

package composer.DataClasses;

import java.util.ArrayList;

public class Chordprogression {

    private ArrayList<Patternelement> patternelements;

    public Chordprogression(ArrayList patternelements){
        this.patternelements = patternelements;
    }

    public int getSize(){
        return patternelements.size();
    }

    public ArrayList<Patternelement> getPatternelements(){
        return patternelements;
    }

    public Patternelement getPatternelement(int i){
        return patternelements.get(i);
    }

}
