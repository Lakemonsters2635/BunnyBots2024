// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.RelaySubsystem;

public class ReverseRelayCommand extends Command {
  /** Creates a new ReverseRelayCommand. */

  RelaySubsystem m_relaySubsystem; 


  public ReverseRelayCommand(RelaySubsystem relaySubsystem) {

  m_relaySubsystem = relaySubsystem;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_relaySubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_relaySubsystem.runRelay();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_relaySubsystem.reverseRelay();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_relaySubsystem.stopRelay();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
