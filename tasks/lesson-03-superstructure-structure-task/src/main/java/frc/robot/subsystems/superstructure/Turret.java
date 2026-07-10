package frc.robot.subsystems.superstructure;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import edu.wpi.first.units.measure.*;
import frc.robot.lib.superstructure.*;

public class Turret extends AngularSubsystem {

  public Turret() {
    super(getMotorConfig());
  }

  public static MotorConfig getMotorConfig() {
    TalonFXConfiguration tc = new TalonFXConfiguration();

    
    tc.Slot0.kS = 0;
    tc.Slot0.kV = 0;
    tc.Slot0.kA = 0;
    tc.Slot0.kG = 0;

    
    tc.Slot0.kP = 100.0;
    tc.Slot0.kI = 0.0;
    tc.Slot0.kD = 0.0;

    tc.MotionMagic.MotionMagicCruiseVelocity = 10;
    tc.MotionMagic.MotionMagicAcceleration = 10;
    

  }

  @Override
  public void setPosition(Angle goalPosition) {
     double angle = goalPosition.in(Degrees);
    if (angle > 180) {
        angle -= 360;
    }
    if (angle > 95) {
        angle = 95;
    }
    if (angle < -95) {
        angle = -95;
    }
    super.setPositionVoltage();
  }
}
