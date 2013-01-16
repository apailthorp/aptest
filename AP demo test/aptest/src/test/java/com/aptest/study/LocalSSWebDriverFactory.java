package com.aptest.study;

public class LocalSSWebDriverFactory {

	public static LocalSSWebDriver<?> getLocalSSWebDriver(String selection){
		if (selection.equalsIgnoreCase("local")) {
			return new SpecialLocalWebDriver();
		}
		else if (selection.equalsIgnoreCase("sauce")) {
			return new SpecialSauceWebDriver();
		}
		else throw new RuntimeException("bad LocalSSWebDriver type");
	}
}
