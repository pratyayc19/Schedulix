# Schedulix Class Diagram

This class diagram illustrates the domain models (DTOs), the strategy interfaces, and the core structural relationships that enforce the Single Responsibility and Dependency Inversion principles throughout the Schedulix framework.

```mermaid
classDiagram
    class Process {
        -String id
        -int arrivalTime
        -int burstTime
        -int priority
        -int remainingTime
        -int startTime
        -int completionTime
        +markCompleted()
        +getTurnaroundTime()
        +getWaitingTime()
        +getResponseTime()
    }

    class AlgorithmType {
        <<enumeration>>
        FCFS
        SJF
        SRTF
        PRIORITY
        PRIORITY_PREEMPTIVE
        ROUND_ROBIN
    }

    class GanttEntry {
        -String processId
        -int startTime
        -int endTime
    }

    class MetricsReport {
        -double averageWaitingTime
        -double averageTurnaroundTime
        -double averageResponseTime
        -double cpuUtilization
        -double throughput
    }

    class SimulationRequest {
        -AlgorithmType algorithmType
        -List~Process~ processes
        -Integer timeQuantum
    }

    class SimulationResult {
        -AlgorithmType algorithmType
        -List~Process~ processes
        -List~GanttEntry~ ganttChart
        -MetricsReport metricsReport
    }

    class SchedulingContext {
        -int currentTime
        -List~Process~ readyQueue
        -List~GanttEntry~ ganttEntries
        -Process currentlyRunningProcess
        -Integer timeQuantum
        +advanceTime(int duration)
        +recordExecution(String, int, int)
        +enqueueProcess(Process)
        +dequeueProcess() Process
    }

    class Scheduler {
        <<interface>>
        +schedule(SchedulingContext context)
        +getAlgorithmName() String
    }

    class SchedulerFactory {
        +create(AlgorithmType type) Scheduler
    }

    class SimulationEngine {
        +run(SimulationRequest request) SimulationResult
    }

    class MetricsCalculator {
        +generateReport(List~Process~ processes) MetricsReport
    }

    class ComparisonEngine {
        -SimulationEngine engine
        +runComparison(SimulationRequest baseRequest)
    }

    %% Composition & Aggregation
    SimulationRequest o-- AlgorithmType
    SimulationRequest o-- Process
    SimulationResult o-- Process
    SimulationResult o-- GanttEntry
    SimulationResult o-- MetricsReport
    
    %% Architectural Dependencies
    SimulationEngine ..> SchedulerFactory : Uses
    SimulationEngine ..> Scheduler : Executes
    SimulationEngine ..> SchedulingContext : Configures
    SimulationEngine ..> MetricsCalculator : Uses
    SimulationEngine ..> SimulationResult : Creates
    
    SchedulerFactory ..> Scheduler : Instantiates
    Scheduler ..> SchedulingContext : Mutates State
    
    ComparisonEngine o-- SimulationEngine : Wraps
```
