package cecs277.passengers;

import cecs277.buildings.Floor;

import cecs277.buildings.FloorObserver;
import cecs277.elevators.Elevator;
import cecs277.elevators.Elevator.Direction;
import cecs277.elevators.ElevatorObserver;

/**
 * A passenger that is either waiting on a floor or riding an elevator.
 */
public class Passenger implements FloorObserver, ElevatorObserver {
	// ADDED FIELDS PART 5.1
	private String name;
	private String shortName;
	private TravelStrategy travelStrat;
	private BoardingStrategy boardingStrat;
	private EmbarkingStrategy embarkingStrat;
	private DebarkingStrategy debarkingStrat;
	
	// An enum for determining whether a Passenger is on a floor, an elevator, or busy (visiting a room in the building).
	public enum PassengerState {
		WAITING_ON_FLOOR,
		ON_ELEVATOR,
		BUSY
	}
	
	// A cute trick for assigning unique IDs to each object that is created. (See the constructor.)
	private static int mNextId;
	protected static int nextPassengerId() {
		return ++mNextId;
	}
	
	private int mIdentifier;
	private PassengerState mCurrentState;
	
	public Passenger(String name, String shortName, TravelStrategy travelStrat, BoardingStrategy boardingStrat, EmbarkingStrategy embarkingStrat, DebarkingStrategy debarkingStrat) {
		mIdentifier = nextPassengerId();
		mCurrentState = PassengerState.WAITING_ON_FLOOR;
		
		// FROM PROJ 3: PART 5.2 (PROJECT 3 PLAN OF ATTACK)
		this.name = name;
		this.shortName = shortName;
		this.travelStrat = travelStrat;
		this.boardingStrat = boardingStrat;
		this.embarkingStrat = embarkingStrat;
		this.debarkingStrat = debarkingStrat;
	}
	
	public void setState(PassengerState state) {
		mCurrentState = state;
	}
	
	/**
	 * Gets the passenger's unique identifier.
	 */
	public int getId() {
		return mIdentifier;
	}
	
	
	/**
	 * Handles an elevator arriving at the passenger's current floor.
	 */
	@Override
	public void elevatorArriving(Floor floor, Elevator elevator) {
		// This is a sanity check. A Passenger should never be observing a Floor they are not waiting on.
		if (floor.getWaitingPassengers().contains(this) && mCurrentState == PassengerState.WAITING_ON_FLOOR) {
			Elevator.Direction elevatorDirection = elevator.getCurrentDirection();
			
			// TODO: check if the elevator is either NOT_MOVING, or is going in the direction that this passenger wants.
			// If so, this passenger becomes an observer of the elevator.
			
			//If current direction is NOT_MOVING || Passenger wants to go up || Passenger wants to go down
			if (elevatorDirection == Direction.NOT_MOVING || elevator.getCurrentDirection() == Direction.MOVING_UP  && travelStrat.getDestination() > elevator.getCurrentFloor().getNumber() 
					|| elevator.getCurrentDirection() == Direction.MOVING_DOWN && travelStrat.getDestination() < elevator.getCurrentFloor().getNumber()) {

				elevator.addObserver(this);
			}
			
		}
		// This else should not happen if your code is correct. Do not remove this branch; it reveals errors in your code.
		else {
			throw new RuntimeException("Passenger " + toString() + " is observing Floor " + floor.getNumber() + " but they are " +
			 "not waiting on that floor.");
		}
	}
	
	/**
	 * Handles an observed elevator opening its doors. Depart the elevator if we are on it; otherwise, enter the elevator.
	 */
	@Override
	public void elevatorDoorsOpened(Elevator elevator) {

		
		// PROJECT 3: 6.2.3
		if (mCurrentState == PassengerState.ON_ELEVATOR && debarkingStrat.willLeaveElevator(this, elevator)) {
			
			// PROJECT 3: 6.2.3
			elevator.removePassenger(this);
			
			elevator.removeObserver(this);
			
			// PROJECT 3: 6.2.4
			debarkingStrat.departedElevator(this, elevator);
			
		} else if (mCurrentState == PassengerState.WAITING_ON_FLOOR) {

			// PROJECT 3: 6.2.1
			if (boardingStrat.willBoardElevator(this, elevator)) {
				// Remove the waiting passenger from the current floor
				elevator.getCurrentFloor().removeWaitingPassenger(this);
				// Remove the passenger as an observer of the current floor
				elevator.getCurrentFloor().removeObserver(this);
			
				// PROJECT 3: 6.2.2
				embarkingStrat.enteredElevator(this, elevator);
				
				// Add passenger on the elevator
				elevator.addPassenger(this);
				// Add the passenger as an observer of the elevator
				elevator.addObserver(this);
				// Set the current state to ON_ELEVATOR
				setState(PassengerState.ON_ELEVATOR); 
				
			// PROJECT 3: Other Fixes
			} else {
				
				if (elevator.getCurrentFloor().getNumber() < travelStrat.getDestination()) {
					elevator.getCurrentFloor().requestDirection(Direction.MOVING_UP);
					
				} else if (elevator.getCurrentFloor().getNumber() > travelStrat.getDestination()){
					elevator.getCurrentFloor().requestDirection(Direction.MOVING_DOWN);
				}
			}
			
			
		}

	}

	@Override
	// PROJ 3: ADDED travelStrat.getDestination()
	public String toString() {
		return this.shortName + this.mIdentifier;
	}
	
	@Override
	public void directionRequested(Floor sender, Elevator.Direction direction) {
		// Don't care.
	}
	
	@Override
	public void elevatorWentIdle(Elevator elevator) {
		// Don't care about this.
	}
	
	// The next two methods allow Passengers to be used in data structures, using their id for equality. Don't change 'em.
	@Override
	public int hashCode() {
		return Integer.hashCode(mIdentifier);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Passenger passenger = (Passenger)o;
		return mIdentifier == passenger.mIdentifier;
	}
	
	// PROJ 3: AttentativeDebarking 3.4
	public TravelStrategy getTravelStrategy() {
		return this.travelStrat;
	}
	
	// PROJ 3: Created getter for short name
	public String getShortName() {
		return this.shortName;
	}
	
	// PROJECT 3: Created getter for name
	public String getName() {
		return this.name;
	}

	@Override
	public void elevatorDecelerating(Elevator sender) {

	}
	
}