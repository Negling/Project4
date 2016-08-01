package ua.kiral.project4.test.command;

import static org.junit.Assert.*;
import static ua.kiral.project4.test.command.CommandsProperties.getKey;

import java.math.BigDecimal;
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
import ua.kiral.project4.model.command.impl.OrderManagmentCommand;
import ua.kiral.project4.model.entity.Order;

public class OrderManagmentTest {
	private static MockRequestContainer container;
	private static OrderManagmentCommand command;

	@BeforeClass
	public static void beforeClass() {
		container = new MockRequestContainer(new HashMap<>(), new HashMap<>(), new HashMap<>());
		command = new OrderManagmentCommand(MockDAOFactory.getInstance(), new MockValidator());
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
		container.clearContainerAttributes();
	}

	@Test
	public void nullSessionTest() {
		container.setNullSession(true);
		container.setParameter(getKey("confirm"), "-1");
		/*
		 * with no session during this command executing must fail
		 */
		assertEquals(getKey("errorPath"), command.execute(container));
	
		container.setNullSession(false);
	}

	@Test
	public void failureTest() {
		List<Order> orders = new ArrayList<>();
		Order order = new Order(-1, null, null, "", new BigDecimal("0.00"));
		HttpSession ses = container.getSession();
		orders.add(order);
		container.setParameter(getKey("confirm"), "-1");
		ses.setAttribute(getKey("ordersList"), orders);
		
		/*
		 * with "-1" orderId param DAO mock will fail to update, so, command must be failed too
		 */

		assertEquals(getKey("errorPath"), command.execute(container));
		assertEquals(orders, ses.getAttribute(getKey("ordersList")));

	}

	@Test
	public void successTest() {
		List<Order> orders = new ArrayList<>();
		Order order = new Order(1, null, null, "", new BigDecimal("0.00"));
		HttpSession ses = container.getSession();
		orders.add(order);
		container.setParameter(getKey("confirm"), "1");
		ses.setAttribute(getKey("ordersList"), orders);

		/*
		 * with "1" orderId param DAO mock will succesful update order, and order
		 * must be removed from session container
		 */

		assertEquals(getKey("adminPath"), command.execute(container));
		assertEquals(orders, ses.getAttribute(getKey("ordersList")));
		assertTrue(orders.isEmpty());
	}
}