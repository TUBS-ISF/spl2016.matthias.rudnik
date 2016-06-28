package de.mathias.elevator;

public class FrontDoor implements Door{
	private Thread opening, closing;
	private State state;
	
	public FrontDoor() {
		super();
		this.state = State.CLOSED;
	}
	
	@Override
	public State getState() {
		return state;
	}
	
	@Override
	public boolean isClosed(){
		return (state == State.CLOSED) ? true : false;
	}
	
	@Override
	public void open(){
		if (closing != null)
			synchronized (this) {
				this.notifyAll();
			}
			
		state = State.OPENING;
		Runnable o = new Runnable() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						this.wait(500);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
				state = State.OPEN;
			}
		};
		opening = new Thread(o);
		opening.start();
	}
	
	@Override
	public void close(){
		if (opening != null)
			synchronized (this) {
				this.notifyAll();
			}
		state = State.CLOSING;
		Runnable c = new Runnable() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						System.out.println("door is closing");
						this.wait(500);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
				state = State.CLOSED;
			}
		};
		closing = new Thread(c);
		closing.start();

	}

}
