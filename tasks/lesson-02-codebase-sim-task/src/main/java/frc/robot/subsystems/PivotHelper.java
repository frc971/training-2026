package frc.robot.subsystems;

/**
 * Helper for pivot motion calculations. Computes a per-cycle step toward a target angle, with
 * gravity compensation so the pivot holds position when the error is small.
 */
public final class PivotHelper {
  private PivotHelper() {}

  /**
   * Computes the angle adjustment (in degrees) to apply this cycle. Clamps the raw error to {@code
   * maxStep} so the pivot never jumps more than one step per cycle. When the error is smaller than
   * {@code maxStep}, gravity bias is added so the motor fights gravity and holds position.
   *
   * @param currentAngle current pivot angle (degrees)
   * @param targetAngle desired pivot angle (degrees)
   * @param maxStep maximum step size per cycle (degrees)
   * @return angle delta to add to the current angle this cycle
   */
  public static double computeStep(double currentAngle, double targetAngle, double maxStep) {
    double error = targetAngle - currentAngle;
    if (Math.abs(error) > maxStep) {
      return Math.signum(error) * maxStep;
    }
    return error;
  }
}
