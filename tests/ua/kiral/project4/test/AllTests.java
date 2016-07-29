package ua.kiral.project4.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ua.kiral.project4.test.command.AddProductTest;
import ua.kiral.project4.test.command.BlackListManagmentTest;
import ua.kiral.project4.test.command.DeleteProductTest;
import ua.kiral.project4.test.command.LocaleChangeTest;
import ua.kiral.project4.test.command.LoginTest;
import ua.kiral.project4.test.command.PurchaseTest;
import ua.kiral.project4.test.command.RegistrationTest;
import ua.kiral.project4.test.command.RemoveFromBucketTest;
import ua.kiral.project4.test.command.ShowOrdersTest;
import ua.kiral.project4.test.command.UpdateProductTest;
import ua.kiral.project4.test.command.UpdateUserTest;
import ua.kiral.project4.test.command.validator.DataValidatorTest;

@RunWith(Suite.class)
@SuiteClasses({ AddProductTest.class, DeleteProductTest.class, LocaleChangeTest.class, LoginTest.class,
		PurchaseTest.class, RegistrationTest.class, ShowOrdersTest.class, UpdateProductTest.class, UpdateUserTest.class,
		AddProductTest.class, RemoveFromBucketTest.class, BlackListManagmentTest.class, DataValidatorTest.class })
public class AllTests {

}
