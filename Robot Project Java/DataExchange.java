/**
 * A class for exchanging data between different parts of the robot program.
 */
public class DataExchange {

	// ObstacleDetector
	private volatile boolean obstacleDetected = false;
	// Avoid obstacle
	private volatile boolean avoidObstacle = false;

	// Robot follow commands
	public final static int FOLLOW_LINE = 1;
	public final static int END = 2;
	public final static int AVOID = 3;

	private int command = FOLLOW_LINE; // The current robot command
	private float deviation = 0; // The deviation of the robot from the line
	private int speedmulti = 0;
	private int acceleration = 0;
	private float black_value = 0;
	private float white_value = 0;
	private float rcurrent_speed = 0;    
	private float lcurrent_speed = 0;

	/**
	 * Sets the obstacle detection status.
	 * 
	 * @param status the new status of obstacle detection
	 */
	public void setObstacleDetected(boolean status) {
		this.obstacleDetected = status;
	}

	/**
	 * Returns the obstacle detection status.
	 * 
	 * @return true if obstacle is detected, false otherwise
	 */
	public boolean getObstacleDetected() {
		return obstacleDetected;
	}

	/**
	 * Sets the avoidance status of obstacle.
	 * 
	 * @param avoid the new avoidance status of obstacle
	 */
	public void setAvoidObstacle(boolean avoid) {
		this.avoidObstacle = avoid;
	}

	/**
	 * Returns the avoidance status of obstacle.
	 * 
	 * @return true if obstacle avoidance is enabled, false otherwise
	 */
	public boolean getAvoidObstacle() {
		return avoidObstacle;
	}

	/**
	 * Sets the current robot command.
	 * 
	 * @param command the new command for the robot to execute
	 */
	public void setCommand(int command) {
		this.command = command;
	}

	/**
	 * Returns the current robot command.
	 * 
	 * @return the current robot command
	 */
	public int getCommand() {
		return command;
	}

	/**
	 * Returns the deviation of the robot from the line.
	 * 
	 * @return the deviation of the robot from the line
	 */
	public float getDeviation() {
		return deviation;
	}

	/**
	 * Sets the deviation of the robot from the line.
	 * 
	 * @param deviation the new deviation of the robot from the line
	 */
	public void setDeviation(float deviation) {
		this.deviation = deviation;
	}

	public int getSpeedmulti() {
		return speedmulti;
	}

	public void setSpeedmulti(int speedmulti) {
		this.speedmulti = speedmulti;
	}

	public int getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(int acceleration) {
		this.acceleration = acceleration;
	}

	public float getBlack_value() {
		return black_value;
	}

	public void setBlack_value(float black_value) {
		this.black_value = black_value;
	}

	public float getWhite_value() {
		return white_value;
	}

	public void setWhite_value(float white_value) {
		this.white_value = white_value;
	}

	public float getRcurrent_speed() {
		return rcurrent_speed;
	}

	public void setRcurrent_speed(float rcurrent_speed) {
		this.rcurrent_speed = rcurrent_speed;
	}

	public float getLcurrent_speed() {
		return lcurrent_speed;
	}

	public void setLcurrent_speed(float lcurrent_speed) {
		this.lcurrent_speed = lcurrent_speed;
	}
}
