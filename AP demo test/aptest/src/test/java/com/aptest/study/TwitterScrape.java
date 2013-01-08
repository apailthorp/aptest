package com.aptest.study;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

//@Listeners({ com.aptest.study.TestMailSender.class})
public class TwitterScrape {

	WebDriver selenium;

	static String myOs;
//	Logger log = Logger.getLogger(this.getClass());

	static final boolean logToConsole = true;

	public String sauceUserName;
	String sauceAccessKey;

	SpecialSauceWebDriver localSSWebDriver;

	TestMailer testMailer = new TestMailer();
	public String sauceSessionName;
	public String testName;
	public String build;
	public String emailName;
	public String email;
	public String url;
	public String userName;
	public String userPassword;
	public String targetOs;
	public String targetBrowser;
	public String targetPlatform;
	public String targetBrowserVersion;
	public String fileScreenCapture;
	public String targetMessageString;
	Boolean testPassStatus = false;

	@Parameters({ "sauceUsername", "sauceAccessKey", "testName", "build",
		"notifyEmail", "sendEmail", "url", "userName", "userPassword" })
	@BeforeSuite(alwaysRun = true)
	public void startUp(String inSauceUserName, String inSauceAccessKey,
		String inTestName, String inBuild, String inNotifyEmail,
		String inSendEmail, String inUrl, 
		String inUserName, String inUserPassword) throws Exception {

	// Move local input strings to class level variables
	sauceUserName = inSauceUserName;
	sauceAccessKey = inSauceAccessKey;
	testName = inTestName;
	build = inBuild;
	emailName = inNotifyEmail;
	email = inSendEmail;
	url = inUrl;
	userName = inUserName;
	userPassword = inUserPassword;
	
	// Output the OS the test is running on
	myOs = System.getProperty("os.name");
	Reporter.log("Automation host OS is " + myOs +", Build is " + build, true);

	// Generate the message
	/* create email from utility class with address, subject line, message content */
	testMailer.makeEmail(emailName, "Build: " + build
			+ " suite results", "");

	}
	
	@Parameters({ "sauceUsername", "sauceAccessKey", "testName", "build",
			"notifyEmail", "sendEmail", "url", "userName", "userPassword",
			"browser", "platform", "browserVersion" })
	@BeforeTest(alwaysRun = true)
	public void setUp(String inSauceUserName, String inSauceAccessKey,
			String inTestName, String inBuild, String inNotifyEmail,
			String inSendEmail, String inUrl, String inUserName,
			String inUserPassword, String inBrowser, String inPlatform,
			String inBrowserVersion) throws Exception {

		// Move local input strings to class level variables
		sauceUserName = inSauceUserName;
		sauceAccessKey = inSauceAccessKey;
		testName = inTestName;
		build = inBuild;
		emailName = inNotifyEmail;
		email = inSendEmail;
		url = inUrl;
		userName = inUserName;
		userPassword = inUserPassword;
		targetBrowser = inBrowser;
		targetPlatform = inPlatform;
		targetBrowserVersion = inBrowserVersion;
		fileScreenCapture = "Screenshot_" + targetBrowser + "_"
				+ targetBrowserVersion + "_" + targetPlatform + ".PNG";
		targetMessageString = targetBrowser + " " + targetBrowserVersion + " on " + targetPlatform;

		localSSWebDriver = new SpecialSauceWebDriver();
		selenium = localSSWebDriver.getSelenium(inSauceUserName,
				inSauceAccessKey, inBrowser, inPlatform, inBrowserVersion);

		// Set the name of the test and the initial pass status on the Sauce
		// Labs server
		localSSWebDriver.setTestName(testName);
		localSSWebDriver.setBuild(build);
	}

	@Test
	public void doTest() throws InterruptedException {

		Reporter.log("Build: " + build, logToConsole);
		String emailMessage = "starting " + targetMessageString + "\n";

		selenium.get(url);

		// Get logged in, test for reCaptcha failure
		TwitterLogin screenLogin = new TwitterLogin(selenium);
		Boolean loginTry = screenLogin.doLogin(userName, userPassword);
		if (!(loginTry)) {
			Boolean captchaFound = screenLogin.isCaptchaSeen();
			if (captchaFound) {
				testMailer.addText(targetMessageString + ": Failure during login, reCaptcha found \n");
				Reporter.log("Failure during login, reCaptcha found", logToConsole);
			}
		}
		Assert.assertTrue(loginTry, "Failure during login, reCaptcha found");

		TwitterStreams someStreams = new TwitterStreams(selenium);
		emailMessage += "Build: " + build + "\n " + targetMessageString 
				+ "\n URL: " + url + "\n Login: " + userName
				+ "\n";

		ArrayList<Map<String, String>> someStreamMaps = new ArrayList<Map<String, String>>();
		Assert.assertTrue(someStreams.getStreamMapList(someStreamMaps));

		Reporter.log("\n", logToConsole);
//		emailMessage += "\n";
		for (Map<String, String> someMap : someStreamMaps) {
			// Reverse the order of the map
			for (int i = someMap.keySet().size() - 1; i >= 0; i--) {
				String keyString = (String) someMap.keySet().toArray()[i];
				String mapValue = (String) someMap.get(keyString);
				Reporter.log(keyString + " : " + mapValue, logToConsole);
			}
			Reporter.log("\n", logToConsole);
		}
		
		testMailer.addText(emailMessage);

		testPassStatus = true;
		localSSWebDriver.setTestPass();

	}

	@AfterTest(alwaysRun = true)
	public void cleanupTest() {
		// Grab a screen capture
		File screenshot = localSSWebDriver.getScreenshot();

		// Attach the screen capture to the email
		testMailer.attachFile(screenshot,
				fileScreenCapture);
		testMailer.addText(fileScreenCapture + " attached \n");

		if (!(testPassStatus))
			localSSWebDriver.setTestFail();
		else testMailer.addText(targetMessageString + " finished successfully \n");

		selenium.quit();
	}

	@AfterSuite(alwaysRun = true)
	public void cleanupSuite() {
		Reporter.log("ending suite", logToConsole);
		testMailer.addText("ending suite");
	}

}
