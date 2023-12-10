import java.util.*;

public class SJF implements Scheduler {
    ArrayList<Process> processes;
    ArrayList<Process> timeLine;
    int contextSwitchTime;


    @Override
    public void run(ArrayList<Process> processes) {
        this.processes = processes;
        timeLine = new ArrayList<>();


        int currentTime = 0 ;
        int sz = processes.size();
        for (int i = 0; i < sz; i++) {
            int mnBurstTime= Integer.MAX_VALUE;
            Process p  = new Process();

            for (Process process : processes) {
                if (process.arrivalTime <= currentTime) {
                    if (mnBurstTime > process.burstTime) {
                        mnBurstTime = process.burstTime;
                        p = process;
                    }
                }
            }

            p.executionBeginTime = currentTime ;
            currentTime += p.burstTime;
            p.finishingTime = currentTime;

            timeLine.add(p);
            processes.remove(p);

            currentTime += contextSwitchTime ;

        }


        for (Process process : timeLine) {
            System.out.println(process.name);
        }

    }

    SJF(int contextSwitchTime) {
        this.contextSwitchTime = contextSwitchTime;
    }
}
