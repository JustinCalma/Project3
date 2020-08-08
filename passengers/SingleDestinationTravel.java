package cecs277.passengers;

import cecs277.Simulation;
import cecs277.buildings.Floor;
import cecs277.events.PassengerNextDestinationEvent;

public class SingleDestinationTravel implements TravelStrategy {
	
	// Fields
	private int destinationFloor;
	private int duration;
	
	// SingleDestinationTravel Constructor
	public SingleDestinationTravel(int destinationFloor, int duration) {
		this.destinationFloor = destinationFloor;
		this.duration = duration;
	}
	
	@Override
	public int getDestination() {

		return this.destinationFloor;
	}

	@Override
	public void scheduleNextDestination(Passenger passenger, Floor currentFloor) {
		
		this.destinationFloor = 1;
		Simulation s = currentFloor.getBuilding().getSimulation();
		PassengerNextDestinationEvent ev = new PassengerNextDestinationEvent(s.currentTime() + this.duration, passenger, currentFloor, 2);
		s.scheduleEvent(ev);
	}

}
