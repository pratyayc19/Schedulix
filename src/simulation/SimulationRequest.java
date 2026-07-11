package simulation;

import model.AlgorithmType;
import model.Process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * An immutable container that encapsulates all input parameters required to 
 * execute a CPU scheduling simulation.
 * 
 * It ensures that the simulation engine receives all necessary contextual 
 * information (like time quantum for Round Robin) in a single unified DTO,
 * perfectly setting up the architecture for Phase 3 (Comparison Engine).
 */
public final class SimulationRequest {
    private final AlgorithmType algorithmType;
    private final List<Process> processes;
    private final Integer timeQuantum;

    /**
     * Constructs a new SimulationRequest.
     * 
     * @param algorithmType The scheduling algorithm to be used.
     * @param processes     The list of processes to be simulated.
     * @param timeQuantum   The time quantum (can be null if the algorithm does not require it).
     */
    public SimulationRequest(AlgorithmType algorithmType, List<Process> processes, Integer timeQuantum) {
        if (algorithmType == null) {
            throw new IllegalArgumentException("AlgorithmType cannot be null");
        }
        if (processes == null) {
            throw new IllegalArgumentException("Processes list cannot be null");
        }

        this.algorithmType = algorithmType;
        // Defensively copy lists to prevent external modification
        this.processes = Collections.unmodifiableList(new ArrayList<>(processes));
        this.timeQuantum = timeQuantum;
    }

    /**
     * @return The requested scheduling algorithm.
     */
    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    /**
     * @return An unmodifiable list of the processes to schedule.
     */
    public List<Process> getProcesses() {
        return processes;
    }

    /**
     * @return The time quantum, or null if not applicable.
     */
    public Integer getTimeQuantum() {
        return timeQuantum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimulationRequest that = (SimulationRequest) o;
        return algorithmType == that.algorithmType &&
               processes.equals(that.processes) &&
               Objects.equals(timeQuantum, that.timeQuantum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithmType, processes, timeQuantum);
    }

    @Override
    public String toString() {
        return "SimulationRequest{" +
                "algorithmType=" + algorithmType +
                ", processes=" + processes +
                ", timeQuantum=" + timeQuantum +
                '}';
    }
}
