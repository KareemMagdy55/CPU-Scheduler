import java.util.ArrayList;

public abstract class Scheduler {

    ArrayList<Process>timeline;
    Scheduler(){
        timeline = new ArrayList<>();
    }
    public abstract void run(ArrayList<Process> processes);
    public abstract void printStats();

    public ArrayList<Process> getTimeline() {
        return timeline;
    }
}
