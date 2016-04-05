package uo.sdi.business;

import uo.sdi.model.User;

public interface UserService {

	
	public User buscaUsuario(String login);
	
	public void iniciaSesion(User usuario);
	
	public void actualizarUsario(User usuario);
	
	public void cerrarSesion(User usuario);
	
	public void crearUsuario(User usuario, String comparaPass) throws Exception;
	
}
