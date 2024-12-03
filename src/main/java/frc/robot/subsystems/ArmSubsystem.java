// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ArmSubsystem extends SubsystemBase {
  /** Creates a new ArmSubsystem. */
  TalonFX armMotor;
  //TalonSRX armMotor;
  public ArmSubsystem() {
    armMotor = new TalonFX(Constants.ARM_ID);
    armMotor.setNeutralMode(NeutralModeValue.Brake);
  }

  public double getPosition(){
    return armMotor.getPosition().getValue();
  }
  public void armUp(){
    armMotor.setVoltage(-1*Constants.ARM_POWER*Constants.ARM_VOLTS); //11 volts
  }

  public void armDown(){
    armMotor.setVoltage(Constants.ARM_POWER*Constants.ARM_VOLTS);
  }

  public void stopArm(){
    armMotor.setVoltage(0); 
    //armMotor.set(ControlMode.PercentOutput,0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
