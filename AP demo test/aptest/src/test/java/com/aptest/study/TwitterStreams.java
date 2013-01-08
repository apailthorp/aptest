package com.aptest.study;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TwitterStreams {

	protected WebDriver driver;
	private List<WebElement> targetElements;

	public TwitterStreams(WebDriver driver) {
		this.driver = driver;
	}

	public Boolean getStreamMapList(ArrayList<Map<String, String>> someStreamMaps) {

		targetElements = this.driver.findElements(By.cssSelector("div.stream-item"));

		for (WebElement someElement : targetElements) {
			String[] someStreamStrings = someElement.getText().split("\n");
			HashMap<String, String> someMap = new HashMap<String, String>();
			for (int i = 0; i < someStreamStrings.length - 1; i++) {
				String someStreamString = someStreamStrings[i];
				if (!(someStreamString.equalsIgnoreCase("Expand"))) {
					someMap.put("Line" + String.valueOf(i), someStreamString.replace(String.valueOf((char)8207),""));
				}
			}
			someStreamMaps.add(someMap);
		}

		// Test for successful scrape
		if (someStreamMaps.size() > 0)
			return true;
		else
			return false;
	}
}
