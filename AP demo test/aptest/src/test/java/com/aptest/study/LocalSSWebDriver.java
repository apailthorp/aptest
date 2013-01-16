package com.aptest.study;

import java.io.File;
import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public abstract class LocalSSWebDriver<augmentedDriver> {

	public static DesiredCapabilities getCapabilities(String browser, 
			String browserVersion, String platform){

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

	public static String getJobID(WebDriver inDriver) {
		return ((RemoteWebDriver)inDriver).getSessionId().toString();
	}

	public abstract WebDriver getSelenium(String inSauceUser,
			String inSauceAccessKey, String browser, String platform,
			String browserVersion) throws InterruptedException,
			MalformedURLException;

	public abstract void setTestName(String name);

	public abstract void setBuild(String inBuild);

	public abstract void setTestPass();

	public abstract void setTestFail();

	public abstract File getScreenshot();
	
	public abstract void maximizeWindow();

}