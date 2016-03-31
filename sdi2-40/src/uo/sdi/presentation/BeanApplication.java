package uo.sdi.presentation;

import uo.sdi.model.Application;
import uo.sdi.model.Trip;
import uo.sdi.persistence.ApplicationDao;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.TripDao;

public class BeanApplication {

	BeanUsuario bu = new BeanUsuario();
	BeanViajes bv = new BeanViajes();
	
	
	public BeanApplication(){
		
	}

	/**
	 * Este método registra en los viajes apuntados los viajes a los que se
	 * apunte el usuario.
	 */
	public void apuntarse() {
		ApplicationDao ad = PersistenceFactory.newApplicationDao();
		Application a = new Application();
		a.setTripId(bv.getViaje().getId());
		a.setUserId(bu.getUsuario().getId());
		ad.save(a);
	}
	
}
