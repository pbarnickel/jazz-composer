/*
    Description:    Test-class
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ComposerClasses.sub.test;

import jm.JMC;
import jm.music.data.*;
import jm.util.Write;
import static composer.Interfaces.Tempo.*;

public abstract class DrumBeat implements JMC {

    private static final int DRUM_CHANNEL = 9;
    private static final int STANDARD_KIT = 0;

    protected static final Note KICK_CROTCHET = new Note(BASS_DRUM_1, CROTCHET);
    protected static final Note SNARE_CROCHET = new Note(ACOUSTIC_SNARE, CROTCHET);
    protected static final Note HAT_CROCHET = new Note(CLOSED_HI_HAT, CROTCHET);
    protected static final Note REST_CROCHET = new Note(REST, CROTCHET);

    protected abstract String getName();
    protected abstract void composeKicks(final Phrase kicks);
    protected abstract void composeSnare(final Phrase snare);
    protected abstract void composeHats(final Phrase hats);
    protected void composeClaps(final Phrase claps){

    }

    public final void saveToFile(){
        final Score s = new Score(getName(), ALLEGRO);
        final Phrase kicksPhrase = new Phrase("Kicks");
        composeKicks(kicksPhrase);
        addToBeat(kicksPhrase, s);

        final Phrase snarePhrase = new Phrase("Snare");
        composeSnare(snarePhrase);
        addToBeat(snarePhrase, s);

        final Phrase hatsPhrase = new Phrase("Hats");
        composeHats(hatsPhrase);
        addToBeat(hatsPhrase, s);

        final Phrase clapsPhrase = new Phrase("Claps");
        composeClaps(clapsPhrase);
        addToBeat(clapsPhrase, s);

        Write.midi(s, "midi/" + getName() + ".mid");
    }

    private static final void addToBeat(final Phrase p, final Score s){
        Part part = new Part(p.getTitle(), STANDARD_KIT, DRUM_CHANNEL);
        part.addPhrase(p);
        s.addPart(part);
    }
}
