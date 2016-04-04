package uo.sdi.presentation;

import java.io.Serializable;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import alb.util.log.Log;
import uo.sdi.model.User;
import uo.sdi.model.UserStatus;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.TripDao;
import uo.sdi.persistence.UserDao; 

@ManagedBean(name = "usuarios")
@SessionScoped
public class BeanUsuario implements Serializable {
	private static final long serialVersionUID = 58741L;
	@ManagedProperty("#{viajes}")
	private BeanViajes bv;
	@ManagedProperty("#{apuntados}")
	private BeanApplication ba;
	private User usuario = new User();
	private String comparaPass;
	private String login;
	private String pass;

	public BeanUsuario() {
		if (bv == null && ba == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ELContext contextoEL = context.getELContext();
			Application apli = context.getApplication();
			ExpressionFactory ef = apli.getExpressionFactory();
			ValueExpression ve = ef.createValueExpression(contextoEL,
					"#{viajes}", BeanViajes.class);
			bv = (BeanViajes) ve.getValue(contextoEL);
			ve = ef.createValueExpression(contextoEL, "#{apuntados}",
					BeanApplication.class);
			ba = (BeanApplication) ve.getValue(contextoEL);
		}
	}

	public BeanViajes getBv() {
		return bv;
	}

	public void setBv(BeanViajes bv) {
		this.bv = bv;
	}

	public BeanApplication getBa() {
		return ba;
	}

	public void setBa(BeanApplication ba) {
		this.ba = ba;
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

	/**
	 * Este método comprueba que los datos de inicio sean correctos y carga los
	 * datos necesarios para la vista principal (lista de viajes sin los viajes
	 * que promueve (listaUsuarios de BeanViajes) y lista de viajes a los que se
	 * apuntó (listaViajeApuntado)
	 * 
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
				putUserInSession(usuario);
				rellenarListas();

				return "exito";
			}
		}
		return "fracaso";
	}

	private void putUserInSession(User user) {
		Map<String, Object> session = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		session.put("LOGGEDIN_USER", user);
	}
	
	private void putUserOutSession(User user) {
		Map<String, Object> session = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		session.put("LOGGEDIN_USER", user);
	}

	public String rellenarListas() {
		try {
			bv.listaViajePromotor(usuario.getId());
			bv.listaViajeUsuario(usuario.getId());
			ba.listaApuntadosUsuario(usuario);
			ba.listaApuntadosPromotor();
			return "exito";
		} catch (NullPointerException e) {
			return "fracaso";
		}

	}

	/**
	 * Envía a la BD el objeto User de este bean que ya ha sido previamente
	 * modificado
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
		bv.listaViaje();
		putUserOutSession(null);
	}

	/**
	 * Introduce a la BD el viaje con los datos proporcionados
	 * 
	 * @return exito si se introdujo adecuadamente y fracaso si hubo algún error
	 */
	public String crearViaje() {
		TripDao dao = PersistenceFactory.newTripDao();
		try {
			bv.getViaje().setPromoterId(usuario.getId());
			bv.getViaje().setMaxPax(bv.getViaje().getAvailablePax());
			dao.save(bv.getViaje());
			Log.info("El viaje [%s] ha sido creado satisfactoriamente", bv
					.getViaje().getDeparture().getCity()
					+ "-" + bv.getViaje().getDestination().getCity());
			bv.iniciaViaje();
			bv.listaViaje();
			return "exito";
		} catch (Exception e) {
			Log.error("Ha ocurrido algo creando el viaje [%s]", bv.getViaje()
					.getDeparture().getCity()
					+ "-" + bv.getViaje().getDestination().getCity());
			bv.iniciaViaje();
			return "fracaso";
		}
	}

	/**
	 * Introduce a la BD el usuario con los datos proporcionados y limpia los
	 * valores para que no se muestren después en posteriores formularios si el
	 * mismo usuario quisiera crear varios usuarios.
	 * 
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
