// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.ArmSubsystem;

public class ArmToUpwardPosition extends Command {
  /** Creates a new ArmToUpwardPosition. */
  ArmSubsystem m_armSubsystem;
  public ArmToUpwardPosition(ArmSubsystem armSubsystem) {
    m_armSubsystem = armSubsystem;
    addRequirements(m_armSubsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_armSubsystem.armUp();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_armSubsystem.stopArm();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(m_armSubsystem.getPosition() < Constants.ARM_UP_POSITION){
      return true;
    }
    return false;
  }
}

