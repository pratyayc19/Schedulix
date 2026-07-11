# Schedulix High-Level Architecture

The following diagram illustrates the high-level orchestration flow and structural dependencies between the core components of the Schedulix framework. It highlights the strict separation of concerns between state management (`SchedulingContext`), math calculation (`MetricsCalculator`), and actual algorithmic logic (`Scheduler`).

```mermaid
graph TD
    %% Core Inputs and Orchestration
    Client[Client Code / Comparison Engine] -->|Sends| Req[SimulationRequest]
    Req --> SE[SimulationEngine]
    
    %% Strategy and Factory Patterns
    SE -->|Delegates instantiation to| SF[SchedulerFactory]
    SF -->|Creates concrete instance| S[Scheduler Interface]
    S -.->|Implemented By| ConcreteSchedulers[FCFS, SJF, SRTF, RR, Priority]
    
    %% State Management
    SE -->|Instantiates & Configures| SC[SchedulingContext]
    S -->|Mutates state within| SC
    
    %% Metrics Generation
    SE -->|Passes finished processes to| MC[MetricsCalculator]
    
    %% DTO Construction
    SC -->|Provides Gantt & Process State| SR[SimulationResult]
    MC -->|Generates immutable| MR[MetricsReport]
    MR -->|Embedded into| SR
    
    %% Final Return
    SE -->|Returns| SR
    SR -->|Received by| Client
```
