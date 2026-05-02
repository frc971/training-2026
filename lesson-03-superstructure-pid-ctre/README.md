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
- Understand how our 2026 subsystem abstraction differs from the simpler 2025 `Wrist/WristIO/WristIOSim/WristIOTalonFX` example.

## In our codebase

- Main superstructure coordination: [971-second-robot-2026/src/main/java/frc/robot/subsystems/superstructure/Superstructure.java](../../971-second-robot-2026/src/main/java/frc/robot/subsystems/superstructure/Superstructure.java)
- Lower-level superstructure library: [971-second-robot-2026/src/main/java/frc/robot/lib/superstructure](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure)
- Example motor abstraction: [971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorSubsystem.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorSubsystem.java)
- Example CTRE-backed implementation: [971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorTalonFX.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorTalonFX.java)
- Simulation support: [971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorSim.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorSim.java)

## What is a subsystem?

Think of each subsystem as a self-contained box that manages one robot mechanism or one tightly related group of mechanisms.

Subsystems exist to:

- keep code organized by robot function,
- make the codebase easier to read,
- isolate responsibilities,
- make simulation and testing more realistic,
- reduce the amount of hardware-specific logic spread everywhere.

In this codebase, the word "superstructure" means the coordinated collection of mechanisms that intake, feed, pivot, aim, and shoot. It is not one motor. It is a higher-level controller for many mechanisms working together.

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

## PID control and tuning

Many robot mechanisms need to move to a target position or speed with repeatability. PID is one of the main tools used to do that.

### What is PID?

PID stands for:

- `P` proportional: more current error usually means more correction
- `I` integral: helps clean up small long-term errors
- `D` derivative: reacts to how fast the error is changing

The tunable gains `kP`, `kI`, and `kD` decide how strongly the controller reacts through each of those paths.

### Why tune PID?

- If gains are too low, the mechanism may feel slow or never settle where you want.
- If gains are too high, the mechanism may overshoot, oscillate, or become unstable.
- If the controller structure is fine but the model is wrong, tuning alone may not fix the problem.

### How new members should think about tuning

Do not start from "what number should I change?"

Start from:

1. What is the target?
2. What is the measured feedback?
3. What command is being sent?
4. What physical behavior is actually happening?
5. Is this a logic problem, a sensor problem, a config problem, or a tuning problem?

After that, connect it to CTRE and our wrappers:

- CTRE provides motor and swerve libraries.
- We often interact through our own wrapper classes instead of raw API calls everywhere.
- That wrapper layer is where new members should look to understand repeated patterns.

## CTRE TalonFX in team terms

The TalonFX is the motor controller we often use for mechanisms and drivetrain modules. It includes onboard control features and integrates with CTRE's software stack.

Practical things to know:

- it can control output directly,
- it reports sensor values such as position and velocity,
- it supports built-in closed-loop control,
- it can enforce current limits and motor behavior settings,
- it can run motion-profile-like position control modes such as Motion Magic.

Do not memorize old API names from older seasons and assume they still match the current Phoenix 6 style. Learn the concept first, then confirm the current API in the code.

## Motion Magic

Motion Magic is CTRE's motion-profile-based position control mode. In practice, that means:

- you give it a target position,
- you configure motion constraints such as cruise velocity and acceleration,
- the controller handles the trajectory shape and closed-loop effort internally.

In this repo, that behavior is wrapped instead of being sprayed across every subsystem.

## Code overview: the 2026 pattern

The 2025 wiki example you pasted used a four-file mental model like:

- `Wrist.java`
- `WristIO.java`
- `WristIOTalonFX.java`
- `WristIOSim.java`

That is still a good teaching pattern, but the 2026 second robot has pushed more of that abstraction into shared reusable classes.

The same idea now looks more like this:

- `Superstructure.java`
  - higher-level coordination of many mechanisms
- `MotorSubsystem.java`
  - reusable subsystem logic for a motor-driven mechanism
- `MotorIO.java`
  - the hardware abstraction contract
- `MotorTalonFX.java`
  - real hardware implementation
- `MotorSim.java`
  - simulation implementation
- mechanism-specific classes such as `HoodLeft`, `GroundPivot`, or `TurretLeft`
  - configure or extend the generic behavior for one real mechanism

## What each file is responsible for

### `Superstructure.java`

This is the coordinator. It owns many mechanisms and decides how they should work together based on driver state, mode, and current goals.

Think of it as the high-level brain for mechanism coordination, not the place where every low-level motor detail should live.

### `MotorSubsystem.java`

This is the reusable mechanism logic layer. It stores goals such as:

- target voltage,
- target velocity,
- target position,
- control mode.

It decides which abstract action should happen each cycle and then calls the underlying IO implementation.

### `MotorIO.java`

This is the abstraction boundary. It defines what hardware-specific implementations must provide and what shared state gets exposed upward.

This plays the same conceptual role as `WristIO` in the 2025 lesson, but for a more reusable generalized mechanism pattern.

### `MotorTalonFX.java`

This is the real hardware implementation using CTRE TalonFX and optional CANcoder feedback.

It:

- applies the TalonFX config,
- owns the real motor object,
- reads status signals,
- sends voltage, velocity, and position requests,
- updates measured state fields.

### `MotorSim.java`

This is the simulation implementation.

It does not pretend to be perfect physics. It exists so mechanism logic can be exercised without the real robot.

### Mechanism-specific classes

Files like `HoodLeft.java` or `GroundPivot.java` provide mechanism-specific configuration:

- CAN IDs,
- bus names,
- ratios,
- gains,
- inversion,
- current limits,
- feedforward behavior.

This is where the generic subsystem pattern gets specialized for a real robot part.

## How these pieces work together

At a high level:

`Superstructure`  
uses mechanism classes such as `HoodLeft`, `TurretRight`, `GroundPivot`

Those mechanism classes  
extend shared logic like `MotorSubsystem`, `AngularSubsystem`, or `LinearSubsystem`

Those shared logic classes  
talk through `MotorIO`

`MotorIO`  
is implemented by either `MotorTalonFX` for real hardware or `MotorSim` for simulation

This structure exists for the same reasons the 2025 wiki taught the simpler IO pattern:

- cleaner logic,
- easier simulation,
- better testing,
- easier hardware swaps,
- less duplicated code.

## What to emphasize

- The point is not memorizing every class.
- The point is learning to follow the chain from high-level goal to motor command.
- When a mechanism misbehaves, new members should inspect state, goal, and sensor feedback before touching gains.

## Suggested walkthrough in code

1. Open `Superstructure.java` and find where a mechanism goal is chosen.
2. Open one mechanism class such as `HoodLeft.java`.
3. Identify what is mechanism-specific there.
4. Open `MotorSubsystem.java` and find where control mode determines the action.
5. Open `MotorIO.java` and inspect the abstract methods and logged state.
6. Open `MotorTalonFX.java` and identify where real hardware requests are sent.
7. Open `MotorSim.java` and compare what simulation does instead.

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
- Compare the 2025 `Wrist/WristIO/WristIOSim/WristIOTalonFX` teaching pattern with the 2026 shared abstraction pattern and explain:
  - what stayed conceptually the same,
  - what became more reusable,
  - what became harder to understand at first.

## Debugging prompts

- If the mechanism is wrong, do not start by changing constants.
- Check whether the high-level goal is wrong, the sensor reading is wrong, or the actuator command is wrong.
- Ask whether the mechanism is in the expected mode or state.

## Deliverable / checkoff

- Student can explain what the superstructure is in this repo.
- Student can describe PID in practical debugging terms.
- Student can trace one mechanism from `Superstructure` into lower-level motor control code.
- Student can explain the purpose of `MotorIO`, `MotorTalonFX`, and `MotorSim` without treating them as interchangeable files.

## Before next lesson

- Be ready to implement a small logic task in existing superstructure code.
- Re-read state-related code and note places where thresholds, toggles, and conditions are used.

## Task for this lesson

Follow [Lesson 03 Task: Superstructure Structure Trace](../tasks/lesson-03-superstructure-structure-task/README.md).
