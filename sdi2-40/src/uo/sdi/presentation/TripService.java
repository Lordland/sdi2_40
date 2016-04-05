package uo.sdi.presentation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 



import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import uo.sdi.model.Trip;
import uo.sdi.persistence.PersistenceFactory;
 
 
@ManagedBean(name="TripService", eager = true)
@ApplicationScoped
public class TripService {
     
    private List<Trip> trips;
     
    @PostConstruct
    public void init() {
        trips = PersistenceFactory.newTripDao().findAll();
    }
     
    public List<Trip> getTrips() {
        return trips;
    } 
}