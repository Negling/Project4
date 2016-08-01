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

public class ShowOrdersTest {
	private static MockRequestContainer container;
	private static ShowOrdersCommand command;

	@BeforeClass
	public static void beforeClass() {
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

		/*
		 * with no session during this command executing must fail
		 */
		assertEquals(getKey("errorPath"), command.execute(container));

		container.setNullSession(false);
	}

	@Test
	public void blockedDuringConsiderTest() {
		HttpSession ses = container.getSession();
		ses.setAttribute(getKey("userId"), 0);

		/*
		 * with this param mock will return user with blocked status, based on
		 * this, command must return mainPage path
		 */

		assertTrue(command.execute(container).equals(getKey("mainPage")));
	}

	@Test
	public void emtyHistoryTest() {
		HttpSession ses = container.getSession();
		ses.setAttribute(getKey("userId"), -1);

		/*
		 * with "-1" userId param mock will return null orders list, msg and
		 * orders path expected
		 */

		assertTrue(command.execute(container).equals(getKey("ordersPath")));
		assertTrue(container.getAttribute(getKey("emptyOrderList")) != null);
	}

	@Test
	public void successTest() {
		HttpSession ses = container.getSession();
		ses.setAttribute(getKey("userId"), 1);

		/*
		 * with "1" userid param mock will return notEmpty order list, orders,
		 * orderDetails, products list expected.
		 */

		assertTrue(command.execute(container).equals(getKey("ordersPath")));
		assertTrue(ses.getAttribute(getKey("ordersList")) != null);
		assertTrue(ses.getAttribute(getKey("orderDetailsList")) != null);
		assertTrue(ses.getAttribute(getKey("productDetailsList")) != null);
	}
}