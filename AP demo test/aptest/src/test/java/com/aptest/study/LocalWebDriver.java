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

public class LocalWebDriver extends RemoteWebDriver {

	private WebDriver driver;
	private WebDriver augmentedDriver;

	public WebDriver getSelenium(String inSauceUser, String inSauceAccessKey, 
			String browser, String platform, String browserVersion
			)
					throws InterruptedException, MalformedURLException {

		File file = new File("C:\\Program Files\\Selenium\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());

		DesiredCapabilities dc = new DesiredCapabilities();
		dc = getCapabilities(browser, browserVersion, platform);

		driver = new RemoteWebDriver(
				new URL(
						"http://localhost:4444/wd/hub"),
						dc);

		augmentedDriver = new Augmenter().augment(driver);

		//		sauceJobID = getJobID(driver);
		driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);

		return driver;

	}

	public static DesiredCapabilities getCapabilities(String browser, String browserVersion, String platform){

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

	public String getJobID(WebDriver inDriver) {
		return null;
	}

	public Boolean setTestName(String name){
		return true;
	}

	public void setBuild(String inBuild){
		return;
	}

	public void setTestPass(){
		return;
	}

	public void setTestFail(){
		return;
	}

	public File getScreenshot(){
		return ((TakesScreenshot)augmentedDriver).
				getScreenshotAs(OutputType.FILE);
	}
}
