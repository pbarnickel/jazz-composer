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

    private Part piano = new Part("Piano", PIANO, 0);
    private Part bass = new Part("Bass", BASS, 1);
    private Part drums = new Part("Drums", DRUM, 2);
    private Tone tone;
    private int repeat;
    private ArrayList<Patternelement> pattern;

    //TODO: cut old

    public Backingtrack(){}

    public Backingtrack(Boolean instruments[], int tempo, Tone tone, int repeat, ArrayList<Patternelement> pattern){
        this.tone = tone;
        this.repeat = repeat;
        this.pattern = pattern;

        //generate parts
        if(instruments[0])generatePianoPart();
        if(instruments[1])generateBassPart();
        if(instruments[2])generateDrumsPart();

        //Final operations
        score.addPart(piano);
        score.addPart(bass);
        score.addPart(drums);
        score.setTempo(tempo);

        //TODO Generate Piano Part
        //TODO Generate Bass Part
        //TODO Generate Drums Part
    }

    //Generates piano part in score
    public void generatePianoPart(){

        for(int i=0; i<repeat; i++){

        }

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
    }

    //Generates bass part in score
    public void generateBassPart(){

    }

    //Generates drums part in score
    public void generateDrumsPart(){

    }
}
