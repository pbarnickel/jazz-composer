/*
    Description:    Test-class
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ComposerClasses.sub.test;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.View;

public class Stochastic implements JMC {

    public void start(){
        Score s = new Score("Stochastic Demo");
        Part instrument = new Part("Piano", PIANO, 0);
        Phrase phr = new Phrase();

        for(int i=0; i<32; i++){
            Note n = new Note((int)(Math.random()*127), Q);
            phr.addNote(n);
        }
        instrument.addPhrase(phr);
        s.addPart(instrument);

        View.notate(s);
    }

}
