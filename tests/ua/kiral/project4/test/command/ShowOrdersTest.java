package ua.kiral.project4.test.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ua.kiral.project4.test.command.CommandsProperties.getKey;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.kiral.project4.mock.dao.MockDAOFactory;
import ua.kiral.project4.mock.request.MockRequestContainer;
import ua.kiral.project4.mock.validator.MockValidator;
import ua.kiral.project4.model.command.impl.ShowOrdersCommand;
import ua.kiral.project4.model.dao.exceptions.DAOException;

public class ShowOrdersTest {
	private static MockRequestContainer container;
	private static ShowOrdersCommand command;

	@BeforeClass
	public static void beforeClass() throws DAOException {
		container = new MockRequestContainer(null, new HashMap<>(), new HashMap<>());
		command = new ShowOrdersCommand(MockDAOFactory.getInstance(), new MockValidator());
	}

	@AfterClass
	public static void afterClass() {
		container = null;
		command = null;
	}

	@Before
	public void before() {
		container.clearContainerAttributes();
		container.clearSessionAttributes();
	}

	@Test
	public void nullSessionTest() {
		container.setNullSession(true);

		assertEquals(getKey("errorPath"), command.execute(container));

		container.setNullSession(false);
	}

	@Test
	public void blockedByConsideringTest() {
		HttpSession ses = container.getSession(false);
		ses.setAttribute(getKey("userId"), 0);

		assertTrue(command.execute(container).equals(getKey("mainPage")));
	}

	@Test
	public void emtyHistoryTest() {
		HttpSession ses = container.getSession(false);
		ses.setAttribute(getKey("userId"), -1);

		assertTrue(command.execute(container).equals(getKey("ordersPath")));
		assertTrue(container.getAttribute(getKey("emptyOrderList")) != null);
	}

	@Test
	public void successTest() {
		HttpSession ses = container.getSession(false);
		ses.setAttribute(getKey("userId"), 1);

		assertTrue(command.execute(container).equals(getKey("ordersPath")));
		assertTrue(ses.getAttribute(getKey("ordersList")) != null);
		assertTrue(ses.getAttribute(getKey("orderDetailsList")) != null);
	}
}