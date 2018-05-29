/*
    Description:    Data-model-class for Tone of composition.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           15.05.2018
 */

package composer.Model;

import jm.music.data.Note;
import jm.util.Play;

public class Tone extends MusicElement {
    private int pitch;

    public Tone(String name, int pitch){
        this.name = name;
        this.pitch = pitch;
    }

    public int getPitch(){ return this.pitch;}

    public String getName(){ return this.name;}

    public void setPitch(int pitch){ this.pitch = pitch;}

    //Returns pitch as String for dynamically UI-outputs
    public String getPitchAsString(){ return Integer.toString(this.pitch);}

    //Plays pitch
    public void play(){
        Play.midi(new Note(this.pitch, OV_QUARTER_NOTE));
    }
}
