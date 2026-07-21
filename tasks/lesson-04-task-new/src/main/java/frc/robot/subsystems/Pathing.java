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
    if (goal == Goal.NOT_ACTIVE){

      stopAll();
      logState();
      return;

    }else if (goal == Goal.ACTIVE){
      arm.setPosition(SCORE_ARM_DEGREES);
    }

    if (isArmReadyForEndEffector()){

      endEffector.setVoltage(SCORE_END_EFFECTOR_VOLTS);


    }else{
      endEffector.stop();
    }
    
    if (isEndEffectorReadyForElevator()){

      //move the elevator to score_elevator_meters
      //elevator is called elevator

      elevator.setHeight(SCORE_ELEVATOR_METERS);






    }else{

      elevator.stop();
    }
  
    // If the goal is NOT_ACTIVE, stop all three mechanisms and return.
    // If the goal is ACTIVE:
    // 1. Move the arm to SCORE_ARM_DEGREES.
    // 2. Only run the end effector at SCORE_END_EFFECTOR_VOLTS after the arm is ready.
    // 3. Only move the elevator to SCORE_ELEVATOR_METERS after the end effector is ready.
    // 4. Otherwise keep mechanisms that are not allowed to move yet stopped or at zero.
    logState();
  }

  public boolean isArmReadyForEndEffector() {

    return Math.abs(arm.getCurrentDegrees() - SCORE_ARM_DEGREES) <= ARM_TOLERANCE_DEGREES;
    



   
    // TODO: Return true when the arm is within ARM_TOLERANCE_DEGREES of SCORE_ARM_DEGREES.
    
  }

  public boolean isEndEffectorReadyForElevator() {

    return (Math.abs(endEffector.getAppliedVolts() - SCORE_END_EFFECTOR_VOLTS) <= END_EFFECTOR_TOLERANCE_VOLTS) && isArmReadyForEndEffector();


    // TODO: Return true only after the arm is ready and the end effector is within
    // END_EFFECTOR_TOLERANCE_VOLTS of SCORE_END_EFFECTOR_VOLTS.
    
  }

  public boolean isReadyToScore() {

    return (isEndEffectorReadyForElevator() && (Math.abs(elevator.getCurrentMeters() - SCORE_ELEVATOR_METERS) <= ELEVATOR_TOLERANCE_METERS));

    
    // TODO: Return true when the arm, end effector, and elevator have all reached their targets.
    // Use ELEVATOR_TOLERANCE_METERS for the elevator check.
    
  }

  private void stopAll() {
    arm.stop();
    endEffector.stop();
    elevator.stop();
  }

  private void logState() {
    // Log goal, arm ready for ee, arm ready for elevator, and ready to score here 
    
    
    //example     Logger.recordOutput("Superstructure/Goal", getGoal().name());

    Logger.recordOutput("goal", getGoal());
    Logger.recordOutput("arm ready for ee", isArmReadyForEndEffector());
    Logger.recordOutput("ee ready for elevator", isEndEffectorReadyForElevator());
    Logger.recordOutput("ready to score", isReadyToScore());

  }
}
