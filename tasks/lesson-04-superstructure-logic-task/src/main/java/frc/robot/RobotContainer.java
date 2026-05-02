// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SuperstructureCoordinator;

public class RobotContainer {
  private final Shooter shooter = new Shooter();
  private final Indexer indexer = new Indexer();
  private final SuperstructureCoordinator superstructure =
      new SuperstructureCoordinator(shooter, indexer);

  private final CommandJoystick joystick = new CommandJoystick(0);

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    joystick
        .button(1)
        .whileTrue(Commands.run(superstructure::requestShoot, superstructure))
        .onFalse(Commands.runOnce(superstructure::stopAll, superstructure));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
