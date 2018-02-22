/*
    Description:    Test-class
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ComposerClasses.sub.test;

import jm.music.data.Note;
import jm.music.data.Phrase;

public class ShuffleBeat extends BasicBeat {

    @Override
    protected String getName(){
        return "Shuffle drum beat";
    }

    @Override
    protected void composeHats(final Phrase hats){
        for(int i=0; i<4; i++){
            hats.addNote(new Note(CLOSED_HI_HAT, QUAVER_TRIPLET));
            hats.addNote(new Note(PEDAL_HI_HAT, QUAVER_TRIPLET));
            hats.addNote(new Note(OPEN_HI_HAT, QUAVER_TRIPLET));
        }
    }

}
