# Lesson 06: Autos / B-Line / XRP Task

## Purpose

Teach the structure of autonomous code in this repo and give students a task that feels like a small real contribution instead of only passive explanation.

## Prerequisites

- Weeks 2-5.
- Comfortable tracing code from chooser or config to subsystem behavior.

## Time box

1-2 hours

## Learning goals

- Understand how autos are defined and selected in our codebase.
- Understand the role of B-Line and path assets in autonomous behavior.
- Recognize how commands and path segments combine into a routine.
- Complete a small autonomous coding task, ideally on an XRP or sim target if feasible.

## In our codebase

- Auto chooser and routine construction: [971-second-robot-2026/src/main/java/frc/robot/subsystems/Autos.java](../../971-second-robot-2026/src/main/java/frc/robot/subsystems/Autos.java)
- Path assets: [971-second-robot-2026/src/main/deploy/autos](../../971-second-robot-2026/src/main/deploy/autos)
- PathPlanner assets: [971-second-robot-2026/src/main/deploy/pathplanner](../../971-second-robot-2026/src/main/deploy/pathplanner)
- B-Line dependency marker: [971-second-robot-2026/vendordeps/BLine-Lib.json](../../971-second-robot-2026/vendordeps/BLine-Lib.json)
- Robot lifecycle entry to autos: [971-second-robot-2026/src/main/java/frc/robot/Robot.java](../../971-second-robot-2026/src/main/java/frc/robot/Robot.java)

## Lesson content

Start in `Autos.java`. Show that autos are not magic strings floating around the repo. The file defines routines, chooser options, path segment loading, mirroring behavior, and the final command sequence.

Key ideas to teach:

- an auto routine is a list of path names,
- chooser selection changes what gets cached and built,
- path data comes from deployed assets,
- the auto command is assembled from path-following commands.

Then connect this to B-Line:

- B-Line is part of the path following flow used here.
- Students do not need to understand every library detail yet.
- They do need to understand which parts are team-owned and which parts are library-owned.

## XRP task framing

If an XRP field setup is ready, let each student implement a different simple auto and run it there. If XRP field support is not ready, keep the exercise in simulation or code review form. Do not block the lesson on XRP-specific infrastructure.

Possible student tasks:

- add a simple new auto routine using existing path segments,
- create a small variation of an existing auto,
- inspect and explain why mirroring is or is not allowed for a routine,
- verify the autonomous start pose logic.

## Commands note

Commands can be explained here only as much as needed to understand:

- what gets returned from `getAutonomousCommand`,
- why autos are composed as sequences,
- how events or triggers can be attached during path execution.

## Exercise

- Read `Autos.java` and explain:
  - what an `AutoRoutine` contains,
  - what gets cached,
  - how start pose is determined,
  - how the final command is built.
- Implement or sketch one new simple auto variant.
- If XRP or sim support is available, run it and explain the observed behavior.

## Debugging prompts

- If an auto does not appear, inspect chooser population first.
- If the start pose seems wrong, inspect path loading and mirror logic.
- If the command exists but behavior is wrong, ask whether the issue is path data, path following, or event/subsystem behavior.

## Deliverable / checkoff

- Student can explain the flow from auto chooser selection to autonomous command execution.
- Student completes one small auto-related task or variation.
- Student can describe what part of the stack they changed and what part they left to the library.

## Before next lesson

- Be ready to transition from training tasks into real team work.
- Think about what skills still feel weak: code reading, Java, debugging, sim, deploy, or review turnaround.
