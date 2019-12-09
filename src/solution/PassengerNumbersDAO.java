package solution;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import baseclasses.DataLoadingException;
import baseclasses.IPassengerNumbersDAO;

/**
 * The PassengerNumbersDAO is responsible for loading an SQLite database
 * containing forecasts of passenger numbers for flights on dates
 */
public class PassengerNumbersDAO implements IPassengerNumbersDAO {
	
	ArrayList<String> numbers = new ArrayList<>();

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
		for(String line: numbers)
		{
			String[] lineData = line.split(" ");
			LocalDate dateData = LocalDate.parse(lineData[0]);
			int flightNumberData = Integer.parseInt(lineData[1]);
			int passengerData = Integer.parseInt(lineData[2]);
			
			if((flightNumberData == flightNumber)&& (dateData == date))
			{
				return passengerData;
			}
		}
		return 0;
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
			c= DriverManager.getConnection("jdbc:sqlite:"+p);
			
			Statement sql = c.createStatement();
			ResultSet rs = sql.executeQuery("SELECT * FROM PassengerNumbers;");
			
			while(rs.next())
			{
				try {
					String temp = rs.getString("Date") +" "+ rs.getString("FlightNumber") + " " + rs.getString("Passengers");
					System.out.println(temp);
					numbers.add(temp);
				}
				catch(Exception e)
				{
					throw new DataLoadingException();
				}
			}
		}
		catch(SQLException | DataLoadingException e)
		{
			throw new DataLoadingException();
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
