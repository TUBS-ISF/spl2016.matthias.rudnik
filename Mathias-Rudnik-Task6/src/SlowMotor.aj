import de.mathias.elevator.Motor;


public aspect SlowMotor {
	public static class de.mathias.elevator.Elevator.SlowerMotor implements Motor{
		@Override
		public void doStep() throws InterruptedException {
			/* Super complicated motor control code */
			Thread.sleep(30);
		}

	}
	private Motor de.mathias.elevator.Elevator.motor = new de.mathias.elevator.Elevator.SlowerMotor();
}