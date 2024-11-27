// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class VacumnSubsystem extends SubsystemBase {
  /** Creates a new VacuumSubsystem. */
  TalonSRX vacumnMotor; 
  boolean vacumnEnabled = false; 
  public VacumnSubsystem(int canID) {
    vacumnMotor = new TalonSRX(canID);
    vacumnEnabled = false;

  }

  public void enableVacumn(){
    vacumnEnabled = true;
    vacumnMotor.set(TalonSRXControlMode.PercentOutput, .3);
    System.out.println("enableVacuum");
  }

  public void stopVacumn(){
    vacumnEnabled = false;
    vacumnMotor.set(TalonSRXControlMode.PercentOutput, 0);
    System.out.println("stopVacuum");
  }

  public void toggleVacumn(){
    if(vacumnEnabled){
      stopVacumn();
    }
    else{
      enableVacumn();
    }
  }

  public boolean getVacumnEnabled(){
    return vacumnEnabled;
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
