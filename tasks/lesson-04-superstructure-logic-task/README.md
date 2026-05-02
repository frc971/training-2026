# Lesson 04 Task: Superstructure Logic Change

# IMPORTANT: DO NOT ATTEMPT THIS TASK WITHOUT READING [LESSON 04](../../lesson-04-superstructure-logic-task/README.md)

This task now includes a starter project built around a simple shooter + indexer flow. The goal is to practice the kind of threshold-based integration logic newer members often get first.

## Starter Project Focus

Files you should care about first:

- `src/main/java/frc/robot/RobotContainer.java`
- `src/main/java/frc/robot/subsystems/Shooter.java`
- `src/main/java/frc/robot/subsystems/Indexer.java`
- `src/main/java/frc/robot/subsystems/SuperstructureCoordinator.java`

## Your Mission

1. Read the current control flow.
2. In `SuperstructureCoordinator.java`, implement `isReadyToFeed()` so the indexer only feeds once the shooter is at least 90% of the target RPM.
3. Hold button 1 to request the shoot sequence.
4. Verify in simulation that:
   - the shooter spins up first
   - the indexer stays off until the shooter is ready
   - releasing the button stops everything

## Test Your Code

Before asking for checkoff:

- run simulation
- hold button 1
- watch the SmartDashboard values
- confirm `Indexer/Feeding` only turns true after the shooter reaches speed

## Why This Matters

- This is the same kind of thresholding or integration logic that shows up in real superstructure work.
- The hard part is usually not writing a lot of code. It is choosing the correct layer and proving the behavior changed for the right reason.

## Wrap-Up

Once your code works:

1. Create a branch.
2. Commit clearly.
3. Ask for review.
4. Be ready to explain why the logic belongs in the coordinator instead of directly inside `Indexer`.
