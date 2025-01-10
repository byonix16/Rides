package modelo.bean;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.primefaces.event.SelectEvent;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named; 
import modelo.businessLogic.*;
import modelo.dominio.*;
import modelo.exceptions.RideAlreadyExistException;
import modelo.exceptions.RideMustBeLaterThanTodayException;

@Named("main")
@SessionScoped
public class MainBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private Date fecha;
	private String from;
	private String to;
	private List<Ride> rides;
	private List<String> starts;
	private List<String> finishes;
	private String start;
	private String end;
	private BLFacadeImplementation bl;
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	private String places;
	private String price;
	
	public List<String> getStarts() {
		return starts;
	}

	public void setStarts(List<String> starts) {
		this.starts = starts;
	}

	public List<String> getFinishes() {
		return finishes;
	}

	public void setFinishes(List<String> finishes) {
		this.finishes = finishes;
	}

	public String getPlaces() {
		return places;
	}

	public void setPlaces(String places) {
		this.places = places;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Date getFecha() {
	return fecha;
	}
	
	public void setFecha(Date fecha) {
	this.fecha = fecha;
	}

	
	public String getNombre() {
		 return nombre;
		 }
		public void setNombre(String nombre) {
		 this.nombre = nombre;
		 }
		
	public String crear() {
		bl = new BLFacadeImplementation();
		 return "crear";
		 } 
	
	public String main() {
		 return "main";
		 } 
	
	public String comprobar() {
		bl = new BLFacadeImplementation();
		starts = bl.getDepartCities();
		 return "comprobar";
		 } 
	
	public void onDateSelect(SelectEvent event) {
		 FacesContext.getCurrentInstance().addMessage(null,
		 new FacesMessage("Fecha escogida: "+event.getObject()));
		 System.out.println(event.getObject());
		 fecha = (Date) event.getObject();
		} 
	public void fillRides() {
		if(fecha == null || start == null || end == null)
			System.out.println("Missing data");
		else {
			if(!start.equals("") && !end.equals("")) {
				setRides(bl.getRides(start, end, fecha));
			} else
				System.out.println("Missing data");
		}
		
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	} 
	
	public void createRide() throws RideMustBeLaterThanTodayException, RideAlreadyExistException {
		if(checkdata()) {
					int np;
					float pr;
					boolean error = false;
					try {
						np =Integer.parseInt(places);
						pr =Float.parseFloat(price);
						bl.createRide(from, to, fecha, np, pr,"test@ikasle.ehu.eus");
						System.out.println("Ride created");
						
					} catch (Exception e) {
						error = true;
					}
					if (error) 
						System.out.println("An error ocurred");
		}
				
	}
	
	public boolean checkdata() throws NumberFormatException{
		boolean r = true;
		
		if(fecha == null || from == null || to == null || places == null || price == null) {
			System.out.println("Missing data");
			r=false;
		} else if (from.equals("") || to.equals("") || places.equals("") || price.equals("")) {
			System.out.println("Missing data");
			r=false;
		} else { 
			try {
			if (Float.parseFloat(price) < 0 || Integer.parseInt(places) < 0 || Integer.parseInt(places) > 20) {
				System.out.println("Data is negative or excessive");
				r=false;
			}
			} catch (Exception e) {
				System.out.println("Erroneous data");
				r=false;
			}
		}
		
		return r;
	}
	
	public void fillends() {
		finishes = bl.getDestinationCities(start);
	}
	
	public void doNothing() {
		//this method does nothing but its used to force a refresh
	}
}
