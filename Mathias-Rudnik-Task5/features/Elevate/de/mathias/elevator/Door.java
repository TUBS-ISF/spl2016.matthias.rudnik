package de.mathias.elevator;

public interface Door {
	enum State {OPEN, CLOSED, OPENING, CLOSING};
	public State getState();
	public boolean isClosed();
	public void open();
	public void close();
}
