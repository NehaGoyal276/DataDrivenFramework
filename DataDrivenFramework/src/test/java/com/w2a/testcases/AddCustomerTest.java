package com.w2a.testcases;

import java.util.Hashtable;

import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.w2a.base.TestBase;
import com.w2a.utilities.TestUtil;

public class AddCustomerTest extends TestBase {

	@Test(dataProviderClass = TestUtil.class,
			dataProvider = "dp")

	public void addCustomerTest(
			Hashtable<String, String> data)
			throws InterruptedException {

		// check runmode
		if (!data.get("runmode").equalsIgnoreCase("Y")) {

			throw new SkipException(
					"Skipping the test case as Runmode is NO");
		}

		// click add customer button
		click("addCustBtn_CSS");

		// enter firstname
		type("firstName_CSS",
				data.get("firstname"));

		// enter lastname
		type("lastName_XPATH",
				data.get("lastname"));

		// enter postcode
		type("postCode_CSS",
				data.get("postcode"));

		// click add button
		click("addBtn_CSS");

		// wait for alert
		Alert alert = wait.until(
				ExpectedConditions.alertIsPresent());

		// validate alert text
		Assert.assertTrue(
				alert.getText()
				.contains(data.get("alerttext")));

		Thread.sleep(2000);

		alert.accept();

		log.debug("Customer added successfully");
	}
}