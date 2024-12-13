// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.ObjectTrackerSubsystem;

/** Add your docs here. */
public class AutonomousCommands {
    private DrivetrainSubsystem m_dts;
    private VisionAutoCommand m_vac;
    private ObjectTrackerSubsystem m_obja;

    public AutonomousCommands(DrivetrainSubsystem dts, ObjectTrackerSubsystem obja){
        m_dts = dts;
        m_obja = obja; // ObjectTrackerSubsystem()
    }

     public Command postSeasonAutoStraight(){  
        return new SequentialCommandGroup(
            new InstantCommand(() -> m_dts.resetOdometry(new Pose2d(0, 0, new Rotation2d(0)))).withTimeout(0.1),
            new InstantCommand(() -> m_dts.resetAngle()).withTimeout(0.1),
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

    public Command goToToteVision(){
        // VisionAutoCommand vac = new VisionAutoCommand(m_dts, m_obja);

        return new SequentialCommandGroup(
            new InstantCommand(() -> m_dts.resetOdometry(new Pose2d(0, 0, new Rotation2d()))).withTimeout(0.5),
            new InstantCommand(() -> m_dts.resetAngle()).withTimeout(0.5),
            // new VisionAutoCommand(m_dts, m_obja, 0.000001, 20, 0) // Lining with the tote
            new VisionAutoCommand(m_dts, m_obja).visionCreatePath( 
                0.1, 
                40, 
                0) // Lining with the tote
            // new VisionAutoCommand(m_dts, m_obja).visionCreatePath( 
            //     0.000001, 
            //     5, 
            //     0) // Lining with the tote
            // vac.visionCreatePath( // Going in front of the corral
            //     0.0000001, 
            //     Units.metersToInches(Constants.DRIVETRAIN_WHEELBASE_LENGTH/2), 
            //     0
            // )
        );
    }
}
