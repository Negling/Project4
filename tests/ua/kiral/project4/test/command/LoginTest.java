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
import ua.kiral.project4.model.command.impl.LoginCommand;
import ua.kiral.project4.model.dao.exceptions.DAOException;

public class LoginTest {
	private static MockRequestContainer container;
	private static LoginCommand command;

	@BeforeClass
	public static void beforeClass() throws DAOException {
		container = new MockRequestContainer(new HashMap<>(), new HashMap<>(), new HashMap<>());
		command = new LoginCommand(MockDAOFactory.getInstance(), new MockValidator());
	}

	@AfterClass
	public static void afterClass() {
		container = null;
		command = null;
	}

	@Before
	public void after() {
		container.clearContainerParams();
		container.clearSessionAttributes();
	}

	@Test
	public void loggedUserTest() {
		HttpSession ses = container.getSession(false);
		ses.setAttribute(getKey("userOnline"), true);
		ses.setAttribute(getKey("userLogin"), "mock");

		assertTrue(getKey("contentPath").equals(command.execute(container)));
	}

	@Test
	public void loggedAdminTest() {
		HttpSession ses = container.getSession(false);
		ses.setAttribute(getKey("userOnline"), true);
		ses.setAttribute(getKey("userLogin"), getKey("adminLogin"));

		assertTrue(getKey("adminPath").equals(command.execute(container)));
	}

	@Test
	public void wrongPasswordTest() {
		HttpSession ses = container.getSession(false);
		container.setParameter(getKey("userLogin"), "mock");
		container.setParameter(getKey("userPass"), "qwerty4");

		assertTrue(getKey("loginPath").equals(command.execute(container)));
		assertTrue(container.getAttribute(getKey("loginFailedMSD")) != null);
		assertTrue(ses.getAttribute(getKey("userOnline")) == null);
	}

	@Test
	public void blockedUserLogin() {
		HttpSession ses = container.getSession(false);
		container.setParameter(getKey("userLogin"), "blocked");
		container.setParameter(getKey("userPass"), "mock");

		assertTrue(getKey("loginPath").equals(command.execute(container)));
		assertTrue(container.getAttribute(getKey("blackListMSG")) != null);
		assertTrue(ses.getAttribute(getKey("userOnline")) == null);
	}

	@Test
	public void adminLoggingTest() {
		HttpSession ses = container.getSession(false);
		container.setParameter(getKey("userLogin"), getKey("adminLogin"));
		container.setParameter(getKey("userPass"), "mock");

		assertTrue(getKey("adminPath").equals(command.execute(container)));
		assertTrue(ses.getAttribute(getKey("userOnline")) != null);
		assertTrue(ses.getAttribute(getKey("usersList")) != null);
		assertTrue(ses.getAttribute(getKey("productsList")) != null);
	}

	@Test
	public void userLoggingTest() {
		HttpSession ses = container.getSession(false);
		container.setParameter(getKey("userLogin"), "mock");
		container.setParameter(getKey("userPass"), "mock");

		assertTrue(getKey("contentPath").equals(command.execute(container)));
		assertTrue(ses.getAttribute(getKey("userOnline")) != null);
		assertTrue(ses.getAttribute(getKey("purchaseBucket")) != null);
		assertTrue(ses.getAttribute(getKey("productsList")) != null);
	}
}