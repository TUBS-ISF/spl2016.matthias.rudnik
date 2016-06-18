package de.mathias.elevator;

import emergency.EmergencyNotification;

/**
 * TODO description
 */
public class Elevator {
	private Door backDoor;
	public Elevator(int minFloor, int maxFloor, FloorNotification fNotification, EmergencyNotification eNotification) {
		this.backDoor = new BackDoor();
	}
	public Door getBackDoor() {
		return this.backDoor;
	}

	private void openDoor() {
		backDoor.open();
		original();
	}

	private synchronized void closeDoor() {
		backDoor.close();
		System.out.println("waiting for back door to close");
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
		original();
	}
}