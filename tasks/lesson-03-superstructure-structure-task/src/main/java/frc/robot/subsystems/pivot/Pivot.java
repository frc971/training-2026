package frc.robot.subsystems.pivot;

import frc.robot.lib.motor.MotorIO;
import frc.robot.lib.motor.MotorSubsystem;

public class Pivot extends MotorSubsystem {
  private static final double DEPLOY_PERCENT = 0.30;
  private static final double STOW_PERCENT = -0.30;

  public Pivot(MotorIO io) {
    super("Pivot", io);
  }

  public void moveTowardDeployed() {
    setPercentOutput(DEPLOY_PERCENT);
  }

  public void moveTowardStowed() {
    setPercentOutput(STOW_PERCENT);
  }

  public void stop() {
    setPercentOutput(0.0);
  }
}
