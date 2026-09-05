package frc.robot;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.subsystems.superstructure.Turret;

public class RobotContainer {
  private final CommandJoystick joystick = new CommandJoystick(0);

  // Declare Turret subsystem
  private final Turret turret;

  public RobotContainer() {
    // Instantiate Turret subsystem
    turret = new Turret();

    configureBindings();
  }

  private void configureBindings() {
   joystick.button(1).onTrue(Commands.runOnce(() -> turret.setPosition(Degrees.of(45))));
    joystick.button(2).onTrue(Commands.runOnce(() -> turret.setPosition(Degrees.of(200))));
  }
}