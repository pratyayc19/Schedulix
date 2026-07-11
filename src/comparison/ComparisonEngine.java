package comparison;

import metrics.MetricsReport;
import model.AlgorithmType;
import model.Process;
import simulation.SimulationEngine;
import simulation.SimulationRequest;
import simulation.SimulationResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Automates the execution of a single workload across all supported scheduling 
 * algorithms and generates a comprehensive performance comparison report.
 * 
 * This class adheres strictly to the existing architecture by reusing the 
 * SimulationEngine, completely avoiding any duplicated scheduling logic.
 */
public class ComparisonEngine {

    private final SimulationEngine engine;

    public ComparisonEngine() {
        this.engine = new SimulationEngine();
    }

    /**
     * Executes the comparison simulation and prints the final formatted report.
     * 
     * @param baseRequest A representative request containing the processes and time quantum. 
     *                    The algorithm specified in this request is ignored.
     */
    public void runComparison(SimulationRequest baseRequest) {
        Map<AlgorithmType, MetricsReport> results = new HashMap<>();

        // 1. Execute the exact same workload on every algorithm
        for (AlgorithmType type : AlgorithmType.values()) {
            SimulationRequest clonedRequest = cloneRequest(baseRequest, type);
            SimulationResult result = engine.run(clonedRequest);
            results.put(type, result.getMetricsReport());
        }

        // 2. Generate the formatted report
        printReport(results);
    }

    /**
     * Deep copies the simulation request processes to ensure algorithms do not 
     * mutate shared state and corrupt subsequent simulations.
     */
    private SimulationRequest cloneRequest(SimulationRequest original, AlgorithmType newType) {
        List<Process> clonedProcesses = new ArrayList<>();
        for (Process p : original.getProcesses()) {
            clonedProcesses.add(new Process(p.getId(), p.getArrivalTime(), p.getBurstTime(), p.getPriority()));
        }
        return new SimulationRequest(newType, clonedProcesses, original.getTimeQuantum());
    }

    /**
     * Prints the comparative results and automatically calculates the winning algorithms.
     */
    private void printReport(Map<AlgorithmType, MetricsReport> results) {
        System.out.println("====================================================================================================");
        System.out.println(String.format("%-40s %-12s %-12s %-12s %-12s %-12s", 
                "Algorithm", "Avg WT", "Avg TAT", "Avg RT", "CPU Util", "Throughput"));
        System.out.println("====================================================================================================");

        AlgorithmType bestWtAlgo = null;
        AlgorithmType bestTatAlgo = null;
        AlgorithmType bestRtAlgo = null;
        AlgorithmType bestTpAlgo = null;

        double bestWt = Double.MAX_VALUE;
        double bestTat = Double.MAX_VALUE;
        double bestRt = Double.MAX_VALUE;
        double bestTp = -1;

        for (AlgorithmType type : AlgorithmType.values()) {
            MetricsReport report = results.get(type);
            
            System.out.println(String.format("%-40s %-12.2f %-12.2f %-12.2f %-12.2f %-12.4f", 
                    type.getDisplayName(),
                    report.getAverageWaitingTime(),
                    report.getAverageTurnaroundTime(),
                    report.getAverageResponseTime(),
                    report.getCpuUtilization(),
                    report.getThroughput()));

            // Track winners (Lower is better for WT, TAT, RT)
            if (report.getAverageWaitingTime() < bestWt) {
                bestWt = report.getAverageWaitingTime();
                bestWtAlgo = type;
            }
            if (report.getAverageTurnaroundTime() < bestTat) {
                bestTat = report.getAverageTurnaroundTime();
                bestTatAlgo = type;
            }
            if (report.getAverageResponseTime() < bestRt) {
                bestRt = report.getAverageResponseTime();
                bestRtAlgo = type;
            }
            // Higher is better for Throughput
            if (report.getThroughput() > bestTp) {
                bestTp = report.getThroughput();
                bestTpAlgo = type;
            }
        }
        
        System.out.println("====================================================================================================");
        System.out.println();
        System.out.println("--- Performance Winners ---");
        System.out.println("Best Waiting Time    : " + (bestWtAlgo != null ? bestWtAlgo.getDisplayName() : "N/A"));
        System.out.println("Best Turnaround Time : " + (bestTatAlgo != null ? bestTatAlgo.getDisplayName() : "N/A"));
        System.out.println("Best Response Time   : " + (bestRtAlgo != null ? bestRtAlgo.getDisplayName() : "N/A"));
        System.out.println("Highest Throughput   : " + (bestTpAlgo != null ? bestTpAlgo.getDisplayName() : "N/A"));
        System.out.println("====================================================================================================");
    }
}
