package uo.sdi.acciones;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uo.sdi.model.ListaApuntados;
import uo.sdi.model.Seat;
import uo.sdi.model.SeatStatus;
import uo.sdi.model.Trip;
import uo.sdi.model.User;
import uo.sdi.model.ListaApuntados.PeticionEstado;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.TripDao;
import uo.sdi.persistence.UserDao;

public class AceptarPeticionAction implements Accion {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		UserDao dao = PersistenceFactory.newUserDao();
		String pene = request.getParameter("IdUsuarioAceptar");
		User u = dao.findById(Long.valueOf(pene));
		TripDao daoViaje = PersistenceFactory.newTripDao();
		Trip v = daoViaje.findById(Long.parseLong(request.getParameter("IdViajeAceptar")));
		PeticionEstado estado = PeticionEstado.valueOf(request.getParameter("estado"));
		Long[] ids = new Long[2];
		ids[0] = u.getId();
		ids[1] = v.getId();
		Seat asiento = null;
		if(estado.equals(PeticionEstado.EXCLUDED)){
			asiento = PersistenceFactory.newSeatDao().findById(ids);
			asiento.setStatus(SeatStatus.ACCEPTED);
			asiento.setUserId(u.getId());
			asiento.setTripId(v.getId());
			asiento.setComment("");
			v.setAvailablePax(v.getAvailablePax()-1);
			PersistenceFactory.newSeatDao().update(asiento);
			daoViaje.update(v);
		}
		if(estado.equals(PeticionEstado.PENDANT)){
			asiento = new Seat();
			asiento.setStatus(SeatStatus.ACCEPTED);
			asiento.setUserId(u.getId());
			asiento.setTripId(v.getId());
			asiento.setComment("");
			v.setAvailablePax(v.getAvailablePax()-1);
			PersistenceFactory.newSeatDao().save(asiento);
			daoViaje.update(v);
		}
		
		@SuppressWarnings("unchecked")
		List<ListaApuntados> lista = (List<ListaApuntados>) request.getSession().getAttribute("listaApuntadoPromotor");
		for(ListaApuntados t : lista){
			if(t.getUsuario().getId().equals(u.getId()) && t.getViaje().getId().equals(v.getId())){
				t.setAsiento(asiento);
				t.setViaje(v);
				t.setRelacionViaje();
			}
		}
		@SuppressWarnings("unchecked")
		List<Trip> l = (List<Trip>) request.getServletContext().getAttribute("listaViajes");
		for(Trip t : l){
			if(t.getId().equals(v.getId())){
				t.setAvailablePax(v.getAvailablePax());
			}
		}
		@SuppressWarnings("unchecked")
		List<Trip> l2 = (List<Trip>) request.getSession().getAttribute("listaPromotor");
		for(Trip t : l2){
			if(t.getId().equals(v.getId()))
				t.setAvailablePax(v.getAvailablePax());
		}
		request.getSession().setAttribute("listaPromotor",l2);
		request.getSession().setAttribute("listaApuntadoPromotor",lista);
		request.getServletContext().setAttribute("listaViajes",l);
		return "EXITO";
	}

}
