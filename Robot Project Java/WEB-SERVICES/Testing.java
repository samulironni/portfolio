package services;

//Import required libraries
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import data.Robot;
import data.Speeddata;

@Path("/testing")
public class Testing {

	// Create an instance of the EntityManagerFactory class
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("robot");

	// Method to add new Robot values from the form to the database
	@POST
	@Path("/newrobotvalues")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Robot newRobotValues(Robot robot) {
		// Create a new EntityManager instance
		EntityManager em = emf.createEntityManager();
		// Start a new transaction
		em.getTransaction().begin();
		// Persist the Robot object in the database
		em.persist(robot);
		// Commit the transaction and close the entity manager
		em.getTransaction().commit();
		em.close();
		// Return the persisted Robot object
		return robot;
	}

	// Method to get the latest Robot statistics from the database
	@GET
	@Path("/getlatestvalues")
	@Produces(MediaType.APPLICATION_JSON)
	public Robot getLatestStats() {
		// Create a new EntityManager instance
		EntityManager em = emf.createEntityManager();
		// Get the maximum ID from the Robot table
		int maxId = (int) em.createQuery("SELECT MAX(R.id) FROM Robot R").getSingleResult();
		int lastId = maxId;
		// Get a list of all Robot objects with ID greater than or equal to lastId
		List<Robot> list = em.createQuery("SELECT R FROM Robot R WHERE R.id >= :lastId").setParameter("lastId", lastId)
				.getResultList();
		// Close the entity manager and entity manager factory
		em.close();
		emf.close();
		// Return the first Robot object in the list
		return list.get(0);
	}

	// Method to get the latest Speeddata from the database
	@GET
	@Path("/getdbspeed")
	@Produces(MediaType.APPLICATION_JSON)
	public Speeddata getDbSpeed() {
		// Create a new EntityManager instance
		EntityManager em = emf.createEntityManager();
		// Get the maximum ID from the Speeddata table
		int maxId = (int) em.createQuery("SELECT MAX(R.id) FROM Speeddata R").getSingleResult();
		int lastId = maxId;
		// Get a list of all Speeddata objects with ID greater than or equal to lastId
		List<Speeddata> list = em.createQuery("SELECT R FROM Speeddata R WHERE R.id >= :lastId")
				.setParameter("lastId", lastId).getResultList();
		// Close the entity manager and entity manager factory
		em.close();
		emf.close();
		// Return the first Speeddata object in the list
		return list.get(0);
	}

	// Method to send the latest values to robot from the database as a string
	@GET
	@Path("/newrobovalues")
	@Produces(MediaType.TEXT_PLAIN)
	public String newRoboValues() {
		// Create a new connection to the database
		EntityManager em = emf.createEntityManager();

		// Get the latest robot values from the database
		int maxId = (int) em.createQuery("SELECT MAX(R.id) FROM Robot R").getSingleResult();
		int lastId = maxId;

		List<Robot> list = em.createQuery("SELECT R FROM Robot R WHERE R.id >= :lastId").setParameter("lastId", lastId)
				.getResultList();

		em.close();
		emf.close();

		// Build and return a string with the latest robot values
		Robot r = list.get(0);
		String palaute = "" + r.getAcceleration() + " " + r.getSpeedmulti() + " " + r.getBlack_value() + " "
				+ r.getWhite_value();
		return palaute;
	}

	// Method to get the data from the robot as parameters and store it to database
	@GET
	@Path("/speedfromrobot/{lcurrent_speed}/{rcurrent_speed}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response speedFromRobot(@PathParam("lcurrent_speed") float lcurrentspeed,
			@PathParam("rcurrent_speed") float rcurrentspeed) {

		try {
			// Create a new connection to the database
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();

			// Create a new instance of the SpeedData entity class with the given values
			Speeddata speedData = new Speeddata();
			speedData.setLcurrent_speed(lcurrentspeed);
			speedData.setRcurrent_speed(rcurrentspeed);

			// Persist the SpeedData object in the database
			em.persist(speedData);

			// Commit the transaction and close the entity manager
			em.getTransaction().commit();
			em.close();

			// Return a successful response
			return Response.ok().build();
		} catch (Exception e) {
			// If there was an error, return an error response with the error message
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Failed to save data: " + e.getMessage()).build();
		}
	}
}
