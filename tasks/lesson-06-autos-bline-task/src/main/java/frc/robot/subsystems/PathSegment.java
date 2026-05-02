package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PathSegment {
  private final String name;
  private final Pose2d startPose;
  private final double durationSeconds;

  public PathSegment(String name) {
    this.name = name;

    try {
      Path file =
          Filesystem.getDeployDirectory().toPath().resolve("autos/paths").resolve(name + ".path");
      List<String> lines = Files.readAllLines(file);

      String[] poseParts = lines.get(0).replace("startPose=", "").split(",");
      startPose =
          new Pose2d(
              Double.parseDouble(poseParts[0]),
              Double.parseDouble(poseParts[1]),
              Rotation2d.fromDegrees(Double.parseDouble(poseParts[2])));
      durationSeconds = Double.parseDouble(lines.get(1).replace("durationSeconds=", ""));
    } catch (IOException e) {
      throw new RuntimeException("Failed to load path segment " + name, e);
    }
  }

  public String getName() {
    return name;
  }

  public Pose2d getStartPose() {
    return startPose;
  }

  public Command asCommand() {
    return Commands.sequence(
        Commands.print("Following path segment: " + name),
        Commands.waitSeconds(durationSeconds));
  }
}
