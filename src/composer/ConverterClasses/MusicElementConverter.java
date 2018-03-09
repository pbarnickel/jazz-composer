package composer.ConverterClasses;

import composer.DataClasses.MusicElement;
import javafx.util.StringConverter;

public class MusicElementConverter extends StringConverter<MusicElement> {

    @Override
    public String toString(MusicElement object) {
        return object.getName();
    }

    @Override
    public MusicElement fromString(String string) {
        return null;
    }
}
