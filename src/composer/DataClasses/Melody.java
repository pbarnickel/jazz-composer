/*
    Description:    Data-model-class for Melody-user-input.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           15.05.2018
 */

package composer.DataClasses;

public class Melody {

    private double inversion;
    private double sortOfPitches;
    private double jumper;
    private MusicStructure majorScale;
    private MusicStructure minorScale;

    public Melody(){}

    public Melody(double inversion, double sortOfPitches, double jumper, MusicStructure majorScale, MusicStructure minorScale){
        this.inversion = inversion;
        this.sortOfPitches = sortOfPitches;
        this.jumper = jumper;
        this.majorScale = majorScale;
        this.minorScale = minorScale;
    }

    public double getInversion() {return this.inversion;}
    public double getSortOfPitches() {return this.sortOfPitches;}
    public double getJumper() {return this.jumper;}
    public MusicStructure getMajorScale() {return majorScale;}
    public MusicStructure getMinorScale() {return minorScale;}
}
