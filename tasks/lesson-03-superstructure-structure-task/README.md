# Lesson 03 Task: Superstructure Structure Trace

# IMPORTANT: DO NOT ATTEMPT THIS TASK WITHOUT READING [LESSON 03](../../lesson-03-superstructure-pid-ctre/README.md)

This task includes a starter project built to teach the 2026-style shared abstraction pattern, not just the older one-subsystem-one-IO-interface pattern.

## Starter Project Focus

This mini repo uses a simplified reusable motor abstraction:

- `src/main/java/frc/robot/lib/motor/MotorIO.java`
- `src/main/java/frc/robot/lib/motor/MotorSubsystem.java`
- `src/main/java/frc/robot/subsystems/pivot/Pivot.java`
- `src/main/java/frc/robot/subsystems/pivot/PivotIOSim.java`
- `src/main/java/frc/robot/subsystems/pivot/PivotIOTalonFX.java`
- `src/main/java/frc/robot/RobotContainer.java`

It is intentionally smaller than the real 2026 second robot, but the layers line up with the same ideas.

## Your Mission

1. Read the project and explain what each layer does.
2. In `RobotContainer.java`, instantiate the `Pivot` with:
   - `PivotIOTalonFX` on real hardware
   - `PivotIOSim` in simulation
3. Bind:
   - button 1 to move the pivot toward deployed
   - button 2 to move the pivot toward stowed
   - releasing either button should stop the pivot
4. Complete the TODOs in:
   - `PivotIOSim.java`
   - `PivotIOTalonFX.java`

## Test Your Code

Before asking for checkoff:

- run simulation
- hold button 1 and confirm the pivot angle increases
- hold button 2 and confirm the pivot angle decreases
- explain where simulated state is updated and where real hardware state would come from

## Why This Matters

- This is the same layering idea you need for the 2026 superstructure codebase.
- If you do not understand which file owns logic versus IO versus configuration, you will edit the wrong place in the real repo.

## Wrap-Up

Once your code works:

1. Create a branch.
2. Commit clearly.
3. Ask for review.
4. Be ready to compare this starter structure against the real `MotorIO` and `MotorSubsystem` pattern in `971-second-robot-2026`.
