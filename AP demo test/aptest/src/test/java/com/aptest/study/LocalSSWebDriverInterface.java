package com.aptest.study;

import java.io.File;
import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;

public interface LocalSSWebDriverInterface {

	public abstract WebDriver getSelenium(String inSauceUser,
			String inSauceAccessKey, String browser, String platform,
			String browserVersion) throws InterruptedException,
			MalformedURLException;

	public abstract String getJobID(WebDriver inDriver);

	public abstract void setTestName(String name);

	public abstract void setBuild(String inBuild);

	public abstract void setTestPass();

	public abstract void setTestFail();

	public abstract File getScreenshot();
	
	public abstract void maximizeWindow();

}