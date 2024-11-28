// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ArmSubsystem extends SubsystemBase {
  /** Creates a new ArmSubsystem. */
  Joystick leftJoystick = new Joystick(0);
  TalonFX armMotor;
  //TalonSRX armMotor;
  public ArmSubsystem() {
    armMotor = new TalonFX(Constants.ARM_ID);
    //armMotor = new TalonSRX(20);
  }

  public void powerArm(){
    double power = leftJoystick.getThrottle() * .2;
    armMotor.set(power);
    System.out.println(power);
    System.out.println(armMotor.getPosition());
    //armMotor.set(ControlMode.PercentOutput, 0.2 * leftJoystick.getThrottle());
    //SmartDashboard.put("Current ArmPos", armMotor.getPosition());
  }

  public void stopArm(){
    armMotor.setVoltage(0); 
    //armMotor.set(ControlMode.PercentOutput,0);
  }

  @Override
  public void periodic() {
    powerArm();
    // This method will be called once per scheduler run
  }
}
