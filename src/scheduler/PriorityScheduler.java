package scheduler;

import model.Process;
import simulation.SchedulingContext;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Implements the non-preemptive Priority scheduling algorithm.
 * 
 * This algorithm selects the arrived process with the highest priority 
 * (represented by the lowest numeric value). Once a process starts execution, 
 * it runs to completion without being preempted. It operates exclusively on 
 * the shared SchedulingContext, reusing the established simulation infrastructure.
 */
public class PriorityScheduler implements Scheduler {

    @Override
    public void schedule(SchedulingContext context) {
        if (context == null || context.isReadyQueueEmpty()) {
            return;
        }

        List<Process> readyQueue = context.getReadyQueue();

        while (!readyQueue.isEmpty()) {
            Process process = selectNextProcess(readyQueue, context.getCurrentTime());
            readyQueue.remove(process);

            context.setCurrentlyRunningProcess(process);

            // Handle CPU idle periods if the selected process hasn't arrived yet
            if (context.getCurrentTime() < process.getArrivalTime()) {
                context.recordIdle(context.getCurrentTime(), process.getArrivalTime());
                context.advanceTime(process.getArrivalTime() - context.getCurrentTime());
            }

            // Mark execution start
            process.setStartTime(context.getCurrentTime());
            int executionStart = context.getCurrentTime();

            // Advance simulation clock by the entire burst time (non-preemptive)
            context.advanceTime(process.getBurstTime());
            int executionEnd = context.getCurrentTime();

            // Record Completion Time and Gantt Entry
            process.setCompletionTime(executionEnd);
            context.recordExecution(process.getId(), executionStart, executionEnd);

            // Finalize process state
            process.markCompleted();
            context.completeProcess(process);
        }
    }

    /**
     * Determines the next process to execute.
     * 
     * Prioritizes the process with the highest priority (lowest numeric value).
     * Tie-breakers: Arrival Time -> Process ID.
     */
    private Process selectNextProcess(List<Process> readyQueue, int currentTime) {
        // Attempt to find an already arrived process with the highest priority
        Optional<Process> nextArrived = readyQueue.stream()
                .filter(p -> p.getArrivalTime() <= currentTime)
                .min(Comparator.comparingInt(Process::getPriority)
                               .thenComparingInt(Process::getArrivalTime)
                               .thenComparing(Process::getId));

        // If an arrived process exists, return it.
        // Otherwise, skip forward and select the earliest arriving process in the future.
        // Note: If multiple processes arrive at that exact same future time, we still evaluate priority first.
        return nextArrived.orElseGet(() -> readyQueue.stream()
                .min(Comparator.comparingInt(Process::getArrivalTime)
                               .thenComparingInt(Process::getPriority)
                               .thenComparing(Process::getId))
                .orElse(null));
    }

    @Override
    public String getAlgorithmName() {
        return "Priority Scheduling (Non-Preemptive)";
    }
}
