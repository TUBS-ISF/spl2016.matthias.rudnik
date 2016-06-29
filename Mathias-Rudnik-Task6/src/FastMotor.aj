import de.mathias.elevator.Motor;


public aspect FastMotor {
	public static class de.mathias.elevator.Elevator.FasterMotor implements Motor{
		@Override
		public void doStep() throws InterruptedException {
			/* Super complicated motor control code */
			Thread.sleep(10);
		}

	}
	public Motor de.mathias.elevator.Elevator.motor = new de.mathias.elevator.Elevator.FasterMotor();
}