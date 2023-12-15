import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MainAlgorithm {

    int nProcesses ;
    int roundRobinQuantumTime;
    int contextSwitchingTime;

    ArrayList<Process> processes ;

    MainAlgorithm(){
        Scanner in = new Scanner(System.in);
        nProcesses = in.nextInt();
        roundRobinQuantumTime = in.nextInt();
        contextSwitchingTime = in.nextInt() ;
        processes = new ArrayList<>() ;

        for (int i = 0; i < nProcesses; i++) {

            Process process = new Process() ;
            System.out.println("enter process info :");

            process.name = in.next();
            process.arrivalTime = in.nextInt();
            in.nextLine();
            process.burstTime = in.nextInt();
            process.processNum= i+1;
            process.priorityNumber = in.nextInt();
            
//            process.color =in.nextInt();

            System.out.println("---------------------------------------");
            processes.add(process);
        }
        Scheduler s = new SJF(contextSwitchingTime);
        s.run(new ArrayList<>(processes));
        s.printStats();
    }
}
