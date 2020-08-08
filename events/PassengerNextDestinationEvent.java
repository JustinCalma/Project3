package cecs277.events;

import cecs277.Simulation;
import cecs277.buildings.Floor;
import cecs277.passengers.Passenger;

/**
 * A simulation event that adds an existing passenger to a given floor, as if they have finished with their
 * task on that floor and are now waiting for an elevator to go to their next destination.
 */
public class PassengerNextDestinationEvent extends SimulationEvent {
	private Passenger mPassenger;
	private Floor mStartingFloor;
	
	public PassengerNextDestinationEvent(long scheduledTime, Passenger passenger, Floor startingFloor, int priority) {
		super(scheduledTime, priority);
		mPassenger = passenger;
		mStartingFloor = startingFloor;
	}
	
	
	@Override
	public void execute(Simulation sim) {
		mPassenger.setState(Passenger.PassengerState.WAITING_ON_FLOOR);
		mStartingFloor.addWaitingPassenger(mPassenger);
	}
	
	@Override
	// PROJECT 3: Added getTravelStrategy()
	public String toString() {
		
		// PROJECT 3: Logger
		return "Passenger " + mPassenger.getId() + " (" + mPassenger.getName() + ") joining floor " + mStartingFloor.getNumber() +
				 ", heading to floor " + mPassenger.getTravelStrategy().getDestination();
		
	}
}
