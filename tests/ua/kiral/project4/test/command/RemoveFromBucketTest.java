package ua.kiral.project4.test.command;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ua.kiral.project4.test.command.CommandsProperties.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.kiral.project4.mock.dao.MockDAOFactory;
import ua.kiral.project4.mock.request.MockRequestContainer;
import ua.kiral.project4.mock.validator.MockValidator;
import ua.kiral.project4.model.command.impl.RemoveFromBucketCommand;
import ua.kiral.project4.model.entity.Bucket;
import ua.kiral.project4.model.entity.Product;

public class RemoveFromBucketTest {
	private static MockRequestContainer container;
	private static RemoveFromBucketCommand command;

	@BeforeClass
	public static void beforeClass() {
		container = new MockRequestContainer(new HashMap<>(), new HashMap<>(), null);
		command = new RemoveFromBucketCommand(MockDAOFactory.getInstance(), new MockValidator());
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

		/*
		 * with no session during this command executing must fail
		 */
		assertEquals(getKey("errorPath"), command.execute(container));

		container.setNullSession(false);
	}

	@Test
	public void blockedByRemovingTest() {
		HttpSession ses = container.getSession();
		ses.setAttribute(getKey("userId"), 0);

		/*
		 * if user status is blocked, command must return main page path, no
		 * other actions will be performed
		 */

		assertTrue(command.execute(container).equals(getKey("mainPage")));
	}

	@Test
	public void nullProductTest() {
		HttpSession ses = container.getSession();
		ses.setAttribute(getKey("userId"), 1);
		container.setParameter(getKey("productToPurchaseId"), "0");

		/*
		 * if product doesn't exist in DB, error path must be returned
		 */

		assertTrue(command.execute(container).equals(getKey("errorPath")));
	}

	@Test
	public void nullBucketTest() {
		HttpSession ses = container.getSession();
		ses.setAttribute(getKey("userId"), 1);
		container.setParameter(getKey("productToPurchaseId"), "1");
		
		/*
		 * if session contains no bucket value, error page must be returned
		 */

		assertTrue(command.execute(container).equals(getKey("errorPath")));
	}

	@Test
	public void emptyBucketTest() {
		Bucket testBucket = new Bucket();
		HttpSession ses = container.getSession();
		ses.setAttribute(getKey("userId"), 1);
		ses.setAttribute(getKey("purchaseBucket"), testBucket);
		container.setParameter(getKey("productToPurchaseId"), "1");
		
		/*
		 * if bucket is empty, no actions must be performed
		 */

		assertTrue(command.execute(container).equals(getKey("contentPath")));

		testBucket = (Bucket) ses.getAttribute(getKey("purchaseBucket"));

		assertTrue(testBucket.isEmpty());
	}

	@Test
	public void notEmptyBucketTest() {
		Bucket testBucket = new Bucket();
		HttpSession ses = container.getSession();
		ses.setAttribute(getKey("userId"), 1);
		ses.setAttribute(getKey("purchaseBucket"), testBucket);
		container.setParameter(getKey("productToPurchaseId"), "1");
		testBucket.putProduct(new Product(1, "mock", new BigDecimal("0.00"), false), 1);

		/*
		 * if bucket contains one element, after executing command content path
		 * expected, and bucket must be empty
		 */

		assertTrue(command.execute(container).equals(getKey("contentPath")));

		testBucket = (Bucket) ses.getAttribute(getKey("purchaseBucket"));

		assertTrue(testBucket.isEmpty());
	}

	@Test
	public void differentProductsTest() {
		Bucket testBucket = new Bucket();
		HttpSession ses = container.getSession();
		ses.setAttribute(getKey("userId"), 1);
		ses.setAttribute(getKey("purchaseBucket"), testBucket);
		container.setParameter(getKey("productToPurchaseId"), "1");
		testBucket.putProduct(new Product(1, "mock", new BigDecimal("0.00"), false), 1);
		testBucket.putProduct(new Product(2, "mock", new BigDecimal("0.00"), false), 1);

		/*
		 * bucket contains two different products, after removing command, one
		 * product in the bucket expected.
		 */

		assertTrue(command.execute(container).equals(getKey("contentPath")));

		testBucket = (Bucket) ses.getAttribute(getKey("purchaseBucket"));

		assertFalse(testBucket.isEmpty());
		assertTrue(testBucket.size() == 1);
	}
}