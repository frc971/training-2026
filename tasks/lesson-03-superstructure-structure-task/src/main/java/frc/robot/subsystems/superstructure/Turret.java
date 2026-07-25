package frc.robot.subsystems.superstructure;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.*;
import frc.robot.lib.superstructure.*;

public class Turret extends AngularSubsystem {

  public Turret() {
    // TODO: Fill in constructor
    super(getMotorConfig());
  }

  public static MotorConfig getMotorConfig() {
    TalonFXConfiguration tc = new TalonFXConfiguration();

    
    // TODO: Fill in Motion Magic values, PID and feedforward gains

    // TODO: For PID, let kP = 100, kI = 0, and kD = 0. Let feedforward
    // gains = 0
    tc.Slot0.kS = 0;
    tc.Slot0.kV = 0;
    tc.Slot0.kA = 0;
    tc.Slot0.kG = 0;
    
    tc.Slot0.kP = 100;
    tc.Slot0.kI = 0;
    tc.Slot0.kD = 0;

    // TODO: For motion magic let cruise velocity = 10, acceleration = 10
    tc.MotionMagic.MotionMagicCruiseVelocity = 10;
    tc.MotionMagic.MotionMagicAcceleration = 10;
    tc.MotionMagic.MotionMagicJerk = 0;

    return MotorConfig.builder()
      .NAME("Turret")
      .ID(0)
      .BUS(new CANBus("rio"))
      .TALONFX_CONFIG(tc)
      .FOC(false)
      .build();
    

  }

  @Override
  public void setPosition(Angle goalPosition) {
    // TODO: Normalize the goal position to be within the range of -180 to 180 degrees and clamp
    // the goal position to the physical limits of the turret.
    Angle normalized = Degrees.of(MathUtil.inputModulus(goalPosition.in(Degree), -180, 180));
    double degnorm = normalized.in(Degrees);
    Angle clamped = Degrees.of(MathUtil.clamp(degnorm, -95, 95));
    
    super.setPositionVoltage(clamped);
    SmartDashboard.putNumber("Turret Angle", clamped.in(Degrees));
    SmartDashboard.putNumber("Input Degrees", degnorm);
    SmartDashboard.putNumber("raw", goalPosition.in(Degrees));
  }

  
}
