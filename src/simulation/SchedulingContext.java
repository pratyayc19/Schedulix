package simulation;

import model.Process;
import visualization.GanttEntry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the complete mutable execution state of a scheduling simulation.
 * 
 * By passing a single SchedulingContext to all algorithms, we eliminate 
 * duplicated state management (like tracking currentTime or recording Gantt entries)
 * across different Scheduler implementations. This heavily promotes code reuse.
 */
public class SchedulingContext {
    private int currentTime;
    private final List<Process> readyQueue;
    private final List<Process> completedProcesses;
    private final List<GanttEntry> ganttEntries;
    private Process currentlyRunningProcess;
    private final Integer timeQuantum;

    /**
     * Initializes a fresh context for a new simulation run.
     * 
     * @param timeQuantum The time quantum for round-robin algorithms (nullable).
     */
    public SchedulingContext(Integer timeQuantum) {
        this.currentTime = 0;
        // LinkedList used to optimize O(1) queue enqueue/dequeue operations
        this.readyQueue = new LinkedList<>(); 
        this.completedProcesses = new ArrayList<>();
        this.ganttEntries = new ArrayList<>();
        this.timeQuantum = timeQuantum;
    }

    /**
     * @return The current simulated time clock.
     */
    public int getCurrentTime() {
        return currentTime;
    }

    /**
     * Increments the simulated time clock by 1 unit.
     */
    public void incrementCurrentTime() {
        this.currentTime++;
    }

    /**
     * Advances the simulated time clock by a specific amount.
     * 
     * @param amount The number of time units to advance.
     */
    public void advanceTime(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot advance time by a negative amount.");
        }
        this.currentTime += amount;
    }

    /**
     * Adds a process to the back of the ready queue.
     */
    public void enqueueProcess(Process process) {
        if (process != null) {
            readyQueue.add(process);
        }
    }

    /**
     * Removes and returns the process at the front of the ready queue.
     * 
     * @return The dequeued Process, or null if the queue is empty.
     */
    public Process dequeueProcess() {
        if (readyQueue.isEmpty()) {
            return null;
        }
        return readyQueue.remove(0);
    }

    /**
     * @return true if the ready queue has no processes waiting.
     */
    public boolean isReadyQueueEmpty() {
        return readyQueue.isEmpty();
    }

    /**
     * Marks a process as completed and removes it from the running state.
     */
    public void completeProcess(Process process) {
        if (process != null) {
            completedProcesses.add(process);
            if (process.equals(currentlyRunningProcess)) {
                currentlyRunningProcess = null;
            }
        }
    }

    /**
     * Records a successful continuous execution block for a process.
     */
    public void recordExecution(String processId, int start, int end) {
        if (start < end) {
            ganttEntries.add(new GanttEntry(processId, start, end));
        }
    }

    /**
     * Records a period where the CPU was completely idle.
     */
    public void recordIdle(int start, int end) {
        if (start < end) {
            ganttEntries.add(new GanttEntry("IDLE", start, end));
        }
    }

    // Standard Getters & Setters

    public List<Process> getReadyQueue() {
        return readyQueue;
    }

    public List<Process> getCompletedProcesses() {
        return completedProcesses;
    }

    public List<GanttEntry> getGanttEntries() {
        return ganttEntries;
    }

    public Process getCurrentlyRunningProcess() {
        return currentlyRunningProcess;
    }

    public void setCurrentlyRunningProcess(Process process) {
        this.currentlyRunningProcess = process;
    }

    public Integer getTimeQuantum() {
        return timeQuantum;
    }
}
