package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.lib.superstructure.Toggle;
import frc.robot.subsystems.Pathing.Goal;
import org.littletonrobotics.junction.Logger;

public class Superstructure {
  private final Arm arm = new Arm();
  private final EndEffector endEffector = new EndEffector();
  private final Elevator elevator = new Elevator();
  private final Pathing pathing = new Pathing(arm, endEffector, elevator);
  private final Joystick joystick = new Joystick(0);
  private final Toggle pathingToggle = new Toggle(new Trigger(() -> joystick.getRawButton(1)));

  public void setGoal(Goal goal) {
    pathing.setGoal(goal);
  }

  public Goal getGoal() {
    return pathing.getGoal();
  }

  public void reset() {
    pathingToggle.reset();
    setGoal(Goal.NOT_ACTIVE);
  }

  public void periodic() {
    updateKeybinds();

    pathing.periodic();

    arm.periodic();
    endEffector.periodic();
    elevator.periodic();

    Logger.recordOutput("Superstructure/Goal", getGoal().name());
    Logger.recordOutput("Superstructure/PathingToggle", pathingToggle.toggled());
  }

  private void updateKeybinds() {
    pathingToggle.periodic();

    // TODO: Use pathingToggle.toggled() to set the Pathing goal.
    // When the toggle is true, the goal should be ACTIVE.
    // When the toggle is false, the goal should be NOT_ACTIVE.
  }
}
