package cecs277.passengers;

import java.util.Random;

import cecs277.Simulation;

public class StonerFactory implements PassengerFactory {
	private int weight;
	
	public StonerFactory() {
		this.weight = 1;
	}
	
	public StonerFactory(int weight) {
		this.weight = weight;
	}
	
	@Override
	public String factoryName() {

		return "Stoner";
	}

	@Override
	public String shortName() {

		return "S";
	}

	@Override
	public int factoryWeight() {

		return this.weight;
	}

	@Override
	public BoardingStrategy createBoardingStrategy(Simulation simulation) {
		
		BoardingStrategy cb = new CapacityBoarding();
		
		return cb;
	}

	@Override
	public TravelStrategy createTravelStrategy(Simulation simulation) {
		
		Random r = simulation.getRandom();

		int destination = (int)(r.nextInt(simulation.getBuilding().getFloorCount() - 1)) + 2;
				
		int duration = (int) (r.nextGaussian() * 1200 + 3600);
		
		TravelStrategy sdt = new SingleDestinationTravel(destination, duration);
		
		return sdt;
		
	}

	@Override
	public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
		
		EmbarkingStrategy ce = new ClumsyEmbarking();
		
		return ce;
	}

	@Override
	public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
		
		DebarkingStrategy cd = new ConfusedDebarking();
		
		return cd;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public int getWeight() {
		return this.weight;
	}

}
