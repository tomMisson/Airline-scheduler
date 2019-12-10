package solution;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import baseclasses.DataLoadingException;
import baseclasses.IPassengerNumbersDAO;

/**
 * The PassengerNumbersDAO is responsible for loading an SQLite database
 * containing forecasts of passenger numbers for flights on dates
 */
public class PassengerNumbersDAO implements IPassengerNumbersDAO {
	
	Map<String, Integer> numbers = new HashMap<>();

	/**
	 * Returns the number of passenger number entries in the cache
	 * @return the number of passenger number entries in the cache
	 */
	@Override
	public int getNumberOfEntries() {
		return numbers.size();
	}

	/**
	 * Returns the predicted number of passengers for a given flight on a given date, or -1 if no data available
	 * @param flightNumber The flight number of the flight to check for
	 * @param date the date of the flight to check for
	 * @return the predicted number of passengers, or -1 if no data available
	 */
	@Override
	public int getPassengerNumbersFor(int flightNumber, LocalDate date) {
		int result = -1;
		if(numbers.containsKey(flightNumber+ " "+ date)){result = numbers.get(flightNumber+ " "+ date);}
		return result;
	}

	/**
	 * Loads the passenger numbers data from the specified SQLite database into a cache for future calls to getPassengerNumbersFor()
	 * Multiple calls to this method are additive, but flight numbers/dates previously cached will be overwritten
	 * The cache can be reset by calling reset() 
	 * @param p The path of the SQLite database to load data from
	 * @throws DataLoadingException If there is a problem loading from the database
	 */
	@Override
	public void loadPassengerNumbersData(Path p) throws DataLoadingException {

		Connection c = null;
		try {
			
			c= DriverManager.getConnection("jdbc:sqlite:"+p.toString());
			
			Statement sql = c.createStatement();
			ResultSet rs = sql.executeQuery("SELECT * FROM PassengerNumbers;");
			
			while(rs.next())
			{
				numbers.put(rs.getString("FlightNumber") + " " + rs.getString("Date"), Integer.parseInt(rs.getString("Passengers")));
			}
			
			c.close();
			rs.close();
			sql.close();
		}
		catch(Exception e)
		{
			throw new DataLoadingException(e);
		}
	}

	/**
	 * Removes all data from the DAO, ready to start again if needed
	 */
	@Override
	public void reset() {
		numbers.clear();
	}

}
