// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

// TODO: Figure out whether to use phoenix6 or phoenix, aka phoenix5
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ToteGrabberSubsystem extends SubsystemBase {
  /** Creates a new ToteGrabberSubsystem. */
  TalonFX toteGrabberMotor;
  public ToteGrabberSubsystem() {
    toteGrabberMotor = new TalonFX(Constants.TOTE_GRABBER_ID);
  }

  public void toteGrabberDown(){
    toteGrabberMotor.set(0.05);
  }
  public void toteGrabberUp(){
    toteGrabberMotor.set(-0.2);
  }
  public void toteGrabberStop(){
    toteGrabberMotor.set(0);
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
