package scheduler;

import simulation.SchedulingContext;

/**
 * Defines the core contract for all CPU scheduling algorithms.
 * This interface adheres to the Dependency Inversion Principle (DIP) and 
 * Strategy Design Pattern, allowing the application to seamlessly swap 
 * out scheduling implementations (e.g., FCFS, SJF) without modifying client code.
 */
public interface Scheduler {
    
    /**
     * Executes the scheduling algorithm utilizing the shared simulation context.
     * Implementing classes are expected to update process states and record Gantt entries.
     *
     * @param context The mutable execution state of the current simulation.
     */
    void schedule(SchedulingContext context);

    /**
     * Provides the name of the scheduling algorithm.
     * 
     * @return The name of the scheduling algorithm.
     */
    String getAlgorithmName();
}
