package de.mathias.elevator;

public class Door{
	enum State {OPEN, CLOSED, OPENING, CLOSING};
	private Thread opening, closing;
	private State state;
	
	public Door() {
		super();
		this.state = State.CLOSED;
	}
	
	public State getState() {
		return state;
	}
	
	public boolean isClosed(){
		return (state == State.CLOSED) ? true : false;
	}
	
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
