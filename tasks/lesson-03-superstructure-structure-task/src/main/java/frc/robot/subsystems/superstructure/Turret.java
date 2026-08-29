package frc.robot.subsystems.superstructure;

import static edu.wpi.first.units.Units.Degrees;

import com.ctre.phoenix6.configs.TalonFXConfiguration;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.Angle;
import frc.robot.lib.superstructure.AngularSubsystem;
import frc.robot.lib.superstructure.MotorConfig;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turret extends AngularSubsystem {
  public static final Angle LOWER_LIMIT = Degrees.of(-95.0);
  public static final Angle UPPER_LIMIT = Degrees.of(95.0);

  public Turret() {
    super(getMotorConfig());
  }

  public static MotorConfig getMotorConfig() {
    TalonFXConfiguration tc = new TalonFXConfiguration();

    // Fill in Motion Magic values, PID and feedforward gains

    // For PID, let kP = 100, kI = 0, and kD = 0. Let feedforward
    // gains = 0
    tc.Slot0.kS = 0.0;
    tc.Slot0.kV = 0.0;
    tc.Slot0.kA = 0.0;
    tc.Slot0.kG = 0.0;

    tc.Slot0.kP = 100.0;  
    tc.Slot0.kI = 0.0;  
    tc.Slot0.kD = 0.0;  

    // For motion magic let cruise velocity = 10, acceleration = 10
    tc.MotionMagic.MotionMagicCruiseVelocity = 10.0;
    tc.MotionMagic.MotionMagicAcceleration = 10.0;
    tc.MotionMagic.MotionMagicJerk = 0.0;

    return MotorConfig.builder()
        .NAME("Turret")
        .ID(1)
        .LOG_UNIT(Degrees)
        .TALONFX_CONFIG(tc)
        .build();
  }

  @Override
  public void setPosition(Angle goalPosition) {
    // TODO: Normalize the goal position to be within the range of -180 to 180 degrees and clamp
    // the goal position to the physical limits of the turret.
    double normalizedGoal = MathUtil.inputModulus(goalPosition.in(Degrees), -180, 180);
    double clampedGoal = MathUtil.clamp(normalizedGoal, LOWER_LIMIT.in(Degrees), UPPER_LIMIT.in(Degrees));

    super.setPositionVoltage(Degrees.of(clampedGoal));
    SmartDashboard.putNumber("Clamped Goal", clampedGoal);
    SmartDashboard.putNumber("Normalized Goal", normalizedGoal);
    SmartDashboard.putNumber("Not Normalized Goal", goalPosition.in(Degrees));
  }
}
