package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.subsystems.pivot.Pivot;
import frc.robot.subsystems.pivot.PivotIOSim;
import frc.robot.subsystems.pivot.PivotIOTalonFX;

public class RobotContainer {
  private final CommandJoystick joystick = new CommandJoystick(0);

  // private final Pivot pivot;

  public RobotContainer() {
    // TODO: Use real or sim PivotIO
    Pivot pivot = new Pivot(RobotBase.isReal() ? new PivotIOTalonFX() : new PivotIOSim());

    configureBindings();
  }

  private void configureBindings() {
    // TODO: While holding button 1, move toward deployed
    // TODO: While holding button 2, move toward stowed
    // TODO: When each button is released, stop the pivot
    /*
    joystick
        .button(1)
        .whileTrue(...)
        .onFalse(...);
    */
  }
}
