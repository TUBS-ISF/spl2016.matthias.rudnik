package de.mathias.elevator;

/**
 * TODO description
 */
public class Elevator {
	public void closeDoorImmediately() {
		synchronized (this) {
			if (state == State.WAITING && !jobs.isEmpty()) {
				state = State.CLOSING;
				this.notifyAll();
			}
		}
	}
}