package scheduler;

import model.AlgorithmType;

/**
 * Factory class responsible for centralizing the creation of Scheduler instances.
 * 
 * By implementing the Factory Design Pattern, this class decouples the 
 * SimulationEngine from the concrete scheduler implementations. This adheres 
 * strictly to the Open/Closed Principle (OCP) and Dependency Inversion Principle (DIP):
 * we can add new algorithms (e.g., SJF, Round Robin) by simply updating this factory, 
 * without touching the core SimulationEngine logic.
 */
public final class SchedulerFactory {

    // Private constructor to prevent instantiation of a utility factory
    private SchedulerFactory() {
        throw new UnsupportedOperationException("Factory class cannot be instantiated.");
    }

    /**
     * Creates and returns the appropriate Scheduler implementation for the given AlgorithmType.
     * 
     * @param algorithmType The type of scheduling algorithm requested.
     * @return A concrete instance of a class implementing the Scheduler interface.
     * @throws IllegalArgumentException if the provided AlgorithmType is null or currently unsupported.
     */
    public static Scheduler create(AlgorithmType algorithmType) {
        if (algorithmType == null) {
            throw new IllegalArgumentException("AlgorithmType cannot be null.");
        }

        switch (algorithmType) {
            case FCFS:
                return new FirstComeFirstServedScheduler();
            case SJF:
                return new ShortestJobFirstScheduler();
            case SRTF:
                return new ShortestRemainingTimeFirstScheduler();
            case PRIORITY:
                return new PriorityScheduler();
            case PRIORITY_PREEMPTIVE:
                return new PriorityPreemptiveScheduler();
            case ROUND_ROBIN:
                return new RoundRobinScheduler();
            // Future algorithms will be added here in Phase 2
            default:
                throw new IllegalArgumentException("Unsupported scheduling algorithm: " + algorithmType);
        }
    }
}
