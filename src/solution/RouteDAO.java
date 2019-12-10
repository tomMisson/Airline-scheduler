package solution;
import java.io.IOException;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import baseclasses.DataLoadingException;
import baseclasses.IRouteDAO;
import baseclasses.Route;

/**
 * The RouteDAO parses XML files of route information, each route specifying
 * where the airline flies from, to, and on which day of the week
 */
public class RouteDAO implements IRouteDAO {

	List<Route> Routes = new ArrayList<Route>();
	
	/**
	 * Finds all flights that depart on the specified day of the week
	 * @param dayOfWeek A three letter day of the week, e.g. "Tue"
	 * @return A list of all routes that depart on this day
	 */
	@Override
	public List<Route> findRoutesByDayOfWeek(String dayOfWeek) {
		List<Route> byDoW = new ArrayList<>();
		
		for(Route r: Routes)
		{
			if(r.getDayOfWeek().equals(dayOfWeek))
			{
				byDoW.add(r);
			}
		}
		return byDoW;
	}

	/**
	 * Finds all of the flights that depart from a specific airport on a specific day of the week
	 * @param airportCode the three letter code of the airport to search for, e.g. "MAN"
	 * @param dayOfWeek the three letter day of the week code to searh for, e.g. "Tue"
	 * @return A list of all routes from that airport on that day
	 */
	@Override
	public List<Route> findRoutesByDepartureAirportAndDay(String airportCode, String dayOfWeek) {
		List<Route> byAirportAndDay = new ArrayList<>();
		
		for(Route r: Routes)
		{
			if(r.getDayOfWeek().equals(dayOfWeek) && r.getDepartureAirportCode().equals(airportCode))
			{
				byAirportAndDay.add(r);
			}
		}
		return byAirportAndDay;
	}

	/**
	 * Finds all of the flights that depart from a specific airport
	 * @param airportCode the three letter code of the airport to search for, e.g. "MAN"
	 * @return A list of all of the routes departing the specified airport
	 */
	@Override
	public List<Route> findRoutesDepartingAirport(String airportCode) {
		List<Route> byAirport = new ArrayList<>();
		
		for(Route r: Routes)
		{
			if(r.getDepartureAirportCode().equals(airportCode))
			{
				byAirport.add(r);
			}
		}
		return byAirport;
	}

	/**
	 * Finds all of the flights that depart on the specified date
	 * @param date the date to search for
	 * @return A list of all routes that depart on this date
	 */
	@Override
	public List<Route> findRoutesbyDate(LocalDate date) {
		List<Route> byDate = new ArrayList<>();
		
		for(Route r: Routes)
		{
			DayOfWeek day = null;
			try
			{
				switch (r.getDayOfWeek()) {
					case "Mon":
						day = DayOfWeek.MONDAY;
						break;
					case "Tue":
						day = DayOfWeek.TUESDAY;
						break;
					case "Wed":
						day = DayOfWeek.WEDNESDAY;
						break;
					case "Thu":
						day = DayOfWeek.THURSDAY;
						break;
					case "Fri":
						day = DayOfWeek.FRIDAY;
						break;
					case "Sat":
						day = DayOfWeek.SATURDAY;
						break;
					case "Sun":
						day = DayOfWeek.SUNDAY;
						break;
					default:
						throw new DataLoadingException();
				}
				
				if(day.equals(date.getDayOfWeek()))
				{
					byDate.add(r);
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		return byDate;
	}

	/**
	 * Returns The full list of all currently loaded routes
	 * @return The full list of all currently loaded routes
	 */
	@Override
	public List<Route> getAllRoutes() {
		// TODO Auto-generated method stub
		return Routes;
	}

	/**
	 * Returns The number of routes currently loaded
	 * @return The number of routes currently loaded
	 */
	@Override
	public int getNumberOfRoutes() {
		// TODO Auto-generated method stub
		return Routes.size();
	}

	/**
	 * Loads the route data from the specified file, adding them to the currently loaded routes
	 * Multiple calls to this function, perhaps on different files, would thus be cumulative
	 * @param p A Path pointing to the file from which data could be loaded
	 * @throws DataLoadingException if anything goes wrong. The exception's "cause" indicates the underlying exception
	 */
	@Override
	public void loadRouteData(Path p) throws DataLoadingException {
		
		Route temp = new Route();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(p.toString());
			Element root = doc.getDocumentElement();
			
			NodeList routesList = root.getElementsByTagName("Route");
			
			for(int i=0; i<routesList.getLength(); i++)
			{
				Node route = routesList.item(i);
				
				try {
				
					if(route.getNodeType() == Node.ELEMENT_NODE)
					{
						Element routeData = (Element) route;
						
					
						temp.setFlightNumber(Integer.parseInt(routeData.getElementsByTagName("FlightNumber").item(0).getTextContent()));
				
						temp.setDayOfWeek(routeData.getElementsByTagName("DayOfWeek").item(0).getTextContent());
				
						temp.setDepartureTime(LocalTime.parse(routeData.getElementsByTagName("DepartureTime").item(0).getTextContent(), formatter));
						
						temp.setDepartureAirportCode(routeData.getElementsByTagName("DepartureAirportCode").item(0).getTextContent());
						
						temp.setDepartureAirport(routeData.getElementsByTagName("DepartureAirport").item(0).getTextContent());
					
						temp.setArrivalTime(LocalTime.parse(routeData.getElementsByTagName("ArrivalTime").item(0).getTextContent()));
				
						temp.setArrivalAirport(routeData.getElementsByTagName("ArrivalAirport").item(0).getTextContent());
						
						temp.setArrivalAirportCode(routeData.getElementsByTagName("ArrivalAirportCode").item(0).getTextContent());
							
						temp.setDuration(java.time.Duration.parse(routeData.getElementsByTagName("Duration").item(0).getTextContent()));
							
					}
					
				}
				catch(Exception e)
				{
					throw new DataLoadingException();
				}
				Routes.add(temp);
				temp = new Route();
				
			}
		}
		catch(ParserConfigurationException | SAXException | IOException e)
		{
			System.err.println("Error Parsing XML: "+e);
			throw new DataLoadingException();
		}
	}

	/**
	 * Unloads all of the crew currently loaded, ready to start again if needed
	 */
	@Override
	public void reset() {
		Routes = new ArrayList<Route>();

	}

}
