package composer.ConverterClasses;

import composer.DataClasses.MusicStructure;
import javafx.util.StringConverter;

public class MusicStructureConverter extends StringConverter<MusicStructure> {

    @Override
    public String toString(MusicStructure object) {
        return object.getName();
    }

    @Override
    public MusicStructure fromString(String string) {
        return null;
    }
}
