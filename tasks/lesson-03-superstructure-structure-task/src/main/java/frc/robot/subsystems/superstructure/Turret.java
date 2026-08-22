package frc.robot.subsystems.superstructure;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.*;
import frc.robot.lib.superstructure.*;

public class Turret extends AngularSubsystem {
  public static final Angle LOWER_LIMIT = Degrees.of(-95);
  public static final Angle UPPER_LIMIT = Degrees.of(95);

  public Turret() {
    super(getMotorConfig());
  }

  public static MotorConfig getMotorConfig() {
    TalonFXConfiguration tc = new TalonFXConfiguration();

    tc.Slot0.kP = 100;
    tc.Slot0.kI = 0;
    tc.Slot0.kD = 0;
    tc.Slot0.kS = 0;
    tc.Slot0.kV = 0;
    tc.Slot0.kA = 0;
    tc.Slot0.kG = 0;

    tc.MotionMagic.MotionMagicCruiseVelocity = 10;
    tc.MotionMagic.MotionMagicAcceleration = 10;

    tc.Feedback.SensorToMechanismRatio = 1;

    return MotorConfig.builder()
        .NAME("Turret")
        .ID(1)
        .LOG_UNIT(Degrees)
        .TALONFX_CONFIG(tc)
        .build();
  }

  @Override
  public void setPosition(Angle goalPosition) {
    double normalizedDegrees = MathUtil.inputModulus(goalPosition.in(Degrees), -180, 180);
    double clampedDegrees =
        MathUtil.clamp(normalizedDegrees, LOWER_LIMIT.in(Degrees), UPPER_LIMIT.in(Degrees));
    super.setPositionVoltage(Degrees.of(clampedDegrees));
  }
}
