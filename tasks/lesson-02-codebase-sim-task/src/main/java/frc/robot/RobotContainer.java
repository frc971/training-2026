// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.subsystems.Pivot;

public class RobotContainer {
  // Subsystems
  private final Pivot pivot = new Pivot();

  // Controller
  private final CommandJoystick joystick = new CommandJoystick(0);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    // TODO: Bind button 1 to pivot.setTargetAngleDegrees(35.0)
    // TODO: Bind button 2 to pivot.setTargetAngleDegrees(0.0)
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
