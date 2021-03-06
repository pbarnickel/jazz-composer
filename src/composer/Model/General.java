/*
    Description:    Data-model-class for General-user-input.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           16.05.2018
 */

package composer.Model;

public class General {

    private int tempo;
    private int repeat;
    private double humanizerTolerance;
    private double dynamics;
    private Tone tone;

    public General(int tempo, int repeat, double humanizerTolerance, double dynamics, Tone tone){
        this.tempo = tempo;
        this.repeat = repeat;
        this.humanizerTolerance = humanizerTolerance;
        this.dynamics = dynamics;
        this.tone = tone;
    }

    public int getTempo() {return this.tempo;}
    public int getRepeat() {return this.repeat;}
    public double getHumanizerTolerance() {return this.humanizerTolerance;}
    public double getDynamics() {return this.dynamics;}
    public Tone getTone() {return this.tone;}

}
