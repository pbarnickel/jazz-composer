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
import java.util.Arrays;

import static composer.Main.p;

public class Backingtrack extends Composer {

    private Part piano = new Part("Piano", PIANO, 0);
    private Part bass = new Part("Bass", BASS, 1);
    private Part drums = new Part("Drums", DRUM, 2);
    private Tone tone;
    private int repeat;
    private ArrayList<Patternelement> pattern;
    private ArrayList<Range> eighthsProbabilityRanges;

    public Backingtrack(){}

    public Backingtrack(Boolean instruments[], int tempo, Tone tone, int repeat, ArrayList<Patternelement> pattern, double humanizerTolerance, ArrayList<Range> eighthsProbabilityRanges){
        this.tone = tone;
        this.repeat = repeat;
        this.pattern = pattern;
        this.eighthsProbabilityRanges = eighthsProbabilityRanges;
        this.humanizerTolerance = humanizerTolerance;
        initHumanizer(5);

        //generate parts
        if(instruments[0])generatePianoPart();
        if(instruments[1])generateBassPart();
        if(instruments[2])generateDrumsPart();

        //Final operations
        score.addPart(piano);
        score.addPart(bass);
        score.addPart(drums);
        score.setTempo(tempo);
    }

    //Generates piano part in score
    public void generatePianoPart(){

        for(int i=0; i<repeat; i++){
            int lengthPattern = pattern.size();
            for(int j=0; j<lengthPattern; j++){
                ArrayList<CPhrase> bar;
                if(pattern.get(j).getTactProportion().equals("Full")){
                    bar = calcBar(new ArrayList<Patternelement>(Arrays.asList(pattern.get(j))));
                    p("Pattern Full - 1.: " + pattern.get(j).getTactProportion());
                } else if(pattern.get(j).getTactProportion().equals("Semi")) {
                    bar = calcBar(new ArrayList<Patternelement>(Arrays.asList(pattern.get(j), pattern.get(j+1))));
                    j++;
                    p("Pattern Semi - 1.: " + pattern.get(j).getTactProportion() + ", 2.: " + pattern.get(j).getTactProportion());
                }
            }
            p("----------------------------------------------------------------------------------");

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

    //Calculates a full bar and returns result in a CPhrase-List
    public ArrayList<CPhrase> calcBar(ArrayList<Patternelement> patternpart){
        ArrayList<CPhrase> bar = new ArrayList<CPhrase>();
        int length = patternpart.size();
        int nrOfUsesInBar = getNrOfUsesInBar(3);
        p("Size: " + Integer.toString(length) + ", Uses: " + Integer.toString(nrOfUsesInBar));
        return bar;
    }
}