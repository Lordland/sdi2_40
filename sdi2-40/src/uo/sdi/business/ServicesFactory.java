package uo.sdi.business;

import uo.sdi.business.impl.ApplicationServiceImpl;
import uo.sdi.business.impl.TripServiceImpl;
import uo.sdi.business.impl.UserServiceImpl;

public class ServicesFactory {

	
	public static TripService newTripService(){
		return new TripServiceImpl();
	}
	
	public static UserService newUserService(){
		return new UserServiceImpl();
	}
	
	public static ApplicationService newApplicationService(){
		return new ApplicationServiceImpl();
	}
}
