# Lesson 04: Superstructure Logic

## Purpose

Move from theory into the coordination layer. Learn how `Superstructure.java` reads controller input, chooses goals, and drives multiple mechanisms together -- then practice making a small, scoped logic change.

## Prerequisites

- Finish reading [Lesson 03](../../lesson-03-superstructure-pid-ctre/README.md) and complete [Lesson 03 Task](../../tasks/lesson-03-superstructure-structure-task/README.md).
- Be able to trace a single mechanism from `Superstructure` down to `MotorTalonFX` (Lesson 03 exercise).

## Time box

1-2 hours

## Learning goals

- Read `Superstructure.periodic()` and explain how it decides what each mechanism should do.
- Understand how `SetpointGoal` and `Setpoint` carry targets for multiple mechanisms at once.
- Recognize the difference between teleop and autonomous flows.
- Make a small, scoped logic change inside a real subsystem flow.
- Use logging and state inspection to verify the change.

## The Files We Will Use

Keep these files open while reading:

- [Superstructure.java](../../971-second-robot-2026/src/main/java/frc/robot/subsystems/superstructure/Superstructure.java)
- [SetpointGoal.java](../../971-second-robot-2026/src/main/java/frc/robot/subsystems/superstructure/SetpointGoal.java)
- [Setpoint.java](../../971-second-robot-2026/src/main/java/frc/robot/subsystems/superstructure/Setpoint.java)
- [Controllers.java](../../971-second-robot-2026/src/main/java/frc/robot/subsystems/Controllers.java)
- [Toggle.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/Toggle.java)
- [Edge.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/Edge.java)

## Understanding SetpointGoal

The `SetpointGoal` enum is the bridge between high-level robot intent and individual mechanism targets. Each enum value wraps a `Setpoint` object that contains optional targets for every mechanism.

Open `SetpointGoal.java`. Each entry defines a named robot state:

```java
// SetpointGoal.java
public enum SetpointGoal {
  NEUTRAL(
      Setpoint.builder()
           .withLeftFlywheelRPS(10.0)
           .withLeftHoodInches(0.0)
           .withLeftTurretDegrees(0.0)
           .withRightFlywheelRPS(10.0)
           .withRightHoodInches(0.0)
           .withRightTurretDegrees(0.0)
           .withGroundRollersVolts(0.0)
           .withRollerFloorVolts(0.0)
           .withB2Volts(0.0)
           .withKickerVolts(0.0)
           .withGroundPivotDegrees(120.0)),

  INTAKE_PIVOT(Setpoint.builder().withGroundPivotDegrees(0.0)),

  INDEX(Setpoint.builder().withRollerFloorVolts(8.0).withB2Volts(7.0).withKickerVolts(8.0)),
   // ... more
}
```

Notice the pattern:

- **`NEUTRAL`** sets targets for almost every mechanism. It is the default "resting" state.
- **`INTAKE_PIVOT`** only sets `groundPivot` to 0 degrees. All other mechanisms are untouched.
- **`INDEX`** only sets roller voltages. Hoods and turrets are not affected.

This is intentional. Each goal only specifies what should change. The `setGoal()` method applies only the fields that are present.

Open `Setpoint.java`. It uses `Optional` fields and a builder pattern:

```java
// Setpoint.java
public class Setpoint {
  private Optional<Voltage> groundRollers = Optional.empty();
  private Optional<Angle> groundPivot = Optional.empty();
  private Optional<AngularVelocity> leftFlywheel = Optional.empty();
   // ...

  public Setpoint withGroundPivotDegrees(double degrees) {
    this.groundPivot = Optional.of(Degrees.of(degrees));
    return this;
   }
}
```

The builder pattern lets you chain calls like `Setpoint.builder().withGroundPivotDegrees(0.0)`. Any field not explicitly set remains `Optional.empty()`.

## How Superstructure Applies Goals

Open `Superstructure.java`. The method `setGoal(Setpoint setpoint)` checks each Optional and commands only the mechanisms that have values:

```java
// Superstructure.java, lines 299-340
public void setGoal(Setpoint setpoint) {
  if (setpoint.getRollerFloor().isPresent()) {
    rollerFloor.setVoltage(setpoint.getRollerFloor().get());
   }
  if (setpoint.getB2().isPresent()) {
    b2.setVoltage(setpoint.getB2().get());
   }
   // ... more mechanisms

  if (setpoint.getGroundPivot().isPresent()) {
    groundPivot.setPosition(setpoint.getGroundPivot().get());
   }
   // ... hoods, turrets, flywheels
}
```

And the convenience overload:

```java
// Superstructure.java, lines 342-344
public void setGoal(SetpointGoal setpoint) {
  setGoal(setpoint.getSetpoint());
}
```

This is the key design: **calling `setGoal` for one mechanism does not affect others.** If `INTAKE_PIVOT` only sets `groundPivot`, the flywheels keep doing whatever they were doing.

## Tracing a Teleop Path

Let us trace what happens when the driver holds the intake pivot button.

### Step 1: Controller Input

From `Controllers.java`:

```java
Controllers.java:13
public static final Toggle INTAKE_PIVOT = new Toggle(TROY.leftBumper());
```

The driver presses the left bumper on Troy's controller. `INTAKE_PIVOT.toggled()` returns `true` when the bumper is held.

### Step 2: Superstructure Reads the Input

From `Superstructure.periodic()`:

```java
Superstructure.java:176-186
if (Controllers.INTAKE_PIVOT.toggled()) {
  setGoal(SetpointGoal.INTAKE_PIVOT);

  if (Controllers.JUICE.getAsBoolean()) {
    int t = (int) (juiceTimer.get() * 100);
    if (t % 100 < 50) {
      setGoal(SetpointGoal.INTAKE_PIVOT_JUICE);
     }
   }
}
```

When the toggle is active, `setGoal(SetpointGoal.INTAKE_PIVOT)` is called. If the juice button is also held, it overrides to `INTAKE_PIVOT_JUICE` (55 degrees instead of 0).

### Step 3: SetpointGoal Carries the Target

From `SetpointGoal.java`:

```java
INTAKE_PIVOT(Setpoint.builder().withGroundPivotDegrees(0.0)),
INTAKE_PIVOT_JUICE(Setpoint.builder().withGroundPivotDegrees(55)),
```

### Step 4: setGoal() Applies Only the Ground Pivot

```java
Superstructure.java:310-312
if (setpoint.getGroundPivot().isPresent()) {
  groundPivot.setPosition(setpoint.getGroundPivot().get());
}
```

All other `Optional` fields are empty, so no other mechanism is touched.

### Step 5: Down to the Motor (Lesson 03 Recap)

```
groundPivot.setPosition(Degrees.of(0.0))
    -> MotorSubsystem.setPosition() stores goal, sets Mode.POSITION
    -> MotorSubsystem.periodic() dispatches io.setPosition(goalPosition)
    -> MotorTalonFX.setPosition() sends DynamicMotionMagicVoltage to TalonFX
```

### The Full Chain

```
Driver holds left bumper
    -> Controllers.INTAKE_PIVOT.toggled() == true
    -> Superstructure.setGoal(SetpointGoal.INTAKE_PIVOT)
    -> SetpointGoal.INTAKE_PIVOT.getSetpoint() returns Setpoint with groundPivot = 0 degrees
    -> Superstructure.setGoal(Setpoint) sees groundPivot.isPresent()
    -> groundPivot.setPosition(Degrees.of(0.0))
    -> MotorSubsystem stores goal, controlMode = POSITION
    -> MotorSubsystem.periodic() -> io.setPosition() -> TalonFX Motion Magic
```

## Another Path: Shooting

Let us trace a shooting request. This path is more complex because it involves `ShooterHandler`.

### Step 1: Controller Input

```java
Controllers.java:19-27
public static final Trigger SHOOT_REDUNDANCY = TROY.axisGreaterThan(3, 0.9);
public static final Trigger SHOOT = ANDRE.axisGreaterThan(3, 0.9);
public static final Trigger SHOOTING = SHOOT_REDUNDANCY.or(SHOOT);
```

### Step 2: Superstructure Evaluates the Shot

```java
Superstructure.java:104-108
boolean wantsShot =
    Controllers.LEFT_SHUTTLE.getAsBoolean()
         || Controllers.RIGHT_SHUTTLE.getAsBoolean()
         || Controllers.SHOOT.getAsBoolean()
         || Controllers.SHOOT_REDUNDANCY.getAsBoolean();
```

### Step 3: ShooterHandler Gets the Goal

```java
Superstructure.java:128-131 (TARGETING case)
shooterHandlerLeft.setShooterGoal(ShooterHandler.Goal.ACTIVE);
shooterHandlerRight.setShooterGoal(ShooterHandler.Goal.ACTIVE);
```

### Step 4: ShooterHandler Commands Individual Mechanisms

```java
Superstructure.java:195-208
if (wantsShot && DriverStation.isEnabled()) {
  shooterHandlerLeft.getHoodAngle().ifPresent(hoodLeft::setPosition);
  shooterHandlerLeft.getFlywheelSpeed()
       .ifPresent(speed -> flywheelLeft.setVelocity(speed.plus(offset)));
  // ... same for right side
}
```

Notice this path **bypasses** `setGoal(SetpointGoal)`. The ShooterHandler computes targets dynamically based on object state and alliance color, then directly calls `setPosition` and `setVelocity` on the individual mechanisms.

This is an important distinction:

- **SetpointGoal path**: static, predefined targets. Used for intake, indexing, outtake, neutral.
- **ShooterHandler path**: dynamic, computed targets. Used for shooting and shuttling.

Both paths converge at the mechanism level. `hoodLeft.setPosition(...)` goes through the same `MotorSubsystem` -> `MotorIO` -> `MotorTalonFX` chain regardless of who called it.

## The Indexer Logic

The indexer (rollers that feed a note from the intake to the shooter) only runs when two conditions are met:

```java
Superstructure.java:221-233
boolean indexing =
    wantsShot
         && ((shooterHandlerLeft.getShooterState() == ShooterHandler.State.FIRING
                 || shooterHandlerRight.getShooterState() == ShooterHandler.State.FIRING)
               || shooterGoal == ShooterGoal.MANUAL);

if (Controllers.OUTTAKE.getAsBoolean()) {
  setGoal(SetpointGoal.OUTTAKE);
} else if (Controllers.UNJAM.getAsBoolean()) {
  setGoal(SetpointGoal.UNJAM);
} else if (indexing) {
  setGoal(SetpointGoal.INDEX);
}
```

So the indexer runs when:
1. The driver wants to shoot (`wantsShot`), AND
2. At least one shooter is in the `FIRING` state -- OR the driver is in manual mode.

But it is **preempted** by outtake or unjam if those buttons are held.

This is the kind of integration logic you will be asked to modify. Understanding the precedence order -- outtake > unjam > indexing -- matters before you add new conditions.

## Teleop vs Autonomous

`Superstructure.periodic()` has two main branches:

```java
if (DriverStation.isTeleop()) {
  // ... driver-controlled logic
} else if (DriverStation.isAutonomous()) {
  // ... auto-controlled logic
}
```

The teleop branch reads controller inputs and reacts to buttons. The autonomous branch executes pre-planned sequences. Both use the same `setGoal()` and mechanism methods -- the difference is what triggers them.

In autonomous, note the `shootAuto()` command:

```java
Superstructure.java:387-403
public Command shootAuto() {
  return Commands.waitUntil(() -> !drivetrain.isRobotOnBump())
       .andThen(Commands.runOnce(() -> {
         // set target state based on alliance
         shooterHandlerLeft.setTargetState(curTarget);
         shooterHandlerRight.setTargetState(curTarget);
         shooterHandlerLeft.setShooterGoal(ShooterHandler.Goal.ACTIVE);
         shooterHandlerRight.setShooterGoal(ShooterHandler.Goal.ACTIVE);
       }));
}
```

This waits for the robot to settle, then activates both shooters. The autonomous `periodic()` branch checks `shooterHandlerLeft.getShooterGoal() == Goal.ACTIVE` and commands the mechanisms each loop.

## How These Pieces Fit Together

```
Controllers.java                    -- defines button/trigger bindings
SetpointGoal.java                   -- named robot states (NEUTRAL, INDEX, INTAKE_PIVOT, ...)
Setpoint.java                       -- carrier for per-mechanism target values
Superstructure.java                 -- reads inputs, chooses goals, commands mechanisms
    |
    +-- setGoal(SetpointGoal)       -- static predefined targets
    +-- shooterHandler.*            -- dynamic computed targets
    |
    +-- groundPivot.setPosition()   -- individual mechanism calls
    +-- flywheelLeft.setVelocity()
    +-- hoodLeft.setPosition()
        |
        +-- MotorSubsystem          -- stores goal, selects mode
            |
            +-- MotorIO             -- abstraction boundary
                |
                +-- MotorTalonFX    -- real hardware
                +-- MotorSim        -- simulation
```

## Good First Tasks

This is the first lesson where you should feel some ambiguity. That is intentional. Real team tasks are not "write this from scratch." They are "read the current behavior, understand where the right place is, and add a simple piece of logic without damaging the rest."

**Good first tasks:**

- Add thresholding around a state transition so an action only happens when a mechanism is ready.
- Add a log output for a specific state or decision point.
- Gate a behavior behind a clearer condition using existing controller or subsystem state.
- Refactor repeated condition logic into a small helper without changing behavior.
- Add a button-triggered behavior that follows an existing pattern.

**Bad first tasks:**

- Rewrites of the superstructure state model.
- Broad architecture changes.
- Tuning by guesswork.
- Large unreviewed additions copied from GPT.

## Debugging Your Change

When you make a logic change in `Superstructure` or nearby:

1. **Before editing**, write down the current behavior and the desired behavior.
2. **Choose the smallest correct edit point.** Do not restructure surrounding code.
3. **Test in sim** when possible. Check that the code path runs and the state changes.
4. **Verify with logs.** Look for the goal you expect, the control mode you expect, and the measured response.

If the code compiles but does nothing:
- Check whether the code path is actually reached.
- Check whether your condition is too restrictive.
- Check whether another `setGoal` call later in `periodic()` overwrites your change.

If behavior is wrong:
- Verify you changed the correct layer. A wrong goal is a `Superstructure` problem. A wrong motor output is a `MotorTalonFX` or tuning problem.
- Reduce the logic to the exact condition that should be `true` or `false`.

## Exercise

1. Open `Superstructure.periodic()`. Find every call to `setGoal()` in the teleop branch. For each call, write down:
    - What triggers it (which controller input or condition).
    - Which mechanisms the corresponding `SetpointGoal` affects.
    - Whether it can be overridden by a later call.

2. Trace the precedence of the indexer logic. What happens if the driver holds both `OUTTAKE` and wants to shoot?

3. Find the `ShooterGoal` enum inside `Superstructure.java`. Trace how `shooterGoal` switches between `MANUAL` and `TARGETING`, and what each mode does differently.

4. Pick one `SetpointGoal` value. List exactly which mechanisms receive a target and which do not. Explain what happens to the mechanisms that are not mentioned.

## Checkoff

- Student can explain the teleop flow in `Superstructure.periodic()`.
- Student can trace one button press from `Controllers.java` to a mechanism command.
- Student can explain the difference between the `SetpointGoal` path and the `ShooterHandler` path.
- Student can identify which `setGoal` calls can override each other and in what order.
- Student completes one scoped logic task (see below).

## Before Next Lesson

- Review drivetrain-related classes so the names and structure are familiar.
- Be ready to compare mechanism logic with drivetrain architecture.

## Task for this lesson

Follow [Lesson 04 Task: Superstructure Logic Change](../tasks/lesson-04-superstructure-logic-task/README.md).
