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
    private int deviation;

    public Backingtrack(){}

    public Backingtrack(Boolean instruments[], int tempo, Tone tone, int repeat, ArrayList<Patternelement> pattern, double humanizerTolerance, ArrayList<Eighth> eighths, int deviation){
        this.piano = new Part("Piano", PIANO, 0);
        this.bass = new Part("Bass", BASS, 1);
        this.drums = new Part("Drums", DRUM, 2);
        this.tone = tone;
        this.repeat = repeat;
        this.pattern = pattern;
        this.eighths = eighths;
        this.deviation = deviation;
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
                Patternelement oldPatternelement = pattern.get(j);
                if(j>0)oldPatternelement = pattern.get(j-1);
                if (pattern.get(j).getTactProportion().equals("Full")){
                    bar = generatePianoBar(new ArrayList<Patternelement>(Arrays.asList(pattern.get(j))), oldPatternelement);
                } else {
                    bar = generatePianoBar(new ArrayList<Patternelement>(Arrays.asList(pattern.get(j), pattern.get(j+1))), oldPatternelement);
                    j++;
                }
                piano.addCPhrase(bar);
            }
        }
    }

    //Generates bass part in score
    public void generateBassPart(){
        Phrase bar = new Phrase();
        for (int i=0; i<repeat; i++){
            int lengthPattern = pattern.size();
            for (int j=0; j<lengthPattern; j++){
                Note note = new Note(C4, WHOLE_NOTE);
                bar.addNote(note);
                bass.addPhrase(bar);
            }
        }
    }


    //Generates drums part in score
    public void generateDrumsPart(){

    }

    //Generates a full bar and returns result in a CPhrase
    public CPhrase generatePianoBar(ArrayList<Patternelement> patternpart, Patternelement oldPatternelement){

        //Generate random eighth positions
        int startEighth = calcStartEighth();

        //Select patternelement of bar-part by eighth-position → patternpart.size = {1..2} (1 x Full (1) --- or 2 x Semi (2))
        Patternelement currentPatternelement;
        int lengthPatternpart = patternpart.size();
        if(startEighth < 4){
            currentPatternelement = patternpart.get(0);
        } else {
            currentPatternelement = patternpart.get(lengthPatternpart-1);
        }

        //Generate bar-uses
        BarUses barUses = new BarUses(startEighth);

        //Add start-rest
        CPhrase bar = new CPhrase();
        double sum = 0;
        bar.addChord(getRest(), calcStartOfEighthInBarByPosition(startEighth));
        sum += calcStartOfEighthInBarByPosition(startEighth);
        p("  Rest: " + Double.toString(calcStartOfEighthInBarByPosition(startEighth)));

        //Generate a for bar specific deviation between this chord and the last one
        int localDeviation = new Random().nextInt(deviation + 1);
        ArrayList<Integer> usage = calcDeviationInUsage(localDeviation, currentPatternelement, oldPatternelement);

        //Generate chordcomplexity
        usage = generateChordcomplexity(currentPatternelement);

        //Generate and add chords by nr of uses → First uses [duration: 1.0], Last use [duration: 0.66666]
        //TODO: Consider Chordcomplexity and chord-alternatives
        for(int i=0; i<barUses.getBarUses().size(); i++){
            bar.addChord(
                    getUsageInContext(
                        usage,tone.getPitch() + currentPatternelement.getTranspose() + barUses.getBarUses().get(i).getProcedure()
                    ),
                    barUses.getBarUses().get(i).getDuration()
            );
            sum += barUses.getBarUses().get(i).getDuration();
            p("  Use : " + Double.toString(barUses.getBarUses().get(i).getDuration()));
        }

        //Add end-rest
        bar.addChord(getRest(), barUses.getEndRest());
        sum += barUses.getEndRest();
        p("  Rest: " + Double.toString(barUses.getEndRest()));
        p("SUM:    " + Double.toString(sum));
        p("-----------------------------------------------");

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