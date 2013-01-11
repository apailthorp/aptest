package com.aptest.study;

import java.io.File;
import java.util.List;

import org.testng.ISuite;
import org.testng.reporters.EmailableReporter;
import org.testng.xml.XmlSuite;

public class TestEmailReporterSender extends EmailableReporter{
	TestMailer testMailer = new TestMailer();

	@Override
	public void generateReport(List<XmlSuite> arg0, List<ISuite> arg1, String arg2) {

		super.generateReport(arg0, arg1, arg2);

		/* fetch the completed default report */
	    File resultsFile = new File(arg2 + "\\emailable-report.html");
		String someBuildIdAsParameter = arg1.get(0).getParameter("build");

		/* add file to the email with build referencing name, then send the email */
		testMailer.attachFile(resultsFile,"build_" + someBuildIdAsParameter + "_emailable-report.html");
		if (arg1.get(0).getParameter("sendEmail").equalsIgnoreCase("true"))
			testMailer.sendEmail();
	}
}