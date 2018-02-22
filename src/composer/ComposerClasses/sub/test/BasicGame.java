/*
    Description:    Test-class
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ComposerClasses.sub.test;

import jm.JMC;
import jm.music.data.*;
import jm.util.View;

public class BasicGame implements JMC {

    public void start(){
        new BasicGame();
    }

    public BasicGame(){
        Phrase phr = new Phrase();
        for(int i=0; i<16; i++){
            Note n = new Note(60 + i, 0.125);
            phr.addNote(n);
        }
        Part p = new Part(phr);
        Score s = new Score(p);

        View.show(s);
    }

}
