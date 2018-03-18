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

import com.sun.deploy.security.CPCallbackHandler;
import composer.ComposerClasses.Composer;
import composer.DataClasses.*;
import jm.music.data.*;
import jm.util.Play;

import java.lang.reflect.Array;
import java.util.*;

import static composer.Main.p;

public class Backingtrack extends Composer {

    private Part piano;
    private Part bass;
    private Part drums;
    private Tone tone;
    private int repeat;
    private ArrayList<Patternelement> pattern;
    private ArrayList<Eighth> eighths;

    public Backingtrack(){}

    public Backingtrack(Boolean instruments[], int tempo, Tone tone, int repeat, ArrayList<Patternelement> pattern, double humanizerTolerance, ArrayList<Eighth> eighths){
        this.piano = new Part("Piano", PIANO, 0);
        this.bass = new Part("Bass", BASS, 1);
        this.drums = new Part("Drums", DRUM, 2);
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

        CPhrase bar;
        for (int i=0; i<repeat; i++){
            int lengthPattern = pattern.size();
            for (int j=0; j<lengthPattern; j++){
                if (pattern.get(j).getTactProportion().equals("Full")){
                    bar = generateBar(new ArrayList<Patternelement>(Arrays.asList(pattern.get(j))));
                } else {
                    bar = generateBar(new ArrayList<Patternelement>(Arrays.asList(pattern.get(j), pattern.get(j+1))));
                    j++;
                }
                piano.addCPhrase(bar);
            }
        }
    }

    //Generates bass part in score
    public void generateBassPart(){

    }


    //Generates drums part in score
    public void generateDrumsPart(){

    }

    //Generates a full bar and returns result in a CPhrase
    public CPhrase generateBar(ArrayList<Patternelement> patternpart){

        //Generate random eighth positions
        int startEighth = calcStartEighth();

        //Select patternelement of bar-part by eighth-position → patternpart.size = {1..2} (1 x Full (1) --- or 2 x Semi (2))
        Patternelement currentPatternelemnt;
        int lengthPatternpart = patternpart.size();
        if(startEighth < 4){
            currentPatternelemnt = patternpart.get(0);
        } else {
            currentPatternelemnt = patternpart.get(lengthPatternpart-1);
        }

        //Generate bar-uses
        BarUses barUses = new BarUses(startEighth, eighths.size());

        //Add start-rest
        CPhrase bar = new CPhrase();
        bar.addChord(getRest(), calcStartOfEighthInBarByPosition(startEighth));

        //Generate and add chords by nr of uses → First uses [duration: 1.0], Last use [duration: 0.66666]
        for(int i=0; i<barUses.getBarUses().size(); i++){
            bar.addChord(
                    getUsageInContext(
                        currentPatternelemnt.getChord().getUsage(),tone.getPitch() + currentPatternelemnt.getTranspose() + barUses.getBarUses().get(i).getProcedure()
                    ),
                    barUses.getBarUses().get(i).getDuration()
            );
        }

        //Add end-rest
        bar.addChord(getRest(), barUses.getEndRest());

        return bar;
    }

    //Calculates random start-position for CPhrase-uses in bar considering the eighth-probabilities
    public int calcStartEighth(){
        //Get random number
        int rand = new Random().nextInt(eighths.get(eighths.size()-1).getRange().getEnd()+1);
        int lengthEighths = eighths.size();

        //Identifying eighth by range
        for (int j=0; j<lengthEighths; j++) {
            if (eighths.get(j).getRange().isInRange(rand)) {
                return eighths.get(j).getPosition();
            }
        }

        return 0;
    }

    //Calculates start of an eighth in a bar by position
    public double calcStartOfEighthInBarByPosition(int position){
        double start = position / 2;
        start += (position % 2) * QUARTER_NOTE * 2 / 3;
        return start;
    }

}