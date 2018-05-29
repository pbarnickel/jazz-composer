/*
    Description:    Generalized Data-model-class for MusicStructure, MusicStructureGroup, Chordcomplexity.
                    Name is used for labeling the MusicElement in the UI.
    Author:         Philipp Barnickel
    Version:        1.0
    Date:           13.03.2018
 */

package composer.Model;

import composer.Interfaces.Constants;

public class MusicElement implements Constants {

    protected String name;

    public String getName(){ return this.name;}

    public void setName(String name){ this.name = name;}

}
