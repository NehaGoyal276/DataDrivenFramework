package com.w2a.testcases;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.w2a.base.TestBase;

public class BankManagerLoginTest extends TestBase {

	@Test
	public void loginAsBankManager()
			throws InterruptedException, IOException {

		// intentionally failing verification
		verifyEquals("abc",
				"Way2Automation Banking App");

		log.debug("Inside Login Test");

		click("bmlBtn_CSS");

		Thread.sleep(3000);

		Assert.assertTrue(
				isElementPresent(
						By.cssSelector(
								OR.getProperty("addCustBtn_CSS"))),
				"Login not Successful");

		log.debug("Login successfully executed");
	}
}