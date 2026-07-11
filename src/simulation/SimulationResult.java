package simulation;

import model.AlgorithmType;
import model.Process;
import visualization.GanttEntry;
import metrics.MetricsReport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * An immutable container holding the complete results of a scheduling simulation.
 * 
 * It encapsulates the algorithm used, the final state of the processes,
 * the generated Gantt chart, and the computed metrics.
 * Defensive copying is used to ensure immutability.
 */
public final class SimulationResult {
    private final AlgorithmType algorithmType;
    private final List<Process> processes;
    private final List<GanttEntry> ganttChart;
    private final MetricsReport metricsReport;

    /**
     * Constructs a new SimulationResult.
     * 
     * @param algorithmType The algorithm used for the simulation.
     * @param processes     The list of processes in their final state.
     * @param ganttChart    The chronological list of execution blocks.
     * @param metricsReport The calculated statistics for the simulation.
     */
    public SimulationResult(AlgorithmType algorithmType, 
                            List<Process> processes, 
                            List<GanttEntry> ganttChart, 
                            MetricsReport metricsReport) {
        if (algorithmType == null) throw new IllegalArgumentException("AlgorithmType cannot be null");
        if (processes == null) throw new IllegalArgumentException("Processes list cannot be null");
        if (ganttChart == null) throw new IllegalArgumentException("Gantt chart cannot be null");
        if (metricsReport == null) throw new IllegalArgumentException("MetricsReport cannot be null");

        this.algorithmType = algorithmType;
        // Defensively copy lists to prevent external modification, 
        // and wrap them to prevent casting vulnerabilities.
        this.processes = Collections.unmodifiableList(new ArrayList<>(processes));
        this.ganttChart = Collections.unmodifiableList(new ArrayList<>(ganttChart));
        this.metricsReport = metricsReport;
    }

    /**
     * @return The algorithm type used to generate these results.
     */
    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    /**
     * @return An unmodifiable list of the scheduled processes.
     */
    public List<Process> getProcesses() {
        return processes;
    }

    /**
     * @return An unmodifiable list representing the timeline execution blocks.
     */
    public List<GanttEntry> getGanttChart() {
        return ganttChart;
    }

    /**
     * @return The computed metrics for the simulation.
     */
    public MetricsReport getMetricsReport() {
        return metricsReport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimulationResult that = (SimulationResult) o;
        return algorithmType == that.algorithmType &&
               processes.equals(that.processes) &&
               ganttChart.equals(that.ganttChart) &&
               metricsReport.equals(that.metricsReport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithmType, processes, ganttChart, metricsReport);
    }

    @Override
    public String toString() {
        return "SimulationResult{" +
                "algorithmType=" + algorithmType +
                ", processes=" + processes +
                ", ganttChart=" + ganttChart +
                ", metricsReport=" + metricsReport +
                '}';
    }
}
