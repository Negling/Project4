package ua.kiral.project4.test.command;

import static org.junit.Assert.assertTrue;
import static ua.kiral.project4.test.command.CommandsProperties.*;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.kiral.project4.mock.dao.MockDAOFactory;
import ua.kiral.project4.mock.request.MockRequestContainer;
import ua.kiral.project4.mock.validator.MockValidator;
import ua.kiral.project4.model.command.impl.RegistrationCommand;

public class RegistrationTest {
	private static MockRequestContainer container;
	private static RegistrationCommand command;

	@BeforeClass
	public static void beforeClass() {
		container = new MockRequestContainer(new HashMap<>(), null, new HashMap<>());
		command = new RegistrationCommand(MockDAOFactory.getInstance(), new MockValidator());
	}
	
	@AfterClass
	public static void afterClass() {
		container = null;
		command = null;
	}

	@Before
	public void before() {
		container.clearContainerAttributes();
		container.clearContainerParams();
	}
	
	@Test
	public void alreadyRegisteredTest() {
		container.setParameter(getKey("userLogin"), "mock");

		/*
		 * by "mock" login value DAO mock will return user object, according to this,
		 * registration cant be continued, failed registration msg expected
		 */

		assertTrue(command.execute(container).equals(getKey("registrationPath")));
		assertTrue(container.getAttribute(getKey("registrationFailedMSG")) != null);
	}
	
	@Test
	public void successTest() {
		container.setParameter(getKey("userLogin"), "null");
		container.setParameter(getKey("userGender"), "");

		/*
		 * with "null" login value DAO mock will return null, and user must be
		 * successfuly registered.
		 */

		assertTrue(command.execute(container).equals(getKey("loginPath")));
		assertTrue(container.getAttribute(getKey("registrationFailedMSG")) == null);
	}
}