package uo.sdi.convert;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import uo.sdi.model.Trip;
import uo.sdi.presentation.TripService;
 
 
@FacesConverter("TripConverter")
public class TripConverter implements Converter {
 
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                TripService service = (TripService) fc.getExternalContext().getApplicationMap().get("TripService");
                return service.getTrips().get(Integer.parseInt(value));
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Trip."));
            }
        }
        else {
            return null;
        }
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null && !(object instanceof String)) {
            return String.valueOf(((Trip) object).getId());
        }
        else {
            return null;
        }
    }   
}     
