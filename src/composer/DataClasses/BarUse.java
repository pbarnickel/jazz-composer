package composer.DataClasses;

public class BarUse {
    double duration;
    int procedure;

    public BarUse(double duration, int procedure){
        this.duration = duration;
        this.procedure = procedure;
    }

    public double getDuration(){
        return this.duration;
    }

    public int getProcedure(){
        return this.procedure;
    }
}
