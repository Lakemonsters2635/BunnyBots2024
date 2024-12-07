// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class VacuumSubsystem extends SubsystemBase {
  /** Creates a new VacuumSubsystem. */

  public TalonSRX vacuumMotor;

 
  public VacuumSubsystem(int can) {

    vacuumMotor = new TalonSRX(can);
  }

  public void runVacuum(){
    vacuumMotor.set(ControlMode.PercentOutput, Constants.runVacuumSpeed); //TODO: find TalonSRXControlMode
  }

  public void stopVacuum(){
     vacuumMotor.set(ControlMode.PercentOutput, Constants.stopVacuumSpeed);  //TODO: find TalonSRXControlMode
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
