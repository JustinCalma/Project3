package cecs277.passengers;
import cecs277.Simulation;
import cecs277.buildings.Floor;
import cecs277.events.PassengerNextDestinationEvent;

import java.util.ArrayList;

public class MultipleDestinationTravel implements TravelStrategy {

	// Fields 
	private ArrayList<Integer> destinations = new ArrayList<Integer>();
	private ArrayList<Long>  durations = new ArrayList<Long>();
	
	// MultipleDestinationTravel Constructor
	public MultipleDestinationTravel(ArrayList<Integer> destinations, ArrayList<Long> durations) {
		this.destinations = destinations;
		this.durations = durations;
	}
	
	@Override
	public int getDestination() {

		return destinations.get(0);
	}

	@Override
	public void scheduleNextDestination(Passenger passenger, Floor currentFloor) {
		
		// First destination in their list is removed
		destinations.remove(0); 
		
		// Schedules a PassengerNextDestinationEvent 
		Simulation s = currentFloor.getBuilding().getSimulation();
		PassengerNextDestinationEvent ev = new PassengerNextDestinationEvent(s.currentTime() + durations.get(0), passenger, currentFloor, 2);
		s.scheduleEvent(ev);
		
	}

}
