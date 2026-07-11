package scheduler;

import model.Process;
import simulation.SchedulingContext;

import java.util.Comparator;
import java.util.List;

/**
 * Implements the First-Come, First-Served (FCFS) scheduling algorithm.
 * 
 * FCFS is a non-preemptive algorithm that schedules processes strictly 
 * based on their arrival times. It operates entirely on the shared 
 * SchedulingContext, eliminating duplicated state management.
 */
public class FirstComeFirstServedScheduler implements Scheduler {

    @Override
    public void schedule(SchedulingContext context) {
        if (context == null || context.isReadyQueueEmpty()) {
            return;
        }

        // 1. Extract and deterministically sort the ready queue
        List<Process> readyQueue = context.getReadyQueue();
        readyQueue.sort(Comparator.comparingInt(Process::getArrivalTime)
                                  .thenComparing(Process::getId));

        // 2. Process all tasks in the queue
        while (!context.isReadyQueueEmpty()) {
            Process process = context.dequeueProcess();
            context.setCurrentlyRunningProcess(process);

            // Handle CPU idle periods
            if (context.getCurrentTime() < process.getArrivalTime()) {
                context.recordIdle(context.getCurrentTime(), process.getArrivalTime());
                context.advanceTime(process.getArrivalTime() - context.getCurrentTime());
            }

            // Mark execution start
            process.setStartTime(context.getCurrentTime());
            int executionStart = context.getCurrentTime();
            
            // Advance simulation clock by burst time
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

    @Override
    public String getAlgorithmName() {
        return "First-Come, First-Served (FCFS)";
    }
}
