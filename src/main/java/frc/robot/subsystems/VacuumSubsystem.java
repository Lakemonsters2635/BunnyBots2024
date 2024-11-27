// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class VacuumSubsystem extends SubsystemBase {
  /** Creates a new VacuumSubsystem. */
  TalonSRX vacuumMotor; 
  boolean vacuumEnabled = false; 
  public VacuumSubsystem(int canID) {
    vacuumMotor = new TalonSRX(canID);
    vacuumEnabled = false;

  }

  public void enableVacuum(){
    vacuumEnabled = true;
    vacuumMotor.set(TalonSRXControlMode.PercentOutput, .3);
    System.out.println("enableVacuum");
  }

  public void stopVacuum(){
    vacuumEnabled = false;
    vacuumMotor.set(TalonSRXControlMode.PercentOutput, 0);
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
