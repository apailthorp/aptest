package com.aptest.study;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.testng.Reporter;

public class TestMail {
	public Logger log = Logger.getLogger(this.getClass());
	public static final boolean logToConsole = true;

	public static Message makeEmail(String emailTo, String emailSubject, String emailMessage){
		final String username = "automation@pailthorp.net";
		final String password = "PushTheButton247!";

		Message message = null;

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "mail.pailthorp.net");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(emailTo));
			message.setSubject(emailSubject);

			// create the message part 
			MimeBodyPart messageBodyPart = 
					new MimeBodyPart();

			//fill message
			messageBodyPart.setText(emailMessage);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);

			Reporter.log("email message to " + emailTo + " created", logToConsole);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}	

		return message;
	}

	public static Message attachFile(Message emailMessage, File fileAttachment, String fileName){

		Multipart multipart;
		try {
			multipart = (Multipart)emailMessage.getContent();
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		} catch (MessagingException e1) {
			throw new RuntimeException(e1);
		}

		// Part two is attachment
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		DataSource source = 
				new FileDataSource(fileAttachment);

		try {
			messageBodyPart.setDataHandler(
					new DataHandler(source));

			messageBodyPart.setFileName(fileName);

			multipart.addBodyPart(messageBodyPart);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}	

		Reporter.log("File attached: " + fileName, logToConsole);

		return emailMessage;

	}

	public static void sendEmail(Message emailMessage){
		
		try {
			Transport.send(emailMessage);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		finally {
			Reporter.log("Email sent", logToConsole);
		}

	}


}
