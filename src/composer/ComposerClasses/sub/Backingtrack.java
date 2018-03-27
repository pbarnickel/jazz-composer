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
import jm.util.View;
import jm.util.Write;

import javax.sound.midi.MidiUnavailableException;
import java.util.*;

import static composer.Main.p;

public class Backingtrack extends Composer {

    private Part piano;
    private Part bass;
    private Part drums_ride;
    private Part drums_snare;
    private int repeat;
    private ArrayList<Patternelement> pattern;
    private ArrayList<Eighth> eighths;
    private int deviation;
    private boolean style;

    public Backingtrack() throws MidiUnavailableException {
        super();
    }

    public Backingtrack(Boolean instruments[], int tempo, Tone tone, int repeat, ArrayList<Patternelement> pattern,
                        double humanizerTolerance, ArrayList<Eighth> eighths, int deviation, double dynamic) throws MidiUnavailableException {
        super();
        this.piano = new Part("Piano", PIANO, 0);
        this.bass = new Part("Bass", BASS, 1);
        this.drums_ride = new Part("Drums Ride", DRUM, 2);
        this.drums_snare = new Part("Drums Snare", DRUM, 2);
        this.tone = tone;
        this.repeat = repeat;
        this.pattern = pattern;
        this.eighths = eighths;
        this.deviation = deviation;
        this.humanizerTolerance = humanizerTolerance;
        this.dynamic = dynamic;
        this.style = false;

        //generate parts
        if(instruments[0])generatePianoPart();
        if(instruments[1])generateBassPart();
        if(instruments[2])generateDrumsPart();

        //Final operations
        score.addPart(piano);
        score.addPart(bass);
        score.addPart(drums_ride);
        score.addPart(drums_snare);
        score.setTempo(tempo);

        //Write MIDI audio-file for listening compositions in GUI
        Write.midi(score, settings.getDefault_location() + runtimeFile);
    }

    //Generates piano part in score
    public void generatePianoPart(){
        CPhrase bar;
        for (int i=0; i<repeat; i++){
            int lengthPattern = pattern.size();
            for (int j=0; j<lengthPattern; j++){
                if (pattern.get(j).getTactProportion().equals("Full")){
                    bar = generatePianoBar(new ArrayList<Patternelement>(Arrays.asList(pattern.get(j))));
                } else {
                    bar = generatePianoBar(new ArrayList<Patternelement>(Arrays.asList(pattern.get(j), pattern.get(j+1))));
                    j++;
                }
                piano.addCPhrase(bar);
            }
        }
    }

    //Generates bass part in score
    public void generateBassPart(){
        Phrase bar;
        for (int i=0; i<repeat; i++){
            this.style = new Random().nextBoolean();
            int lengthPattern = pattern.size();
            for (int j=0; j<lengthPattern; j++){
                if(pattern.get(j).getTactProportion().equals("Full")){
                    bar = generateBassBar(pattern.get(j));
                } else {
                    bar = generateBassBar(pattern.get(j));
                    j++;
                }
                bass.addPhrase(bar);
            }
        }
    }


    //Generates drums part in score
    public void generateDrumsPart(){
        Phrase bar;
        for(int i=0; i<repeat; i++){
            int length = 0;
            int lengthPattern = pattern.size();
            for(int j=0; j<lengthPattern; j++){
                length++;
                if(pattern.get(j).getTactProportion().equals("Semi")) j++;
            }
            for(int j=0; j<length; j++){
                bar = generateRide();
                drums_ride.addPhrase(bar);
                bar = generateSnare();
                drums_snare.addPhrase(bar);
            }
        }
    }

    //Generates a full piano-bar and returns result in a CPhrase
    public CPhrase generatePianoBar(ArrayList<Patternelement> patternpart){

        //Calculates pre-patternelement
        Patternelement prePatternelement;
        if(patternpart.get(0).getOrder()>0) prePatternelement = pattern.get(patternpart.get(0).getOrder() - 1);
        else prePatternelement = pattern.get(0);

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
        double humanizer = generateHumanizer(5);
        bar.addChord(getRest(), humanizer * calcStartOfEighthInBarByPosition(startEighth));
        //double sum = humanizer * calcStartOfEighthInBarByPosition(startEighth);
        //p(Double.toString(humanizer * calcStartOfEighthInBarByPosition(startEighth)));

        //Generate a for bar specific deviation between this chord and the last one
        int localDeviation = new Random().nextInt(deviation + 1);
        ArrayList<Integer> usage = calcDeviationInUsage(localDeviation, currentPatternelement, prePatternelement);
        currentPatternelement.getChord().setUsage(usage);
        //p(currentPatternelement.getChord().getUsageAsString());

        //Generate chordcomplexity
        usage = generateChordcomplexity(currentPatternelement);
        //for(int i=0; i<usage.size(); i++)p(Integer.toString(usage.get(i)));
        //p("-------------------------");

        //Generate and add chords by nr of uses → First uses [duration: 1.0], Last use [duration: 0.66666]
        for(int i=0; i<barUses.getBarUses().size(); i++){
            barUses.getBarUse(i).setDuration(barUses.getBarUse(i).getDuration() * generateHumanizer(5));
            bar.addChord(
                    getUsageInContext(
                        usage,tone.getPitch() + currentPatternelement.getTranspose() + barUses.getBarUse(i).getProcedure()
                    ),
                    barUses.getBarUse(i).getDuration()
            );
            //sum += barUses.getBarUse(i).getDuration();
            //p(Double.toString(barUses.getBarUse(i).getDuration()));
        }

        //Add end-rest
        humanizer = generateHumanizer(5);
        bar.addChord(getRest(), humanizer * barUses.getEndRest());
        //sum += humanizer * barUses.getEndRest();
        //p(Double.toString(humanizer * barUses.getEndRest()));
        //p("SUM:   " + Double.toString(sum));
        //p("---------------------------------------------");

        //Set dynamic of bar
        int d = generateDynamic();
        bar.setDynamic(d);

        return bar;
    }

    //Generates a full bass-bar and returns result in a Phrase
    public Phrase generateBassBar(Patternelement patternelement){
        Phrase bar = new Phrase();

        //TODO build phrase for [true|false]
        //TODO Consider dynamics
        //TODO Define bass depth pitch

        int nextRootPitch;
        if(patternelement.getOrder() < pattern.size() - 1) {
            Patternelement next = pattern.get(patternelement.getOrder() + 1);
            int a = next.getChord().getUsage().get(0);
            int b = next.getTranspose();
            int c = tone.getPitch();
            nextRootPitch = a + b + c;
        } else nextRootPitch = patternelement.getChord().getUsage().get(0) + tone.getPitch() + patternelement.getTranspose();
        bar = generateWalkingBass(patternelement, nextRootPitch);
        View.internal(bar);

        //[TRUE] → Walking-Bass, [FALSE] → Standard-Bass
        if(style){
        } else {
        }

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