// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.List;
import java.util.function.Supplier;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class DrivetrainSubsystem extends SubsystemBase {
    public static final double kMaxSpeed = 3.63; // 3.63 meters per second  Max Speed for Front, Back, Left, Right
    public final double kMaxAngularSpeed = Math.PI; // 1/2 rotation per second   Max Speed for Rotation
    private SwerveModuleState[] swerveModuleStates;

    public static Joystick rightJoystick = RobotContainer.rightJoystick;
    public static Joystick leftJoystick = RobotContainer.leftJoystick;

    public final double m_drivetrainWheelbaseWidth =  Constants.DRIVETRAIN_WHEELBASE_WIDTH;  //Calibrated for 2024 BunnyBots
    public final double m_drivetrainWheelbaseLength = Constants.DRIVETRAIN_WHEELBASE_LENGTH; //Calibrated for 2024 BunnyBots

    public String selectedAliance = "blueAlliance"; //Sets Alliance as Blue

    // x is forward       robot is long in the x-direction, i.e. wheelbase length
    // y is to the left   robot is short in the y-direction, i.e. wheelbase width
    // robot front as currently labled on the motors (requires -x trajectory to go out into the +x field direction)
    public final Translation2d m_frontLeftLocation = 
            new Translation2d(-m_drivetrainWheelbaseWidth/2, m_drivetrainWheelbaseLength/2);
    public final Translation2d m_frontRightLocation = 
            new Translation2d(m_drivetrainWheelbaseWidth/2, m_drivetrainWheelbaseLength/2);
    public final Translation2d m_backLeftLocation = 
            new Translation2d(-m_drivetrainWheelbaseWidth/2, -m_drivetrainWheelbaseLength/2);
    public final Translation2d m_backRightLocation = 
            new Translation2d(m_drivetrainWheelbaseWidth/2, -m_drivetrainWheelbaseLength/2);

    public final SwerveModule m_frontLeft = new SwerveModule(Constants.DRIVETRAIN_FRONT_LEFT_DRIVE_MOTOR, 
                                                              Constants.DRIVETRAIN_FRONT_LEFT_ANGLE_MOTOR, 
                                                              Constants.DRIVETRAIN_FRONT_LEFT_ANGLE_ENCODER, 
                                                              Constants.FRONT_LEFT_ANGLE_OFFSET_COMPETITION,
                                                              1.0);
    public final SwerveModule m_frontRight = new SwerveModule(Constants.DRIVETRAIN_FRONT_RIGHT_DRIVE_MOTOR, 
                                                              Constants.DRIVETRAIN_FRONT_RIGHT_ANGLE_MOTOR, 
                                                              Constants.DRIVETRAIN_FRONT_RIGHT_ANGLE_ENCODER, 
                                                              Constants.FRONT_RIGHT_ANGLE_OFFSET_COMPETITION,
                                                              1.0);
    public final SwerveModule m_backLeft = new SwerveModule(Constants.DRIVETRAIN_BACK_LEFT_DRIVE_MOTOR, 
                                                              Constants.DRIVETRAIN_BACK_LEFT_ANGLE_MOTOR, 
                                                              Constants.DRIVETRAIN_BACK_LEFT_ANGLE_ENCODER, 
                                                              Constants.BACK_LEFT_ANGLE_OFFSET_COMPETITION,
                                                              1.0);
    public final SwerveModule m_backRight = new SwerveModule(Constants.DRIVETRAIN_BACK_RIGHT_DRIVE_MOTOR, 
                                                              Constants.DRIVETRAIN_BACK_RIGHT_ANGLE_MOTOR, 
                                                              Constants.DRIVETRAIN_BACK_RIGHT_ANGLE_ENCODER, 
                                                              Constants.BACK_RIGHT_ANGLE_OFFSET_COMPETITION,
                                                              1.0);
  
    public final AHRS m_gyro = new AHRS(SPI.Port.kMXP, (byte) 200);

    private final SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
      m_frontLeftLocation,
      m_frontRightLocation, 
      m_backLeftLocation, 
      m_backRightLocation);
    
    private boolean followJoystics = true;
  
    public final SwerveDriveOdometry m_odometry =
        new SwerveDriveOdometry(
            m_kinematics,
            m_gyro.getRotation2d().unaryMinus(),
            new SwerveModulePosition[] {
              m_frontLeft.getPosition(),
              m_frontRight.getPosition(),
              m_backLeft.getPosition(),
              m_backRight.getPosition()
            });

  /** Creates a new DrivetrianSubsystem. */
  public DrivetrainSubsystem() {
    getPose();

    // resetAngle() should be called before zeroOdometry() because reseting odometry uses gyro values to do the reset
    resetAngle();
    zeroOdometry();
  }

  public void stopMotors(){   //Zero motorPower
    m_backLeft.stop();
    m_frontLeft.stop();
    m_backRight.stop();
    m_frontRight.stop();
  }

  public double toRedHead(double blueHeadingDegrees) {  //Turn Angle from Blue to Red Alliance
    return -1.*(blueHeadingDegrees + 180.);
  }
  public int toRedHead(int blueHeadingDegrees) {
      return -1*(blueHeadingDegrees + 180);
  }

  public Command createPath(Pose2d startPose, Translation2d middlePose, Pose2d endPose){
    int desiredRot =0;
    return createPath(startPose, middlePose, endPose, desiredRot);
  }

  public Command createPath(Pose2d startPose, Translation2d middlePose, Pose2d endPose, double endRot){
    boolean isRedAliance  =false;
    
    isRedAliance = DriverStation.getAlliance().get() == DriverStation.Alliance.Red;

    // if (selectedAliance.equalsIgnoreCase("FMS")) {
    // }
    // else if(selectedAliance.equalsIgnoreCase("blue")){
    //   isRedAliance = false;
    // }
    // else if(selectedAliance.equalsIgnoreCase("red")){
    //   isRedAliance = true;
    // }
    // else{
    //   isRedAliance = false;
    // }

    // SmartDashboard.putString("selectedAlliance",selectedAliance);
    SmartDashboard.putBoolean("isRedAlliance",isRedAliance);
    SmartDashboard.putString("DriverStation.getAlliance().get()",DriverStation.getAlliance().get().name());
    SmartDashboard.putString("DriverStation.Alliance.Red",DriverStation.Alliance.Red.name());

    if (isRedAliance) { 
      startPose = new Pose2d(-startPose.getX(), startPose.getY(), new Rotation2d(Math.toRadians(toRedHead(startPose.getRotation().getDegrees()))));
      middlePose = new Translation2d(-middlePose.getX(), middlePose.getY());
      endPose = new Pose2d(-endPose.getX(), endPose.getY(), new Rotation2d(Math.toRadians(toRedHead(endPose.getRotation().getDegrees()))));
      endRot*=-1;
    }
    // angleSupplier expects a final variable so we create desiredRot and give the value of endRot
    final double desiredRot =endRot;


    TrajectoryConfig trajectoryConfig = new TrajectoryConfig(
      Constants.maxModuleLinearSpeed,  // 3.5
      Constants.maxModuleLinearAccelaration)// 4
      .setKinematics(m_kinematics);

    Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
      startPose,
      List.of(
        middlePose
      ),
      endPose,
      trajectoryConfig
      );

    // double[] x1 = {0.0, 0.0, 0.0};
    // double[] y1 = {0.0, -1.0, 0.0};

    // double[] x2 = {0.0, 0.0, 0.0};
    // double[] y2 = {-1.0, -1.0, 0.0};


    // ControlVector cV1 = new ControlVector(x1, y1);
    // ControlVector cV2 = new ControlVector(x2, y2);

    // ControlVectorList cvl = new ControlVectorList();

    // cvl.add(cV1);
    // cvl.add(cV2);
    
    // Trajectory trajectory = TrajectoryGenerator.generateTrajectory(cvl , trajectoryConfig);

    TrapezoidProfile.Constraints kThetaControllerConstraints = new TrapezoidProfile.Constraints(Constants.kMaxModuleAngularSpeedRadiansPerSecond, Constants.kMaxModuleAngularAccelerationRadiansPerSecondSquared);

    PIDController xController = new PIDController(0.4, 0, 0);
    PIDController yController = new PIDController(0.4, 0, 0);
    // Note: We reduced Kp to 2 so that rottion control loop doesn't saturate the module motor speed during autos
    // This however makes it so that robot cannot turn quickly, which is not good however it enables more acurate and consistent auto paths
    ProfiledPIDController thetaController = new ProfiledPIDController(3, 0, 0.2, kThetaControllerConstraints); // Find a value for the PID
    // ProfiledPIDController thetaController = new ProfiledPIDController(4, 0, 0.2, kThetaControllerConstraints); // Find a value for the PID
    thetaController.enableContinuousInput(-Math.PI, Math.PI);
    Supplier<Rotation2d> angleSupplier = () -> (Rotation2d)(Rotation2d.fromDegrees(desiredRot));


    SwerveControllerCommand swerveControllerCommand = new SwerveControllerCommand(
      trajectory,
      this::getPose,
      m_kinematics,
      xController,
      yController,
      thetaController,
      angleSupplier,
      this::setModuleStates,  // This is a consumer to set the states as defined in docs for SwerveControllerCommand
      this
    );

    return swerveControllerCommand;
  }

  public void  resetAngle(){
    m_gyro.reset();
    // Setting the angle adjustment changes where forward is when you push the controls forward
    // However it doesn't rotate the definition of the odometry x and y
    m_gyro.setAngleAdjustment(0);
  }
  public void resetAngle(int degree){
    m_gyro.reset();
    m_gyro.setAngleAdjustment(degree);
  }

  public void setFollowJoystick(boolean followJoystics){
    this.followJoystics =followJoystics;
  }


  private static double xPowerCommanded = 0;
  private static double yPowerCommanded = 0;
  private static double rotCommanded = 0;

  // three setters here and then call the setteres from the sd execute
  public void setXPowerCommanded(double xPower) {
    xPowerCommanded = xPower;
  }

  public void setYPowerCommanded(double yPower) {
    yPowerCommanded = yPower;
  }

  public void setRotCommanded(double rot) {
    rotCommanded = rot;
  }

  @Override
  public void periodic() {
      //Hat Power Overides for Trimming Position and Rotation
      // System.out.println("X: "+getPose().getX()+"\tY: "+getPose().getY()+"\tRot: "+getPose().getRotation().getDegrees());
      if (followJoystics) {
        if(rightJoystick.getPOV()==Constants.HAT_POV_MOVE_FORWARD ){
          yPowerCommanded = Constants.HAT_POWER_MOVE;
        }
        else if(rightJoystick.getPOV()==Constants.HAT_POV_MOVE_BACK){
          yPowerCommanded = Constants.HAT_POWER_MOVE*-1.0;
        }
        else if(rightJoystick.getPOV()==Constants.HAT_POV_MOVE_RIGHT){
          xPowerCommanded = Constants.HAT_POWER_MOVE*1.0;
        }
        else if(rightJoystick.getPOV()==Constants.HAT_POV_MOVE_LEFT){
          xPowerCommanded = Constants.HAT_POWER_MOVE*-1.0;
        }

        if(leftJoystick.getPOV()==Constants.HAT_POV_ROTATE_RIGHT){
          rotCommanded = Constants.HAT_POWER_ROTATE*-1.0;
        }
        else if(leftJoystick.getPOV()==Constants.HAT_POV_ROTATE_LEFT){
          rotCommanded = Constants.HAT_POWER_ROTATE;
        }

        if (rightJoystick.getY()>0.05 || rightJoystick.getY()<-0.05) {
          yPowerCommanded = rightJoystick.getY() * -1;
        }

        if (rightJoystick.getX()>0.05 || rightJoystick.getX()<-0.05) {
          xPowerCommanded = rightJoystick.getX();
        }

        // TODO: look at the deadband below
        if (Math.pow(rightJoystick.getTwist(),3)>0.05 || Math.pow(rightJoystick.getTwist(),3)<-0.05) {
          rotCommanded = rightJoystick.getTwist() * -1;
        }

      
        this.drive(-xPowerCommanded * DrivetrainSubsystem.kMaxSpeed, 
                  yPowerCommanded * DrivetrainSubsystem.kMaxSpeed,
                  MathUtil.applyDeadband(-rotCommanded * this.kMaxAngularSpeed, 0.2), 
                  true);
      }
      
      SmartDashboard.putNumber("rotCommanded", rotCommanded);

      double loggingState[] = {     //Array for predicted values
        swerveModuleStates[3].angle.getDegrees(), // Order here is BR, FR, BL, FL; order on Advantage Scope is FL, FR, BL, BR, but it works like this and we don't know why
        swerveModuleStates[3].speedMetersPerSecond,
        swerveModuleStates[1].angle.getDegrees(),
        swerveModuleStates[1].speedMetersPerSecond,
        swerveModuleStates[2].angle.getDegrees(),
        swerveModuleStates[2].speedMetersPerSecond,
        swerveModuleStates[0].angle.getDegrees(),
        swerveModuleStates[0].speedMetersPerSecond,
      };

      double actualLoggingState[] = {
        m_backRight.getTurningEncoderRadians() * 180 / Math.PI, // same order problem as predicted values
        m_backRight.getVelocity(),
        m_frontRight.getTurningEncoderRadians() * 180 / Math.PI,
        m_frontRight.getVelocity(),
        m_backLeft.getTurningEncoderRadians() * 180 / Math.PI,
        m_backLeft.getVelocity(),
        m_frontLeft.getTurningEncoderRadians() * 180 / Math.PI,
        m_frontLeft.getVelocity(),
      };

      SmartDashboard.putNumberArray("SwerveModuleStates",loggingState);
      SmartDashboard.putNumberArray("ActualSwerveModuleState", actualLoggingState);

      

    Pose2d lastPose2d = m_odometry.getPoseMeters();
    updateOdometry();
    Pose2d currentPose2d = m_odometry.getPoseMeters();

    SmartDashboard.putNumber("deltaX", currentPose2d.getX()-lastPose2d.getX());
    SmartDashboard.putNumber("deltaY", currentPose2d.getY()-lastPose2d.getY());
    SmartDashboard.putNumber("deltaRotation", currentPose2d.getRotation().getDegrees()-lastPose2d.getRotation().getDegrees());
    putDTSToSmartDashboard();
    tuneAngleOffsetPutToDTS();
    // System.out.println("FL: " + m_frontLeft.printVoltage());
    // System.out.println("FR: " + m_frontRight.printVoltage());
  }

  // public void recalibrateGyro() {
  //   // System.out.println(m_gyro.getRotation2d());
  //   m_gyro.reset();
  //   m_gyro.setAngleAdjustment(180);
  //   // System.out.println(m_gyro.getRotation2d());
  // }

  /**
   * Method to drive the robot using joystick info.
   *
   * @param xSpeed Speed of the robot in the x direction (forward).   -1.0 ... +1.0
   * @param ySpeed Speed of the robot in the y direction (sideways).  -1.0 ... +1.0
   * @param rot Angular rate of the robot.                            -1.0 ... +1.0
   * @param fieldRelative Whether the provided x and y speeds are relative to the field.
   */
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
    // TODO: Move kMaxSpeed and kMaxRotation into this method for ySpeed and xSpeed, and rot
    // TODO: Add another parameter for kMaxSpeed so you have an option to set it
    swerveModuleStates =
        m_kinematics.toSwerveModuleStates(
            fieldRelative
                ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, m_gyro.getRotation2d().unaryMinus())
                : new ChassisSpeeds(xSpeed, ySpeed, rot));
    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, kMaxSpeed);
    // m_frontLeft.setDesiredState(swerveModuleStates[0]);
    // m_frontRight.setDesiredState(swerveModuleStates[1]);
    // m_backLeft.setDesiredState(swerveModuleStates[2]);
    // m_backRight.setDesiredState(swerveModuleStates[3]);
    m_frontLeft.setDesiredState(swerveModuleStates[1]);
    m_frontRight.setDesiredState(swerveModuleStates[0]);
    m_backLeft.setDesiredState(swerveModuleStates[3]);
    m_backRight.setDesiredState(swerveModuleStates[2]);
    SmartDashboard.putNumber("xSpeed", xSpeed);
    SmartDashboard.putNumber("ySpeed", ySpeed);
    SmartDashboard.putNumber("rot", rot);
  }

  /** Updates the field relative position of the robot. */
  public void updateOdometry() {
    m_odometry.update(
        m_gyro.getRotation2d().unaryMinus(),
        new SwerveModulePosition[] {
          m_frontLeft.getPosition(),
          m_frontRight.getPosition(),
          m_backLeft.getPosition(),
          m_backRight.getPosition()
        });
  }

  /** Get pose from odometry field **/
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  public SwerveDriveKinematics getSwerveDriveKinematics() {
    return m_kinematics; 
  }

  /** zeroes drivetrain odometry **/
  public void zeroOdometry() {
    resetOdometry(new Pose2d(new Translation2d(0, 0), new Rotation2d(0)));
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * https://github.com/wpilibsuite/allwpilib/blob/main/wpilibjExamples/src/main/java/edu/wpi/first/wpilibj/examples/swervecontrollercommand/subsystems/DriveSubsystem.java
   * 
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    m_odometry.resetPosition(
        m_gyro.getRotation2d().unaryMinus(),
        new SwerveModulePosition[] {
          m_frontLeft.getPosition(),
          m_frontRight.getPosition(),
          m_backLeft.getPosition(),
          m_backRight.getPosition()
        },
        pose);
  }

  /** Sets the swerve ModuleStates.
   * @param cs The desired SwerveModule states as a ChassisSpeeds object
   */
  public void setDesiredStates(ChassisSpeeds cs) {
    SwerveModuleState[] desiredStates = m_kinematics.toSwerveModuleStates(cs);

    SwerveDriveKinematics.desaturateWheelSpeeds(
        desiredStates, 4);

    m_frontLeft.setDesiredState(desiredStates[1]);
    m_frontRight.setDesiredState(desiredStates[0]);
    m_backLeft.setDesiredState(desiredStates[3]);
    m_backRight.setDesiredState(desiredStates[2]);
  } 

  /** Sets the swerve ModuleStates. Accept a center of rotation for when you DON'T want to rotate
   * around the center of the robot
   * @param cs The desired SwerveModule states as a ChassisSpeeds object
   * @param centerOfRotation Center of rotation. Ex. location of camera
   */
  public void setDesiredStates(ChassisSpeeds cs, Translation2d centerOfRotation) {
    // System.out.println("vX: " + Math.round(cs.vxMetersPerSecond*100.0)/100.0 + "  vY: " + Math.round(cs.vyMetersPerSecond));
    SwerveModuleState[] desiredStates = m_kinematics.toSwerveModuleStates(cs, centerOfRotation);

    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, 4);

    m_frontLeft.setDesiredState(desiredStates[1]);
    m_frontRight.setDesiredState(desiredStates[0]);
    m_backLeft.setDesiredState(desiredStates[3]);
    m_backRight.setDesiredState(desiredStates[2]);
  } 

public ChassisSpeeds getChassisSpeeds() {
    ChassisSpeeds chassisSpeeds = m_kinematics.toChassisSpeeds(m_frontLeft.getState(), m_frontRight.getState(), m_backLeft.getState(), m_backRight.getState());
    return chassisSpeeds;
  }

  public AHRS getGyroscope() {
    return m_gyro; 
  }

  /**Sets the swerve ModuleStates.
   * @param desiredStates The desired SwerveModule states. Array of `SwerveModuleState[]`
   */
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(
        desiredStates, DrivetrainSubsystem.kMaxSpeed);
    m_frontLeft.setDesiredState(desiredStates[1]);
    m_frontRight.setDesiredState(desiredStates[0]);
    m_backLeft.setDesiredState(desiredStates[3]);
    m_backRight.setDesiredState(desiredStates[2]);
  }

  /** Displays all 4 module positions + robot pose (forward/back) in SmartDashboard. 
   * </p> For debugging
   */
  public void putDTSToSmartDashboard() {}

  /**
   * Procedure for tuning:
   * </p>   1. Put tuneAngleOffsetPutToDTS() in periodic(). 
   * </p>   2. Read the angles when the wheels are lined up. 
   * </p>   3. Add/subtract degrees from the angle offsets in Constants until they all read 0/pi/2pi when perfectly lined up
   */
  public void tuneAngleOffsetPutToDTS() {
    // TUNE ANGLE OFFSETS
    
    SmartDashboard.putNumber("FL encoder pos", Math.toDegrees(m_frontLeft.getTurningEncoderRadians()));
    SmartDashboard.putNumber("FR encoder pos", Math.toDegrees(m_frontRight.getTurningEncoderRadians()));
    SmartDashboard.putNumber("BL encoder pos", Math.toDegrees(m_backLeft.getTurningEncoderRadians()));
    SmartDashboard.putNumber("BR encoder pos", Math.toDegrees(m_backRight.getTurningEncoderRadians())); 


    // SmartDashboard.putNumber("FL SMS Speed", swerveModuleStates[0].speedMetersPerSecond);
    // SmartDashboard.putNumber("FL SMS Angle", swerveModuleStates[0].angle.getDegrees());


    // SmartDashboard.putNumber("FR SMS Speed", swerveModuleStates[1].speedMetersPerSecond);
    // SmartDashboard.putNumber("FR SMS Angle", swerveModuleStates[1].angle.getDegrees());

    // SmartDashboard.putNumber("BL SMS Speed", swerveModuleStates[2].speedMetersPerSecond);
    // SmartDashboard.putNumber("BL SMS Angle", swerveModuleStates[2].angle.getDegrees());

    // SmartDashboard.putNumber("BR SMS Speed", swerveModuleStates[3].speedMetersPerSecond);
    // SmartDashboard.putNumber("BR SMS Angle", swerveModuleStates[3].angle.getDegrees());
    
    // SmartDashboard.putNumber("Gyro Rotation 2d",m_gyro.getRotation2d().getDegrees());
    // SmartDashboard.putNumber("Gyro Speed X",m_gyro.getVelocityX());
    // SmartDashboard.putNumber("Gyro Speed Y",m_gyro.getVelocityY());

    SmartDashboard.putNumber("getPose.getX", getPose().getX());
    SmartDashboard.putNumber("getPose.getY", getPose().getY());
    SmartDashboard.putNumber("gyro.getAngle", m_gyro.getAngle());
    SmartDashboard.putNumber("getPose.getRotation", getPose().getRotation().getDegrees());
  }
}