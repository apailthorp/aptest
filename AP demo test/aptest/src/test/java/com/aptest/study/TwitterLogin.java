package com.aptest.study;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TwitterLogin {

	protected WebDriver driver;
	private WebElement targetElement;

	public TwitterLogin(WebDriver driver) {
		this.driver = driver;
	}

	public Boolean doLogin(String user, String password) throws InterruptedException {
		// Get logged in
		// element can move, Chromedriver has a problem: http://code.google.com/p/chromedriver/issues/detail?id=22
		targetElement = this.driver.findElement(By.cssSelector("input#signin-email.text-input"));
		// Use javascript fragment to scroll window?
		// JavascriptExecutor js = (JavascriptExecutor)this.driver;
		// js.executeScript(String.format("window.scrollTo(0, {0});", targetElement.getLocation().y));
		// Don't click, that gets the problem
		// targetElement.click();
		targetElement.sendKeys(user);
		targetElement = this.driver.findElement(By.cssSelector("input#signin-password.text-input"));
		// targetElement.click();
		targetElement.sendKeys(password);
		this.driver.findElement(By.cssSelector("td.flex-table-secondary>button.submit")).click(); 
		
		// TODO: Add additional validation for this return
        // fail if Captcha has been hit
		if (isCaptchaSeen()) return false;
		// fail if not finding the Tweets stream heading
		else if (!(this.driver.getPageSource().contains("Tweets"))) return false;
		else return true;
	}
	
	public Boolean isCaptchaSeen() {
		String pageSource = this.driver.findElement(By.cssSelector("div.message-inside span.message-text")).getText();
		if (pageSource.contains("are you human?")) return true;
		return false;
	}
}
