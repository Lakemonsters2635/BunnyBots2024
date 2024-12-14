// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ArmShakeCommand;
import frc.robot.commands.ArmToDownwardPosition;
import frc.robot.commands.ArmToUpwardPosition;
import frc.robot.commands.AutonomousCommands;
import frc.robot.commands.DrivetrainCommand;
import frc.robot.commands.VacuumCommand;
import frc.robot.subsystems.ArduinoSubsystem;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.commands.ToteGrabberDownCommand;
import frc.robot.commands.ToteGrabberUpCommand;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.ObjectTrackerSubsystem;
import frc.robot.subsystems.VacuumSubsystem;
import frc.robot.subsystems.VacuumSolenoidSubsystem;
import frc.robot.subsystems.ToteGrabberSubsystem;
import frc.robot.subsystems.ArduinoSubsystem;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // Joysticks
  public static final Joystick rightJoystick = new Joystick(Constants.RIGHT_JOYSTICK_CHANNEL);
  public static final Joystick leftJoystick = new Joystick(Constants.LEFT_JOYSTICK_CHANNEL);

  // Subsystems
  public static final ArduinoSubsystem m_arduinoSubsystem = new ArduinoSubsystem();
  public static final DrivetrainSubsystem m_drivetrainSubsystem = new DrivetrainSubsystem();
  public static final VacuumSolenoidSubsystem m_vacuumSolenoidSusbsystem = new VacuumSolenoidSubsystem();
  public static final VacuumSubsystem m_leftVacuumSubsystem = new VacuumSubsystem(Constants.LEFT_VACUUM_MOTOR_ID, true, m_vacuumSolenoidSusbsystem, m_arduinoSubsystem); 
  public static final VacuumSubsystem m_rightVacuumSubsystem = new VacuumSubsystem(Constants.RIGHT_VACUUM_MOTOR_ID, false, m_vacuumSolenoidSusbsystem, m_arduinoSubsystem); 
  public static final ArmSubsystem m_armSubsystem = new ArmSubsystem();
  public static final ToteGrabberSubsystem m_toteGrabberSubsystem = new ToteGrabberSubsystem();
  // public static final ObjectTrackerSubsystem m_objectTrackerSubsystem = new ObjectTrackerSubsystem("Eclipse");
 
  //Command 
  public static final VacuumCommand m_leftVacuumCommand = new VacuumCommand(m_leftVacuumSubsystem);
  public static final VacuumCommand m_rightVacuumCommnad = new VacuumCommand(m_rightVacuumSubsystem);
  public static final ArmToUpwardPosition m_armToUpwardPosition = new ArmToUpwardPosition(m_armSubsystem);
  public static final ArmToDownwardPosition m_armToDownwardPosition = new ArmToDownwardPosition(m_armSubsystem);
  public static final DrivetrainCommand m_driveTrainCommand = new DrivetrainCommand(m_drivetrainSubsystem);
  public static final AutonomousCommands m_autonomousCommands = new AutonomousCommands(m_drivetrainSubsystem);
  public static final ArmShakeCommand m_armShakeCommand = new ArmShakeCommand(m_armSubsystem);  
  public static final ToteGrabberDownCommand m_toteGrabberDownCommand = new ToteGrabberDownCommand(m_toteGrabberSubsystem);
  public static final ToteGrabberUpCommand m_toteGrabberUpCommand = new ToteGrabberUpCommand(m_toteGrabberSubsystem);
  


  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj12.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    //creating buttons

    // right buttons
    Trigger swerveResetButton = new JoystickButton(rightJoystick, Constants.SWERVE_RESET_BUTTON);
    Trigger resetOdometryButton = new JoystickButton(rightJoystick, Constants.ZERO_ODOMETRY_BUTTON);
    Trigger leftVacuumToggle = new JoystickButton(rightJoystick, Constants.LEFT_VACUUM_TOGGLE_BUTTON);
    Trigger rightVacuumToggle = new JoystickButton(rightJoystick, Constants.RIGHT_VACUUM_TOGGLE_BUTTON);
    
   
    // left buttons
    
    
    Trigger armToUpwardPositionButton = new JoystickButton(leftJoystick, Constants.ARM_TO_UPWARD_POSITION_BUTTON);
    Trigger armToDownwardPositionButton = new JoystickButton(leftJoystick, Constants.ARM_TO_DOWNWARD_POSITION_BUTTON);   
    Trigger armShakeButton = new JoystickButton(leftJoystick, Constants.ARM_SHAKE_BUTTON);

    Trigger toggleLeftSolenoidValve = new JoystickButton(leftJoystick, Constants.LEFT_VALVE_TOGGLE_BUTTON); //CONFIGURE BUTTONS LATER
    Trigger toggleRightSolenoidValve = new JoystickButton(leftJoystick, Constants.RIGHT_VALVE_TOGGLE_BUTTON); //CONFIGURE BUTTONS LATER
    

    // Trigger openLeftSolenoidValve = new JoystickButton(leftJoystick, 10);
    // Trigger closeLeftSolenoidValve = new JoystickButton(leftJoystick, 9);

    toggleLeftSolenoidValve.onTrue(new InstantCommand(()->m_vacuumSolenoidSusbsystem.toggleLeftValve()));
    toggleRightSolenoidValve.onTrue(new InstantCommand(()->m_vacuumSolenoidSusbsystem.toggleRightValve()));
    
    leftVacuumToggle.onTrue(m_leftVacuumCommand);
    rightVacuumToggle.onTrue(m_rightVacuumCommnad);

    armToUpwardPositionButton.onTrue(m_armToUpwardPosition);
    armToDownwardPositionButton.onTrue(m_armToDownwardPosition);
    armShakeButton.onTrue(m_armShakeCommand);

    // openLeftSolenoidValve.onTrue(new InstantCommand(()->m_vacuumSolenoidSusbsystem.openLeftValve()));
    // closeLeftSolenoidValve.onTrue(new InstantCommand(()->m_vacuumSolenoidSusbsystem.closeLeftValve()));
    Trigger toteGrabberUpButton = new JoystickButton(leftJoystick, Constants.TOTE_GRABBER_UP_BUTTON);
    Trigger toteGrabberDownButton = new JoystickButton(leftJoystick, Constants.TOTE_GRABBER_DOWN_BUTTON);
    Trigger leftEnableToggleButton = new JoystickButton(leftJoystick, Constants.LEFT_ENABLE_BUTTON);
    Trigger rightEnableToggleButton = new JoystickButton(leftJoystick, Constants.RIGHT_ENABLE_BUTTON);

    // right
    swerveResetButton.onTrue(new InstantCommand(()->m_drivetrainSubsystem.resetAngle()));
    resetOdometryButton.onTrue(
      new SequentialCommandGroup(
        new InstantCommand(()->m_drivetrainSubsystem.resetAngle()),
        new InstantCommand(()->m_drivetrainSubsystem.zeroOdometry())
      )
    );

    leftVacuumToggle.onTrue(m_leftVacuumCommand);
    rightVacuumToggle.onTrue(m_rightVacuumCommnad);

    // left
    leftEnableToggleButton.onTrue(new InstantCommand(()->m_arduinoSubsystem.toggleLeftStrip()));
    rightEnableToggleButton.onTrue(new InstantCommand(()->m_arduinoSubsystem.toggleRightStrip()));
    toteGrabberDownButton.whileTrue(m_toteGrabberDownCommand);
    toteGrabberUpButton.whileTrue(m_toteGrabberUpCommand);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public SendableChooser<Command> getAutonomousCommand() {
    SendableChooser<Command> m_autoChooser = new SendableChooser<>();
    SendableChooser<Command> m_alianceChooser = new SendableChooser<>();
    // m_alianceChooser.addOption("red", new InstantCommand(()->m_drivetrainSubsystem.selectAliance("red")));
    // m_alianceChooser.addOption("blue", new InstantCommand(()->m_drivetrainSubsystem.selectAliance("blue")));
    // m_alianceChooser.addOption("FMS", new InstantCommand(()->m_drivetrainSubsystem.selectAliance("FMS")));
    m_autoChooser.addOption("postSeasonAutoStraight", m_autonomousCommands.postSeasonAutoStraight());
    m_autoChooser.setDefaultOption("postSeasonAutoStraight", m_autonomousCommands.postSeasonAutoStraight());

    SmartDashboard.putData("AutoChooser", m_autoChooser);
    SmartDashboard.putData("AlianceChooser", m_alianceChooser);
    

    return m_autoChooser;
  }
}
