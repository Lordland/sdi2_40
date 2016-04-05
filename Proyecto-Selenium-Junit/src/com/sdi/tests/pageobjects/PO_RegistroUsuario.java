package com.sdi.tests.pageobjects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class PO_RegistroUsuario {

	
	
   public void rellenaFormulario(WebDriver driver, String pnombre, String papellidos, String piduser, String pemail, String ppass, String ppass2)
   {
		WebElement login = driver.findElement(By.id("form-registro:login"));
		login.click();
		login.clear();
		login.sendKeys(pnombre);
		WebElement idnombre = driver.findElement(By.id("form-registro:nombre"));
		idnombre.click();
		idnombre.clear();
		idnombre.sendKeys(papellidos);
		WebElement idapellidos = driver.findElement(By.id("form-registro:apellidos"));
		idapellidos.click();
		idapellidos.clear();
		idapellidos.sendKeys(piduser);
		WebElement idcorreo = driver.findElement(By.id("form-registro:email"));
		idcorreo.click();
		idcorreo.clear();
		idcorreo.sendKeys(pemail);
		WebElement idpass = driver.findElement(By.id("form-registro:pass"));
		idpass.click();
		idpass.clear();
		idpass.sendKeys(ppass);
		WebElement idpass2 = driver.findElement(By.id("form-registro:pass2"));
		idpass2.click();
		idpass2.clear();
		idpass2.sendKeys(ppass2);
		//Pulsar el boton de Salvar
		By boton = By.id("form-registro:botonSalvarUsuario");
		driver.findElement(boton).click();	   
   }
	
	
	
}
