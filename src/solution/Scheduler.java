package solution;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.text.html.CSS;

import baseclasses.Aircraft;
import baseclasses.CabinCrew;
import baseclasses.Crew;
import baseclasses.DoubleBookedException;
import baseclasses.FlightInfo;
import baseclasses.IAircraftDAO;
import baseclasses.ICrewDAO;
import baseclasses.IPassengerNumbersDAO;
import baseclasses.IRouteDAO;
import baseclasses.IScheduler;
import baseclasses.InvalidAllocationException;
import baseclasses.Pilot;
import baseclasses.QualityScoreCalculator;
import baseclasses.Route;
import baseclasses.Schedule;
import baseclasses.SchedulerRunner;

public class Scheduler implements IScheduler {

	@Override
	public Schedule generateSchedule(IAircraftDAO aircraft, ICrewDAO crew, IRouteDAO route, IPassengerNumbersDAO passenger,
			LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		
		Schedule s = new Schedule(route, startDate, endDate);
		s.sort();
				
		ArrayList<Aircraft> UnAlloaircrafts = new ArrayList<>();
		ArrayList<CabinCrew> cabinCrew = new ArrayList<>();
		ArrayList<Pilot> pilots = new ArrayList<>();
		
		Random rand = new Random(); 
		
		UnAlloaircrafts.addAll(aircraft.getAllAircraft());
		cabinCrew.addAll(crew.getAllCabinCrew());
		pilots.addAll(crew.getAllPilots());

		
		int passes = 0;
		int lastRemaining = 0;
		Boolean needsReset = false; 
		
		Boolean error_messages = false;  // Enable Error messages e.g PrintStack trace
		int maxPasses = 10000; // Max number of times it'll try to allocate before resetting
	

		while(!s.isCompleted()) {
			
			if(s.getRemainingAllocations().size() == lastRemaining) {
				passes++;
			} else {
				lastRemaining = s.getRemainingAllocations().size();
				passes = 0;
			}
			
			if(passes>maxPasses) {
				passes = 0;
				needsReset = true;
			}
			
			if(needsReset) {
				s = new Schedule(route, startDate, endDate);
				s.sort();
				UnAlloaircrafts = new ArrayList<>();
				cabinCrew = new ArrayList<>();
				pilots = new ArrayList<>();
				UnAlloaircrafts.addAll(aircraft.getAllAircraft());
				cabinCrew.addAll(crew.getAllCabinCrew());
				pilots.addAll(crew.getAllPilots());
				needsReset = false;
			}
			
			UnAlloaircrafts.addAll(aircraft.getAllAircraft());
			
			if(s.getRemainingAllocations().size()==0)
			{
				break;
			}
			
			for(FlightInfo fi : s.getRemainingAllocations()) {
			
					Route fiRoute = fi.getFlight();

					ArrayList<Aircraft> validPlanes = new ArrayList<>();	
								
					
					for (Aircraft ac : aircraft.getAllAircraft()) {
						
						if (fiRoute.getDepartureAirportCode().contains(ac.getStartingPosition())) {
							// TODO: check if the aircraft hasn't been allocated				
							if ( UnAlloaircrafts.contains(ac) ) {
								validPlanes.add(ac);
							}
							
						}
						
					}
				
					
					for (FlightInfo fl_ac : s.getCompletedAllocations()) {
						if (fl_ac.getFlight().getDepartureAirportCode().contains(fl_ac.getFlight().getDepartureAirportCode())) {
							validPlanes.add(s.getAircraftFor(fl_ac));
						}
					}
					
					if (validPlanes.size() == 0) {
						continue;
					} else {
						try {
							s.allocateAircraftTo(validPlanes.get(rand.nextInt(validPlanes.size())), fi);
							UnAlloaircrafts.remove(s.getAircraftFor(fi));
						} catch (DoubleBookedException e) {
							if (error_messages) {
	
								System.out.println("1 - Double Booked Plane!");
							}
						}
					} 
		
				try {
					s.allocateCabinCrewTo(crew.findCabinCrewByTypeRating(s.getAircraftFor(fi).getTypeCode()).get(rand.nextInt(crew.findCabinCrewByTypeRating(s.getAircraftFor(fi).getTypeCode()).size())), fi);
				} catch (Exception e1) {
					try {
						s.allocateCabinCrewTo(crew.getAllCabinCrew().get(rand.nextInt(crew.getAllCabinCrew().size())), fi);
					} catch (DoubleBookedException e) {
						if (error_messages) {
							System.out.println("2 - Double Booked CabinCrew!");
						}
					}
				}
		
				try {
					s.allocateCaptainTo(crew.findPilotsByTypeRating(s.getAircraftFor(fi).getTypeCode()).get(rand.nextInt(crew.findPilotsByTypeRating(s.getAircraftFor(fi).getTypeCode()).size())), fi);
				} catch (Exception e1) {
					try {
						s.allocateCaptainTo(crew.getAllPilots().get(rand.nextInt(crew.getAllPilots().size())), fi);
					} catch (DoubleBookedException e) {
						if (error_messages) {
					
							System.out.println("3 - Double Booked Pilot!");
						}
					
					}
				}
		
					try {
						s.allocateFirstOfficerTo(crew.findPilotsByTypeRating(s.getAircraftFor(fi).getTypeCode()).get(rand.nextInt(crew.findPilotsByTypeRating(s.getAircraftFor(fi).getTypeCode()).size())), fi);
					} catch (Exception e1) {
						try {
							s.allocateFirstOfficerTo(crew.getAllPilots().get(rand.nextInt(crew.getAllPilots().size())), fi);
						} catch (DoubleBookedException e) {
							if (error_messages) {
					
								System.out.println("4 - Double Booked Crew!");
							}
						}
					}

				try {
					if(s.isValid(fi)) {
						s.completeAllocationFor(fi);
					}
				
				} catch (InvalidAllocationException e) {
					
				}
				
				
			}
			
			System.out.println("Done: " + s.getCompletedAllocations().size() + " Left: " + s.getRemainingAllocations().size());
		}
		
		return s;
	}
	
	@Override
	public void setSchedulerRunner(SchedulerRunner arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
