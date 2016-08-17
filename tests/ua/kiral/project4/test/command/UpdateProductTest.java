package ua.kiral.project4.test.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
import ua.kiral.project4.model.command.impl.UpdateProductCommand;
import ua.kiral.project4.model.entity.Product;

public class UpdateProductTest {	
	private static MockRequestContainer container;
	private static UpdateProductCommand command;
	
	@BeforeClass
	public static void beforeClass() {
		container = new MockRequestContainer(new HashMap<>(), new HashMap<>(), null);
		command = new UpdateProductCommand(MockDAOFactory.getInstance(), new MockValidator());
	}
	
	@AfterClass
	public static void afterClass() {
		container = null;
		command = null;
	}
	
	@Before
	public void before(){
		container.clearContainerParams();
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
	public void nullProductsListTest(){
		container.setParameter(getKey("productCoreUpdateId"), "1");
		
		/*
		 * without products list in session container, error path expected
		 */
		assertEquals(getKey("errorPath"), command.execute(container));
	}
	
	@Test
	public void nullProductTest() {
		HttpSession ses = container.getSession();
		List<Product> products = new ArrayList<>();

		products.add(new Product());
		ses.setAttribute(getKey("productsList"), products);
		container.setParameter(getKey("productCoreUpdateId"), "0");

		/*
		 * with "0" productId param DAo mock will return null walue, error path
		 * expected
		 */

		assertEquals(getKey("errorPath"), command.execute(container));
	}
	
	@Test
	public void succesTest() {
		HttpSession ses = container.getSession();
		List<Product> products = new ArrayList<>();

		products.add(new Product(1, "mock", new BigDecimal("0.00"), false));
		ses.setAttribute(getKey("productsList"), products);
		container.setParameter(getKey("productCoreUpdateId"), "1");
		container.setParameter(getKey("productToUpdatePrice"), "0.00");

		/*
		 * with "1" productId param DAO mock will return correct product
		 * instance, succesfull update expected.
		 */

		assertEquals(getKey("adminPath"), command.execute(container));

		products = (List<Product>) ses.getAttribute(getKey("productsList"));

		assertTrue(products != null);
		assertFalse(products.isEmpty());
	}
}
