package visualization;

/**
 * Represents a single continuous block of execution for a process on the CPU.
 * 
 * In non-preemptive algorithms, a process typically has one GanttEntry.
 * In preemptive algorithms, a process might be interrupted and thus have multiple GanttEntries.
 * This class is immutable because historical execution records should not change.
 */
public class GanttEntry {
    private final String processId;
    private final int startTime;
    private final int endTime;

    /**
     * Constructs a new GanttEntry.
     * 
     * @param processId The ID of the process (or "IDLE" for CPU idle periods).
     * @param startTime The time the execution block began.
     * @param endTime   The time the execution block finished.
     */
    public GanttEntry(String processId, int startTime, int endTime) {
        if (processId == null || processId.trim().isEmpty()) {
            throw new IllegalArgumentException("Process ID cannot be null or empty.");
        }
        if (startTime < 0) {
            throw new IllegalArgumentException("Start time cannot be negative.");
        }
        if (endTime <= startTime) {
            throw new IllegalArgumentException("End time must be strictly greater than start time.");
        }
        
        this.processId = processId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getProcessId() {
        return processId;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return String.format("[%d - %d] %s", startTime, endTime, processId);
    }
}
