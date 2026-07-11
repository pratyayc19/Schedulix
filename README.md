# Schedulix

![Java](https://img.shields.io/badge/Java-23-orange.svg)
![Version](https://img.shields.io/badge/version-1.0-blue.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)
![Status](https://img.shields.io/badge/status-Active-success.svg)

## Project Overview
Schedulix is a modular CPU Scheduling Framework focused on clean software architecture and Operating Systems simulation. It accurately models process states, preemptive and non-preemptive execution, and context switching. Designed with a strict adherence to object-oriented principles, it serves as both an educational resource for scheduling algorithms and a practical demonstration of scalable Java engineering.

## Features
- **Scheduling Metrics:** Calculates Turnaround Time, Waiting Time, Response Time, CPU Utilization, and Throughput in a single mathematically optimized $O(N)$ pass.
- **Algorithm Comparison Engine:** Automates benchmarking to evaluate multiple algorithms side-by-side using identical workloads.
- **Validation Suite:** Leverages property-based invariant testing to guarantee mathematical scheduling constraints are maintained.
- **Console Timeline Renderer:** Generates a visual, ASCII-based Gantt chart to perfectly align and display process execution timelines.

## Implemented Algorithms
1. First-Come, First-Served (FCFS)
2. Shortest Job First (SJF) - Non-Preemptive
3. Shortest Remaining Time First (SRTF) - Preemptive
4. Priority Scheduling - Non-Preemptive
5. Priority Scheduling - Preemptive
6. Round Robin (RR) - Preemptive

## Architecture
Schedulix relies on a modular, layered architecture:
- `SimulationEngine`: The core orchestrator that drives the simulation lifecycle.
- `SchedulingContext`: A shared state abstraction that prevents duplicated tracking variables across schedulers.
- `MetricsCalculator`: An isolated component dedicated exclusively to statistical calculations.
- `ComparisonEngine`: An independent layer for benchmarking workloads across all implementations.

## Technologies Used
- Java 23
- Object-Oriented Programming (OOP)
- SOLID Principles
- Strategy Pattern
- Factory Pattern
- Layered Architecture
- Git

## Folder Structure
```text
src/
├── comparison/      # Automated workload comparison algorithms
├── metrics/         # Statistical calculations and DTOs
├── model/           # Core domain models (Process, AlgorithmType)
├── presentation/    # Visual renderers (Console Gantt Chart)
├── scheduler/       # Scheduling strategy implementations and Factory
├── simulation/      # Orchestration and State Management
└── test/            # Property-based validation suite
```

## Screenshots
**Coming Soon:**
- Validation Suite Output
- Algorithm Comparison Output
- Console Timeline Renderer

## Complexity Table
| Algorithm | Time Complexity | Preemptive | Starvation Risk |
|-----------|-----------------|------------|-----------------|
| FCFS      | $O(N \log N)$   | No         | None            |
| SJF       | $O(N^2)$        | No         | High            |
| SRTF      | $O(N^2)$        | Yes        | High            |
| Priority  | $O(N^2)$        | No         | High (without Aging) |
| Priority (Preemptive) | $O(N^2)$ | Yes | High (without Aging) |
| Round Robin| $O(N \log N)$  | Yes        | None            |

*(Note: Time complexities indicate the algorithmic overhead of the scheduler selecting processes, bounded by $N$ number of processes).*

## Design Patterns Used
- **Strategy Pattern:** Schedulers implement a common `Scheduler` interface, allowing dynamic swapping at runtime.
- **Factory Pattern:** `SchedulerFactory` centralizes the instantiation of scheduling algorithms.
- **Data Transfer Object (DTO):** `SimulationRequest`, `SimulationResult`, `GanttEntry`, and `MetricsReport` encapsulate immutable data across boundaries.
- **Layered Architecture:** Strict separation between simulation logic, metrics calculation, and presentation rendering.

## SOLID Principles Used
- **Single Responsibility Principle (SRP):** `MetricsCalculator` exclusively computes math; `ConsoleTimelineRenderer` exclusively renders text.
- **Open/Closed Principle (OCP):** New algorithms can be added by implementing the interface and expanding the enum without altering the `SimulationEngine`.
- **Dependency Inversion Principle (DIP):** Higher-level orchestrators depend strictly on the `Scheduler` interface rather than concrete algorithmic implementations.

## How to Run

**1. Compile the Project**
Navigate to the root directory and compile the test suite:
```bash
javac -cp src src/test/SchedulerValidationSuite.java
```

**2. Run the Validation Suite**
Verify all algorithms function correctly via invariant testing:
```bash
java -cp src test.SchedulerValidationSuite
```

**3. Run the Comparison Engine**
*(Requires a custom `Main` class to instantiate `ComparisonEngine` and provide a workload)*
```bash
java -cp src Main
```

## Future Roadmap
- Graphical User Interface (JavaFX / Swing)
- Benchmark Engine
- CSV Import/Export
- Playback Engine
- Multi-Level Feedback Queue (MLFQ)
- Multi-Level Queue (MLQ)
