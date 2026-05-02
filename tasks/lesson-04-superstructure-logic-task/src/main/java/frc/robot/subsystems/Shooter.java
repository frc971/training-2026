package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  private static final double MAX_STEP_RPM_PER_CYCLE = 250.0;

  private double targetRpm = 0.0;
  private double currentRpm = 0.0;

  public void spinTo(double targetRpm) {
    this.targetRpm = targetRpm;
  }

  public void stop() {
    targetRpm = 0.0;
  }

  public double getTargetRpm() {
    return targetRpm;
  }

  public double getCurrentRpm() {
    return currentRpm;
  }

  @Override
  public void periodic() {
    double error = targetRpm - currentRpm;
    currentRpm += MathUtil.clamp(error, -MAX_STEP_RPM_PER_CYCLE, MAX_STEP_RPM_PER_CYCLE);

    SmartDashboard.putNumber("Shooter/TargetRPM", targetRpm);
    SmartDashboard.putNumber("Shooter/CurrentRPM", currentRpm);
  }
}
