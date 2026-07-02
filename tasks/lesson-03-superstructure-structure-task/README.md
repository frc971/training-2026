# Lesson 03 Task: Building a Turret Subsystem

# IMPORTANT: DO NOT ATTEMPT THIS TASK WITHOUT READING [LESSON 03](https://github.com/frc971/training-2026/tree/main/lesson-03-superstructure-pid-ctre)

In the real robot codebase, most mechanisms are built using a reusable motor framework instead of directly controlling TalonFXs inside every subsystem.

The framework looks roughly like this:

```text
MotorConfig
    ↓
MotorIO
   /    \
MotorSim MotorTalonFX
    ↓       ↓
MotorSubsystem
    ↓
AngularSubsystem and LinearSubsystem
    ↓
Your Subsystem
```

The goal of this task is to become familiar with that framework by creating a turret subsystem.

---

## What You Will Learn

By completing this task, you will practice:

* Reading a real robot subsystem structure
* Understanding the purpose of `MotorConfig`
* Configuring a motor using the superstructure framework
* Overriding subsystem behavior
* Implementing software limits
* Testing behavior in simulation

---

## Starter Files

You should primarily work in:

```text
src/main/java/frc/robot/subsystems/superstructure/Turret.java
```

The starter file contains several TODOs.

---

## Background

A turret rotates around a vertical axis.

Unlike some mechanisms, a turret cannot rotate forever because wires, tubing, and other hardware can become tangled.

Because of this, we need software limits.

For this exercise, the turret may only rotate between:

```java
public static final Angle LOWER_LIMIT = Degrees.of(-95);
public static final Angle UPPER_LIMIT = Degrees.of(95);
```

If code requests an angle outside this range, the turret should automatically clamp the request to the nearest valid position.

---

## Part 1: Configure the Motor

Inside `getMotorConfig()` complete the TODOs.

You will need to configure:

* Subsystem name
* CAN ID
* Logging units
* Motion Magic settings
* Gear ratio

Read the comments carefully.

---

## Part 2: Override setPosition()

Create a custom implementation of:

```java
@Override
public void setPosition(Angle goalPosition)
```

and normalize the goal angle to be between -180 and 180 degrees. Then clamp the goal angle to the physical limits of the turret whose values were mentioned above (-95 degrees and 95 degrees).

---

## Testing

Create temporary button bindings in `RobotContainer`.

### Button 1

Move turret to:

```java
Degrees.of(45)
```

### Button 2

Move turret to:

```java
Degrees.of(200)
```

---

## Verify

Before requesting checkoff:

* Run simulation
* Confirm the turret moves normally to 45°
* Confirm a 200° request gets clamped
* Confirm logged goals never exceed the turret limits

---

## Why This Matters

Most robot mechanisms are not just motors.

Subsystems frequently add:

* software limits
* feedforward
* safety checks
* special control modes

This task demonstrates how teams build subsystem-specific functionality on top of a reusable motor framework instead of rewriting motor control code for every mechanism.

---

## Wrap-Up

Once your code works:

1. **Create a branch for your work**
2. Commit with a clear commit message
3. Open a pull request
4. Ask a training lead or instructor for review

Be prepared to explain:

* what `MotorConfig` does
* why `MotorIO` exists
* why `setPosition()` was overridden
* how the turret limits work
