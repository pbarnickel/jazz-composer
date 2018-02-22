/*
    Description:    Test-class
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ComposerClasses.sub.test;

public class WriteBasicBeat {

    private static final DrumBeat beat = new BasicBeat();

    public void save(){
        beat.saveToFile();
    }
}
