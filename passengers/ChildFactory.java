package cecs277.passengers;

import java.util.Random;

import cecs277.Simulation;

public class ChildFactory implements PassengerFactory {
	private int weight;
	private int destination;
	
	public ChildFactory() {
		this.weight = 3;
	}
	
	public ChildFactory(int weight) {
		this.weight = weight;
	}
	
	@Override
	public String factoryName() {

		return "Child";
	}

	@Override
	public String shortName() {

		return "C";
	}

	@Override
	public int factoryWeight() {

		return this.weight;
	}

	@Override
	public BoardingStrategy createBoardingStrategy(Simulation simulation) {
		
		BoardingStrategy ab = new AwkwardBoarding(4);
		
		return ab;
		
	}

	@Override
	public TravelStrategy createTravelStrategy(Simulation simulation) {
		
		Random r = simulation.getRandom();

		int destination = (int)(r.nextInt(simulation.getBuilding().getFloorCount() - 1)) + 2;
				
		int duration = (int) (r.nextGaussian() * 1800 + 7200);
		
		TravelStrategy sdt = new SingleDestinationTravel(destination, duration);
		
		return sdt;

	}

	@Override
	public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
		
		EmbarkingStrategy cb = new ClumsyEmbarking();
		
		return cb;
	}

	@Override
	public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
		
		DebarkingStrategy dd = new DistractedDebarking();
		
		return dd;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public int getWeight() {
		return this.weight;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}
	
	public int getDestination() {
		return this.destination;
	}
	
}
