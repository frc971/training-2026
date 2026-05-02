# Lesson 03: Superstructure / PID / CTRE Theory

## Purpose

Introduce the control concepts behind the superstructure so new members can read mechanism code without treating it like magic.

## Prerequisites

- Week 2 codebase overview.
- Basic comfort reading classes and methods in Java.

## Time box

1-2 hours

## Learning goals

- Understand what a superstructure is in our codebase.
- Understand the purpose of PID at a practical level.
- Recognize where CTRE abstractions and wrappers live in our code.
- Be able to identify where to inspect tuning-related behavior before editing anything.

## In our codebase

- Main superstructure coordination: [971-second-robot-2026/src/main/java/frc/robot/subsystems/superstructure/Superstructure.java](../../971-second-robot-2026/src/main/java/frc/robot/subsystems/superstructure/Superstructure.java)
- Lower-level superstructure library: [971-second-robot-2026/src/main/java/frc/robot/lib/superstructure](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure)
- Example motor abstraction: [971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorSubsystem.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorSubsystem.java)
- Example CTRE-backed implementation: [971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorTalonFX.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorTalonFX.java)
- Simulation support: [971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorSim.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorSim.java)

## Lesson content

Start with the word "superstructure" in team terms: this is the coordinated set of mechanisms that score, intake, feed, pivot, or aim. In our repo, `Superstructure.java` is the central coordinator that owns many mechanism objects and decides how they work together.

Then introduce PID in plain language:

- proportional reacts to current error,
- integral reacts to accumulated error,
- derivative reacts to rate of change.

Do not teach PID as math first. Teach it as a debugging tool first:

- What is the target?
- What is the measured state?
- What output is being applied?
- Why might it overshoot, oscillate, lag, or stall?

After that, connect it to CTRE and our wrappers:

- CTRE provides motor and swerve libraries.
- We often interact through our own wrapper classes instead of raw API calls everywhere.
- That wrapper layer is where new members should look to understand repeated patterns.

## What to emphasize

- The point is not memorizing every class.
- The point is learning to follow the chain from high-level goal to motor command.
- When a mechanism misbehaves, new members should inspect state, goal, and sensor feedback before touching gains.

## Theory vs hands-on

This lesson can stay mostly theoretical if no stable tuning demo is ready. If a safe demo exists, show one real example of changing a target and watching measured response. If a CTRE or other simulation demo exists by lesson time, use it; if not, do not promise it.

## Required mech/elec callout

This is where basic CAN understanding matters. Students should know that a TalonFX or sensor on CAN is a real device with IDs, wiring, and failure modes. A software bug and a hardware/config issue can look similar at first.

## Exercise

- Open `Superstructure.java` and identify:
  - which mechanisms it owns,
  - which methods appear to coordinate behavior,
  - where teleop logic changes setpoints or goals.
- Pick one mechanism path and trace it downward into the `lib/superstructure` layer.
- For one position- or velocity-controlled mechanism, answer:
  - what is the goal,
  - what is the measurement,
  - where would you look for control output logic,
  - what would you log first if it behaved badly.

## Debugging prompts

- If the mechanism is wrong, do not start by changing constants.
- Check whether the high-level goal is wrong, the sensor reading is wrong, or the actuator command is wrong.
- Ask whether the mechanism is in the expected mode or state.

## Deliverable / checkoff

- Student can explain what the superstructure is in this repo.
- Student can describe PID in practical debugging terms.
- Student can trace one mechanism from `Superstructure` into lower-level motor control code.

## Before next lesson

- Be ready to implement a small logic task in existing superstructure code.
- Re-read state-related code and note places where thresholds, toggles, and conditions are used.
