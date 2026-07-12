# Lesson 03: Superstructure / Subsystems
## Purpose

Introduce the control concepts and abstraction layers behind the superstructure so new members can read mechanism code without treating it like magic.

## Prerequisites

- Finish reading [Lesson 02](../../lesson-02-codebase-and-sim/README.md) and complete [Lesson 02 Task](../../tasks/lesson-02-codebase-sim-task/README.md).
- Basic comfort reading classes and methods in Java.

## Time box

1-2 hours

## Learning goals

- Understand what a superstructure is in our codebase.
- Understand the purpose of PID and feedforward at a practical level.
- Recognize where CTRE abstractions and wrappers live in our code.
- Trace a position or velocity request from a mechanism class down to the TalonFX.
- Understand how our 2026 subsystem abstraction differs from the simpler 2025 `Wrist/WristIO/WristIOSim/WristIOTalonFX` example.

## The Files We Will Use

Keep these files open while reading:

- [Superstructure.java](../../971-second-robot-2026/src/main/java/frc/robot/subsystems/superstructure/Superstructure.java)
- [GroundPivot.java](../../971-second-robot-2026/src/main/java/frc/robot/subsystems/superstructure/GroundPivot.java)
- [HoodLeft.java](../../971-second-robot-2026/src/main/java/frc/robot/subsystems/superstructure/HoodLeft.java)
- [MotorSubsystem.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorSubsystem.java)
- [MotorIO.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorIO.java)
- [MotorTalonFX.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorTalonFX.java)
- [MotorSim.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/MotorSim.java)
- [AngularSubsystem.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/AngularSubsystem.java)
- [LinearSubsystem.java](../../971-second-robot-2026/src/main/java/frc/robot/lib/superstructure/LinearSubsystem.java)

## What is a Subsystem?

A subsystem is a self-contained piece of robot behavior. A drivetrain is a subsystem. A shooter can be a subsystem. A pivot can be a subsystem.

Subsystems exist to:

- keep code organized by robot function,
- isolate responsibilities,
- make simulation and testing realistic,
- reduce hardware-specific logic spread across the codebase.

## What is the Superstructure?

The superstructure is different from a single-motor subsystem. It is the **coordinated set of mechanisms** that work together above the drivetrain. In this robot, that includes:

- flywheels,
- hoods,
- turrets,
- rollers,
- kicker,
- ground intake pivot,
- ground intake rollers,
- shooter targeting logic.

Open `Superstructure.java`. At the top of the class, look at the fields:

```java
// Superstructure.java, lines 28-45
public final FlywheelRight flywheelRight;
public final FlywheelLeft flywheelLeft;

public final HoodRight hoodRight;
public final HoodLeft hoodLeft;

public final RollerFloor rollerFloor;
public final B2 b2;
public final Kicker kicker;

public final GroundRollers groundRollers;
public final GroundPivot groundPivot;

public final ShooterHandler shooterHandlerRight;
public final ShooterHandler shooterHandlerLeft;

public final TurretRight turretRight;
public final TurretLeft turretLeft;
```

This is your first clue. `Superstructure` is not one motor. It is the coordinator for many mechanisms.

When you read this file, separate these two ideas:

- **`Superstructure` decides what the robot wants.**
- **Mechanism classes and motor wrappers decide how individual motors are commanded.**

That separation matters. If the robot chooses the wrong shooting mode, that is probably not a TalonFX problem. If the TalonFX has the wrong CAN ID or ratio, that is probably not a high-level `Superstructure` logic problem.

## PID Control

PID is a way to correct error.

For a position-controlled mechanism, the error is:

```
target position - measured position
```

For a velocity-controlled mechanism, the error is:

```
target velocity - measured velocity
```

The controller uses that error to decide what output to apply.

### The Three Terms

- **P (Proportional)**: proportional to the current error. More error = more correction.
- **I (Integral)**: based on error accumulated over time. Cleans up small steady-state errors.
- **D (Derivative)**: based on how quickly the error is changing. Dampens overshoot.

You do not need to memorize equations first. Start with behavior:

- If the mechanism moves too weakly or never reaches the target, the correction may be too small.
- If the mechanism overshoots badly, the correction may be too aggressive or the motion constraints may be wrong.
- If the mechanism oscillates, the loop may be overreacting, the sensor may be noisy, or the mechanism may have real mechanical issues.
- If the mechanism does not move at all, PID might not be the problem. The selected goal, control mode, sensor, CAN ID, inversion, current limit, or command path may be wrong.

This is a very helpful paper on PID that you can read if you want a deeper understanding: [Introduction to PID Controllers](https://georgegillard.com/resources/).

## Feedforward

PID reacts to error. Feedforward **predicts** what output you need even when error is zero.

Open `GroundPivot.java`. You will see a `TalonFXConfiguration` with values like:

```java
// GroundPivot.java, lines 24-31
tc.Slot0.kS = 0.37; // Static friction compensation
tc.Slot0.kV = 4.8;  // Velocity feedforward
tc.Slot0.kA = 0.0;  // Acceleration feedforward
tc.Slot0.kG = 0.0;  // Gravity compensation

tc.Slot0.kP = 2.0;  // Proportional gain
tc.Slot0.kI = 0.0;  // Integral gain
tc.Slot0.kD = 0.0;  // Derivative gain
```

The feedforward terms:

- **`kS`**: helps overcome static friction. A small burst of voltage to get the motor moving.
- **`kV`**: the voltage needed per unit of velocity. Higher target velocity = more voltage.
- **`kA`**: the voltage needed per unit of acceleration.
- **`kG`**: compensates for gravity. Important for mechanisms that fight gravity (elevators, arms).

Think of it this way: feedforward is your first guess for what output to apply. PID corrects the difference between what you predicted and what actually happened.

### Motion Magic Constraints

You will also see in `GroundPivot.java`:

```java
// GroundPivot.java, lines 36-38
tc.MotionMagic.MotionMagicCruiseVelocity = .8;
tc.MotionMagic.MotionMagicAcceleration = 10.0;
tc.MotionMagic.MotionMagicJerk = 0.0;
```

Motion Magic is CTRE's profiled position control. Instead of just saying "go to this position however you can", you give CTRE a target plus motion constraints. CTRE shapes the move using the configured cruise velocity and acceleration.

This means a mechanism can look "badly tuned" for multiple reasons:

- PID gains might be wrong.
- Feedforward might be wrong.
- Motion Magic constraints might be wrong.
- The gear ratio or unit conversion might be wrong.
- The commanded target might be wrong.

That is why you inspect before editing.

## CTRE TalonFX

CTRE is the vendor stack for devices like TalonFX motor controllers and CANcoders. The current robot code uses Phoenix 6 APIs.

A TalonFX can:

- receive voltage, velocity, or position control requests,
- report position and velocity,
- report voltage, current, temperature, and connection status,
- use onboard closed-loop gains,
- use Motion Magic,
- apply inversion, neutral mode, current limits, and feedback settings.

We do not want random CTRE calls spread across high-level robot logic. Instead, the repo wraps CTRE behavior in shared classes.

## The 2026 Subsystem Abstraction

### The Problem

If every mechanism wrote its own motor-control logic, the same patterns (store a goal, select a control mode, call the right CTRE request each periodic loop) would be copied dozens of times. That is hard to maintain and easy to break.

### The Solution: Layers

The 2026 code uses a layered abstraction. Each layer has one responsibility:

```
Superstructure.java          -- decides WHAT the robot wants
    |
HoodLeft.java / GroundPivot  -- mechanism-specific config + behavior
    | (extends)
AngularSubsystem.java        -- thin angular wrapper
    | (extends)
MotorSubsystem.java          -- reusable goal/mode logic
    | (calls through)
MotorIO.java                 -- abstraction boundary (abstract class)
    |
    +-- MotorTalonFX.java    -- real hardware implementation
    +-- MotorSim.java        -- simulation implementation
```

### Layer by Layer

#### `MotorIO.java` (The Abstraction Boundary)

`MotorIO.java` is the contract. It defines what mechanism code is allowed to ask for and what state flows back up:

```java
// MotorIO.java, lines 67-91 (abstract methods)
public abstract void setVoltage(Voltage goalVoltage);
public abstract void setVelocity(AngularVelocity goalVelocity);
public abstract void setPosition(Angle goalPosition);
public abstract void setPositionVoltage(Angle goalPosition);
public abstract void setPosition(Distance goalPosition);
public abstract void setPositionVoltage(Distance goalPosition);
public abstract void resetPosition(Angle newPosition);
public abstract void resetPosition(Distance newPosition);
public abstract void setCoast();
```

It also exposes measured state that both implementations must fill:

```java
// MotorIO.java, lines 24-45 (state fields)
protected Voltage appliedVoltage;
protected Current supplyCurrent;
protected Current statorCurrent;
protected Temperature temperature;
protected boolean connected = false;
protected Angle absolutePosition;
protected Angle position;
protected AngularVelocity velocity;
protected Voltage feedforward;
```

`MotorSubsystem` talks to `MotorIO`. It does not know whether the implementation is real hardware or simulation.

#### `MotorTalonFX.java` (Real Hardware)

Open `MotorTalonFX.java`. This is where the real Phoenix 6 objects live:

```java
// MotorTalonFX.java, lines 27-32
protected final TalonFX motor;
protected final VoltageOut voltageRequest;
protected final VelocityVoltage velocityRequest;
protected final DynamicMotionMagicVoltage dynamicMotionMagicPositionRequest;
protected final PositionVoltage positionVoltageRequest;
```

The constructor creates the TalonFX, configures it, and sets up status signals:

```java
// MotorTalonFX.java, lines 42-91
motor = new TalonFX(motorConfig.ID(), motorConfig.BUS());
motor.getConfigurator().apply(talonfxConfig);
positionSignal = motor.getPosition();
velocitySignal = motor.getVelocity();
// ... more signals
```

The `periodic()` method reads from CAN:

```java
// MotorTalonFX.java, lines 151-173
@Override
public void periodic() {
  BaseStatusSignal.refreshAll(positionSignal, velocitySignal, ...);
  position = positionSignal.getValue();
  velocity = velocitySignal.getValue();
  appliedVoltage = appliedVoltageSignal.getValue();
  // ...
}
```

And the control methods send requests to the motor:

```java
// MotorTalonFX.java, lines 98-133
@Override
public void setVoltage(Voltage goalVoltage) {
  motor.setControl(voltageRequest.withOutput(goalVoltage.in(Volts)));
}

@Override
public void setPosition(Angle goalPosition) {
  setDynamicMotionMagicPosition(goalPosition.in(Rotations));
}

private void setDynamicMotionMagicPosition(double goalPosition) {
  motor.setControl(
      dynamicMotionMagicPositionRequest
           .withPosition(goalPosition)
           .withFeedForward(feedforward)
           .withEnableFOC(motorConfig.FOC())
           .withSlot(0));
}
```

Those `motor.setControl(...)` calls are where the wrapper actually sends requests to the TalonFX.

#### `MotorSim.java` (Simulation)

Open `MotorSim.java`. This class does not talk to CAN. It updates fake position and velocity using a simple trapezoid profile:

```java
// MotorSim.java, lines 62-71
@Override
public void periodic() {
  state = profile.calculate(Constants.UPDATE_PERIOD.in(Seconds), state, goal);
  position = Rotations.of(state.position);
  velocity = RotationsPerSecond.of(state.velocity);
  supplyCurrent = Amps.of(10.0);
  connected = true;
  super.periodic();
}
```

The constructor uses the same Motion Magic constraints from config to shape the simulated profile:

```java
// MotorSim.java, lines 19-28
profile = new TrapezoidProfile(
    new TrapezoidProfile.Constraints(
        config.TALONFX_CONFIG().MotionMagic.MotionMagicCruiseVelocity,
        config.TALONFX_CONFIG().MotionMagic.MotionMagicAcceleration));
```

This lets you test mechanism logic without a robot, but it does not prove the real hardware config is correct.

**Simulation can help answer:**
- Did our code select the expected goal?
- Did the mechanism command path run?
- Does the state move in the expected direction?
- Are logs or dashboard values being updated?

**Simulation cannot fully prove:**
- CAN IDs are correct,
- sensors are physically wired correctly,
- the real mechanism has enough torque,
- current limits and friction behave exactly as modeled.

Use sim, but do not overtrust it.

#### `MotorSubsystem.java` -- The Shared Logic

Open `MotorSubsystem.java`. This is the reusable mechanism logic layer.

The enum defines control modes:

```java
// MotorSubsystem.java, lines 21-28
protected enum Mode {
  VOLTAGE,
  VELOCITY,
  POSITION,
  POSITION_VOLTAGE,
  COAST,
  NONE
}
```

The constructor chooses simulation vs real hardware:

```java
// MotorSubsystem.java, lines 46-48
public MotorSubsystem(MotorConfig motorConfig) {
  this(RobotBase.isSimulation() ? new MotorSim(motorConfig) : new MotorTalonFX(motorConfig));
}
```

Goal-setting methods store a value and select a mode:

```java
// MotorSubsystem.java, lines 78-106
public void setVoltage(Voltage goalVoltage) {
  this.goalVoltage = goalVoltage;
  this.controlMode = Mode.VOLTAGE;
}

public void setPosition(Angle goalPosition) {
  this.goalPosition = goalPosition;
  this.controlMode = Mode.POSITION;
}

public void setVelocity(AngularVelocity goalVelocity) {
  this.goalVelocity = goalVelocity;
  this.controlMode = Mode.VELOCITY;
}
```

The `periodic()` dispatches to the correct IO method:

```java
// MotorSubsystem.java, lines 57-65
public void periodic() {
  switch (controlMode) {
    case VOLTAGE -> io.setVoltage(goalVoltage);
    case VELOCITY -> io.setVelocity(goalVelocity);
    case POSITION -> io.setPosition(goalPosition);
    case POSITION_VOLTAGE -> io.setPositionVoltage(goalPosition);
    case COAST -> io.setCoast();
    case NONE -> {}
  }
  io.periodic();
  // ... logging
}
```

#### `AngularSubsystem.java` and `LinearSubsystem.java` -- Domain Wrappers

These are thin subclasses that add type-safe convenience:

- `AngularSubsystem` extends `MotorSubsystem` for mechanisms measured in angles (turrets, hoods, pivot).
- `LinearSubsystem` extends `MotorSubsystem` for mechanisms measured in distance (hoods use lead-screw inches internally).

They add no new logic -- just constructors and domain-specific getters. For example, `LinearSubsystem` provides `getLinearPosition()` which returns a `Distance` instead of an `Angle`.

#### Mechanism Classes -- `GroundPivot`, `HoodLeft`, etc.

These are the leaves. Each mechanism class provides:

1. A `MotorConfig` with mechanism-specific values (CAN ID, bus, ratio, gains, feedforward, current limits, inversion).
2. Any mechanism-specific behavior that does not generalize.

For example, `GroundPivot.java`:

```java
// GroundPivot.java, lines 16-18
public GroundPivot() {
  super(getMotorConfig());
}

// GroundPivot.java, lines 51-58
return MotorConfig.builder()
     .NAME("Ground Pivot")
     .ID(14)
     .BUS(new CANBus("rio"))
     .LOG_UNIT(Degrees)
     .TALONFX_CONFIG(tc)
     .FOC(false)
     .build();
```

Or `HoodLeft.java`, which overrides `setPosition` to add a custom feedforward calculation:

```java
// HoodLeft.java, lines 57-60
@Override
public void setPosition(Angle goalPosition) {
  setFeedforward(calculatePositionFeedforward(goalPosition));
  super.setPosition(goalPosition);
}
```

The inheritance chain for `HoodLeft` is:

```
HoodLeft -> Hood -> LinearSubsystem -> MotorSubsystem -> (uses MotorIO)
```

## Tracing a Request: Full Code Flow

Let us trace `groundPivot.setPosition(...)` all the way down to the hardware:

```
1. Superstructure calls:
     groundPivot.setPosition(SetpointGoal.INTAKE_PIVOT ground-pivot value)

2. GroundPivot inherits setPosition() from MotorSubsystem (line 83):
     this.goalPosition = goalPosition;
     this.controlMode = Mode.POSITION;

3. MotorSubsystem.periodic() dispatches (line 61):
     case POSITION -> io.setPosition(goalPosition);

4. On real robot, io is a MotorTalonFX (line 108):
     setDynamicMotionMagicPosition(goalPosition.in(Rotations));

5. Which calls (line 128):
     motor.setControl(
         dynamicMotionMagicPositionRequest
              .withPosition(goalPosition)
              .withFeedForward(feedforward)
              .withEnableFOC(motorConfig.FOC())
              .withSlot(0));

6. The TalonFX receives a Dynamic Motion Magic request
   and uses the configured PID + feedforward + Motion Magic constraints
   to drive the motor toward the target position.
```

In simulation the path diverges at step 3:

```
3. In sim, io is a MotorSim (line 42):
     goal = new TrapezoidProfile.State(goalPosition.in(Rotations), 0);

4. MotorSim.periodic() calculates (line 63):
     state = profile.calculate(dt, state, goal);
     position = Rotations.of(state.position);
```

## The Original 2025 Wrist Pattern

Now that you have seen the current 2026 shared motor pattern, look at the older and simpler abstraction. This is a teaching example -- not the current code.

### `Wrist.java`

The subsystem. Owns wrist behavior, but does not directly construct or command a TalonFX.

```java
package frc.robot.subsystems.wrist;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class Wrist extends SubsystemBase {
  private final WristIO io;
  private final WristIO.WristIOInputs inputs = new WristIO.WristIOInputs();

  public Wrist(WristIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.recordOutput("Wrist/PositionDeg", inputs.positionDeg);
    Logger.recordOutput("Wrist/VelocityDegPerSec", inputs.velocityDegPerSec);
    Logger.recordOutput("Wrist/AppliedVolts", inputs.appliedVolts);
    Logger.recordOutput("Wrist/Connected", inputs.connected);
  }

  public void setPositionDeg(double degrees) { io.setPositionDeg(degrees); }
  public void setVoltage(double volts) { io.setVoltage(volts); }
  public void stop() { io.setVoltage(0.0); }
  public double getPositionDeg() { return inputs.positionDeg; }
}
```

### `WristIO.java`

The contract between wrist logic and implementations.

```java
package frc.robot.subsystems.wrist;

public interface WristIO {
  class WristIOInputs {
    public double positionDeg = 0.0;
    public double velocityDegPerSec = 0.0;
    public double appliedVolts = 0.0;
    public boolean connected = false;
  }

  default void updateInputs(WristIOInputs inputs) {}
  default void setVoltage(double volts) {}
  default void setPositionDeg(double degrees) {}
}
```

### `WristIOSim.java`

Fake implementation for local testing:

```java
package frc.robot.subsystems.wrist;

import edu.wpi.first.math.MathUtil;

public class WristIOSim implements WristIO {
  private double positionDeg = 0.0;
  private double velocityDegPerSec = 0.0;
  private double appliedVolts = 0.0;
  private double targetDeg = 0.0;
  private boolean closedLoop = false;

  @Override
  public void updateInputs(WristIOInputs inputs) {
    if (closedLoop) {
      double errorDeg = targetDeg - positionDeg;
      velocityDegPerSec = MathUtil.clamp(errorDeg * 8.0, -MAX_SPEED, MAX_SPEED);
      appliedVolts = MathUtil.clamp(errorDeg * 0.2, -12.0, 12.0);
    }
    positionDeg += velocityDegPerSec * LOOP_TIME_SEC;
    positionDeg = MathUtil.clamp(positionDeg, MIN_POSITION_DEG, MAX_POSITION_DEG);
    inputs.positionDeg = positionDeg;
    inputs.connected = true;
  }

  @Override
  public void setVoltage(double volts) {
    closedLoop = false;
    appliedVolts = MathUtil.clamp(volts, -12.0, 12.0);
  }

  @Override
  public void setPositionDeg(double degrees) {
    closedLoop = true;
    targetDeg = MathUtil.clamp(degrees, MIN_POSITION_DEG, MAX_POSITION_DEG);
  }
}
```

### `WristIOTalonFX.java`

Real hardware implementation:

```java
package frc.robot.subsystems.wrist;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
// ... more imports

public class WristIOTalonFX implements WristIO {
  private final TalonFX motor = new TalonFX(WRIST_MOTOR_ID, CAN_BUS);
  private final VoltageOut voltageRequest = new VoltageOut(0.0);
  private final PositionVoltage positionRequest = new PositionVoltage(0.0);

  public WristIOTalonFX() {
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    config.Feedback.SensorToMechanismRatio = SENSOR_TO_MECHANISM_RATIO;
    config.Slot0.kS = 0.1;
    config.Slot0.kG = 0.0;
    config.Slot0.kP = 20.0;
    config.Slot0.kI = 0.0;
    config.Slot0.kD = 0.0;
    motor.getConfigurator().apply(config);
  }

  @Override
  public void setVoltage(double volts) {
    motor.setControl(voltageRequest.withOutput(volts));
  }

  @Override
  public void setPositionDeg(double degrees) {
    motor.setControl(positionRequest.withPosition(degrees / 360.0));
  }
}
```

### The Old Flow

```
Wrist.java               -- owns wrist behavior, calls io.setPositionDeg()
WristIO.java             -- defines inputs and commands
WristIOSim.java          -- fake local state
WristIOTalonFX.java      -- real CTRE hardware
```

A button asking the wrist to deploy:

```
button or command
   -> wrist.setPositionDeg(60.0)
   -> io.setPositionDeg(60.0)
   -> WristIOSim updates fake state, OR WristIOTalonFX sends a CTRE request
```

The weakness here is that it's repetitive. Every mechanism needs its own `ArmIO`, `ElevatorIO`, `HoodIO`, etc.

## How the 2025 Pattern Maps to 2026

```
2025 wrist pattern              2026 shared pattern
--------------------------------------------------------------
Wrist.java                      GroundPivot.java / HoodLeft.java
WristIO.java                    MotorIO.java
WristIOSim.java                 MotorSim.java
WristIOTalonFX.java             MotorTalonFX.java
Wrist constants/config          MotorConfig + mechanism TalonFXConfiguration
Wrist periodic control flow     MotorSubsystem.periodic()
```

**What stayed the same:**
- Mechanism logic is separated from hardware-specific IO.
- Separate real and simulated implementations.
- Logged inputs and outputs for debugging.

**What changed:**
- One shared `MotorIO` instead of a unique IO interface per mechanism.
- One shared `MotorTalonFX` instead of a unique TalonFX class per mechanism.
- Mechanism-specific details moved into `MotorConfig` and each mechanism's `TalonFXConfiguration`.
- `MotorSubsystem` handles goal storage and mode dispatch for every mechanism.

The 2026 pattern is more reusable but harder to read at first because you must follow inheritance and configuration.

## How To Investigate Before Editing

When a mechanism behaves badly, write down the evidence in this order:

1. The selected high-level goal.
2. The target position, velocity, or voltage.
3. The measured position or velocity.
4. The applied voltage or percent output.
5. The active control mode.
6. The relevant config values: CAN ID, bus, ratio, inversion, gains, feedforward, current limits, Motion Magic constraints.
7. Whether you are running sim or real hardware.

Then decide which layer is suspicious:

| Symptom | Likely layer |
|---------|-------------|
| Wrong goal selected | `Superstructure`, controller logic, `SetpointGoal` |
| Correct goal but wrong target value | Setpoint definitions, units |
| Correct target but wrong measured state | Sensor config, ratios, reset behavior, sim state |
| Correct target and measurement but wrong output | Control mode, gains, feedforward, CTRE request type |
| Works in sim but not real | CAN IDs, bus names, inversion, sensors, current limits |

## What Not To Do

- Do not start by randomly changing `kP`.
- Do not edit `MotorTalonFX` just because a single mechanism acts wrong.
- Do not assume a sim result proves hardware configuration.
- Do not assume a real robot issue is "just mechanical" before checking logs.
- Do not copy an old CTRE API pattern from another year without checking the Phoenix 6 code currently in this repo.

## Required Mech/Elec Callout

This is where basic CAN understanding matters. A TalonFX or CANcoder on CAN is a real device with an ID, wiring, and failure modes. A software bug and a hardware/config issue can look similar at first. Students should know:

- Each device on CAN has a unique ID.
- Devices connect to a specific bus ("rio", "Superstructure Bus", etc.).
- If a device is disconnected or has the wrong ID, `connected` will be false.
- Motor inversion affects direction. Wrong inversion = motor spins opposite of what software expects.

## Exercise

Work through this slowly with the real code open.

1. Open `Superstructure.java`. List the mechanisms it owns.
2. Open `GroundPivot.java`. Identify:
   - CAN ID,
   - CAN bus,
   - inversion,
   - ratio,
   - PID gains (`kP`, `kI`, `kD`),
   - feedforward gains (`kS`, `kV`, `kA`, `kG`),
   - Motion Magic constraints (cruise velocity, acceleration, jerk).
3. Open `HoodLeft.java`. Trace the inheritance: `HoodLeft` -> `Hood` -> `LinearSubsystem` -> `MotorSubsystem`. Note what each layer adds.
4. Open `MotorSubsystem.java`. Find the `Mode` enum and the `periodic()` switch. Identify which mode `setPosition()` selects.
5. Open `MotorTalonFX.java`. Find the CTRE request object that `setPosition()` ultimately uses.
6. Open `MotorSim.java`. Explain what simulation does instead of sending a CTRE request.

When you are done, explain the whole path out loud:

```
GroundPivot.setPosition(Degrees.of(0.0)) calls MotorSubsystem.setPosition().
That stores the goal and sets controlMode to POSITION.
MotorSubsystem.periodic() dispatches to io.setPosition(goalPosition).
On real hardware, MotorTalonFX.setPosition() sends a DynamicMotionMagicVoltage request.
The TalonFX at CAN ID 14 on "rio" uses Slot 0 gains and Motion Magic constraints.
In simulation, MotorSim uses a TrapezoidProfile to fake the motion.
```

## Checkoff

Be ready to answer these questions:

- What does "superstructure" mean in this repo?
- Why do we use PID or closed-loop control for mechanisms?
- What is the difference between PID and feedforward?
- Where are CTRE TalonFX requests sent?
- What does `MotorIO` let us separate?
- How does `MotorSubsystem` choose between voltage, velocity, position, and coast?
- Why is the 2026 abstraction harder to read than the 2025 `WristIO` pattern?
- What would you inspect before changing a tuning constant?

## Before Next Lesson

- Re-read `Superstructure.java` with attention to how `periodic()` decides which goals to set.
- Note places where controller inputs, mode toggles, and conditions change behavior.
- Be ready to implement a small logic change in the superstructure coordination layer.

## Task for this lesson

Follow [Lesson 03 Task: Superstructure Structure Trace](../tasks/lesson-03-superstructure-structure-task/README.md).
