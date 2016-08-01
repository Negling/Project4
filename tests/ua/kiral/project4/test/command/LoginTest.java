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

public class LoginTest {
	private static MockRequestContainer container;
	private static LoginCommand command;

	@BeforeClass
	public static void beforeClass() {
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
		HttpSession ses = container.getSession();
		ses.setAttribute(getKey("userOnline"), true);
		ses.setAttribute(getKey("userLogin"), "mock");

		/*
		 * if userOnline value is not empty, command must redirect user to
		 * content page
		 */

		assertTrue(getKey("contentPath").equals(command.execute(container)));
	}

	@Test
	public void loggedAdminTest() {
		HttpSession ses = container.getSession();
		ses.setAttribute(getKey("userOnline"), true);
		ses.setAttribute(getKey("userLogin"), getKey("adminLogin"));

		/*
		 * if userOnline value is not empty, and this user is admin, command
		 * must redirect him to admin page
		 */

		assertTrue(getKey("adminPath").equals(command.execute(container)));
	}

	@Test
	public void wrongPasswordTest() {
		HttpSession ses = container.getSession();
		container.setParameter(getKey("userLogin"), "mock");
		container.setParameter(getKey("userPass"), "qwerty4");

		/*
		 * mock will return user "mock" with "mock" password, according to this,
		 * mock not equal to qwerty4, and login must be failed, with appropriate
		 * messages in request container
		 */

		assertTrue(getKey("loginPath").equals(command.execute(container)));
		assertTrue(container.getAttribute(getKey("loginFailedMSD")) != null);
		assertTrue(ses.getAttribute(getKey("userOnline")) == null);
	}

	@Test
	public void blockedUserLogin() {
		HttpSession ses = container.getSession();
		container.setParameter(getKey("userLogin"), "blocked");
		container.setParameter(getKey("userPass"), "mock");
		
		/*
		 * mock will return user "mock" with "blocked" status, according to
		 * this, login must be failed, with appropriate messages in request
		 * container
		 */

		assertTrue(getKey("loginPath").equals(command.execute(container)));
		assertTrue(container.getAttribute(getKey("blackListMSG")) != null);
		assertTrue(ses.getAttribute(getKey("userOnline")) == null);
	}

	@Test
	public void adminLoggingTest() {
		HttpSession ses = container.getSession();
		container.setParameter(getKey("userLogin"), getKey("adminLogin"));
		container.setParameter(getKey("userPass"), "mock");

		/*
		 * if incoming params is correct, and user identified as admin,
		 * conforming data must be loaded in session container, also admin must
		 * be marked as already logged
		 */

		assertTrue(getKey("adminPath").equals(command.execute(container)));
		assertTrue(ses.getAttribute(getKey("userOnline")) != null);
		assertTrue(ses.getAttribute(getKey("usersList")) != null);
		assertTrue(ses.getAttribute(getKey("productsList")) != null);
	}

	@Test
	public void userLoggingTest() {
		HttpSession ses = container.getSession();
		container.setParameter(getKey("userLogin"), "mock");
		container.setParameter(getKey("userPass"), "mock");

		/*
		 * if incoming params is correct, and user identified, products data
		 * must be loaded in session container, also user must be marked as
		 * already logged
		 */

		assertTrue(getKey("contentPath").equals(command.execute(container)));
		assertTrue(ses.getAttribute(getKey("userOnline")) != null);
		assertTrue(ses.getAttribute(getKey("purchaseBucket")) != null);
		assertTrue(ses.getAttribute(getKey("productsList")) != null);
	}
}