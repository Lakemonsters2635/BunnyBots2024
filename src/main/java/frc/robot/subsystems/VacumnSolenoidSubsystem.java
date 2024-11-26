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

public class VacumnSolenoidSubsystem extends SubsystemBase {
  /** Creates a new VacumnSolenoidSubsystem. */
  private Solenoid leftValve;
  private Solenoid rightValve;
  private boolean isRedAlliance = DriverStation.getAlliance().get() == DriverStation.Alliance.Red; 
  private boolean isLeftRed = true; //INPUT TRUE VALUES LATER
  private boolean isLeftBlue = true; //INPUT TRUE VALUES LATER
  private boolean isRightRed = true; //INPUT TRUE VALUES LATER
  private boolean isRightBlue = true; //INPUT TRUE VALUES LATER


  public VacumnSolenoidSubsystem() {
    leftValve = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.LEFT_VALVE_ID);  //FIX CONSTANTS LATER
    rightValve = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.RIGHT_VALVE_ID); //FIX CONSTANTS LATER
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
    }
    else{
      openLeftValve();
    }

  }
  public void toggleRightValve(){
    if(rightValve.get()){
      closeRightValve();
    }
    else{
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
    if(isRedAlliance == isLeftBlue){
      openLeftValve();
      if(delay(2)){
        closeLeftValve();
      }
    }
    if(isRedAlliance == isRightBlue){
      openRightValve();
      if(delay(2)){
        closeRightValve();
      }
    }
    
    // This method will be called once per scheduler run
  }
}
