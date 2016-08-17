package ua.kiral.project4.test.command;

import static org.junit.Assert.*;
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
import ua.kiral.project4.model.command.impl.AddProductCommand;
import ua.kiral.project4.model.entity.Product;

public class AddProductTest {
	private static Product testProduct;
	private static AddProductCommand command;
	private static MockRequestContainer container;

	@BeforeClass
	public static void beforeClass() {
		testProduct = new Product(-1, "mock", new BigDecimal("0.00"), false);

		/*
		 * expected request values
		 */
		Map<String, String> requestParams = new HashMap<String, String>() {
			{
				put(getKey("productToUpdatePrice"), testProduct.getPrice().toString());
				put(getKey("productToUpdateName"), testProduct.getName());
				put(getKey("productCoreUpdateId"), testProduct.getId().toString());
			}
		};

		container = new MockRequestContainer(requestParams, new HashMap<>(), null);
		command = new AddProductCommand(MockDAOFactory.getInstance(), new MockValidator());
	}

	@AfterClass
	public static void afterClass() {
		container = null;
		command = null;
		testProduct = null;
	}

	@Before
	public void before() {
		container.clearSessionAttributes();
		container.getSession().setAttribute(getKey("productsList"), new ArrayList<>());
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
	public void pathTest() {
		assertEquals(getKey("adminPath"), command.execute(container));
	}

	@Test
	public void addTest() {
		HttpSession ses = container.getSession();
		List<Product> products = (List<Product>) ses.getAttribute(getKey("productsList"));

		/*
		 * after executing comand we expect that products list in session will
		 * contain new one
		 */
		assertEquals(getKey("adminPath"), command.execute(container));
		assertTrue(products.size() == 1);
	}

	@Test
	public void correctParamReadTest() {
		assertEquals(getKey("adminPath"), command.execute(container));

		Product actual = ((List<Product>) container.getSession().getAttribute(getKey("productsList"))).get(0);

		/*
		 * assesrt that input params for new products was read successful
		 */
		assertFalse(actual == null);
		assertEquals(actual.getId(), testProduct.getId());
		assertEquals(actual.getName(), testProduct.getName());
		assertEquals(actual.getPrice().toString(), testProduct.getPrice().toString());
	}
}