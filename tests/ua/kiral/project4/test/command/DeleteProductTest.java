package ua.kiral.project4.test.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ua.kiral.project4.test.command.CommandsProperties.*;

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
import ua.kiral.project4.model.dao.DAOFactory;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Product;

public class DeleteProductTest {
	private static Product testInstance;
	private static DeleteProductCommand command;
	private static MockRequestContainer container;

	@BeforeClass
	public static void beforeClass() throws DAOException {
		DAOFactory mockfactory = MockDAOFactory.getInstance();
		testInstance = mockfactory.getProductDAO().getProductByID(1);
		List<Product> mockList = new ArrayList<>();
		mockList.add(testInstance);

		Map<String, String> requestParams = new HashMap<String, String>() {
			{
				put(getKey("productCoreUpdateId"), testInstance.getId().toString());
			}
		};

		container = new MockRequestContainer(requestParams, new HashMap<>(), null);
		command = new DeleteProductCommand(mockfactory, new MockValidator());
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
		container.getSession(false).setAttribute(getKey("productsList"), mockList);
	}

	@Test
	public void deleteTest() {
		List<Product> products = (List<Product>) container.getSession(false).getAttribute(getKey("productsList"));
		assertEquals(getKey("adminPath"), command.execute(container));
		assertTrue(products.isEmpty());
	}

	@Test
	public void emtyListTest() {
		HttpSession ses = container.getSession(false);
		ses.setAttribute("products", new ArrayList<>());
		List<Product> products = (List<Product>) ses.getAttribute(getKey("productsList"));

		assertEquals(getKey("adminPath"), command.execute(container));
		assertTrue(products.isEmpty());
	}

	@Test
	public void nullSessionTest() {
		container.setNullSession(true);

		assertEquals(getKey("errorPath"), command.execute(container));

		container.setNullSession(false);
	}
}