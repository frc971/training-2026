package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pivot extends SubsystemBase {
  private static final double STEP_DEGREES_PER_CYCLE = 2.0;
  private static final double AT_TARGET_TOLERANCE_DEG = 1.5;

  private double currentAngleDegrees = 0.0;
  private double targetAngleDegrees = 0.0;

  public void setTargetAngleDegrees(double targetAngleDegrees) {
    this.targetAngleDegrees = targetAngleDegrees;
  }

  public double getCurrentAngleDegrees() {
    return currentAngleDegrees;
  }

  public double getTargetAngleDegrees() {
    return targetAngleDegrees;
  }

  public boolean atTarget() {
    return Math.abs(targetAngleDegrees - currentAngleDegrees) <= AT_TARGET_TOLERANCE_DEG;
  }

  @Override
  public void periodic() {
    // TODO: Publish current angle, target angle, and atTarget() to SmartDashboard
    SmartDashboard.putNumber("Pivot Current Angle", currentAngleDegrees);
    SmartDashboard.putNumber("Pivot Target Angle", targetAngleDegrees);
    SmartDashboard.putBoolean("Pivot At Target", atTarget());
  
  }

  @Override
  public void simulationPeriodic() {
    currentAngleDegrees +=
        PivotHelper.computeStep(currentAngleDegrees, targetAngleDegrees, STEP_DEGREES_PER_CYCLE);
    currentAngleDegrees = MathUtil.clamp(currentAngleDegrees, -15, 15);
    
  } 
}
