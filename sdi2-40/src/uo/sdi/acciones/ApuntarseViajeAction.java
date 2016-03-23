package uo.sdi.acciones;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import alb.util.log.Log;
import uo.sdi.acciones.Accion;
import uo.sdi.model.Application;
import uo.sdi.model.ListaApuntados;
import uo.sdi.model.Trip;
import uo.sdi.model.User;
import uo.sdi.persistence.ApplicationDao;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.TripDao;

public class ApuntarseViajeAction implements Accion {

	@SuppressWarnings("unchecked")
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		// Obtener viaje por el Id pasado en la request
		TripDao dao = PersistenceFactory.newTripDao();
		Trip viaje = dao.findById(Long.valueOf(request.getParameter("ID")));
		try {
			// Sacar usuario de la sesión
			User usuario = (User) request.getSession().getAttribute("user");
			// Crear reserva y almacenarla
			ApplicationDao daoA = PersistenceFactory.newApplicationDao();
			Application dto = new Application();
			dto.setTripId(viaje.getId());
			dto.setUserId(usuario.getId());
			// Generar listado a mostrar
			ArrayList<ListaApuntados> lista = new ArrayList<ListaApuntados>();
			daoA.save(dto);
			lista = (ArrayList<ListaApuntados>) request.getSession().getAttribute("listaApuntado");
			ListaApuntados a = new ListaApuntados();
			a.setViaje(viaje);
			a.setUsuario(usuario);
			a.setRelacionViaje();
			a.setAsiento(PersistenceFactory.newSeatDao().findByUserAndTrip(
					usuario.getId(), viaje.getId()));
			lista.add(a);
			request.getSession().setAttribute("listaApuntado", lista);
			Log.debug("Reservada plaza para el viaje [%s] por el usuario [%s]",
					viaje.getId(), usuario.getLogin());
		} catch (Exception e) {
			Log.error(
					"Algo ha ocurrido reservando la plaza para el viaje [%s]",
					viaje.getId());
		}
		return "EXITO";
	}

}
