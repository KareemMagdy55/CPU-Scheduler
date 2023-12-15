import java.util.*;

public class SJF extends Scheduler {
    ArrayList<Process> processes;
    int contextSwitchTime;


    @Override
    public void run(ArrayList<Process> processes) {
        this.processes = processes;
        timeline = new ArrayList<>();


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

            timeline.add(p);
            processes.remove(p);
            currentTime += contextSwitchTime ;
        }
    }

    @Override
    public void printStats() {
        System.out.println("======================================");
        System.out.println("---------SJF Scheduler--------");

        float avgWaitingTime = 0 ;
        int avgTurnaroundTime = 0 ;

        System.out.println("-------Execution order------");
        for (Process process : timeline) {
            System.out.print(process.name + ' ');
            int turnaroundTime = process.finishingTime - process.arrivalTime;
            avgWaitingTime += turnaroundTime - process.burstTime;
            avgTurnaroundTime += turnaroundTime;
        }
        System.out.println("\n======================================");
        System.out.println("---------Waiting Time for each process--------");
        for (Process process : timeline) {
            int turnaroundTime = process.finishingTime - process.arrivalTime;
            int waitingTime  =turnaroundTime - process.burstTime;
            System.out.println(process.name + "====>" + waitingTime);
        }
        System.out.println("======================================");
        System.out.println("---------Turnaround Time for each process--------");
        for (Process process : timeline) {
            int turnaroundTime = process.finishingTime - process.arrivalTime;
            System.out.println(process.name + "====>" + turnaroundTime);
        }
        System.out.println("======================================");
        System.out.println("Average Waiting Time = " + avgWaitingTime / timeline.size());
        System.out.println("Average Turnaround Time = " + avgTurnaroundTime/ timeline.size());

    }


    SJF(int contextSwitchTime) {
        this.contextSwitchTime = contextSwitchTime;
    }
}
