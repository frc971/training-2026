package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SuperstructureCoordinator extends SubsystemBase {
  private static final double TARGET_SHOOT_RPM = 4500.0;

  private final Shooter shooter;
  private final Indexer indexer;

  public SuperstructureCoordinator(Shooter shooter, Indexer indexer) {
    this.shooter = shooter;
    this.indexer = indexer;
  }

  public void requestShoot() {
    shooter.spinTo(TARGET_SHOOT_RPM);

    if (isReadyToFeed()) {
      indexer.feed();
    } else {
      indexer.stop();
    }
  }

  public void stopAll() {
    shooter.stop();
    indexer.stop();
  }

  public boolean isReadyToFeed() {
    // TODO: Return true when current RPM is at least 90% of the target RPM
    System.out.println(shooter.getCurrentRpm());
    if(TARGET_SHOOT_RPM * 0.9 <= shooter.getCurrentRpm()){
      return true;
    }else{
      return false;
    }
    
  }
  
  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Superstructure/ReadyToFeed", isReadyToFeed());
  }
}
