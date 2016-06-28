import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

public aspect Cleaning {
	@POST
	@Timed
	@Path("cleaningMode")
	@Consumes(MediaType.APPLICATION_JSON)
	public int de.mathias.elevator.ElevatorRessource.enableCleaningMode(boolean b) {
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