package uo.sdi.business.impl;

import java.util.Date;
import java.util.List;

import alb.util.log.Log;
import uo.sdi.business.TripService;
import uo.sdi.model.AddressPoint;
import uo.sdi.model.Trip;
import uo.sdi.model.TripStatus;
import uo.sdi.model.User;
import uo.sdi.model.Waypoint;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.TripDao;

public class TripServiceImpl implements TripService{

	
	public void actualizarViaje(Trip v){
		TripDao td = PersistenceFactory.newTripDao();
		td.update(v);
	}
	
	public List<Trip> listarViajes(){
		return PersistenceFactory.newTripDao().findAll();
	}
	
	public void actualizaViajeMod(Trip v){
		v.setAvailablePax(v.getMaxPax());
		TripDao td = PersistenceFactory.newTripDao();
		td.update(v);
	}
	
	public Trip iniciaViaje(){
		Trip viaje = new Trip();
		viaje.setDeparture(new AddressPoint("Mi direccion", "Mi ciudad",
				"Mi país", "Mi provincia", "Mi código postal", new Waypoint(
						0.0, 0.0)));
		viaje.setDestination(new AddressPoint("Mi direccion", "Mi ciudad",
				"Mi país", "Mi provincia", "Mi código postal", new Waypoint(
						0.0, 0.0)));
		viaje.setArrivalDate(new Date());
		viaje.setClosingDate(new Date());
		viaje.setDepartureDate(new Date());
		viaje.setComments("Comentario de prueba");
		viaje.setAvailablePax(5);
		viaje.setMaxPax(5);
		viaje.setEstimatedCost(50.0);
		viaje.setStatus(TripStatus.OPEN);
		return viaje;
	}
	
	public void creaViaje(Trip viaje, User usuario){
		try{
		viaje.setPromoterId(usuario.getId());
		viaje.setMaxPax(viaje.getAvailablePax());
		PersistenceFactory.newTripDao().save(viaje);
		Log.info("El viaje [%s] ha sido creado satisfactoriamente", viaje.getDeparture().getCity()
				+ "-" + viaje.getDestination().getCity());
		}
		catch(Exception e){
			Log.error("Ha ocurrido algo creando el viaje [%s]", viaje
					.getDeparture().getCity()
					+ "-" + viaje.getDestination().getCity());
		}
		
	}
	
}
