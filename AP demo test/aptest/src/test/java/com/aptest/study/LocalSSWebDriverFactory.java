package com.aptest.study;

public class LocalSSWebDriverFactory {

	public static LocalSSWebDriverInterface getLocalSSWebDriver(String selection){
		if (selection.equalsIgnoreCase("local")) {
			return new LocalSSWebDriver();
		}
		else if (selection.equalsIgnoreCase("sauce")) {
			return new SpecialSauceWebDriver();
		}
		else throw new RuntimeException("bad LocalSSWebDriver type");
	}

}
