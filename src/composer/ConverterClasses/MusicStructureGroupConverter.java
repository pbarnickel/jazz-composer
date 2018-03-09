package composer.ConverterClasses;

import composer.DataClasses.MusicStructureGroup;
import javafx.util.StringConverter;

public class MusicStructureGroupConverter extends StringConverter<MusicStructureGroup> {

    @Override
    public String toString(MusicStructureGroup object) {
        return object.getName();
    }

    @Override
    public MusicStructureGroup fromString(String string) {
        return null;
    }
}
