package composer.ConverterClasses;

import composer.DataClasses.Chordcomplexity;
import javafx.util.StringConverter;

public class ChordcomplexityConverter extends StringConverter<Chordcomplexity> {
    @Override
    public String toString(Chordcomplexity object) {
        return object.getTerm();
    }

    @Override
    public Chordcomplexity fromString(String string) {
        return null;
    }
}
