package com.sdi.tests.Tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.sdi.tests.pageobjects.PO_RegistroUsuario;
import com.sdi.tests.pageobjects.PO_RegistroViaje;
import com.sdi.tests.utils.SeleniumUtils;

//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SDI2_Tests {

	WebDriver driver;
	List<WebElement> elementos = null;

	public SDI2_Tests() {
	}

	@Before
	public void run() {
		// Creamos el driver para Firefox. Recomendable usar Firefox.
		driver = new FirefoxDriver();
		// Vamos a la página principal de mi aplicación
		driver.get("http://localhost:8280/sdi2-40");
	}

	@After
	public void end() {
		// Cerramos el navegador
		driver.quit();
	}

	// PRUEBAS

	// 1. [RegVal] Registro de Usuario con datos válidos.
	@Test
	public void t01_RegVal() {
		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu1:gestion1",
				"menu1:registroUsuario");

		SeleniumUtils.EsperaCargaPagina(driver, "id",
				"form-registro:botonSalvarUsuario", 10);

		new PO_RegistroUsuario().rellenaFormulario(driver, "Voda", "Hector",
				"Alvarez", "hec_insti@hotmail.com", "a", "a");

		// Esperamos a que se cargue la pagina de listado
		// concretamente la tabla "tablalistado"
		SeleniumUtils.EsperaCargaPagina(driver, "id", "menu1", 10);

		// Comprobamos que aparezca el elemento insertado en el listado
		SeleniumUtils.textoPresentePagina(driver, "Inicio de sesión");
	}

	// 2. [RegInval] Registro de Usuario con datos inválidos (contraseñas
	// diferentes).
	@Test
	public void t02_RegInval() {
		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu1:gestion1",
				"menu1:registroUsuario");

		SeleniumUtils.EsperaCargaPagina(driver, "id",
				"form-registro:botonSalvarUsuario", 10);

		new PO_RegistroUsuario().rellenaFormulario(driver, "Voda", "Hector",
				"Alvarez", "hec_insti@hotmail.com", "a", "b");

		// Esperamos a que se cargue la pagina de listado
		// concretamente la tabla "tablalistado"
		SeleniumUtils.EsperaCargaPagina(driver, "id", "menu2", 10);

		// Comprobamos que aparezca el elemento insertado en el listado
		SeleniumUtils.textoPresentePagina(driver, "Registro de usuario");
	}

	// 3. [IdVal] Identificación de Usuario registrado con datos válidos.
	@Test
	public void t03_IdVal() {
		// Esperamos a que se cargue el campo de usuario
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		Actions builder = new Actions(driver);
		builder.sendKeys("user1").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user1").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();
		// Encontrar elemento de la siguiente vista
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id",
				"form-principal", 2);
		assertTrue(elementos != null);
	}

	// 4. [IdInval] Identificación de usuario registrado con datos inválidos.
	@Test
	public void t04_IdInval() {
		// Esperamos a que se cargue el campo de usuario
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		Actions builder = new Actions(driver);
		builder.sendKeys("user1").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user2").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();
		// Encontrar elemento de la siguiente vista
		try {
			elementos = SeleniumUtils.EsperaCargaPagina(driver, "id",
					"form-principal", 2);
		} catch (TimeoutException e) {
		}
	}

	// 5. [AccInval] Intento de acceso con URL desde un usuario no público (no
	// identificado). Intento de acceso a vistas de acceso privado.
	@Test
	public void t05_AccInval() {
		// Esperamos a que cargue el inicio de sesión
		SeleniumUtils.textoPresentePagina(driver, "Inicio de sesión");
		// Intentamos acceder a otra página (principal.xhtml en restricted
		driver.get("http://localhost:8280/sdi2-40/restricted/principal.xhtml");
		// Esperamos a cargar De nuevo Inicio de sesión. Fallará si nos dejó ir
		// al otro lado
		SeleniumUtils.textoPresentePagina(driver, "Inicio de sesión");

	}

	// 6. [RegViajeVal] Registro de un viaje nuevo con datos válidos.
	@Test
	public void t06_RegViajeVal() {
		// Esperamos a que se cargue el campo de usuario
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		Actions builder = new Actions(driver);
		builder.sendKeys("user1").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user1").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();

		// Esperamos a que cargue la página principal
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		// Pulsamos para ir a la vista promotor
		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu3:gestion2",
				"menu3:promotor");

		// Esperamos a que cargue la página de promotor
		SeleniumUtils.textoPresentePagina(driver, "Promotor");

		SeleniumUtils.EsperaCargaPagina(driver, "id", "form2", 2);

		// Pulsamos para ir al formulario de crear viaje
		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu4:gestion4",
				"menu4:crearViaje");

		SeleniumUtils.EsperaCargaPagina(driver, "id",
				"form-registro:botonSalvarViaje", 2);

		new PO_RegistroViaje().rellenaFormulario(driver,
				"Avenida de la camocha", "Gijon", "Asturias", "Espania",
				"33391", "55", "48", "Calle corrida", "Cáceres", "Cáceres",
				"Espania", "33857", "35", "89", "19/03/2017 14:00:00",
				"10/03/2017 00:00:00", "8/03/2017 00:00:00", "6", "50",
				"Es mi cumple");

		SeleniumUtils.textoPresentePagina(driver, "Promotor");

	}

	// 7. [RegViajeInVal] Registro de un viaje nuevo con datos inválidos.
	@Test
	public void t07_RegViajeInVal() {
		// Esperamos a que se cargue el campo de usuario
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		Actions builder = new Actions(driver);
		builder.sendKeys("user1").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user1").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();

		// Esperamos a que cargue la página principal
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		// Pulsamos para ir a la vista promotor
		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu3:gestion2",
				"menu3:promotor");

		// Esperamos a que cargue la página de promotor
		SeleniumUtils.textoPresentePagina(driver, "Promotor");

		SeleniumUtils.EsperaCargaPagina(driver, "id", "form2", 2);

		// Pulsamos para ir al formulario de crear viaje
		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu4:gestion4",
				"menu4:crearViaje");

		SeleniumUtils.EsperaCargaPagina(driver, "id",
				"form-registro:botonSalvarViaje", 2);

		new PO_RegistroViaje().rellenaFormulario(driver,
				"Avenida de la camocha", "Gijon", "Asturias", "Espania",
				"33391", "55", "48", "Calle corrida", "Cáceres", "Cáceres",
				"Espania", "33857", "35", "89", "19/03/2017 14:00:00",
				"10/03/2017 00:00:00", "8/03/2017 00:00:00", " 6", " 50",
				"Es mi cumple");

		SeleniumUtils.textoPresentePagina(driver, "Crear viaje");
	}

	// 8. [EditViajeVal] Edición de viaje existente con datos válidos.
	@Test
	public void t08_EditViajeVal() {
		// Esperamos a que se cargue el campo de usuario
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		Actions builder = new Actions(driver);
		builder.sendKeys("user1").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user1").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();

		// Esperamos a que cargue la página principal
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		// Pulsamos para ir a la vista promotor
		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu3:gestion2",
				"menu3:promotor");

		// Esperamos a que cargue la página de promotor
		SeleniumUtils.textoPresentePagina(driver, "Promotor");

		SeleniumUtils.EsperaCargaPagina(driver, "id", "form2", 2);

		// Buscamos el botón de modificar
		WebElement table = driver.findElement(By.id("form2:promotor"));
		// Now get all the TR elements from the table
		List<WebElement> allRows = table.findElements(By.linkText("MODIFICAR"));
		// And iterate over them, getting the cells

		allRows.get(0).click();

		SeleniumUtils.EsperaCargaPagina(driver, "id",
				"form-registro:botonActualizarViaje", 2);

		new PO_RegistroViaje().rellenaFormulario(driver,
				"Avenida de la camocha", "Gijon", "Asturias", "Espania",
				"33391", "55", "48", "Calle corrida", "Cáceres", "Cáceres",
				"Espania", "33857", "35", "89", "19/03/2017 14:00:00",
				"10/03/2017 00:00:00", "8/03/2017 00:00:00", "6", "50",
				"Es mi cumple");

		SeleniumUtils.textoPresentePagina(driver, "Promotor");

	}

	// 9. [EditViajeInVal] Edición de viaje existente con datos inválidos.
	@Test
	public void t09_EditViajeInVal() {
		// Esperamos a que se cargue el campo de usuario
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		Actions builder = new Actions(driver);
		builder.sendKeys("user1").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user1").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();

		// Esperamos a que cargue la página principal
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		// Pulsamos para ir a la vista promotor
		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu3:gestion2",
				"menu3:promotor");

		// Esperamos a que cargue la página de promotor
		SeleniumUtils.textoPresentePagina(driver, "Promotor");

		SeleniumUtils.EsperaCargaPagina(driver, "id", "form2", 2);

		// Buscamos el botón de modificar
		WebElement table = driver.findElement(By.id("form2:promotor"));
		// Now get all the TR elements from the table
		List<WebElement> allRows = table.findElements(By.linkText("MODIFICAR"));
		// And iterate over them, getting the cells

		allRows.get(0).click();

		SeleniumUtils.EsperaCargaPagina(driver, "id",
				"form-registro:botonActualizarViaje", 2);

		new PO_RegistroViaje().rellenaFormulario(driver,
				"Avenida de la camocha", "Gijon", "Asturias", "Espania",
				"33391", "55", "48", "", "Cáceres", "Cáceres", "Espania",
				"33857", "35", "89", "19/03/2017 14:00:00",
				"10/03/2017 00:00:00", "8/03/2017 00:00:00", " 6", "50",
				"Es mi cumple");

		SeleniumUtils.textoPresentePagina(driver, "Modificar viaje");
	}

	// 10. [CancelViajeVal] Cancelación de un viaje existente por un
	// promotor.
	@Test
	public void t10_CancelViajeVal() {
		// Esperamos a que se cargue el campo de usuario
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		Actions builder = new Actions(driver);
		builder.sendKeys("user1").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user1").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();

		// Esperamos a que cargue la página principal
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		// Pulsamos para ir a la vista promotor
		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu3:gestion2",
				"menu3:promotor");

		// Esperamos a que cargue la página de promotor
		SeleniumUtils.textoPresentePagina(driver, "Promotor");

		SeleniumUtils.EsperaCargaPagina(driver, "id", "form2", 2);

		WebElement table = driver.findElement(By.id("form2:promotor"));

		List<WebElement> allRows = table.findElements(By.className("modif"));

		allRows.get(0).click();

		List<WebElement> allRows2 = table.findElements(By.className("combo"));

		WebElement combo = allRows2.get(0);

		Select selectBox = new Select(combo);
		selectBox.selectByValue("CANCELLED");

		allRows.get(0).click();

	}

	// 11. [CancelMulViajeVal] Cancelación de múltiples viajes existentes
	// por un promotor.
	@Test
	public void t11_CancelMulViajeVal() {

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		Actions builder = new Actions(driver);
		builder.sendKeys("user1").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user1").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();

		// Esperamos a que cargue la página principal
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		// Pulsamos para ir a la vista promotor
		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu3:gestion2",
				"menu3:promotor");

		// Esperamos a que cargue la página de promotor
		SeleniumUtils.textoPresentePagina(driver, "Promotor");

		SeleniumUtils.EsperaCargaPagina(driver, "id", "form2", 2);

		WebElement table = driver.findElement(By.id("form2:promotor"));

		List<WebElement> allRows = table.findElements(By.className("modif"));

		allRows.get(0).click();

		List<WebElement> allRows2 = table.findElements(By.className("combo"));

		WebElement combo = allRows2.get(0);

		Select selectBox = new Select(combo);
		selectBox.selectByValue("CANCELLED");

		allRows.get(0).click();

		allRows.get(1).click();

		allRows2 = table.findElements(By.className("combo"));

		combo = allRows2.get(1);

		selectBox = new Select(combo);
		selectBox.selectByValue("CANCELLED");

		allRows.get(1).click();

		allRows.get(2).click();

		allRows2 = table.findElements(By.className("combo"));

		combo = allRows2.get(2);

		selectBox = new Select(combo);
		selectBox.selectByValue("CANCELLED");

		allRows.get(2).click();
	}

	// 12. [Ins1ViajeAceptVal] Inscribir en un viaje un solo usuario y ser
	// admitido por el promotor.
	@Test
	public void t12_Ins1ViajeAceptVal() {

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		Actions builder = new Actions(driver);
		builder.sendKeys("user1").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user1").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();

		// Esperamos a que cargue la página principal
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		WebElement table = driver.findElement(By.id("form-principal:viaje"));

		List<WebElement> allRows = table
				.findElements(By.className("apuntarse"));

		allRows.get(0).click();

		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu3:gestion2",
				"menu3:cerrar1");

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		builder = new Actions(driver);
		builder.sendKeys("user2").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user2").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();

		// Esperamos a que cargue la página principal
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		// Pulsamos para ir a la vista promotor
		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu3:gestion2",
				"menu3:promotor");

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "class", "aceptar",
				2);

		table = driver.findElement(By.id("form2:apuntados"));

		allRows = table.findElements(By.className("aceptar"));

		allRows.get(0).click();

	}

	// 13. [Ins2ViajeAceptVal] Inscribir en un viaje dos usuarios y ser
	// admitidos los dos por el promotor.
	@Test
	public void t13_Ins2ViajeAceptVal() {

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		Actions builder = new Actions(driver);
		builder.sendKeys("user1").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user1").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();

		// Esperamos a que cargue la página principal
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		WebElement table = driver.findElement(By.id("form-principal:viaje"));

		List<WebElement> allRows = table
				.findElements(By.className("apuntarse"));

		allRows.get(0).click();

		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu3:gestion2",
				"menu3:cerrar1");

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		builder = new Actions(driver);
		builder.sendKeys("user3").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user3").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();

		// Esperamos a que cargue la página principal
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		table = driver.findElement(By.id("form-principal:viaje"));

		allRows = table.findElements(By.className("apuntarse"));

		allRows.get(1).click();

		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu3:gestion2",
				"menu3:cerrar1");

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		builder = new Actions(driver);
		builder.sendKeys("user2").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user2").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();

		// Esperamos a que cargue la página principal
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		// Pulsamos para ir a la vista promotor
		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu3:gestion2",
				"menu3:promotor");

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "class", "aceptar",
				2);

		table = driver.findElement(By.id("form2:apuntados"));

		allRows = table.findElements(By.className("rechazar"));

		for(WebElement e : allRows)
			e.click();

		allRows = table.findElements(By.className("aceptar"));

		allRows.get(0).click();
		allRows.get(1).click();
	}

	// 14. [Ins3ViajeAceptInval] Inscribir en un viaje (2 plazas máximo)
	// dos usuarios y ser admitidos los dos y que un tercero intente
	// inscribirse
	// en ese mismo viaje pero ya no pueda por falta de plazas.

	@Test
	public void t14_Ins3ViajeAceptInval() {

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		Actions builder = new Actions(driver);
		builder.sendKeys("user2").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user2").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();

		// Esperamos a que cargue la página principal
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		WebElement table = driver.findElement(By.id("form-principal:viaje"));

		List<WebElement> allRows = table
				.findElements(By.className("apuntarse"));

		allRows.get(0).click();

		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu3:gestion2",
				"menu3:cerrar1");

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		builder = new Actions(driver);
		builder.sendKeys("user3").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user3").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();

		// Esperamos a que cargue la página principal
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		table = driver.findElement(By.id("form-principal:viaje"));

		allRows = table.findElements(By.className("apuntarse"));

		allRows.get(0).click();

		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu3:gestion2",
				"menu3:cerrar1");
		
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		builder = new Actions(driver);
		builder.sendKeys("user1").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("user1").perform();

		// Esperamos a que se cargue el boton
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();

		// Esperamos a que cargue la página principal
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		// Pulsamos para ir a la vista promotor
		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu3:gestion2",
				"menu3:promotor");

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "class", "aceptar",
				2);

		table = driver.findElement(By.id("form2:apuntados"));

		allRows = table.findElements(By.className("rechazar"));

		for(WebElement e : allRows)
			e.click();
		
		allRows = table.findElements(By.className("aceptar"));

		allRows.get(0).click();
		allRows.get(1).click();
		
		SeleniumUtils.ClickSubopcionMenuHover(driver, "menu4:gestion5",
				"menu4:cerrar2");
		
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
		// Click en elelemento
		elementos.get(0).click();
		// Teclea en el campo user
		builder = new Actions(driver);
		builder.sendKeys("vodka").perform();
		// Esperamos a que se cargue el campo de pass
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
		// Click en el elemento
		elementos.get(0).click();
		// Teclea en el campo pass
		builder = new Actions(driver);
		builder.sendKeys("a").perform();
		
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
				2);
		// Click en el elemento
		elementos.get(0).click();
		
		SeleniumUtils.textoPresentePagina(driver, "Página principal");

		table = driver.findElement(By.id("form-principal:viaje"));

		allRows = table.findElements(By.className("apuntarse"));

		allRows.get(0).click();

	}

	// 15. [CancelNoPromotorVal] Un usuario no promotor Cancela plaza.
	@Test
	public void t15_CancelNoPromotorVal() {
		// Esperamos a que se cargue el campo de usuario
				elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "user", 2);
				// Click en elelemento
				elementos.get(0).click();
				// Teclea en el campo user
				Actions builder = new Actions(driver);
				builder.sendKeys("user1").perform();
				// Esperamos a que se cargue el campo de pass
				elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "pass", 2);
				// Click en el elemento
				elementos.get(0).click();
				// Teclea en el campo pass
				builder = new Actions(driver);
				builder.sendKeys("user1").perform();

				// Esperamos a que se cargue el boton
				elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "enviarUser",
						2);
				// Click en el elemento
				elementos.get(0).click();

				// Esperamos a que cargue la página principal
				SeleniumUtils.textoPresentePagina(driver, "Página principal");
				
				WebElement table = driver.findElement(By.id("form-principal:apuntados"));

				List<WebElement> allRows = table
						.findElements(By.className("desapuntarse"));
				
				allRows.get(0).click();
	}

	// 16. [Rech1ViajeVal] Inscribir en un viaje un usuario que será
	// admitido y después rechazarlo por el promotor.

	@Test
	public void t16_Rech1ViajeVal() {

	}

	// 17. [i18N1] Cambio del idioma por defecto a un segundo idioma.
	// (Probar algunas vistas)
	@Test
	public void t17_i18N1() {

	}

	// 18. [i18N2] Cambio del idioma por defecto a un segundo idioma y
	// vuelta al idioma por defecto. (Probar algunas vistas)
	@Test
	public void t18_i18N2() {

	} // 19. [OpFiltrado] Prueba para el filtrado opcional.

	@Test
	public void t19_OpFiltrado() {

	} // 20. [OpOrden] Prueba para la ordenación opcional.

	@Test
	public void t20_OpOrden() {

	} // 21. [OpPag] Prueba para la paginación opcional.

	@Test
	public void t21_OpPag() {

	} // 22. [OpMante] Prueba del mantenimiento programado opcional.

	@Test
	public void t22_OpMante() {

	}

}