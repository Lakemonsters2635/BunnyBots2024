// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.ObjectTrackerSubsystem;
import frc.robot.subsystems.VacuumSubsystem;

/** Add your docs here. */
public class AutonomousCommands {
    private DrivetrainSubsystem m_dts;
    private ObjectTrackerSubsystem m_obja;
    private VacuumCommand m_lvc;
    private VacuumCommand m_rvc;

    private ArmShakeCommand asc;
    // private VacuumCommand m_lvc2;
    // private VacuumCommand m_rvc2;

    public AutonomousCommands(DrivetrainSubsystem dts, ObjectTrackerSubsystem obja, VacuumCommand m_leftVacuumCommand, VacuumCommand m_rightVacuumCommand, VacuumSubsystem m_vssl, VacuumSubsystem m_vssr, ArmShakeCommand arm){
        m_dts = dts;
        m_obja = obja; // ObjectTrackerSubsystem()

        m_lvc = m_leftVacuumCommand;
        m_rvc = m_rightVacuumCommand;
        asc =arm;
        // m_lvc2 = m_leftVacuumCommand;
        // m_rvc2 = m_rightVacuumCommand;
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
    public Command goOnlyRight(){
        return new SequentialCommandGroup(
            new InstantCommand(() -> m_dts.resetOdometry(new Pose2d(0, 0, new Rotation2d()))).withTimeout(0.1),
            new InstantCommand(() -> m_dts.resetAngle()),
            m_dts.createPath( 
                        new Pose2d(0,0, new Rotation2d(Math.toRadians(0))),
                        new Translation2d(1, 0),
                        new Pose2d(2, 0, new Rotation2d(Math.toRadians(0)))
            ),
            new InstantCommand(()->m_dts.stopMotors())
        );
    }

    public Command goToMidTarget(){
        return new SequentialCommandGroup(
            new InstantCommand(()->m_dts.resetAngle()),
            new InstantCommand(()->m_dts.zeroOdometry()),
            m_dts.createPath(
                new Pose2d(0, 0, new Rotation2d(Units.degreesToRadians(90))), 
                new Translation2d(0, 1), 
                new Pose2d(0, 2, new Rotation2d(Units.degreesToRadians(90))),
                0
            ),
            new InstantCommand(()->m_dts.stopMotors())
        );
    }

    public void asdf(String label){
        System.out.println(label + " Degrees: " + m_dts.getPose().getRotation().getDegrees());
    }
    public Command goToRightTarget(){
        return new SequentialCommandGroup(
            new InstantCommand(()->asdf("getPose.getRotation before ")).withTimeout(0.1),
            new InstantCommand(()->m_dts.zeroOdometry()).withTimeout(0.1),
            new InstantCommand(()->m_dts.resetAngle()).withTimeout(0.1),
            new InstantCommand(()->asdf("getPose.getRotation after ")).withTimeout(0.1),
            m_dts.createPath(
                new Pose2d(0, 0, new Rotation2d(Units.degreesToRadians(45))), 
                new Translation2d(1.25, 1.25), 
                new Pose2d(2.5, 2.5, new Rotation2d(Units.degreesToRadians(45))),
                0.5
            ),
            new InstantCommand(()->m_dts.stopMotors())
        );
    }
    public Command goToLeftTarget(){
        return new SequentialCommandGroup(
            new InstantCommand(()->m_dts.zeroOdometry()),
            new InstantCommand(()->m_dts.resetAngle()),
            m_dts.createPath(
                new Pose2d(0, 0, new Rotation2d(Units.degreesToRadians(135))), 
                new Translation2d(-1, 1), 
                new Pose2d(-2, 2, new Rotation2d(Units.degreesToRadians(135)))
            ),
            new InstantCommand(()->m_dts.stopMotors())
        );
    }

    public Command goToMidTargetTurn45(){
        return new SequentialCommandGroup(
            new InstantCommand(()->m_dts.zeroOdometry()),
            new InstantCommand(()->m_dts.resetAngle()),
            m_dts.createPath(
                new Pose2d(0, 0, new Rotation2d(Units.degreesToRadians(90))), 
                new Translation2d(0, 1), 
                new Pose2d(0, 2, new Rotation2d(Units.degreesToRadians(90))),
                45
            ),
            new InstantCommand(()->m_dts.stopMotors())
        );
    }
    public Command goToRightTargetTurn45(){
        return new SequentialCommandGroup(
            new InstantCommand(()->m_dts.zeroOdometry()),
            new InstantCommand(()->m_dts.resetAngle()),
            m_dts.createPath(
                new Pose2d(0, 0, new Rotation2d(Units.degreesToRadians(45))), 
                new Translation2d(1, 1), 
                new Pose2d(2, 2, new Rotation2d(Units.degreesToRadians(45))),
                45
            ),
            new InstantCommand(()->m_dts.stopMotors())
        );
    }
    public Command goToLeftTargetTurn45(){
        return new SequentialCommandGroup(
            new InstantCommand(()->m_dts.zeroOdometry()),
            new InstantCommand(()->m_dts.resetAngle()),
            m_dts.createPath(
                new Pose2d(0, 0, new Rotation2d(Units.degreesToRadians(135))), 
                new Translation2d(-1, 1), 
                new Pose2d(-2, 2, new Rotation2d(Units.degreesToRadians(135))),
                45
            ),
            new InstantCommand(()->m_dts.stopMotors())
        );
    }
    public Command goToMidTargetTurn0(){
        return new SequentialCommandGroup(
            new InstantCommand(()->m_dts.zeroOdometry()),
            new InstantCommand(()->m_dts.resetAngle()),
            m_dts.createPath(
                new Pose2d(0, 0, new Rotation2d(Units.degreesToRadians(90))), 
                new Translation2d(0, 1), 
                new Pose2d(0, 2, new Rotation2d(Units.degreesToRadians(90))),
                0
            ),
            new InstantCommand(()->m_dts.stopMotors())
        );
    }
    public Command goToLeftTargetTurn0(){
        return new SequentialCommandGroup(
            new InstantCommand(()->m_dts.zeroOdometry()),
            new InstantCommand(()->m_dts.resetAngle()),
            m_dts.createPath(
                new Pose2d(0, 0, new Rotation2d(Units.degreesToRadians(45))), 
                new Translation2d(-1, 1), 
                new Pose2d(-2, 2, new Rotation2d(Units.degreesToRadians(45))),
                0
            ),
            new InstantCommand(()->m_dts.stopMotors())
        );
    }
    public Command goToRightTargetTurn0(){
        return new SequentialCommandGroup(
            new InstantCommand(()->m_dts.zeroOdometry()),
            new InstantCommand(()->m_dts.resetAngle()),
            m_dts.createPath(
                new Pose2d(0, 0, new Rotation2d(Units.degreesToRadians(135))), 
                new Translation2d(1, 1), 
                new Pose2d(2, 2, new Rotation2d(Units.degreesToRadians(135))),
                0
            ),
            new InstantCommand(()->m_dts.stopMotors())
        );
    }

    public Command goToToteVision(){
        // VisionAutoCommand vac = new VisionAutoCommand(m_dts, m_obja);

        return new SequentialCommandGroup(
            new InstantCommand(() -> m_dts.resetOdometry(new Pose2d(0, 0, new Rotation2d()))).withTimeout(0.5),
            new InstantCommand(() -> m_dts.resetAngle()).withTimeout(0.5)

            // new VisionAutoCommand(m_dts, m_obja, 0.000001, 20, 0) // Lining with the tote
            ,new VisionAutoCommand(m_dts, m_obja,15)
            // .visionCreatePath( 
            //     0.00001, //MUST BE NONZERO
            //     -40, 
            //     0).withTimeout(1)
            // ,new InstantCommand(() -> m_dts.resetAngle()).withTimeout(0.5)
            // ,new InstantCommand(() -> m_dts.resetOdometry(new Pose2d(0, 0, new Rotation2d()))).withTimeout(0.5)
            // ,m_dts.createPath(
            //      new Pose2d(0,0, new Rotation2d(Math.toRadians(0))),
            //      new Translation2d(Units.inchesToMeters(5), Units.inchesToMeters(-20)),
            //      new Pose2d(Units.inchesToMeters(10), Units.inchesToMeters(-40), new Rotation2d(Math.toRadians(0))),
            //      0)
            ,new InstantCommand(()->m_dts.stopMotors()).withTimeout(0.1)   
            // new InstantCommand(() -> m_dts.resetAngle()).withTimeout(0.5),
            // new InstantCommand(() -> m_dts.resetOdometry(new Pose2d(0, 0, new Rotation2d()))).withTimeout(0.5),
            // m_dts.createPath(
            //      new Pose2d(0,0, new Rotation2d(Math.toRadians(0))),
            //      new Translation2d(0, Units.inchesToMeters(-10)),
            //      new Pose2d(0, Units.inchesToMeters(-20), new Rotation2d(Math.toRadians(90))),
            //      0),
            // new InstantCommand(()->m_dts.stopMotors())
            //     ) // Lining with the tote
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

    public Command goToToteVision(int tagId, double xPrime, double zPrime, double finalYa){
        // VisionAutoCommand vac = new VisionAutoCommand(m_dts, m_obja);
        
        return new SequentialCommandGroup(
            new InstantCommand(() -> m_dts.resetOdometry(new Pose2d(0, 0, new Rotation2d()))).withTimeout(0.5),
            new InstantCommand(() -> m_dts.resetAngle()).withTimeout(0.5)

            // new VisionAutoCommand(m_dts, m_obja, 0.000001, 20, 0) // Lining with the tote
            ,new VisionAutoCommand(m_dts, m_obja, tagId, xPrime, zPrime, finalYa)
            // .visionCreatePath( 
            //     0.00001, //MUST BE NONZERO
            //     -40, 
            //     0).withTimeout(1)
            // ,new InstantCommand(() -> m_dts.resetAngle()).withTimeout(0.5)
            // ,new InstantCommand(() -> m_dts.resetOdometry(new Pose2d(0, 0, new Rotation2d()))).withTimeout(0.5)
            // ,m_dts.createPath(
            //      new Pose2d(0,0, new Rotation2d(Math.toRadians(0))),
            //      new Translation2d(Units.inchesToMeters(5), Units.inchesToMeters(-20)),
            //      new Pose2d(Units.inchesToMeters(10), Units.inchesToMeters(-40), new Rotation2d(Math.toRadians(0))),
            //      0)
            ,new InstantCommand(()->m_dts.stopMotors()).withTimeout(0.1)   
            // new InstantCommand(() -> m_dts.resetAngle()).withTimeout(0.5),
            // new InstantCommand(() -> m_dts.resetOdometry(new Pose2d(0, 0, new Rotation2d()))).withTimeout(0.5),
            // m_dts.createPath(
            //      new Pose2d(0,0, new Rotation2d(Math.toRadians(0))),
            //      new Translation2d(0, Units.inchesToMeters(-10)),
            //      new Pose2d(0, Units.inchesToMeters(-20), new Rotation2d(Math.toRadians(90))),
            //      0),
            // new InstantCommand(()->m_dts.stopMotors())
            //     ) // Lining with the tote
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

    public Command comboAprilTag(int tagId1, double xPrime1, double zPrime1, double finalYa1, int tagId2, double xPrime2, double zPrime2, double finalYa2){
        
        return new SequentialCommandGroup(
            new InstantCommand(() -> m_dts.resetOdometry(new Pose2d(0, 0, new Rotation2d()))).withTimeout(0.5),
            new InstantCommand(() -> m_dts.resetAngle()).withTimeout(0.5)
            ,  new VisionAutoCommand(m_dts, m_obja, tagId1, xPrime1, zPrime1, finalYa1).visionCreatePath(xPrime1, zPrime1, finalYa1)
            // ,new VisionAutoCommand(m_dts, m_obja, tagId1, xPrime1, zPrime1, finalYa1)
            // ,new InstantCommand(()->m_dts.stopMotors()).withTimeout(0.1)
            // ,new VisionAutoCommand(m_dts, m_obja, tagId2, xPrime2, zPrime2, finalYa2)
            // ,new InstantCommand(()->m_dts.stopMotors()).withTimeout(0.1)   
            
        );
    }

    public Command heading0(){
        return m_dts.createPath(
            new Pose2d(0,0, new Rotation2d(Math.PI/2)),
             new Translation2d(0,0.5),
             new Pose2d(0,1,new Rotation2d(Math.PI/2)));
    }

    public Command goToToteScoreBunnyAuto(){
        return new SequentialCommandGroup(
            m_lvc,
            m_rvc,
            // new VacuumCommand(new VacuumSubsystem(Constants.RIGHT_VACUUM_MOTOR_ID, false, new VacuumSolenoidSubsystem())),
            goToToteVision(),
            new ArmToUpwardPosition(new ArmSubsystem()),
            // m_lvc
            // m_rvc
            asc
        );
    }

    public Command visionAutoData(){
        return new SequentialCommandGroup(
            new InstantCommand(() -> m_dts.stopMotors()),
            new VisionAutoCommand(m_dts, m_obja));
    }
}
