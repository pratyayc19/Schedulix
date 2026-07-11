package metrics;

/**
 * An immutable Data Transfer Object (DTO) that encapsulates the statistical 
 * results of a CPU scheduling simulation.
 * 
 * By passing this single object around, we ensure that metrics are computed 
 * once and safely shared across the application without risk of mutation.
 */
public class MetricsReport {
    private final double averageWaitingTime;
    private final double averageTurnaroundTime;
    private final double averageResponseTime;
    private final double cpuUtilization;
    private final double throughput;

    /**
     * Constructs a complete MetricsReport.
     * 
     * @param averageWaitingTime    The average time processes spent waiting in the ready queue.
     * @param averageTurnaroundTime The average time taken from arrival to completion.
     * @param averageResponseTime   The average time taken from arrival to first execution.
     * @param cpuUtilization        The percentage of time the CPU was active.
     * @param throughput            The number of processes completed per unit of time.
     */
    public MetricsReport(double averageWaitingTime, 
                         double averageTurnaroundTime, 
                         double averageResponseTime, 
                         double cpuUtilization, 
                         double throughput) {
        this.averageWaitingTime = averageWaitingTime;
        this.averageTurnaroundTime = averageTurnaroundTime;
        this.averageResponseTime = averageResponseTime;
        this.cpuUtilization = cpuUtilization;
        this.throughput = throughput;
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public double getAverageTurnaroundTime() {
        return averageTurnaroundTime;
    }

    public double getAverageResponseTime() {
        return averageResponseTime;
    }

    public double getCpuUtilization() {
        return cpuUtilization;
    }

    public double getThroughput() {
        return throughput;
    }
}
