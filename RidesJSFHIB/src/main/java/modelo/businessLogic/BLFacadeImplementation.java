package modelo.businessLogic;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

//import javax.jws.WebMethod;
//import javax.jws.WebService;

//import configuration.ConfigXML;
import modelo.dataAccess.DataAccess;
import modelo.dominio.*;
import modelo.exceptions.*;


/**
 * It implements the business logic as a web service.
 */
//@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation  implements BLFacade {
	DataAccess dbManager;

	public BLFacadeImplementation()  {		
//		System.out.println("Creating BLFacadeImplementation instance");
//		
//		
		    dbManager=new DataAccess();
//		    
//		//dbManager.close();

		
	}
	
    public BLFacadeImplementation(DataAccess da)  {
//		
//		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
//		ConfigXML c=ConfigXML.getInstance();
//		
//		dbManager=da;		
	}
    
    
    /**
     * {@inheritDoc}
     */
    public List<String> getDepartCities(){
		return dbManager.getDepartCities();	
    	
    }
    /**
     * {@inheritDoc}
     */
	public List<String> getDestinationCities(String from){
		return dbManager.getArrivalCities(from);
	}

	/**
	 * {@inheritDoc}
	 */
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
		return dbManager.createRide(from, to, date, nPlaces, price, driverEmail);
   };
	
   /**
    * {@inheritDoc}
    */
	public List<Ride> getRides(String from, String to, Date date){
		return dbManager.getRides(from, to, date);
	}

    
	/**
	 * {@inheritDoc}
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		return dbManager.getThisMonthDatesWithRides(from, to, date);
	}
	
	
	public void close() {
//		DataAccess dB4oManager=new DataAccess();
//
//		dB4oManager.close();

	}

	/**
	 * {@inheritDoc}
	 */
//    @WebMethod	
	 public void initializeBD(){
//    	dbManager.open();
//		dbManager.initializeDB();
//		dbManager.close();
	}

}

