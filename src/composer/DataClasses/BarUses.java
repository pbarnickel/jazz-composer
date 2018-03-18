/*
    Description:    Helper-Class for Calculating matching uses considering the start eighth of bar;
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           22.02.2018
 */

package composer.DataClasses;

import jm.JMC;

import java.util.ArrayList;
import java.util.Random;

public class BarUses implements JMC {
    private int nr_wholes = 0;              // 4.0
    private int nr_halfs = 0;               // 2.0
    private int nr_quarters = 0;            // 1.0
    private int nr_quarter_tripples = 0;    // 0.66
    private int nr_eighth_tripples = 0;     // 0.33
    private ArrayList<BarUse> barUses;

    public BarUses(int startEighth, int nrOfEighths){
        barUses = new ArrayList<BarUse>();

        //Calculate scope for generation
        double possibleScope = WHOLE_NOTE - getStartTime(startEighth);

        //Calculate a random possibility down to EIGHTH-TRIPPLE-NOTE
        if(possibleScope == WHOLE_NOTE){
            if(new Random().nextBoolean()){
                nr_wholes++;
            } else {
                possibleScope = generateFirstUses(possibleScope);
                possibleScope = generateLastUses(possibleScope);
            }
        }

        //Generate a random nr of uses
        int nrUses = new Random().nextInt(Math.min(3, nrOfEighths)) + 1;

        //Generate bar-uses by nr of uses
        if(possibleScope == WHOLE_NOTE && nrUses == 1){
            barUses.add(new BarUse(WHOLE_NOTE, 0,0));
        } else {
            if(nrUses == 1){
                barUses.add(new BarUse(possibleScope, 0,0));
            } else if(nrUses == 2){
                int procedure = new Random().nextInt(3) - 1;
                int start_of_procedure;
                if(procedure < 0) start_of_procedure = new Random().nextInt(2);
                else start_of_procedure = new Random().nextInt(3) - 1;
                possibleScope = generateUse(possibleScope, procedure, start_of_procedure, HALF_NOTE, QUARTER_NOTE);
                possibleScope = generateUse(possibleScope, procedure, start_of_procedure, QUARTER_NOTE_TRIPLET, EIGHTH_NOTE_TRIPLET);
            } else if(nrUses == 3){

            }
        }
    }

    public double generateUse(double scope, int procedure, int start_of_procedure, double high, double low){
        if(scope >= high){
            if(new Random().nextBoolean()){
                barUses.add(new BarUse(high, procedure, start_of_procedure));
                scope -= high;
            } else {
                barUses.add(new BarUse(low, procedure, start_of_procedure));
                scope -= low;
            }
        } else if(scope >= low) {
            barUses.add(new BarUse(low, procedure, start_of_procedure));
            scope -= low;
        }
        return scope;
    }

    //Returns the start time of
    public double getStartTime(int position) {
        double start = position / 2;
        start += (position % 2) * QUARTER_NOTE * 2 / 3;
        return start;
    }

    public double generateFirstUses(double scope){
        while(scope > QUARTER_NOTE_TRIPLET){
            if(scope >= HALF_NOTE){
                if(new Random().nextBoolean()){
                    nr_halfs++;
                    scope = scope - HALF_NOTE;
                } else {
                    nr_quarters++;
                    scope = scope - QUARTER_NOTE;
                }
            } else {
                if(scope >= QUARTER_NOTE){
                    nr_quarters++;
                    scope = scope - QUARTER_NOTE;
                }
            }
        }
        return scope;
    }

    public double generateLastUses(double scope){
        while(scope > 0){
            if(scope >= QUARTER_NOTE_TRIPLET){
                if(new Random().nextBoolean()){
                    nr_quarter_tripples++;
                    scope = scope - QUARTER_NOTE_TRIPLET;
                } else {
                    nr_eighth_tripples++;
                    scope = scope - EIGHTH_NOTE_TRIPLET;
                }
            } else {
                if(scope >= EIGHTH_NOTE_TRIPLET){
                    nr_eighth_tripples++;
                    scope = scope - EIGHTH_NOTE_TRIPLET;
                }
            }
        }
        return scope;
    }
}
