package uo.sdi.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.*;
import uo.sdi.model.Application;
import uo.sdi.model.Trip;
import uo.sdi.model.TripStatus;
import uo.sdi.persistence.ApplicationDao;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.TripDao;

@ManagedBean(name = "viajes")
@SessionScoped
public class BeanViajes implements Serializable {
	private static final long serialVersionUID = 56897L;

	private Trip viaje = new Trip();
	private List<Trip> viajes = null;
	private List<Trip> viajesUsuario = null;
	private List<Trip> viajesPromotor = null;
	private List<Trip> viajesApuntados = null;

	/**
	 * Crea el managed bean e inicializa los valores necesarios
	 */
	public BeanViajes() {
		listaViaje();
		viajesUsuario = new ArrayList<Trip>();
		viajesPromotor = new ArrayList<Trip>();
		viajesApuntados = new ArrayList<Trip>();
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
	
	public List<Trip> getViajesApuntados() {
		return viajesApuntados;
	}

	public void setViajesApuntados(List<Trip> viajesApuntados) {
		this.viajesApuntados = viajesApuntados;
	}

	/**
	 * 
	 */
	public void iniciaViaje() {
		viaje.setStatus(TripStatus.DONE);
	}

	/**
	 * Cancela un viaje ya existente
	 */
	public void cancelaViaje() {
		viaje.setStatus(TripStatus.CANCELLED);
	}
	
	/**
	 * Vuelve a dejar disponible un viaje ya existente que habia sido cancelado
	 */
	public void abreViaje() {
		viaje.setStatus(TripStatus.OPEN);
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
		 for(Trip t : viajes){
			 if(!t.getPromoterId().equals(id)){
				 aux.add(t);
			 }
		 }
		 setViajesUsuario(aux);
	}

	/**
	 * Lista los viajes de la BD los cuales ell usuario que ha iniciado
	 * sesión es promotor
	 */
	public void listaViajePromotor(long id) {
		 List<Trip> aux = new ArrayList<Trip>();
		 for(Trip t : viajes){
			 if(t.getPromoterId().equals(id)){
				 aux.add(t);
			 }
		 }
		 setViajesPromotor(aux);
	}

	/**
	 * Lista los viajes a los que el usuario que ha iniciado sesión se
	 * ha apuntado
	 */
	public void listaViajeApuntado(long id) {
		 ApplicationDao ad = PersistenceFactory.newApplicationDao();
		 TripDao td = PersistenceFactory.newTripDao();
		 List<Application> aux = ad.findByUserId(id);
		 List<Trip> v = new ArrayList<Trip>();
		 for(Application t : aux){
			 v.add(td.findById(t.getTripId()));
		 }
		 setViajesApuntados(v);
	}

	/**
	 * Modifica un viaje concreto que el
	 * usuario seleccione
	 */
	public void cambiaViaje() {
		TripDao td = PersistenceFactory.newTripDao();
		Trip t = td.findById(viaje.getId());
		td.update(t);
	}
	
	
}