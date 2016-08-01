package ua.kiral.project4.test.command;

import static org.junit.Assert.*;
import static ua.kiral.project4.test.command.CommandsProperties.*;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.kiral.project4.mock.dao.MockDAOFactory;
import ua.kiral.project4.mock.request.MockRequestContainer;
import ua.kiral.project4.mock.validator.MockValidator;
import ua.kiral.project4.model.command.impl.LocaleChangeCommand;

public class LocaleChangeTest {
	private static MockRequestContainer container;
	private static LocaleChangeCommand command;

	@BeforeClass
	public static void beforeClass() {
		container = new MockRequestContainer(new HashMap<>(), new HashMap<>(), null);
		command = new LocaleChangeCommand(MockDAOFactory.getInstance(), new MockValidator());
	}

	@AfterClass
	public static void afterClass() {
		container = null;
		command = null;
	}

	@Before
	public void before() {
		container.clearSessionAttributes();
		container.clearContainerParams();
		container.setParameter(getKey("requestPath"), getKey("mainPage"));
	}
	
	@Test
	public void changeValueTest() {
		container.setParameter(getKey("locale"), "RU");
		HttpSession ses = container.getSession();
		ses.setAttribute(getKey("locale"), "EN");
		
		/*
		 * session contains EN locale value, after executing command it must
		 * appear as RU value
		 */
		assertTrue(command.execute(container).equals(getKey("mainPage")));
		assertTrue(ses.getAttribute(getKey("locale")).equals("RU"));
	}
	
	@Test
	public void consistentValueTest(){
		container.setParameter(getKey("locale"), "EN");
		HttpSession ses = container.getSession();
		ses.setAttribute(getKey("locale"), "EN");
		
		/*
		 * session contains RU locale value, after executing command it must
		 * appear as EN value
		 */
		assertTrue(command.execute(container).equals(getKey("mainPage")));
		assertTrue(ses.getAttribute(getKey("locale")).equals("EN"));
	}
}
