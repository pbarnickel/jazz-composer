/*
    Description:    Test-class
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           02.02.2018
*/

package composer.ComposerClasses.sub.test;

public class WriteShuffleBeat {

    private static final DrumBeat beat = new ShuffleBeat();

    public void save(){
        beat.saveToFile();
    }

}
