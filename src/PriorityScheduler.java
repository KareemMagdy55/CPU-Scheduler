import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityScheduler extends Scheduler{

    float agingFactor ;
    int timeToChange ;
    PriorityQueue<Process> readyQueue;

    PriorityScheduler(){
        agingFactor = 0.5f;
        // any prime number above 10 would be better choice in changing priority formula.
        timeToChange = 17;

    }


    @Override
    public void run(ArrayList<Process> processes) {

        readyQueue = new PriorityQueue<>(Comparator
                .comparingInt(Process::getArrivalTime).
                thenComparingInt(Process::getPriorityNumber).
                thenComparingInt(Process::getProcessNum));

        readyQueue.addAll(processes);
        int currTime = 0 ;
        while(!readyQueue.isEmpty()){
            // solve starvation problem
            // at every period of time ( PRIME number of seconds), the priority of all
            // process is being updated by specific formula to solve the starvation (Waiting for a long time for Big priority Processes)...

            if (currTime %  timeToChange == 0 && currTime != 0){

                ArrayList<Process>capture = new ArrayList<>();
                for (int i = 0; i < readyQueue.size(); i++) {
                    Process p = readyQueue.poll();
                    updatePriority(p, currTime);
                    capture.add(p);
                }
                readyQueue.addAll(capture);
            }



            Process p = readyQueue.peek();
            if (p.arrivalTime <= currTime){
                readyQueue.poll();
                timeline.add(p);

                // update values of chosen process
                p.executionBeginTime = currTime ;
                currTime += p.burstTime;
                p.finishingTime = currTime;

            }else {
                currTime ++ ;
            }
        }

        
    }

    private void updatePriority(Process p, int currentTime) {
        p.priorityNumber -= ((currentTime - p.arrivalTime) * agingFactor);
    }

    @Override
    public void printStats() {
        System.out.println("======================================");
        System.out.println("---------Priority Scheduler--------");
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

}
