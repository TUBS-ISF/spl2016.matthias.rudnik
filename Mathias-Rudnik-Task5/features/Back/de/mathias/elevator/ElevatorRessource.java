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
	@GET
	@Timed
	public ElevatorState getElevatorState() {
		ElevatorState st = original();
		st.setBackDoor(e.getBackDoor());
		return st;
	}
}