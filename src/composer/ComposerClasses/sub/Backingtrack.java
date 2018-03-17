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
import java.util.*;

import static composer.Main.p;

public class Backingtrack extends Composer {

    private Part piano = new Part("Piano", PIANO, 0);
    private Part bass = new Part("Bass", BASS, 1);
    private Part drums = new Part("Drums", DRUM, 2);
    private Tone tone;
    private int repeat;
    private ArrayList<Patternelement> pattern;
    private ArrayList<Eighth> eighths;

    public Backingtrack(){}

    public Backingtrack(Boolean instruments[], int tempo, Tone tone, int repeat, ArrayList<Patternelement> pattern, double humanizerTolerance, ArrayList<Eighth> eighths){
        this.tone = tone;
        this.repeat = repeat;
        this.pattern = pattern;
        this.eighths = eighths;
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

        ArrayList<CPhrase> bar;
        for (int i=0; i<repeat; i++){
            int lengthPattern = pattern.size();
            for (int j=0; j<lengthPattern; j++){
                if (pattern.get(j).getTactProportion().equals("Full")){
                    bar = calcBar(new ArrayList<Patternelement>(Arrays.asList(pattern.get(j))));
                } else {
                    bar = calcBar(new ArrayList<Patternelement>(Arrays.asList(pattern.get(j), pattern.get(j+1))));
                    j++;
                }
                piano = addCPhrasesToPart(piano, bar);
            }
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

        //Get random number {1..max} for the number of uses in a bar
        int nrOfUsesInBar = new Random().nextInt(Math.min(3,eighths.size())) + 1;

        //Calculates random eighth positions
        ArrayList<Integer> eighthPositions = calcEighthPositions(nrOfUsesInBar);
        p("-----------------------------------------------------------------------------");

        return bar;
    }

    //Calc random start-position for CPhrase-uses in bar considering the eighth-probabilities
    public ArrayList<Integer> calcEighthPositions(int nrOfUsesInBar){
        ArrayList<Integer> eighthRandomPositions = new ArrayList<Integer>();
        int rand;
        int lengthEighths = eighths.size();
        for (int i=0; i<nrOfUsesInBar; i++){
            //Generating random position as long as eighth is unique in list
            do {
                rand = new Random().nextInt(eighths.get(eighths.size()-1).getRange().getEnd()+1);
                //Identifying eighth by range
                for (int j=0; j<lengthEighths; j++) {
                    if (eighths.get(j).getRange().isInRange(rand)) {
                        //Overwriting random number with random position
                        rand = eighths.get(j).getPosition();
                        continue;
                    }
                }
            } while (eighthRandomPositions.indexOf(rand)>-1);
            //Adding eighth to list
            eighthRandomPositions.add(rand);
        }

        //Sorting the list
        Collections.sort(eighthRandomPositions);

        for(int i=0; i<eighthRandomPositions.size(); i++){
            p(Integer.toString(eighthRandomPositions.get(i)));
        }

        return eighthRandomPositions;
    }
}