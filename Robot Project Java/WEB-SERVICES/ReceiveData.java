import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReceiveData extends Thread {

	private DataExchange DE;
	URL url = null;
	HttpURLConnection conn = null;
	InputStreamReader isr = null;
	BufferedReader br = null;
	String s = null;

	public ReceiveData(DataExchange dataExchange) {
		this.DE = dataExchange;
	}

	// Override the run() method of the Thread class
	@Override
	public void run() {

		while (true) {

			try {
				// Create a URL object with the URL to connect to
				url = new URL("http://192.168.0.103:8080/rest/testing/newrobovalues");

				// Open a connection to the URL
				conn = (HttpURLConnection) url.openConnection();

				// If the connection is null, print an error message
				if (conn == null) {
					System.out.println("No connection!!!");
				}

				// Get the input stream from the connection
				InputStream is = null;
				try {
					is = conn.getInputStream();
				} catch (Exception e) {
					// If there is an exception getting the input stream, print an error message
					System.out.println("Exception conn.getInputSteam()");
					e.printStackTrace();
					System.out.println("Cannot get InputStream!");
				}

				// Create an InputStreamReader and BufferedReader to read from the input stream
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);

				// Read a line from the BufferedReader
				s = br.readLine();

				// Split the line by spaces and convert each value to its appropriate data type
				String[] arvot = s.split(" ");
				int speedmulti = Integer.parseInt(arvot[0]);
				int acceleration = Integer.parseInt(arvot[1]);
				float black_value = Float.parseFloat(arvot[2]);
				float white_value = Float.parseFloat(arvot[3]);

				// Pass the values to DataExchange object
				DE.setSpeedmulti(speedmulti);
				DE.setAcceleration(acceleration);
				DE.setBlack_value(black_value);
				DE.setWhite_value(white_value);

			} catch (Exception e) {
				// If there is an exception, print an error message
				e.printStackTrace();
				System.out.println("Some problem!");
			}

			// Wait for 2 seconds before checking for new data again
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// If there is an exception while waiting, print an error message
				e.printStackTrace();
			}
		}
	}
}
