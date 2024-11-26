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
  TalonSRX vacumnMotor; 
  TalonFX armMotor; // Delete this
  public VacuumSubsystem() {
    vacumnMotor = new TalonSRX(Constants.VACUUM_1_MOTOR_ID);
    armMotor = new TalonFX(20);

  }

  public void enableVacuum1(){
    vacumnMotor.set(TalonSRXControlMode.PercentOutput, .3);
    System.out.println("enableVacuum");
  }

  public void stopVacuum1(){
    vacumnMotor.set(TalonSRXControlMode.PercentOutput, 0);
    System.out.println("stopVacuum");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
