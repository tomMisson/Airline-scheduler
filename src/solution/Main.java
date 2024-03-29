package solution;

import java.util.List;
import java.nio.file.Paths;
import java.time.LocalDate;

import baseclasses.Aircraft;
import baseclasses.Crew;
import baseclasses.DataLoadingException;
import baseclasses.IAircraftDAO;
import baseclasses.ICrewDAO;
import baseclasses.IPassengerNumbersDAO;
import baseclasses.IRouteDAO;
import baseclasses.IScheduler;
import baseclasses.Route;

/**
 * This class allows you to run the code in your classes yourself, for testing and development
 */
public class Main {

	public static void main(String[] args) {	
		IAircraftDAO aircraft = new AircraftDAO();
		IRouteDAO routes = new RouteDAO();
		ICrewDAO crew = new CrewDAO();
		IPassengerNumbersDAO passengers = new PassengerNumbersDAO();
		
		IScheduler s = new Scheduler();
		
		try {
			aircraft.loadAircraftData(Paths.get("./data/aircraft.csv"));
			routes.loadRouteData(Paths.get("./data/routes.xml"));
			crew.loadCrewData(Paths.get("./data/crew.json"));
			passengers.loadPassengerNumbersData(Paths.get("./data/passengernumbers.db"));
			
			s.generateSchedule(aircraft, crew, routes, passengers, LocalDate.parse("2020-07-01"), LocalDate.parse("2020-08-31"));
		}
		catch (DataLoadingException dle) {
			System.err.println("Error loading aircraft data");
			dle.printStackTrace();
		}
	}

}