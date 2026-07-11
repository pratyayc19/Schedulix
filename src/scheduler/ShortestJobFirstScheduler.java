package scheduler;

import model.Process;
import simulation.SchedulingContext;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Implements the non-preemptive Shortest Job First (SJF) scheduling algorithm.
 * 
 * SJF minimizes the average waiting time by executing the process with the 
 * smallest burst time among all arrived processes. It operates exclusively 
 * on the unified SchedulingContext.
 */
public class ShortestJobFirstScheduler implements Scheduler {

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
     * If multiple processes have arrived, selects the one with the smallest burst time.
     * Tie-breakers: Arrival Time -> Process ID.
     * If no processes have arrived yet, selects the process that will arrive next.
     */
    private Process selectNextProcess(List<Process> readyQueue, int currentTime) {
        // Attempt to find an already arrived process with the shortest burst time
        Optional<Process> nextArrived = readyQueue.stream()
                .filter(p -> p.getArrivalTime() <= currentTime)
                .min(Comparator.comparingInt(Process::getBurstTime)
                               .thenComparingInt(Process::getArrivalTime)
                               .thenComparing(Process::getId));

        // If an arrived process exists, return it.
        // Otherwise, skip forward and select the earliest arriving process in the future.
        return nextArrived.orElseGet(() -> readyQueue.stream()
                .min(Comparator.comparingInt(Process::getArrivalTime)
                               .thenComparingInt(Process::getBurstTime)
                               .thenComparing(Process::getId))
                .orElse(null));
    }

    @Override
    public String getAlgorithmName() {
        return "Shortest Job First (SJF)";
    }
}
