import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;


public aspect OpenDoor {
	public void de.mathias.elevator.Elevator.openDoorImmediately() {
		synchronized (this) {
			if ((state == State.WAITING || state == State.CLOSING)
					&& !jobs.isEmpty())
				state = State.OPENING;
			this.notifyAll();
		}
	}
	
	@POST
	@Timed
	@Path("openDoor")
	@Consumes(MediaType.APPLICATION_JSON)
	public int de.mathias.elevator.ElevatorRessource.openDoor(boolean b) {
		if (b) {
			System.out.println("Opening Door");
			e.openDoorImmediately();
		}
		return 0;
	}
}