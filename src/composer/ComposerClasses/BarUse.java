/*
    Description:    Helper-Class for Calculating matching uses considering the start eighth of bar;
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           19.03.2018
 */

package composer.ComposerClasses;

public class BarUse {
    private double duration;
    private int procedure;

    BarUse(double duration, int procedure){
        this.duration = duration;
        this.procedure = procedure;
    }

    public double getDuration(){
        return this.duration;
    }

    public int getProcedure(){
        return this.procedure;
    }

    public void setDuration(double duration){
        this.duration = duration;
    }
}
