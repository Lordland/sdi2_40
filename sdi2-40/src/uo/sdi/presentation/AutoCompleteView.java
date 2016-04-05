package uo.sdi.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
 

import org.primefaces.event.SelectEvent;

import uo.sdi.model.Trip;
 
@ManagedBean
public class AutoCompleteView {
     
    private List<Trip> selectedTrips;
     
    @ManagedProperty("#{TripService}")
    private TripService service;
    
    private Trip viaje;

	public List<Trip> completeTripCity(String query) {
        List<Trip> allTrips = service.getTrips();
        List<Trip> filteredTrips = new ArrayList<Trip>();
         
        for (int i = 0; i < allTrips.size(); i++) {
            Trip skin = allTrips.get(i);
            if(skin.getDeparture().getCity().toLowerCase().startsWith(query) 
            		|| skin.getDestination().getCity().toLowerCase().startsWith(query)) {
                filteredTrips.add(skin);
            }
        }
         
        return filteredTrips;
    }
	
	public Trip getViaje() {
		return viaje;
	}

	public void setViaje(Trip viaje) {
		this.viaje = viaje;
	}
    
    public List<Trip> completeTripCountry(String query) {
        List<Trip> allTrips = service.getTrips();
        List<Trip> filteredTrips = new ArrayList<Trip>();
         
        for (int i = 0; i < allTrips.size(); i++) {
            Trip skin = allTrips.get(i);
            if(skin.getDeparture().getCountry().toLowerCase().startsWith(query)
            		|| skin.getDestination().getCountry().toLowerCase().startsWith(query)) {
                filteredTrips.add(skin);
            }
        }
         
        return filteredTrips;
    }
    
    public List<Trip> completeTripState(String query) {
        List<Trip> allTrips = service.getTrips();
        List<Trip> filteredTrips = new ArrayList<Trip>();
         
        for (int i = 0; i < allTrips.size(); i++) {
            Trip skin = allTrips.get(i);
            if(skin.getDeparture().getState().toLowerCase().startsWith(query)
            		|| skin.getDestination().getState().toLowerCase().startsWith(query)) {
                filteredTrips.add(skin);
            }
        }
         
        return filteredTrips;
    }

 
    public List<Trip> getSelectedTrips() {
        return selectedTrips;
    }
 
    public void setSelectedTrips(List<Trip> selectedTrips) {
        this.selectedTrips = selectedTrips;
    }
     
    public void setService(TripService service) {
        this.service = service;
    }
 
}