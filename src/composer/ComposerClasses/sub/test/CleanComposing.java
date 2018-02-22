/*
    Description:    Test-class
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ComposerClasses.sub.test;

import jm.JMC;
import jm.music.data.*;
import jm.util.*;

public class CleanComposing implements JMC {


    public void compose(){
        //Create a middle C half-note
        Note n = new Note(C4, HALF_NOTE);

        //Create a phrase
        Phrase p = new Phrase();

        //Put the note inside the phrase                            -> Reihe von aneinander folgenden Noten
        p.addNote(n);

        //Pack the phrase into a part                               -> Instrument
        Part part = new Part();
        part.addPhrase(p);

        //Pack the part into a score titled Test Score              -> Partitur
        Score s = new Score("Test Score");
        s.addPart(part);

        //Write the score to a MIDI-File to disk
        Write.midi(s,"midi/test.mid");
    }
}
