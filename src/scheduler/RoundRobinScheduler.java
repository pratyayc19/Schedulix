package scheduler;

import model.Process;
import simulation.SchedulingContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Implements the preemptive Round Robin (RR) scheduling algorithm.
 * 
 * Round Robin executes each process for a strict time quantum. If the process 
 * does not finish within this quantum, it is preempted and placed at the back 
 * of the ready queue. This algorithm guarantees fairness and prevents starvation.
 */
public class RoundRobinScheduler implements Scheduler {

    @Override
    public void schedule(SchedulingContext context) {
        if (context == null || context.isReadyQueueEmpty()) {
            return;
        }

        if (context.getTimeQuantum() == null || context.getTimeQuantum() <= 0) {
            throw new IllegalArgumentException("Round Robin requires a strictly positive time quantum.");
        }

        int timeQuantum = context.getTimeQuantum();

        // 1. Extract all processes into a local pool to manage absolute arrivals.
        List<Process> sortedList = new ArrayList<>(context.getReadyQueue());
        sortedList.sort(Comparator.comparingInt(Process::getArrivalTime)
                                  .thenComparing(Process::getId));
        
        // LinkedList provides O(1) removal from the head
        List<Process> unarrived = new LinkedList<>(sortedList);
        
        // Clear the context's ready queue so we can use it as the true runtime FIFO queue.
        context.getReadyQueue().clear();

        // 2. Initial arrivals at T=0
        enqueueNewArrivals(unarrived, context, context.getCurrentTime());

        // 3. Execute Round Robin
        while (!context.isReadyQueueEmpty() || !unarrived.isEmpty()) {
            
            // Handle CPU Idle Periods
            if (context.isReadyQueueEmpty()) {
                Process nextArriving = unarrived.get(0);
                context.recordIdle(context.getCurrentTime(), nextArriving.getArrivalTime());
                context.advanceTime(nextArriving.getArrivalTime() - context.getCurrentTime());
                enqueueNewArrivals(unarrived, context, context.getCurrentTime());
            }

            // Fetch next process in the FIFO queue
            Process process = context.dequeueProcess();
            context.setCurrentlyRunningProcess(process);

            // Record Start Time only on the absolute first CPU acquisition
            if (process.getStartTime() == -1) {
                process.setStartTime(context.getCurrentTime());
            }

            int executionStart = context.getCurrentTime();
            int duration = Math.min(process.getRemainingTime(), timeQuantum);

            // Advance simulation clock by the executed duration
            context.advanceTime(duration);
            int executionEnd = context.getCurrentTime();
            
            // Record execution slice
            process.setRemainingTime(process.getRemainingTime() - duration);
            context.recordExecution(process.getId(), executionStart, executionEnd);

            // CRITICAL STEP: New arrivals must enter the queue BEFORE the preempted process is re-enqueued.
            enqueueNewArrivals(unarrived, context, context.getCurrentTime());

            // Handle process completion or preemption
            if (process.getRemainingTime() == 0) {
                process.setCompletionTime(context.getCurrentTime());
                process.markCompleted();
                context.completeProcess(process);
            } else {
                // Preempt and push to the back of the queue
                context.enqueueProcess(process);
            }
        }
    }

    /**
     * Helper method to transfer processes from the unarrived pool to the 
     * active ready queue when the simulation clock reaches their arrival time.
     */
    private void enqueueNewArrivals(List<Process> unarrived, SchedulingContext context, int currentTime) {
        while (!unarrived.isEmpty() && unarrived.get(0).getArrivalTime() <= currentTime) {
            context.enqueueProcess(unarrived.remove(0));
        }
    }

    @Override
    public String getAlgorithmName() {
        return "Round Robin (RR)";
    }
}
