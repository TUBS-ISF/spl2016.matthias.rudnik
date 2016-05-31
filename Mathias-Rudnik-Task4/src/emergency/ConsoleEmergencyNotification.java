package emergency;

public class ConsoleEmergencyNotification implements EmergencyNotification {

	@Override
	public void handleEmergency() {	
		System.out.println("HELP HELP HELP");
	}

}
