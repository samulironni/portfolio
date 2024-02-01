
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColorSensor extends Thread {

	private DataExchange DE;
	private EV3ColorSensor cs;
	private SampleProvider redSample;
	private float[] redSampleData;

	public ColorSensor(DataExchange dataExchange) {
		this.DE = dataExchange;
		cs = new EV3ColorSensor(SensorPort.S2);
		redSample = cs.getRedMode();
		redSampleData = new float[redSample.sampleSize()];
	}

	// Override the run() method of the Thread class
	@Override
	public void run() {

		// Set colorsensor on
		cs.setFloodlight(true);

		while (true) {

			// Read the value of the color sensor and store it in array
			redSample.fetchSample(redSampleData, 0);

			// Pattern to calculate deviation
			float deviation = (redSampleData[0] - (DE.getBlack_value() + DE.getWhite_value()) / 2);

			// Pass deviation variable to DataExchange object
			DE.setDeviation(deviation);
		}
	}
}
