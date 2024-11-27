// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.VacumnSubsystem;

public class VacumnCommand extends Command {
  /** Creates a new VacumnCommand. */
  Timer m_timer = new Timer();
  VacumnSubsystem m_vacumnSubsystem;
  boolean initialVacumnState = false;
  public VacumnCommand(VacumnSubsystem vacumnSubsystem) {
    m_vacumnSubsystem = vacumnSubsystem;
    addRequirements(m_vacumnSubsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("RUNNING TOGGLE VACUMN");
    System.out.println(m_vacumnSubsystem.getVacumnEnabled());
    m_vacumnSubsystem.toggleVacumn();
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
