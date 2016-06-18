package de.mathias.elevator;

public class ConsoleFloorNotification implements FloorNotification {

	@Override
	public void reachedFloor(int floor) {
		System.out.println("Reached Floor: " + floor);
	}

}
