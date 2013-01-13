package com.aptest.study;

import java.io.File;

import javax.mail.MessagingException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

//@Listeners({ com.aptest.study.TestMailSender.class})
public class CheckSeConfVenue {

	WebDriver selenium;

	static String myOs;

	static final boolean logToConsole = true;
	static boolean allPassing = true;

	// Extended RemoteWebDriver includes some utility methods
	LocalWebDriver localSSWebDriver;

	// Utility class for sending email
	TestMailer testMailer = new TestMailer();

	// Available for use after test method has completed
	public String sauceSessionName;
	public String testName;
	public String build;
	public String emailName;
	public String emailSendFlag;
	public String url;
	public String targetOs;
	public String targetBrowser;
	public String targetPlatform;
	public String targetBrowserVersion;
	public String targetDescription;
	public String fileScreenCapture;
	Boolean testPassStatus = false;

	@Parameters({"testName", "build", "notifyEmail"})
	@BeforeSuite(alwaysRun = true)
	public void startUp(String inTestName, String inBuild, String inNotifyEmail)
	{
		// set class level values for later use
		testName = inTestName;
		build = inBuild;
		emailName = inNotifyEmail;

		// Output the OS, name of test, and build running
		myOs = System.getProperty("os.name");
		Reporter.log("Automation host OS is " + myOs +", Test is " + inTestName + " Build is " + inBuild, true);

		// Generate the notify message
		/* create email from utility class with address, subject line */
		testMailer.makeEmail(inNotifyEmail, "Test: " + inTestName + " Build: " + inBuild
				+ " suite results", "");
	}

	@Parameters({ "sauceUsername", "sauceAccessKey", "testName", "build",
		"notifyEmail", "sendEmail", "url", 
		"browser", "platform", "browserVersion" })
	@BeforeTest(alwaysRun = true)
	public void setUp(String inSauceUserName, String inSauceAccessKey,
			String inTestName, String inBuild, String inNotifyEmail,
			String inSendEmail, String inUrl, String inBrowser, String inPlatform,
			String inBrowserVersion) throws Exception {

		// Move local input strings to class level variables
		testName = inTestName;
		build = inBuild;
		emailName = inNotifyEmail;
		url = inUrl;
		targetBrowser = inBrowser;
		targetPlatform = inPlatform;
		targetBrowserVersion = inBrowserVersion;
		targetDescription = targetBrowser + " " + targetBrowserVersion + " on " + targetPlatform;
		fileScreenCapture = "Screenshot_" + targetBrowser + "_"
				+ targetBrowserVersion + "_" + targetPlatform + ".PNG";

		// Get the RemoteWebDriver with the custom extensions
		localSSWebDriver = new LocalWebDriver();
		// Use it to get a selenium to operate against
		selenium = localSSWebDriver.getSelenium(inSauceUserName,
				inSauceAccessKey, inBrowser, inPlatform, inBrowserVersion);

		// Set the name of the test and the build on the Sauce Labs server
		localSSWebDriver.setTestName(testName);
		localSSWebDriver.setBuild(build);
	}

	@Test
	public void doTest() throws InterruptedException {

		// Output some progress, compose email message start
		Reporter.log("Build: " + build, logToConsole);
		String emailMessage = "starting " + targetDescription + "\n";
		emailMessage += "Build: " + build + "\n " + targetDescription 
				+ "\n URL: " + url 
				+ "\n";
		testMailer.addText(emailMessage);

		// Open the web site under test
		selenium.get(url);
		// Grab some text containing element
		WebElement localSSElement = selenium.findElements(By.cssSelector("p:nth-child(4)")).get(0);

		testMailer.addText("Found text:");
		Reporter.log("Found text", logToConsole);
		String someText = localSSElement.getText();
		testMailer.addText(someText + " \n");
		Reporter.log(someText, logToConsole);
		Assert.assertTrue((someText.contentEquals("We will provide a special registration link" +
				" for booking in the next couple of weeks.")), 
				"Message changed\n");
		
		// Testing is done
		testPassStatus = true;
		localSSWebDriver.setTestPass();

	}

	@AfterTest(alwaysRun = true)
	public void cleanupTest() {
		// Maximize the screen and grab a screen capture
		selenium.manage().window().maximize();
		File screenshot = localSSWebDriver.getScreenshot();

		// Attach the screen capture to the email
		testMailer.attachFile(screenshot,
				fileScreenCapture);
		testMailer.addText(fileScreenCapture + " attached \n");

		if (!(testPassStatus)) {
			localSSWebDriver.setTestFail();
			testMailer.addText("Failure recorded for " + targetDescription + "\n");
		}
		else testMailer.addText(targetDescription + " finished successfully \n");
		allPassing = (allPassing & testPassStatus);
		selenium.quit();
	}

	@AfterSuite(alwaysRun = true)
	public void cleanupSuite() throws MessagingException {
		Reporter.log("ending suite", logToConsole);
		testMailer.addText("ending suite");
		allPassing = (allPassing & testPassStatus);
		if (!(allPassing)) {
			testMailer.setSubject("Failure in " + testMailer.getSubject());
		}
		else testMailer.setSubject("Passing in " + testMailer.getSubject());

		// email will be sent by a listener
	}

}