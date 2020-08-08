
package cecs277.buildings;

import cecs277.elevators.ElevatorObserver;

import cecs277.passengers.Passenger;
import cecs277.elevators.Elevator;
import cecs277.elevators.Elevator.Direction;

import java.util.*;

public class Floor implements ElevatorObserver {
	private Building mBuilding;
	private List<Passenger> mPassengers = new ArrayList<>();
	private ArrayList<FloorObserver> mObservers = new ArrayList<>();
	private int mNumber;
	
	private boolean buttonUp;
	private boolean buttonDown;
	
	public Floor(int number, Building building) {
		mNumber = number;
		mBuilding = building;
	}
	
	
	/**
	 * Sets a flag that the given direction has been requested by a passenger on this floor. If the direction
	 * had NOT already been requested, then all observers of the floor are notified that directionRequested.
	 * @param direction
	 */
	public void requestDirection(Elevator.Direction direction) {

		if (direction == Direction.MOVING_UP && directionIsPressed(direction)) {
			buttonUp = true;
			// if only one button can be pressed then add "buttonDown = false"
		} else if (direction == Direction.MOVING_DOWN && directionIsPressed(direction)) {
			buttonDown = true;
			// if only one button can be pressed then add "buttonUp = false"
		} else {
			// Alerts all observers that directionRequested
			ArrayList<FloorObserver> temp = new ArrayList<>(mObservers);
			for (FloorObserver i : temp) {
				i.directionRequested(this, direction);
			}
		}
	}
	
	/**
	 * Returns true if the given direction button has been pressed.
	 */
	public boolean directionIsPressed(Elevator.Direction direction) {
		
		boolean key = false;
		if (buttonUp == true && direction == Direction.MOVING_UP) {
			key = true;
		} else if (buttonDown == true && direction == Direction.MOVING_DOWN) {
			key = true;
		}
		return key;
	}
	
	/**
	 * Clears the given direction button so it is no longer pressed.
	 */
	public void clearDirection(Elevator.Direction direction) {
		
		if (direction == Direction.MOVING_UP) {
			buttonUp = false;
		} else if (direction == Direction.MOVING_DOWN) {
			buttonDown = false;
		}
	}
	
	/**
	 * Adds a given Passenger as a waiting passenger on this floor, and presses the passenger's direction button.
	 */
	public void addWaitingPassenger(Passenger p) {
		mPassengers.add(p);
		addObserver(p);
		p.setState(Passenger.PassengerState.WAITING_ON_FLOOR);
			
		// PROJECT 3
		if (mNumber < p.getTravelStrategy().getDestination()) {
			requestDirection(Direction.MOVING_UP);
		
		} else if (mNumber > p.getTravelStrategy().getDestination()) {
			requestDirection(Direction.MOVING_DOWN);
		}
	}
	
	// Created Getters
	public boolean getButtonUp() {
		return this.buttonUp;
	}
	
	public boolean getButtonDown() {
		return this.buttonDown;
	}
	
	/**
	 * Removes the given Passenger from the floor's waiting passengers.
	 */
	public void removeWaitingPassenger(Passenger p) {
		mPassengers.remove(p);
	}
	
	
	// Simple accessors.
	public int getNumber() {
		return mNumber;
	}
	
	public List<Passenger> getWaitingPassengers() {
		return mPassengers;
	}
	
	@Override
	public String toString() {
		return "Floor " + mNumber;
	}
	
	// Observer methods.
	public void removeObserver(FloorObserver observer) {
		mObservers.remove(observer);
	}
	
	public void addObserver(FloorObserver observer) {
		mObservers.add(observer);
	}
	
	// Observer methods.
	@Override
	public void elevatorDecelerating(Elevator elevator) {
		
		// If the elevator is arriving at this floor
		if (elevator.getCurrentFloor().getNumber() == getNumber()) { 
						
			List<FloorObserver> temp = new ArrayList<>(mObservers);
			// Alert all the floor's observers that elevatorArriving
			for (FloorObserver i : temp) {								
				i.elevatorArriving(this, elevator);
			}
			// Clear the elevator's current direction 
			clearDirection(elevator.getCurrentDirection());
		} 

	}
	
	@Override
	public void elevatorDoorsOpened(Elevator elevator) {
		// Not needed.
	}
	
	@Override
	public void elevatorWentIdle(Elevator elevator) {
		// Not needed.
	}
	
	// PROJ 3: Used for SingleDestinationTravel 3.4
	public Building getBuilding() {
		return this.mBuilding;
	}
	
	// PROJECT 3: 11.2
	public Direction findDirection() {
		
		if (getButtonUp()) {
			return Direction.MOVING_UP;
		} else {
			return Direction.MOVING_DOWN;
		} 		
	}
	
}
