/*
    Description:    Helper-Class for Calculating matching uses considering the start eighth of bar;
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           19.03.2018
 */

package composer.DataClasses;

public class BarUse {
    double duration;
    int procedure;

    public BarUse(double duration, int procedure){
        this.duration = duration;
        this.procedure = procedure;
    }

    public double getDuration(){
        return this.duration;
    }

    public int getProcedure(){
        return this.procedure;
    }
}
