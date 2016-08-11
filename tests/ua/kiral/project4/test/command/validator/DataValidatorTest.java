package ua.kiral.project4.test.command.validator;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.kiral.project4.mock.request.MockRequestContainer;
import ua.kiral.project4.model.command.validation.DataValidator;

import static ua.kiral.project4.test.command.CommandsProperties.*;

public class DataValidatorTest {
	private static DataValidator validator;
	private static MockRequestContainer container;

	@BeforeClass
	public static void beforeClass() {
		validator = new DataValidator(ResourceBundle.getBundle("resources/commands"));
		container = new MockRequestContainer(new HashMap<>(), null, null);
	}

	@AfterClass
	public static void afterClass() {
		validator = null;
		container = null;
	}
	
	@Test
	public void validateLoginAndPasswordTest() throws ReflectiveOperationException {
		Method method = DataValidator.class.getDeclaredMethod("validateLoginType", String.class);
		method.setAccessible(true);

		assertFalse((Boolean) method.invoke(validator, ""));
		assertFalse((Boolean) method.invoke(validator, "324235jkdsfsjdkfsjkfjsfsfsf8474834ryjdknfsjdjsdf"));
		assertFalse((Boolean)method.invoke(validator, "&fggd"));
		assertFalse((Boolean)method.invoke(validator, "DUKENUKEM&#"));
		assertFalse((Boolean)method.invoke(validator, "!@#$%^&*(()_+"));
		assertFalse((Boolean)method.invoke(validator, "Паша Тигр"));
		assertFalse((Boolean)method.invoke(validator, "PAsha TIGR"));
		assertTrue((Boolean)method.invoke(validator, "PAsha_TIGR"));
	}
	
	@Test
	public void validateEmailTest() throws ReflectiveOperationException {
		Method method = DataValidator.class.getDeclaredMethod("validateEmailType", String.class);
		method.setAccessible(true);
		
		assertFalse((Boolean) method.invoke(validator, ""));
		assertFalse((Boolean)method.invoke(validator, "DUKENUK#@EM&#"));
		assertFalse((Boolean)method.invoke(validator, "Паша@Тигр"));
		assertFalse((Boolean)method.invoke(validator, "Паша@Тигр.gmail"));
		assertTrue((Boolean)method.invoke(validator, "PAsha@TIGR.gmail"));
		
	}
	
	@Test
	public void validateLogin(){
		container.clearContainerParams();
		container.setParameter(getKey("userLogin"), "Vasya");
		container.setParameter(getKey("userPass"), "Pupkin");
		
		assertTrue(validator.validateLoginForm(container));
		
		container.clearContainerParams();
		container.setParameter(getKey("userLogin"), "3429502398&&&");
		container.setParameter(getKey("userPass"), ")");
		
		assertFalse(validator.validateLoginForm(container));
		
		container.clearContainerParams();
		container.setParameter(getKey("userLogin"), "Время - утро. Пол 6-го");
		container.setParameter(getKey("userPass"), "А я пишу код. Сурово. ");
		
		assertFalse(validator.validateLoginForm(container));
		
	}
}
