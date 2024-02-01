
import lejos.hardware.Button;

public class Main2 {

	private static DataExchange DE;
	private static ColorSensor CS;
	private static ObstacleDetector OD;
	private static Motors MO;
	private static SendData SD;
	private static ReceiveData RD;

	public static void main(String[] args) {

		// Create data exchange object
		DE = new DataExchange();

		// Create line follower thread
		CS = new ColorSensor(DE);

		// Create obstacle detector thread
		OD = new ObstacleDetector(DE);

		// Create motor thread
		MO = new Motors(DE);
		
		// Create send data thread
		SD = new SendData(DE);
		
		// Create receive data thread
		RD = new ReceiveData(DE);

		// Start threads
		CS.start();
		OD.start();
		MO.start();
		SD.start();
		RD.start();
		
		// Press any button to exit the program
		Button.waitForAnyPress();
		System.exit(0);
	}
}
