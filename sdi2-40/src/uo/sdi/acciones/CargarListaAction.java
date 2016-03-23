package uo.sdi.acciones;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uo.sdi.model.Application;
import uo.sdi.model.ListaApuntados;
import uo.sdi.model.Seat;
import uo.sdi.model.Trip;
import uo.sdi.persistence.PersistenceFactory;

public class CargarListaAction implements Accion {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		List<ListaApuntados> apuntados = new ArrayList<ListaApuntados>();
		List<Application> reservas = PersistenceFactory.newApplicationDao()
				.findAll();
		List<Seat> asientos = PersistenceFactory.newSeatDao().findAll();
		setListaApuntadosPromotor(request, apuntados, reservas, asientos);
		return "EXITO";
	}

	private void setListaApuntadosPromotor(HttpServletRequest request,
			List<ListaApuntados> apuntados, List<Application> reservas,
			List<Seat> asientos) {
		@SuppressWarnings("unchecked")
		List<Trip> viajes = (List<Trip>) request.getSession().getAttribute(
				"listaPromotor");
		for (Trip t : viajes) {
			for (Application ap : reservas) {
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
						apuntados.add(a);
				}
			}
		}
		request.getSession().setAttribute("listaApuntadoPromotor", apuntados);
	}

}
