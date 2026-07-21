package frc.robot.subsystems;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class Pathing {
  public enum Goal {
    ACTIVE,
    NOT_ACTIVE
  }

  private static final double SCORE_ARM_DEGREES = 62.0;
  private static final double SCORE_END_EFFECTOR_VOLTS = 6.0;
  private static final double SCORE_ELEVATOR_METERS = 1.25;
  private static final double ARM_TOLERANCE_DEGREES = 3.0;
  private static final double END_EFFECTOR_TOLERANCE_VOLTS = 0.5;
  private static final double ELEVATOR_TOLERANCE_METERS = 0.05;

  private final Arm arm;
  private final EndEffector endEffector;
  private final Elevator elevator;

  @AutoLogOutput
  private Goal goal = Goal.NOT_ACTIVE;

  public Pathing(Arm arm, EndEffector endEffector, Elevator elevator) {
    this.arm = arm;
    this.endEffector = endEffector;
    this.elevator = elevator;
  }

  public void setGoal(Goal goal) {
    this.goal = goal;
  }

  public Goal getGoal() {
    return goal;
  }

  public void periodic() {
    // TODO: Write this periodic method.
    // If the goal is NOT_ACTIVE, stop all three mechanisms and return.
    if(goal == Goal.NOT_ACTIVE){
      stopAll();
      logState();
      return;
    }
    // If the goal is ACTIVE:
    if(goal == Goal.ACTIVE){
    // 1. Move the arm to SCORE_ARM_DEGREES.
      arm.setPosition(SCORE_ARM_DEGREES);
      // 2. Only run the end effector at SCORE_END_EFFECTOR_VOLTS after the arm is ready.
      if(isArmReadyForEndEffector()){
        endEffector.setVoltage(SCORE_END_EFFECTOR_VOLTS);
      }
      else{
        endEffector.stop();
      }
      if(isEndEffectorReadyForElevator()){
          // 3. Only move the elevator to SCORE_ELEVATOR_METERS after the end effector is ready.
        elevator.setHeight(SCORE_ELEVATOR_METERS);
      }
      else{
        elevator.stop();
        }    
        
        
      } 
    
    logState();
}

  public boolean isArmReadyForEndEffector() {
    // TODO: Return true when the arm is within ARM_TOLERANCE_DEGREES of SCORE_ARM_DEGREES.
    if(Math.abs(arm.getCurrentDegrees() - SCORE_ARM_DEGREES) <= ARM_TOLERANCE_DEGREES){
      return true;
    }
    return false;
  }

  public boolean isEndEffectorReadyForElevator() {
    // TODO: Return true only after the arm is ready and the end effector is within
    // END_EFFECTOR_TOLERANCE_VOLTS of SCORE_END_EFFECTOR_VOLTS.
    if(Math.abs(endEffector.getAppliedVolts() - SCORE_END_EFFECTOR_VOLTS) <= END_EFFECTOR_TOLERANCE_VOLTS){
      return true;
    }
    return false;
  }

  public boolean isReadyToScore() {
    // TODO: Return true when the arm, end effector, and elevator have all reached their targets.
    // Use ELEVATOR_TOLERANCE_METERS for the elevator check.
    if(Math.abs(elevator.getCurrentMeters() - SCORE_ELEVATOR_METERS) <= ELEVATOR_TOLERANCE_METERS){
      return true;
    }
    return false;
  }
  

  private void stopAll() {
    arm.stop();
    endEffector.stop();
    elevator.stop();
  }

  private void logState() {
    // Log goal, arm ready for ee, arm ready for elevator, and ready to score here 
    Logger.recordOutput("Pathing/Goal", goal.name());
    Logger.recordOutput("Pathing/ArmReadyForEndEffector", isArmReadyForEndEffector());
    Logger.recordOutput("Pathing/EndEffectorReadyForElevator", isEndEffectorReadyForElevator());
    Logger.recordOutput("Pathing/ReadyToScore", isReadyToScore());
  }
}
