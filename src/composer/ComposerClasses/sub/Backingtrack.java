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
import composer.DataClasses.*;
import jm.music.data.*;
import jm.util.Play;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static composer.Main.p;

public class Backingtrack extends Composer {

    private Part piano = new Part("Piano", PIANO, 0);
    private Part bass = new Part("Bass", BASS, 1);
    private Part drums = new Part("Drums", DRUM, 2);
    private Tone tone;
    private int repeat;
    private double humanFactor;
    private ArrayList<Patternelement> pattern;
    private ArrayList<Range> eighthsProbabilityRanges;

    //TODO: cut old

    public Backingtrack(){}

    public Backingtrack(Boolean instruments[], int tempo, Tone tone, int repeat, ArrayList<Patternelement> pattern, double humanFactor, ArrayList<Range> eighthsProbabilityRanges){
        this.tone = tone;
        this.repeat = repeat;
        this.pattern = pattern;
        this.humanFactor = calcHumanizerFactor(humanFactor, 5);
        this.eighthsProbabilityRanges = eighthsProbabilityRanges;

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

            //TODO for every patternelement: extract tact, compose tact
            //TODO add Chords to part

            //Evtl
            //TODO class Tact contains Swing CPhrases
            //TODO class Composition extends Score
            //TODO class CompositionPart extends Part contains Tacts[]
        }

    }

    //Generates bass part in score
    public void generateBassPart(){

    }

    //Generates drums part in score
    public void generateDrumsPart(){

    }
}

/*

    1. Nehme Patternelement
    2. Prüfe ob Semi oder Full
        a) Wenn Semi: nimm dieses Element und das nächste und sende

 */