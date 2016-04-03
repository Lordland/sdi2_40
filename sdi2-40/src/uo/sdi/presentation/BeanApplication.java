package uo.sdi.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import uo.sdi.model.ListaApuntados;
import uo.sdi.model.ListaApuntados.PeticionEstado;
import uo.sdi.model.Seat;
import uo.sdi.model.SeatStatus;
import uo.sdi.model.Trip;
import uo.sdi.model.User;
import uo.sdi.persistence.ApplicationDao;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.SeatDao;
import uo.sdi.persistence.TripDao;

@ManagedBean(name = "apuntados")
public class BeanApplication {

	@ManagedProperty("#{viajes}")
	BeanViajes bv;
	List<ListaApuntados> listaApuntadosUsuario;

	List<ListaApuntados> listaApuntadosPromotor;
	ListaApuntados apuntado;

	private boolean renderIframeColumn;

	public BeanApplication() {
		if (bv == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ELContext contextoEL = context.getELContext();
			Application apli = context.getApplication();
			ExpressionFactory ef = apli.getExpressionFactory();
			ValueExpression ve = ef.createValueExpression(contextoEL,
					"#{viajes}", BeanViajes.class);
			bv = (BeanViajes) ve.getValue(contextoEL);
		}
		listaApuntadosUsuario = new ArrayList<ListaApuntados>();
		listaApuntadosPromotor = new ArrayList<ListaApuntados>();
		apuntado = new ListaApuntados();
	}

	public boolean isRenderIframeColumn() {
		return renderIframeColumn;
	}

	public void setRenderIframeColumn(boolean renderIframeColumn) {
		this.renderIframeColumn = renderIframeColumn;
	}

	public ListaApuntados getApuntado() {
		return apuntado;
	}

	public void setApuntado(ListaApuntados apuntado) {
		this.apuntado = apuntado;
	}

	public List<ListaApuntados> getListaApuntadosUsuario() {
		return listaApuntadosUsuario;
	}

	public void setListaApuntadosUsuario(
			List<ListaApuntados> listaApuntadosUsuario) {
		this.listaApuntadosUsuario = listaApuntadosUsuario;
	}

	public List<ListaApuntados> getListaApuntadosPromotor() {
		return listaApuntadosPromotor;
	}

	public void setListaApuntadosPromotor(
			List<ListaApuntados> listaApuntadosPromotor) {
		this.listaApuntadosPromotor = listaApuntadosPromotor;
	}

	public void aceptar() {
		Seat s = new Seat();
		SeatDao sd = PersistenceFactory.newSeatDao();
		TripDao td = PersistenceFactory.newTripDao();
		// Añadido en BD
		Long ids[] = { apuntado.getUsuario().getId(),
				apuntado.getViaje().getId() };
		Seat seat = sd.findById(ids);
		if (seat == null) {
			s.setStatus(SeatStatus.ACCEPTED);
			s.setTripId(apuntado.getViaje().getId());
			s.setUserId(apuntado.getUsuario().getId());
			sd.save(s);
		} else {
			seat.setStatus(SeatStatus.ACCEPTED);
			sd.update(seat);
		}
		apuntado.getViaje().setAvailablePax(
				apuntado.getViaje().getAvailablePax() - 1);
		td.update(apuntado.getViaje());
		// Actualizacion interfaz
		bv.listaViaje();
		for (ListaApuntados ap : listaApuntadosPromotor) {
			if (ap.equals(apuntado)) {
				if (ap.getAsiento() == null)
					ap.setAsiento(s);
				else
					ap.setAsiento(seat);
				ap.setRelacionViaje();
			}
		}

	}

	public void cancelar() {
		// Insercion en BD
		SeatDao sd = PersistenceFactory.newSeatDao();
		TripDao td = PersistenceFactory.newTripDao();
		Long ids[] = { apuntado.getUsuario().getId(),
				apuntado.getViaje().getId() };
		Seat s = sd.findById(ids);
		s.setStatus(SeatStatus.EXCLUDED);
		sd.update(s);
		apuntado.getViaje().setAvailablePax(
				apuntado.getViaje().getAvailablePax() + 1);
		td.update(apuntado.getViaje());
		// Actualizacion interfaz
		bv.listaViaje();
		for (ListaApuntados ap : listaApuntadosPromotor) {
			if (ap.equals(apuntado)) {
				ap.setAsiento(s);
				ap.setRelacionViaje();
			}
		}
	}

	public void apuntarse(User usuario) {

		ListaApuntados ap = new ListaApuntados();
		ap.setViaje(bv.getViaje());
		ap.setUsuario(usuario);
		ap.setRelacionViaje();
		boolean esta = false;
		for (ListaApuntados apunta : listaApuntadosUsuario) {
			if (apunta.getViaje().equals(ap.getViaje())
					&& apunta.getUsuario().equals(ap.getUsuario())) {
				esta = true;
			}
		}
		if (!esta) {
			// Añadido a la BD
			ApplicationDao ad = PersistenceFactory.newApplicationDao();
			uo.sdi.model.Application a = new uo.sdi.model.Application();
			a.setTripId(ap.getViaje().getId());
			a.setUserId(ap.getUsuario().getId());
			ad.save(a);
			// Actualizacion de la lista
			if (listaApuntadosUsuario.isEmpty()) {
				listaApuntadosUsuario.add(ap);
			} else {
				listaApuntadosUsuario.add(ap);
			}
		}
	}

	public void desapuntarse() {
		// Borrado de la BD
		ApplicationDao ad = PersistenceFactory.newApplicationDao();
		TripDao td = PersistenceFactory.newTripDao();
		SeatDao sd = PersistenceFactory.newSeatDao();
		Long[] ids = { apuntado.getUsuario().getId(),
				apuntado.getViaje().getId() };
		int borrado = ad.delete(ids);
		if(apuntado.getRelacionViaje().equals(PeticionEstado.ACCEPTED)){
			apuntado.getViaje().setAvailablePax(apuntado.getViaje().getAvailablePax() +1 );
			td.update(apuntado.getViaje());
		}
		if(apuntado.getAsiento() != null){
			sd.delete(ids);
		}
		
		// Actualizacion de la lista
		if (borrado > 0) {
			if (listaApuntadosUsuario.contains(apuntado))
				listaApuntadosUsuario.remove(apuntado);
		}
		bv.listaViajeUsuario(apuntado.getUsuario().getId());

	}

	public void listaApuntadosUsuario(User usuario) {
		listaApuntadosUsuario = new ArrayList<ListaApuntados>();
		List<uo.sdi.model.Application> reservas = PersistenceFactory
				.newApplicationDao().findByUserId(usuario.getId());
		List<Seat> asientos = PersistenceFactory.newSeatDao().findAll();
		List<Trip> viajes = PersistenceFactory.newTripDao().findAll();
		for (Trip t : viajes) {
			for (uo.sdi.model.Application ap : reservas) {
				ListaApuntados a = new ListaApuntados();
				if (ap.getTripId().equals(t.getId())
						&& usuario.getId().equals(ap.getUserId())) {
					a.setUsuario(usuario);
					a.setViaje(t);
					for (Seat s : asientos) {
						if (s.getTripId().equals(t.getId())
								&& usuario.getId().equals(s.getUserId())) {
							a.setAsiento(s);
						}
					}
					a.setRelacionViaje();
					listaApuntadosUsuario.add(a);
				}
			}
		}
	}

	public void listaApuntadosPromotor() {
		listaApuntadosPromotor = new ArrayList<ListaApuntados>();
		List<uo.sdi.model.Application> reservas = PersistenceFactory
				.newApplicationDao().findAll();
		List<Seat> asientos = PersistenceFactory.newSeatDao().findAll();
		for (Trip t : bv.getViajesPromotor()) {
			for (uo.sdi.model.Application ap : reservas) {
				ListaApuntados a = new ListaApuntados();
				if (ap.getTripId().equals(t.getId())) {
					a.setUsuario(PersistenceFactory.newUserDao().findById(
							ap.getUserId()));
					a.setViaje(t);
					for (Seat s : asientos) {
						if (s.getTripId().equals(t.getId())
								&& a.getUsuario().getId().equals(s.getUserId())) {
							a.setAsiento(s);
						}
					}
					a.setRelacionViaje();
					listaApuntadosPromotor.add(a);
				}
			}
		}
	}

	public String vistaPromotor() {
		listaApuntadosPromotor();
		return "promotor";
	}
}
