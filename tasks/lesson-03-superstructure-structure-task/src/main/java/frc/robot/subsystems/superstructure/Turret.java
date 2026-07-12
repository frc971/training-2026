package frc.robot.subsystems.superstructure;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.*;
import frc.robot.lib.superstructure.*;

public class Turret extends AngularSubsystem {
  public static Angle LOWER_LIMIT = Degrees.of(-180);
  public static Angle UPPER_LIMIT = Degrees.of(180);
  
  public Turret() {
    super(getMotorConfig());
  }

  public static MotorConfig getMotorConfig() {
    TalonFXConfiguration tc = new TalonFXConfiguration();

    // TODO: Fill in Motion Magic values, PID and feedforward gains

    // TODO: For PID, let kP = 100, kI = 0, and kD = 0. Let feedforward
    // gains = 0
    
    tc.Slot0.kP = 100;
    tc.Slot0.kI = 0;
    tc.Slot0.kD =0;
    // TODO: For motion magic let cruise velocity = 10, acceleration = 10
    tc.MotionMagic.MotionMagicCruiseVelocity = 10;
    tc.MotionMagic.MotionMagicAcceleration = 10;
    tc.MotionMagic.MotionMagicJerk = 0;

    return MotorConfig.builder()
        .NAME("Turret")
        .ID(1)
        .LOG_UNIT(Degrees)
        .BUS(new CANBus("rio"))
        .TALONFX_CONFIG(tc)
        .FOC(false)
        .build();


    
  }

  @Override
  public void setPosition(Angle goalPosition) {
    // TODO: Normalize the goal position to be within the range of -180 to 180 degrees and clamp
    // the goal position to the physical limits of the turret.
    double targetDegrees = goalPosition.in(Degrees);
    targetDegrees = MathUtil.inputModulus(goalPosition.in(Degrees),-180,180);
    targetDegrees = MathUtil.clamp(targetDegrees, LOWER_LIMIT.in(Degrees), UPPER_LIMIT.in(Degrees));
    




    
    super.setPositionVoltage(Degrees.of(targetDegrees));
  }
}
