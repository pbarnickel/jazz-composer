/*
    Description:    Helper-Class for Calculating matching uses considering the start eighth of bar;
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           19.03.2018
 */

package composer.DataClasses;

import jm.JMC;

import java.util.ArrayList;
import java.util.Random;

public class BarUses implements JMC {
    private ArrayList<BarUse> barUses;
    private double endRest;

    public BarUses(int startEighth){
        barUses = new ArrayList<BarUse>();

        //Calculate scope for generation
        double possibleScope = WHOLE_NOTE - getStartTime(startEighth);

        //Generate a random nr of uses
        int nrUses = new Random().nextInt(3) + 1;

        //Generate bar-uses by nr of uses
        if(possibleScope == WHOLE_NOTE && nrUses == 1){
            barUses.add(new BarUse(WHOLE_NOTE, 0));
            possibleScope -= WHOLE_NOTE;
        } else {
            if(nrUses == 1){
                barUses.add(new BarUse(possibleScope, 0));
                possibleScope -= possibleScope;
            } else if(nrUses == 2){
                int procedure = new Random().nextInt(3) - 1;
                int procedure_2;
                switch (procedure){
                    case -1:    procedure_2 = 1;
                                break;
                    case 1:     procedure_2 = -1;
                                break;
                    default:    procedure_2 = new Random().nextInt(3) - 1;
                }
                possibleScope = generateUse(possibleScope, procedure, HALF_NOTE, QUARTER_NOTE);
                possibleScope = generateUse(possibleScope, procedure + procedure_2, QUARTER_NOTE_TRIPLET, EIGHTH_NOTE_TRIPLET);
            } else if(nrUses == 3){
                int procedure;
                if(new Random().nextBoolean())procedure = 1;
                else procedure = -1;
                possibleScope = generateUse(possibleScope, 0, HALF_NOTE, QUARTER_NOTE);
                possibleScope = generateUse(possibleScope, procedure, HALF_NOTE, QUARTER_NOTE);
                possibleScope = generateUse(possibleScope, 0, QUARTER_NOTE_TRIPLET, EIGHTH_NOTE_TRIPLET);
            }
        }

        //Calculate end-rest
        endRest = possibleScope;
    }

    public ArrayList<BarUse> getBarUses(){
        return this.barUses;
    }

    public double getEndRest(){
        return this.endRest;
    }

    //Generates a use
    public double generateUse(double scope, int procedure, double high, double low){
        if(scope >= high){
            if(new Random().nextBoolean()){
                barUses.add(new BarUse(high, procedure));
                scope -= high;
            } else {
                barUses.add(new BarUse(low, procedure));
                scope -= low;
            }
        } else if(scope >= low) {
            barUses.add(new BarUse(low, procedure));
            scope -= low;
        }
        return scope;
    }

    //Returns the start time of
    private double getStartTime(int position) {
        double start = position / 2;
        start += (position % 2) * QUARTER_NOTE * 2 / 3;
        return start;
    }
}
