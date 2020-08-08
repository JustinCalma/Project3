package cecs277.elevators;

import cecs277.Simulation; 
import cecs277.buildings.Building;
import cecs277.buildings.Floor;
import cecs277.buildings.FloorObserver;
import cecs277.events.*;
import cecs277.passengers.Passenger;

import java.util.*;
import java.util.stream.Collectors;

public class Elevator implements FloorObserver {
	
	public enum ElevatorState {
		IDLE_STATE,
		DOORS_OPENING,
		DOORS_CLOSING,
		DOORS_OPEN,
		ACCELERATING,
		DECELERATING,
		MOVING
	}
	
	public enum Direction {
		NOT_MOVING,
		MOVING_UP,
		MOVING_DOWN
	}
	
	
	private int mNumber;
	private Building mBuilding;

	// PROJECT 3: 10.2
	private OperationMode mode;
	private ElevatorState mCurrentState;
	
	private Direction mCurrentDirection = Direction.NOT_MOVING;
	private Floor mCurrentFloor;
	private List<Passenger> mPassengers = new ArrayList<>();
	
	private List<ElevatorObserver> mObservers = new ArrayList<>();
		
	private ArrayList<Integer> requestedFloor = new ArrayList<>(); 	
	
	public Elevator(int number, Building bld) {
		mNumber = number;
		mBuilding = bld;
		mCurrentFloor = bld.getFloor(1);
		
		// Project 3: 10.3
		this.mode = new IdleMode();
		
		this.mCurrentState = ElevatorState.IDLE_STATE;
		
		scheduleStateChange(ElevatorState.IDLE_STATE, 0);
	}
	
	/**
	 * Helper method to schedule a state change in a given number of seconds from now.
	 */
	public void scheduleStateChange(ElevatorState state, long timeFromNow) {
		
		Simulation sim = mBuilding.getSimulation();
				
		sim.scheduleEvent(new ElevatorStateEvent(sim.currentTime() + timeFromNow, state, this, 1));
	}
	
	/**
	 * Adds the given passenger to the elevator's list of passengers, and requests the passenger's destination floor.
	 */
	public void addPassenger(Passenger passenger) {

		mPassengers.add(passenger);
		
		// PROJECT 3: Adding requested floors in corresponding embarking strategy
//		requestedFloor.add(passenger.getTravelStrategy().getDestination());
		
	}
	
	public void removePassenger(Passenger passenger) {
		mPassengers.remove(passenger);
	}
	
	
	/**
	 * Schedules the elevator's next state change based on its current state.
	 */
	public void tick() {
		
		// PROJECT 3: 10.6
		mode.tick(this);

	}	
	
	/**
	 * Sends an idle elevator to the given floor.
	 */
	public void dispatchTo(Floor floor) {
		
		// PROJECT 3: 10.6
		if (mCurrentFloor.getNumber() < floor.getNumber()) {
			mode.dispatchToFloor(this, floor, Direction.MOVING_DOWN);
			
		} else if (mCurrentFloor.getNumber() > floor.getNumber()) {
			mode.dispatchToFloor(this, floor, Direction.MOVING_UP);
		}
				
	}
	
	// Simple accessors
	public Floor getCurrentFloor() {
		return mCurrentFloor;
	}
	
	public Direction getCurrentDirection() {
		return mCurrentDirection;
	}
	
	public Building getBuilding() {
		return mBuilding;
	}
	
	/**
	 * Returns true if this elevator is in the idle state.
	 * @return
	 */
	public boolean isIdle() {

		if (mCurrentState == ElevatorState.IDLE_STATE) {
			return true;
		}
		return false;
	}
	
	// All elevators have a capacity of 10, for now.
	public int getCapacity() {
		return 10;
	}
	
	public int getPassengerCount() {
		return mPassengers.size();
	}
	
	// Simple mutators
	
	// PROJECT 3: 10.3
	public void setMode(OperationMode mode) {
		this.mode = mode;
	}
	
	public void setState(ElevatorState state) {
		this.mCurrentState = state;
	}
	
	public void setCurrentDirection(Direction direction) {
		mCurrentDirection = direction;
	}
	
	public void setCurrentFloor(Floor floor) {
		mCurrentFloor = floor;
	}
	
	// Observers
	public void addObserver(ElevatorObserver observer) {
		mObservers.add(observer);
	}
	
	public void removeObserver(ElevatorObserver observer) {
		mObservers.remove(observer);
	}
	
	
	// FloorObserver methods
	@Override
	public void elevatorArriving(Floor floor, Elevator elevator) {
		// Not used.
	}
	
	/**
	 * Triggered when our current floor receives a direction request.
	 */
	@Override
	public void directionRequested(Floor sender, Direction direction) {
		
		// PROJECT 3: 10.6
		mode.directionRequested(this, sender, direction);
		
	}
	
	public int nextRequestUp(int fromFloor) {

		for (int i = 0; i < requestedFloor.size(); i++) {
			if (fromFloor < requestedFloor.get(i)) {

				return requestedFloor.get(i);
			}
		}
		return -1;
	}
	
	public int nextRequestDown(int fromFloor) {
		for (int i = requestedFloor.size() - 1; i > 0 ; i++) {
			if (fromFloor < requestedFloor.get(i)) {
				return requestedFloor.get(i);
			}
		}
		return -1;
	}
	
	// Created Getter
	public ElevatorState getState() {
		return mCurrentState;
	}
	
	// PROJ 3: 3.1
	public int requestDestinationFloor(Passenger passenger) {
		return passenger.getTravelStrategy().getDestination();
	}
	
	// PROJ 3: 3.2
	public void callScheduleNextDestination(Passenger passenger) {
		passenger.getTravelStrategy().scheduleNextDestination(passenger, mCurrentFloor);
	}
	
	// Voodoo magic.
	@Override
	// PROJECT 3:
	// Added getTravelStrategy()
	public String toString() {
		
		return "Elevator " + mNumber + " [" + mode + "]" + " - " + mCurrentFloor + " - " + mCurrentState + " - " + mCurrentDirection + " - "
		 + Arrays.toString(mPassengers.toArray()) + " {" + mPassengers.stream().map(p -> Integer.toString(p.getTravelStrategy().getDestination())).collect(Collectors.joining(", ")) + "}";		

	}

	// PROJECT 3: 10.2
	public void scheduleModeChange(OperationMode mode, ElevatorState state, int timeFromNow) {

		Simulation sim = mBuilding.getSimulation();
		
		sim.scheduleEvent(new ElevatorModeEvent(sim.currentTime() + timeFromNow, mode, state, this, 0));
		
	}

	// PROJECT 3: 10.2
	public void announceElevatorIdle() {
		
        List<ElevatorObserver> temp = new ArrayList<>(mObservers);
        for (ElevatorObserver i : temp) {
            i.elevatorWentIdle(this);
        }   
		
	}

	// PROJECT 3: 10.2
	public void announceElevatorDecelerating() {
		
        List<ElevatorObserver> temp = new ArrayList<>(mObservers);
        for (ElevatorObserver i : temp) {
            i.elevatorDecelerating(this);
        }   
		
	}
	
	// PROJECT 3: 10.4
	public ArrayList<Integer> getRequestedFloor() {
		return this.requestedFloor;
	}
	
	// PROJECT 3: 10.4
	public List<ElevatorObserver> getObservers() {
		return this.mObservers;
	}
	
	// PROJECT 3: 10.7
	public boolean canBeDispatchedToFloor() {
		return mode.canBeDispatchedToFloor(this, mCurrentFloor);
	}
	
	// PROJECT 3: Helper function for DECELERATING STATE
	public boolean checkLeaving() {
		
		for (Passenger p : mPassengers) {
			if (p.getTravelStrategy().getDestination() == this.mCurrentFloor.getNumber()) {
				return true;
			}
		}
		return false;
	}
	
	// PROJECT 3: Helper function for DISPATCHING
	public List<Passenger> getPassengers() {
		return this.mPassengers;
	}
	
	// PROJECT 3: Additional Work
	public void disable(OperationMode mode, ElevatorState state, int timeFromNow) {
		
		Simulation sim = mBuilding.getSimulation();
		sim.scheduleEvent(new ElevatorModeEvent(sim.currentTime() + timeFromNow, mode, state, this, 0));
		
	}
	
	// PROJECT 3: Additional Work
	public void enable(OperationMode mode, ElevatorState state, int timeFromNow) {
				
		Simulation sim = mBuilding.getSimulation();
		sim.scheduleEvent(new ElevatorModeEvent(sim.currentTime() + timeFromNow, mode, state, this, 0));
		
	}	

}
