# Lesson 02 Task: Codebase + Simulator Investigation

# IMPORTANT: DO NOT ATTEMPT THIS TASK WITHOUT READING [LESSON 02](../../lesson-02-codebase-and-sim/README.md)

This task now includes a small WPILib starter project, similar to the old 2025 mini repos.

The point is not to build a full robot. The point is to practice:

- navigating a normal WPILib project layout,
- tracing control flow,
- running simulation,
- and using dashboard-visible state instead of guessing.

## Starter Project Focus

This mini repo contains a simple `Pivot` subsystem.

Files you should care about first:

- `src/main/java/frc/robot/RobotContainer.java`
- `src/main/java/frc/robot/subsystems/Pivot.java`

## Your Mission

1. Read the project structure and explain what the major files do.
2. In `RobotContainer.java`, bind:
   - joystick button 1 to deploy the pivot to `35.0` degrees
   - joystick button 2 to stow the pivot to `0.0` degrees
3. In `Pivot.java`, publish:
   - current angle
   - target angle
   - at-target status
   to SmartDashboard in `periodic()`.
4. Run simulation and verify the pivot moves toward the target angle.

## Test Your Code

Before asking for checkoff:

- start sim with `./gradlew simulateJava`
- switch to teleop
- press button 1 and confirm the target changes to `35.0`
- press button 2 and confirm the target changes to `0.0`
- verify the dashboard values move and settle

## Why This Matters

- This is a smaller version of the same workflow you will use in the real repo.
- You need to be able to read project structure and inspect state before jumping into bigger subsystem code.
- The simulator is useful because it lets you verify behavior before robot time exists.

## Wrap-Up

Once your code works:

1. Create a branch for your work.
2. Commit with a clear message.
3. Ask a training lead or student to review it.
4. Be ready to explain the code path you touched.
