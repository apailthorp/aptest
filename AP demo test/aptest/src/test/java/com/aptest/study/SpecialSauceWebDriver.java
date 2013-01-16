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

public class SpecialSauceWebDriver
	extends LocalSSWebDriver<Object> {
	
	private static String sauceUser;
	private static String sauceAccessKey;
	private WebDriver augmentedDriver;
	private SauceREST sauceWebDriverREST;
	private Map<String, Object>mapSauceJob = new HashMap<String, Object>();
	private String sauceJobID;
	private String urlRemoteWebDriver = "@ondemand.saucelabs.com:80/wd/hub";
	
	public WebDriver getSelenium(String inSauceUser, String inSauceAccessKey, 
			String browser, String platform, String browserVersion
			)
					throws InterruptedException, MalformedURLException {

		sauceUser = inSauceUser;
		sauceAccessKey = inSauceAccessKey;
		sauceWebDriverREST = getSauceREST(sauceUser, sauceAccessKey);

		DesiredCapabilities dc = new DesiredCapabilities();
		dc = getCapabilities(browser, browserVersion, platform);
		augmentedDriver = new Augmenter().augment( new RemoteWebDriver(
				new URL("http://" +  sauceUser + ":" + sauceAccessKey + urlRemoteWebDriver),dc));

		sauceJobID = getJobID(augmentedDriver);
		augmentedDriver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);

		return augmentedDriver;


	}

	private SauceREST getSauceREST(String inSauceUserName, String inSauceAccessKey) {
		return new SauceREST(inSauceUserName, inSauceAccessKey);
	}
	
	public void setTestName(String name){
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
