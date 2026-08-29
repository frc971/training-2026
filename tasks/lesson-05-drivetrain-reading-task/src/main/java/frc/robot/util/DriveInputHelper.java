package frc.robot.util;

public class DriveInputHelper {
  private static final double TRANSLATION_DEADBAND = 0.08;
  private static final double ROTATION_DEADBAND = 0.10;
  private static final double TRANSLATION_EXPONENT = 2.0;
  private static final double ROTATION_EXPONENT = 2.0;
  private static final double NORMAL_TRANSLATION_SCALE = 4.0;
  private static final double NORMAL_ROTATION_SCALE = 3.0;
  private static final double SLOW_TRANSLATION_SCALE = 1.5;
  private static final double SLOW_ROTATION_SCALE = 1.0;

  public record DriveRequest(double xSpeed, double ySpeed, double rotSpeed) {}

  public DriveRequest process(double xInput, double yInput, double rotInput, boolean slowMode) {
    double x = applyDeadband(xInput, TRANSLATION_DEADBAND);
    double y = applyDeadband(yInput, TRANSLATION_DEADBAND);
    double rot = applyDeadband(rotInput, ROTATION_DEADBAND);

    x = applyExponent(x, TRANSLATION_EXPONENT);
    y = applyExponent(y, TRANSLATION_EXPONENT);
    rot = applyExponent(rot, ROTATION_EXPONENT);

    if (slowMode) {
      x *= SLOW_TRANSLATION_SCALE;
      y *= SLOW_TRANSLATION_SCALE;
      rot *= SLOW_ROTATION_SCALE;
    } else { 
      x *= NORMAL_ROTATION_SCALE;
      y *= NORMAL_ROTATION_SCALE;
      rot *= NORMAL_ROTATION_SCALE;
    }

    return new DriveRequest(x, y, rot);
  }

  private double applyDeadband(double value, double deadband) {
    if (value < deadband) {
      return 0.0;
    } else if (value < 0) {
      value = (value-deadband) / (1-deadband);
    } else {
      value = (value+deadband) / (1-deadband);
    }
    return value;
  }

  private double applyExponent(double value, double exponent) {
    double magnitude = Math.pow(Math.abs(value),exponent);
    value = Math.copySign(magnitude, value);
    return value;
  }
}
