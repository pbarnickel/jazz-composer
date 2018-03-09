/*
    Description:    Data-model-class for Chordcomplexity.
                    Min establish the minimum of chord-sizes.
                    Max establish the maximum of chord-sizes.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           22.02.2018
 */

package composer.DataClasses;

public class Chordcomplexity extends MusicElement {
    private int min;
    private int max;

    public Chordcomplexity(String name, int min, int max){
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public String getMinAsString() {return Integer.toString(min);}

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public String getMaxAsString() {return Integer.toString(max);}

    public void setMax(int max) {
        this.max = max;
    }
}
