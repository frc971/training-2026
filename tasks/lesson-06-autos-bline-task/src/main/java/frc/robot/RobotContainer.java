// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Autos;
import frc.robot.subsystems.Drivebase;

public class RobotContainer {
  private final Drivebase drivebase = new Drivebase();
  private final Autos autos = new Autos(drivebase);

  public void disabledPeriodic() {
    autos.updatePreview();
  }

  public Command getAutonomousCommand() {
    return autos.getAutonomousCommand();
  }
}
