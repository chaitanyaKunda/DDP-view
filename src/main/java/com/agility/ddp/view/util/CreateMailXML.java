package com.agility.ddp.view.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date; 
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.agility.ddp.dao.RuleDao;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
public class CreateMailXML {
	
       
	private static final Logger logger = LoggerFactory.getLogger(CreateMailXML.class);
	
	public String production = "";
	public String strEmailFromAddress="" ;
	public String strEmailTo="";
	public String strSmtpHost="";
	public String namBatch="";
	public String otherBatch="";
	public String xmlFileLocation="";
	public CreateMailXML() {
		
	}
	public boolean createXML(List<RuleDao> ruleDaos,String company,String toAddress,String region)
	{
		logger.debug("CreateMailXML.createXML(List<RuleDao> ruleDaos,String company) Method Invoked.");
		strEmailTo = toAddress;
		String strMailFromaddress = "";
		String strEmailCC="";
		String xmlRootElement="";
		String xmlChildName="";
		String xmlChildRuleId="";
		String xmlChildMailID="";
		String xmlChildCCMailID="";
		String xmlChildSubject="";
		String xmlChildBody="";
		String xmlChildDocType="";
		String xmlChildBranch="";
		String xmlChildEmailing="";
		String xmlChildPrinting="";
		 Properties prop = new Properties();
			try {
			    //load a properties file from class path, inside static method
			    prop.load(CreateMailXML.class.getClassLoader().getResourceAsStream("common.properties"));
			    production = prop.getProperty("app.evn");
			    strEmailFromAddress = prop.getProperty("email.from");
			    if(!region.equalsIgnoreCase(""))
			    {
			    	strEmailCC = prop.getProperty("email."+region.trim()+".cc");
			    	if(strEmailCC == null || strEmailCC.equalsIgnoreCase(""))
			    		strEmailCC = prop.getProperty("email.cc");
			    }
			    else
			    	strEmailCC = prop.getProperty("email.cc");
			    strSmtpHost = prop.getProperty("smtp.host");
			    namBatch = prop.getProperty("batch.nam");
			    otherBatch = prop.getProperty("batch.other");
			    xmlFileLocation = prop.getProperty("xml.file.location");
			    xmlRootElement = prop.getProperty("xml.rootTag");
			    xmlChildName = prop.getProperty("xml.childTag.name");
			    xmlChildRuleId = prop.getProperty("xml.childTag.ruleId");
			    xmlChildMailID = prop.getProperty("xml.childTag.mailID");
			    xmlChildCCMailID = prop.getProperty("xml.childTag.cCMailID");
			    xmlChildSubject = prop.getProperty("xml.childTag.subject");
			    xmlChildBody = prop.getProperty("xml.childTag.body");
			    xmlChildDocType = prop.getProperty("xml.childTag.DocType");
			    xmlChildBranch = prop.getProperty("xml.childTag.branch");
			    xmlChildEmailing = prop.getProperty("xml.childTag.Emailing");
			    xmlChildPrinting = prop.getProperty("xml.childTag.Printing");
			    strMailFromaddress = prop.getProperty("email.from");
			} 
			catch (IOException ex) {
			    ex.printStackTrace();
			}
		
		
		boolean success = false;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
			.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			Document doc = impl.createDocument(null, null, null);
			Element e1 = doc.createElement(xmlRootElement);
			doc.appendChild(e1);
			for (int i=0;i<ruleDaos.size();i++){
				RuleDao ruleList= ruleDaos.get(i);
				
				if( ruleList.getGenSource().equalsIgnoreCase("3rd Party") || ruleList.getRdtStatus()==1 )
					continue;
				
				String partyId = ruleList.getRdtPartyId().trim();
				String partyCode = ruleList.getRdtPartyCode();
				/*
				 * removing special charecters partyId
				 * if partyId start with digit append NUM_ before paryIt since this is root element in xml file
				 */
				if (partyId.startsWith("*")) {
					partyId = "ALL";
					partyCode = "ALL";
				} else {
					partyId =partyId.replaceAll("[^a-zA-Z0-9_.]", "");
					if(partyId.substring(0,1).matches("[0-9]"))
					{
						partyId="NUM_"+partyId;
					}
				}
				partyId = partyId.replaceAll("/", "_").toUpperCase();
				//System.out.println(sublist.get(0).toString()+"-"+sublist.get(3).toString());
				try{
					Element e2 = doc.createElement(partyId+"-"+ruleList.getRdtDocType().replaceAll(" ", "")+"-"+partyCode);
					Element e3 = doc.createElement(xmlChildName);
					Element e10 = doc.createElement(xmlChildRuleId);
					Element e4 = doc.createElement(xmlChildMailID);
					Element e5 = doc.createElement(xmlChildCCMailID);
					Element e11 = doc.createElement(xmlChildSubject);
					Element e12 = doc.createElement(xmlChildBody);
					Element e6 = doc.createElement(xmlChildDocType);
					Element e7 = doc.createElement(xmlChildBranch);
					Element e8 = doc.createElement(xmlChildEmailing);
					Element e9 = doc.createElement(xmlChildPrinting);
					
					e2.appendChild(e3);
					e2.appendChild(e10);
					e2.appendChild(e4);
					e2.appendChild(e5);
					e2.appendChild(e6);
					e2.appendChild(e7);
					e2.appendChild(e8);
					e2.appendChild(e9);
					e2.appendChild(e10);
					e2.appendChild(e11);
					e2.appendChild(e12);

					e10.setTextContent(String.valueOf(ruleList.getRid()));
					e3.setTextContent((ruleList.getRdtPartyId().startsWith("*") ? "ALL" :ruleList.getRdtPartyId().toUpperCase()));
					e4.setTextContent(ruleList.getCemEmailTo());
					e5.setTextContent(ruleList.getCemEmailCc());
					e11.setTextContent(ruleList.getCemSubject());
					e12.setTextContent(ruleList.getCemBody());
					e6.setTextContent(ruleList.getRdtDocType());
					e7.setTextContent(ruleList.getRdtBranch());
					e8.setTextContent(ruleList.getCopEmail());
					e9.setTextContent(ruleList.getCopPrint());
					e1.appendChild(e2);
				}
				catch(Exception e){
					logger.error("Error occured at: "+partyId+" : "+partyCode);
				}
				
				//System.out.println(e2.getTextContent());

			}
			doc.normalizeDocument();
			Source source = new DOMSource(doc);
			// Prepare the output file
			String Filename="";
			if(production.equalsIgnoreCase("PROD")){
				Filename="DDP_"+company+Constant.EmailFileName;
			}else{
				Filename="DDP_"+"UAT_"+company+Constant.EmailFileName;
			}
			
			File xmlFfile = new File("C:\\Data"+ File.separator + Filename);

			System.out.println("XML file in "+xmlFfile.getAbsolutePath());
			try {
				FileOutputStream in = new FileOutputStream(xmlFfile);
				Result result = new StreamResult(in);
				try {
					// Write the DOM document to the file
					Transformer xformer = TransformerFactory.newInstance().newTransformer();
					xformer.transform(source, result);
					//xformer.reset();
					in.close();
					// result.
					// filename.renameTo();
					success = true;
					logger.debug("CreateMailXML.createXML(List<RuleDao> ruleDaos,String company) : createXML files created successfully");
					logger.debug("CreateMailXML.createXML(List<RuleDao> ruleDaos,String company) : Calling send email methods");
					String attachName=xmlFfile.getName();
					String content =attachName+" File has been Edited";
					String subject="Email Config Edited-"+production+"-"+company;
					sendMail(strSmtpHost, strMailFromaddress, strEmailTo, strEmailCC, subject, content, xmlFfile);
					logger.info("Email sent successfully");
				} catch (TransformerConfigurationException e) {
					logger.error("Error Details"+e.getMessage());
					success = false;
				} catch (TransformerFactoryConfigurationError e) {
					logger.error("Error Details : "+e.getMessage());
					success = false;
				} catch (TransformerException e) {
					logger.error("Error Details: "+e.getMessage());
					success = false;
				}
			} catch (FileNotFoundException e) {
				logger.error("Error Details: "+e.getMessage());
			} catch (IOException e) {
				logger.error("Error Details: "+e.getMessage());
			}
			try {
				logger.debug("CreateMailXML.createXML(List<RuleDao> ruleDaos,String company) : Moving files to server is started");
				MoveFiles(region);
				logger.debug("CreateMailXML.createXML(List<RuleDao> ruleDaos,String company) : Moving files to server is completed");
			} catch (Exception e) {
				logger.error("Error Details: "+e.getMessage());
			}

		} catch (DOMException e) {
			logger.error("Error Details: "+e.getMessage());
			success = false;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			success = false;
		}
		logger.debug("CreateMailXML.createXML(List<RuleDao> ruleDaos,String company) Executed Successfully.");
		return success;
	}


//	public ArrayList getDetails(String company,IDfSession session){
//
//		String query = "select branch,clientid,mailid,ccmailid,emailing, printing, documenttype,party " +
//		" from dm_dbo.mail_clients, dm_dbo.mail_docs where dm_dbo.mail_clients.sno=dm_dbo.mail_docs.sno and company ='" + company+"'";
//
//		System.out.println(query);
//		ArrayList<ArrayList> Parent = new ArrayList();
//		try {
//			IDfCollection col=execQuery(query,session);
//
//			while(col.next()){
//				ArrayList<String> subList = new ArrayList();
//
//				subList.add(col.getString("clientid").replace("/", ""));
//				subList.add(col.getString("mailid"));
//				subList.add(col.getString("ccmailid"));
//				subList.add(col.getString("documenttype"));
//				subList.add(col.getString("branch"));
//				subList.add(col.getString("emailing"));
//				subList.add(col.getString("printing"));
//				subList.add(col.getString("party"));
//				Parent.add(subList);
//			}
//			col.close();
//
//
//
//		}catch(Exception e){
//
//		}
//		return Parent;
//	}


	
	public void sendMail(String smtpHost,String mailFromAddress,String mailToAddress,String mailCCAddress,String mailSubject,String mailBody,File filename) {
		try {
			// 10.20.1.27
			if(null==mailFromAddress){
				mailFromAddress="DMSSupport@agility.com";
			}
			if(null==mailToAddress){
				mailToAddress ="ckunda@agility.com";
			}
			Properties props = System.getProperties();
			// -- Attaching to default Session, or we could start a new one --
			props.put("mail.smtp.host", smtpHost);
			javax.mail.Session sess = javax.mail.Session.getDefaultInstance(props, null);
			// -- Create a new message --
			Message msg=new MimeMessage(sess);
			// -- Set the FROM and TO fields --
			msg.setFrom(new InternetAddress(mailFromAddress));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailToAddress, false));
			//--- CC Address
			mailCCAddress = mailCCAddress.replaceAll(";", ",");
			StringTokenizer ccAddressTokens = new StringTokenizer(mailCCAddress, ",");
			InternetAddress[] addressTo = new InternetAddress[ccAddressTokens
					.countTokens()];
			int j = 0;
			while (ccAddressTokens.hasMoreTokens()) {
				addressTo[j] = new InternetAddress(ccAddressTokens.nextToken());
				j++;
			}
//			 msg.setRecipients( javax.mail.Message.RecipientType.CC,InternetAddress.parse(strEmailCC, false));
			 msg.setRecipients( javax.mail.Message.RecipientType.CC,addressTo);
			// create the message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(mailBody,"text/html");
			// fill message
			//messageBodyPart.setText(Constants.BODYCONTENT);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			if(filename != null)
			{
				// Part two is attachment
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(filename);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(filename.getName());
				multipart.addBodyPart(messageBodyPart);
			}
			msg.setContent(multipart);
			msg.setSubject(mailSubject);
			msg.setSentDate(new Date());
			// -- Send the message --
			Transport.send(msg);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	} 

	public void MoveFiles(String region){
		String batchFileName="";
		if (region.equalsIgnoreCase("NAM")){
			batchFileName=namBatch;
		}else{
			batchFileName=otherBatch;
		}
		ProcessBuilder builder = new ProcessBuilder(batchFileName);
		builder.directory(new File(xmlFileLocation));
		try {
			Process javap = builder.start();
			writeProcessOutput(javap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	static void writeProcessOutput(Process process) throws Exception{
		InputStreamReader tempReader = new InputStreamReader(
				new BufferedInputStream(process.getInputStream()));
		BufferedReader reader = new BufferedReader(tempReader);
		while (true){
			String line = reader.readLine();
			if (line == null)
				break;
			System.out.println(line);
		}		

		}
		

}
