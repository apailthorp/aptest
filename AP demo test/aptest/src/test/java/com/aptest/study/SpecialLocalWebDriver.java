package com.aptest.study;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SpecialLocalWebDriver extends LocalSSWebDriver<Object> {

	private WebDriver augmentedDriver;

	public WebDriver getSelenium(String inSauceUser, String inSauceAccessKey,
			String browser, String platform, String browserVersion)
			throws InterruptedException, MalformedURLException {

		File fileIe = new File(
				"C:\\Program Files\\Selenium\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", fileIe.getAbsolutePath());

		DesiredCapabilities dc = new DesiredCapabilities();
		dc = getCapabilities(browser, browserVersion, platform);

		augmentedDriver = new RemoteWebDriver(new URL(
				"http://localhost:4444/wd/hub"), dc);

		augmentedDriver = new Augmenter().augment(new RemoteWebDriver(new URL(
				"http://localhost:4444/wd/hub"), dc));

		augmentedDriver.manage().timeouts()
				.implicitlyWait(90, TimeUnit.SECONDS);

		return augmentedDriver;
	}

	public void setTestName(String name) {
		return;
	}

	public void setBuild(String inBuild) {
		return;
	}

	public void setTestPass() {
		return;
	}

	public void setTestFail() {
		return;
	}

	public File getScreenshot(){
		return ((TakesScreenshot)augmentedDriver).
				getScreenshotAs(OutputType.FILE);
	}

	public void maximizeWindow() {
		augmentedDriver.manage().window().maximize();
	}
}
