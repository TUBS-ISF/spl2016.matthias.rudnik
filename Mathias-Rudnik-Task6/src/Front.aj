import de.mathias.elevator.Door;
import de.mathias.elevator.Elevator;
import de.mathias.elevator.FrontDoor;
import de.mathias.elevator.Elevator.State;


public aspect Front {
	public Door de.mathias.elevator.Elevator.frontDoor = new FrontDoor();
	public Door de.mathias.elevator.Elevator.getFrontDoor() {
		if(frontDoor != null)
			return this.frontDoor;
		return null;
	}
	
	before(Elevator e): target(e) && call(void de.mathias.elevator.Elevator.openDoor()){
		e.frontDoor.open();
	}
	
	before(Elevator e): target(e) && call(void de.mathias.elevator.Elevator.closeDoor()){
		e.frontDoor.close();
		System.out.println("waiting for front door to close");
		while (!e.frontDoor.isClosed() && e.state == State.CLOSING) {
			try {
				synchronized (this) {
					this.wait(100);
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				return;
			}
		}
	}
}