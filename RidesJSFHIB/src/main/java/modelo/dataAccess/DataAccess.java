package modelo.dataAccess;

import java.io.File;
import java.net.NoRouteToHostException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import modelo.JPAUtil;
//import configuration.ConfigXML;
//import configuration.UtilDate;
import modelo.dominio.*;

import modelo.exceptions.*;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess {
//	private  EntityManager  db;
//	private  EntityManagerFactory emf;

//	ConfigXML c=ConfigXML.getInstance();

	public DataAccess() {
//		if (c.isDatabaseInitialized()) {
//			String fileName=c.getDbFilename();
//
//			File fileToDelete= new File(fileName);
//			if(fileToDelete.delete()){
//				File fileToDeleteTemp= new File(fileName+"$");
//				fileToDeleteTemp.delete();
//
//				  System.out.println("File deleted");
//				} else {
//				  System.out.println("Operation failed");S
//				}
//		}
//		open();
//		if  (c.isDatabaseInitialized())initializeDB();
//		
//		System.out.println("DataAccess created => isDatabaseLocal: "+c.isDatabaseLocal()+" isDatabaseInitialized: "+c.isDatabaseInitialized());
//
//		close();

	}

	public DataAccess(EntityManager db) {
//    	this.db=db;
	}

	/**
	 * This is the data access method that initializes the database with some events
	 * and questions. This method is invoked by the business logic (constructor of
	 * BLFacadeImplementation) when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 */
	public void initializeDB() {
//		
//		db.getTransaction().begin();
//
//		try {
//
//		   Calendar today = Calendar.getInstance();
//		   
//		   int month=today.get(Calendar.MONTH);
//		   int year=today.get(Calendar.YEAR);
//		   if (month==12) { month=1; year+=1;}  
//	    
//		   
//		    //Create drivers 
//			Driver driver1=new Driver("driver1@gmail.com","Aitor Fernandez");
//			Driver driver2=new Driver("driver2@gmail.com","Ane Gaztañaga");
//			Driver driver3=new Driver("driver3@gmail.com","Test driver");
//
//			
//			//Create rides
//			driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year,month,15), 4, 7);
//			driver1.addRide("Donostia", "Gazteiz", UtilDate.newDate(year,month,6), 4, 8);
//			driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 4, 4);
//
//			driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year,month,7), 4, 8);
//			
//			driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year,month,15), 3, 3);
//			driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 2, 5);
//			driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year,month,6), 2, 5);
//
//			driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,14), 1, 3);
//
//			
//						
//			db.persist(driver1);
//			db.persist(driver2);
//			db.persist(driver3);
//
//	
//			db.getTransaction().commit();
//			System.out.println("Db initialized");
//		}
//		catch (Exception e){
//			e.printStackTrace();
//		}
	}

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */
	public List<String> getDepartCities() {
//			TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
		EntityManager em = JPAUtil.getEntityManager();
		List<Ride> query = new ArrayList<Ride>();
		List<String> result = new ArrayList<String>();
		try {
			em.getTransaction().begin();
			query = em.createQuery("from Ride", Ride.class).getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
		for (Ride r : query)
			result.add(r.getpFrom());
		return result;

//		 List<EventoLogin> result = new ArrayList<EventoLogin>(); 
//		List<String> cities = query.getResultList();
//			return cities;

	}

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from) {
//		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",String.class);
//		query.setParameter(1, from);
//		List<String> arrivingCities = query.getResultList(); 
//		return arrivingCities;
		EntityManager em = JPAUtil.getEntityManager();
		List<Ride> query = new ArrayList<Ride>();
		List<String> result = new ArrayList<String>();
		try {
			em.getTransaction().begin();
			query = em.createQuery("from Ride where pfrom = '" + from + "'", Ride.class).getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
		for (Ride r : query)
			result.add(r.getpTo());
		return result;
	}

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param nPlaces     available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException         if the same ride already exists for
	 *                                           the driver
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {

		// System.out.println(">> DataAccess: createRide=> from= "+from+" to= "+to+"
		// driver="+driverEmail+" date "+date);
//		try {
//			if(new Date().compareTo(date)>0) {
//				throw new RideMustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
//			}
//			db.getTransaction().begin();
//			
//			Driver driver = db.find(Driver.class, driverEmail);
//			if (driver.doesRideExists(from, to, date)) {
//				db.getTransaction().commit();
//				throw new RideAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
//			}
//			Ride ride = driver.addRide(from, to, date, nPlaces, price);
//			//next instruction can be obviated
//			db.persist(driver); 
//			db.getTransaction().commit();
//
//			return ride;
//		} catch (NullPointerException e) {
//			// TODO Auto-generated catch block
//			db.getTransaction().commit();
//			return null;
//		}
		Ride r = new Ride();
		if (new Date().compareTo(date) > 0) {
			throw new RideMustBeLaterThanTodayException("RideMustBeLaterThanToday");
		}
		EntityManager em = JPAUtil.getEntityManager();
		List<Driver> drivers = new ArrayList<Driver>();
		try {
			em.getTransaction().begin();
			drivers = em.createQuery("from Driver where email = '" + driverEmail + "'", Driver.class).getResultList();
			if (drivers.size() == 1) {
				System.out.println("Driver found");
				Driver driver = drivers.get(0);
				if (driver.doesRideExists(from, to, date))
					throw new RideAlreadyExistException("RideAlreadyExists");
				else {
					r = driver.addRide(from, to, date, nPlaces, price);
					em.persist(r);
					em.refresh(driver);
				}
				em.getTransaction().commit();
			} else {
				System.out.println("Driver doesnt exist");
			}
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
		return r;
	}

	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		EntityManager em = JPAUtil.getEntityManager();
		List<Ride> query = new ArrayList<Ride>();
		try {
			em.getTransaction().begin();

			from = from.replace("'", "''");
			to = to.replace("'", "''");
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			query = em.createQuery("from Ride where pfrom = '" + from + "' and pto = '" + to + "'"
					+ " and YEAR(date) = " + localDate.getYear() + " and MONTH(date) = " + localDate.getMonthValue()
					+ " and DAY(date) = " + localDate.getDayOfMonth(), Ride.class).getResultList();

//			  
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
		return query;
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
//		System.out.println(">> DataAccess: getEventsMonth");
//		List<Date> res = new ArrayList<>();	
//		
//		Date firstDayMonthDate= UtilDate.firstDayMonth(date);
//		Date lastDayMonthDate= UtilDate.lastDayMonth(date);
//				
//		
//		TypedQuery<Date> query = db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",Date.class);   
//		
//		query.setParameter(1, from);
//		query.setParameter(2, to);
//		query.setParameter(3, firstDayMonthDate);
//		query.setParameter(4, lastDayMonthDate);
//		List<Date> dates = query.getResultList();
//	 	 for (Date d:dates){
//		   res.add(d);
//		  }
//	 	return res;
		Date firstDayMonthDate = firstDayMonth(date);
		Date lastDayMonthDate = lastDayMonth(date);

		EntityManager em = JPAUtil.getEntityManager();
		List<Ride> query = new ArrayList<Ride>();
		List<Date> result = new ArrayList<Date>();
		try {
			em.getTransaction().begin();
			query = em.createQuery("from Ride where pfrom = " + from + " and pto = " + to + " and date between"
					+ firstDayMonthDate + " and " + lastDayMonthDate, Ride.class).getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
		for (Ride r : query)
			result.add(r.getDate());
		return result;
	}

	public static Date firstDayMonth(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setTimeZone(TimeZone.getTimeZone("CET"));
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}

	public static Date lastDayMonth(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setTimeZone(TimeZone.getTimeZone("CET"));
		// int month=calendar.get(Calendar.MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();

	}

	public void open() {
//		
//		String fileName=c.getDbFilename();
//		if (c.isDatabaseLocal()) {
//			emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
//			db = emf.createEntityManager();
//		} else {
//			Map<String, String> properties = new HashMap<>();
//			  properties.put("javax.persistence.jdbc.user", c.getUser());
//			  properties.put("javax.persistence.jdbc.password", c.getPassword());
//
//			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);
//			  db = emf.createEntityManager();
//    	   }
//		System.out.println("DataAccess opened => isDatabaseLocal: "+c.isDatabaseLocal());
//
//		
	}

	public void close() {
//		db.close();
//		System.out.println("DataAcess closed");
	}

}
