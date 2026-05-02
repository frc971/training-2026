# Lesson 05 Task: Drivetrain Reading + Explanation

# IMPORTANT: DO NOT ATTEMPT THIS TASK WITHOUT READING [LESSON 05](../../lesson-05-drivetrain-intro/README.md)

This task includes a starter project that isolates the driver-input processing part of drivetrain code. It is intentionally smaller than a real CTRE swerve project, but it teaches the same reasoning around deadband, exponential shaping, and scaling.

## Starter Project Focus

Files you should care about first:

- `src/main/java/frc/robot/RobotContainer.java`
- `src/main/java/frc/robot/subsystems/Drivebase.java`
- `src/main/java/frc/robot/util/DriveInputHelper.java`

## Your Mission

1. Read the input path from joystick to drive request.
2. In `DriveInputHelper.java`, complete the TODOs to:
   - apply deadband
   - apply exponential shaping
   - apply normal vs slow-mode scaling
3. Run simulation and watch the published drive request values change as you move the joystick.

## Test Your Code

Before asking for checkoff:

- run simulation
- move the joystick slightly and confirm deadband prevents tiny commands
- move it farther and confirm values grow nonlinearly
- hold button 1 and confirm slow mode reduces the output

## Why This Matters

- A lot of drivetrain feel comes from input shaping before the drivetrain hardware sees anything.
- New members should be able to reason about this layer even if they are not writing full swerve code yet.

## Wrap-Up

Once your code works:

1. Create a branch.
2. Commit clearly.
3. Ask for review.
4. Be ready to explain which part of the path is helper logic and which part is subsystem behavior.
