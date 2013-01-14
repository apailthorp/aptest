package com.aptest.study;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.saucerest.SauceREST;

public class SpecialSauceWebDriver extends RemoteWebDriver implements LocalSSWebDriverInterface {
	
	private static String sauceUser;
	private static String sauceAccessKey;
	private WebDriver augmentedDriver;
	private WebDriver driver;
	private SauceREST sauceWebDriverREST;
	private Map<String, Object>mapSauceJob = new HashMap<String, Object>();
	private String sauceJobID;
	
	public WebDriver getSelenium(String inSauceUser, String inSauceAccessKey, 
			String browser, String platform, String browserVersion
			)
					throws InterruptedException, MalformedURLException {

		sauceUser = inSauceUser;
		sauceAccessKey = inSauceAccessKey;
		sauceWebDriverREST = getSauceREST(sauceUser, sauceAccessKey);

		DesiredCapabilities dc = new DesiredCapabilities();
		dc = getCapabilities(browser, browserVersion, platform);
		dc = getCapabilities(browser, null, platform);

		driver = new RemoteWebDriver(
				new URL(
						"http://" +  sauceUser + ":" + sauceAccessKey + "@ondemand.saucelabs.com:80/wd/hub"),
						dc);
		augmentedDriver = new Augmenter().augment(driver);
		
		sauceJobID = getJobID(driver);
		driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);

		return driver;

	}

	public DesiredCapabilities getCapabilities(String browser, String browserVersion, String platform){

		DesiredCapabilities capabilities = null;

		if (browser.equalsIgnoreCase("internet explorer") | (browser.equalsIgnoreCase("ie"))){
			capabilities = DesiredCapabilities.internetExplorer();
		}
		else if (browser.equalsIgnoreCase("chrome")){
			capabilities = DesiredCapabilities.chrome();

		}
		else if (browser.equalsIgnoreCase("firefox")){
			capabilities = DesiredCapabilities.firefox();
		}

		else if (browser.equalsIgnoreCase("safari")){
			capabilities = DesiredCapabilities.safari();
		}

		else if (browser.equalsIgnoreCase("iphone")){
			capabilities = DesiredCapabilities.iphone();
		}

		if (browserVersion != "" ){ 
			capabilities.setCapability("version", browserVersion);
		}

		capabilities.setCapability("platform", platform);

		return capabilities;

	}

	private SauceREST getSauceREST(String inSauceUserName, String inSauceAccessKey) {
		return new SauceREST(inSauceUserName, inSauceAccessKey);
	}
	
	public String getJobID(WebDriver inDriver) {
		return ((RemoteWebDriver)inDriver).getSessionId().toString();
	}

	public void setTestName(String name){
		// Using REST call, set the name of the test and the initial pass status on the Sauce Labs server
		mapSauceJob.put("name", name);
		try {
			sauceWebDriverREST.updateJobInfo(sauceJobID, mapSauceJob);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}            
	}
	
	public void setBuild(String inBuild){
		mapSauceJob.put("build", inBuild);
		try {
			sauceWebDriverREST.updateJobInfo(sauceJobID, mapSauceJob);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}            
	}
	
	public void setTestPass(){
		try {
			sauceWebDriverREST.jobPassed(sauceJobID);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setTestFail(){
		try {
			sauceWebDriverREST.jobFailed(sauceJobID);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public File getScreenshot(){
		return ((TakesScreenshot)augmentedDriver).
				getScreenshotAs(OutputType.FILE);
	}

	public void maximizeWindow() {
		return;
	}
}
