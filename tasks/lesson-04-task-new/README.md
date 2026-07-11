# Lesson 04 Task: Staged Superstructure Logic

# IMPORTANT: DO NOT ATTEMPT THIS TASK WITHOUT READING [LESSON 04](../lesson-04-superstructure-logic-task/README.md)

This task includes a starter project built around a simple arm, end effector, and elevator flow. The goal is to practice threshold-based integration logic in `Pathing`, while `Superstructure` owns the operator input that toggles the pathing goal.

## Starter Project Focus

Files you should care about first:

- `src/main/java/frc/robot/RobotContainer.java`
- `src/main/java/frc/robot/subsystems/Arm.java`
- `src/main/java/frc/robot/subsystems/EndEffector.java`
- `src/main/java/frc/robot/subsystems/Elevator.java`
- `src/main/java/frc/robot/subsystems/Superstructure.java`
- `src/main/java/frc/robot/subsystems/Pathing.java`
- `src/main/java/frc/robot/lib/superstructure/Toggle.java`

## Your Mission

1. Read the current control flow.
2. In `Superstructure.java`, implement the TODO keybind logic using the provided `Toggle` so button 1 toggles the `Pathing` goal between `ACTIVE` and `NOT_ACTIVE`.
3. In `Pathing.java`, implement `periodic()` and the three TODO helper methods:
   - `periodic()`
   - `isArmReadyForEndEffector()`
   - `isEndEffectorReadyForElevator()`
   - `isReadyToScore()`
4. Press button 1 once to request the high scoring sequence, then press it again to turn the sequence off.
5. Verify in simulation that:
   - the arm moves to `62.0` degrees first
   - the end effector stays at `0.0` volts until the arm is within `3.0` degrees
   - the elevator stays at `0.0` meters until the end effector is within `0.5` volts of `6.0` volts
   - `Pathing/ReadyToScore` only becomes true once all three mechanisms are at their targets
   - pressing button 1 again stops the sequence

## Test Your Code

Before asking for checkoff:

- run simulation
- press button 1 once
- watch the AdvantageKit values in NetworkTables or AdvantageScope
- confirm `Superstructure/PathingToggle` changes once per button press
- confirm `Pathing/Goal` toggles between `ACTIVE` and `NOT_ACTIVE` once per button press
- confirm `EndEffector/TargetVolts` stays at `0.0` until the arm reaches position
- confirm `Elevator/TargetMeters` stays at `0.0` until the end effector reaches voltage
- confirm `Pathing/ReadyToScore` only turns true after the elevator reaches height

## Why This Matters

- This is the same kind of thresholding or integration logic that shows up in real superstructure work.
- `Superstructure` owns the operator input, while `Pathing` owns the goal and the sequence for reaching that goal.
- The hard part is usually not writing a lot of code. It is choosing the correct layer and proving the behavior changed for the right reason.

## Wrap-Up

Once your code works:

1. Create a branch.
2. Commit clearly.
3. Ask for review.
4. Be ready to explain why the sequencing logic belongs in `Pathing` instead of directly inside `Arm`, `EndEffector`, or `Elevator`.
