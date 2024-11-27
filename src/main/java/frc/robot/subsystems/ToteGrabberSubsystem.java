// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ToteGrabberSubsystem extends SubsystemBase {
  /** Creates a new ToteGrabberSubsystem. */
  CANSparkMax toteGrabberMotor;
  public ToteGrabberSubsystem() {
    toteGrabberMotor = new CANSparkMax(Constants.TOTE_GRABBER_ID, MotorType.kBrushless);
  }

  public void toteGrabberDown(){
    toteGrabberMotor.set(0.1);
  }
  public void toteGrabberUp(){
    toteGrabberMotor.set(-0.1);
  }
  public void toteGrabberStop(){
    toteGrabberMotor.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
