package cecs277.passengers;

import java.util.ArrayList;
import java.util.Random;

import cecs277.Simulation;

public class WorkerFactory implements PassengerFactory {
	private int weight;
	
	public WorkerFactory() {
		this.weight = 2;
	}
	
	public WorkerFactory(int weight) {
		this.weight = weight;
	}
	
	@Override
	public String factoryName() {
		return "Worker";
	}

	@Override
	public String shortName() {
		return "W";
	}

	@Override
	public int factoryWeight() {
		return this.weight;
	}

	@Override
	public BoardingStrategy createBoardingStrategy(Simulation simulation) {
		
		BoardingStrategy tb = new ThresholdBoarding(3);
		
		return tb;
	}

	@Override
	public TravelStrategy createTravelStrategy(Simulation simulation) {
		
		Random r = simulation.getRandom();
		
		// "X" the amount of floors the worker will visit before returning to floor 1
		int x = (int)(r.nextInt(5) + 2);
		
		//Creating the ArrayList for destinations
		ArrayList<Integer> destinations = new ArrayList<>();
		
		// Generate an int from 2 to N that is not the same as the previous generated num
		for (int i = 0; i < x; i++) {
			int prevDestination = 0;
			int randDestination = (int)(r.nextInt(simulation.getBuilding().getFloorCount() - 1) + 2); 
			// Keep generating random num until it isn't the same as the previous num
			while(randDestination == prevDestination) {
				randDestination = (int)(r.nextInt(simulation.getBuilding().getFloorCount() - 1) + 2); 
			}
			// Set new prevDestination for next iteration
			prevDestination = randDestination;
			// Adding randDestination into destinations ArrayList
			destinations.add(randDestination);
		}
		
		// Creating the ArrayList for duration
		ArrayList<Long> durations = new ArrayList<>();
		// Generate the list of durations
		for (int i = 0; i < x; i++) {
			int duration = (int)(r.nextGaussian() * 180 + 600);
			durations.add((long) duration);
		}
		
		TravelStrategy mdt = new MultipleDestinationTravel(destinations, durations);
		
		return mdt;
	}

	@Override
	public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
		
		EmbarkingStrategy eb = new ResponsibleEmbarking();

		return eb;
	}

	@Override
	public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
		
		DebarkingStrategy ad = new AttentiveDebarking();
		
		return ad;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public int getWeight() {
		return this.weight;
	}

}
