// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VacuumSubsystem extends SubsystemBase {
  /** Creates a new VacuumSubsystem. */
  VacuumSolenoidSubsystem m_vacuumSolenoidSubsystem; 
  TalonSRX vacuumMotor; 
  boolean vacuumEnabled = false; 
  boolean leftVacuum = false;
  public VacuumSubsystem(int canID, boolean isLeftVacuum, VacuumSolenoidSubsystem vacuumSolenoidSubsystem) {
    vacuumMotor = new TalonSRX(canID);
    vacuumEnabled = false;
    leftVacuum = isLeftVacuum;
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
    // This method will be called once per scheduler run
  }
}
