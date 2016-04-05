package uo.sdi.business.impl;

import java.util.Map;

import javax.faces.context.FacesContext;

import alb.util.log.Log;
import uo.sdi.business.UserService;
import uo.sdi.model.User;
import uo.sdi.model.UserStatus;
import uo.sdi.persistence.PersistenceFactory;

public class UserServiceImpl implements UserService{
	
	public User buscaUsuario(String login){
		return PersistenceFactory.newUserDao().findByLogin(login);
	}
	
	public void crearUsuario(User usuario, String comparaPass) throws Exception{
		usuario.setStatus(UserStatus.ACTIVE);
		try {
			if(comparaPass.equals(usuario.getPassword())){
			PersistenceFactory.newUserDao().save(usuario);
			Log.info("El usuario [%s] ha sido creado satisfactoriamente",
					usuario.getLogin());
			}
			else{
				throw new Exception();
			}
		} catch (Exception e) {
			Log.error("Ha ocurrido algo creando al usuario [%s]",
					usuario.getLogin());
			throw new Exception();
		}
	}
	
	public void iniciaSesion(User usuario){
		putUserInSession(usuario);
	}
	
	public void cerrarSesion(User usuario){
		putUserOutSession(usuario);
	}
	
	public void actualizarUsario(User usuario){
		PersistenceFactory.newUserDao().update(usuario);
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
}
