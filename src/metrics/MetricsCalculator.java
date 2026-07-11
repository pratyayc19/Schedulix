package metrics;

import model.Process;
import java.util.List;

/**
 * Utility class responsible exclusively for computing scheduling metrics.
 * 
 * It relies entirely on the state stored within the Process objects, ensuring
 * a single source of truth. It computes all required metrics in a single 
 * traversal (O(N) time complexity) for optimal performance.
 */
public final class MetricsCalculator {

    // Private constructor to prevent instantiation of a utility class
    private MetricsCalculator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Generates a complete metrics report by traversing the process list exactly once.
     * 
     * @param processes The list of scheduled and completed processes.
     * @return An immutable MetricsReport containing all calculated statistics.
     */
    public static MetricsReport generateReport(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            return new MetricsReport(0.0, 0.0, 0.0, 0.0, 0.0);
        }

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        double totalResponseTime = 0;
        
        int totalBurstTime = 0;
        int minArrivalTime = Integer.MAX_VALUE;
        int maxCompletionTime = 0;

        for (Process process : processes) {
            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
            totalResponseTime += process.getResponseTime();
            
            totalBurstTime += process.getBurstTime();
            
            if (process.getArrivalTime() < minArrivalTime) {
                minArrivalTime = process.getArrivalTime();
            }
            if (process.getCompletionTime() > maxCompletionTime) {
                maxCompletionTime = process.getCompletionTime();
            }
        }

        int size = processes.size();
        double averageWaitingTime = totalWaitingTime / size;
        double averageTurnaroundTime = totalTurnaroundTime / size;
        double averageResponseTime = totalResponseTime / size;

        int totalSimulationTime = maxCompletionTime - minArrivalTime;
        double cpuUtilization = 0.0;
        double throughput = 0.0;

        if (totalSimulationTime > 0) {
            cpuUtilization = ((double) totalBurstTime / totalSimulationTime) * 100.0;
            throughput = (double) size / totalSimulationTime;
        }

        return new MetricsReport(
                averageWaitingTime,
                averageTurnaroundTime,
                averageResponseTime,
                cpuUtilization,
                throughput
        );
    }
}
