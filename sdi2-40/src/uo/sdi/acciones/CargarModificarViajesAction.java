package uo.sdi.acciones;



import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uo.sdi.model.Trip;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.TripDao;

public class CargarModificarViajesAction implements Accion{

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		Long id = Long.valueOf(request.getParameter("IdViaje"));
		TripDao dao = PersistenceFactory.newTripDao();
		Trip dto = dao.findById(id);
		request.getSession().setAttribute("viajeMod", dto);

		request.getSession().setAttribute("llegada", new Timestamp(dto.getArrivalDate().getTime()).toString().replace(" ", "T"));

		request.getSession().setAttribute("salida", new Timestamp(dto.getDepartureDate().getTime()).toString().replace(" ", "T"));
		
		request.getSession().setAttribute("cierre", new Timestamp(dto.getClosingDate().getTime()).toString().replace(" ", "T"));
		return "EXITO";
	}

}
