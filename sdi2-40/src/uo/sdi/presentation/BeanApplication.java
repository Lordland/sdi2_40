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
import uo.sdi.model.Seat;
import uo.sdi.model.SeatStatus;
import uo.sdi.model.Trip;
import uo.sdi.model.User;
import uo.sdi.persistence.ApplicationDao;
import uo.sdi.persistence.PersistenceFactory;

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

	
	public void aceptar(){
		Seat s = new Seat();
		s.setStatus(SeatStatus.ACCEPTED);
		apuntado.setAsiento(s);
		apuntado.setRelacionViaje();
		apuntado.getViaje().setAvailablePax(apuntado.getViaje().getAvailablePax()-1);
	}
	
	public void cancelar(){
		Seat s = apuntado.getAsiento();
		s.setStatus(SeatStatus.EXCLUDED);
		apuntado.setAsiento(s);
		apuntado.setRelacionViaje();
		apuntado.getViaje().setAvailablePax(apuntado.getViaje().getAvailablePax()+1);
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
		Long[] ids = { apuntado.getUsuario().getId(),
				apuntado.getViaje().getId() };
		int borrado = ad.delete(ids);
		// Actualizacion de la lista
		if (borrado > 0) {
			if (listaApuntadosUsuario.contains(apuntado))
				listaApuntadosUsuario.remove(apuntado);
		}

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
		List<uo.sdi.model.Application> reservas = PersistenceFactory.newApplicationDao()
				.findAll();
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
	
	public String vistaPromotor(){
		listaApuntadosPromotor();
		return "promotor";
	}
}
