package frc.robot.subsystems.superstructure;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.*;
import frc.robot.lib.superstructure.*;
import edu.wpi.first.math.MathUtil;


public class Turret extends AngularSubsystem {
    public static final Angle LOWER_LIMIT = Degrees.of(-180);
    public static final Angle UPPER_LIMIT = Degrees.of(180);

  public Turret() {
    // TODO: Fill in constructor
    super(getMotorConfig());
  }

  public static MotorConfig getMotorConfig() {
    TalonFXConfiguration tc = new TalonFXConfiguration();


    tc.Slot0.kP = 100;
    tc.Slot0.kI = 0;
    tc.Slot0.kD = 0;

    // TODO: Fill in Motion Magic values, PID and feedforward gains

    // TODO: For PID, let kP = 100, kI = 0, and kD = 0. Let feedforward
    // gains = 0

    // TODO: For motion magic let cruise velocity = 10, acceleration = 10
    tc.MotionMagic.MotionMagicCruiseVelocity = 10;
    tc.MotionMagic.MotionMagicAcceleration = 10.0;
    tc.MotionMagic.MotionMagicJerk = 0.0;

    return MotorConfig.builder()
     .NAME("Ground Pivot")
     .ID(14)
     .BUS(new CANBus("rio"))
     .LOG_UNIT(Degrees)
     .TALONFX_CONFIG(tc)
     .FOC(false)
     .build();

   

  }

  @Override
  public void setPosition(Angle goalPosition) {
    Angle normalizedGoal = Radians.of(MathUtil.angleModulus(goalPosition.in(Radians)));
    Angle clampedGoal = Degrees.of(
        MathUtil.clamp(normalizedGoal.in(Degrees), LOWER_LIMIT.in(Degrees), UPPER_LIMIT.in(Degrees)));

    super.setPositionVoltage(clampedGoal);
  }
}
