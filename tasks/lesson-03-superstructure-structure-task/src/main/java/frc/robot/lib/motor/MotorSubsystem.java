package frc.robot.lib.motor;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.lib.motor.MotorIO.MotorIOInputs;

public class MotorSubsystem extends SubsystemBase {
  protected final MotorIO io;
  protected final MotorIOInputs inputs = new MotorIOInputs();
  private final String name;

  public MotorSubsystem(String name, MotorIO io) {
    this.name = name;
    this.io = io;
  }

  public void setPercentOutput(double percent) {
    io.setPercentOutput(percent);
  }

  public double getPositionDeg() {
    return inputs.positionDeg;
  }

  public double getVelocityDegPerSec() {
    return inputs.velocityDegPerSec;
  }

  public boolean isConnected() {
    return inputs.connected;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    SmartDashboard.putNumber(name + "/PositionDeg", inputs.positionDeg);
    SmartDashboard.putNumber(name + "/VelocityDegPerSec", inputs.velocityDegPerSec);
    SmartDashboard.putNumber(name + "/AppliedPercent", inputs.appliedPercent);
    SmartDashboard.putBoolean(name + "/Connected", inputs.connected);
  }
}
