// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {
  private boolean feeding = false;

  public void feed() {
    feeding = true;
  }

  public void stop() {
    feeding = false;
  }

  public boolean isFeeding() {
    return feeding;
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Indexer/Feeding", feeding);
  }

  @Override
  public void simulationPeriodic() {
    // Nothing extra needed for this simple task.
  }
}
