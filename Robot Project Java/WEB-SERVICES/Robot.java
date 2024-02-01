package data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // This annotation indicates that this class is an entity that can be persisted to database
		
@Table(name = "lego_control_panel") // specifies the name of the database table
public class Robot {

	@Id // indicates that this field is the primary key of this entity
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "speedmulti") // This annotation specifies the name of the column in the database table
	private int speedmulti;

	@Column(name = "acceleration") // This annotation specifies the name of the column in the database table
	private int acceleration;

	@Column(name = "black_value") // This annotation specifies the name of the column in the database table
	private float black_value;

	@Column(name = "white_value") // This annotation specifies the name of the column in the database table
	private float white_value;

	// Getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	// Constructors for the class
	public Robot(int speedmulti, int acceleration, float black_value, float white_value) {
		this.speedmulti = speedmulti;
		this.acceleration = acceleration;
		this.black_value = black_value;
		this.white_value = white_value;
	}

	public Robot() {
		// Default constructor
	}
	
	//toString() method to return a string representation of the object
	@Override
	public String toString() {
		return "Robot{" + "id=" + id + ", speed=" + speedmulti + ", acceleration=" + acceleration + ", blackValue="
				+ black_value + ", whiteValue=" + white_value + '}';
	}
}
