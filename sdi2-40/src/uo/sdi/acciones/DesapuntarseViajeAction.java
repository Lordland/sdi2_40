package uo.sdi.acciones;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uo.sdi.model.ListaApuntados;
import uo.sdi.model.Trip;
import uo.sdi.model.User;
import uo.sdi.persistence.ApplicationDao;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.TripDao;
import alb.util.log.Log;

public class DesapuntarseViajeAction implements Accion{

	@SuppressWarnings("unchecked")
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		TripDao dao = PersistenceFactory.newTripDao();
		Trip viaje = dao.findById(Long.valueOf(request.getParameter("IdViaje")));
		try {
		User usuario = (User) request.getSession().getAttribute("user");
		ApplicationDao daoS = PersistenceFactory.newApplicationDao();
		Long[] ids = new Long[2];
		ids[0] = usuario.getId();
		ids[1] = viaje.getId();
		daoS.delete(ids);
		ArrayList<ListaApuntados> lista = (ArrayList<ListaApuntados>) request.getSession().getAttribute("listaApuntado");
		for(ListaApuntados t : lista){
			if(t.getViaje().getId().equals(viaje.getId()) && t.getUsuario().getId().equals(usuario.getId())){
				lista.remove(t);
			}
		}
		request.getSession().setAttribute("listaApuntado", lista);
		Log.debug("Eliminada la plaza para el viaje [%s] por el usuario [%s]",viaje.getId(), usuario.getLogin());
		}
		catch(Exception e){
			Log.error("Algo ha ocurrido eliminando la plaza para el viaje [%s]",viaje.getId());
		}
		return "EXITO";
	}

}
