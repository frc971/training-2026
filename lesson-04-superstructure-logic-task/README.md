# Lesson 04: Superstructure Logic Task

## Purpose

Move from explanation into realistic newcomer work. The goal is to practice reading existing code, understanding the current behavior, and implementing a small logic change without breaking unrelated systems.

## Prerequisites

- Weeks 2-3.
- Comfort reading `Superstructure.java` and nearby classes.

## Time box

1-2 hours

## Learning goals

- Read older code before editing.
- Make a small, scoped change inside a real subsystem flow.
- Use logging, state inspection, and nearby patterns to debug.
- Practice the style of task a new member is likely to receive early in the season.

## In our codebase

- Main coordination file: [971-second-robot-2026/src/main/java/frc/robot/subsystems/superstructure/Superstructure.java](../../971-second-robot-2026/src/main/java/frc/robot/subsystems/superstructure/Superstructure.java)
- Controller inputs: [971-second-robot-2026/src/main/java/frc/robot/subsystems/Controllers.java](../../971-second-robot-2026/src/main/java/frc/robot/subsystems/Controllers.java)
- Utility patterns: [971-second-robot-2026/src/main/java/frc/robot/lib/superstructure](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure)

## Lesson content

This is the first lesson where the student should feel some ambiguity. That is intentional. Real team tasks are often not "write this from scratch." They are "read the current behavior, understand where the right place is, and add a simple piece of logic without damaging the rest."

Good first tasks for training look like:

- threshold-based behavior,
- a simple safety or gating condition,
- a helper method or utility extraction,
- a logging addition,
- a small state transition change,
- a straightforward button-triggered behavior.

Bad first tasks are:

- rewrites,
- broad architecture changes,
- tuning by guesswork,
- large unreviewed additions copied from GPT.

## Suggested training task

Use a scoped superstructure logic task such as one of these:

- Add thresholding around a state transition so an action only happens when a mechanism is ready.
- Add a log output for a specific state or decision point.
- Gate a behavior behind a clearer condition using existing controller or subsystem state.
- Refactor repeated condition logic into a small helper without changing behavior.

The exact task can vary, but the structure of the work should stay the same:

1. Read the current code path.
2. Explain the current behavior before changing anything.
3. Choose the smallest correct edit point.
4. Test or simulate the change.
5. Explain how you know it worked.

## Why this lesson exists

This lesson is directly targeting a team problem: newer members often can complete a Java eval but still cannot turn that into useful progress on real robot code.

This task is meant to force the missing skills:

- reading older code,
- choosing the right edit location,
- testing behavior,
- and explaining implementation choices.

That matters more here than perfect code style on the first try.

## Good task shapes

Examples that fit this lesson well:

- add or adjust a readiness threshold,
- add a small log signal around a state decision,
- prevent an action from running under a simple unsafe condition,
- clean up duplicated conditional logic into a helper,
- add a small button-driven behavior that follows an existing pattern.

Examples that do not fit:

- rewriting the superstructure state model,
- changing many subsystems at once,
- tuning constants without understanding the system,
- adding behavior with no test plan.

## What mentors should watch for

- Does the student search before asking?
- Do they read nearby code or only the line they want to edit?
- Can they describe what broke when debugging?
- Do they use logs or evidence, or only instinct?

## Exercise

- Pick one simple task in or near `Superstructure.java`.
- Before editing, write down:
  - current behavior,
  - desired behavior,
  - files you expect to touch,
  - one risk.
- Implement the change.
- Test using sim, logs, or inspection if possible.
- Explain what evidence shows the change is correct.

## Debugging prompts

- If behavior is wrong, verify the student changed the correct layer.
- If the code compiles but does nothing, check whether the code path is actually reached.
- If the logic feels hard to reason about, reduce it to the exact condition that should be true or false.

## Deliverable / checkoff

- Student can explain the existing behavior before describing their edit.
- Student completes one scoped logic task with support.
- Student can point to evidence that the behavior changed as intended.
- Student can explain why the chosen edit point was the right layer.

## Before next lesson

- Review drivetrain-related classes so the names and structure are familiar.
- Be ready to compare mechanism logic with drivetrain architecture.

## Task for this lesson

Follow [Lesson 04 Task: Superstructure Logic Change](../tasks/lesson-04-superstructure-logic-task/README.md).
