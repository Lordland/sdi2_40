package uo.sdi.presentation;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.*;
import javax.faces.event.ActionEvent;

import uo.sdi.model.Trip;
import uo.sdi.persistence.PersistenceFactory;

@ManagedBean(name = "viajes")
@SessionScoped
public class BeanViajes implements Serializable {
	private static final long serialVersionUID = 56897L;

	private Trip viaje = new Trip();
	private List<Trip> viajes = null;
	private List<Trip> viajesUsuario = null;
	private List<Trip> viajesPromotor = null;

	/**
	 * Crea el managed bean e inicializa los valores necesarios
	 */
	public BeanViajes() {
		listaViaje();
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

	}

	/**
	 * Cancela un viaje ya existente
	 */
	public void cancelaViaje() {

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
	public void listaViajeUsuario() {

	}

	/**
	 * Lista los viajes de la BD los cuales ell usuario que ha iniciado
	 * sesión es promotor
	 */
	public void listaViajePromotor() {

	}

	/**
	 * Lista los viajes a los que el usuario que ha iniciado sesión se
	 * ha apuntado
	 */
	public void listaViajeApuntado() {

	}

	/**
	 * Modifica un viaje concreto que el
	 * usuario seleccione
	 */
	public void cambiaViaje() {

	}

	/**
	 * Este método registra en los viajes apuntados los viajes a los que se
	 * apunte el usuario.
	 */
	public void apuntarse() {

	}
}