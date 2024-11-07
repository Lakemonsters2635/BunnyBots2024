// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.PowerDistribution;


public class VacuumSubsystem extends SubsystemBase {
  /** Creates a new VacuumSubsystem. */
  private PowerDistribution powerDistribution;
  double current;
  public VacuumSubsystem() {
    // module id is can id of the PDH, check using REV Hardware Client
    powerDistribution = new PowerDistribution(20, PowerDistribution.ModuleType.kRev); // TODO: Change this id
  }

  @Override
  public void periodic() {
    // This on is the PDH id of vacuum
    current = powerDistribution.getCurrent(17); // TODO: change this id
    SmartDashboard.putNumber("Vacuum current", current);
  }
}
