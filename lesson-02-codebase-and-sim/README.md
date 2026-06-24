## Prerequisites 

Finish reading [Lesson 1](https://github.com/frc971/training-2026/wiki/Lesson-1:-Intro-to-Git-&-GitHub) and complete [Lesson 1 Task](https://github.com/frc971/training-2026/tree/main/tasks/java-evaluation).

## Learning goals

- Understand the top-level flow of a WPILib Java robot project.
- Navigate the main folders in our 2026 repos.
- Run simulation and understand what the `sim/` config is doing.
- Use AdvantageScope to inspect logs and visualization output.

## Time box

1-2 hours

## In our codebase

- Main robot lifecycle: [971-second-robot-2026/src/main/java/frc/robot/Robot.java](https://github.com/frc971/971-second-robot-2026/blob/main/src/main/java/frc/robot/Robot.java)
- Wiring and subsystem setup: [971-second-robot-2026/src/main/java/frc/robot/RobotContainer.java](https://github.com/frc971/971-second-robot-2026/blob/main/src/main/java/frc/robot/RobotContainer.java)
- Controller bindings: [971-second-robot-2026/src/main/java/frc/robot/subsystems/Controllers.java](https://github.com/frc971/971-second-robot-2026/blob/main/src/main/java/frc/robot/subsystems/Controllers.java)
- Simulation config notes: [971-second-robot-2026/sim/README.md](https://github.com/frc971/971-second-robot-2026/blob/main/sim/README.md)
- Example visualization reference: [971-second-robot-2026/src/main/java/frc/robot/subsystems/superstructure/Visualization.java](https://github.com/frc971/971-second-robot-2026/blob/5fe7a6ceba3d09d6de73443ca9e77ccd1b3a4196/src/main/java/frc/robot/subsystems/superstructure/Visualization.java)

---

## Typical WPILib Project

Before diving into our specific code, understand the WPILib lifecycle that every robot project follows. This is the skeleton our code fills in the details.

### The Entry Point: `Main.java`

Every WPILib robot program starts in a single `main()` method. 

```java
public static void main(String... args) {
    RobotBase.startRobot(Robot::new);
}
```

This tells WPILib to construct our `Robot` class and manage its lifecycle. **You should never need to edit this file.**

### The Robot Lifecycle

WPILib calls methods on your `Robot` class in a fixed order during a match:

| Phase | Method | When | Runs |
|---|---|---|---|
| Power on | `robotInit()` | Once when robot boots | Once |
| Disabled | `disabledInit()` | When disabled (before auto, between auto/teleop, after match) | Each disable |
| Disabled loop | `disabledPeriodic()` | While disabled, ~20 ms loop | Repeated |
| Auto start | `autonomousInit()` | When auto begins | Once per auto |
| Auto loop | `autonomousPeriodic()` | During auto, ~20 ms loop | Repeated |
| Auto end | `autonomousExit()` | When auto ends | Once per auto |
| Teleop start | `teleopInit()` | When teleop begins | Once per teleop |
| Teleop loop | `teleopPeriodic()` | During teleop, ~20 ms loop | Repeated |
| Teleop end | `teleopExit()` | When teleop ends | Once per teleop |
| Every loop | `robotPeriodic()` | Every 20 ms regardless of mode | Every cycle |

The `robotPeriodic()` method  runs every 20 ms for the entire lifetime of the robot and is where you put logic that must run in **every** mode (vision updates, logging, command scheduler ticks).

### The Command-Based Framework

WPILib provides a command-based framework on top of this lifecycle. The key concepts:

- **Subsystem** — a piece of robot hardware (drivetrain, shooter, intake). Each subsystem owns its actuators and sensors.
- **Command** — a discrete action that runs for a bounded time ("shoot for 2 seconds", "run intake until sensor triggers"). Commands require subsystems, ensuring a subsystem isn't controlled by two commands at once.
- **CommandScheduler** — the central dispatcher. Each `robotPeriodic()` tick, it runs all active commands and checks which idle commands should start based on their triggers.
- **Default Command** — a command automatically running on a subsystem whenever no other command is. For the drivetrain, this is always driver control.

In our code, `CommandScheduler.getInstance().run()` at the bottom of `robotPeriodic()` is what makes everything move. Without that one line, no commands ever execute.

### The Typical Data Flow

```
Driver sticks a joystick
  -> Controllers.java reads the input as a Trigger or axis value
    -> RobotContainer.java default command reads the controller values
      -> Drivetrain receives a SwerveRequest (speeds, direction)
        -> Phoenix motors execute over CAN
          -> Encoders report back new positions
            -> Odometry updates the robot's estimated pose
              -> Logger records the pose to NetworkTables / log file
                -> AdvantageScope / SimGui displays the robot on a field
```

Understanding this chain end-to-end is more important than memorizing any single file. When something goes wrong, trace the chain from input to output.

---

## Codebase Map

Use this lesson to explain the project structure from the outside in. Open the `971-second-robot-2026` folder in VS Code and explore each path:

### Top-level directories

```
971-second-robot-2026/
├── build.gradle              # Gradle build config, dependencies, sim support
├── vendordeps/               # Third-party JSON library descriptors
├── sim/                      # SimGui window layout + key mapping JSON
├── ascope_assets/            # AdvantageScope 3D robot model (.glb files)
├── scripts/                  # Utility scripts (flip-auto.py, extract-logs.sh, etc.)
├── src/main/deploy/          # Files copied to robot at deploy time
└── src/main/java/frc/robot/  # All robot code
```

### `src/main/java/frc/robot/`

```
frc/robot/
├── Main.java                    # Entry point — never edit
├── Robot.java                   # Lifecycle: init, periodic, mode transitions
├── RobotContainer.java          # Wires subsystems + controllers + default commands
├── Constants.java               # Mode (REAL/SIM/REPLAY), sim physics, PID tuning
├── Telemetry.java               # Publishes drivetrain state to NetworkTables + logs
├── generated/
│   └── TunerConstants.java      # CTRE Phoenix Tuner output — DO NOT HAND-EDIT
├── lib/
│   ├── JoystickValues.java      # Joystick input processing (deadband, curve, slew)
│   ├── shooter/                 # Shooter physics solver, speed tables, shot math
│   ├── simulation/              # MapleSim swerve physics integration
│   └── superstructure/          # Motor/sensor abstractions (MotorIO, CANRange, Toggle, Edge)
└── subsystems/
    ├── CommandSwerveDrivetrain.java  # Swerve drivetrain + MapleSim + SysId + logging
    ├── Controllers.java              # Xbox controller bindings (TROY=driver, ANDRE=operator)
    ├── Autos.java                    # BLine auto routines + SmartDashboard chooser
    ├── HubShiftUtil.java             # Hub shift game-state tracking
    ├── superstructure/               # All mechanism subsystems (turrets, hoods, flywheels, intake)
    └── vision/                       # BOS (vision odometry) + TagHelper
```

### What each folder means

- **`src/main/java/frc/robot`** — lifecycle classes (`Robot`, `Main`) and the wiring layer (`RobotContainer`). These are the first files you read to understand any WPILib robot project.
- **`subsystems/`** — the bulk of robot logic. Drivetrain, autos, controllers, vision, and all superstructure mechanisms. Each subsystem is a self-contained unit that controls specific hardware.
- **`lib/`** — reusable helpers and lower-level abstractions. These classes are **used by** subsystems but don't own hardware themselves. Examples: `JoystickValues` (input curve/slew), `Edge`/`Toggle` (one-shot button detection), `MotorIO` (motor interface), shooter physics math.
- **`generated/`** — auto-generated constants from the CTRE Phoenix Tuner tool. Never hand-edit `TunerConstants.java`; re-run the Tuner if hardware changes.
- **`src/main/deploy`** — files copied to the RoboRIO at deploy time. This is where autonomous path JSON files live:
   - `autos/paths/` — BLine path definitions (F_Normal.json, S_Normal.json, H_Normal.json, etc.)
   - `autos/config.json` — BLine global configuration
   - `pathplanner/` — PathPlanner paths and autos (backup/alternative auto system)
- **`vendordeps/`** — JSON files that tell Gradle where to download third-party libraries. Each JSON file is a "vendor dependency" that adds classes to your project:
   - `Phoenix6-frc2026-latest.json` — CTRE Phoenix 6 (motor controllers, Pigeon 2 gyro, CANdevices)
   - `AdvantageKit.json` — AdvantageKit (logging, replay, visualization)
   - `BLine-Lib.json` — BLine (vision-based autonomous path following)
   - `PathplannerLib.json` — PathPlanner (alternative path generation tool)
   - `maple-sim.json` — MapleSim (swerve drivetrain physics simulation)
   - `WPILibNewCommands.json` — WPILib command-based framework
- **`sim/`** — local simulation GUI configuration. Contains three JSON files and an HTML key mapping reference. More below.
- **`ascope_assets/`** — 3D robot model files (`.glb`) and a `config.json` that maps AdvantageKit visualization components to physical joints on the robot. This is what lets AdvantageScope show a 3D robot that moves as you drive in sim.

---

## How to Read a New Robot Repo Without Drowning

New members often make the mistake of reading every file as if all files are equally important. They are not.

For a first pass:

1. **Start with the lifecycle entry point.** Open `Robot.java` and read the constructor and `robotPeriodic()`. This tells you what subsystems exist and what runs every cycle.
2. **Find the wiring and input layer.** Open `RobotContainer.java`. This shows you how subsystems are connected and what the default command (always-running driver control) does.
3. **Identify the major subsystems.** Look at the `subsystems/` folder. Read the top-level class in each subfolder to understand what hardware it controls. Don't read every method — scan field declarations and constructor parameters.
4. **Look at deploy assets and library markers.** Glance at `src/main/deploy/` to see what path files exist. Check `vendordeps/` to understand what third-party libraries the robot depends on.
5. **Only then dive into one subsystem deeply.** Pick one mechanism — the drivetrain, a shooter, an intake — and read its full implementation.

---

## Lesson Content

### Step 1: `Robot.java` — the lifecycle entry point

Open `Robot.java` and trace what happens:

**The constructor (`Robot()`):**
- Records metadata to the logger (project name, team number, robot name).
- Branches on `Constants.MODE`:
   - **REAL** — logs to a `.wpilog` file on the RoboRIO AND publishes to NetworkTables (`NT4Publisher`). This is match day.
   - **SIM** — publishes only to NetworkTables. This is desktop simulation.
   - **REPLAY** — reads an old `.wpilog` file and replays it. This is post-match analysis. A secondary writer saves a `_sim` suffix log with simulated data overlaid on the replay.
- Calls `Logger.start()` — AdvantageKit begins recording.
- Initializes `TagHelper` (vision tag lookup), `RobotContainer` (wires everything), `BOS` (camera-based odometry), and `Autos` (auto routines).
- **Key insight:** By the time the constructor finishes, the entire robot is constructed and wired. No subsystem is created lazily.

**`robotInit()`:**
- Starts `DataLogManager` (WPILib's built-in logging).
- Calls `robotContainer.resetSuperstructure()` to zero all mechanism encoders.

**`robotPeriodic()` — the heart of the robot:**
Every 20 ms, this method:
1. Calls `robotContainer.periodic()` → which calls `superstructure.periodic()` → which runs all teleop/auto mechanism logic and calls `periodic()` on every individual motor subsystem.
2. Logs hub shift timing info (game-state tracking for our 2026 game strategy).
3. Calls `bos.updatePose()` — updates the vision-based pose estimate.
4. Checks `Controllers.ODOMETRY_RESET` (driver Y button) — if pressed, resets the drivetrain pose to the last vision reading.
5. Checks `Controllers.DISABLE_OTF` (operator X button) — same pose reset, used when On-The-Fly aiming is disabled.
6. Calls `CommandScheduler.getInstance().run()` — executes all active commands and evaluates triggers. **This is the line that makes the robot do anything.**

**`disabledPeriodic()`:**
- Computes the autonomous start pose from the currently selected auto routine.
- Flips the pose to the red alliance coordinate system if on red.
- Sends the pose to telemetry so it shows on the SimGui field.

**`autonomousInit()`:**
- Gets the auto command from the `Autos` chooser.
- Schedules `superstructure.neutral()` (resets all mechanisms to stowed positions).
- Schedules the autonomous command.
- Re-initializes hub shift tracking.

**`teleopInit()`:**
- Cancels the autonomous command if still running (e.g., auto ran longer than 15 seconds).
- Re-initializes hub shift tracking.
- The drivetrain's default command (driver control) automatically starts because no command is explicitly scheduled — the CommandScheduler handles this.

### Step 2: `RobotContainer.java` — wiring everything together

Open `RobotContainer.java` and trace the wiring:

**Field declarations:**
- Creates a `Superstructure` (which in turn creates all mechanism subsystems).
- Defines three `SwerveRequest.FieldCentric` configurations with different speed profiles:
   - `drive` — full speed (3.5 m/s translation, 0.8 rot/s angular)
   - `shootingDrive` — 30% speed for steady aiming while shooting
   - `shuttlingDrive` — 50% speed for careful left/right shuttle movement
- Each profile has its own deadband and slew rate limiters to prevent jarring transitions.
- Creates the drivetrain via `TunerConstants.createDrivetrain()`.

**The constructor:**
1. Creates the `Superstructure` (passing `this` so superstructure can reference the drivetrain).
2. Calls `configureDrivetrain()` — sets the default command.
3. Silences the joystick connection warning (cosmetic).
4. Registers the telemetry callback: `drivetrain.registerTelemetry(logger::telemeterize)`.
5. In simulation only, resets the robot pose to field center `(3, 3, 0)`.
6. Registers BLine event triggers — these fire commands at specific points along an auto path:
   - `"shoot"` → `superstructure.shootAuto()`
   - `"shootNoJuice"` → `superstructure.shootAutoNoJuice()`
   - `"neutral"` → `superstructure.neutral()`
   - `"intakeDown"` → `superstructure.intakePivotDownAuto()`

**`configureDrivetrain()` — the default command:**
This is the most important method for understanding driver control. The drivetrain's default command:
1. Reads the driver controller (`Controllers.TROY`) left stick for translation and right stick for rotation.
2. If the operator holds the shoot trigger and the driver isn't trying to move, applies a brake (stops the robot for a stable shot).
3. Otherwise, cross-fades between the normal/shooting/shuttling speed profiles based on operator controller state (`Controllers.SHOOTING`, `Controllers.SHUTTLING`).
4. Applies joystick input through `JoystickValues` — exponential curve shaping and slew rate limiting for smooth control.

### Step 3: `Controllers.java` — the input layer

Open `Controllers.java`. This file defines two Xbox controllers:
- **TROY** (port 0) — the driver. Controls drivetrain via left stick, has intake pivot toggle (left bumper), intake rollers (left trigger), shoot redundancy (right trigger), odometry reset (Y).
- **ANDRE** (port 1) — the operator. Controls shooting (right trigger), shuttling (bumpers), manual mode toggle (Y), OTF disable (X), and D-pad fine-tuning for flywheel and turret setpoints.

Notice the abstractions:
- `Toggle` — tracks button state changes (on/off/on/off). Used for intake pivot and manual mode.
- `Edge` — fires once on the rising edge of a button press. Used for D-pad setpoint adjustments so holding the button doesn't increment repeatedly.
- Composite triggers like `SHOOTING = SHOOT_REDUNDANCY.or(SHOOT)` allow either the driver or operator to initiate a shot.

### Step 4: `Visualization.java` — 3D robot in AdvantageScope

Open `subsystems/superstructure/Visualization.java`. This class:
1. Defines static `Transform3d` constants that describe the kinematic chain — the physical offset from robot center to each joint (turrets, hoods, ground pivot).
2. In `periodic()`, composes live encoder readings into full 3D poses for each component.
3. Logs all five component poses via `Logger.recordOutput("Visualization/Components", ...)`.
4. The `ascope_assets/Robot_First/config.json` file maps these logged transforms to animated joints on the 3D robot model.

When you open AdvantageScope during simulation, you can see the robot model's turrets rotate and hoods tilt in real time as the code runs.




---

## Simulator Explanation

## Watch this video: [FRC 971 - WPILib Simulator and AdvantageScope Tutorial](https://www.youtube.com/watch?v=7fhh3e_5KcM).

WPILib simulation is useful because it gives you a place to verify control flow before real robot time is available.

Use this lesson to make that distinction explicit:

- **What simulation does well:** tests control flow (does pressing the shoot trigger actually call the shooter commands?), verifies wiring (are controllers mapped to the right ports?), catches null pointer exceptions before deploy, and models physics approximately (MapleSim simulates swerve wheel friction, robot mass, and inertia).
- **What simulation cannot prove:** exact motor current draw, real CAN bus timing, encoder noise, wire lengths, mechanical binding, battery sag under load, camera latency, or RF interference.
- **Why it is still worth using:** it catches obvious wiring and control-flow mistakes early. A command that never schedules in sim will never schedule on the robot either.

### The Simulation Workflow

1. **`build.gradle` enables desktop simulation:**
   ```groovy
   def includeDesktopSupport = true
   wpi.sim.addGui().defaultEnabled = true
   wpi.sim.addDriverstation()
   ```
   This tells GradleRIO to compile and run the robot program as a desktop Java application instead of deploying to a RoboRIO. The `addGui()` call launches SimGui (the WPILib simulation dashboard), and `addDriverstation()` emulates a Driver Station for testing auto/teleop mode transitions.

2. **Running simulation in VS Code:**
   - Either ```Command + Shift + P``` (Mac) or ```Ctrl + Shift + P``` (Windows) and select WPILib: Simulate Robot Code from the dropdown or type ```./gradlew simulateJava``` in the terminal. 
   - VS Code compiles the project and runs `java -cp ... frc.robot.Main` on your desktop.
   - SimGui opens automatically, showing the field, dashboard, and robot state.
   - The robot program thinks it is running — `robotInit()`, `robotPeriodic()`, and all periodic methods execute on their normal 20 ms loop.

3. **The `sim/` folder:**
   Contains saved SimGui window layout and keyboard-to-joystick mapping JSON files:
   - `simgui.json` — NetworkTables window configuration (field view, robot pose visualization, SmartDashboard bindings, data types). Configures the field to show robot pose as a pink arrow with a green body.
   - `simgui-ds.json` — keyboard mappings for two virtual Xbox controllers:
      - **Keyboard 0 (Driver):** WASD = left stick, Arrow keys = right stick, U/O = triggers, 1-0 = face buttons
      - **Keyboard 1 (Operator):** Q/E = left X, P/; = right Y, T/G/H/F = POV, Z/X/C/V/B/N/M/=/,/= = face buttons
   - `simgui-window.json` — dock layout for the SimGui window (19 panels arranged in a grid).
   - `Simulation Key Mapping.html` — a visual reference you can open in a browser showing colored keyboard diagrams.

   **Before first run, copy the sim config to the project root:**
   ```bash
   cp sim/simgui*.json .
   ```
   SimGui reads these files from the project root. The `sim/` folder is the "source of truth"; the copies at root are the "deployed" config.

4. **The FMS (Field Management System) in SimGui:**
   The Robot State / FMS panel lets you simulate match events:
   - Click "Auto" → "Enable" to enter autonomous mode (triggers `autonomousInit()`).
   - After 15 seconds (or click "Teleop" → "Enable") to enter teleop (triggers `teleopInit()`).
   - Click "Disable" to return to disabled mode.
   - Use the alliance color selector to test red-alliance coordinate flipping.

### Simulation Limitations — What to Watch For

| Works in sim | Does NOT work the same in sim |
|---|---|
| Command scheduling, default commands | Real CAN bus latency and packet loss |
| Controller inputs via keyboard | Encoder noise, slip, and missed packets |
| MapleSim swerve physics (approximate) | Exact wheel traction, carpet friction |
| NetworkTables publishing/subscribing | Real camera latency and frame drops |
| AdvantageScope 3D visualization | Mechanical binding, gear backlash |
| Auto path playback | Battery voltage sag under high current |
| Mode transitions (disabled → auto → teleop) | Real RoboRIO timing jitter |

**Rule of thumb:** If it doesn't work in sim, it won't work on the robot. If it works in sim, it *probably* works on the robot — but still test on hardware before trusting it.

---

## AdvantageScope and Logging

This repo uses AdvantageKit for all logging. AdvantageKit replaces the traditional SmartDashboard/logs combination with a unified system that records every signal to a `.wpilog` file and publishes to NetworkTables simultaneously.

### How Logging Is Configured

In `Robot.java`, the constructor branches on `Constants.MODE`:

```java
switch (Constants.MODE) {
  case REAL -> {
    Logger.addDataReceiver(new WPILOGWriter());    // writes .wpilog to RoboRIO
    Logger.addDataReceiver(new NT4Publisher());     // publishes to NetworkTables
  }
  case SIM -> {
    Logger.addDataReceiver(new NT4Publisher());     // NetworkTables only
  }
  case REPLAY -> {
    setUseTiming(false);                            // use log timestamps, not wall clock
    String logPath = LogFileUtil.findReplayLog();
    Logger.setReplaySource(new WPILOGReader(logPath)); // read old log
    Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim"))); // write sim overlay
  }
}
Logger.start();
```

- **REAL mode** (match day): every `Logger.recordOutput()` call writes to a `.wpilog` file on the RoboRIO flash AND publishes to NetworkTables. The log file survives robot restarts and can be downloaded for post-match analysis.
- **SIM mode** (desktop simulation): publishes to NetworkTables only. No `.wpilog` file is written during normal sim runs (since sim is fast to restart, you usually just re-run instead of replaying).
- **REPLAY mode** (post-match analysis): reads an existing `.wpilog` file as its source of truth. All `Logger.recordInput()` values come from the log, so the simulation is deterministic — you get the exact same sensor readings every replay. The `_sim` suffix writer records simulated outputs alongside the replayed inputs for comparison.

To run a replay:
```bash
./gradlew replayWatch
```
Then place a `.wpilog` file at `src/main/deploy/.advantagekit/replay/log.wpilog`, or use the AdvantageScope replay UI to select a log file.

### Recording vs Publishing

Two methods, different purposes:

- `Logger.recordOutput("key", value)` — records a value to the log and publishes to NetworkTables. Use this for values you want to see in AdvantageScope or analyze later. Our code logs poses, motor currents, IMU data, hub shift state, and superstructure visualization.
- `Logger.recordInput("key", value)` — records sensor inputs. In REPLAY mode, these values are read from the log instead of live sensors, making replays deterministic.

### Using AdvantageScope

AdvantageScope is the companion desktop application to AdvantageKit. It connects to your robot's NetworkTables (or reads log files) and provides:

1. **Field view** — shows robot pose, auto start pose, and trajectory on a rendered field. Open via the `/Pose` NetworkTables entry.
2. **3D robot visualization** — shows the robot model from `ascope_assets/` with animated joints driven by `Visualization/Components` logged transforms. Turrets rotate, hoods tilt, ground pivot angles — all in real time.
3. **Polars** — time-series graphs of any logged signal. Plot motor currents, encoder positions, joystick inputs, or any `Logger.recordOutput()` key.
4. **Alerts** — displays warnings and errors published via the AdvantageKit alert system.
5. **System stats** — CPU usage, loop timing, memory, CAN status.

To open AdvantageScope:
- Launch it from the WPILib tools directory, or from your applications folder after WPILib install.
- Connect via NetworkTables by entering your robot's IP (real robot) or `127.0.0.1` (simulation).
- Or open a `.wpilog` file directly for offline analysis.

### Why Logging Matters

- **It gives you evidence.** Instead of "I thought the shooter ran," you can show the exact flywheel RPM over time.
- **It helps you debug without guessing.** If the robot didn't shoot, check the log: was the shoot command scheduled? Did the shooter reach speed? Was the indexer running?
- **It lets you inspect behavior after the run.** A match lasts 135 seconds. You can't observe everything live. The log captures everything so you can review afterward.

---

## Why This Matters for Real Team Tasks

Many first tasks do not involve writing a whole new subsystem. They involve understanding:

- **Where state lives** — subsystem fields hold the current state of mechanisms (positions, velocities, setpoints).
- **Where a control decision is made** — `Superstructure.periodic()` contains the teleop/auto logic tree that decides which setpoint to apply based on controller inputs and game state.
- **Where logs should be added** — any new debug value goes through `Logger.recordOutput()` or `Logger.recordInput()`.
- **How to observe whether the code path actually ran** — check AdvantageScope polars, check the field view, or add a one-off log line and re-run.

---

## Required Mech/Elec Callout

Software members should know the code is not acting on abstract variables. It is commanding hardware over CAN, reading sensors, and trying to move mechanisms with real constraints. Even in sim, keep asking "what hardware would this line affect on a robot?"

When you write `flywheelLeft.setVelocity(500 RPM)`, a Talon FX motor controller receives a CAN message and drives current through a Kraken X60 motor. When you read `getPigeon2().getYaw()`, you are reading an IMU angle over CANFD. When you call `groundPivot.setFeedforward(Volts.of(-0.4))`, you are applying a voltage offset to fight gravity on a pivot joint.

Understanding the hardware behind the abstraction makes you a better software engineer on this team. Talk to the mech and elec leads when your code touches a mechanism you haven't seen physically.

---

## Exercise

### Part A: Code walkthrough

1. **Open `Robot.java`** and explain what happens in:
   - The constructor — what are the three modes? What does each mode configure for logging?
   - `robotPeriodic()` — what runs every 20 ms? What is the last line and why is it critical?
   - `autonomousInit()` — how is the auto command obtained and scheduled? What else runs during auto?
   - `teleopInit()` — what happens to the auto command? Why?

2. **Open `RobotContainer.java`** and trace how the drivetrain default command is chosen:
   - What three drive profiles exist and when does each activate?
   - What happens when the operator holds shoot and the driver doesn't move?
   - How does the code decide between shooting and shuttling speed profiles?

3. **Open `Controllers.java`** and identify:
   - Which controller port is driver vs operator?
   - What does `SHOOTING` combine? What does `SHUTTLING` require?
   - What is the difference between a `Toggle` and an `Edge`?

### Part B: Simulation

4. Copy the sim config files to the project root:
   ```bash
   cd 971-second-robot-2026
   cp sim/simgui*.json .
   ```

5. Run the simulator from VS Code (Run and Debug → "Run Simulation"). Verify:
   - SimGui opens with a field view and dashboard.
   - You can drive with WASD + arrow keys (driver keyboard mapping).
   - The robot pose updates on the field as you drive.
   - Pressing the operator shoot key (right trigger mapping) causes the drivetrain to brake.

6. Open AdvantageScope and connect to `127.0.0.1`. Identify:
   - What controls the driver and operator inputs (check `sim/simgui-ds.json` or `Simulation Key Mapping.html`).
   - Where pose or telemetry changes can be seen (the field view, the `DriveState` NetworkTables topic).
   - What logs or visual outputs are available (check `Drive/Pose`, `Drive/IMU/`, `Drive/Currents/`, `Visualization/Components`, `HubShift/`).

### Part C: Trace a signal

7. Pick one logged signal — for example `Drive/Pose` — and trace it through the code:
   - Where is it recorded? (`CommandSwerveDrivetrain.periodic()` calls `Logger.recordOutput("Drive/Pose", getState().Pose)`)
   - Where does the value come from? (`getState().Pose` comes from the Phoenix swerve odometry, which fuses encoder readings and IMU data)
   - Where does it go? (AdvantageKit writes it to the log and publishes to NetworkTables; AdvantageScope reads it from NetworkTables and draws the robot on the field)

---

## Debugging Prompts

Use these when things go wrong during the exercise:

- **Sim runs but behavior is confusing.** Find the default command or trigger path first. The drivetrain default command in `RobotContainer.configureDrivetrain()` is almost always where driver control issues originate.
- **Controls do nothing.** Inspect `Controllers.java` before changing subsystem logic. The most common mistake is wiring a button to the wrong controller port or confusing `getAsBoolean()` with `toggled()` / edge detection.
- **A value changes but nothing visible happens.** Ask whether it is only logged (check `Logger.recordOutput`), only simulated (check if MapleSim consumes it), or actually consumed by a subsystem `periodic()` method.
- **SimGui doesn't show the field.** Check that the `simgui*.json` files are at the project root, not just in `sim/`. SimGui reads from the working directory.
- **AdvantageScope can't connect.** Verify the sim process is running first, then check that you're connecting to `127.0.0.1` on the default NetworkTables port (1735 for NT4).
- **The 3D robot model doesn't animate.** Check that `Visualization.periodic()` is being called (it is, from `Superstructure.periodic()`) and that the `ascope_assets/Robot_First/config.json` component positions match the `Transform3d` offsets in `Visualization.java`.

---

## Before Next Lesson

- Re-read the superstructure folder names so Week 3 is less overwhelming. Specifically look at `TurretLeft`, `TurretRight`, `HoodLeft`, `HoodRight`, `FlywheelLeft`, `FlywheelRight`, `GroundPivot`, `GroundRollers`, `RollerFloor`, `B2`, `Kicker`, and `ShooterHandler`.
- Be ready to talk about control loops, motor abstractions, and CTRE Phoenix structure (Talon FX, Kraken X60, CAN bus, PID + feedforward).
- Try driving the robot in sim and pressing every button on both controllers. Notice what happens and what doesn't — you'll understand why in the next lessons.

---

## Task for this lesson

Follow [Lesson 02 Task: Codebase + Simulator Investigation](https://github.com/frc971/training-2026/tree/main/tasks/lesson-02-codebase-sim-task).
