import lejos.hardware.Sound;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class Motors extends Thread {

	private DataExchange DE;
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor rightMotor;

	public Motors(DataExchange dataExchange) {
		this.DE = dataExchange;
		leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
		rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	}

	// Override the run() method of the Thread class
	@Override
	public void run() {

		while (true) {

			// Acceleration for the motors
			leftMotor.setAcceleration(DE.getAcceleration());
			rightMotor.setAcceleration(DE.getAcceleration());

			if (DE.getCommand() == DataExchange.FOLLOW_LINE) { // Follow line command

				// Set the speed of the motors
				leftMotor.setSpeed(200 + (int) (DE.getDeviation() * DE.getSpeedmulti()));
				rightMotor.setSpeed(200 - (int) (DE.getDeviation() * DE.getSpeedmulti()));
				
				// Store speed of the motors into variables and pass them to DataExchange object
				float lcurrent_speed = leftMotor.getSpeed();
				float rcurrent_speed = rightMotor.getSpeed();
				DE.setLcurrent_speed(lcurrent_speed);
				DE.setRcurrent_speed(rcurrent_speed);

				// Set the motors go forward
				leftMotor.forward();
				rightMotor.forward();

			} else if (DE.getCommand() == DataExchange.END) { // End routine command

				// Stop the motors
				leftMotor.stop();
				rightMotor.stop();

				// Make the robot spin...
				leftMotor.setSpeed(600);
				rightMotor.setSpeed(600);
				leftMotor.backward();
				rightMotor.forward();

				// ...and play some sounds
				Sound.playTone(400, 1000);
				Sound.playTone(600, 1500);
				Sound.playTone(800, 1500);
				Sound.playTone(1000, 1000);

				// Make the ending show last 5sec
				Delay.msDelay(5000);

				// Finally, stop the motors and break the loop
				leftMotor.stop();
				rightMotor.stop();
				break;

			} else if (DE.getCommand() == DataExchange.AVOID) { // Avoid obstacle command

				// Move robot left for 1,8sec
				leftMotor.setSpeed(150);
				rightMotor.setSpeed(300);
				leftMotor.forward();
				rightMotor.forward();

				Delay.msDelay(1800);

				// Move robot right for 2sec
				leftMotor.setSpeed(300);
				rightMotor.setSpeed(100);
				leftMotor.forward();
				rightMotor.forward();

				Delay.msDelay(2000);

				// Move robot straight 1,4sec
				leftMotor.setSpeed(200);
				rightMotor.setSpeed(200);
				leftMotor.forward();
				rightMotor.forward();

				Delay.msDelay(1400);

				// Rotate the robot so it is easier for it to continue to follow a line
				leftMotor.rotate(90);
				Delay.msDelay(200);

				// Robot continues to follow a line
				DE.setCommand(DataExchange.FOLLOW_LINE);
			}
		}
	}
}
