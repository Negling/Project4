package ua.kiral.project4.test.command;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ua.kiral.project4.test.command.CommandsProperties.*;

import ua.kiral.project4.mock.dao.MockDAOFactory;
import ua.kiral.project4.mock.request.MockRequestContainer;
import ua.kiral.project4.mock.validator.MockValidator;
import ua.kiral.project4.model.command.impl.PurchaseCommand;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Bucket;
import ua.kiral.project4.model.entity.Product;

public class PurchaseTest {
	private static MockRequestContainer container;
	private static PurchaseCommand command;
	private static Product testProduct;

	@BeforeClass
	public static void beforeClass() throws DAOException {
		container = new MockRequestContainer(new HashMap<>(), new HashMap<>(), new HashMap<>());
		command = new PurchaseCommand(MockDAOFactory.getInstance(), new MockValidator());
		testProduct = new Product(1, "mock", new BigDecimal("0.00"));
	}

	@AfterClass
	public static void afterClass() {
		container = null;
		command = null;
		testProduct = null;
	}

	@Before
	public void before() {
		container.clearContainerParams();
		container.clearSessionAttributes();
		container.clearContainerAttributes();
		container.getSession(false).setAttribute(getKey("purchaseBucket"), new Bucket());
	}

	@Test
	public void nullSessionTest() {
		container.setNullSession(true);

		assertEquals(getKey("errorPath"), command.execute(container));

		container.setNullSession(false);
	}

	@Test
	public void blockedByPurchasingTest() {
		HttpSession ses = container.getSession(false);
		ses.setAttribute(getKey("userId"), 0);

		assertTrue(command.execute(container).equals(getKey("mainPage")));
	}

	@Test
	public void orderCreationFailureTest() {
		HttpSession ses = container.getSession(false);
		ses.setAttribute(getKey("userId"), -1);
		Bucket bucket = (Bucket) ses.getAttribute(getKey("purchaseBucket"));	
		bucket.putProduct(testProduct, 1);

		assertTrue(command.execute(container).equals(getKey("errorPath")));
	}

	@Test
	public void emptyBucketTest() {
		HttpSession ses = container.getSession(false);
		ses.setAttribute(getKey("userId"), 1);

		assertTrue(command.execute(container).equals(getKey("contentPath")));
	}

	@Test
	public void successTest() {
		HttpSession ses = container.getSession(false);
		ses.setAttribute(getKey("userId"), 1);
		Bucket bucket = (Bucket) ses.getAttribute(getKey("purchaseBucket"));
		bucket.putProduct(testProduct, 1);

		assertTrue(command.execute(container).equals(getKey("contentPath")));
		assertTrue(bucket.isEmpty());
	}
}