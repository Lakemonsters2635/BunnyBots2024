// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DigitalSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArduinoSubsystem extends SubsystemBase {
  /** Creates a new ArduinoSubsystem. */
  DigitalInput redLeftIn = new DigitalInput(Constants.RED_LEFT_ID);
  DigitalInput blueLeftIn = new DigitalInput(Constants.BLUE_LEFT_ID);
  DigitalInput redRightIn = new DigitalInput(Constants.
  RED_RIGHT_ID);
  DigitalInput blueRightIn = new DigitalInput(Constants.BLUE_RIGHT_ID);
  DigitalOutput leftEnable = new DigitalOutput(Constants.LEFT_ENABLE_ID);
  DigitalOutput rightEnable = new DigitalOutput(Constants.RIGHT_ENABLE_ID);
  public ArduinoSubsystem() {}

  public boolean getRedLeft(){
    return redLeftIn.get();
  }
   public boolean getBlueLeft(){
    return blueLeftIn.get();
  }
   public boolean getRedRight(){
    return redRightIn.get();
  }
  public boolean getBlueRight(){
    return blueRightIn.get();
  }

  public void toggleRightStrip(){
    rightEnable.set(!rightEnable.get());
  }

  public void toggleLeftStrip(){
    leftEnable.set(!leftEnable.get());
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("redLeft", getRedLeft());
    SmartDashboard.putBoolean("blueLeft", getBlueLeft());
    SmartDashboard.putBoolean("redRight", getRedRight());
    SmartDashboard.putBoolean("blueRight", getBlueRight());

    // This method will be called once per scheduler6 run

  }
}
