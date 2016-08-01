package ua.kiral.project4.test.command;

import static org.junit.Assert.*;
import static ua.kiral.project4.test.command.CommandsProperties.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.kiral.project4.mock.dao.MockDAOFactory;
import ua.kiral.project4.mock.request.MockRequestContainer;
import ua.kiral.project4.mock.validator.MockValidator;
import ua.kiral.project4.model.command.impl.BlackListManagmentCommand;
import ua.kiral.project4.model.entity.User;

public class BlackListManagmentTest {
	private static MockRequestContainer container;
	private static BlackListManagmentCommand command;

	@BeforeClass
	public static void beforeClass() {
		container = new MockRequestContainer(new HashMap<>(), new HashMap<>(), null);
		command = new BlackListManagmentCommand(MockDAOFactory.getInstance(), new MockValidator());
	}

	@AfterClass
	public static void afterClass() {
		container = null;
		command = null;
	}

	@Before
	public void before() {
		container.clearContainerParams();
		container.clearSessionAttributes();
	}

	@Test
	public void nullSessionTest() {
		container.setNullSession(true);
		container.setParameter(getKey("unbanLogin"), "mock");

		/*
		 * with no session during this command executing must fail
		 */

		assertEquals(getKey("errorPath"), command.execute(container));

		container.setNullSession(false);
	}

	@Test
	public void testBan() {
		HttpSession ses = container.getSession();
		List<User> users = new ArrayList<>();
		User mock = new User();
		mock.setLogin("mock");
		mock.setBlocked(false);
		users.add(mock);
		container.setParameter(getKey("banLogin"), "mock");
		ses.setAttribute(getKey("usersList"), users);

		/*
		 * here we got users list in session, with one user which is currently
		 * unblocked, after executing command he must appear with blocked status
		 */
		assertEquals(getKey("adminPath"), command.execute(container));
		assertTrue(mock.getBlocked());
		assertTrue(users.size() == 1);

	}

	@Test
	public void testUnban() {
		HttpSession ses = container.getSession();
		List<User> users = new ArrayList<>();
		User mock = new User();
		mock.setLogin("mock");
		mock.setBlocked(true);
		users.add(mock);
		container.setParameter(getKey("unbanLogin"), "mock");
		ses.setAttribute(getKey("usersList"), users);

		/*
		 * here we got users list in session, with one user which is currently
		 * blocked, after executing command he must appear with unblocked status
		 */

		assertEquals(getKey("adminPath"), command.execute(container));
		assertFalse(mock.getBlocked());
		assertTrue(users.size() == 1);
	}
}