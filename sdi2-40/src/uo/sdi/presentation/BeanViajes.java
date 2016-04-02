package uo.sdi.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.*;

import org.primefaces.event.RowEditEvent;

import uo.sdi.model.AddressPoint;
import uo.sdi.model.Trip;
import uo.sdi.model.TripStatus;
import uo.sdi.model.Waypoint;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.TripDao;

@ManagedBean(name = "viajes")
@ApplicationScoped
public class BeanViajes implements Serializable {
	private static final long serialVersionUID = 56897L;

	private Trip viaje;
	private List<Trip> viajes = null;
	private List<Trip> viajesUsuario = null;
	private List<Trip> viajesPromotor = null;
	private List<TripStatus> estados = null;
	private TripStatus estado;

	/**
	 * Crea el managed bean e inicializa los valores necesarios
	 */
	public BeanViajes() {
		iniciaViaje();
		listaViaje();
		viajesUsuario = new ArrayList<Trip>();
		viajesPromotor = new ArrayList<Trip>();
		estados = new ArrayList<TripStatus>();
		estados.add(TripStatus.CANCELLED);
		estados.add(TripStatus.CLOSED);
		estados.add(TripStatus.DONE);
		estados.add(TripStatus.OPEN);
	}

	public List<TripStatus> getEstados() {
		return estados;
	}

	public void setEstados(List<TripStatus> estados) {
		this.estados = estados;
	}

	public List<Trip> getViajesUsuario() {
		return viajesUsuario;
	}

	public void setViajesUsuario(List<Trip> viajesUsuario) {
		this.viajesUsuario = viajesUsuario;
	}

	public List<Trip> getViajesPromotor() {
		return viajesPromotor;
	}

	public void setViajesPromotor(List<Trip> viajesPromotor) {
		this.viajesPromotor = viajesPromotor;
	}

	public Trip getViaje() {
		return viaje;
	}

	public void setViaje(Trip viaje) {
		this.viaje = viaje;
	}

	public List<Trip> getViajes() {
		return viajes;
	}

	public void setViajes(List<Trip> viajes) {
		this.viajes = viajes;
	}
	
	/**
	 * 
	 */
	public void iniciaViaje() {
		viaje = new Trip();
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
	}
	

	public void actualizar(RowEditEvent event){
		Trip v = (Trip) event.getObject();
		TripDao td = PersistenceFactory.newTripDao();
		td.update(v);
	} void cancelarActualizar(RowEditEvent event){
		
	}
	/**
	 * Lista todos los viajes existentes en la BD
	 */
	public void listaViaje() {
		setViajes(PersistenceFactory.newTripDao().findAll());
	}

	/**
	 * Lista los viajes de la BD menos aquellos a los que el usuario que ha
	 * iniciado sesión es promotor de ellos
	 */
	public void listaViajeUsuario(long id) {
		List<Trip> aux = new ArrayList<Trip>();
		for (Trip t : viajes) {
			if (!t.getPromoterId().equals(id)) {
				aux.add(t);
			}
		}
		setViajesUsuario(aux);
	}

	/**
	 * Lista los viajes de la BD los cuales ell usuario que ha iniciado sesión
	 * es promotor
	 */
	public void listaViajePromotor(long id) {
		List<Trip> aux = new ArrayList<Trip>();
		for (Trip t : viajes) {
			if (t.getPromoterId().equals(id)) {
				aux.add(t);
			}
		}
		setViajesPromotor(aux);
	}

	/**
	 * Modifica un viaje concreto que el usuario seleccione
	 */
	public void cambiaViaje() {
		TripDao td = PersistenceFactory.newTripDao();
		Trip t = td.findById(viaje.getId());
		td.update(t);
	}

	public TripStatus getEstado() {
		return estado;
	}

	public void setEstado(TripStatus estado) {
		this.estado = estado;
	}

	

}