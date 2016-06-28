
public aspect EmergencyMode {
	public void de.mathias.elevator.Elevator.enableEmegerency() {
		eNotification.handleEmergency();
		this.stopElevator();
	}

	public void de.mathias.elevator.Elevator.disableEmegerency() {
		this.startElevator();
	}
}