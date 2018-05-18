/*
    Description:    Data-model-class for eighths with position and range.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           15.05.2018
 */

package composer.DataClasses;

public class Eighth {
    private int position;
    private double rowData;
    private Range range;

    public Eighth(int position, double rowData, Range range){
        this.position = position;
        this.rowData = rowData;
        this.range = range;
    }

    public int getPosition(){
        return this.position;
    }

    public double getRowData(){
        return this.rowData;
    }

    public Range getRange(){
        return this.range;
    }
}
