import java.util.*;

public class SRTF extends Scheduler {
    PriorityQueue<Process> readyQueue;
    ArrayList<Process> processes;
    float agingFactor;
    int timeToChange;

    SRTF() {
        agingFactor = 0.5f;

        // any prime number above 10 would be better choice in changing priority formula.
        timeToChange = 17;
    }

    public static Map<Integer, Integer> starvationSolverMap = new HashMap<>();

    public void updateProcessesList(Process p) {
        for (Process process : processes) {
            if (process.processNum == p.processNum) {
                process.burstTime = p.burstTime;
                return;
            }
        }
    }

    public void updateStarvationMap(Process p, int currentTime) {
        int currentValue = starvationSolverMap.get(p.getProcessNum());
        currentValue -= ((currentTime - p.arrivalTime) * agingFactor);
        starvationSolverMap.put(p.getProcessNum(), currentValue);
    }

    //
    @Override
    public void run(ArrayList<Process> processes) {
        for (Process p : processes)
            starvationSolverMap.put(p.getProcessNum(), 0);


        this.processes = processes;
        readyQueue = new PriorityQueue<>(Comparator
                .comparingInt(Process::getStarvationVal)
                .thenComparingInt(Process::getBurstTime)
                .thenComparingInt(Process::getArrivalTime));


        int currentTime = 0;
        while (!processes.isEmpty()) {

            // solution starvation problem
            if (currentTime % timeToChange == 0 && currentTime != 0) {
                for (Process process : processes) {
                    updateStarvationMap(process, currentTime);
                }
            }

            readyQueue.clear();
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime) {
                    readyQueue.add(process);
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime ++ ;
                continue;
            }

            Process peekProcess = new Process(readyQueue.poll());

            // update process info.
            peekProcess.burstTime--;
            peekProcess.finishingTime = currentTime + 1;


            if (!timeline.isEmpty() && Objects.equals(timeline.get(timeline.size() - 1).processNum, peekProcess.processNum)) {

                Process lastTimeLineProcess = timeline.get(timeline.size() - 1);

                lastTimeLineProcess.finishingTime = currentTime + 1;
                lastTimeLineProcess.burstTime = peekProcess.burstTime;

                peekProcess = lastTimeLineProcess;

            } else {
                timeline.add(peekProcess);
                peekProcess.executionBeginTime = currentTime;

            }

            updateProcessesList(peekProcess);
            if (peekProcess.burstTime == 0){
                for (int i = 0; i < processes.size(); i++) {
                    Process tmp = processes.get(i);
                    if (tmp.getProcessNum() == peekProcess.getProcessNum()) {
                        processes.remove(i);
                        break;
                    }
                }
            }else readyQueue.add(peekProcess);

            currentTime += 1;
        }

    }

    @Override
    public void printStats() {
        System.out.println("======================================");
        System.out.println("---------SRTF Scheduler--------");

        float avgWaitingTime = 0;
        int avgTurnaroundTime = 0;


        System.out.println("-------Execution order------");
        for (Process process : timeline) {
            System.out.print(process.name + ' ');

        }

//        ArrayList<Process> newTimeline;
        Map<Integer,Process>newTime=new HashMap<>();
        for (int i = 0; i <timeline.size() ; i++) {

            if(!newTime.containsKey(timeline.get(i).getProcessNum())){

                timeline.get(i).burstTime +=(timeline.get(i).finishingTime-timeline.get(i).executionBeginTime);
                newTime.put(timeline.get(i).getProcessNum(),timeline.get(i));
                continue;
            }
            newTime.get(timeline.get(i).getProcessNum()).finishingTime = timeline.get(i).finishingTime;

        }
        timeline.clear();
        for (Map.Entry<Integer, Process> entry : newTime.entrySet()) {
            timeline.add(entry.getValue());
        }


        System.out.println("\n======================================");
        System.out.println("---------Waiting Time for each process--------");
        for (Process process : timeline) {
            int turnaroundTime = process.finishingTime - process.arrivalTime;
            int waitingTime = turnaroundTime - process.burstTime;
            System.out.println(process.name + "====>" + waitingTime);
            avgWaitingTime += turnaroundTime - process.burstTime;
            avgTurnaroundTime += turnaroundTime;
        }
        System.out.println("======================================");
        System.out.println("---------Turnaround Time for each process--------");
        for (Process process : timeline) {
            int turnaroundTime = process.finishingTime - process.arrivalTime;
            System.out.println(process.name + "====>" + turnaroundTime);
        }
        System.out.println("======================================");
        System.out.println("Average Waiting Time = " + avgWaitingTime / timeline.size());
        System.out.println("Average Turnaround Time = " + avgTurnaroundTime / timeline.size());
    }
}
