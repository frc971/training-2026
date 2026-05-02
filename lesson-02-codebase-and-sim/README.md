# Lesson 02: Codebase Intro / WPILib Simulator / AdvantageScope

## Purpose

Give new members a bird's-eye view of the codebase and a first real workflow for running code without touching a robot.

## Prerequisites

- Week 1 setup is complete.
- Student can clone and open the repo on their own.

## Time box

1-2 hours

## Learning goals

- Understand the top-level flow of a WPILib Java robot project.
- Navigate the main folders in our 2026 repos.
- Run simulation and understand what the `sim/` config is doing.
- Use AdvantageScope to inspect logs and visualization output.

## In our codebase

- Main robot lifecycle: [971-second-robot-2026/src/main/java/frc/robot/Robot.java](../../971-second-robot-2026/src/main/java/frc/robot/Robot.java)
- Wiring and subsystem setup: [971-second-robot-2026/src/main/java/frc/robot/RobotContainer.java](../../971-second-robot-2026/src/main/java/frc/robot/RobotContainer.java)
- Controller bindings: [971-second-robot-2026/src/main/java/frc/robot/subsystems/Controllers.java](../../971-second-robot-2026/src/main/java/frc/robot/subsystems/Controllers.java)
- Simulation config notes: [971-second-robot-2026/sim/README.md](../../971-second-robot-2026/sim/README.md)
- Example visualization reference: [971-first-robot-2026/src/main/java/frc/robot/subsystems/superstructure/Visualization.java](../../971-first-robot-2026/src/main/java/frc/robot/subsystems/superstructure/Visualization.java)

## Codebase map

Use this lesson to explain the project structure from the outside in:

- `src/main/java/frc/robot`
  - robot lifecycle classes such as `Robot` and `Main`
  - wiring and input flow through `RobotContainer`
- `subsystems`
  - drivetrain, autos, controllers, vision, and superstructure logic
- `lib`
  - reusable helpers and lower-level abstractions
- `generated`
  - generated constants such as tuner output
- `src/main/deploy`
  - files deployed with the robot program, including auto/path assets
- `vendordeps`
  - third-party libraries such as Phoenix, AdvantageKit, PathPlanner, and B-Line
- `sim`
  - local simulation GUI configuration files

## How to read a new robot repo without drowning

New members often make the mistake of reading every file as if all files are equally important. They are not.

For a first pass:

1. Start with the lifecycle entry point.
2. Find the wiring and input layer.
3. Identify the major subsystems.
4. Look at deploy assets and library markers.
5. Only then dive into one subsystem deeply.

## Lesson content

Start at `Robot.java`. Show that this is the lifecycle entry point and that it sets up logging, mode-specific behavior, and major subsystems. Then move to `RobotContainer.java` to show where default behavior, controller handling, and subsystem ownership come together.

A new member should leave this lesson understanding the difference between:

- lifecycle code,
- subsystem code,
- helper libraries,
- deploy assets,
- and simulation support files.

## Simulator explanation

WPILib simulation is useful because it gives you a place to verify control flow before real robot time is available.

It is not a perfect copy of the robot. It is a development tool.

Use this lesson to make that distinction explicit:

- simulation helps test logic and some modeled behavior,
- simulation does not prove every hardware detail is correct,
- simulation is still worth using because it catches obvious wiring and control-flow mistakes early.

Then show the simulator workflow:

- The repo enables desktop simulation in `build.gradle`.
- The `sim/` folder contains saved GUI layout and key map JSON files.
- Simulation lets members test control flow and inspect state earlier than full robot testing.

## Suggested walkthrough

1. Open `Robot.java`.
2. Find where logging is configured.
3. Find where `RobotContainer` is created.
4. Open `RobotContainer.java`.
5. Trace one default subsystem behavior.
6. Look at `sim/README.md` and explain why the JSON files exist.
7. Run sim and match at least one code path to one visible behavior.

## AdvantageScope and logging

This repo uses AdvantageKit logging. In `Robot.java`, the logger setup changes based on mode:

- real robot logs to log files and NetworkTables,
- sim publishes to NetworkTables,
- replay can read old logs.

Use this to explain why logging matters:

- it gives you evidence,
- it helps you debug without guessing,
- it lets you inspect behavior after the run instead of relying on memory.

If you want a clearer visualization example, use first robot code and show how visualization output is recorded and viewed.

## Why this matters for real team tasks

Many first tasks do not involve writing a whole new subsystem. They involve understanding:

- where state lives,
- where a control decision is made,
- where logs should be added,
- and how to observe whether the code path actually ran.

## Required mech/elec callout

Software members should know the code is not acting on abstract variables. It is commanding hardware over CAN, reading sensors, and trying to move mechanisms with real constraints. Even in sim, keep asking "what hardware would this line affect on a robot?"

## Exercise

- Open `Robot.java` and explain what happens in:
  - constructor,
  - `robotPeriodic`,
  - `autonomousInit`,
  - `teleopInit`.
- Open `RobotContainer.java` and trace how the drivetrain default command is chosen.
- Copy the `sim/simgui*.json` files to the project root if needed for local sim setup.
- Run simulator and identify:
  - what controls the driver and operator inputs,
  - where pose or telemetry changes can be seen,
  - what logs or visual outputs are available.

## Debugging prompts

- If sim runs but behavior is confusing, find the default command or trigger path first.
- If controls do nothing, inspect `Controllers.java` before changing subsystem logic.
- If a value changes but nothing visible happens, ask whether it is only logged, only simulated, or actually consumed elsewhere.

## Deliverable / checkoff

- Student can explain the role of `Robot`, `RobotContainer`, `subsystems`, `lib`, `vendordeps`, and `src/main/deploy`.
- Student can describe the purpose of the `sim/` folder.
- Student can name one logged or visualized signal they inspected in sim or AdvantageScope.
- Student can describe one important limitation of simulation.

## Before next lesson

- Re-read the superstructure folder names so Week 3 is less overwhelming.
- Be ready to talk about control loops, motor abstractions, and CTRE structure.

## Task for this lesson

Follow [Lesson 02 Task: Codebase + Simulator Investigation](../tasks/lesson-02-codebase-sim-task/README.md).
