/*
    Description:    Data-model-class for Chordcomplexity.
                    Min establish the minimum of chord-sizes.
                    Max establish the maximum of chord-sizes.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           22.02.2018
 */

package composer.DataClasses;

public class Chordcomplexity {
    private String term;
    private int min;
    private int max;

    public Chordcomplexity(String term, int min, int max){
        this.term = term;
        this.min = min;
        this.max = max;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
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
