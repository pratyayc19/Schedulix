import comparison.ComparisonEngine;
import model.AlgorithmType;
import model.Process;
import simulation.SimulationRequest;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=======================================================================================================");
        System.out.println("||                                          SCHEDULIX                                                ||");
        System.out.println("||                              CPU Scheduling Simulation Engine                                     ||");
        System.out.println("=======================================================================================================\n");

        // Create a realistic sample workload
        List<Process> processes = Arrays.asList(
                new Process("P1", 0, 8, 3),
                new Process("P2", 1, 4, 1),
                new Process("P3", 2, 9, 4),
                new Process("P4", 3, 5, 2)
        );

        int timeQuantum = 4;
        
        // Wrap in a simulation request
        // (Algorithm type here is just a placeholder, ComparisonEngine automatically tests all algorithms)
        SimulationRequest request = new SimulationRequest(AlgorithmType.FCFS, processes, timeQuantum);

        // Run the automated comparison suite
        ComparisonEngine engine = new ComparisonEngine();
        engine.runComparison(request);
    }
}
