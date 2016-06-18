package de.mathias.elevator;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * TODO description
 */
@Path("/elevator")
@Produces(MediaType.APPLICATION_JSON)
public class ElevatorRessource {
	@POST
	@Timed
	@Path("cleaningMode")
	@Consumes(MediaType.APPLICATION_JSON)
	public int enableCleaningMode(boolean b) {
		if (b) {
			System.out.println("Enabling Cleaning Mode");
			e.enableCleaningMode();
		} else {
			System.out.println("Disabling Cleaning Mode");
			e.disableCleaningMode();
		}
		return 0;
	}
}