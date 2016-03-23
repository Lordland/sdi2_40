package uo.sdi.acciones;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uo.sdi.model.Application;
import uo.sdi.model.ListaApuntados;
import uo.sdi.model.Seat;
import uo.sdi.model.Trip;
import uo.sdi.model.User;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.UserDao;
import alb.util.log.Log;

public class ValidarseAction implements Accion {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {

		String resultado = "EXITO";
		String nombreUsuario = request.getParameter("nombreUsuario");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			UserDao dao = PersistenceFactory.newUserDao();
			User userByLogin = dao.findByLogin(nombreUsuario);
			if (userByLogin != null
					&& userByLogin.getPassword().equals(password)) {
				session.setAttribute("user", userByLogin);
				setListaApuntados(session, userByLogin);
				setListaViajesPromovidos(session, userByLogin);
				Log.info("El usuario [%s] ha iniciado sesión", nombreUsuario);
			} else {
				session.invalidate();
				Log.info("El usuario [%s] no está registrado", nombreUsuario);
				resultado = "FRACASO";
			}
		} else if (!nombreUsuario.equals(session.getAttribute("user"))) {
			Log.info(
					"Se ha intentado iniciar sesión como [%s] teniendo la sesión iniciada como [%s]",
					nombreUsuario,
					((User) session.getAttribute("user")).getLogin());
			session.invalidate();
			resultado = "FRACASO";
		}
		return resultado;
	}

	private void setListaViajesPromovidos(HttpSession session, User userByLogin) {
		List<Trip> viajes;
		List<Trip> promovido = new ArrayList<Trip>();
		viajes = PersistenceFactory.newTripDao().findAll();
		for(Trip t : viajes) {
			if(t.getPromoterId().equals(userByLogin.getId()))
				promovido.add(t);
		}
		session.setAttribute("listaPromotor", promovido);
	}

	private void setListaApuntados(HttpSession session, User userByLogin) {
		List<ListaApuntados> apuntados = new ArrayList<ListaApuntados>();
		List<Application> reservas = PersistenceFactory
				.newApplicationDao().findByUserId(userByLogin.getId());
		List<Seat> asientos = PersistenceFactory.newSeatDao().findAll();
		List<Trip> viajes = PersistenceFactory.newTripDao().findAll();
		for (Trip t : viajes) {
			for (Application ap : reservas) {
				ListaApuntados a = new ListaApuntados();
				if (ap.getTripId().equals(t.getId())
						&& userByLogin.getId().equals(ap.getUserId())) {
					a.setUsuario(userByLogin);
					a.setViaje(t);
					for (Seat s : asientos) {
						if (s.getTripId().equals(t.getId())
								&& userByLogin.getId().equals(
										s.getUserId())) {
							a.setAsiento(s);
						}
					}
					a.setRelacionViaje();
					apuntados.add(a);
				}
			}
		}
		session.setAttribute("listaApuntado", apuntados);
	}

	@Override
	public String toString() {
		return getClass().getName();
	}

}
