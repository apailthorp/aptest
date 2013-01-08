package com.aptest.study;

import java.io.File;
import java.util.List;

import javax.mail.Message;

import org.testng.ISuite;
import org.testng.reporters.EmailableReporter;
import org.testng.xml.XmlSuite;

public class TestMailSender extends EmailableReporter{
	TestMailer testMailer = new TestMailer();
	Message resultEmail;

	@Override
	public void generateReport(List<XmlSuite> arg0, List<ISuite> arg1, String arg2) {

		super.generateReport(arg0, arg1, arg2);

		String someBuildIdAsParameter = arg1.get(0).getParameter("build");
		String someEmailAsParameter = arg1.get(0).getParameter("notifyEmail");
		/* create email from utility class with address, subject line, message content */
		resultEmail = testMailer.makeEmail(someEmailAsParameter, "Build: " + someBuildIdAsParameter
				+ " suite results", "results attached");

		/* fetch the hopefully completed default report */
	    File resultsFile = new File(arg2 + "\\emailable-report.html");

		/* add file to the email with build referencing name, then send the email */
		resultEmail = testMailer.attachFile(resultEmail, resultsFile, //.getAbsoluteFile(),
				"build_" + someBuildIdAsParameter + "_emailable-report.html");
		testMailer.sendEmail(resultEmail);
	}
}