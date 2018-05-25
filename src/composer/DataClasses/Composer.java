/*
    Description:    Root-Composer-Class for manipulating the Score including some common methods for
                    interpreting user-input.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.DataClasses;

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

    //Common data
    private Settings settings = new Settings();
    private String runtimeFile = "/runtime.mid";
    private Sequencer sequencer = MidiSystem.getSequencer();
    private BJCProjectFileHandler bjcProjectFileHandler = new BJCProjectFileHandler();

    //Composition data
    private Score score = new Score();
    private Part piano;
    private Part bass;
    private Part drums_ride;
    private Part drums_snare;
    private Part trumpet;

    //User data
    private General general;
    private Chordprogression chordprogression;
    private Backingtrack backingtrack;
    private Melody melody;
    private Swing swing;

    public Composer() throws MidiUnavailableException {
        //Loading settings
        settings.loadSettings();
    }

    public Composer(General general, Chordprogression chordprogression, Backingtrack backingtrack, Melody melody, Swing swing) throws MidiUnavailableException {
        //Loading settings
        settings.loadSettings();

        //Taking over user data
        this.general = general;
        this.chordprogression = chordprogression;
        this.backingtrack = backingtrack;
        this.melody = melody;
        this.swing = swing;

        //Initialize instrument data
        this.piano = new Part("Piano", PIANO, 0);
        this.bass = new Part("Bass", BASS, 1);
        this.drums_ride = new Part("Drums Ride", 0,9);
        this.drums_snare = new Part("Drums Snare", 0,9);
        this.trumpet = new Part("Trumpet Melody", TRUMPET, 2);

        //Composing
        generateComposition();
        finalizeComposition();
    }

    /************************************** Common methods ***************************************/

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

    //Saves composition as BJC-Project-File
    public void writeBJCProjectFile(String path){
        bjcProjectFileHandler.writeBJC(this, path);
    }

    //Reads a BJC-Project-File as Composer-object
    public void readBJCProjectFile(String path) throws MidiUnavailableException {
        Composer composer = bjcProjectFileHandler.readBJC(path);
        if(composer != null) {
            this.general = composer.general;
            this.chordprogression = composer.chordprogression;
            this.backingtrack = composer.backingtrack;
            this.melody = composer.melody;
            this.swing = composer.swing;
        }
    }

    public General getGeneral() {return general;}
    public Chordprogression getChordprogression() {return chordprogression;}
    public Backingtrack getBackingtrack() {return backingtrack;}
    public Melody getMelody() {return melody;}
    public Swing getSwing() {return swing;}
    public void setGeneral(General general) {this.general = general;}
    public void setChordprogression(Chordprogression chordprogression) {this.chordprogression = chordprogression;}
    public void setBackingtrack(Backingtrack backingtrack) {this.backingtrack = backingtrack;}
    public void setMelody(Melody melody) {this.melody = melody;}
    public void setSwing(Swing swing) {this.swing = swing;}

    /********************************* Composer methods *****************************************************/

    //Generates compositions of different instruments
    public void generateComposition(){
        if(backingtrack.getPiano()) generatePianoPart();
        if(backingtrack.getBass())  generateBassPart();
        if(backingtrack.getDrums()) generateDrumsPart();
        if(melody.getState())       generateTrumpetPart();
    }

    //Final steps in composition
    public void finalizeComposition(){
        score.addPart(piano);
        score.addPart(bass);
        score.addPart(drums_ride);
        //score.addPart(drums_snare);
        score.addPart(trumpet);
        score.setTempo(general.getTempo());

        //Write MIDI audio-file for listening compositions in GUI
        Write.midi(score, settings.getDefault_location() + runtimeFile);
    }

    //Returns randomly a style-type with a user-probability as input
    //for example for bass style (walking-bass, normal) or trumpet style (bebop, normal)
    public boolean randomStyle(double userProbability){
        int rand = new Random().nextInt(100) + 1;
        Range range = new Range(0, (int) userProbability);
        if(range.isInRange(rand)) return true;
        return false;
    }

    //Calculates random start-position for CPhrase-uses in bar considering the eighth-probabilities
    public int calcStartEighth(){
        //Get random number
        int rand = new Random().nextInt(swing.getEighths().get(swing.getSize()-1).getRange().getEnd()+1);
        int lengthEighths = swing.getSize();

        //Identifying eighth by range
        for (int j=0; j<lengthEighths; j++) {
            if (swing.getEighth(j).getRange().isInRange(rand)) {
                return swing.getEighth(j).getPosition();
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

    //Returns random humanizer-factor for multiplication in algorithms considering the humanizer tolerance
    public double generateHumanizer(int percentage){
        double factor = new Random().nextInt((int) (general.getHumanizerTolerance() + 1)) / 100.0;
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

        //If complexity is smaller or equal than length -> the usage gets a cut or stays
        if(complexity <= length){
            usage = new ArrayList<Integer>(usage.subList(0, complexity));
        } else {
            int pos = 0;

            //Adds the new usage-pitches until the length of usage is as long as complexity
            for(int i=length; i<complexity; i++){
                usage.add(usage.get(pos) + 12);
                pos++;
            }
        }

        return usage;
    }

    //Returns a random dynamics-value
    public int generateDynamics(){
        int tolerance = new Random().nextInt(10) + 10;
        double toleranceFactor = tolerance / 100.0;
        if(new Random().nextBoolean()) toleranceFactor += 1.0;
        else toleranceFactor = 1.0 - toleranceFactor;
        toleranceFactor *= generateHumanizer(100) * general.getDynamics() / 100.0;
        int value = (int) (toleranceFactor * 127);
        if(value > 127) value = 127;
        return value;
    }

    //Returns a piano-melody-note (1 octave incremented)
    public Note getMelodyNote(int pitch, double duration){
        return new Note(pitch + 12, duration);
    }

    //Returns a bass-note (2 octaves decremented)
    public Note getBassNote(int pitch){
        return new Note(pitch - 24, QUARTER_NOTE);
    }

    //Returns a bass-phrase of a full-bar-used chord in a walking-bass style
    public Phrase generateWalkingBass(Patternelement patternelement, int rootPitchOfNext){
        Phrase bar = new Phrase();
        int transpose = general.getTone().getPitch() + patternelement.getTranspose();
        bar.addNote(getBassNote(patternelement.getChord().getUsage().get(0) + transpose));
        bar.addNote(getBassNote(patternelement.getChord().getUsage().get(1) + transpose));
        bar.addNote(getBassNote(patternelement.getChord().getUsage().get(0) + transpose));
        bar.addNote(getBassNote(rootPitchOfNext - 1));
        return bar;
    }

    //Returns a bass-phrase of a full-bar-used chord in a normal style
    public Phrase generateNormalBass(Patternelement patternelement, int rootPitchOfNext){
        Phrase bar = new Phrase();
        //int root = patternelement.getChord().getUsage().get(0);
        int transpose = general.getTone().getPitch() + patternelement.getTranspose();
        bar.addNote(getBassNote(patternelement.getChord().getUsage().get(0) + transpose));
        bar.addNote(getBassNote(24));       //REST makes a shrill sound play, so 24 - 24 = 0
        bar.addNote(getBassNote(24));       //REST makes a shrill sound play, so 24 - 24 = 0
        bar.addNote(getBassNote(rootPitchOfNext - 1));
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

    //Calculates humanizer-tolerance (duration) in bass, drums or trumpet-bar
    public Phrase calcHumanizerInBar(Phrase phrase, double possibleScope){

        int length = phrase.length();
        double savePossibleScope = possibleScope;
        double rest = Math.abs(generateHumanizer(OV_HUMANIZER_PERCENTAGE) * (possibleScope / length) - (possibleScope / length));
        Phrase newPhrase = new Phrase();
        newPhrase.addNote(new Note(getRest()[0], rest));
        possibleScope -= rest;
        double duration;
        for(int i=0; i<length; i++){
            duration = phrase.getNote(i).getRhythmValue() * generateHumanizer(OV_HUMANIZER_PERCENTAGE);
            if(possibleScope < duration){
                duration = possibleScope;
            }
            newPhrase.addNote(new Note(phrase.getNote(i).getPitch(), duration));
            possibleScope -= duration;
        }
        if(possibleScope > 0) {
            rest = possibleScope;
            newPhrase.addNote(new Note(getRest()[0], rest));
        } else {
            double difference = phrase.getEndTime() - savePossibleScope;
            newPhrase.getNote(newPhrase.getSize() - 1).setRhythmValue(newPhrase.getNote(newPhrase.getSize() - 1).getRhythmValue() - difference);
        }

        return newPhrase;
    }

    //Returns a trumpet-phrase of a bar in a bebop-trumpet style
    public Phrase generateTrumpet(Patternelement patternelement, int goalPitch, Range rangeOfUses, ArrayList<Double> durations, int probabilityOfRest){
        Phrase bar = new Phrase();
        double possibleScope = WHOLE_NOTE;
        if(patternelement.getTactProportion().equals("Semi")) possibleScope = HALF_NOTE;
        int uses = new Random().nextInt(rangeOfUses.getEnd() - rangeOfUses.getStart() + 1);
        uses += rangeOfUses.getStart();

        //Generate random eighth positions
        int startEighth = calcStartEighth();
        if(possibleScope == HALF_NOTE){
            if(startEighth > 3) startEighth -= 4;
        }

        //Add start-rest
        double rest = calcStartOfEighthInBarByPosition(startEighth);
        bar.add(new Note(getRest()[0], rest));
        possibleScope -= rest;

        //Reserve pre-note
        int len = durations.size();
        double duration_end;
        do {
            duration_end = durations.get(new Random().nextInt(len));
        } while(duration_end > possibleScope);
        uses--;
        possibleScope -= duration_end;

        //Main part
        double biggestDuration = durations.get(0);
        double duration;
        int pitch;
        int probRest;
        for(int i=1; i<len; i++) if(durations.get(i) > biggestDuration) biggestDuration = durations.get(i);
        while(uses > 0){
            if(possibleScope >= biggestDuration || possibleScope < EIGHTH_NOTE_TRIPLET) {
                duration = durations.get(new Random().nextInt(len));
                if(possibleScope < EIGHTH_NOTE_TRIPLET) {
                    duration = possibleScope;
                    uses = 0;
                }
                probRest = new Random().nextInt(100);
                //Note
                if (probRest > probabilityOfRest) {
                    pitch = getRandomTrumpetPitch(patternelement.getChord(), patternelement.getTranspose(), patternelement.getMode());
                    bar.addNote(getMelodyNote(pitch, duration));
                }
                //Rest
                else {
                    bar.addNote(new Note(getRest()[0], duration));
                }
                possibleScope -= duration;
                uses--;
            } else {
                //Remove old biggestDuration and identify new one in durations-list
                for (int i = 1; i < len; i++) {
                    if (durations.get(i) == biggestDuration) {
                        durations.remove(i);
                        len--;
                    }
                }
                biggestDuration = durations.get(0);
                for (int i = 1; i < len; i++)
                    if (durations.get(i) > biggestDuration) biggestDuration = durations.get(i);
            }
        }

        //Last note
        bar.addNote(getMelodyNote(goalPitch - 1, duration_end));

        //Add end-rest
        Note endRest = new Note(getRest()[0], possibleScope);
        bar.addNote(endRest);

        //Sort pitches -> decided randomly
        if(randomStyle(melody.getSortOfPitches())) {
            bar = sortTrumpetPitches(bar);
        }

        return bar;
    }

    //Returns a random pitch by a root-pitch from the matching scale
    public int getRandomTrumpetPitch(MusicStructure chord, int rootPitch, String mode){
        //Scale-specific calculations
        int pitch;
        MusicStructure scale;
        if(melody.getMelodyByScale()) {
            scale = melody.getMajorScale();
            if (mode.equals("Minor")) scale = melody.getMinorScale();
        } else scale = chord;
        int length = scale.getUsage().size();

        //Pitch-specific calculations
        int rand = new Random().nextInt(length);
        pitch = rootPitch + general.getTone().getPitch();
        pitch += scale.getUsage().get(rand);

        return pitch;
    }

    //Sorts all trumpet-pitches out of the last because this points on the next patternelement
    public Phrase sortTrumpetPitches(Phrase bar){

        Phrase newBar = new Phrase();
        int length;
        int lowest;
        Note note;

        while(bar.length() > 0){
            note = bar.getNote(0);
            if(note.getPitch() == getRest()[0]){
                newBar.addNote(new Note(note.getPitch(), note.getRhythmValue()));
            } else {
                lowest = 0;
                length = bar.length();
                for(int i=0; i<length; i++){
                    if(bar.getNote(i).getPitch() != getRest()[0] && bar.getNote(i).getPitch() < bar.getNote(lowest).getPitch()){
                        lowest = i;
                    }
                }
                newBar.addNote(new Note(bar.getNote(lowest).getPitch(), bar.getNote(lowest).getRhythmValue()));
                bar.getNote(lowest).setPitch(note.getPitch());
                bar.getNote(lowest).setRhythmValue(note.getRhythmValue());
            }
            bar.removeNote(0);
        }

        return newBar;
    }

    /************************************** PART GENERATION *********************************************************/

    //Generates piano part in score
    public void generatePianoPart(){
        CPhrase bar;
        p("PIANO------------------------");
        for (int i=0; i<general.getRepeat(); i++){
            int lengthPattern = chordprogression.getSize();
            for (int j=0; j<lengthPattern; j++){
                if (chordprogression.getPatternelement(j).getTactProportion().equals("Full")){
                    bar = generatePianoBar(new ArrayList<Patternelement>(Arrays.asList(chordprogression.getPatternelement(j))));
                } else {
                    bar = generatePianoBar(new ArrayList<Patternelement>(Arrays.asList(chordprogression.getPatternelement(j), chordprogression.getPatternelement(j+1))));
                    j++;
                }
                p(Double.toString(bar.getEndTime()));
                piano.addCPhrase(bar);
            }
        }
    }

    //Generates bass part in score
    public void generateBassPart(){
        Phrase bar;
        p("BASS------------------------");
        for (int i=0; i<general.getRepeat(); i++){
            int lengthPattern = chordprogression.getSize();
            for (int j=1; j<lengthPattern; j++){
                bar = generateBassBar(chordprogression.getPatternelement(j));
                p(Double.toString(bar.getEndTime()));
                bass.addPhrase(bar);
            }
        }
    }

    //Generates drums part in score
    public void generateDrumsPart(){
        Phrase bar;
        for(int i=0; i<general.getRepeat(); i++){
            int length = 0;
            int lengthPattern = chordprogression.getSize();
            for(int j=0; j<lengthPattern; j++){
                length++;
                if(chordprogression.getPatternelement(j).getTactProportion().equals("Semi")) j++;
            }
            for(int j=0; j<length; j++){

                bar = generateRide();
                //Set dynamics of bar
                int d = generateDynamics();
                bar.setDynamic(d);

                //Set humanizer-factor in bar
                bar = calcHumanizerInBar(bar, WHOLE_NOTE);
                drums_ride.addPhrase(bar);

                //Set dynamics of bar
                /*d = generateDynamics();
                bar.setDynamic(d);
                bar = generateSnare();
                drums_snare.addPhrase(bar);*/
            }
        }
    }

    //Generates melody part in score
    public void generateTrumpetPart(){
        Phrase bar;
        p("TRUMPET------------------------");
        for(int i=0; i<general.getRepeat(); i++){
            int lengthPattern = chordprogression.getSize();
            for (int j=0; j<lengthPattern; j++){
                bar = generateTrumpetBar(chordprogression.getPatternelement(j));
                p(Double.toString(bar.getEndTime()));
                trumpet.addPhrase(bar);
            }
        }
    }



    /************************************** BAR GENERATION *********************************************************/

    //Generates a full piano-bar and returns result in a CPhrase
    public CPhrase generatePianoBar(ArrayList<Patternelement> patternpart){

        //Calculates pre-patternelement
        Patternelement prePatternelement;
        if(patternpart.get(0).getOrder()>0) prePatternelement = chordprogression.getPatternelement(patternpart.get(0).getOrder() - 1);
        else prePatternelement = chordprogression.getPatternelement(0);

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
        double humanizer = generateHumanizer(OV_HUMANIZER_PERCENTAGE);
        bar.addChord(getRest(), humanizer * calcStartOfEighthInBarByPosition(startEighth));
        //double sum = humanizer * calcStartOfEighthInBarByPosition(startEighth);
        //p("1. Rest: " + Double.toString(humanizer * calcStartOfEighthInBarByPosition(startEighth)));

        //Generate a for bar specific deviation between this chord and the last one
        int localDeviation = new Random().nextInt( backingtrack.getDeviation().getNormData() + 1);
        ArrayList<Integer> usage = calcDeviationInUsage(localDeviation, currentPatternelement, prePatternelement);
        currentPatternelement.getChord().setUsage(usage);
        //p(currentPatternelement.getChord().getUsageAsString());

        //Generate chordcomplexity
        usage = generateChordcomplexity(currentPatternelement);
        //for(int i=0; i<usage.size(); i++)p(Integer.toString(usage.get(i)));
        //p("-------------------------");

        //Generate and add chords by nr of uses → First uses [duration: 1.0], Last use [duration: 0.66666]
        for(int i=0; i<barUses.getBarUses().size(); i++){
            barUses.getBarUse(i).setDuration(barUses.getBarUse(i).getDuration() * generateHumanizer(OV_HUMANIZER_PERCENTAGE));
            bar.addChord(
                    getUsageInContext(
                            usage,general.getTone().getPitch() + currentPatternelement.getTranspose() + barUses.getBarUse(i).getProcedure()
                    ),
                    barUses.getBarUse(i).getDuration()
            );
            //sum += barUses.getBarUse(i).getDuration();
            //p("Use: " + Double.toString(barUses.getBarUse(i).getDuration()));
        }

        //Add end-rest
        humanizer = generateHumanizer(OV_HUMANIZER_PERCENTAGE);
        bar.addChord(getRest(), humanizer * barUses.getEndRest());
        //sum += humanizer * barUses.getEndRest()
        //p("2. Rest: " + Double.toString(humanizer * barUses.getEndRest()));
        //p("SUM:   " + Double.toString(sum));
        //p("---------------------------------------------");

        //Set dynamics of bar
        int d = generateDynamics();
        bar.setDynamic(d);

        return bar;
    }

    //Generates a full bass-bar and returns result in a Phrase
    public Phrase generateBassBar(Patternelement patternelement){
        Phrase bar;
        int nextRootPitch;
        Patternelement next;
        if(patternelement.getOrder() < chordprogression.getSize() - 1) {
            next = chordprogression.getPatternelement(patternelement.getOrder() + 1);
        } else next = patternelement;
        nextRootPitch = next.getChord().getUsage().get(0) + next.getTranspose() + general.getTone().getPitch();
        //View.internal(bar);

        //[TRUE] → Walking-Bass, [FALSE] → Standard-Bass
        if(randomStyle(backingtrack.getWalkingBass())){
            bar = generateWalkingBass(patternelement, nextRootPitch);
        } else {
            bar = generateNormalBass(patternelement, nextRootPitch);
        }

        //Set humanizer-factor in bar
        bar = calcHumanizerInBar(bar, WHOLE_NOTE);

        //Set dynamics of bar
        int d = generateDynamics();
        bar.setDynamic(d);

        return bar;
    }

    //Generates a full piano-melody-bar and returns result as a Phrase
    public Phrase generateTrumpetBar(Patternelement patternelement){
        Phrase bar;
        Patternelement next;
        int goalPitch;
        if(patternelement.getOrder() < chordprogression.getSize() -1){
             next = chordprogression.getPatternelement(patternelement.getOrder() + 1);
        } else next = patternelement;
        goalPitch = next.getChord().getUsage().get(0) + next.getTranspose() + general.getTone().getPitch();

        ArrayList<Double> durations = new ArrayList<>();
        durations.add(EIGHTH_NOTE_TRIPLET);
        durations.add(EIGHTH_NOTE);
        durations.add(QUARTER_NOTE_TRIPLET);
        if(randomStyle(melody.getBebop())){
            durations.add(QUARTER_NOTE);
            bar = generateTrumpet(patternelement, goalPitch, OV_RANGE_USES_IN_BEBOP_TRUMPET, durations, OV_PROBABILITY_OF_REST_IN_BEBOP_TRUMPET);
        } else {
            bar = generateTrumpet(patternelement, goalPitch, OV_RANGE_USES_IN_NORMAL_TRUMPET, durations, OV_PROBABILITY_OF_REST_IN_NORMAL_TRUMPET);
        }

        //Set humanizer-factor in bar
        double possibleScope = WHOLE_NOTE;
        if(patternelement.getTactProportion().equals("Semi")) possibleScope = HALF_NOTE;
        bar = calcHumanizerInBar(bar, possibleScope);

        //Set dynamics of bar
        int d = generateDynamics();
        bar.setDynamic(d);

        return bar;
    }
}
