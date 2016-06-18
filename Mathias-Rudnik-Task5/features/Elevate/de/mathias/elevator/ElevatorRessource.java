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

/**
 * 
 * @author mathias
 *
 */
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
		ElevatorState st = new ElevatorState(e.getMinFloor(), e.getMaxFloor(),
				e.getMaxHeight(), e.getJobs(), e.getFloor(), e.getPosition(),
				e.isRunning(),
				null,
				null
		);
		return st;
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
