package de.mathias.elevator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

/**
 * TODO description
 */
@Path("/elevator")
@Produces(MediaType.APPLICATION_JSON)
public class ElevatorRessource {
	@POST
	@Timed
	@Path("closeDoor")
	@Consumes(MediaType.APPLICATION_JSON)
	public int closeDoor(boolean b) {
		if (b) {
			System.out.println("Closing Door");
			e.closeDoorImmediately();
		}
		return 0;
	}
}