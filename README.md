# 2026 Software Training

This repo is the lesson set for new software members in 2026. Each lesson lives in its own folder and is written like a GitHub wiki page: short sections, direct links, concrete repo references, and a task or checkoff at the end.

The main reference codebase is [971-second-robot-2026](../971-second-robot-2026/README.md). When a concept is easier to show on a different robot, the lesson calls that out explicitly.

# Training material is also located in the [wiki](https://github.com/frc971/training-2026/wiki)

## How to use this repo

- Read the lesson page before starting the weekly task.
- Follow the repo links inside the lesson while reading code.
- Do the exercise or task at the end of the page.
- Test in sim when possible before asking for review.
- Submit work through normal branch and PR flow once a mentor says the task is ready for repo work.

## Expectations for new members

- Understand the shape of our Java/WPILib codebase well enough to find the right subsystem, command path, or helper class.
- Use the Java we actually use, not just syntax from the eval. That includes reading interfaces, inheritance, wrappers, records, utility classes, and subsystem structure.
- Debug actively. Do not stop at "it didn't work" and do not depend on GPT copy-paste loops as the main workflow.
- Be able to complete easier high-priority tasks or lower-priority exploratory tasks with support.
- Prioritize understanding the implementation over trying to look productive.

## Required context outside software

- New software members should also get basic mech/elec context.
- At minimum they should understand what CAN is, what hardware is on the bus, and the rough path from code to motor behavior.
- Later in training, they also need exposure to Driver Station, deploy flow, and how code is tested on a real robot.

## Lessons

1. [Lesson 01: Software Intro / Tooling Setup / Git + GitHub](lesson-01-intro-tooling-git/README.md)
2. [Lesson 02: Codebase Intro / WPILib Simulator / AdvantageScope](lesson-02-codebase-and-sim/README.md)
3. [Lesson 03: Superstructure / PID / CTRE Theory](lesson-03-superstructure-pid-ctre/README.md)
4. [Lesson 04: Superstructure Logic Task](lesson-04-superstructure-logic-task/README.md)
5. [Lesson 05: Drivetrain Introduction](lesson-05-drivetrain-intro/README.md)
6. [Lesson 06: Autos / B-Line / XRP Task](lesson-06-autos-bline-xrp/README.md)
7. [Lesson 07-08: Transition to Software Member](lesson-07-08-transition-to-software-member/README.md)

## Tasks

- Task index: [tasks/README.md](tasks/README.md)
- Java eval copy from 2025: [tasks/java-evaluation/README.md](tasks/java-evaluation/README.md)
- Every lesson after intro/tooling now has a corresponding task page in `tasks/`.

## Suggested pacing

- Before Week 1: complete Java eval.
- Weeks 1-6: complete one lesson per Saturday and do the small task or checkoff attached to it.
- Weeks 7-8: transition into real robot work, 3rd robot work, or a scoped contribution on an existing codebase with close support.

## Repo references used across this training

- Second robot structure: [971-second-robot-2026/src/main/java/frc/robot](../971-second-robot-2026/src/main/java/frc/robot)
- Simulation config: [971-second-robot-2026/sim](../971-second-robot-2026/sim)
- Autos and deploy assets: [971-second-robot-2026/src/main/deploy](../971-second-robot-2026/src/main/deploy)
- First robot visualization reference when needed: [971-first-robot-2026/src/main/java/frc/robot](../971-first-robot-2026/src/main/java/frc/robot)
