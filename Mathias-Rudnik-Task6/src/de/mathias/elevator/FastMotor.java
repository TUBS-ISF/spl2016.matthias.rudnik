package de.mathias.elevator;

public class FastMotor implements Motor {

	@Override
	public void doStep() throws InterruptedException {
		/* Super complicated motor control code */
		Thread.sleep(10);
	}

}
