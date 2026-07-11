package test;

import model.AlgorithmType;
import model.Process;
import simulation.SimulationEngine;
import simulation.SimulationRequest;
import simulation.SimulationResult;
import visualization.GanttEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Validation suite to automatically verify the correctness of all scheduling algorithms.
 * 
 * Uses Invariant Testing (Property-Based Testing) to ensure that regardless of the 
 * workload or the algorithm used, the fundamental mathematical constraints of CPU 
 * scheduling hold true (e.g., Turnaround Time == Completion Time - Arrival Time).
 */
public class SchedulerValidationSuite {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   SCHEDULIX AUTOMATED VALIDATION SUITE");
        System.out.println("==================================================");

        List<SimulationRequest> workloads = buildWorkloads();
        SimulationEngine engine = new SimulationEngine();

        for (AlgorithmType algorithm : AlgorithmType.values()) {
            boolean allPassed = true;
            System.out.print(String.format("Validating %-40s ", algorithm.name() + "..."));
            
            for (SimulationRequest request : workloads) {
                // Clone the processes for each algorithm so they start fresh
                SimulationRequest freshRequest = cloneRequest(request, algorithm);
                try {
                    SimulationResult result = engine.run(freshRequest);
                    if (!verifyInvariants(result)) {
                        allPassed = false;
                        break;
                    }
                } catch (Exception e) {
                    allPassed = false;
                    break;
                }
            }

            if (allPassed) {
                System.out.println("[ PASS ]");
            } else {
                System.out.println("[ FAIL ]");
            }
        }
    }

    /**
     * Constructs multiple deterministic test cases to stress test the schedulers.
     */
    private static List<SimulationRequest> buildWorkloads() {
        List<SimulationRequest> workloads = new ArrayList<>();

        // 1. Simple sequential arrivals
        workloads.add(new SimulationRequest(AlgorithmType.FCFS, Arrays.asList(
                new Process("P1", 0, 5, 1),
                new Process("P2", 2, 3, 2),
                new Process("P3", 4, 1, 3)
        ), 2));

        // 2. Simultaneous arrivals
        workloads.add(new SimulationRequest(AlgorithmType.FCFS, Arrays.asList(
                new Process("P1", 0, 4, 2),
                new Process("P2", 0, 2, 1),
                new Process("P3", 0, 3, 3)
        ), 2));

        // 3. CPU idle periods (gap between P1 ending and P2 arriving)
        workloads.add(new SimulationRequest(AlgorithmType.FCFS, Arrays.asList(
                new Process("P1", 0, 2, 1),
                new Process("P2", 5, 2, 1) 
        ), 2));

        // 4. High priority arrival
        workloads.add(new SimulationRequest(AlgorithmType.FCFS, Arrays.asList(
                new Process("P1", 0, 10, 5),
                new Process("P2", 2, 2, 1)
        ), 2));

        // 5. Round Robin quantum validation
        workloads.add(new SimulationRequest(AlgorithmType.FCFS, Arrays.asList(
                new Process("P1", 0, 5, 1),
                new Process("P2", 1, 4, 1),
                new Process("P3", 2, 3, 1)
        ), 2));

        return workloads;
    }

    /**
     * Creates a deep copy of a simulation request, explicitly injecting the new AlgorithmType.
     */
    private static SimulationRequest cloneRequest(SimulationRequest original, AlgorithmType newType) {
        List<Process> clonedProcesses = new ArrayList<>();
        for (Process p : original.getProcesses()) {
            clonedProcesses.add(new Process(p.getId(), p.getArrivalTime(), p.getBurstTime(), p.getPriority()));
        }
        return new SimulationRequest(newType, clonedProcesses, original.getTimeQuantum());
    }

    /**
     * Mathematically verifies that a SimulationResult does not violate the laws of scheduling.
     * Validates Completion Time, Waiting Time, Turnaround Time, Response Time, and Gantt structure.
     */
    private static boolean verifyInvariants(SimulationResult result) {
        for (Process p : result.getProcesses()) {
            // 1. Verify metric constraints
            if (p.getTurnaroundTime() != p.getCompletionTime() - p.getArrivalTime()) return false;
            if (p.getWaitingTime() != p.getTurnaroundTime() - p.getBurstTime()) return false;
            if (p.getResponseTime() != p.getStartTime() - p.getArrivalTime()) return false;

            // 2. Verify Gantt entries sum up exactly to the total Burst Time
            int executedTime = 0;
            for (GanttEntry entry : result.getGanttChart()) {
                if (entry.getProcessId().equals(p.getId())) {
                    executedTime += (entry.getEndTime() - entry.getStartTime());
                }
            }
            if (executedTime != p.getBurstTime()) return false;
        }

        // 3. Verify Gantt chart chronological order and lack of overlaps
        List<GanttEntry> gantt = result.getGanttChart();
        for (int i = 0; i < gantt.size() - 1; i++) {
            GanttEntry current = gantt.get(i);
            GanttEntry next = gantt.get(i + 1);
            if (current.getEndTime() > next.getStartTime()) {
                return false; // Time overlap detected
            }
        }

        return true;
    }
}
