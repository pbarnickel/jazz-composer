/*
    Description:    Root-Composer-Class for manipulating the Score including some common methods for
                    interpreting user-input.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ComposerClasses;

import composer.DataClasses.Settings;
import composer.Interfaces.Tempo;
import jm.JMC;
import jm.music.data.*;
import jm.util.Play;
import jm.util.Read;
import jm.util.View;
import jm.util.Write;

import java.util.Random;

import static composer.Main.p;

public class Composer implements JMC, Tempo {

    protected Score score = new Score();
    protected Settings settings = new Settings();
    protected double humanizerTolerance;

    public Composer(){
        settings.loadSettings();
    }

    public Score getScore(){
        return this.score;
    }

    //Writes MIDI in score
    public void readMIDIinScore(String path){Read.midi(this.score, path);}

    //Writes Score in MIDI
    public void writeScoreinMIDI(String path){
        Write.midi(this.score, path);
    }

    //Empties the score
    public void initScore(){ this.score.empty();}

    //Shows the score in JMusic-View
    public void showScore(){
        View.show(this.score);
    }

    //Plays the score as MIDI
    public void playScore(){ Play.midi(this.score); }

    //Initializes the humanizer from slider and returns percentage of humanizer-tolerance. Input -> max. Tolerance
    public void initHumanizer(double maxTolerance){
        humanizerTolerance = humanizerTolerance * maxTolerance / 10000;
    }

    //Returns random humanizer-factor for multiplication in algorithms considering the humanizer tolerance
    public double calcHumanizer(){
        if(new Random().nextBoolean()) return 1 - humanizerTolerance;
        else return 1 + humanizerTolerance;
    }

    //TODO: Write method to calc Swing positions by WSK - set in UI
    //TODO: Calc random chords and set them in Swing-Positions. Return Cphrases
    //TODO: implement getRootBassNote with given pitch - 36 (3 octaves down)
}
