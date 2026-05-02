# Lesson 05: Drivetrain Introduction

## Purpose

Introduce the drivetrain architecture to the depth needed for new members to understand how drive code is structured, how CTRE swerve fits in, and where small future tasks may live.

## Prerequisites

- Weeks 2-4.
- Basic understanding of CTRE concepts from Week 3.

## Time box

1-2 hours

## Learning goals

- Understand the role of the drivetrain subsystem in this repo.
- Recognize the difference between team code, generated constants, and vendor code expectations.
- Follow default drive behavior from controller input to drivetrain request.
- Understand where sim support for drivetrain lives.

## In our codebase

- Drivetrain subsystem: [971-second-robot-2026/src/main/java/frc/robot/subsystems/CommandSwerveDrivetrain.java](../../971-second-robot-2026/src/main/java/frc/robot/subsystems/CommandSwerveDrivetrain.java)
- Tuner-generated constants: [971-second-robot-2026/src/main/java/frc/robot/generated/TunerConstants.java](../../971-second-robot-2026/src/main/java/frc/robot/generated/TunerConstants.java)
- Robot wiring path: [971-second-robot-2026/src/main/java/frc/robot/RobotContainer.java](../../971-second-robot-2026/src/main/java/frc/robot/RobotContainer.java)
- Simulation support: [971-second-robot-2026/src/main/java/frc/robot/lib/simulation/MapleSimSwerveDrivetrain.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/simulation/MapleSimSwerveDrivetrain.java)

## Lesson content

Start in `RobotContainer.java` and trace how joystick values become drivetrain requests. Show the deadbands, scaling, slew rate limiting, and the different drive modes.

Then move into `CommandSwerveDrivetrain.java` and explain the important idea: this class sits between our robot code and CTRE swerve functionality. It handles:

- applying requests,
- sysid routines,
- perspective handling,
- simulation thread startup,
- and helper behavior around the vendor drivetrain.

This is a good place to remind students that not all code in a robot repo is the same kind of code:

- some is vendor-facing glue,
- some is project-specific behavior,
- some is generated and should be treated carefully,
- some is simulation support.

## Commands note

If command-based structure needs to be introduced here to support the auto lesson, keep it concrete. Focus on what a default command is, what triggers do, and how a subsystem receives requests. Do not turn this into an abstract command framework lecture.

## Required mech/elec callout

Drivetrain bugs are often not only code bugs. Students should know that module orientation, inversion, CAN IDs, and hardware state can all change observed behavior. Software debugging has to stay connected to real robot context.

## Exercise

- In `RobotContainer.java`, trace one controller input from stick read to `SwerveRequest`.
- In `CommandSwerveDrivetrain.java`, identify:
  - where sim is started,
  - where sysid routines are declared,
  - where requests are applied.
- Explain the purpose of `TunerConstants`.
- If the drivetrain rewrite changes later, compare the lesson notes against the new structure and note what moved.

## Debugging prompts

- If drive feel is wrong, inspect deadbands, scaling, and slew limiters before assuming the motors are wrong.
- If sim and real differ, ask what the sim layer is approximating and what it is not.
- If a student wants to edit generated constants casually, stop and ask whether that file is actually the right ownership point.

## Deliverable / checkoff

- Student can explain how joystick input reaches the drivetrain.
- Student can describe the role of `CommandSwerveDrivetrain` and `TunerConstants`.
- Student can point to where sim support for drivetrain behavior lives.

## Before next lesson

- Be ready to think about autos as a combination of paths, commands, and field-aware behavior.
