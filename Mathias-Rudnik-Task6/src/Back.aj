import de.mathias.elevator.BackDoor;
import de.mathias.elevator.Door;
import de.mathias.elevator.Elevator;
import de.mathias.elevator.Elevator.State;
import de.mathias.elevator.ElevatorRessource;
import de.mathias.elevator.ElevatorState;

public aspect Back {
	public Door de.mathias.elevator.Elevator.backDoor = new BackDoor();

	public Door de.mathias.elevator.Elevator.getBackDoor() {
		if(backDoor != null)
			return this.backDoor;
		return null;
	}
	
	ElevatorState around(ElevatorRessource e): target(e) && execution(ElevatorState de.mathias.elevator.ElevatorRessource.getElevatorState()){
		ElevatorState s = proceed(e);
		s.setBackDoor(e.e.getBackDoor());
		return s;
	}
	
	before(Elevator e): target(e) && call(void de.mathias.elevator.Elevator.openDoor()){
		e.backDoor.open();
	}
	
	before(Elevator e): target(e) && call(void de.mathias.elevator.Elevator.closeDoor()){
		e.backDoor.close();
		System.out.println("waiting for front door to close");
		while (!e.backDoor.isClosed() && e.state == State.CLOSING) {
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