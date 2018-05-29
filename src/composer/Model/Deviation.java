/*
    Description:    Data-model-class to hold UI-inserted and calculated deviation
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           18.05.2018
 */

package composer.Model;

public class Deviation {

    private double rowData;
    private int normData;

    public Deviation(double rowData){
        this.rowData = rowData;
        //Calculates the percentage of row-data and multiplies it with 12 because there are 12 keys
        //Later this deviation is used for compositions to locate in the given deviation range a
        //match between two usages.
        this.normData = (int) (rowData / 100 * 12);
    }

    public double getRowData(){
        return rowData;
    }

    public int getNormData(){
        return normData;
    }

}
