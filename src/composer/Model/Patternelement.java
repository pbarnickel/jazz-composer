/*
    Description:    Data-model-class for a Patternelement.
                    Transpose is the key up from the mode of the jazz composition.
                    Chordgroup is the Group of neighbour-chords of the used chord.
                    Chord is the specific used chord.
                    Chordcomplexity is the range of complexity in which chords are generated.
                    TactProportion is the information about how much the Patternelement is used in a tact. [50% | 100%]
    Version:        1.0
    Date:           13.03.2018
 */

package composer.Model;

public class Patternelement {

    private int order;
    private int transpose;
    private MusicStructureGroup chordgroup;
    private MusicStructure chord;
    private Chordcomplexity chordcomplexity;
    private String tactProportion;

    public Patternelement(int order, int transpose, MusicStructureGroup chordgroup, MusicStructure chord, Chordcomplexity chordcomplexity, String tactProportion){
        this.order = order;
        this.transpose = transpose;
        this.chordgroup = chordgroup;
        this.chord = chord;
        this.chordcomplexity = chordcomplexity;
        this.tactProportion = tactProportion;
    }

    public int getOrder(){ return this.order;}

    public String getOrderAsString(){ return Integer.toString(this.order);}

    public int getTranspose(){ return this.transpose;}

    public String getTransposeAsString(){ return Integer.toString(this.transpose);}

    public MusicStructureGroup getChordgroup() {return this.chordgroup;}

    public MusicStructure getChord(){ return this.chord;}

    public Chordcomplexity getChordcomplexity(){ return this.chordcomplexity;}

    public String getTactProportion(){ return this.tactProportion;}

    public void setOrder(int order){ this.order = order;}

    public void setTranspose(int transpose){ this.transpose = transpose;}

    public void setChordgroup(MusicStructureGroup chordgroup) {this.chordgroup = chordgroup;}

    public void setChord(MusicStructure chord){ this.chord = chord;}

    public void setChordcomplexity(Chordcomplexity chordcomplexity){ this.chordcomplexity = chordcomplexity;}

    public void setTactProportion(String tactProportion){ this.tactProportion = tactProportion;}

    //Getters for dynamically UI-outputs--------------------------------------------------------
    public String getGroupName(){ return this.chordgroup.getName();}
    public String getChordName(){ return this.chord.getName();}
    public String getChordcomplexityName(){return this.getChordcomplexity().getName();}
    public String getMode(){ return this.chord.getMode();}
    public String getUsage(){ return this.chord.getUsageAsString();}
    //------------------------------------------------------------------------------------------
}
