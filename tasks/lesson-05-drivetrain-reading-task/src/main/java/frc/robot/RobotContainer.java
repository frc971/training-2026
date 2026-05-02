// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.subsystems.Drivebase;
import frc.robot.util.DriveInputHelper;

public class RobotContainer {
  private final Drivebase drivebase = new Drivebase();
  private final CommandJoystick joystick = new CommandJoystick(0);
  private final DriveInputHelper driveInputHelper = new DriveInputHelper();

  public RobotContainer() {
    drivebase.setDefaultCommand(
        Commands.run(
            () ->
                drivebase.drive(
                    driveInputHelper.process(
                        -joystick.getY(),
                        -joystick.getX(),
                        -joystick.getZ(),
                        joystick.button(1).getAsBoolean())),
            drivebase));

    configureBindings();
  }

  private void configureBindings() {}

  public Command getAutonomousCommand() {
    return null;
  }
}
