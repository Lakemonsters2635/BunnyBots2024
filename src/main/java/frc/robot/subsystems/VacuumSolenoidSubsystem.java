// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class VacuumSolenoidSubsystem extends SubsystemBase {
  /** Creates a new VacuumSolenoidSubsystem. */
  private Solenoid leftValve;
  private Solenoid rightValve;
  private boolean isRedAlliance = DriverStation.getAlliance().get() == DriverStation.Alliance.Red; 
  private boolean isLeftRed = false; //INPUT TRUE VALUES LATER
  private boolean isLeftBlue = false; //INPUT TRUE VALUES LATER
  private boolean isRightRed = false; //INPUT TRUE VALUES LATER
  private boolean isRightBlue = false; //INPUT TRUE VALUES LATER


  public VacuumSolenoidSubsystem() {
    leftValve = new Solenoid(Constants.PNEUMATICS_CONTROL_HUB_ID, PneumaticsModuleType.CTREPCM, Constants.LEFT_VALVE_ID);  //FIX CONSTANTS LATER
    rightValve = new Solenoid(Constants.PNEUMATICS_CONTROL_HUB_ID, PneumaticsModuleType.CTREPCM, Constants.RIGHT_VALVE_ID); //FIX CONSTANTS LATER
  }

  public void openLeftValve(){
    leftValve.set(true);
  }

  public void openRightValve(){
    rightValve.set(true);
  }

  public void closeLeftValve(){
    leftValve.set(false);
  }

  public void closeRightValve(){
    rightValve.set(false);
  }

  public void toggleLeftValve(){
    if(leftValve.get()){
      closeLeftValve();
      System.out.println("Left valve close");
    }
    else{
      openLeftValve();
      System.out.println("Left valve open");
    }


  }
  public void toggleRightValve(){
    if(rightValve.get()){
      System.out.println("right valve close");
      closeRightValve();
    }
    else{
      System.out.println("right valve open");
      openRightValve();
    }
  }

  public boolean delay(double seconds){
    Timer m_timer = new Timer();
    m_timer.start();
    if(m_timer.get() == seconds){
      m_timer.stop();
      m_timer.reset();
      return true;
    }
    return false;
  }

  @Override
  public void periodic() {
    //2 sec then close
    if((isRedAlliance  && isLeftBlue) || (!isRedAlliance && isLeftRed)){
      openLeftValve();
      if(delay(2)){
        closeLeftValve();
      }
    }
    if((isRedAlliance && isRightBlue) || (!isRedAlliance && isRightRed)){
      openRightValve();
      if(delay(2)){
        closeRightValve();
      }
    }
    
    // This method will be called once per scheduler run
  }
}
