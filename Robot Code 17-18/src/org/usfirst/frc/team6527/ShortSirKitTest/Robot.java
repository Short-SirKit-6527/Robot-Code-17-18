
package org.usfirst.frc.team6527.ShortSirKitTest;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team6527.ShortSirKitTest.commands.ExampleCommand;
import org.usfirst.frc.team6527.ShortSirKitTest.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Joystick;
import com.ctre.CANTalon;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	
	public VictorSP left;
	public VictorSP right; 
	public CANTalon arm;
	public XboxController xbox;
	public Joystick flightStick;
	public DigitalInput speedSwitch;
	public DigitalInput limitSwitch;
	public DigitalOutput ledshine;	
	public int ledTime;
	public int ledPercent;
	
	/** All of these are different things
	 * victorsp
	 * VICTORSP
	 * VictorSP
	 */

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		arm = new CANTalon(1);
		left = new VictorSP(1);
		right = new VictorSP(0);
		xbox = new XboxController(0);
		flightStick = new Joystick(1);
		speedSwitch = new DigitalInput(0);
		limitSwitch = new DigitalInput(1);
		ledshine = new DigitalOutput(2);
		ledTime = 0;
	    ledPercent = 0;
	}
	
	@Override
	public void robotPeriodic() {
		ledTime += 1;
		if(ledTime>255) ledTime = 0;
        ledshine.set(ledTime<=ledPercent);
	}
	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}
	
	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	
	//xbox.getRawButton(5)
	//xbox.getRawButton(6)
	
	// and &&
	//tt = t
	//ff = f
	//tf = f
	//ft = f
	
	// or ||
	//tt = t
	//ff = f
	//tf = t
	//ft = t
	
	// not !
	// t = f
	// f = t
	
	public double calcMove(boolean isLeft) {
		double value;
		if (xbox.getRawButton(6) && speedSwitch.get()) {
			return -1.0;
		}
		else {	
			if (xbox.getRawButton(5)) {
				value = 2.0;
			}
			else {
				value = 6.0;
			}
			if (isLeft) {
				return (xbox.getRawAxis(1) - xbox.getRawAxis(0)) / value;
			}
			else {
				return (xbox.getRawAxis(1) + xbox.getRawAxis(0)) / value;
			}
		}
	}
	
	
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		
		left.set(calcMove(true));
		right.set(calcMove(false));
		
		double armSpeed = flightStick.getRawAxis(1);
		if (armSpeed < 0 && limitSwitch.get()) {
			armSpeed = 0;
		}
		arm.set(armSpeed / -4);
		
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
