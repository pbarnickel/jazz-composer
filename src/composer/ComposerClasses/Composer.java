/*
    Description:    Root-Composer-Class for manipulating the Score including some common methods for
                    interpreting user-input.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ComposerClasses;

import composer.DataClasses.Chordcomplexity;
import composer.DataClasses.MusicStructure;
import composer.DataClasses.MusicStructureGroup;
import composer.DataClasses.Settings;
import composer.Interfaces.Tempo;
import jm.JMC;
import jm.music.data.*;
import jm.util.Play;
import jm.util.Read;
import jm.util.View;
import jm.util.Write;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Composer implements JMC, Tempo {

    protected Score score = new Score();
    protected Settings settings = new Settings();
    protected MusicStructureGroup chordgroup;
    protected Chordcomplexity chordcomplexity;

    public Composer(){
        settings.loadSettings();
    }

    public void setScore(Score score){
        this.score = score;
    }
    public Score getScore(){
        return this.score;
    }
    public MusicStructureGroup getChordgroup() {return chordgroup;}
    public void setChordgroup(MusicStructureGroup chordgroup) {this.chordgroup = chordgroup;}
    public Chordcomplexity getChordcomplexity() {return chordcomplexity;}
    public void setChordcomplexity(Chordcomplexity chordcomplexity) {this.chordcomplexity = chordcomplexity;}

    public void readMIDIinScore(String path){Read.midi(this.score, path);}

    public void writeScoreinMIDI(String path){
        Write.midi(this.score, path);
    }

    public void initScore(){
        this.score.empty();
    }

    public void showScore(){
        View.show(this.score);
    }

    public void playScore(){ Play.midi(this.score); }

    public int interpretTone(String tone){
        int t;
        switch (tone){
            case "C":   t = C4; break;
            case "C#":   t = CS4; break;
            case "Cb":   t = CF4; break;
            case "D":   t = D4; break;
            case "D#":   t = DS4; break;
            case "Db":   t = DF4; break;
            case "E":   t = E4; break;
            case "E#":   t = ES4; break;
            case "Eb":   t = EF4; break;
            case "F":   t = F4; break;
            case "F#":   t = FS4; break;
            case "Fb":   t = FF4; break;
            case "G":   t = G4; break;
            case "G#":   t = GS4; break;
            case "Gb":   t = GF4; break;
            case "A":   t = A4; break;
            case "A#":   t = AS4; break;
            case "Ab":   t = AF4; break;
            case "B":   t = B4; break;
            case "B#":   t = BS4; break;
            case "Bb":   t = BF4; break;
            default: t = C4;
        }
        return t;
    }

    public List<Integer> interpretPattern(String pattern){
        String tone;
        List<Integer> tones = new ArrayList<Integer>();
        while(pattern.indexOf("-")>-1){
            tone = pattern.substring(0,pattern.indexOf("-"));
            tones.add(interpretTone(tone));
            pattern = pattern.substring(pattern.indexOf("-")+1);
        }
        tones.add(interpretTone(pattern));
        return tones;
    }

    public CPhrase getChord(int rootpitch, MusicStructure chord, double duration){
        CPhrase ch = new CPhrase();
        int len = chord.getUsage().size();
        int[] result = new int[len];
        for(int i=0; i<len; i++){
            result[i] = rootpitch + chord.getUsage().get(i);
        }
        ch.addChord(result,duration);
        return ch;
    }

    public MusicStructure getRandomChord(){
        Random rand = new Random();
        int r;
        if(chordgroup.getNrOfMusicStructures() > 0) {
            r = rand.nextInt(chordgroup.getNrOfMusicStructures());
            return chordgroup.getMusicStructures().get(r);
        } else {
            r = rand.nextInt(settings.getChordgroups().get(settings.getIndexOfGroup(settings.getChordgroups(),"Basic")).getNrOfMusicStructures());
            return settings.getChordgroups().get(settings.getIndexOfGroup(settings.getChordgroups(),"Basic")).getMusicStructures().get(r);
        }
    }

    public Note getRandomBassNote(int pitch, int[] chord){
        Note note;
        Random rand = new Random();
        int r = rand.nextInt(chord.length);
        note = new Note(pitch + r - 36,QUADRAPHONIC);
        return note;
    }

    public Note getRootBassNote(int pitch){
        return new Note(pitch - 36, QUADRAPHONIC);
    }
}
