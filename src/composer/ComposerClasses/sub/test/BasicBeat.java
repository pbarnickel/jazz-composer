/*
    Description:    Test-class
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ComposerClasses.sub.test;

import jm.music.data.Phrase;

public class BasicBeat extends DrumBeat {

    @Override
    protected String getName(){
        return "Basic drum beat";
    }

    @Override
    protected void composeKicks(final Phrase kick){
        kick.addNote(KICK_CROTCHET);
        kick.addNote(REST_CROCHET);
        kick.addNote(KICK_CROTCHET);
        kick.addNote(REST_CROCHET);
    }

    @Override
    protected void composeHats(final Phrase snare) {
        snare.addNote(REST_CROCHET);
        snare.addNote(SNARE_CROCHET);
        snare.addNote(REST_CROCHET);
        snare.addNote(SNARE_CROCHET);
    }

    @Override
    protected void composeSnare(final Phrase hats) {
        hats.addNote(HAT_CROCHET);
        hats.addNote(HAT_CROCHET);
        hats.addNote(HAT_CROCHET);
        hats.addNote(HAT_CROCHET);
    }
}
