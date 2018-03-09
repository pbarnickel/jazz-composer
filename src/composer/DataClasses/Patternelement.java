package composer.DataClasses;

public class Patternelement {

    private int transpose;
    private MusicStructureGroup chordgroup;
    private MusicStructure chord;
    private Chordcomplexity chordcomplexity;

    public Patternelement(int transpose, MusicStructureGroup chordgroup, MusicStructure chord, Chordcomplexity chordcomplexity){
        this.transpose = transpose;
        this.chordgroup = chordgroup;
        this.chord = chord;
        this.chordcomplexity = chordcomplexity;
    }

    public int getTranspose(){ return this.transpose;}

    public MusicStructureGroup getChordgroup(){ return this.chordgroup;}

    public Chordcomplexity getChordcomplexity(){ return this.chordcomplexity;}

    public MusicStructure getChord(){ return this.chord;}

    public void setTranspose(int transpose){ this.transpose = transpose;}

    public void setChordgroup(MusicStructureGroup chordgroup){ this.chordgroup = chordgroup;}

    public void setChord(MusicStructure chord){ this.chord = chord;}

    public void setChordcomplexity(Chordcomplexity chordcomplexity){ this.chordcomplexity = chordcomplexity;}
}
