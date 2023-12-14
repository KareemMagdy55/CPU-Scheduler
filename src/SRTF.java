import java.util.*;

public class SRTF extends Scheduler{
    PriorityQueue<Process> readyQueue;



    public boolean find(Process p) {
        Queue<Process> copyQueue = new LinkedList<>(readyQueue);

        while (!copyQueue.isEmpty()) {
            Process process = copyQueue.poll();
            if (process.equals(p)) {
                return true;
            }
        }

        return false;
    }
    @Override
    public void run(ArrayList<Process> processes) {

        readyQueue = new PriorityQueue<>(Comparator
                .comparingInt(Process::getArrivalTime)
                .thenComparingInt(Process::getBurstTime)
                .thenComparingInt(Process::getProcessNum))
                ;

        readyQueue.addAll(processes);
        int currentTime=0;
        while (!readyQueue.isEmpty()){

            int sz = readyQueue.size();
            for (int i = 0; i <sz; i++) {
                Process p1 = readyQueue.poll();
                if (p1.arrivalTime < currentTime){
                    p1.arrivalTime = currentTime;
                }
                readyQueue.add(p1);
            }


            Process p  = readyQueue.poll();
            if(p.arrivalTime<=currentTime){
                p.burstTime --;
                p.executionBeginTime = currentTime;
                currentTime ++ ;
                p.finishingTime = currentTime;
                // 1 7
                if(!timeline.isEmpty() && Objects.equals(timeline.get(timeline.size() - 1).name, p.name)){
                    timeline.get(timeline.size() - 1).finishingTime=currentTime;
                    timeline.get(timeline.size() - 1).burstTime = p.burstTime;
                    if (p.burstTime != 0)
                        readyQueue.add(timeline.get(timeline.size() - 1));
                }
                else{
                    timeline.add(p);
                    if(p.burstTime != 0){
                        readyQueue.add(p);
                    }
                }
            }else {
                currentTime ++ ;
                readyQueue.add(p);
            }
        }
    }

    @Override
    public void printStats() {
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

