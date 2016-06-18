package de.mathias.elevator;

import de.mathias.elevator.Elevator;

/**
 * TODO description
 */
public class Elevator {
	public void openDoorImmediately() {
		synchronized (this) {
			if ((state == State.WAITING || state == State.CLOSING)
					&& !jobs.isEmpty())
				state = State.OPENING;
			this.notifyAll();
		}
	}
}