package frc.robot;

import static edu.wpi.first.units.Units.Degrees;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.subsystems.superstructure.Turret;

public class RobotContainer {
  private final CommandJoystick joystick = new CommandJoystick(0);

  // TODO: Add Turret
  private final Turret turret = new Turret();

  public RobotContainer() {
    // TODO: Add Turret

    configureBindings();
  }

  private void configureBindings() {
    // TODO: When button 1 is pressed turret moves to 45 deg
    joystick.button(1).onTrue(Commands.runOnce(() -> turret.setPosition(Degrees.of(45))));
    // TODO: When button 2 is pressed turret moves to 200 deg
    joystick.button(2).onTrue(Commands.runOnce(() -> turret.setPosition(Degrees.of(200))));
  }
}
