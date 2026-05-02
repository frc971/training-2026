package frc.robot.subsystems.pivot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.lib.motor.MotorIO;

public class PivotIOTalonFX implements MotorIO {
  private final TalonFX motor;
  private final DutyCycleOut percentRequest = new DutyCycleOut(0.0);
  private double appliedPercent = 0.0;

  public PivotIOTalonFX() {
    motor = new TalonFX(1);

    TalonFXConfiguration config = new TalonFXConfiguration();
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    motor.getConfigurator().apply(config);
  }

  @Override
  public void updateInputs(MotorIOInputs inputs) {
    // TODO: Read position in rotations and convert to degrees
    inputs.positionDeg = 0.0;

    // TODO: Read velocity in rotations per second and convert to degrees per second
    inputs.velocityDegPerSec = 0.0;

    inputs.appliedPercent = appliedPercent;
    inputs.connected = true;
  }

  @Override
  public void setPercentOutput(double percent) {
    // TODO: Use percentRequest.withOutput(percent) with motor.setControl(...)
    appliedPercent = percent;
  }
}
