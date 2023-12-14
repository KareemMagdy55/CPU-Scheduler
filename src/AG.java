import javax.lang.model.element.QualifiedNameable;
import java.util.*;

import static java.lang.Math.min;

public class AG extends Scheduler{

    ArrayList<Process> processes ;
//    ArrayList<Process>timeLine;
    public static  Map<Integer, Integer> processesAG=new HashMap<>();
    Map<Integer, Integer> processesQuantumTime;
    PriorityQueue<Process> readyQueue;
    Map<Integer,Boolean>enterReadyQueue;
    int quantumTime ;
    int currentTime;
    AG(int quantumTime){
        this.quantumTime = quantumTime ;
        processesQuantumTime=new HashMap<>();
//        processesAG =
        currentTime=0;
        enterReadyQueue = new HashMap<>();
        readyQueue = new PriorityQueue<>();
        timeline=new ArrayList<>();
        processes = new ArrayList<>();

    }


    public void buildAGFactors() {
        Random random = new Random();
        int randomNumber;

        for (Process p : processes) {
            randomNumber = random.nextInt(20);
            int formula =  p.arrivalTime + p.burstTime;

            if (randomNumber < 10){
                formula += randomNumber ;
            }else if (randomNumber > 10){
                formula += 10;
            }else {
                formula += p.priorityNumber;
            }

            processesAG.put(p.getProcessNum(), formula);
        }

    }
    public void changeQuantum(Process p, Integer quantum){
        int currentQuantumTime = processesQuantumTime.getOrDefault( p.getProcessNum(), quantumTime);
        processesQuantumTime.put(p.getProcessNum(), currentQuantumTime + quantum);
    }
    public int getAVG(){ // Wrong change to ready queue
        int quantumTimeAVG = 0 ;

        for (int i = 0; i < processes.size(); i++) {
            if(processes.get(i).getArrivalTime()<=currentTime) {
                int tmp = processesQuantumTime.getOrDefault(processes.get(i).getProcessNum(), quantumTime);
                quantumTimeAVG += tmp;
            }
        }
        quantumTimeAVG = (int) Math.ceil((quantumTimeAVG / processes.size()) * 0.1);

        return quantumTimeAVG;
    }

    @Override
    public void run(ArrayList<Process> processes) {
        this.processes = processes;
        buildAGFactors();



        readyQueue = new PriorityQueue<>(Comparator
                .comparingInt(Process::getAgFactor).
                thenComparingInt(Process::getArrivalTime));

        for (int i = 0; i <processes.size() ; i++) {
            processesQuantumTime.put(processes.get(i).getProcessNum(), quantumTime);
            if(processes.get(i).getArrivalTime()<=currentTime && !enterReadyQueue.getOrDefault(processes.get(i).getProcessNum(), false)){
                readyQueue.add(processes.get(i));
                enterReadyQueue.put(processes.get(i).getProcessNum(),true);
            }
        }
        while (!readyQueue.isEmpty()){

            for (int i = 0; i <processes.size() ; i++) {
                if(processes.get(i).getArrivalTime()<=currentTime && !enterReadyQueue.getOrDefault(processes.get(i).getProcessNum(), false)){
                    readyQueue.add(processes.get(i));
                    enterReadyQueue.put(processes.get(i).getProcessNum(),true);
                }
            }
            Process p= new Process(Objects.requireNonNull(readyQueue.poll()));


            int halfQuantumWillTake=(processesQuantumTime.get(p.getProcessNum())+1)/2; //

            p.executionBeginTime=currentTime;
            currentTime+=Math.min(halfQuantumWillTake, p.burstTime);
            p.burstTime-=Math.min(halfQuantumWillTake, p.burstTime);
            // p1 2 17 =>15
            changeQuantum (p,-Math.min(p.burstTime,halfQuantumWillTake));
            p.finishingTime=currentTime;
//            halfQuantumWillTake-=Math.min(p.burstTime,halfQuantumWillTake);
//            if(halfQuantumWillTake==0){//
                for (int i = 0; i <processes.size() ; i++) {
                    if(processes.get(i).getArrivalTime()<=currentTime && !enterReadyQueue.getOrDefault(processes.get(i).getProcessNum(), false)){
                        readyQueue.add(processes.get(i));
                        enterReadyQueue.put(processes.get(i).getProcessNum(),true);
                    }
                }

            Process newP= null;
            if (readyQueue.peek() != null) {
                newP = new Process((readyQueue.peek()));
            }

            if (!timeline.isEmpty() && Objects.equals(timeline.get(timeline.size() - 1).processNum, p.processNum)) {

                    Process lastTimeLineProcess = timeline.get(timeline.size() - 1);

                    lastTimeLineProcess.finishingTime = currentTime ;
                    lastTimeLineProcess.burstTime = p.burstTime;

                    p = lastTimeLineProcess;

            }else {
                timeline.add(p);
            }
                if(p.burstTime==0) {
                    continue;
                }
                else if(processesQuantumTime.get(p.getProcessNum())==0 && p.burstTime>0){ // case 1  10%
                    changeQuantum(p,getAVG()+ quantumTime);
                }
                else if(newP != null && p.getProcessNum()!=newP.getProcessNum()){ // p1 p2
                        changeQuantum(p,quantumTime); // 4
                }
                readyQueue.add(p);
//            }
        }
    }

    @Override
    public void printStats() {

        System.out.println("-------Execution order------");
        for (Process process : timeline) {
            System.out.print(process.name + ' ');
//            int turnaroundTime = process.finishingTime - process.arrivalTime;
//            avgWaitingTime += turnaroundTime - process.burstTime;
//            avgTurnaroundTime += turnaroundTime;
        }
    }
}