package uo.sdi.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import alb.util.log.Log;
import uo.sdi.model.Trip;
import uo.sdi.model.User;
import uo.sdi.model.UserStatus;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.TripDao;
import uo.sdi.persistence.UserDao;

@ManagedBean(name = "usuarios")
@SessionScoped
public class BeanUsuario implements Serializable {
	private static final long serialVersionUID = 58741L;
	BeanViajes bv = new BeanViajes();
	private User usuario = new User();
	private String comparaPass;
	private String login;
	private String pass;
	
	private List<Trip> listaPromotor = new ArrayList<Trip>();
	private List<Trip> listaUsuarios = new ArrayList<Trip>();
	private List<Trip> listaApuntados = new ArrayList<Trip>();
	private Trip viaje = new Trip();

	

	public BeanUsuario() {
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public String getComparaPass() {
		return comparaPass;
	}

	public void setComparaPass(String comparaPass) {
		this.comparaPass = comparaPass;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public List<Trip> getListaPromotor() {
		return listaPromotor;
	}

	public void setListaPromotor(List<Trip> listaPromotor) {
		this.listaPromotor = listaPromotor;
	}

	public List<Trip> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Trip> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public List<Trip> getListaApuntados() {
		return listaApuntados;
	}

	public void setListaApuntados(List<Trip> listaApuntados) {
		this.listaApuntados = listaApuntados;
	}

	public Trip getViaje() {
		return viaje;
	}

	public void setViaje(Trip viaje) {
		this.viaje = viaje;
	}
	
	/**
	 * Este método comprueba que los datos de inicio  sean correctos y carga los datos
	 * necesarios para la vista principal (lista de viajes sin los viajes que promueve
	 * (listaUsuarios de BeanViajes) y lista de viajes a los que se apuntó
	 * (listaViajeApuntado)
	 * @return exito en caso de que todo fuera bien y fracaso en el contrario
	 */
	public String iniciarSesion() {
		if (usuario != null) {
			UserDao dao = PersistenceFactory.newUserDao();
			User userByLogin = dao.findByLogin(login);
			if (userByLogin != null && userByLogin.getPassword().equals(pass)) {
				Log.info("El usuario [%s] ha iniciado sesión",
						usuario.getLogin());
				usuario = userByLogin;
				rellenarListas();
				return "exito";
			}
		}
		return "fracaso";
	}
	
	public String rellenarListas(){
		try{
			bv.listaViajePromotor(usuario.getId());
			setListaPromotor(bv.getViajesPromotor());
			bv.listaViajeUsuario(usuario.getId());
			setListaUsuarios(bv.getViajesUsuario());
			bv.listaViajeApuntado(usuario.getId());
			setListaApuntados(bv.getViajesApuntados());
			return "exito";
		}catch(NullPointerException e){
			return "fracaso";
		}
		
	}

	/**
	 * Envía a la BD el objeto User de este bean que ya ha sido 
	 * previamente modificado
	 */
	public void modificarUsuario() {
		UserDao dao = PersistenceFactory.newUserDao();
		dao.update(usuario);
	}

	/**
	 * Cierra la sesión de usuario actual y deja el bean listo para aceptar
	 * nuevos datos.
	 */
	public void cerrarSesion() {
		setUsuario(new User());
		pass = "";
		login = "";
	}
	
	/**
	 * Introduce a la BD el viaje con los datos proporcionados
	 * @return exito si se introdujo adecuadamente y fracaso si hubo algún error
	 */
	public String crearViaje() {
		TripDao dao = PersistenceFactory.newTripDao();
		try {
			dao.save(viaje);
			Log.info("El viaje [%s] ha sido creado satisfactoriamente",
					viaje.getDeparture().getCity()+"-"+viaje.getDestination().getCity());
			setViaje(new Trip());
			return "exito";
		} catch (Exception e) {
			Log.error("Ha ocurrido algo creando el viaje [%s]",
					viaje.getDeparture().getCity()+"-"+viaje.getDestination().getCity());
			setViaje(new Trip());
			return "fracaso";
		}
	}

	/**
	 * Introduce a la BD el usuario con los datos proporcionados y limpia los valores
	 * para que no se muestren después en posteriores formularios si el mismo usuario
	 * quisiera crear varios usuarios.
	 * @return exito si se introdujo adecuadamente y fracaso si hubo algún error
	 */
	public String crearUsuario() {
		UserDao dao = PersistenceFactory.newUserDao();
		usuario.setStatus(UserStatus.ACTIVE);
		try {
			dao.save(usuario);
			Log.info("El usuario [%s] ha sido creado satisfactoriamente",
					usuario.getLogin());
			setUsuario(new User());
			comparaPass = "";
			return "exito";
		} catch (Exception e) {
			Log.error("Ha ocurrido algo creando al usuario [%s]",
					usuario.getLogin());
			setUsuario(new User());
			comparaPass = "";
			return "fracaso";
		}

	}

	

}
