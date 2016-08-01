package ua.kiral.project4.test.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ua.kiral.project4.test.command.CommandsProperties.*;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.kiral.project4.mock.dao.MockDAOFactory;
import ua.kiral.project4.mock.request.MockRequestContainer;
import ua.kiral.project4.mock.validator.MockValidator;
import ua.kiral.project4.model.command.impl.AddProductToBucketCommand;
import ua.kiral.project4.model.entity.Bucket;
import ua.kiral.project4.model.entity.Product;

public class AddProductToBucketTest {
	private static MockRequestContainer container;
	private static AddProductToBucketCommand command;

	@BeforeClass
	public static void beforeClass() {
		container = new MockRequestContainer(new HashMap<>(), new HashMap<>(), null);
		command = new AddProductToBucketCommand(MockDAOFactory.getInstance(), new MockValidator());
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
	public void blockedByAddingTest() {
		HttpSession ses = container.getSession();
		/*
		 * with this param mock will return user with blocked status, based on
		 * this, command must return mainPage path
		 */
		ses.setAttribute(getKey("userId"), 0);

		assertTrue(command.execute(container).equals(getKey("mainPage")));
	}

	@Test
	public void nullProductTest() {
		HttpSession ses = container.getSession();
		ses.setAttribute(getKey("userId"), 1);
		/*
		 * with this param product mock will return null, it means that in db no
		 * such product, or it was deleted, according to this command must
		 * return error path
		 */
		container.setParameter(getKey("productToPurchaseId"), "0");

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
		 * with this params after executing command bucket must contain exactly
		 * one product.
		 */

		assertTrue(command.execute(container).equals(getKey("contentPath")));

		testBucket = (Bucket) ses.getAttribute(getKey("purchaseBucket"));

		assertFalse(testBucket.isEmpty());
		assertTrue(testBucket.size() == 1);
	}

	@Test
	public void notEmptyBucketTest() {
		Bucket testBucket = new Bucket();
		HttpSession ses = container.getSession();
		testBucket.putProduct(new Product(-1, "mock", new BigDecimal("0.00")), 1);
		ses.setAttribute(getKey("userId"), 1);
		ses.setAttribute(getKey("purchaseBucket"), testBucket);
		container.setParameter(getKey("productToPurchaseId"), "1");

		/*
		 * same as emtyBucket test, but this time we previously have one product
		 * in bucket
		 */

		assertTrue(command.execute(container).equals(getKey("contentPath")));

		testBucket = (Bucket) ses.getAttribute(getKey("purchaseBucket"));

		assertFalse(testBucket.isEmpty());
		assertTrue(testBucket.size() == 2);
	}
}