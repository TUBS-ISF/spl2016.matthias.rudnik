package de.mathias.elevator;

import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElevatorState {
	private int minFloor;
	private int maxFloor;
	private int maxHeight;
	private TreeSet<Integer> jobs;
	private int floor = 0;
	// 100 height units per floor; position relative from bottom of elevator to
	// bottom of minFloor
	private int position;
	private boolean running;
	private Door frontDoor;
	private Door backDoor;
	
	
	public ElevatorState(int minFloor, int maxFloor, int maxHeight,
			TreeSet<Integer> jobs, int floor, int position, boolean running, Door frontDoor, Door backDoor) {
		super();
		this.minFloor = minFloor;
		this.maxFloor = maxFloor;
		this.maxHeight = maxHeight;
		this.jobs = jobs;
		this.floor = floor;
		this.position = position;
		this.running = running;
		this.frontDoor = frontDoor;
		this.backDoor = backDoor;
	}
	
	@JsonProperty
	public Door getFrontDoor() {
		return frontDoor;
	}

	@JsonProperty
	public Door getBackDoor() {
		return backDoor;
	}

	@JsonProperty
	public int getMinFloor() {
		return minFloor;
	}
	@JsonProperty
	public int getMaxFloor() {
		return maxFloor;
	}
	@JsonProperty
	public int getMaxHeight() {
		return maxHeight;
	}
	@JsonProperty
	public TreeSet<Integer> getJobs() {
		return jobs;
	}
	@JsonProperty
	public int getFloor() {
		return floor;
	}
	@JsonProperty
	public int getPosition() {
		return position;
	}
	@JsonProperty
	public boolean isRunning() {
		return running;
	}
	
}
