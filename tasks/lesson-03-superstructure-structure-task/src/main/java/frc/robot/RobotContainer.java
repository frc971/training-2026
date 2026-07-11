package frc.robot;

import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.subsystems.superstructure.Turret;
import edu.wpi.first.wpilibj2.command.Commands;
import static edu.wpi.first.units.Units.Degrees;

public class RobotContainer {
  private final CommandJoystick joystick = new CommandJoystick(0);

  private final Turret turret = new Turret();

  public RobotContainer() {
    Turret turret = new Turret();
    // TODO: Add Turret

    configureBindings();
  }

  private void configureBindings() {
    joystick.button(1).onTrue(Commands.runOnce(() -> turret.setPosition(Degrees.of(45))));
    joystick.button(1).onTrue(Commands.runOnce(() -> turret.setPosition(Degrees.of(200))));

  }
}
