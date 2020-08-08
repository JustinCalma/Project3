package cecs277.events;

import cecs277.Simulation; 
import cecs277.elevators.Elevator;
import cecs277.elevators.Elevator.ElevatorState;
import cecs277.elevators.OperationMode;

/**
 * A simulation event that sets an elevator's state and calls its tick() method.
 */
public class ElevatorModeEvent extends SimulationEvent {
	private OperationMode mode;
	private ElevatorState state;
	private Elevator mElevator;
	
	public ElevatorModeEvent(long scheduledTime, OperationMode mode, ElevatorState state, Elevator elevator, int priority) {
		super(scheduledTime, priority);
		this.mode = mode;
		this.state = state;
		mElevator = elevator;
	}
	@Override
	public void execute(Simulation sim) {
		mElevator.setMode(mode);
		mElevator.setState(state);
		mElevator.tick();
	}
	
	@Override
	public String toString() {
		
		// PROJECT 3: Logger
		return "" + mElevator;
		
	}
}
