/*
    Description:    Data-model-class for Backingtrack-user-input.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           16.05.2018
 */

package composer.Model;

public class Backingtrack {

    private boolean piano;
    private boolean bass;
    private boolean drums;
    private Deviation deviation;
    private double walkingBass;

    public Backingtrack(boolean piano, boolean bass, boolean drums, Deviation deviation, double walkingBass){
        this.piano = piano;
        this.bass = bass;
        this.drums = drums;
        this.deviation = deviation;
        this.walkingBass = walkingBass;
    }

    public boolean getPiano() {return this.piano;}
    public boolean getBass() {return this.bass;}
    public boolean getDrums() {return this.drums;}
    public Deviation getDeviation() {return this.deviation;}
    public double getWalkingBass() {return this.walkingBass;}

}
