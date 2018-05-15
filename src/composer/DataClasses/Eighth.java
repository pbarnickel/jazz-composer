/*
    Description:    Data-model-class for eighths with position and range.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           15.05.2018
 */

package composer.DataClasses;

public class Eighth {
    private int position;
    private Range range;

    public Eighth(int position, Range range){
        this.position = position;
        this.range = range;
    }

    public int getPosition(){
        return this.position;
    }

    public Range getRange(){
        return this.range;
    }
}
