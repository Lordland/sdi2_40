package com.sdi.tests.pageobjects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_RegistroViaje {

	public void rellenaFormulario(WebDriver driver, String pdirSal,
			String pciuSal, String pesSal, String ppaSal, String pzipSal,
			String platSal, String plonSal, String pdirDes, String pciuDes,
			String pesDes, String ppaDes, String pzipDes, String platDes,
			String plonDes, String pfechLlegada, String pfechSal,
			String pfechLim, String ppmax, String pcoste, String pcomentario) {
		WebElement dirSal = driver.findElement(By
				.id("form-registro:direccionSalida"));
		dirSal.click();
		dirSal.clear();
		dirSal.sendKeys(pdirSal);
		WebElement ciuSal = driver.findElement(By
				.id("form-registro:ciudadSalida"));
		ciuSal.click();
		ciuSal.clear();
		ciuSal.sendKeys(pciuSal);
		WebElement esSal = driver.findElement(By
				.id("form-registro:estadoSalida"));
		esSal.click();
		esSal.clear();
		esSal.sendKeys(pesSal);
		WebElement paSal = driver
				.findElement(By.id("form-registro:paisSalida"));
		paSal.click();
		paSal.clear();
		paSal.sendKeys(ppaSal);
		WebElement zipSal = driver
				.findElement(By.id("form-registro:zipSalida"));
		zipSal.click();
		zipSal.clear();
		zipSal.sendKeys(pzipSal);
		WebElement latSal = driver.findElement(By
				.id("form-registro:latitudSalida"));
		latSal.click();
		//latSal.clear();
		latSal.sendKeys(platSal);
		WebElement lonSal = driver.findElement(By
				.id("form-registro:longitudSalida"));
		lonSal.click();
		//lonSal.clear();
		lonSal.sendKeys(plonSal);
		WebElement dirDes = driver.findElement(By
				.id("form-registro:direccionLlegada"));
		dirDes.click();
		dirDes.clear();
		dirDes.sendKeys(pdirDes);
		WebElement ciuDes = driver.findElement(By
				.id("form-registro:ciudadLlegada"));
		ciuDes.click();
		ciuDes.clear();
		ciuDes.sendKeys(pciuDes);
		WebElement esDes = driver.findElement(By
				.id("form-registro:estadoLlegada"));
		esDes.click();
		esDes.clear();
		esDes.sendKeys(pesDes);
		WebElement paDes = driver.findElement(By
				.id("form-registro:paisLlegada"));
		paDes.click();
		paDes.clear();
		paDes.sendKeys(ppaDes);
		WebElement zipDes = driver.findElement(By
				.id("form-registro:zipLlegada"));
		zipDes.click();
		zipDes.clear();
		zipDes.sendKeys(pzipDes);
		WebElement latDes = driver.findElement(By
				.id("form-registro:latitudLlegada"));
		latDes.click();
		//latDes.clear();
		latDes.sendKeys(platDes);
		WebElement lonDes = driver.findElement(By
				.id("form-registro:longitudLlegada"));
		lonDes.click();
		//lonDes.clear();
		lonDes.sendKeys(plonSal);
		WebElement fechLlegada = driver.findElement(By
				.id("form-registro:fechallegada"));
		fechLlegada.click();
		// fechLlegada.clear();
		fechLlegada.sendKeys(pfechLlegada);
		WebElement fechSal = driver.findElement(By
				.id("form-registro:fechasalida"));
		fechSal.click();
		// fechSal.clear();
		fechSal.sendKeys(pfechSal);
		WebElement fechLim = driver.findElement(By
				.id("form-registro:fechalimite"));
		fechLim.click();
		// fechLim.clear();
		fechLim.sendKeys(pfechLim);
		WebElement pmax = driver.findElement(By
				.id("form-registro:plazasMaximas"));
		pmax.click();
		//pmax.clear();
		pmax.sendKeys(ppmax);
		WebElement coste = driver.findElement(By.id("form-registro:coste"));
		coste.click();
		//coste.clear();
		coste.sendKeys(pcoste);
		WebElement comentario = driver.findElement(By
				.id("form-registro:comentarios"));
		comentario.click();
		comentario.clear();
		comentario.sendKeys(pcomentario);
		// Pulsar el boton de Salvar
		WebElement boton = null;
		try {
			boton = driver.findElement(By.id("form-registro:botonSalvarViaje"));
		} catch (Exception e) {
			boton = driver.findElement(By.id("form-registro:botonActualizarViaje"));
		}
		boton.click();
	}

}
