// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.io.IOException;

import edu.wpi.first.cscore.CameraServerJNI;
import edu.wpi.first.math.WPIMathJNI;
import edu.wpi.first.networktables.BooleanTopic;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTablesJNI;
import edu.wpi.first.util.CombinedRuntimeLoader;
import edu.wpi.first.util.WPIUtilJNI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class streamDeckSubsystem{
  /** Creates a new streamDeckSubsystem. 
   * @throws IOException */
  public streamDeckSubsystem() throws IOException {
    NetworkTablesJNI.Helper.setExtractOnStaticLoad(false);
        WPIUtilJNI.Helper.setExtractOnStaticLoad(false);
        WPIMathJNI.Helper.setExtractOnStaticLoad(false);
        CameraServerJNI.Helper.setExtractOnStaticLoad(false);

        CombinedRuntimeLoader.loadLibraries(streamDeckSubsystem.class, "wpiutiljni", "wpimathjni", "ntcorejni",
                "cscorejnicvstatic");
        new streamDeckSubsystem().run();
  }

  public void run(){
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("StreamDeck");
    // DoubleSubscriber sub = table.getDoubleTopic("boolExample").subscribe(0.0); // Change the name of the topic
    inst.startClient4("StreamDeck");
    inst.setServerTeam(2635); 
    inst.startDSClient(); 
    while (true) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            System.out.println("interrupted");
            return;
        }
        BooleanTopic subRec = table.getBooleanTopic("0");
        String subVal = subRec.toString();
        System.out.println(subVal);
  }
  }
}
