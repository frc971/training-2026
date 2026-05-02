package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import java.util.List;

public class Autos {
  public record AutoRoutine(String displayLabel, List<String> pathNames) {}

  private final Drivebase drivebase;
  private final SendableChooser<AutoRoutine> chooser = new SendableChooser<>();

  private AutoRoutine cachedRoutine = null;
  private List<PathSegment> cachedSegments = List.of();
  private Pose2d cachedStartPose = new Pose2d();

  public Autos(Drivebase drivebase) {
    this.drivebase = drivebase;

    chooser.setDefaultOption("None", null);
    for (AutoRoutine routine : AUTO_ROUTINES) {
      chooser.addOption(routine.displayLabel(), routine);
    }

    SmartDashboard.putData("Auto Mode", chooser);
  }

  public Command getAutonomousCommand() {
    updateSelectedAutoCache();

    if (cachedSegments.isEmpty()) {
      return Commands.none();
    }

    return Commands.sequence(cachedSegments.stream().map(PathSegment::asCommand).toArray(Command[]::new));
  }

  public void updatePreview() {
    updateSelectedAutoCache();
    drivebase.setPreviewPose(getAutonomousStartPose());
    SmartDashboard.putString(
        "Auto/SelectedName", cachedRoutine == null ? "None" : cachedRoutine.displayLabel());
  }

  public Pose2d getAutonomousStartPose() {
    // TODO: Return the cached start pose for the currently selected auto
    return new Pose2d();
  }

  private void updateSelectedAutoCache() {
    AutoRoutine selected = chooser.getSelected();

    if (selected == cachedRoutine) {
      return;
    }

    cachedRoutine = selected;

    if (selected == null) {
      cachedSegments = List.of();
      cachedStartPose = new Pose2d();
      return;
    }

    cachedSegments = selected.pathNames().stream().map(PathSegment::new).toList();
    cachedStartPose = cachedSegments.isEmpty() ? new Pose2d() : cachedSegments.get(0).getStartPose();
  }

  // TODO: Add one new auto routine using the provided path names in src/main/deploy/autos/paths
  public static final List<AutoRoutine> AUTO_ROUTINES =
      List.of(
          new AutoRoutine("Sprint", List.of("StartToMid", "MidToScore")),
          new AutoRoutine("Sweep", List.of("StartToRight", "RightToScore")));
}
