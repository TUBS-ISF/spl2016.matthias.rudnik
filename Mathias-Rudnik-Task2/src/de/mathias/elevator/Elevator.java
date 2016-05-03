package de.mathias.elevator;

import java.util.Collections;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import properties.PropertyManager;

public class Elevator implements Runnable {
	private int minFloor;
	private int maxFloor;
	private int maxHeight;
	private NavigableSet<Integer> jobs;
	private int floor = 0;
	// 100 height units per floor; position relative from bottom of elevator to
	// bottom of minFloor
	private int position;
	private Thread elevatorThread;
	private boolean running;
	private Door frontDoor;
	private Door backDoor;

	private enum State {
		MOVING, OPENING, WAITING, CLOSING
	}

	private enum Direction {
		UP, DOWN
	}

	private State state;
	private Direction dir;

	public Elevator(int minFloor, int maxFloor) {
		if (PropertyManager.getProperty("Front"))
			this.frontDoor = new Door();
		if (PropertyManager.getProperty("Back"))
			this.backDoor = new Door();
		this.minFloor = minFloor;
		this.maxFloor = maxFloor;
		this.maxHeight = (maxFloor - minFloor) * 100;
		this.floor = minFloor;
		this.running = false;
		this.position = 0;
		this.state = State.WAITING;
		this.dir = Direction.UP;
		this.jobs = Collections
				.synchronizedNavigableSet(new TreeSet<Integer>());
		elevatorThread = new Thread(this);
	}

	public Door getFrontDoor() {
		return this.frontDoor;
	}

	public Door getBackDoor() {
		return this.backDoor;
	}

	public void startElevator() {
		this.running = true;
		elevatorThread.start();
	}

	public void stopElevator() {
		this.running = false;
	}

	public int getFloor() {
		return floor;
	}

	public int getMinFloor() {
		return minFloor;
	}

	public int getMaxFloor() {
		return maxFloor;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public TreeSet<Integer> getJobs() {
		return new TreeSet<Integer>(jobs);
	}

	public int getPosition() {
		return position;
	}

	public Thread getElevatorThread() {
		return elevatorThread;
	}

	public boolean isRunning() {
		return running;
	}

	public State getState() {
		return state;
	}

	public Direction getDir() {
		return dir;
	}

	public void goToFloor(int floor) {
		this.jobs.add(floor);
	}

	private int positionToFloor(int position) {
		return position / 100;
	}

	private void moveToNextFloor() {
		boolean reachedFloorFromJobs = false;
		while (!reachedFloorFromJobs) {
			do {
				position += (dir == Direction.UP) ? 1 : -1;
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (position % 100 != 0);
			floor = positionToFloor(position);
			if (jobs.contains(floor)) {
				jobs.remove(floor);
				synchronized (this) {
					state = State.OPENING;
				}
				reachedFloorFromJobs = true;
			}
		}
	}

	private void openDoor() {
		System.out.println("now realy opening doors");
		if (PropertyManager.getProperty("Front"))
			frontDoor.open();
		if (PropertyManager.getProperty("Back"))
			backDoor.open();
		synchronized (this) {
			state = State.WAITING;
		}

	}

	public void closeDoorImmediately() {
		synchronized (this) {
			if (state == State.WAITING && !jobs.isEmpty()) {
				state = State.CLOSING;
				this.notifyAll();
			}
		}
	}

	public void openDoorImmediately() {
		synchronized (this) {
			if ((state == State.WAITING || state == State.CLOSING)
					&& !jobs.isEmpty())
				state = State.OPENING;
			this.notifyAll();
		}
	}

	private synchronized void closeDoor() {
		if (PropertyManager.getProperty("Front"))
			frontDoor.close();

		if (PropertyManager.getProperty("Back"))
			backDoor.close();

		System.out.println("waiting for front door to close");
		if (PropertyManager.getProperty("Front"))
			while (!frontDoor.isClosed() && state == State.CLOSING) {
				try {
					synchronized (this) {
						this.wait(100);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}

		System.out.println("waiting for back door to close");
		if (PropertyManager.getProperty("Back"))
			while (!backDoor.isClosed() && state == State.CLOSING) {
				try {
					synchronized (this) {
						this.wait(100);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}

		System.out.println("now moving");
		synchronized (this) {
			if (state == State.CLOSING)
				state = State.MOVING;
			System.out.println("now realy moving");
		}
	}

	private void doWait() {
		if (jobs.isEmpty() || floor == jobs.last()) {
			jobs.pollLast();
			return;
		}
		if (dir == Direction.UP) {
			if (floor > jobs.last())
				dir = Direction.DOWN;
		} else {
			if (floor < jobs.first())
				dir = Direction.UP;
		}
		synchronized (this) {
			state = State.CLOSING;
		}
		try {
			synchronized (this) {
				this.wait(5000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void run() {
		while (running) {
			switch (state) {
			case MOVING:
				moveToNextFloor();
				break;
			case OPENING:
				openDoor();
				break;
			case WAITING:
				doWait();
				break;
			case CLOSING:
				closeDoor();
				break;
			default:
				break;
			}
		}
	}

}
