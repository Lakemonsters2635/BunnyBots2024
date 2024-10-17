// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DrivetrainSubsystem;

/** Add your docs here. */
public class AutonomousCommands {
    private DrivetrainSubsystem m_dts;
    public AutonomousCommands(DrivetrainSubsystem dts){
        m_dts = dts;
    }

     public Command postSeasonAutoStraight(){

        
        return new SequentialCommandGroup(
            new InstantCommand(() -> m_dts.resetOdometry(new Pose2d(0, 0, new Rotation2d()))).withTimeout(0.1),
            new InstantCommand(() -> m_dts.resetAngle()),
            m_dts.createPath(
                        new Pose2d(0,0, new Rotation2d(Math.toRadians(90))),
                        new Translation2d(0, 0.5),
                        new Pose2d(0, 1, new Rotation2d(Math.toRadians(90)))
            ));
    }
}
