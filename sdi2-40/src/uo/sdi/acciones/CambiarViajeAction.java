package uo.sdi.acciones;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uo.sdi.model.Trip;
import uo.sdi.model.TripStatus;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.TripDao;

public class CambiarViajeAction implements Accion {

	@SuppressWarnings("unchecked")
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		TripDao dao = PersistenceFactory.newTripDao();
		Trip viaje = dao.findById(Long.parseLong(request.getParameter("IdViaje")));
		Trip viajeABorrar = null;
		String estado = request.getParameter("estado");
		if(estado == null)
			estado = "";
		if(estado.equals("hecho"))
			viaje.setStatus(TripStatus.DONE);
		else if(estado.equals("cerrado"))
			viaje.setStatus(TripStatus.CLOSED);
		else if(viaje.getAvailablePax()  == 0){
			viaje.setStatus(TripStatus.CLOSED);
		}
		else if(viaje.getStatus() == TripStatus.OPEN){
		viaje.setStatus(TripStatus.CANCELLED);
		}
		else if(viaje.getStatus()  == TripStatus.CANCELLED){
			viaje.setStatus(TripStatus.OPEN);
		}
		else if(viaje.getStatus()  == TripStatus.DONE){
			viaje.setStatus(TripStatus.OPEN);
		}
		else if(viaje.getStatus()  == TripStatus.CLOSED){
			viaje.setStatus(TripStatus.OPEN);
		}
		
		
		
		dao.update(viaje);
		List<Trip> lista = (List<Trip>) request.getSession().getAttribute("listaPromotor");
		for(Trip t : lista){
			if(t.getId().equals(viaje.getId())){
				viajeABorrar = t;
			}
		}
		lista.remove(viajeABorrar);
		lista.add(viaje);
		request.getSession().setAttribute("listaPromotor",lista);
		return "EXITO";
	}

}
