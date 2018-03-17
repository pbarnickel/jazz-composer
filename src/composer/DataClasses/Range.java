/*
    Description:    Simple Range Class, to store values for random-programming with probabilities
                    Start: start of range
                    End: end of range
    Version:        1.0
    Date:           15.03.2018
 */

package composer.DataClasses;

public class Range {
    private int start;
    private int end;

    public Range(int start, int end){
        this.start = start;
        this.end = end;
    }

    public int getStart(){
        return this.start;
    }

    public int getEnd(){
        return this.end;
    }

    //Checks if an integer-value is in a range
    public boolean isInRange(int i){
        // 0 <= 21 <= 24
        return start <= i && i <= end;
    }
}
