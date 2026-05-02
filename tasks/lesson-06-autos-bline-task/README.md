# Lesson 06 Task: Auto Routine Investigation or Variant

# IMPORTANT: DO NOT ATTEMPT THIS TASK WITHOUT READING [LESSON 06](../../lesson-06-autos-bline-xrp/README.md)

This task includes a starter project that models the important layers of the 2026 auto stack: chooser selection, path asset names, start pose caching, and building a command sequence.

It is simpler than the real B-Line stack, but it teaches the same reading and reasoning flow.

## Starter Project Focus

Files you should care about first:

- `src/main/java/frc/robot/subsystems/Autos.java`
- `src/main/java/frc/robot/subsystems/PathSegment.java`
- `src/main/java/frc/robot/RobotContainer.java`
- `src/main/deploy/autos/paths/`

## Your Mission

1. Read how autos are defined and cached.
2. In `Autos.java`, add a new routine using the provided path segment names.
3. Complete the TODO in `getAutonomousStartPose()` so the selected routine's preview start pose is shown.
4. Run simulation and confirm:
   - the chooser appears
   - the selected routine name updates
   - the start pose preview updates

## Test Your Code

Before asking for checkoff:

- run simulation
- choose different autos on SmartDashboard
- confirm the preview pose changes
- run autonomous and confirm the routine prints its segment order

## Why This Matters

- Real auto work depends on separating chooser logic, asset naming, path data, and command building.
- Students need to understand those layers before touching the full robot auto stack confidently.

## Wrap-Up

Once your code works:

1. Create a branch.
2. Commit clearly.
3. Ask for review.
4. Be ready to explain what part of the project is mimicking the real `Autos.java` flow from `971-second-robot-2026`.
