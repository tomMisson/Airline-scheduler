package solution;

import java.nio.file.Paths;
import java.time.LocalDate;

import baseclasses.DataLoadingException;
import baseclasses.*;

/**
 * This class allows you to run the code in your classes yourself, for testing and development
 */
public class Main {

	public static void main(String[] args) {	
		IAircraftDAO aircraft = new AircraftDAO();
		IRouteDAO routes = new RouteDAO();
		ICrewDAO crew = new CrewDAO();
		IPassengerNumbersDAO passengers = new PassengerNumbersDAO();
		
		try {
			aircraft.loadAircraftData(Paths.get("./data/aircraft.csv"));
			routes.loadRouteData(Paths.get("./data/routes.xml"));
			crew.loadCrewData(Paths.get("./data/crew.json"));
			passengers.loadPassengerNumbersData(Paths.get("./data/passengernumbers.db"));
		}
		catch (DataLoadingException dle) {
			System.err.println("Error loading aircraft data");
			dle.printStackTrace();
		}
	}

}