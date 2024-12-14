// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.ArduinoSubsystem;


public class VacuumSubsystem extends SubsystemBase {
  /** Creates a new VacuumSubsystem. */
  VacuumSolenoidSubsystem m_vacuumSolenoidSubsystem; 
  TalonSRX vacuumMotor; 

  private ArduinoSubsystem m_arduinoSubsystem;

  private boolean isRedAlliance = DriverStation.getAlliance().get() == DriverStation.Alliance.Red; 
  private boolean isLeftRed;
  private boolean isLeftBlue; 
  private boolean isRightRed;
  private boolean isRightBlue;
  boolean vacuumEnabled = false; 
  boolean leftVacuum = false;
  public VacuumSubsystem(int canID, boolean isLeftVacuum, VacuumSolenoidSubsystem vacuumSolenoidSubsystem, ArduinoSubsystem arduinoSubsystem ) {
    vacuumMotor = new TalonSRX(canID);
    vacuumEnabled = false;
    leftVacuum = isLeftVacuum;
    m_arduinoSubsystem = arduinoSubsystem;
    isLeftRed = m_arduinoSubsystem.getRedLeft(); //INPUT TRUE VALUES LATER
    isLeftBlue = m_arduinoSubsystem.getBlueLeft(); //INPUT TRUE VALUES LATER
    isRightRed= m_arduinoSubsystem.getRedRight(); //INPUT TRUE VALUES LATER
    isRightBlue = m_arduinoSubsystem.getBlueRight(); //INPUT TRUE VALUES LATER
    m_vacuumSolenoidSubsystem = vacuumSolenoidSubsystem;
    


  }

  public void enableVacuum(){
    vacuumEnabled = true;
    vacuumMotor.set(TalonSRXControlMode.PercentOutput, .8);
    System.out.println("enableVacuum");
     if(leftVacuum){
      m_vacuumSolenoidSubsystem.closeLeftValve();
    }
    else{
      m_vacuumSolenoidSubsystem.closeRightValve();
    }
  }

  public void stopVacuum(){
    vacuumEnabled = false;
    vacuumMotor.set(TalonSRXControlMode.PercentOutput, 0);
    if(leftVacuum){
      m_vacuumSolenoidSubsystem.openLeftValve();
    }
    else{
      m_vacuumSolenoidSubsystem.openRightValve();
    }
    System.out.println("stopVacuum");
  }

  public void toggleVacuum(){
    if(vacuumEnabled){
      stopVacuum();
    }
    else{
      enableVacuum();
    }
  }

  public boolean getVacuumEnabled(){
    return vacuumEnabled;
  }
  
  @Override
  public void periodic() {
    if(leftVacuum){
      if((isRedAlliance && isLeftBlue) || (!isRedAlliance && isLeftRed)){
            m_vacuumSolenoidSubsystem.openLeftValve();
            stopVacuum();
            Timer.delay(2);
            m_vacuumSolenoidSubsystem.closeLeftValve();
       }
    }
    else{
      if((isRedAlliance && isRightBlue) || (!isRedAlliance && isRightRed)){
        m_vacuumSolenoidSubsystem.openRightValve();
        stopVacuum();
        Timer.delay(2);
        m_vacuumSolenoidSubsystem.closeRightValve();
      }
    }
    
    // This method will be called once per scheduler run
    

    // This method will be called once per scheduler run
  }
}
