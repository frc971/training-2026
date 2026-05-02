package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.DriveInputHelper.DriveRequest;

public class Drivebase extends SubsystemBase {
  private DriveRequest lastRequest = new DriveRequest(0.0, 0.0, 0.0);

  public void drive(DriveRequest request) {
    lastRequest = request;
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Drive/XSpeed", lastRequest.xSpeed());
    SmartDashboard.putNumber("Drive/YSpeed", lastRequest.ySpeed());
    SmartDashboard.putNumber("Drive/RotSpeed", lastRequest.rotSpeed());
  }
}
