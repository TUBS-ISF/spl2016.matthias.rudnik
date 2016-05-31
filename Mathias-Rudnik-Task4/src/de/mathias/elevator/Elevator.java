package de.mathias.elevator;

import java.util.Collections;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import emergency.EmergencyNotification;
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
	private Motor motor;
	private FloorNotification fNotification;
	private EmergencyNotification eNotification;


	private enum State {
		MOVING, OPENING, WAITING, CLOSING, CLEANING, POLICE
	}

	private enum Direction {
		UP, DOWN
	}

	private State state;
	private Direction dir;

	public Elevator(int minFloor, int maxFloor, Door frontDoor, Door backDoor, Motor motor, FloorNotification fNotification, EmergencyNotification eNotification) {
		this.fNotification = fNotification;
		this.eNotification = eNotification;
		this.motor = motor;
		this.frontDoor = frontDoor;
		this.backDoor = backDoor;
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
	}

	public Door getFrontDoor() {
		if(frontDoor != null)
			return this.frontDoor;
		return null;
	}

	// #if Back
	// @ public Door getBackDoor() {
	// @ return this.backDoor;
	// @ }
	// #endif

	public void startElevator() {
		this.running = true;
		elevatorThread = new Thread(this);
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
		while (!reachedFloorFromJobs && this.running) {
			do {
				position += (dir == Direction.UP) ? 1 : -1;
				try {
					motor.doStep();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (position % 100 != 0 && this.running);
			floor = positionToFloor(position);
			if (jobs.contains(floor)) {
				fNotification.reachedFloor(floor);
				jobs.remove(floor);
				synchronized (this) {
					state = State.OPENING;
				}
				reachedFloorFromJobs = true;
			}
		}
	}

	public void enablePoliceMode() {
		synchronized (this) {
			state = State.POLICE;
			this.notifyAll();
		}
	}

	public void disablePoliceMode() {
		synchronized (this) {
			if (state == State.POLICE)
				state = State.WAITING;
		}
	}

	public void enableCleaningMode() {
		if (PropertyManager.getProperty("CleaningMode"))
			synchronized (this) {
				if (state == State.WAITING) {
					state = State.CLEANING;
					this.notifyAll();
				}
			}
	}

	public void disableCleaningMode() {
		if (PropertyManager.getProperty("CleaningMode"))
			synchronized (this) {
				if (state == State.CLEANING) {
					state = State.WAITING;
					this.notifyAll();
				}
			}
	}

	private void openDoor() {
		System.out.println("now realy opening doors");
		// #if Front
		frontDoor.open();
		// #endif
		// #if Back
		// @ backDoor.open();
		// #endif
		synchronized (this) {
			state = State.WAITING;
		}

	}

	public void closeDoorImmediately() {
		if (PropertyManager.getProperty("CloseDoor"))
			synchronized (this) {
				if (state == State.WAITING && !jobs.isEmpty()) {
					state = State.CLOSING;
					this.notifyAll();
				}
			}
	}

	public void openDoorImmediately() {
		if (PropertyManager.getProperty("OpenDoor"))
			synchronized (this) {
				if ((state == State.WAITING || state == State.CLOSING)
						&& !jobs.isEmpty())
					state = State.OPENING;
				this.notifyAll();
			}
	}

	private synchronized void closeDoor() {
		// #if Front
		frontDoor.close();
		// #endif

		// #if Back
		// @ backDoor.close();
		// #endif

		// #if Front
		System.out.println("waiting for front door to close");
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
		// #endif

		// #if Back
//@		System.out.println("waiting for back door to close");
//@		while (!backDoor.isClosed() && state == State.CLOSING) {
//@			try {
//@				synchronized (this) {
//@					this.wait(100);
//@				}
//@			} catch (InterruptedException e) {
//@				e.printStackTrace();
//@				return;
//@			}
//@		}
		// #endif

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
		try {
			synchronized (this) {
				this.wait(5000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
		synchronized (this) {
			if (state == State.WAITING)
				state = State.CLOSING;
		}
	}

	private void doCleaning() {
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
			case CLEANING:
				doCleaning();
				break;
			default:
				break;
			}
		}
	}
	//#if EmergencyMode
//@	public void enableEmegerency() {
//@		eNotification.handleEmergency();
//@		this.stopElevator();
//@	}
//@
//@	public void disableEmegerency() {
//@		this.startElevator();
//@	}
	//#endif
}
