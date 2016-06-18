package de.mathias.elevator;

/**
 * TODO description
 */
public class Elevator {
	public void enableEmegerency() {
		eNotification.handleEmergency();
		this.stopElevator();
	}

	public void disableEmegerency() {
		this.startElevator();
	}
}