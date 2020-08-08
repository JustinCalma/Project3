package cecs277.passengers;

import cecs277.Simulation;
import cecs277.elevators.Elevator;
import cecs277.events.PassengerNextDestinationEvent;

public class DistractedDebarking implements DebarkingStrategy {
	private boolean missedDestinationFloor = false;

	@Override
	public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
		
		if (missedDestinationFloor == false && elevator.getCurrentFloor().getNumber() == passenger.getTravelStrategy().getDestination()) {
			missedDestinationFloor = true;
			return false;
			
		} else if (missedDestinationFloor == true && elevator.getCurrentFloor().getNumber() != passenger.getTravelStrategy().getDestination()) {
			return true;
		}
		
		return false;
	}

	@Override
	public void departedElevator(Passenger passenger, Elevator elevator) {
		
		if (elevator.getCurrentFloor().getNumber() != passenger.getTravelStrategy().getDestination()) {
			Simulation s = elevator.getCurrentFloor().getBuilding().getSimulation();
			PassengerNextDestinationEvent ev = new PassengerNextDestinationEvent(s.currentTime() + 5, passenger, elevator.getCurrentFloor(), 2);
			s.scheduleEvent(ev);
			
		} else if (elevator.getCurrentFloor().getNumber() == passenger.getTravelStrategy().getDestination()) {
			elevator.callScheduleNextDestination(passenger);

		}	
	}

}
