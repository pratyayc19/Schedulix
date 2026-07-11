package demo;

import model.AlgorithmType;
import model.Process;
import presentation.ConsoleTimelineRenderer;
import simulation.SimulationEngine;
import simulation.SimulationRequest;
import simulation.SimulationResult;
import visualization.GanttEntry;

import java.util.Arrays;
import java.util.List;

/**
 * A dedicated demonstration class intended to generate screenshots for the Schedulix README.
 * It simulates a Round Robin workload to showcase the ConsoleTimelineRenderer's ability 
 * to handle preemptive execution blocks dynamically.
 */
public class TimelineRendererDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================================================");
        System.out.println("                                SCHEDULIX GANTT CHART RENDERER                              ");
        System.out.println("==========================================================================================\n");

        // 1. Create a small sample workload suitable for forcing preemptive slices
        List<Process> processes = Arrays.asList(
                new Process("P1", 0, 8, 1),
                new Process("P2", 1, 5, 1),
                new Process("P3", 2, 2, 1),
                new Process("P4", 3, 4, 1)
        );

        // We use a strict Time Quantum of 3 to force context switching
        int timeQuantum = 3;

        // 2. Create the request specifically demanding Round Robin
        SimulationRequest request = new SimulationRequest(AlgorithmType.ROUND_ROBIN, processes, timeQuantum);

        // 3. Execute the simulation through the central engine
        SimulationEngine engine = new SimulationEngine();
        SimulationResult result = engine.run(request);

        // 4. Extract the chronological execution records
        List<GanttEntry> ganttChart = result.getGanttChart();

        // 5. Render the ASCII chart
        ConsoleTimelineRenderer renderer = new ConsoleTimelineRenderer();
        System.out.println("Algorithm: ROUND ROBIN (Quantum = " + timeQuantum + ")\n");
        
        renderer.render(ganttChart);
        
        System.out.println("\n==========================================================================================");
    }
}
