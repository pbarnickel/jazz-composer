package composer.DataClasses;

import jm.music.data.Note;
import jm.util.Play;

public class Tone extends MusicElement {
    private double pitch;

    public Tone(String name, double pitch){
        this.name = name;
        this.pitch = pitch;
    }

    public double getPitch(){ return this.pitch;}

    public void setPitch(double pitch){ this.pitch = pitch;}

    //Returns pitch as String for dynamically UI-outputs
    public String getPitchAsString(){ return Double.toString(this.pitch);}

    //Plays pitch
    public void play(){
        Play.midi(new Note(this.pitch, QUARTER_NOTE));
    }
}
