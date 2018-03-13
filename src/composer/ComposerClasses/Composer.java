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

import static composer.Main.p;

public class Composer implements JMC, Tempo {

    protected Score score = new Score();
    protected Settings settings = new Settings();

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

    //TODO: implement getRootBassNote with given pitch - 36 (3 octaves down)
}
