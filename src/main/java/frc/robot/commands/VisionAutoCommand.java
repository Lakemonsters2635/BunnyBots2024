// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import org.opencv.core.Mat;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.ObjectTrackerSubsystem;

public class VisionAutoCommand extends Command {
  /** Creates a new VisionAutoCommand. */
  DrivetrainSubsystem m_dts;
  ObjectTrackerSubsystem m_ots;
  double visionX;
  double visionY;
  double visionZ;
  double visionYa;

  double fieldX;
  double fieldY;

  double heading_fixMe; // This really shouldn't be a class variable

  // double xPrime;
  // double zPrime;
  // double finalYa;

  public VisionAutoCommand(DrivetrainSubsystem dts, ObjectTrackerSubsystem ots) {
    m_dts = dts;
    m_ots = ots;

    // this.xPrime = xPrime0;
    // this.zPrime = zPrime0;
    // this.finalYa = finalYa0;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // Don't need to get m_ots.data() because it is already called in Robot.java periodic

    try{
      visionX = m_ots.getVisionX();
      visionY = m_ots.getVisionY();
      visionZ = m_ots.getVisionZ();
      visionYa = m_ots.getVisionYa();

      SmartDashboard.putNumber("Robot x", m_dts.getPose().getX());
      SmartDashboard.putNumber("Robot y", m_dts.getPose().getY());
      SmartDashboard.putNumber("Robot rot", m_dts.getPose().getRotation().getDegrees());
    }
    catch(Exception e) {
      System.out.println(e);
    }

    // visionCreatePath(xPrime, zPrime, finalYa).schedule();

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    visionAutoData(0, 0, 0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_dts.stopMotors();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false; //TODO: Change back to true after done debugging
  }

  public Pose2d visionAutoData(double xPrime, double zPrime, double finalYa){
    try{
      m_ots.data();
      visionX = m_ots.visionX;
      visionZ = m_ots.visionZ;
      visionY = m_ots.visionY;
      visionYa = m_ots.visionYa;
    }
    catch(Exception e){

    }
    
    // xPrime = 23.5;
    // zPrime = -16.5;
    // finalYa = 0;
    visionYa*=-1;
    double x_vt = xPrime * Math.cos(Math.toRadians(visionYa)) + -zPrime * Math.sin(Math.toRadians(visionYa));
    double z_vt = xPrime * Math.sin(Math.toRadians(visionYa)) + zPrime * Math.cos(Math.toRadians(visionYa));

    double deltaRobotX = -1 * (visionX + x_vt);
    double deltaRobotY = -1 * (visionZ + z_vt);

    // corrects for the camera position TODO: does this need to be meters for the field
    deltaRobotX += -8.5;
    deltaRobotY += -12.875;

    // TODO: the following linear regression calculation doesn't work
    // deltaRobotX -= -0.191819 * visionYa;
    // deltaRobotY +=  0.21868 * visionYa;

    double botRadians = Units.degreesToRadians(m_dts.getPose().getRotation().getDegrees());
    double angleOffset = -Units.degreesToRadians(90); 

    // double heading = Math.atan(deltaRobotX/deltaRobotY)+botRadians+ angleOffset;
    heading_fixMe = Math.atan(deltaRobotX/Math.abs(deltaRobotY))+botRadians+ angleOffset;
    double finalAngle = visionYa + finalYa + Units.radiansToDegrees(botRadians);

    // double transformationAngle = botRadians; 

    // double deltaFieldX = ((deltaRobotX*Math.cos(transformationAngle))+ -(deltaRobotY*Math.sin(transformationAngle)));
    // double deltaFieldY = (deltaRobotX*Math.sin(transformationAngle))+ (deltaRobotY*Math.cos(transformationAngle));

    // We need to change the direction of the botRadions to get the correct transformation
    // TODO: This needs to be documented with drawings and pictures.
    double transformationAngle = -1* botRadians;

    double deltaFieldX = ((deltaRobotX*Math.cos(transformationAngle))+ -(deltaRobotY*Math.sin(transformationAngle)));
    double deltaFieldY = (deltaRobotX*Math.sin(transformationAngle))+ (deltaRobotY*Math.cos(transformationAngle));

    // deltaFieldX -= -0.191819 * visionYa;
    // deltaFieldY -=  0.21868  * visionYa;

    SmartDashboard.putNumber("x_vt", x_vt);
    SmartDashboard.putNumber("z_vt", z_vt);
    SmartDashboard.putNumber("xPrime", xPrime);
    SmartDashboard.putNumber("zPrime", zPrime);
    SmartDashboard.putNumber("deltaRobotX", deltaRobotX);
    SmartDashboard.putNumber("deltaRobotY", deltaRobotY);
    SmartDashboard.putNumber("visionAuto.botRadians", botRadians);
    SmartDashboard.putNumber("visionAuto.heading", heading_fixMe);
    SmartDashboard.putNumber("deltaFieldX", deltaFieldX);
    SmartDashboard.putNumber("deltaFieldY", deltaFieldY);
    SmartDashboard.putNumber("finalAngle", finalAngle);

    return new Pose2d(
      Units.inchesToMeters(deltaFieldX), 
      Units.inchesToMeters(deltaFieldY), 
      new Rotation2d(Units.degreesToRadians(finalAngle))
    );
  }

  public Command visionCreatePath(double xPrime, double zPrime, double finalYa){
    // since it is known that xPrime must be non-zero, we should just add 0.000001 to prevent
    // a division by zero.  This protects us from inadvertently providing an invalid answer and allows 
    // us to specify 0 for xPrime when this function is called which is more intuitive than forcing the 
    // user of this function to enter 0.0001 manually to avoid an error.

    //when defining zPrime and xPrime zPrime is positive going behind the april tag and xPrime is positive right of the april tag
    xPrime += 0.00000112358;
    
    m_ots.data();
    // while(m_ots.getNearestAprilTagDetection() == null){
    //   try {
    //     wait(10);
    //   } catch (InterruptedException e) {
    //     // TODO Auto-generated catch block
    //     e.printStackTrace();
    //   }
    // }
    try{
      visionX = m_ots.visionX;
      visionY = m_ots.visionY;
      visionZ = m_ots.visionZ;
      visionYa = m_ots.visionYa;

      SmartDashboard.putNumber("Robot x", m_dts.getPose().getX());
      SmartDashboard.putNumber("Robot y", m_dts.getPose().getY());
      SmartDashboard.putNumber("Robot rot", m_dts.getPose().getRotation().getDegrees());

      SmartDashboard.putNumber("visionXInitial", visionX);
      SmartDashboard.putNumber("visionYInitial", visionY);
      SmartDashboard.putNumber("visionZInitial", visionZ);
      SmartDashboard.putNumber("visionYaInitial", visionYa);

    }
    catch(Exception e) {
      System.out.println(e);
    }

    // visionX = m_ots.visionX;
    // visionZ = m_ots.visionZ;
    // visionY = m_ots.visionY;
    SmartDashboard.putNumber("visionXAuto", visionX);
    SmartDashboard.putNumber("visionYAuto", visionY);
    SmartDashboard.putNumber("visionZAuto", visionZ);
    SmartDashboard.putNumber("visionYaAuto", visionYa);

    Pose2d botPose = m_dts.getPose();

    
    // SmartDashboard.putNumber("deltaFieldX", deltaFieldX);
    // SmartDashboard.putNumber("deltaFieldY", deltaFieldY);
    // ---
    // Input for the following is x prime and z prime offsets from the april tag
    // need Alpha =
    // if the tote is on the left side of the robot nad the robot has to turn to the left to line up we ned +15, if robot needs to turn to the right we need -15
    // TODO: Camera center of rotation isn't working, needs to be fixed
    // double xPrime = 15;  //-13.5
    // double zPrime = -15; //8.5

    // double xPrimeSign = xPrime / Math.abs(xPrime);
    // // This is the original equation works for negative xPrime however doesn't work for positive xPrime
    // double alpha = Math.atan(zPrime/(-xPrime));
    // Taking the negative of the absolute value "fixes" it but we should really figure out equations and draw the pictures nicely.
    // double alpha = Math.atan(zPrime/Math.abs(xPrime));

    // // visionYa is in degrees
    // // need Phi = 
    // double phi = alpha - Math.toRadians(-visionYa)*(-1*xPrimeSign);
    // // need c =
    // double c = Math.sqrt(Math.pow(zPrime, 2) + Math.pow(xPrime, 2));
    // // need z_t = 
    // double z_t = c*Math.sin(phi);
    // // need x_t = 
    // double x_t = c*Math.cos(phi);
    // subtract z_t and X_t from vision x and vision z before calculating delta robot x and y
    

    // SmartDashboard.putNumber("x_t", x_t);
    // SmartDashboard.putNumber("z_t", z_t);
    // SmartDashboard.putNumber("alpha", alpha);
    // SmartDashboard.putNumber("phi", phi);
    // SmartDashboard.putNumber("xPrimeSign", xPrimeSign);

    // // ---
    // double deltaRobotX = -1* Units.inchesToMeters(visionX-x_t*(-1*xPrimeSign)); // We are facing the april tag first so there is no need to change in robot x
    // double deltaRobotY =  -1* Units.inchesToMeters(visionZ-z_t); // We want to end our auto 1 meter away from the apriltag

    // SmartDashboard.putNumber("deltaRobotX in inches", Units.metersToInches(deltaRobotX));
    // SmartDashboard.putNumber("deltaRobotY in inches", Units.metersToInches(deltaRobotY));
    // double botRadians = Units.degreesToRadians(m_dts.m_gyro.getAngle());
    // // //double botRadians = m_dts.getPose().getRotation().getRadians();
    // // SmartDashboard.putNumber("botRadians", botRadians);

    // double angleOffset = -Units.degreesToRadians(90); 
    // double heading = Math.atan(deltaRobotX/deltaRobotY)+botRadians+ angleOffset;

    // finalYa is in degrees
    // double finalYa = 0;
    // finalAngle is in degrees
    // double finalAngle = -visionYa + finalYa + Units.radiansToDegrees(botRadians);
    // //System.out.println("BOT RADIANS BOT RADIANS " + botRadians);

    // SmartDashboard.putNumber("finalAngle", finalAngle);
    
    // Figure out the trigonometri which converts deltaRobotX and deltaRobotY to deltaFieldX and deltaFieldY
    // double deltaFieldX = ((deltaRobotX*Math.cos(botRadians))+ (deltaRobotY*Math.sin(botRadians)));
    // double deltaFieldY = -(deltaRobotX*Math.sin(botRadians))+ (deltaRobotY*Math.cos(botRadians));

    // if(visionYa < 0){
    //    deltaFieldY = -(deltaRobotX*Math.sin(botRadians))+ (deltaRobotY*Math.cos(botRadians));
    // }
    // else{
    //    deltaFieldY = (deltaRobotX*Math.sin(botRadians))+ (deltaRobotY*Math.cos(botRadians));
    // }

    //deltaFieldX *=-1;
    // deltaFieldY += 1 + Units.inchesToMeters(13.5);

    // double x_vt = xPrime * Math.cos(Math.toRadians(visionYa)) + zPrime * Math.sin(Math.toRadians(visionYa));
    // double z_vt = xPrime * Math.sin(Math.toRadians(visionYa)) + zPrime * Math.cos(Math.toRadians(visionYa));

    // double deltaRobotX = -1 * (visionX + x_vt);
    // double deltaRobotY = -1 * (visionZ + z_vt);

    // double botRadians = Units.degreesToRadians(m_dts.m_gyro.getAngle());
    // double angleOffset = -Units.degreesToRadians(90); 

    // double heading = Math.atan(deltaRobotX/deltaRobotY)+botRadians+ angleOffset;
    // double finalAngle = -visionYa + finalYa + Units.radiansToDegrees(botRadians);

    // double deltaFieldX = ((deltaRobotX*Math.cos(botRadians))+ (deltaRobotY*Math.sin(botRadians)));
    // double deltaFieldY = -(deltaRobotX*Math.sin(botRadians))+ (deltaRobotY*Math.cos(botRadians));

    // SmartDashboard.putNumber("x_vt", x_vt);
    // SmartDashboard.putNumber("z_vt", z_vt);
    // SmartDashboard.putNumber("xPrime", xPrime);
    // SmartDashboard.putNumber("xPrime", xPrime);
    // SmartDashboard.putNumber("zPrime", xPrime);
    // SmartDashboard.putNumber("deltaRobotX", deltaRobotX);
    // SmartDashboard.putNumber("deltaRobotY", deltaRobotY);
    // SmartDashboard.putNumber("visionAuto.botRadians", botRadians);
    // SmartDashboard.putNumber("visionAuto.heading", heading);
    // SmartDashboard.putNumber("deltaFieldX", deltaFieldX);
    // SmartDashboard.putNumber("deltaFieldY", deltaFieldY);
    // SmartDashboard.putNumber("finalAngle", finalAngle);
    Pose2d fieldDeltaPose = visionAutoData(xPrime, zPrime, finalYa);
    System.out.println("VisionAutoCommand running!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    // SmartDashboard.putNumber("deltaRobotX", deltaRobotX);
    // SmartDashboard.putNumber("deltaRobotY", deltaRobotY);
    // SmartDashboard.putNumber("deltaFieldX", deltaFieldX);
    // SmartDashboard.putNumber("deltaFieldY", deltaFieldY);

    // SmartDashboard.putNumber("VisionAuto.heading", heading);

    // return new SequentialCommandGroup(
    //   new InstantCommand(()->SmartDashboard.putNumber("dts.getPose() x before",m_dts.getPose().getX())),
    //   new InstantCommand(()->SmartDashboard.putNumber("dts.getPose() y before",m_dts.getPose().getY())),
    //   new InstantCommand(()->SmartDashboard.putNumber("dts.getPose() rotation before",m_dts.getPose().getRotation().getDegrees())),
    //   //new InstantCommand(() -> m_dts.resetAngle()).withTimeout(0.1),

    //   m_dts.createVisionPath(
    //     new Pose2d(
    //       botPose.getX(), 
    //       botPose.getY(), 
    //       new Rotation2d(heading)   // TODO need to explain this rotation offset and point to docs
    //     ), 
    //     new Translation2d(
    //       botPose.getX()+(deltaFieldX/2), 
    //       botPose.getY()+(deltaFieldY/2)
    //     ), 
    //     new Pose2d(
    //       botPose.getX()+deltaFieldX,
    //       botPose.getY()+deltaFieldY, 
    //       new Rotation2d(heading)
    //     ),
    //     finalAngle //- m_dts.getPose().getRotation().getDegrees()//heading+(Math.PI/2)
    //     // ,true
    //   ),
    //   new InstantCommand(()->m_dts.stopMotors()),
    //   new InstantCommand(()->SmartDashboard.putNumber("dts.getPose() x after",m_dts.getPose().getX())),
    //   new InstantCommand(()->SmartDashboard.putNumber("dts.getPose() y after",m_dts.getPose().getY())),
    //   new InstantCommand(()->SmartDashboard.putNumber("dts.getPose() rotation after",m_dts.getPose().getRotation().getDegrees()))
    // );

    return new SequentialCommandGroup(
      new InstantCommand(()->SmartDashboard.putNumber("dts.getPose() x before",m_dts.getPose().getX())),
      new InstantCommand(()->SmartDashboard.putNumber("dts.getPose() y before",m_dts.getPose().getY())),
      new InstantCommand(()->SmartDashboard.putNumber("dts.getPose() rotation before",m_dts.getPose().getRotation().getDegrees())),
      m_dts.createVisionPath(
        new Pose2d(
          botPose.getX(), 
          botPose.getY(), 
          new Rotation2d(heading_fixMe)   // TODO need to explain this rotation offset and point to docs
        ), 
        new Translation2d(
          botPose.getX()+(fieldDeltaPose.getX()/2), 
          botPose.getY()+(fieldDeltaPose.getY()/2)
        ), 
        new Pose2d(
          botPose.getX()+fieldDeltaPose.getX(),
          botPose.getY()+fieldDeltaPose.getY(), 
          new Rotation2d(heading_fixMe)
        ),
        fieldDeltaPose.getRotation().getDegrees()
        // finalAngle //heading+(Math.PI/2)
        // ,true
      ),
      new InstantCommand(()->m_dts.stopMotors()),
      new InstantCommand(()->SmartDashboard.putNumber("dts.getPose() x after",m_dts.getPose().getX())),
      new InstantCommand(()->SmartDashboard.putNumber("dts.getPose() y after",m_dts.getPose().getY())),
      new InstantCommand(()->SmartDashboard.putNumber("dts.getPose() rotation after",m_dts.getPose().getRotation().getDegrees()))
    );

    // return new Command() {
      
    // };
  }
}
