/*
    Description:    Root-Composer-Class for manipulating the Score including some common methods for
                    interpreting user-input.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ComposerClasses;

import composer.DataClasses.Chordcomplexity;
import composer.DataClasses.Patternelement;
import composer.DataClasses.Settings;
import composer.Interfaces.Constants;
import jm.JMC;
import jm.music.data.*;
import jm.util.Play;
import jm.util.Read;
import jm.util.View;
import jm.util.Write;

import java.util.*;

public class Composer implements JMC, Constants {

    protected Score score = new Score();
    protected Settings settings = new Settings();
    protected double humanizerTolerance;

    public Composer(){
        settings.loadSettings();
    }

    public Score getScore(){
        return this.score;
    }

    //Writes MIDI in score
    public void readMIDIinScore(String path) {
        Read.midi(this.score, path);
    }

    //Writes Score in MIDI
    public void writeScoreinMIDI(String path){
        Write.midi(this.score, path);
    }

    //Empties the score
    public void initScore() {
        this.score.empty();
    }

    //Shows the score in JMusic-View
    public void showScore(){
        View.show(this.score);
    }

    //Plays the score as MIDI
    public void playScore() {
        Play.midi(this.score);
    }

    //Initializes the humanizer from slider and returns percentage of humanizer-tolerance. Input -> max. Tolerance
    public void initHumanizer(double maxTolerance){
        humanizerTolerance = humanizerTolerance * maxTolerance / 10000;
    }

    //Returns random humanizer-factor for multiplication in algorithms considering the humanizer tolerance
    public double calcHumanizer(){
        if(new Random().nextBoolean()) return 1 - humanizerTolerance;
        else return 1 + humanizerTolerance;
    }

    //Returns an usage array added with a root pitch
    public int[] getUsageInContext(ArrayList<Integer> usage, int rootPitch){
        int length = usage.size();
        int[] result = new int[length];
        for (int i=0; i<length; i++){
            result[i] = usage.get(i) + rootPitch;
        }
        return result;
    }

    //Returns a rest
    public int[] getRest(){
        return new int[]{REST};
    }

    //Returns a chord-usage - considering the deviation
    public ArrayList<Integer> calcDeviationInUsage(int deviation, Patternelement current, Patternelement old){
        int transposeDifference = current.getTranspose() - old.getTranspose();
        ArrayList<Integer> currentUsage = current.getChord().getUsage();
        int length = currentUsage.size();

        //Find position to start usage considering deviation
        int pos = 0;
        labelFindPosition:
        for(int i=deviation; i>=0; i--) {
            for (int j = 0; j < length; j++) {
                if ((currentUsage.get(j) + transposeDifference + i) == i ||
                    (currentUsage.get(j) + transposeDifference + i) == (i + 12)){
                    pos = j;
                    break labelFindPosition;
                }
            }
        }

        //Shift usage as long as the founded item with matching deviation is the first list-item
        Collections.rotate(currentUsage, length - pos);

        //Scale down the pitch of items as long as items left are bigger than items on the right
        for(int i=0; i<length; i++){
            if(i < (length - 1) && currentUsage.get(i) > currentUsage.get(i+1)){
                currentUsage.set(i, currentUsage.get(i) - 12);
            } else break;
        }

        return currentUsage;
    }

    //Returns an usage of a chord considering the chordcomplexity
    public ArrayList<Integer> generateChordcomplexity(Patternelement patternelement){
        ArrayList<Integer> usage = patternelement.getChord().getUsage();
        int complexity = new Random().nextInt(patternelement.getChordcomplexity().getMax() - patternelement.getChordcomplexity().getMin() + 1)
                         + patternelement.getChordcomplexity().getMin();
        int length = usage.size();
        if(complexity <= length){
            usage = new ArrayList<Integer>(usage.subList(0, complexity));
        } else {
            int pos = 0;
            for(int i=length; i<complexity; i++){
                usage.add(usage.get(pos) + 12);
                pos++;
            }
        }

        return usage;
    }

    //TODO: implement getRootBassNote with given pitch - 36 (3 octaves down)
}
