import de.mathias.elevator.Door;
import de.mathias.elevator.Elevator;
import de.mathias.elevator.Elevator.State;
import de.mathias.elevator.ElevatorRessource;
import de.mathias.elevator.ElevatorState;
import de.mathias.elevator.FrontDoor;


public aspect Front {
	public Door Elevator.frontDoor = new FrontDoor();
	
	public Door Elevator.getFrontDoor() {
		if(frontDoor != null)
			return this.frontDoor;
		return null;
	}
	
	ElevatorState around(ElevatorRessource e): target(e) && execution(ElevatorState de.mathias.elevator.ElevatorRessource.getElevatorState()){
		ElevatorState s = proceed(e);
		s.setFrontDoor(e.e.getFrontDoor());
		return s;
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