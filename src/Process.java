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

    public Process(String name, int arrivalTime) {
        this.name = name;
        this.arrivalTime = 0;
        // set all to zero
        this.burstTime = arrivalTime;
        this.priorityNumber = 0;
        this.executionBeginTime = 0;
        this.finishingTime = 0;
        this.processNum = 0;
    }
    public Process() {

    }

    // copy constructor


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
