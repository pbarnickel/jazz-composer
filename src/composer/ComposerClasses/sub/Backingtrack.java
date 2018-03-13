/*
    Description:    Composer class for generating a backing track considering of user-inputs.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018

        Klasse zur Generierung von Jazz-Backing-Tracks im Barnickel Style

        Eingabe: Pitch, Name und Angabe der Länge des Backing-Tracks
        Verarbeitung:   Generierung der Akkordfolge im Barnickel-Style
        Example:    C ->    A ->    D ->    G ->    ...
                    root    -3      +2      -5
        Ausgabe: Score (Datenstruktur mit mehreren aufeinanderfolgenden Akkorden)

        Fortführend:    - 'Dynamisieren' der Backing Track Intelligenz hinsichtlich
                        - Variieren der Akkorde im Schema   (-> Inversion)
                        - Zufallsprinzipien der genutzten Akkorde
                        - Einbringen von Bass und Schlagzeug
*/

package composer.ComposerClasses.sub;

import composer.ComposerClasses.Composer;
import composer.DataClasses.MusicStructure;
import composer.DataClasses.MusicStructureGroup;
import composer.DataClasses.Patternelement;
import composer.DataClasses.Tone;
import jm.music.data.*;
import jm.util.Play;

import java.util.ArrayList;

import static composer.Main.p;

public class Backingtrack extends Composer {

    private Part test = new Part("Piano", 0, 10);
    private Part piano = new Part("Piano", PIANO, 0);
    private Part bass = new Part("Bass", BASS, 1);
    private Part drums = new Part("Drums", DRUM, 2);
    private Tone tone;
    private int tempo;
    private int repeat;
    private ArrayList<Patternelement> pattern;

    //TODO: cut old

    public Backingtrack(){}

    public Backingtrack(Boolean instruments[], int tempo, Tone tone, int repeat, ArrayList<Patternelement> pattern){
        //transfer input data into data model
        this.tempo = tempo;
        this.tone = tone;
        this.repeat = repeat;
        this.pattern = pattern;

        //generate Parts
        if(instruments[0])generatePianoPart();
        if(instruments[1])generateBassPart();
        if(instruments[2])generateDrumsPart();
    }

    public MusicStructureGroup filterComplexity(){
        MusicStructureGroup newChordgroup = new MusicStructureGroup();
        int length = chordgroup.getNrOfMusicStructures();
        int size;
        for(int i=0; i<length; i++){
            size = chordgroup.getMusicStructures().get(i).getUsage().size();
            if(size <= chordcomplexity.getMax() && size >= chordcomplexity.getMin()){
                newChordgroup.addMusicStructure(chordgroup.getMusicStructures().get(i));
            }
        }
        return newChordgroup;
    }

    public void generatePianoPart(){
        CPhrase chord;
        MusicStructure randChord;

        /*for(int i=0; i<repeat; i++) {
            for (int j = 0; j < this.pattern.size(); j++) {
                randChord = getRandomChord();
                chord = getChord(this.pattern.get(j), randChord, C);
                piano.addCPhrase(chord);
                chord = getChord(this.pattern.get(j), randChord, EIGHTH_NOTE);
                piano.addCPhrase(chord);

                //bass_note = new Note(this.pattern.get(j) - 36, QUADRAPHONIC);          // root bass note
                //bass_note = getRandomBassNote(this.tone, randChord);
                //bass_phrase.addNote(bass_note);
                //bass.addPhrase(bass_phrase);
            }
        }*/

        score.add(piano);
        score.add(bass);
        score.setTempo(this.tempo);

        //TODO Generate Piano Part
        //TODO Generate Bass Part
        //TODO Generate Drums Part
        //TODO Add parts to Score
        //TODO Return different data-structures for printing in UI
    }

    public void generateBassPart(){

    }

    public void generateDrumsPart(){

    }

    /******************************************** TEST *****************************************************/

    public Score getBackingTrack(int rootPitch, String name, int len){
        score.setTitle(name);
        for(int i=0; i<len; i++){
            addFullJazzChord(rootPitch);
            addFullJazzChordInv(rootPitch);
            addFullJazzChordMinThree(rootPitch - 3);
            addFullJazzChordDMaj(rootPitch + 2);
            addFullJazzChordG(rootPitch - 5);
        }
        score.addPart(test);
        return score;
    }

    public void addFullJazzChordG(int rp){
        int[] pitchArray = new int[5];
        pitchArray[0] = rp - 12;
        pitchArray[1] = rp - 2;
        pitchArray[2] = rp + 1;
        pitchArray[3] = rp + 4;
        pitchArray[4] = rp + 7;
        CPhrase chord = new CPhrase();
        chord.addChord(pitchArray, C);
        test.addCPhrase(chord);
    }

    public void addFullJazzChordDMaj(int rp){
        int[] pitchArray = new int[5];
        pitchArray[0] = rp - 12;
        pitchArray[1] = rp + 3;
        pitchArray[2] = rp + 7;
        pitchArray[3] = rp + 10;
        pitchArray[4] = rp + 14;
        CPhrase chord = new CPhrase();
        chord.addChord(pitchArray, C);
        test.addCPhrase(chord);
    }

    public void addFullJazzChordMinThree(int rp){
        int[] pitchArray = new int[6];
        pitchArray[0] = rp - 12;
        pitchArray[1] = rp - 2;
        pitchArray[2] = rp + 1;
        pitchArray[3] = rp + 4;
        pitchArray[4] = rp + 7;
        pitchArray[5] = rp + 10;
        CPhrase chord = new CPhrase();
        chord.addChord(pitchArray, C);
        test.addCPhrase(chord);
    }

    public void addFullJazzChord(int rp){
        int[] pitchArray = new int[5];
        pitchArray[0] = rp;
        pitchArray[1] = rp + 4;
        pitchArray[2] = rp + 7;
        pitchArray[3] = rp + 9;
        pitchArray[4] = rp + 12;
        CPhrase chord = new CPhrase();
        chord.addChord(pitchArray, C);
        test.addCPhrase(chord);
    }

    public void addFullJazzChordInv(int rp){
        int[] pitchArray = new int[5];
        pitchArray[0] = rp + 9;
        pitchArray[1] = rp + 12;
        pitchArray[2] = rp + 14;
        pitchArray[3] = rp + 17;
        pitchArray[4] = rp + 21;
        CPhrase chord = new CPhrase();
        chord.addChord(pitchArray, C);
        test.addCPhrase(chord);
    }
}
