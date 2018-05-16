/*
    Description:    Data-model-class for Pattern-user-input.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           16.05.2018
 */

package composer.DataClasses;

import java.util.ArrayList;

public class Pattern {

    private ArrayList<Patternelement> pattern;

    public Pattern(){}

    public Pattern(ArrayList pattern){
        this.pattern = pattern;
    }

    public  int getSize(){
        return pattern.size();
    }

    public void addPatternelement(Patternelement patternelement){
        pattern.add(patternelement);
    }

    public void delPatternelement(int i){
        pattern.remove(i);
    }

    public Patternelement getPatternelement(int i){
        return pattern.get(i);
    }

    public ArrayList<Patternelement> getPattern(){
        return pattern;
    }

}
