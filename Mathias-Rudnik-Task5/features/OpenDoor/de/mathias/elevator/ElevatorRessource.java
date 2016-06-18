package de.mathias.elevator;

/**
 * TODO description
 */
public class ElevatorRessource {
	@POST
	@Timed
	@Path("openDoor")
	@Consumes(MediaType.APPLICATION_JSON)
	public int openDoor(boolean b) {
		if (b) {
			System.out.println("Opening Door");
			e.openDoorImmediately();
		}
		return 0;
	}
}