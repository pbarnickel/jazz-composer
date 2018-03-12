package composer.DataClasses;

public class Patternelement {

    private int transpose;
    private MusicStructureGroup chordgroup;
    private MusicStructure chord;
    private Chordcomplexity chordcomplexity;
    private String tactProportion;

    public Patternelement(int transpose, MusicStructureGroup chordgroup, MusicStructure chord, Chordcomplexity chordcomplexity, String tactProportion){
        this.transpose = transpose;
        this.chordgroup = chordgroup;
        this.chord = chord;
        this.chordcomplexity = chordcomplexity;
        this.tactProportion = tactProportion;
    }

    public int getTranspose(){ return this.transpose;}

    public String getTransposeAsString(){ return Integer.toString(this.transpose);}

    public MusicStructureGroup getChordgroup() {return this.chordgroup;}

    public MusicStructure getChord(){ return this.chord;}

    public Chordcomplexity getChordcomplexity(){ return this.chordcomplexity;}

    public String getTactProportion(){ return this.tactProportion;}

    public String getGroupName(){ return this.chordgroup.getName();}

    public String getChordName(){ return this.chord.getName();}

    public String getChordcomplexityName(){return this.getChordcomplexity().getName();}

    public String getMode(){ return this.chord.getMode();}

    public String getUsage(){ return this.chord.getUsageAsString();}

    public void setTranspose(int transpose){ this.transpose = transpose;}

    public void setChordgroup(MusicStructureGroup chordgroup) {this.chordgroup = chordgroup;}

    public void setChord(MusicStructure chord){ this.chord = chord;}

    public void setChordcomplexity(Chordcomplexity chordcomplexity){ this.chordcomplexity = chordcomplexity;}

    public void setTactProportion(String tactProportion){ this.tactProportion = tactProportion;}
}
