import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;


public aspect CloseDoor {
	public void de.mathias.elevator.Elevator.closeDoorImmediately() {
		synchronized (this) {
			if (state == State.WAITING && !jobs.isEmpty()) {
				state = State.CLOSING;
				this.notifyAll();
			}
		}
	}
	
	@POST
	@Timed
	@Path("closeDoor")
	@Consumes(MediaType.APPLICATION_JSON)
	public int de.mathias.elevator.ElevatorRessource.closeDoor(boolean b) {
		if (b) {
			System.out.println("Closing Door");
			e.closeDoorImmediately();
		}
		return 0;
	}
}