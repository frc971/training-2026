package frc.robot.subsystems.superstructure;

import static edu.wpi.first.units.Units.*;
import edu.wpi.first.math.MathUtil;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import edu.wpi.first.units.measure.*;
import frc.robot.lib.superstructure.*;

public class Turret extends AngularSubsystem {

  public Turret() {
    super(getMotorConfig());
    String name = "Turret";
  }

  public static MotorConfig getMotorConfig() {
    TalonFXConfiguration tc = new TalonFXConfiguration();

    // TODO: Fill in Motion Magic values, PID and feedforward gains
    tc.Slot0.kP = 100;
    tc.Slot0.kI = 0;
    tc.Slot0.kD = 0;
    tc.Slot0.kS = 0;
    tc.Slot0.kV = 0;
    tc.Slot0.kA = 0;
    tc.Slot0.kG = 0;
    int cruiseVelocity = 10;
    int acceleration = 10;

    return MotorConfig.builder()
     .NAME("Ground Pivot")
     .ID(14)
     .BUS(new CANBus("rio"))
     .LOG_UNIT(Degrees)
     .TALONFX_CONFIG(tc)
     .FOC(false)
     .build();


    // TODO: For PID, let kP = 100, kI = 0, and kD = 0. Let feedforward
    // gains = 0

    // TODO: For motion magic let cruise velocity = 10, acceleration = 10



  }

  @Override
  public void setPosition(Angle goalPosition) { 
    // TODO: Normalize the goal position to be within the range of -180 to 180 degrees and clamp
    // the goal position to the physical limits of the turret.
    double normalizedGoalPosition = goalPosition.in(Degrees);
    normalizedGoalPosition = MathUtil.inputModulus(normalizedGoalPosition,-180,180);
    Angle clampedAngle = Degrees.of(MathUtil.clamp(normalizedGoalPosition, -95, 95));
    
      super.setPositionVoltage(Degrees.of(45));
  }
}
