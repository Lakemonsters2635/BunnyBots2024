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
import frc.robot.Constants;
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
                        new Pose2d(0,0, new Rotation2d(Math.toRadians(45))),
                        new Translation2d(0.5, 0.5),
                        new Pose2d(1, 1, new Rotation2d(Math.toRadians(45)))
            ));
    }
    public Command goToMidCorral(){ 
        //Goes to the middle of the corral starting from middle position facing forward 90 degrees
        return new SequentialCommandGroup(
            new InstantCommand(() -> m_dts.resetOdometry(new Pose2d(0, 0, new Rotation2d()))).withTimeout(0.1),
            new InstantCommand(() -> m_dts.resetAngle()),
            m_dts.createPath( 
                        new Pose2d(0,0, new Rotation2d(Math.toRadians(90))),
                        new Translation2d(0, 9/Constants.FEET_TO_METERS),
                        new Pose2d(0, 18/Constants.FEET_TO_METERS, new Rotation2d(Math.toRadians(90)))
            ));
    }
    public Command goToMidfromLeftCorral(){ 
        //THIS WILL CRASH ON REAL FIELD
        //Goes to the middle of the corral starting from left position touching the back wall facing forward 90 degrees
        return new SequentialCommandGroup(
            new InstantCommand(() -> m_dts.resetOdometry(new Pose2d(0, 0, new Rotation2d()))).withTimeout(0.1),
            new InstantCommand(() -> m_dts.resetAngle()),
            m_dts.createPath( 
                        new Pose2d(0,0, new Rotation2d(Math.toRadians(70))),
                        new Translation2d(1.5/Constants.FEET_TO_METERS, 10/Constants.FEET_TO_METERS),
                        new Pose2d(3/Constants.FEET_TO_METERS, 20/Constants.FEET_TO_METERS, new Rotation2d(Math.toRadians(90)))
            ));
    }
    public Command goToMidfromRightCorral(){ 
        //THIS WILL CRASH ON REAL FIELD
        //Goes to the middle of the corral starting from right position touching the back wall facing forward 90 degrees
        return new SequentialCommandGroup(
            new InstantCommand(() -> m_dts.resetOdometry(new Pose2d(0, 0, new Rotation2d()))).withTimeout(0.1),
            new InstantCommand(() -> m_dts.resetAngle()),
            m_dts.createPath( 
                        new Pose2d(0,0, new Rotation2d(Math.toRadians(110))),
                        new Translation2d(-1.5/Constants.FEET_TO_METERS, 10/Constants.FEET_TO_METERS),
                        new Pose2d(-3/Constants.FEET_TO_METERS, 20/Constants.FEET_TO_METERS, new Rotation2d(Math.toRadians(90)))
            ));
    }
}
