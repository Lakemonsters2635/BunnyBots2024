## Future investigations

* Figure out how we generate trajectory and if `TrajGenerator.java` is helpful or how we do it in FRC2024 is better.
* Figure out if we actually need to set `isRedAlliance` to false in `DrivetrainSubsystem.createPath()`  
* Figure out if we can simplify the multiplying by * -1 later in `DrivetrainSubsystem.drive()`
* Fix Documentation on LoggingState, actualLoggingState, and Advantage Scope in `DrivetrainSybsystem.periodic()`
* Figure out what data we can get of SparkMax and Krakens.. Desired Data: Voltage, Current, Speed, Position/Encoder Position, Temperature
  * Krakens: 
    * ASDF
  * SparkMax
    * ASDF
    * `getBusVoltage()`
* Figure out how to read Amps from the power distribution
  ```java
  import edu.wpi.first.wpilibj.PowerDistributionPanel;
  public class Robot extends TimedRobot {
      private PowerDistributionPanel pdp = new PowerDistributionPanel();
      @Override
      public void robotPeriodic() {
          // Read the current draw on channel 0
          double current = pdp.getCurrent(0);
          System.out.println("Current on channel 0: " + current + "A");
      }
  }
  ```
* Figure out which motors have embedded temperature sensors, and how to get these values.