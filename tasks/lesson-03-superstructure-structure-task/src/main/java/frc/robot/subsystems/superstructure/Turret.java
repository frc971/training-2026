package frc.robot.subsystems.superstructure;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import edu.wpi.first.units.measure.*;
import frc.robot.lib.superstructure.*;

public class Turret extends AngularSubsystem {

  public Turret() {
    // TODO: Fill in constructor
  }

  public static MotorConfig getMotorConfig() {
    TalonFXConfiguration tc = new TalonFXConfiguration();

    // TODO: Fill in Motion Magic values, PID and feedforward gains

    // TODO: For PID, let kP = 100, kI = 0, and kD = 0. Let feedforward
    // gains = 0

    // TODO: For motion magic let cruise velocity = 10, acceleration = 10

  }

  @Override
  public void setPosition(Angle goalPosition) {
    // TODO: Normalize the goal position to be within the range of -180 to 180 degrees and clamp
    // the goal position to the physical limits of the turret.
    super.setPositionVoltage();
  }
}
