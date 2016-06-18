package de.mathias.elevator;

import emergency.EmergencyNotification;

/**
 * TODO description
 */
public class Elevator {
	private Door frontDoor;
	public Elevator(int minFloor, int maxFloor, FloorNotification fNotification, EmergencyNotification eNotification) {
		this.frontDoor = new FrontDoor();
	}
	public Door getFrontDoor() {
		return this.frontDoor;
	}
	
	private void openDoor() {
		frontDoor.open();
		original();
	}
	
	private synchronized void closeDoor() {
		frontDoor.close();
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
		original();
	}
}