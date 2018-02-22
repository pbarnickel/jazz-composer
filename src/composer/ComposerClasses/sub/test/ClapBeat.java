/*
    Description:    Test-class
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ComposerClasses.sub.test;

import jm.music.data.Note;
import jm.music.data.Phrase;

public class ClapBeat extends DiscoBeat {

    @Override
    protected String getName(){
        return "Disco drum beat with claps";
    }

    @Override
    protected void composeClaps(final Phrase claps){
        claps.addNote(new Note(REST, MINIM));
        claps.addNote(new Note(REST, QUAVER));
        claps.addNote(new Note(HAND_CLAP, QUAVER));
        claps.addNote(new Note(HAND_CLAP, CROTCHET));
    }

}
