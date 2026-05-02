package frc.robot.lib.motor;

public interface MotorIO {
  class MotorIOInputs {
    public double positionDeg = 0.0;
    public double velocityDegPerSec = 0.0;
    public double appliedPercent = 0.0;
    public boolean connected = false;
  }

  void updateInputs(MotorIOInputs inputs);

  void setPercentOutput(double percent);
}
