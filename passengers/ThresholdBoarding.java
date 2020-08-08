package cecs277.passengers;

import cecs277.elevators.Elevator;

public class ThresholdBoarding implements BoardingStrategy {

	// Fields
	private int threshold;
	
	// Constructor
	public ThresholdBoarding(int threshold) {
		this.threshold = threshold;
	}

	@Override
	public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
		
		if (elevator.getPassengerCount() <= threshold) {
			return true;
		}
		
		return false;
	}
	

}
