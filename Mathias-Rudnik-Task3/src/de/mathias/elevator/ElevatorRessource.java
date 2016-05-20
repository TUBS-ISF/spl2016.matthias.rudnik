package de.mathias.elevator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;

@Path("/elevator")
@Produces(MediaType.APPLICATION_JSON)
public class ElevatorRessource {
	private final AtomicLong counter;
	private Elevator e;

	public ElevatorRessource(Elevator e) {
		this.counter = new AtomicLong();
		this.e = e;
	}

	@GET
	@Timed
	public ElevatorState getElevatorState() {
		return new ElevatorState(e.getMinFloor(), e.getMaxFloor(),
				e.getMaxHeight(), e.getJobs(), e.getFloor(), e.getPosition(),
				e.isRunning(),
				// #if Front
				e.getFrontDoor(),
				// #else
				// @ null,
				// #endif
				// #if Back
				// @ e.getBackDoor()
				// #else
				null
		// #endif
		);
	}

	static class Job {
		@JsonProperty
		String floor;
	}

	@GET
	@Timed
	@Path("job")
	@Produces(MediaType.APPLICATION_JSON)
	public Job updateRecord() {
		Job j = new Job();
		j.floor = "" + e.getFloor();
		return j;
	}

	//#if CloseDoor
//@	@POST
//@	@Timed
//@	@Path("closeDoor")
//@	@Consumes(MediaType.APPLICATION_JSON)
//@	public int closeDoor(boolean b) {
//@		if (b) {
//@			System.out.println("Closing Door");
//@			e.closeDoorImmediately();
//@		}
//@		return 0;
//@	}
	//#endif
	
	//#if EmergencyMode
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
	//#endif
	
	//#if OpenDoor
//@	@POST
//@	@Timed
//@	@Path("openDoor")
//@	@Consumes(MediaType.APPLICATION_JSON)
//@	public int openDoor(boolean b) {
//@		if (b) {
//@			System.out.println("Opening Door");
//@			e.openDoorImmediately();
//@		}
//@		return 0;
//@	}
	//#endif
	
	//#ifdef CleaningMode
//@	@POST
//@	@Timed
//@	@Path("cleaningMode")
//@	@Consumes(MediaType.APPLICATION_JSON)
//@	public int enableCleaningMode(boolean b) {
//@		if (b) {
//@			System.out.println("Enabling Cleaning Mode");
//@			e.enableCleaningMode();
//@		} else {
//@			System.out.println("Disabling Cleaning Mode");
//@			e.disableCleaningMode();
//@		}
//@		return 0;
//@	}
	//#endif

	@POST
	@Timed
	@Path("job")
	@Consumes(MediaType.APPLICATION_JSON)
	public int updateRecord(Job job) {
		System.out.println("going to floor " + job.floor);
		e.goToFloor(Integer.parseInt(job.floor));
		return 0;
	}
}
