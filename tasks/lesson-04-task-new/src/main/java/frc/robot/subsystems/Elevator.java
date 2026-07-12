package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import org.littletonrobotics.junction.Logger;

public class Elevator {
  private static final double MAX_STEP_METERS_PER_CYCLE = 0.03;

  private double targetMeters = 0.0;
  private double currentMeters = 0.0;

  public void setHeight(double targetMeters) {
    this.targetMeters = targetMeters;
  }

  public void stop() {
    targetMeters = currentMeters;
  }

  public double getTargetMeters() {
    return targetMeters;
  }

  public double getCurrentMeters() {
    return currentMeters;
  }

  public void periodic() {
    double error = targetMeters - currentMeters;
    currentMeters += MathUtil.clamp(error, -MAX_STEP_METERS_PER_CYCLE, MAX_STEP_METERS_PER_CYCLE);

    Logger.recordOutput("Elevator/TargetMeters", targetMeters);
    Logger.recordOutput("Elevator/CurrentMeters", currentMeters);
  }
}
