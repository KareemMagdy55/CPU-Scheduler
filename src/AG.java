import java.util.*;

import static java.lang.Math.min;

public class AG extends Scheduler {

    ArrayList<Process> processes;
    //    ArrayList<Process>timeLine;
    public static Map<Integer, Integer> processesAG = new HashMap<>();
    Map<Integer, Integer> processesQuantumTime; // new
    Map<Integer, Integer> processesOldQuantumTime; // old
    ArrayList<Process> circular;
    PriorityQueue<Process> readyQueue;
    Map<Integer, Boolean> enterReadyQueue;
    int quantumTime;
    int currentTime;
    int finshedProcess;
    AG(int quantumTime) {
        this.quantumTime = quantumTime;
        processesQuantumTime = new HashMap<>();
        processesOldQuantumTime = new HashMap<>();
        currentTime = 0;
        enterReadyQueue = new HashMap<>();
        readyQueue = new PriorityQueue<>();
        timeline = new ArrayList<>();
        processes = new ArrayList<>();
        circular = new ArrayList<>();
    }


    public void buildAGFactors() {
        Random random = new Random();
        int randomNumber;

        for (Process p : processes) {
            randomNumber = random.nextInt(20);
            int formula = p.arrivalTime + p.burstTime;

            if (randomNumber < 10) {
                formula += randomNumber;
            } else if (randomNumber > 10) {
                formula += 10;
            } else {
                formula += p.priorityNumber;
            }

            processesAG.put(p.getProcessNum(), formula);
        }

    }

    public void changeQuantum(Process p, Integer quantum) {
        int currentQuantumTime = processesQuantumTime.getOrDefault(p.getProcessNum(), quantumTime);
        processesQuantumTime.put(p.getProcessNum(), currentQuantumTime + quantum);
    }

    public int getAVG() { // Wrong change to ready queue
        int quantumTimeAVG = 0;

        for (Process process : circular) {
            if (process.getArrivalTime() <= currentTime) {
                int tmp = processesQuantumTime.getOrDefault(process.getProcessNum(), 0);
                quantumTimeAVG += tmp;
            }
        }
        quantumTimeAVG = (int) Math.ceil(((double) quantumTimeAVG / circular.size()) * 0.1);

        return quantumTimeAVG;
    }

    public Process getTale() {
        Process pickedProcess = new Process(circular.get(0));

        int sz = readyQueue.size();

        ArrayList<Process> tmp = new ArrayList<>();
        for (int i = 0; i < sz; i++) {
            if (readyQueue.peek().getProcessNum()==pickedProcess.getProcessNum()) {
              pickedProcess =  new Process(readyQueue.poll());
            } else {
                tmp.add(readyQueue.poll());
            }
        }

        // p1  p2 p3
        // 20 17 16
        readyQueue.addAll(tmp);

        circular.remove(0);
        circular.add(pickedProcess);

        return pickedProcess;
    }
//    public void buildReadyQueue(){
//
//    }

    @Override
    public void run(ArrayList<Process> processes) {


        finshedProcess = processes.size() ;
        for (int i = 0; i < processes.size(); i++) {
            processesOldQuantumTime.put(new Process(processes.get(i)).processNum, quantumTime);
        }
        this.processes = processes;
        buildAGFactors();
//        processesAG.put(1, 2); // 0 5 6
//        // 20 5
//        processesAG.put(2, 50);
////        processesAG.put(3, 16);
////        processesAG.put(4, 43);
////        processes.get(3).arrivalTime =  55;


        readyQueue = new PriorityQueue<>(Comparator
                .comparingInt(Process::getAgFactor).
                thenComparingInt(Process::getArrivalTime));

        for (int i = 0; i < processes.size(); i++) {
            processesQuantumTime.put(processes.get(i).getProcessNum(), quantumTime);
            if (processes.get(i).getArrivalTime() <= currentTime && !enterReadyQueue.getOrDefault(processes.get(i).getProcessNum(), false)) {
                readyQueue.add(processes.get(i));
                enterReadyQueue.put(processes.get(i).getProcessNum(), true);
                circular.add(processes.get(i));
            }
        }
        boolean flag = false;
        while (!readyQueue.isEmpty()  || finshedProcess>0) {


            buildReadyQueue(processes);

            if (readyQueue.isEmpty()){
                currentTime++;
                continue;
            }
            Process p;
            if (flag) {
                flag = false;
                p = getTale();
            } else {
                p = new Process(Objects.requireNonNull(readyQueue.poll()));
            }

            // p1 p2 p3
            int halfQuantumWillTake = (processesQuantumTime.get(p.getProcessNum()) + 1) / 2; //i
            if (!timeline.isEmpty() && Objects.equals(timeline.get(timeline.size() - 1).processNum, p.processNum))
                halfQuantumWillTake = 1;

            p.executionBeginTime = currentTime;
            currentTime += Math.min(halfQuantumWillTake, p.burstTime);
            p.burstTime -= Math.min(halfQuantumWillTake, p.burstTime);
            // p1 2 17 =>15
            changeQuantum(p, -Math.min(p.burstTime, halfQuantumWillTake)); //

            p.finishingTime = currentTime;
            if(p.burstTime>0){
                readyQueue.add(p);
            }
            buildReadyQueue(processes);

            Process newP = null;
            if (readyQueue.peek() != null) {
                newP = new Process((readyQueue.peek()));
            }

            if (!timeline.isEmpty() && Objects.equals(timeline.get(timeline.size() - 1).processNum, p.processNum)) {

                Process lastTimeLineProcess = timeline.get(timeline.size() - 1);

                lastTimeLineProcess.finishingTime = currentTime;
                lastTimeLineProcess.burstTime = p.burstTime;

                p = lastTimeLineProcess;

            } else {
                timeline.add(p);
            }
            if (p.burstTime == 0) {
                flag = true;
                for (int i = 0; i < circular.size(); i++) {
                    if (circular.get(i).getProcessNum() == p.getProcessNum()) {
                        circular.remove(i);
                        break;
                    }

                }

                finshedProcess -= 1  ;
            } else if (processesQuantumTime.get(p.getProcessNum()) == 0 && p.burstTime > 0) { // case 1  10%
                // 4
                processesOldQuantumTime.put(p.getProcessNum(), processesOldQuantumTime.get(p.getProcessNum()) + getAVG());
                int newQ = processesOldQuantumTime.get(p.getProcessNum());
                processesQuantumTime.put(p.getProcessNum(), newQ);
                flag = true;
            } else if (newP != null && p.getProcessNum() != newP.getProcessNum()) { // p1 p2
                int rem = processesQuantumTime.get(p.getProcessNum());
                processesOldQuantumTime.put(p.getProcessNum(), processesOldQuantumTime.get(p.getProcessNum()) + rem);
                int newQ = processesOldQuantumTime.get(p.getProcessNum());
                processesQuantumTime.put(p.getProcessNum(), newQ);
            }
        }

    }

    private void buildReadyQueue(ArrayList<Process> processes) {
        for (int i = 0; i < processes.size(); i++) {
            if (processes.get(i).getArrivalTime() <= currentTime && !enterReadyQueue.getOrDefault(processes.get(i).getProcessNum(), false)) {
                readyQueue.add(processes.get(i));
                enterReadyQueue.put(processes.get(i).getProcessNum(), true);
                circular.add(processes.get(i));
            }
        }
    }

    @Override
    public void printStats() {
        float avgWaitingTime = 0;
        int avgTurnaroundTime = 0;

        ArrayList<Process> tmpTimeline = new ArrayList<>(timeline);

        System.out.println("-------Execution order------");
        for (Process process : timeline) {
            System.out.print(process.name + ' ');

        }

//        ArrayList<Process> newTimeline;
        Map<Integer,Process>newTime=new HashMap<>();
        for (Process value : timeline) {

            if (!newTime.containsKey(value.getProcessNum())) {

                value.burstTime += (value.finishingTime - value.executionBeginTime);
                newTime.put(value.getProcessNum(), value);
                continue;
            }
            newTime.get(value.getProcessNum()).finishingTime = value.finishingTime;

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
        timeline = new ArrayList<>(tmpTimeline);
    }

}