package frc.robot;

import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.lib.superstructure.MotorConfig;
import frc.robot.subsystems.superstructure.Turret;
import static edu.wpi.first.units.Units.Degrees;
import edu.wpi.first.wpilibj2.command.Commands;

public class RobotContainer {
  private final CommandJoystick joystick = new CommandJoystick(0);

  private Turret turret;

  public RobotContainer() {
    turret = new Turret();

    configureBindings();
  }

  private void configureBindings() {
    joystick.button(1).onTrue(Commands.runOnce(() -> turret.setPosition(Degrees.of(45))));
    joystick.button(2).onTrue(Commands.runOnce(() -> turret.setPosition(Degrees.of(200))));
  }
}
