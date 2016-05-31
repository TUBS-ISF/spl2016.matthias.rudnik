package de.mathias.elevator;

public class SlowMotor implements Motor {

	@Override
	public void doStep() throws InterruptedException {
		/* Super complicated motor control code */
		Thread.sleep(30);
	}

}
