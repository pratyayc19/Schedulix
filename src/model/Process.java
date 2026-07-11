package model;

import java.util.Objects;

/**
 * Represents a process in the CPU scheduling simulation.
 * This class follows the Single Responsibility Principle by only managing the state
 * and attributes of a single process.
 */
public class Process {
    private final String id;
    private final int arrivalTime;
    private final int burstTime;
    private final int priority;
    
    // State variables updated by the scheduler
    private int remainingTime;
    private int startTime = -1;
    private int completionTime = -1;

    public Process(String id, int arrivalTime, int burstTime) {
        this(id, arrivalTime, burstTime, 0);
    }

    public Process(String id, int arrivalTime, int burstTime, int priority) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Process ID cannot be null or empty.");
        }
        if (arrivalTime < 0) {
            throw new IllegalArgumentException("Arrival time cannot be negative.");
        }
        if (burstTime <= 0) {
            throw new IllegalArgumentException("Burst time must be strictly positive.");
        }
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }

    public String getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        if (remainingTime < 0) {
            throw new IllegalArgumentException("Remaining time cannot be negative.");
        }
        this.remainingTime = remainingTime;
    }

    public void markCompleted() {
        this.remainingTime = 0;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        if (startTime < arrivalTime) {
            throw new IllegalArgumentException("Start time cannot be before arrival time.");
        }
        this.startTime = startTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        if (completionTime < startTime) {
            throw new IllegalArgumentException("Completion time cannot be before start time.");
        }
        this.completionTime = completionTime;
    }

    // Calculated metrics

    public int getTurnaroundTime() {
        if (completionTime == -1) return 0;
        return completionTime - arrivalTime;
    }

    public int getWaitingTime() {
        return getTurnaroundTime() - burstTime;
    }

    public int getResponseTime() {
        if (startTime == -1) return 0;
        return startTime - arrivalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Process process = (Process) o;
        return id.equals(process.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Process{" +
                "id='" + id + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", priority=" + priority +
                ", remainingTime=" + remainingTime +
                ", startTime=" + startTime +
                ", completionTime=" + completionTime +
                '}';
    }
}
