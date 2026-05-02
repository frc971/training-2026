package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivebase extends SubsystemBase {
  private Pose2d previewPose = new Pose2d();

  public void setPreviewPose(Pose2d pose) {
    previewPose = pose;
  }

  @Override
  public void periodic() {
    SmartDashboard.putString("Auto/PreviewPose", previewPose.toString());
  }
}
