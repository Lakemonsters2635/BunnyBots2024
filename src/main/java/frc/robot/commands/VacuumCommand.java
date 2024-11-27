// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.VacuumSubsystem;

public class VacuumCommand extends Command {
  /** Creates a new VacuumCommand. */
  Timer m_timer = new Timer();
  VacuumSubsystem m_vacuumSubsystem;
  boolean initialVacuumState = false;
  public VacuumCommand(VacuumSubsystem vacuumSubsystem) {
    m_vacuumSubsystem = vacuumSubsystem;
    addRequirements(m_vacuumSubsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("RUNNING TOGGLE VACUUM");
    System.out.println(m_vacuumSubsystem.getVacuumEnabled());
    m_vacuumSubsystem.toggleVacuum();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    m_timer.delay(0.2);
    return true;
  }
}
