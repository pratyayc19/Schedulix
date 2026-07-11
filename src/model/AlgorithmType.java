package model;

/**
 * Enumerates all supported CPU scheduling algorithms in the Schedulix engine.
 * 
 * Using an enum instead of raw strings enforces type safety, prevents typos, 
 * and provides a single source of truth for the algorithms supported by the 
 * engine.
 */
public enum AlgorithmType {
    FCFS("First-Come, First-Served"),
    SJF("Shortest Job First (Non-Preemptive)"),
    SRTF("Shortest Remaining Time First"),
    PRIORITY("Priority Scheduling (Non-Preemptive)"),
    PRIORITY_PREEMPTIVE("Priority Scheduling (Preemptive)"),
    ROUND_ROBIN("Round Robin");

    private final String displayName;

    /**
     * Constructor for AlgorithmType.
     * 
     * @param displayName The human-readable name of the algorithm.
     */
    AlgorithmType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the human-readable display name of the scheduling algorithm.
     * 
     * @return The formatted name of the algorithm.
     */
    public String getDisplayName() {
        return displayName;
    }
}
