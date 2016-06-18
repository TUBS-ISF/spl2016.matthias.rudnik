package de.mathias.elevator;

/**
 * TODO description
 */
public class Elevator {
	public void enableCleaningMode() {
		synchronized (this) {
			if (state == State.WAITING) {
				state = State.CLEANING;
				this.notifyAll();
			}
		}
	}
	public void disableCleaningMode() {
		synchronized (this) {
			if (state == State.CLEANING) {
				state = State.WAITING;
				this.notifyAll();
			}
		}
	}
}