package composer.DataClasses;

public class BarUse {
    double duration;
    int start;
    int procedure;

    public BarUse(double duration, int procedure, int start){
        this.duration = duration;
        this.start = start;
        this.procedure = procedure;
    }

    public double getDuration(){
        return this.duration;
    }

    public int getProcedure(){
        return this.procedure;
    }
}
