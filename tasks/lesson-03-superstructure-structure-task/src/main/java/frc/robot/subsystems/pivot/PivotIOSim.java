package frc.robot.subsystems.pivot;

import edu.wpi.first.math.MathUtil;
import frc.robot.lib.motor.MotorIO;

public class PivotIOSim implements MotorIO {
  private static final double MAX_SPEED_DEG_PER_SEC = 180.0;

  private double appliedPercent = 0.0;
  private double positionDeg = 0.0;

  @Override
  public void updateInputs(MotorIOInputs inputs) {
    // TODO: Simulate velocity using the stored appliedPercent and MAX_SPEED_DEG_PER_SEC
    double velocityDegPerSec = 0.0;

    // TODO: Advance positionDeg forward using velocity and a 20 ms loop time
    positionDeg = MathUtil.clamp(positionDeg, -15.0, 75.0);

    inputs.positionDeg = positionDeg;
    inputs.velocityDegPerSec = velocityDegPerSec;
    inputs.appliedPercent = appliedPercent;
    inputs.connected = true;
  }

  @Override
  public void setPercentOutput(double percent) {
    // TODO: Store percent after clamping to [-1.0, 1.0]
    appliedPercent = 0.0;
  }
}
