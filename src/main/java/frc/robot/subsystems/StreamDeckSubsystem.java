// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.AddressableLEDBuffer.IndexedColorIterator;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class StreamDeckSubsystem extends SubsystemBase {
  /** Creates a new StreamDeckSubsystem. */
  private NetworkTableInstance ntinst;

  private NetworkTable table;
  int listenerHandle;

  public StreamDeckSubsystem() {
    ntinst = NetworkTableInstance.getDefault();
    ntinst.setServer("127.0.0.1");
    table = ntinst.getTable("StreamDeck");
    ntinst.removeListener(0);

    clear();
  }

  public boolean isPressed(int index){
    boolean val = table.getBooleanTopic(index+"").getEntry(false).getAsBoolean();
    table.getBooleanTopic(index+"").getEntry(false).close();
    clear();
    return val;
  }

  public void clear(){
    for(String index: table.getKeys()){
      NetworkTableEntry entry = table.getEntry(index);
      entry.clearPersistent();
      entry.setDefaultValue(false);
    }
  }

  @Override
  public void periodic() {
    if(isPressed(0)){
      System.out.println("INDEX 0 IS PRESSED");
    }
    else{
      System.out.println("INDEX 0 IS NOT PRESSED");
    }
    // This method will be called once per scheduler run
  }
}
