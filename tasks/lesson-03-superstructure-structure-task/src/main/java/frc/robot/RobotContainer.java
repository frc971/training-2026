package frc.robot;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.subsystems.superstructure.Turret;
import static edu.wpi.first.units.Units.Degrees;

public class RobotContainer {
  private final CommandJoystick joystick = new CommandJoystick(0);

  public Turret turret;

  public RobotContainer() {
    turret = new Turret();

    configureBindings();
  }

  private void configureBindings() {
    joystick.button(1).onTrue(Commands.runOnce(() -> turret.setPosition(Degrees.of(45))));
     joystick.button(2).onTrue(Commands.runOnce(() -> turret.setPosition(Degrees.of(200))));
    // TODO: When button 1 is pressed turret moves to 45 deg
    // TODO: When button 2 is pressed turret moves to 200 deg
  }
}
