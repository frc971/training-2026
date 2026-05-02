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

## Lesson content

Start at `Robot.java`. Show that this is the lifecycle entry point and that it sets up logging, mode-specific behavior, and major subsystems. Then move to `RobotContainer.java` to show where default behavior, controller handling, and subsystem ownership come together.

A new member should leave this lesson understanding the difference between:

- lifecycle code,
- subsystem code,
- helper libraries,
- deploy assets,
- and simulation support files.

Then show the simulator workflow:

- The repo enables desktop simulation in `build.gradle`.
- The `sim/` folder contains saved GUI layout and key map JSON files.
- Simulation lets members test control flow and inspect state earlier than full robot testing.

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

## Before next lesson

- Re-read the superstructure folder names so Week 3 is less overwhelming.
- Be ready to talk about control loops, motor abstractions, and CTRE structure.
