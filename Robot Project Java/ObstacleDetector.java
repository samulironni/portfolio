
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class ObstacleDetector extends Thread {

	private DataExchange DE;
	private EV3UltrasonicSensor us;
	private SampleProvider ultraDistance;
	private float[] ultraData;

	public ObstacleDetector(DataExchange dataExchange) {
		this.DE = dataExchange;
		us = new EV3UltrasonicSensor(SensorPort.S1);
		ultraDistance = us.getDistanceMode();
		ultraData = new float[ultraDistance.sampleSize()];
	}

	// Override the run() method of the Thread class
	@Override
	public void run() {

		// Introduce the variable for detections
		int numOfDetections = 0;

		while (true) {

			// Fetch the ultrasonic data and store the distance in a variable
			ultraDistance.fetchSample(ultraData, 0);
			float distance = ultraData[0] * 100;

			// Display the current distance on the LCD screen
			LCD.drawString("Distance: " + distance, 1, 5);
			LCD.refresh();

			if (distance <= 25) {// If the obstacle is detected within 25 cm

				// It increases the count of detections
				numOfDetections++;

				// Sets the 'obstacleDetected' flag to true in the DataExchange object
				DE.setObstacleDetected(true);

				// Print a message on the LCD and make a warning sound
				LCD.drawString("Obstacle detected " + numOfDetections + " time(s)", 1, 5);
				LCD.refresh();
				Sound.beep();

				// If it is the first detection, set the 'command' to 'AVOID'
				if (numOfDetections == 1) {
					DE.setCommand(DataExchange.AVOID);

					// If it is the third or more detection, set the 'command' to 'END'
				} else if (numOfDetections >= 3) {
					DE.setCommand(DataExchange.END);
				}
			}
		}
	}
}
