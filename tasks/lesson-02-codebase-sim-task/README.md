# Lesson 02 Task: Codebase + Simulator Investigation

# IMPORTANT: DO NOT ATTEMPT THIS TASK WITHOUT READING [LESSON 02](https://github.com/frc971/training-2026/tree/main/lesson-02-codebase-and-sim)

This task now includes a small WPILib starter project, similar to the old 2025 mini repos.

The point is not to build a full robot. The point is to practice:

- navigating a normal WPILib project layout,
- tracing control flow,
- running simulation,
- using dashboard-visible state instead of guessing,
- and finding bugs by observing behavior (not staring at code).

## Starter Project Focus

This mini repo contains a simple `Pivot` subsystem.

Files you should care about first:

- `src/main/java/frc/robot/RobotContainer.java`
- `src/main/java/frc/robot/subsystems/Pivot.java`
- `src/main/java/frc/robot/subsystems/PivotHelper.java`

## Your Mission

You will do a simple task similar to what was shown at the end of the [FRC 971 - WPILib Simulator and AdvantageScope Tutorial](https://www.youtube.com/watch?v=7fhh3e_5KcM).

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

## Bug Hunt

1. **Run the sim** and observe the behavior. Press button 1 (deploy to 35°), wait
   for the pivot to settle, then watch what happens.
2. **Use SmartDashboard / AdvantageScope** to inspect the values. Is the pivot
   holding position at the target? Is `AtTarget` staying `true` once it arrives?
3. **Trace the bug** from the symptom back to the root cause in `PivotHelper.java`.
   Read `computeStep()` carefully!
4. **Fix the bug** and re-run sim to confirm the pivot behaves correctly.

## Why This Matters

- Always test code in sim before!
- This is a smaller version of the same workflow you will use in the real repo.
- You need to be able to read project structure and inspect state before jumping into bigger subsystem code.
- The simulator is useful because it lets you verify behavior before robot time exists.
- **Observing behavior > reading code.** A method can look correct and still be wrong. You are learning how to use **simulation to catch those bugs.**

## Wrap-Up

Once your code works:

1. Create a branch for your work.
2. Commit with a clear message.
3. **Make a PR and ask a training lead or instructor to review it.**
4. Be ready to explain the code path you touched and the bug you found, and we'll check you off after.
