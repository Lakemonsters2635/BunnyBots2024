// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class RelaySubsystem extends SubsystemBase {
  /** Creates a new RelaySubsystem. */

final Relay m_relay;


  public RelaySubsystem() {
    m_relay = new Relay(Constants.RELAY_CHANEL);
  }


  public void runRelay(){
    m_relay.set(Relay.Value.kOn);
  }

  public void forwardRelay(){
    m_relay.set(Relay.Value.kForward);
  }
    
  public void reverseRelay(){
    m_relay.set(Relay.Value.kReverse);
  }

  public void stopRelay(){
    m_relay.set(Relay.Value.kOff);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
