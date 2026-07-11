package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import org.littletonrobotics.junction.Logger;

public class Arm {
  private static final double MAX_STEP_DEGREES_PER_CYCLE = 2.0;

  private double targetDegrees = 0.0;
  private double currentDegrees = 0.0;

  public void setPosition(double targetDegrees) {
    this.targetDegrees = targetDegrees;
  }

  public void stop() {
    targetDegrees = currentDegrees;
  }

  public double getTargetDegrees() {
    return targetDegrees;
  }

  public double getCurrentDegrees() {
    return currentDegrees;
  }

  public void periodic() {
    double error = targetDegrees - currentDegrees;
    currentDegrees +=
        MathUtil.clamp(error, -MAX_STEP_DEGREES_PER_CYCLE, MAX_STEP_DEGREES_PER_CYCLE);

    Logger.recordOutput("Arm/TargetDegrees", targetDegrees);
    Logger.recordOutput("Arm/CurrentDegrees", currentDegrees);
  }
}
