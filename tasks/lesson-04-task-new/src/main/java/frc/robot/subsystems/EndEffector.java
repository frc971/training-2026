package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import org.littletonrobotics.junction.Logger;

public class EndEffector {
  private static final double MAX_STEP_VOLTS_PER_CYCLE = 0.4;

  private double targetVolts = 0.0;
  private double appliedVolts = 0.0;

  public void setVoltage(double targetVolts) {
    this.targetVolts = MathUtil.clamp(targetVolts, -12.0, 12.0);
  }

  public void stop() {
    targetVolts = 0.0;
  }

  public double getTargetVolts() {
    return targetVolts;
  }

  public double getAppliedVolts() {
    return appliedVolts;
  }

  public void periodic() {
    double error = targetVolts - appliedVolts;
    appliedVolts += MathUtil.clamp(error, -MAX_STEP_VOLTS_PER_CYCLE, MAX_STEP_VOLTS_PER_CYCLE);

    Logger.recordOutput("EndEffector/TargetVolts", targetVolts);
    Logger.recordOutput("EndEffector/AppliedVolts", appliedVolts);
  }
}
