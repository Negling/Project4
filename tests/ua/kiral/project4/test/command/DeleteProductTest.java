package ua.kiral.project4.test.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ua.kiral.project4.test.command.CommandsProperties.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.kiral.project4.mock.dao.MockDAOFactory;
import ua.kiral.project4.mock.request.MockRequestContainer;
import ua.kiral.project4.mock.validator.MockValidator;
import ua.kiral.project4.model.command.impl.DeleteProductCommand;
import ua.kiral.project4.model.entity.Product;

public class DeleteProductTest {
	private static Product testInstance;
	private static DeleteProductCommand command;
	private static MockRequestContainer container;

	@BeforeClass
	public static void beforeClass() {
		List<Product> mockList = new ArrayList<>();
		Map<String, String> requestParams = new HashMap<>();
		
		testInstance = new Product(1, "mock", new BigDecimal(0.00));
		mockList.add(testInstance);
		requestParams.put(getKey("productCoreUpdateId"), testInstance.getId().toString());

		container = new MockRequestContainer(requestParams, new HashMap<>(), null);
		command = new DeleteProductCommand(MockDAOFactory.getInstance(), new MockValidator());
	}

	@AfterClass
	public static void afterClass() {
		testInstance = null;
		container = null;
		command = null;
	}

	@Before
	public void before() {
		List<Product> mockList = new ArrayList<>();
		mockList.add(testInstance);
		container.clearSessionAttributes();
		container.getSession().setAttribute(getKey("productsList"), mockList);
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
	public void deleteTest() {
		List<Product> products = (List<Product>) container.getSession().getAttribute(getKey("productsList"));
		/*
		 * at start we have products list with one product, after executing
		 * command it must be empty
		 */

		assertEquals(getKey("adminPath"), command.execute(container));
		assertTrue(products.isEmpty());
	}

	@Test
	public void emtyListTest() {
		HttpSession ses = container.getSession();
		ses.setAttribute("products", new ArrayList<>());
		List<Product> products = (List<Product>) ses.getAttribute(getKey("productsList"));

		/*
		 * here we have empty products list, after executing command it must be
		 * the same
		 */

		assertEquals(getKey("adminPath"), command.execute(container));
		assertTrue(products.isEmpty());
	}
}