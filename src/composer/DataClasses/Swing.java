/*
    Description:    Data-model-class for Swing-user-input.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           16.05.2018
 */

package composer.DataClasses;

import java.util.ArrayList;

public class Swing {

    private ArrayList<Eighth> eighths;

    public Swing(){}

    public Swing(ArrayList eighths){
        this.eighths = eighths;
    }

    public  int getSize(){
        return eighths.size();
    }

    public Eighth getEighth(int i){
        return eighths.get(i);
    }

    public ArrayList<Eighth> getEighths(){
        return eighths;
    }

}
