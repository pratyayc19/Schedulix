package scheduler;

import model.Process;
import simulation.SchedulingContext;

import java.util.Comparator;
import java.util.List;

/**
 * Implements the preemptive Priority scheduling algorithm.
 * 
 * It continuously evaluates the ready queue to ensure that the process with the 
 * highest priority (strictly lowest numeric value) is always running. If a higher 
 * priority process arrives, the current process is immediately preempted. 
 * Reuses the unified SchedulingContext for all state management.
 */
public class PriorityPreemptiveScheduler implements Scheduler {

    @Override
    public void schedule(SchedulingContext context) {
        if (context == null || context.isReadyQueueEmpty()) {
            return;
        }

        List<Process> readyQueue = context.getReadyQueue();
        Process currentProcess = null;
        int executionStart = 0;

        while (!readyQueue.isEmpty()) {
            int currentTime = context.getCurrentTime();
            Process nextProcess = selectNextProcess(readyQueue, currentTime);

            // Handle CPU Idle Periods
            if (nextProcess == null) {
                int nextArrival = getNextArrivalTime(readyQueue);
                context.recordIdle(currentTime, nextArrival);
                context.advanceTime(nextArrival - currentTime);
                continue;
            }

            // Detect Context Switch (Preemption or new process starting)
            if (currentProcess != null && !currentProcess.equals(nextProcess) && currentProcess.getRemainingTime() > 0) {
                context.recordExecution(currentProcess.getId(), executionStart, currentTime);
                executionStart = currentTime;
            } else if (currentProcess == null || !currentProcess.equals(nextProcess)) {
                executionStart = currentTime;
            }

            currentProcess = nextProcess;
            context.setCurrentlyRunningProcess(currentProcess);

            // Record Start Time only on the absolute first CPU acquisition
            if (currentProcess.getStartTime() == -1) {
                currentProcess.setStartTime(currentTime);
            }

            // Determine how long this process can run before completion or preemption
            int duration = findExecutionDuration(readyQueue, currentProcess, currentTime);

            // Execute the time slice
            context.advanceTime(duration);
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - duration);

            // Handle Process Completion
            if (currentProcess.getRemainingTime() == 0) {
                currentProcess.setCompletionTime(context.getCurrentTime());
                context.recordExecution(currentProcess.getId(), executionStart, context.getCurrentTime());
                currentProcess.markCompleted();
                context.completeProcess(currentProcess);
                readyQueue.remove(currentProcess);
                currentProcess = null; // Clear to force a new execution start on next loop
            }
        }
    }

    /**
     * Selects the arrived process with the highest priority (lowest numeric value).
     * Tie-breakers: Arrival Time -> Process ID.
     */
    private Process selectNextProcess(List<Process> readyQueue, int currentTime) {
        return readyQueue.stream()
                .filter(p -> p.getArrivalTime() <= currentTime)
                .min(Comparator.comparingInt(Process::getPriority)
                               .thenComparingInt(Process::getArrivalTime)
                               .thenComparing(Process::getId))
                .orElse(null);
    }

    /**
     * Determines the exact time duration a process can run before it either completes
     * or is preempted by a newly arriving process with a strictly higher priority.
     */
    private int findExecutionDuration(List<Process> readyQueue, Process current, int currentTime) {
        int actualDuration = current.getRemainingTime();
        
        for (Process p : readyQueue) {
            if (p.getArrivalTime() > currentTime) {
                int delta = p.getArrivalTime() - currentTime;
                if (delta < actualDuration) {
                    // Preempt strictly if the newly arrived process has a higher priority (lower numeric value).
                    // Equal priority does not preempt because the current process arrived earlier.
                    if (p.getPriority() < current.getPriority()) {
                        actualDuration = delta;
                    }
                }
            }
        }
        return actualDuration;
    }

    /**
     * Finds the earliest arrival time among all processes currently in the queue.
     * Used exclusively when the CPU is idle.
     */
    private int getNextArrivalTime(List<Process> readyQueue) {
        return readyQueue.stream()
                .mapToInt(Process::getArrivalTime)
                .min()
                .orElseThrow(() -> new IllegalStateException("Ready queue is empty during idle check"));
    }

    @Override
    public String getAlgorithmName() {
        return "Priority Scheduling (Preemptive)";
    }
}
