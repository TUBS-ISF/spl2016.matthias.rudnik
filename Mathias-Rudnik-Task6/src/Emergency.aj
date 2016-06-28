import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;



public aspect Emergency {
	declare precedence: EmergencyMode, Emergency;

	@POST
	@Timed
	@Path("emergency")
	@Consumes(MediaType.APPLICATION_JSON)
	public int de.mathias.elevator.ElevatorRessource.emergency(boolean b) {
		if (b) {
			System.out.println("Emergency mode enabled");
			e.enableEmegerency();
		}
		else{
			System.out.println("Emergency mode disabled");
			e.disableEmegerency();
		}
		return 0;
	}
}