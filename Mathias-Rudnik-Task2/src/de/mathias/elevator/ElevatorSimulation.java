package de.mathias.elevator;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Scanner;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

public class ElevatorSimulation extends Application<ElevatorConfiguration> {
	private Elevator elevator;
	
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(System.in);
		Elevator elevator = new Elevator(0, 10);
		elevator.startElevator();
		elevator.goToFloor(4);
		elevator.goToFloor(7);
		elevator.goToFloor(9);
		ElevatorSimulation eSim = new ElevatorSimulation();
		eSim.elevator = elevator;
		eSim.run(args[0]);
		while(true){
			//clearScreen();
			//printElevatorPosition(elevator);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void clearScreen(){
		try {
			Runtime.getRuntime().exec("clear");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printElevatorPosition(Elevator e){
		for(int i = e.getMaxFloor() - e.getMinFloor(); i >= 0; i--){
			System.out.print("[");
			if(e.getMinFloor() + i == e.getFloor())	System.out.print("x");
			else System.out.print(" ");
			System.out.println("]");
		}
	}

	@Override
	public void run(ElevatorConfiguration configuration, Environment environment)
			throws Exception {
		// Enable CORS headers
	    final FilterRegistration.Dynamic cors =
	        environment.servlets().addFilter("CORS", CrossOriginFilter.class);

	    // Configure CORS parameters
	    cors.setInitParameter("allowedOrigins", "*");
	    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
	    cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

	    // Add URL mapping
	    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		
	    final ElevatorRessource resource = new ElevatorRessource(this.elevator);
	        environment.jersey().register(resource);
		
	}
}
