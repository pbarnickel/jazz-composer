/*
    Description:    Root-Composer-Class for manipulating the Score including some common methods for
                    interpreting user-input.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ComposerClasses;

import composer.DataClasses.Patternelement;
import composer.DataClasses.Settings;
import composer.DataClasses.Tone;
import composer.Interfaces.Constants;
import jm.JMC;
import jm.music.data.*;
import jm.util.Read;
import jm.util.View;
import jm.util.Write;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import java.io.*;
import java.util.*;

import static composer.Main.p;

public class Composer implements JMC, Constants {

    protected Score score = new Score();
    protected Settings settings = new Settings();
    protected double humanizerTolerance;
    protected double dynamic;
    protected Tone tone;
    protected String runtimeFile = "/runtime.mid";
    private Sequencer sequencer = MidiSystem.getSequencer();

    public Composer() throws MidiUnavailableException {
        //Loading settings
        settings.loadSettings();
    }

    public Score getScore(){
        return this.score;
    }

    public Sequencer getSequencer(){
        return this.sequencer;
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
    public void playScore() throws MidiUnavailableException, IOException, InvalidMidiDataException {
        if(sequencer.isOpen()){
            sequencer.start();
        } else {
            sequencer.open();
            InputStream is = new BufferedInputStream(new FileInputStream(new File(settings.getDefault_location() + runtimeFile)));
            sequencer.setSequence(is);
            sequencer.start();
        }
    }

    //Pauses playing MIDI
    public void pauseScore(){
        sequencer.stop();
    }

    //Stops playing MIDI
    public void stopScore(){
        sequencer.stop();
        sequencer.close();
    }

    //Shows statistics of score
    public void showStatistics(){
        View.histogram(score);
    }

    //Returns random humanizer-factor for multiplication in algorithms considering the humanizer tolerance
    public double generateHumanizer(int percentage){
        double factor = new Random().nextInt((int) (humanizerTolerance + 1)) / 100.0;
        factor *= percentage / 100.0;
        if(new Random().nextBoolean()) return 1.0 - factor;
        else return 1.0 + factor;
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
    public ArrayList<Integer> calcDeviationInUsage(int deviation, Patternelement current, Patternelement pre){
        int transposeDifference = current.getTranspose() - pre.getTranspose();
        ArrayList<Integer> currentUsage = new ArrayList<>();
        currentUsage.addAll(current.getChord().getUsage());
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
        ArrayList<Integer> usage = new ArrayList<>();
        usage.addAll(patternelement.getChord().getUsage());
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

    //Returns a random dynamic-value
    public int generateDynamic(){
        int tolerance = new Random().nextInt(10) + 10;
        double toleranceFactor = tolerance / 100.0;
        if(new Random().nextBoolean()) toleranceFactor += 1.0;
        else toleranceFactor = 1.0 - toleranceFactor;
        toleranceFactor *= generateHumanizer(100) * dynamic / 100.0;
        int value = (int) (toleranceFactor * 127);
        if(value > 127) value = 127;
        return value;
    }

    //Returns a bass-note (3 octaves decremented)
    public Note getBassNote(int pitch){
        return new Note(pitch - 24, QUARTER_NOTE);
    }

    //Returns a bass-phrase of a full-bar-used chord
    public Phrase generateWalkingBass(Patternelement patternelement, int rootPitchOfNext){
        Phrase bar = new Phrase();
        int root = patternelement.getChord().getUsage().get(0);
        int transpose = this.tone.getPitch() + patternelement.getTranspose();
        bar.addNote(getBassNote(patternelement.getChord().getUsage().get(0) + transpose));
        bar.addNote(getBassNote(patternelement.getChord().getUsage().get(1) + transpose));
        bar.addNote(getBassNote(patternelement.getChord().getUsage().get(2) + transpose));
        bar.addNote(getBassNote(rootPitchOfNext - 1));
        //bar.addNote(getBassNote(REST));
        return bar;
    }

    //Returns a ride-phrase
    public Phrase generateRide(){
        Phrase bar = new Phrase();
        int ride = 51;
        bar.addNote(new Note(ride, QUARTER_NOTE));
        bar.addNote(new Note(ride, QUARTER_NOTE_TRIPLET));
        bar.addNote(new Note(ride, EIGHTH_NOTE_TRIPLET));
        bar.addNote(new Note(ride, QUARTER_NOTE));
        bar.addNote(new Note(ride, QUARTER_NOTE));
        return bar;
    }

    //Returns a snare-phrase
    public Phrase generateSnare(){
        Phrase bar = new Phrase();
        int snare = 38;
        for(int i=0; i<4; i++){
            bar.addNote(new Note(REST, QUARTER_NOTE_TRIPLET));
            bar.addNote(new Note(snare, EIGHTH_NOTE_TRIPLET, (int)(Math.random()*60)));
        }
        return bar;
    }
}
