public class Process {
    String name ;
    int color;

    int arrivalTime;
    int burstTime;
    int priorityNumber;
    int executionBeginTime;
    int finishingTime ;

    int processNum;
    public Process(Process process) {
        this.name = process.name;
        this.color = process.color;
        this.arrivalTime = process.arrivalTime;
        this.burstTime = process.burstTime;
        this.priorityNumber = process.priorityNumber;
        this.executionBeginTime = process.executionBeginTime;
        this.finishingTime = process.finishingTime;
        this.processNum = process.processNum;
    }

    public Process() {

    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriorityNumber() {
        return priorityNumber;
    }

    public int getProcessNum() {
        return processNum;
    }
    public int getAgFactor(){
        return AG.processesAG.get(this.processNum);
    }
    public int getStarvationVal(){
        return SRTF.starvationSolverMap.get(this.processNum);
    }
}
