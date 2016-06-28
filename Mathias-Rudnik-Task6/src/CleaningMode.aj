public aspect CleaningMode {
	public void de.mathias.elevator.Elevator.enableCleaningMode() {
		synchronized (this) {
			if (state == State.WAITING) {
				state = State.CLEANING;
				this.notifyAll();
			}
		}
	}

	public void de.mathias.elevator.Elevator.disableCleaningMode() {
		synchronized (this) {
			if (state == State.CLEANING) {
				state = State.WAITING;
				this.notifyAll();
			}
		}
	}
}