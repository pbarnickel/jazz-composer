package composer.DataClasses;

public class Eighth {
    private int position;
    private Range range;

    public Eighth(int position, Range range){
        this.position = position;
        this.range = range;
    }

    public int getPosition(){
        return this.position;
    }

    public Range getRange(){
        return this.range;
    }
}
