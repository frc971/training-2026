package frc.robot.subsystems.superstructure;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.*;
import frc.robot.lib.superstructure.*;

public class Turret extends AngularSubsystem {

  public static final Angle LOWER_LIMIT = Degrees.of(-95);
  public static final Angle UPPER_LIMIT = Degrees.of(95);

  public Turret() {
    // TODO: Fill in constructor
    super(getMotorConfig());
  }

  public static MotorConfig getMotorConfig() {
    TalonFXConfiguration tc = new TalonFXConfiguration();

    // TODO: Fill in Motion Magic values, PID and feedforward gains
    tc.Slot0.kP = 100.0;
    tc.Slot0.kI = 0.0;
    tc.Slot0.kD = 0.0;

    // TODO: For PID, let kP = 100, kI = 0, and kD = 0. Let feedforward
    // gains = 0

    // TODO: For motion magic let cruise velocity = 10, acceleration = 10
    tc.MotionMagic.MotionMagicCruiseVelocity = 10.0;
    tc.MotionMagic.MotionMagicAcceleration = 10.0;
    tc.MotionMagic.MotionMagicJerk = 0.0;

    return MotorConfig.builder()
        .NAME("turret")
        .ID(21)
        .BUS(new CANBus("rio"))
        .LOG_UNIT(Degrees)
        .TALONFX_CONFIG(tc)
        .FOC(true)
        .build();
  }

  @Override
  public void setPosition(Angle goalPosition) {
    // TODO: Normalize the goal position to be within the range of -180 to 180 degrees and clamp
    // the goal position to the physical limits of the turret.
    double goalDegree = goalPosition.in(Degrees);
    goalDegree = MathUtil.inputModulus(goalDegree, -180.0, 180.0);
    goalDegree = MathUtil.clamp(goalDegree, LOWER_LIMIT.in(Degrees), UPPER_LIMIT.in(Degrees));
    System.out.println("Turret goal: " + goalDegree);
    super.setPosition(Degrees.of(goalDegree));
  }
}
