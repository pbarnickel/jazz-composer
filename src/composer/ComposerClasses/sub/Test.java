/*
    Description:    Test-Composing-Class for some test-use-cases.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ComposerClasses.sub;

import composer.ComposerClasses.sub.test.*;
import jm.music.data.*;
import jm.JMC;
import jm.music.tools.*;
import jm.util.*;

public class Test implements JMC {

    public void dots_and_dashes(){
        Note n = new Note(C4, QUARTER_NOTE);
        Phrase phr = new Phrase();
        phr.addNote(n);

        View.notate(phr);
    }

    public void arpeggio(){
        int[] pitches = {C4, F4, BF4};
        Phrase phr = new Phrase();
        for(int i=0; i<pitches.length; i++){
            Note n = new Note(pitches[i], THIRTYSECOND_NOTE);
            phr.addNote(n);
        }

        //repeat arpeggio a few times
        Mod.repeat(phr, 3);

        //save it as file
        //Write.midi(phr, "midi/test_arpeggio.mid");

        View.notate(phr);
    }

    public void createDrumBeat(){
        WriteBasicBeat beat_basic = new WriteBasicBeat();
        beat_basic.save();
        WriteClapBeat beat_clap = new WriteClapBeat();
        beat_clap.save();
        WriteDiscoBeat beat_disco = new WriteDiscoBeat();
        beat_disco.save();
        WriteShuffleBeat beat_shuffle = new WriteShuffleBeat();
        beat_shuffle.save();
    }

    public void basicGame(){
        BasicGame bg = new BasicGame();
    }

    public void stochastic(){
        Stochastic s = new Stochastic();
        s.start();
    }

    public void cleanComposing(){
        CleanComposing cc = new CleanComposing();
        cc.compose();
    }

}
