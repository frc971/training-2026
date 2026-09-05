package frc.robot.subsystems.superstructure;

import static edu.wpi.first.units.Units.Degrees;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.lib.superstructure.AngularSubsystem;
import frc.robot.lib.superstructure.MotorConfig;

public class Turret extends AngularSubsystem {
  public static final Angle LOWER_LIMIT = Degrees.of(-95);
 public static final Angle UPPER_LIMIT = Degrees.of(95); 

  public Turret() {
    // TODO: Fill in constructor
    super(getMotorConfig());
  }

  public static MotorConfig getMotorConfig() {
    TalonFXConfiguration tc = new TalonFXConfiguration();


    //PID
      tc.Slot0.kP = 100.0;
      tc.Slot0.kI = 0.0;
      tc.Slot0.kD = 0.0;

    //Feedforward 
      tc.Slot0.kS = 0.0;
      tc.Slot0.kV = 0.0;
      tc.Slot0.kA = 0.0;
      tc.Slot0.kG = 0.0;

    // Motion Magic
      tc.MotionMagic.MotionMagicCruiseVelocity = 10.0;
      tc.MotionMagic.MotionMagicAcceleration = 10.0;
      tc.MotionMagic.MotionMagicJerk = 0.0;


      tc.Feedback.SensorToMechanismRatio = 1.0; 

    return MotorConfig.builder()
        .NAME("Turret")
        .ID(1)
        .BUS(new CANBus("rio"))
        .LOG_UNIT(Degrees)
        .TALONFX_CONFIG(tc)
        .FOC(false)
        .build();

  }

  @Override
  public void setPosition(Angle goalPosition) {
    // TODO: Normalize the goal position to be within the range of -180 to 180 degrees and clamp



    double normalizedDeg = MathUtil.inputModulus(
        goalPosition.in(Degrees), 
        -180.0, 
        180.0
    );

   
    double clampedDeg = MathUtil.clamp(
        normalizedDeg, 
        LOWER_LIMIT.in(Degrees), 
        UPPER_LIMIT.in(Degrees)
    );


    //super.setPosition(Degrees.of(clampedDeg));
   super.setPositionVoltage(Degrees.of(clampedDeg));
    SmartDashboard.putNumber("Clamped Deg", clampedDeg);
    SmartDashboard.putNumber("Normalized Deg", normalizedDeg);
    SmartDashboard.putNumber("Not Normalized Deg", goalPosition.in(Degrees));

   
  }
}
