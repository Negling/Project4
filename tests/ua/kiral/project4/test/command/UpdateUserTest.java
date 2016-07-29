package ua.kiral.project4.test.command;

import static org.junit.Assert.*;
import static ua.kiral.project4.test.command.CommandsProperties.getKey;

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
import ua.kiral.project4.model.command.impl.UpdateUserCommand;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.User;

public class UpdateUserTest {
	private static MockRequestContainer container;
	private static UpdateUserCommand command;

	@BeforeClass
	public static void beforeClass() throws DAOException {
		container = new MockRequestContainer(new HashMap<>(), new HashMap<>(), null);
		command = new UpdateUserCommand(MockDAOFactory.getInstance(), new MockValidator());
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

		assertEquals(getKey("errorPath"), command.execute(container));

		container.setNullSession(false);
	}
	
	@Test
	public void nullUsersListTest(){
		HttpSession ses = container.getSession(false);
		ses.setAttribute(getKey("usersList"), null);
		container.setParameter(getKey("userCoreUpdateLogin"), "mock");
		
		assertEquals(getKey("errorPath"), command.execute(container));
	}
	
	@Test
	public void nullUserTest(){
		HttpSession ses = container.getSession(false);
		List<User> users = new ArrayList<>();
		users.add(new User());
		ses.setAttribute(getKey("usersList"), users);
		container.setParameter(getKey("userCoreUpdateLogin"), "null");
		
		assertEquals(getKey("errorPath"), command.execute(container));
	}
	
	@Test
	public void succesTest() {
		HttpSession ses = container.getSession(false);
		List<User> users = new ArrayList<>();
		User user = new User(1, "", "", "mock", "", "", false, false);
		users.add(user);
		ses.setAttribute(getKey("usersList"), users);
		container.setParameter(getKey("userCoreUpdateLogin"), "mock");

		assertEquals(getKey("adminPath"), command.execute(container));

		users = (List<User>) ses.getAttribute(getKey("usersList"));

		assertTrue(users != null);
		assertFalse(users.isEmpty());
	}
}