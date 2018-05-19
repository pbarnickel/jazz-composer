/*
    Description:    Data-model-class for Melody-user-input.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           15.05.2018
 */

package composer.DataClasses;

public class Melody {

    private boolean state;
    private double sortOfPitches;
    private double bebop;
    private MusicStructure majorScale;
    private MusicStructure minorScale;

    public Melody(boolean state, double sortOfPitches, double bebop, MusicStructure majorScale, MusicStructure minorScale){
        this.state = state;
        this.sortOfPitches = sortOfPitches;
        this.bebop = bebop;
        this.majorScale = majorScale;
        this.minorScale = minorScale;
    }

    public Melody(boolean state){
        this.state = state;
    }

    public boolean getState() {return this.state;}
    public double getSortOfPitches() {return this.sortOfPitches;}
    public double getBebop() {return this.bebop;}
    public MusicStructure getMajorScale() {return majorScale;}
    public MusicStructure getMinorScale() {return minorScale;}
}
