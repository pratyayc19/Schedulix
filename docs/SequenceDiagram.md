# Schedulix Sequence Diagram

This sequence diagram illustrates the exact chronological execution flow of a single simulation request, from the moment it is received by the `SimulationEngine` to the final return of the immutable `SimulationResult`.

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Engine as SimulationEngine
    participant Factory as SchedulerFactory
    participant Context as SchedulingContext
    participant Scheduler as ConcreteScheduler
    participant Metrics as MetricsCalculator

    Client->>Engine: run(SimulationRequest)
    activate Engine
    
    %% Strategy Retrieval
    Engine->>Factory: create(request.getAlgorithmType())
    activate Factory
    Factory-->>Engine: return Scheduler Instance
    deactivate Factory
    
    %% Context Initialization
    Engine->>Context: new SchedulingContext(timeQuantum)
    activate Context
    
    loop for each Process in request
        Engine->>Context: enqueueProcess(process)
    end
    
    %% Scheduling Execution
    Engine->>Scheduler: schedule(context)
    activate Scheduler
    
    loop until Context.isReadyQueueEmpty()
        Scheduler->>Context: dequeueProcess()
        
        %% State Mutations
        Scheduler->>Context: advanceTime(duration)
        Scheduler->>Context: recordExecution(id, start, end)
        
        opt if process completes
            Scheduler->>Context: completeProcess(process)
        end
    end
    
    Scheduler-->>Engine: (execution complete)
    deactivate Scheduler
    
    %% Gantt and Metric Extraction
    Engine->>Context: getGanttEntries()
    Context-->>Engine: List~GanttEntry~
    deactivate Context
    
    Engine->>Metrics: generateReport(processes)
    activate Metrics
    Metrics-->>Engine: MetricsReport
    deactivate Metrics
    
    %% Final Assembly
    Engine->>Client: return SimulationResult
    deactivate Engine
```
