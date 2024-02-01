package data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Speeddata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Primary key of the table with automatically generated value
    private int id;
    
    @Column(name="lcurrent_speed") //Column name to database
    private float lcurrent_speed;
    
    @Column(name="rcurrent_speed") //Column name to database
    private float rcurrent_speed;
    
    // Constructor with parameters		
    public Speeddata(float lcurrent_speed, float rcurrent_speed) {
        this.lcurrent_speed = lcurrent_speed;
        this.rcurrent_speed = rcurrent_speed;
    }
    
    // Default constructor
    public Speeddata() {
    }
    
    // Generate getters and setters for the fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public float getLcurrent_speed() {
		return lcurrent_speed;
	}

	public void setLcurrent_speed(float lcurrent_speed) {
		this.lcurrent_speed = lcurrent_speed;
	}

	public float getRcurrent_speed() {
		return rcurrent_speed;
	}

	public void setRcurrent_speed(float rcurrent_speed) {
		this.rcurrent_speed = rcurrent_speed;
	}
}
