import de.mathias.elevator.BackDoor;
import de.mathias.elevator.Door;

public aspect Back {
	public Door de.mathias.elevator.Elevator.backDoor = new BackDoor();

	public Door de.mathias.elevator.Elevator.getBackDoor() {
		if(backDoor != null)
			return this.backDoor;
		return null;
	}

}