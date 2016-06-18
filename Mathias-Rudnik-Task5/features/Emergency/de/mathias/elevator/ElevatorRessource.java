package de.mathias.elevator;

/**
 * TODO description
 */
public class ElevatorRessource {
	@POST
	@Timed
	@Path("emergency")
	@Consumes(MediaType.APPLICATION_JSON)
	public int emergency(boolean b) {
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