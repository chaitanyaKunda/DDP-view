package com.agility.ddp.view.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.agility.ddp.core.util.FileUtils;
import com.agility.ddp.core.util.SchedulerJobUtil;
import com.agility.ddp.dao.RuleDao;
import com.agility.ddp.data.domain.DdpBranch;
import com.agility.ddp.data.domain.DdpCategorizationHolder;
import com.agility.ddp.data.domain.DdpCategorizationHolderService;
import com.agility.ddp.data.domain.DdpCategorizedDocs;
import com.agility.ddp.data.domain.DdpCategorizedDocsService;
import com.agility.ddp.data.domain.DdpCommEmail;
import com.agility.ddp.data.domain.DdpCommEmailService;
import com.agility.ddp.data.domain.DdpCommOptionsLkp;
import com.agility.ddp.data.domain.DdpCommOptionsLkpPK;
import com.agility.ddp.data.domain.DdpCommOptionsLkpService;
import com.agility.ddp.data.domain.DdpCommOptionsSetup;
import com.agility.ddp.data.domain.DdpCompany;
import com.agility.ddp.data.domain.DdpCompanyService;
import com.agility.ddp.data.domain.DdpDmsDocsDetail;
import com.agility.ddp.data.domain.DdpDoctype;
import com.agility.ddp.data.domain.DdpDoctypeService;
import com.agility.ddp.data.domain.DdpExportQuery;
import com.agility.ddp.data.domain.DdpExportQueryService;
import com.agility.ddp.data.domain.DdpExportQueryUi;
import com.agility.ddp.data.domain.DdpExportQueryUiService;
import com.agility.ddp.data.domain.DdpExportSuccessReport;
import com.agility.ddp.data.domain.DdpExportVersionLkp;
import com.agility.ddp.data.domain.DdpExportVersionLkpPK;
import com.agility.ddp.data.domain.DdpGenSourceLkp;
import com.agility.ddp.data.domain.DdpGenSourceLkpPK;
import com.agility.ddp.data.domain.DdpGenSourceLkpService;
import com.agility.ddp.data.domain.DdpGenSourceSetup;
import com.agility.ddp.data.domain.DdpMultiEmails;
import com.agility.ddp.data.domain.DdpMultiEmailsService;
import com.agility.ddp.data.domain.DdpParty;
import com.agility.ddp.data.domain.DdpPartyService;
import com.agility.ddp.data.domain.DdpRateLkp;
import com.agility.ddp.data.domain.DdpRateLkpPK;
import com.agility.ddp.data.domain.DdpRateLkpService;
import com.agility.ddp.data.domain.DdpRuleDetail;
import com.agility.ddp.data.domain.DdpRuleDetailService;
import com.agility.ddp.data.domain.DdpUser;
import com.agility.ddp.data.domain.DdpUserService;
import com.agility.ddp.view.web.ReportWrapper;
/**
 * 
 * @author CKunda
 *
 */
@Controller
@RequestMapping(value = "ruleutil")
public class RuleUtil {

	public static final Logger logger = LoggerFactory.getLogger(RuleUtil.class);
	@Autowired
	public JdbcTemplate jdbcTemplate;
	
	@Autowired
	public DdpRuleDetailService ddpRuleDetailService;
	
	@Autowired
	public DdpUserService ddpUserService;
	
	@Autowired
	public DdpCompanyService ddpCompanyService;
	
	@Autowired
	public DdpDoctypeService ddpDoctypeService;
	
	@Autowired
	public DdpPartyService ddpPartyService;
	
	@Autowired
	public DdpGenSourceLkpService ddpGenSourceLkpService;
	
	@Autowired
	public DdpRateLkpService ddpRateLkpService;
	
	@Autowired
	public DdpCommOptionsLkpService ddpCommOptionsLkpService;
	
	@Autowired
	public DdpCommEmailService ddpCommEmailService;
	
	@Autowired
	public DdpCategorizationHolderService ddpCategorizationHolderService;
	
	@Autowired
	public DdpCategorizedDocsService ddpCategorizedDocsService;
	
	@Autowired
	public DdpMultiEmailsService ddpMultiEmailService;
	
	
	@Autowired
	private ApplicationUIProperties env;
	
	@RequestMapping(value = "/aedreports/form",method = RequestMethod.GET)
	public String reportAEDForm(Model uiModel)
	{
		addDateTimeFormatPatterns(uiModel);
		String strUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		populateForm(uiModel,strUserName,env.getProperty("rule.aed.firstchars"));
		ReportUtil reportUtil = new ReportUtil();
		reportUtil.setReportWrapper(new ReportWrapper());
		reportUtil.setRulesetupReportWrapper(new RulesetupReportWrapper());
		uiModel.addAttribute("reportUtil", reportUtil);
		return "ddpreports/aedrepgenerate";
	}
	@RequestMapping(value = "/aedreports",method = RequestMethod.POST)
	public String reportAED(@Valid ReportUtil reportUtil,BindingResult bindingResult, Model uiModel,HttpServletRequest request,HttpServletResponse response)
	{
		String selectedTab = request.getParameter("selectedTab");
		if(selectedTab.equalsIgnoreCase("transTab"))
		{
			ReportWrapper reportWrapper = reportUtil.getReportWrapper();
			if(reportUtil.getTransCompanyList() != null)
				reportWrapper.setCompanyList(reportUtil.getTransCompanyList());
			if(reportUtil.getTransDoctypeList() != null)
				reportWrapper.setDoctypeList(reportUtil.getTransDoctypeList());
			if(reportUtil.getTransClientIDList() != null)
				reportWrapper.setClientIDList(reportUtil.getTransClientIDList());
			if(reportWrapper.getEndDate() == null)
			{
				reportWrapper.setEndDate(Calendar.getInstance());
			}
			if(reportWrapper.getStartDate() == null)
			{
				Calendar yearStartDate = Calendar.getInstance();
				yearStartDate.set(Calendar.MONTH, 0);
				yearStartDate.set(Calendar.DATE, 1);
				reportWrapper.setStartDate(yearStartDate);
			}
			 String transmittype = request.getParameter("TransmitTypeTransmittedRB");
			 performOperation("AED", reportWrapper,null,transmittype,response);
		}
		if(selectedTab.equalsIgnoreCase("ruleSetupTab"))
		{
			RulesetupReportWrapper rulesetupReportWrapper = reportUtil.getRulesetupReportWrapper();
			if(reportUtil.getSetupCompanyList() != null)
				rulesetupReportWrapper.setCompanyList(reportUtil.getSetupCompanyList());
			if(reportUtil.getSetupDoctypeList() != null)
				rulesetupReportWrapper.setDoctypeList(reportUtil.getSetupDoctypeList());
			if(reportUtil.getSetupClientIDList() != null)
				rulesetupReportWrapper.setClientIDList(reportUtil.getSetupClientIDList());
			 String transmittype = request.getParameter("TransmitTypeRuleRB");
			 performOperation("AED", null,rulesetupReportWrapper, transmittype,response);
		}
		
		 return "redirect:/";
	}
	@RequestMapping(value = "/consolreports/form",method = RequestMethod.GET)
	public String reportConForm(Model uiModel)
	{
		addDateTimeFormatPatterns(uiModel);
		String strUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		populateForm(uiModel,strUserName,env.getProperty("rule.maed.firstchars"));
		
		ReportUtil reportUtil = new ReportUtil();
		reportUtil.setReportWrapper(new ReportWrapper());
		reportUtil.setRulesetupReportWrapper(new RulesetupReportWrapper());
		uiModel.addAttribute("reportUtil", reportUtil);
		return "ddpreports/consolrepgenerate";
	}
	@RequestMapping(value = "/consolreports",method = RequestMethod.POST)
	public String reportMultiAED(@Valid ReportUtil reportUtil,BindingResult bindingResult, Model uiModel,HttpServletRequest request,HttpServletResponse response)
	{
		String selectedTab = request.getParameter("selectedTab");
		if(selectedTab.equalsIgnoreCase("transTab"))
		{
			ReportWrapper reportWrapper = reportUtil.getReportWrapper();
			if(reportUtil.getTransCompanyList() != null)
				reportWrapper.setCompanyList(reportUtil.getTransCompanyList());
			if(reportUtil.getTransDoctypeList() != null)
				reportWrapper.setDoctypeList(reportUtil.getTransDoctypeList());
			if(reportUtil.getTransClientIDList() != null)
				reportWrapper.setClientIDList(reportUtil.getTransClientIDList());
			if(reportWrapper.getEndDate() == null)
			{
				reportWrapper.setEndDate(Calendar.getInstance());
			}
			if(reportWrapper.getStartDate() == null)
			{
				Calendar yearStartDate = Calendar.getInstance();
				yearStartDate.set(Calendar.MONTH, 0);
				yearStartDate.set(Calendar.DATE, 1);
				reportWrapper.setStartDate(yearStartDate);
			}
			
			 String transmittype = request.getParameter("TransmitTypeTransmittedRB");
			 performReportForConsolidation("MULTI_AED", reportWrapper, null, transmittype,response);
		}
		else
		{
			RulesetupReportWrapper rulesetupReportWrapper = reportUtil.getRulesetupReportWrapper();
			if(reportUtil.getSetupCompanyList() != null)
				rulesetupReportWrapper.setCompanyList(reportUtil.getSetupCompanyList());
			if(reportUtil.getSetupDoctypeList() != null)
				rulesetupReportWrapper.setDoctypeList(reportUtil.getSetupDoctypeList());
			if(reportUtil.getSetupClientIDList() != null)
				rulesetupReportWrapper.setClientIDList(reportUtil.getSetupClientIDList());
			 String transmittype = request.getParameter("TransmitTypeRuleRB");
			 performOperation("MULTI_AED", null,rulesetupReportWrapper, transmittype,response);
		}
		 return "redirect:/";
	}
	@RequestMapping(value = "/exportreports/form",method = RequestMethod.GET)
	public String reportExportForm(Model uiModel)
	{
		addDateTimeFormatPatterns(uiModel);
		String strUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		populateForm(uiModel,strUserName,env.getProperty("rule.exp.firstchars"));
		ReportUtil reportUtil = new ReportUtil();
		reportUtil.setReportWrapper(new ReportWrapper());
		reportUtil.setRulesetupReportWrapper(new RulesetupReportWrapper());
		uiModel.addAttribute("reportUtil", reportUtil);
		return "ddpreports/exportrepgenerate";
	}
	@RequestMapping(value = "/exportreports",method = RequestMethod.POST)
	public String reportExport(@Valid ReportUtil reportUtil,BindingResult bindingResult, Model uiModel,HttpServletRequest request,HttpServletResponse response)
	{
		String selectedTab = request.getParameter("selectedTab");
		if(selectedTab.equalsIgnoreCase("transTab"))
		{
			ReportWrapper reportWrapper = reportUtil.getReportWrapper();
			if(reportUtil.getTransCompanyList() != null)
				reportWrapper.setCompanyList(reportUtil.getTransCompanyList());
			if(reportUtil.getTransDoctypeList() != null)
				reportWrapper.setDoctypeList(reportUtil.getTransDoctypeList());
			if(reportUtil.getTransClientIDList() != null)
				reportWrapper.setClientIDList(reportUtil.getTransClientIDList());
			if(reportWrapper.getEndDate() == null)
			{
				reportWrapper.setEndDate(Calendar.getInstance());
			}
			if(reportWrapper.getStartDate() == null)
			{
				Calendar yearStartDate = Calendar.getInstance();
				yearStartDate.set(Calendar.MONTH, 0);
				yearStartDate.set(Calendar.DATE, 1);
				reportWrapper.setStartDate(yearStartDate);
			}
			
			 String transmittype = request.getParameter("TransmitTypeTransmittedRB");
			 performReportForExport("EXPORT", reportWrapper, null, transmittype,response);
		}
		else
		{
			RulesetupReportWrapper rulesetupReportWrapper = reportUtil.getRulesetupReportWrapper();
			if(reportUtil.getSetupCompanyList() != null)
				rulesetupReportWrapper.setCompanyList(reportUtil.getSetupCompanyList());
			if(reportUtil.getSetupDoctypeList() != null)
				rulesetupReportWrapper.setDoctypeList(reportUtil.getSetupDoctypeList());
			if(reportUtil.getSetupClientIDList() != null)
				rulesetupReportWrapper.setClientIDList(reportUtil.getSetupClientIDList());
			 String transmittype = request.getParameter("TransmitTypeRuleRB");
			 performReportForExport("EXPORT", null,rulesetupReportWrapper, transmittype,response);
		}
		 return "redirect:/";
	}
	public void performOperation(String ruletype,ReportWrapper reportWrapper,RulesetupReportWrapper rulesetupReportWrapper, String transmittype,HttpServletResponse response)
	{
		File generatedFile = null;
		String companies = "";
		String documents = "";
		String statusLst="";
		String clientIDs="";
		String receipantToAddress = "";
		String receipantCcAddress = "";
		
		if(reportWrapper != null)
		{
			receipantToAddress = reportWrapper.getToAddress();
			receipantCcAddress = reportWrapper.getCcAddress();
			if(reportWrapper.getCompanyList() != null)
			   {
				   String col= "ddd.DDD_COMPANY_SOURCE=";
			    	for(String company:reportWrapper.getCompanyList()){
			    		companies  +=col+"'"+company+"' or ";
			    	}
			    	companies = companies.substring(0, companies.lastIndexOf(" or"));
			   }
			if(reportWrapper.getDoctypeList() != null)
			   {
				   String col= "ddd.DDD_CONTROL_DOC_TYPE=";
			    	for(String doctype:reportWrapper.getDoctypeList()){
			    		documents  +=col+"'"+doctype+"' or ";
			    	}
			    	documents = documents.substring(0, documents.lastIndexOf(" or"));
			   }
			if(reportWrapper.getClientIDList() != null)
			   {
				   String col= "rdt.RDT_PARTY_ID=";
			    	for(String clientID:reportWrapper.getClientIDList()){
			    		clientIDs  +=col+"'"+clientID+"' or ";
			    	}
			    	clientIDs = clientIDs.substring(0, clientIDs.lastIndexOf(" or"));
			   }
			if(reportWrapper.getStatusList() != null)
			   {
					if(! reportWrapper.getStatusList().contains("all"))
					{
						 String col= "cat.CAT_STATUS=";
						 for(String stutus:reportWrapper.getStatusList()){
				    		statusLst  +=col+""+stutus+" or ";
						 }
						 statusLst = statusLst.substring(0, statusLst.lastIndexOf(" or"));
					}
			   }
			List<ReportWrapper> reportWrappers = getReportWrapperForSelectedCondition(ruletype+"_RULE", companies, documents,clientIDs,statusLst,reportWrapper.getStartDate(),reportWrapper.getEndDate());
			generatedFile = generateExcelReport(reportWrappers, generatedFile, ruletype.toLowerCase(), reportWrapper.getStartDate(), reportWrapper.getEndDate());
		}
		if(rulesetupReportWrapper != null)
		{
			receipantToAddress = rulesetupReportWrapper.getToAddress();
			receipantCcAddress = rulesetupReportWrapper.getCcAddress();
			if(rulesetupReportWrapper.getCompanyList() != null)
			   {
				   String col= "rdt.RDT_COMPANY=";
			    	for(String company:rulesetupReportWrapper.getCompanyList()){
			    		companies  +=col+"'"+company+"' or ";
			    	}
			    	companies = companies.substring(0, companies.lastIndexOf(" or"));
			   }
			if(rulesetupReportWrapper.getDoctypeList() != null)
			   {
				   String col= "rdt.RDT_DOC_TYPE=";
			    	for(String doctype:rulesetupReportWrapper.getDoctypeList()){
			    		documents  +=col+"'"+doctype+"' or ";
			    	}
			    	documents = documents.substring(0, documents.lastIndexOf(" or"));
			   }
			if(rulesetupReportWrapper.getClientIDList() != null)
			   {
				   String col= "rdt.RDT_PARTY_ID=";
			    	for(String partyID:rulesetupReportWrapper.getClientIDList()){
			    		clientIDs  +=col+"'"+partyID+"' or ";
			    	}
			    	clientIDs = clientIDs.substring(0, clientIDs.lastIndexOf(" or"));
			   }
			if(rulesetupReportWrapper.getStatusList() != null)
			   {
					if(! rulesetupReportWrapper.getStatusList().contains("all"))
					{
						 String col= "rdt.RDT_STATUS=";
						 for(String stutus:rulesetupReportWrapper.getStatusList()){
				    		statusLst  +=col+""+stutus+" or ";
						 }
						 statusLst = statusLst.substring(0, statusLst.lastIndexOf(" or"));
					}
			   }
			List<RulesetupReportWrapper> rulesetupReportWrappers = getRulesetupreportwrapper(ruletype+"_RULE", companies, documents,clientIDs,statusLst);
			generatedFile = generateExcellReportForRulesetups(rulesetupReportWrappers, generatedFile, ruletype.toLowerCase());
		}
		if(transmittype.equalsIgnoreCase("download"))
		{
			performDownload(generatedFile,response);
		}
		else{
			CreateMailXML createMailXML = new CreateMailXML();
			createMailXML.sendMail(env.getProperty("smtp.host"), env.getProperty("email.from"), receipantToAddress, receipantCcAddress, env.getProperty("report."+ruletype.toLowerCase()+".subject"), env.getProperty("report."+ruletype.toLowerCase()+".body"), generatedFile);
		}
		if (generatedFile != null)
			generatedFile.delete();
	}
	
	public void performReportForConsolidation(String ruletype,ReportWrapper reportWrapper,RulesetupReportWrapper rulesetupReportWrapper, String transmittype,HttpServletResponse response)
	{
		File generatedFile = null;
		String companies = "";
		String documents = "";
		String statusLst="";
		String clientIDs="";
		String receipantToAddress = "";
		String receipantCcAddress = "";
		if(reportWrapper != null)
		{
			receipantToAddress = reportWrapper.getToAddress();
			receipantCcAddress = reportWrapper.getCcAddress();
			if(reportWrapper.getCompanyList() != null)
			   {
				   String col= "rdt.RDT_COMPANY=";
			    	for(String company:reportWrapper.getCompanyList()){
			    		companies  +=col+"'"+company+"' or ";
			    	}
			    	companies = companies.substring(0, companies.lastIndexOf(" or"));
			   }
			if(reportWrapper.getDoctypeList() != null)
			   {
				   String col= "maedr.MAEDR_DOC_TYPE=";
			    	for(String doctype:reportWrapper.getDoctypeList()){
			    		documents  +=col+"'"+doctype+"' or ";
			    	}
			    	documents = documents.substring(0, documents.lastIndexOf(" or"));
			   }
			if(reportWrapper.getClientIDList() != null)
			   {
				   String col= "rdt.RDT_PARTY_ID=";
			    	for(String clientID:reportWrapper.getClientIDList()){
			    		clientIDs  +=col+"'"+clientID+"' or ";
			    	}
			    	clientIDs = clientIDs.substring(0, clientIDs.lastIndexOf(" or"));
			   }
			if(reportWrapper.getStatusList() != null)
			   {
					if(! reportWrapper.getStatusList().contains("all"))
					{
						 String col= "cat.CAT_STATUS=";
						 for(String stutus:reportWrapper.getStatusList()){
				    		statusLst  +=col+""+stutus+" or ";
						 }
						 statusLst = statusLst.substring(0, statusLst.lastIndexOf(" or"));
					}
			   }
			List<MultiAEDReportWrapper> multiAEDReportWrapper = getReportWrapperForConsolidatedRules(ruletype+"_RULE", companies, documents,clientIDs,statusLst,reportWrapper.getStartDate(),reportWrapper.getEndDate());
			generatedFile = generateExcelReportForConsolidation(multiAEDReportWrapper, generatedFile, ruletype.toLowerCase(), reportWrapper.getStartDate(), reportWrapper.getEndDate());
		}
		if(rulesetupReportWrapper != null)
		{
			receipantToAddress = rulesetupReportWrapper.getToAddress();
			receipantCcAddress = rulesetupReportWrapper.getCcAddress();
			if(rulesetupReportWrapper.getCompanyList() != null)
			   {
				   String col= "rdt.RDT_COMPANY=";
			    	for(String company:rulesetupReportWrapper.getCompanyList()){
			    		companies  +=col+"'"+company+"' or ";
			    	}
			    	companies = companies.substring(0, companies.lastIndexOf(" or"));
			   }
			if(rulesetupReportWrapper.getDoctypeList() != null)
			   {
				   String col= "maedr.MAEDR_DOC_TYPE=";
			    	for(String doctype:rulesetupReportWrapper.getDoctypeList()){
			    		documents  +=col+"'"+doctype+"' or ";
			    	}
			    	documents = documents.substring(0, documents.lastIndexOf(" or"));
			   }
			if(rulesetupReportWrapper.getClientIDList() != null)
			   {
				   String col= "rdt.RDT_PARTY_ID=";
			    	for(String partyID:rulesetupReportWrapper.getClientIDList()){
			    		clientIDs  +=col+"'"+partyID+"' or ";
			    	}
			    	clientIDs = clientIDs.substring(0, clientIDs.lastIndexOf(" or"));
			   }
			if(rulesetupReportWrapper.getStatusList() != null)
			   {
					if(! rulesetupReportWrapper.getStatusList().contains("all"))
					{
						 String col= "rdt.RDT_STATUS=";
						 for(String stutus:rulesetupReportWrapper.getStatusList()){
				    		statusLst  +=col+""+stutus+" or ";
						 }
						 statusLst = statusLst.substring(0, statusLst.lastIndexOf(" or"));
					}
			   }
			List<RulesetupReportWrapper> rulesetupReportWrappers = getRulesetupreportwrapper(ruletype+"_RULE", companies, documents,clientIDs,statusLst);
			generatedFile = generateExcellReportForRulesetups(rulesetupReportWrappers, generatedFile, ruletype.toLowerCase());
		}
		if(transmittype.equalsIgnoreCase("download"))
		{
			performDownload(generatedFile,response);
		}
		else{
			CreateMailXML createMailXML = new CreateMailXML();
			createMailXML.sendMail(env.getProperty("smtp.host"), env.getProperty("email.from"), receipantToAddress, receipantCcAddress, env.getProperty("report."+ruletype.toLowerCase()+".subject"), env.getProperty("report."+ruletype.toLowerCase()+".body"), generatedFile);
		}
		if (generatedFile != null)
			generatedFile.delete();
	}
	public void performReportForExport(String ruletype,ReportWrapper reportWrapper,RulesetupReportWrapper rulesetupReportWrapper, String transmittype,HttpServletResponse response)
	{
		File generatedFile = null;
		String companies = "";
		String documents = "";
		String statusLst="";
		String clientIDs="";
		String receipantToAddress = "";
		String receipantCcAddress = "";
		if(reportWrapper != null)
		{
			receipantToAddress = reportWrapper.getToAddress();
			receipantCcAddress = reportWrapper.getCcAddress();
			if(reportWrapper.getCompanyList() != null)
			   {
				   String col= "rdt.RDT_COMPANY=";
			    	for(String company:reportWrapper.getCompanyList()){
			    		companies  +=col+"'"+company+"' or ";
			    	}
			    	companies = companies.substring(0, companies.lastIndexOf(" or"));
			   }
			if(reportWrapper.getDoctypeList() != null)
			   {
				   String col= "rdt.RDT_DOC_TYPE=";
			    	for(String doctype:reportWrapper.getDoctypeList()){
			    		documents  +=col+"'"+doctype+"' or ";
			    	}
			    	documents = documents.substring(0, documents.lastIndexOf(" or"));
			   }
			if(reportWrapper.getClientIDList() != null)
			   {
				   String col= "rdt.RDT_PARTY_ID=";
			    	for(String clientID:reportWrapper.getClientIDList()){
			    		clientIDs  +=col+"'"+clientID+"' or ";
			    	}
			    	clientIDs = clientIDs.substring(0, clientIDs.lastIndexOf(" or"));
			   }
			if(reportWrapper.getStatusList() != null)
			   {
					if(! reportWrapper.getStatusList().contains("all"))
					{
						 String col= "cat.CAT_STATUS=";
						 for(String stutus:reportWrapper.getStatusList()){
				    		statusLst  +=col+""+stutus+" or ";
						 }
						 statusLst = statusLst.substring(0, statusLst.lastIndexOf(" or"));
					}
			   }
			List<ExportReportWrapper> exportReportWrapper = getReportWrapperForExportRules(ruletype+"_RULE", companies, documents,clientIDs,reportWrapper.getStatusList(),reportWrapper.getStartDate(),reportWrapper.getEndDate());
			generatedFile = generateExcelReportForExport(exportReportWrapper, generatedFile, ruletype.toLowerCase(), reportWrapper.getStartDate(), reportWrapper.getEndDate());
		}
		if(rulesetupReportWrapper != null)
		{
			receipantToAddress = rulesetupReportWrapper.getToAddress();
			receipantCcAddress = rulesetupReportWrapper.getCcAddress();
			if(rulesetupReportWrapper.getCompanyList() != null)
			   {
				   String col= "rdt.RDT_COMPANY=";
			    	for(String company:rulesetupReportWrapper.getCompanyList()){
			    		companies  +=col+"'"+company+"' or ";
			    	}
			    	companies = companies.substring(0, companies.lastIndexOf(" or"));
			   }
			if(rulesetupReportWrapper.getDoctypeList() != null)
			   {
				   String col= "rdt.RDT_DOC_TYPE=";
			    	for(String doctype:rulesetupReportWrapper.getDoctypeList()){
			    		documents  +=col+"'"+doctype+"' or ";
			    	}
			    	documents = documents.substring(0, documents.lastIndexOf(" or"));
			   }
			if(rulesetupReportWrapper.getClientIDList() != null)
			   {
				   String col= "rdt.RDT_PARTY_ID=";
			    	for(String partyID:rulesetupReportWrapper.getClientIDList()){
			    		clientIDs  +=col+"'"+partyID+"' or ";
			    	}
			    	clientIDs = clientIDs.substring(0, clientIDs.lastIndexOf(" or"));
			   }
			if(rulesetupReportWrapper.getStatusList() != null)
			   {
					if(! rulesetupReportWrapper.getStatusList().contains("all"))
					{
						 String col= "rdt.RDT_STATUS=";
						 for(String stutus:rulesetupReportWrapper.getStatusList()){
				    		statusLst  +=col+""+stutus+" or ";
						 }
						 statusLst = statusLst.substring(0, statusLst.lastIndexOf(" or"));
					}
			   }
			List<RulesetupReportWrapper> rulesetupReportWrappers = getRulesetupreportwrapperForExport(ruletype+"_RULE", companies, documents,clientIDs,statusLst);
			generatedFile = generateExcellReportForExportRulesetups(rulesetupReportWrappers, generatedFile, ruletype.toLowerCase());
		}
		
		if(transmittype.equalsIgnoreCase("download"))
		{
			performDownload(generatedFile,response);
		}
		else{
			CreateMailXML createMailXML = new CreateMailXML();
			createMailXML.sendMail(env.getProperty("smtp.host"), env.getProperty("email.from"), receipantToAddress, receipantCcAddress, env.getProperty("report."+ruletype.toLowerCase()+".subject"), env.getProperty("report."+ruletype.toLowerCase()+".body"), generatedFile);
		}
		if (generatedFile != null)
			generatedFile.delete();
	}
	
	private void performDownload(File generatedFile,HttpServletResponse response)
	{
		if (generatedFile != null) 
		 {
	       	  File downloadFile = null;
		      downloadFile = generatedFile;
	       	 
	       	  try {
		        	  if (downloadFile != null) {
		        		  
		        		  	FileInputStream inputStream = new FileInputStream(downloadFile);
		        		  	response.setContentType("text/csv");
		        		  	response.setContentLength((int) downloadFile.length());
		        		  	
		        		 // set headers for the response
		        	        String headerKey = "Content-Disposition";
		        	        String headerValue = String.format("attachment; filename=\"%s\"",downloadFile.getName());
		        	        response.setHeader(headerKey, headerValue);
		        	     // get output stream of the response
		        	        OutputStream outStream = response.getOutputStream();
		        	        byte[] buffer = new byte[4096];
		        	        int bytesRead = -1;
		        	        
		        	        // write bytes read from the input stream into the output stream
		        	        while ((bytesRead = inputStream.read(buffer)) != -1) {
		        	        	outStream.write(buffer, 0, bytesRead);
		        	        }
		        	        inputStream.close();
		        	        outStream.close();
		        	  }
	       	  
	       	  } catch (Exception  ex) {
	   			  ex.printStackTrace();
	   		  }
        }
	}
	public void addDateTimeFormatPatterns(Model uiModel)
	{
		uiModel.addAttribute("dateformat", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
	}
	public void populateForm(Model uiModel,String strUserName,String ruletype)
	{
		String strUserCompany = getUserCompany(strUserName);
		List<DdpDoctype> listDoctypes = new ArrayList<DdpDoctype>();
		listDoctypes.addAll(getDocTypesByCompany("GIL"));
		listDoctypes.addAll(getDocTypesByCompany(strUserCompany));
//		uiModel.addAttribute("companies", getAccessibleCompaniesLst(strUserName, env.getProperty("rule."+ruletype+".firstchars")));
		uiModel.addAttribute("companies", getAccessibleCompaniesforReports(strUserName,ruletype));
		uiModel.addAttribute("doctypes",listDoctypes);
	}
	/**
	 * 
	 * @param logginUser
	 */
	public List<DdpCompany> getAccessibleCompaniesLst(String strUserName,String roleChars)
	{
		DdpUser ddpUser = checkUserExististance(strUserName).get(0);
		List<DdpCompany> userCompanies = new ArrayList<DdpCompany>();
		String userCompany = ddpUser.getUsrCompanyCode();
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		List<String> authoritiesLst = new ArrayList<String>();
		Set<String> multiCountries = new HashSet<String>();
		  for(GrantedAuthority authority:authorities)
		  {
			  authoritiesLst.add(authority.getAuthority());
			  if(authority.getAuthority().toLowerCase().startsWith(roleChars.toLowerCase()+"_") ||
					  authority.getAuthority().toLowerCase().startsWith(env.getProperty("role_sub_all").toLowerCase()+"_") )
			  {
				  multiCountries.add(authority.getAuthority().split("_")[1].toUpperCase());
			  }
			  if(authority.getAuthority().toLowerCase().startsWith(env.getProperty("Read_only_start").toLowerCase()))
			  {
				  if(authority.getAuthority().split("_")[2].equalsIgnoreCase(env.getProperty("read_only_region_sub")))
				  {
					  if(authority.getAuthority().split("_")[4].equalsIgnoreCase(roleChars) || 
							  authority.getAuthority().split("_")[4].equalsIgnoreCase(env.getProperty("role_sub_all")))
					  {
						  List<DdpCompany> readonlycompanies = new ArrayList<DdpCompany>();
						  try{
							  readonlycompanies = getCompaniesByRegionJdbcTemplate(authority.getAuthority().split("_")[3]);
						  }catch(Exception ex){ logger.error("Unable to find the Region {}",ex); }
						  for(DdpCompany company:readonlycompanies)
							  multiCountries.add(company.getComCompanyCode());
					  }
				  }
				  else
				  {
					  if(authority.getAuthority().split("_")[3].equalsIgnoreCase(roleChars) || 
							  authority.getAuthority().split("_")[3].equalsIgnoreCase(env.getProperty("role_sub_all")))
					  {
						  multiCountries.add(authority.getAuthority().split("_")[2].trim().toUpperCase()); 
					  }
				  }
			  }
		  }
		if(authoritiesLst.contains(env.getProperty("Admin_Role")))
		{
			//all companies
			List<DdpCompany> allCompanys = getAllCompanys();
			userCompanies.addAll(allCompanys);
		}
		else
		{
			if(authoritiesLst.contains(env.getProperty("Region_Role")))
			{
				//get user region
				String userRegion = ddpUser.getUsrRegion();
				//get list of countries in region
		     	userCompanies.addAll(getCompaniesByRegionJdbcTemplate(userRegion));
			}
			if(! multiCountries.isEmpty())
			{
				for(String country:multiCountries)
				{
					DdpCompany authenticateCompany = ddpCompanyService.findDdpCompany(country);
					if(authenticateCompany != null && authenticateCompany.getComStatus()==0)
						userCompanies.add(authenticateCompany);
				}
				if(! multiCountries.contains(userCompany))
				{
					//add logged in user company
		    		DdpCompany ddpCompany = ddpCompanyService.findDdpCompany(userCompany);
		         	userCompanies.add(ddpCompany);
				}
			}
			else
			{
				//add only logged in user company
				DdpCompany ddpCompany = ddpCompanyService.findDdpCompany(userCompany);
		     	userCompanies.add(ddpCompany);
			}
			
			
			if(! userCompanies.isEmpty())
			  {
				  Map<String,DdpCompany> companyMap = new ConcurrentHashMap<String,DdpCompany>();
			         for(DdpCompany company:userCompanies)
			        	 companyMap.put(company.getComCompanyCode(), company);
			         userCompanies.clear();
			         userCompanies.addAll(companyMap.values());
			  }
			
			
//		     if (null != userCompany) {
//		         DdpCompany ddpCompany = ddpCompanyService.findDdpCompany(userCompany);
//		         int i = 0;
//		         int companyIndexVal = 0;
//		         for (DdpCompany company : userCompanies) {
//		             if (company.getComCompanyCode().equalsIgnoreCase(ddpCompany.getComCompanyCode())) {
//		                 companyIndexVal = i;
//		             }
//		             i++;
//		         }
//		         Collections.swap(userCompanies, 0, companyIndexVal);
//		     }
		     
		}
		return userCompanies;
	}
	public List<DdpCompany> getAccessibleCompaniesforReports(String strUserName,String ruleType)
	{
		List<DdpCompany> userCompanies = new ArrayList<DdpCompany>();
		/***/
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		List<String> authoritiesLst = new ArrayList<String>();
		List<String> regionRoles = new ArrayList<String>();
		List<String> localRoles = new ArrayList<String>();
		  for(GrantedAuthority authority:authorities)
		  {
			  authoritiesLst.add(authority.getAuthority());
			  if(authority.getAuthority().startsWith(env.getProperty("Reports_Region_start")) && 
					  ((authority.getAuthority().split("_")[3].equalsIgnoreCase(ruleType)) 
					  || (authority.getAuthority().split("_")[3].equalsIgnoreCase(env.getProperty("role_sub_all")))))
				  regionRoles.add(authority.getAuthority().split("_")[2]);
			  if(authority.getAuthority().startsWith(env.getProperty("Reports_country_start")) && 
					  ((authority.getAuthority().split("_")[3].equalsIgnoreCase(ruleType)) 
							  || (authority.getAuthority().split("_")[3].equalsIgnoreCase(env.getProperty("role_sub_all")))))
				  localRoles.add(authority.getAuthority().split("_")[2]);
		  }
		/****/
		  if(authoritiesLst.contains(env.getProperty("Admin_Role")) || authoritiesLst.contains(env.getProperty("Reports_global_role")))
			  userCompanies.addAll(getAllCompanys());
		  else
		  {
			  try{
				  for(String region:regionRoles)
					  userCompanies.addAll(getCompaniesByRegionJdbcTemplate(region));
				  for(String company:localRoles)
					  userCompanies.add(ddpCompanyService.findDdpCompany(company));
				  /*****/
				  List<DdpCompany> result = new ArrayList<DdpCompany>();
				  Set<String> companyCode = new HashSet<String>();
	
				  for( DdpCompany company : userCompanies ) {
				      if( companyCode.add(company.getComCompanyCode())) {
				          result.add(company );
				      }
				  }
				  userCompanies.clear();
				  userCompanies.addAll(result);
			  }catch(Exception ex){
				  logger.error(ex.getMessage());
			  }
			  /*****/
		  }
		  userCompanies = sortUserCompanies(userCompanies);
		return userCompanies;
	}
	public List<DdpCompany> getListOfReadOnlyAccessibleCompanies(String strUserName,String ruleType)
	{
		List<DdpCompany> userCompanies = new ArrayList<DdpCompany>();
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		List<String> authoritiesLst = new ArrayList<String>();
		for(GrantedAuthority authority:authorities)
		{
			 authoritiesLst.add(authority.getAuthority());
			  if(authority.getAuthority().startsWith(env.getProperty("Read_only_start")))
			  {
				  if(authority.getAuthority().split("_")[2].equalsIgnoreCase(env.getProperty("read_only_region_sub")))
				  {
					  if(authority.getAuthority().split("_")[4].equalsIgnoreCase(ruleType)|| 
							  authority.getAuthority().split("_")[4].equalsIgnoreCase(env.getProperty("role_sub_all")))
					  {
						  try{
							  userCompanies.addAll(getCompaniesByRegionJdbcTemplate(authority.getAuthority().split("_")[3]));
						  }catch(Exception ex){ logger.error("Unable to find the Region {}",ex); }
					  }
				  }
				  else
				  {
					  if(authority.getAuthority().split("_")[3].equalsIgnoreCase(ruleType) || 
							  authority.getAuthority().split("_")[3].equalsIgnoreCase(env.getProperty("role_sub_all")) )
					  {
						  logger.info("User {} has read only role for company{}",strUserName,authority.getAuthority().split("_")[2]);
						  try{
							  userCompanies.add(ddpCompanyService.findDdpCompany(authority.getAuthority().split("_")[2].trim()));
						  }catch(Exception ex){
							  logger.error("Unable to find the company {}",ex);
						  }
					  }
				  }
			  }
		}
		return userCompanies;
	}
    private List<DdpCompany> sortUserCompanies(List<DdpCompany> userCompanies) {
    	if(userCompanies != null &&  !userCompanies.isEmpty())
    	{
    		Collections.sort(userCompanies, new Comparator<DdpCompany>() {
    			@Override
    			public int compare(DdpCompany company1, DdpCompany company2) {
    				return company1.getComCompanyCode().compareTo(company2.getComCompanyCode());
    			}
			});
    	}
		return userCompanies;
	}
	public DdpUser getDdpUser(String crUser)
    {
    	String usrId = this.jdbcTemplate.queryForObject(Constant.GETUSERBYLOGINID,String.class,crUser);
    	 DdpUser user = ddpUserService.findDdpUser(Integer.parseInt(usrId));
    	 return user;
    }
	/**
	 *  
	 * @param strUserName
	 * @return
	 */
	public String getUserCompany(String strUserName)
	{
        return jdbcTemplate.queryForObject(Constant.USERCOMPANY, String.class, strUserName);
    }
	public List<DdpUser> checkUserExististance(String strLoginId)
	   {
		   List<DdpUser> usrLst = this.jdbcTemplate.query(Constant.GETUSERBYLOGINID,new Object[]{strLoginId},new RowMapper<DdpUser>(){

			@Override
			public DdpUser mapRow(ResultSet rs, int rowNum) throws SQLException {
				DdpUser user = ddpUserService.findDdpUser(rs.getInt("USR_ID"));
				return user;
			}});
		   return usrLst;
	   }
	 /**
	    * @param userLoginId
	    * @return User Region
	    */
	   public String getUserRegion(String strUserName) 
	   {
	       return jdbcTemplate.queryForObject(Constant.USERREGION, String.class, strUserName);
	   }
   /**
    * 
    * @param region
    * @return
    */
   public List<DdpCompany> getCompaniesByRegionJdbcTemplate(String region) {
       List<DdpCompany> ddpCompanies = this.jdbcTemplate.query(Constant.COMPANIESBYREGION, new Object[] { region }, new RowMapper<DdpCompany>() {

           @Override
           public DdpCompany mapRow(ResultSet rs, int rowNum) throws SQLException {
        	   return ddpCompanyService.findDdpCompany(rs.getString("COM_COMPANY_CODE"));
//           	DdpCompany ddpCompany = new DdpCompany();
//           	ddpCompany.setComCompanyCode(rs.getString("COM_COMPANY_CODE"));
//              	ddpCompany.setComCompanyName(rs.getString("COM_COMPANY_NAME"));
//           	ddpCompany.setComCountryCode(rs.getString("COM_COUNTRY_CODE"));
//           	ddpCompany.setComCountryName(rs.getString("COM_COUNTRY_NAME"));
//           	ddpCompany.setComRegion(rs.getString("COM_REGION"));
//           	ddpCompany.setComStatus(rs.getInt("COM_STATUS"));
//           	return ddpCompany;
           }
       });
       return ddpCompanies;
   }
   /**
    * 
    * @return list of companys whose status is active
    */
   public List<DdpCompany> getAllCompanys()
   {
	   List<DdpCompany> ddpCompanys = this.jdbcTemplate.query(Constant.SELECT_COMPANY_BY_STATUS,new RowMapper<DdpCompany>(){

		@Override
		public DdpCompany mapRow(ResultSet rs, int rowNum) throws SQLException {
				DdpCompany company = new DdpCompany();
				company.setComCompanyCode(rs.getString("COM_COMPANY_CODE"));
				company.setComCompanyName(rs.getString("COM_COMPANY_NAME"));
				company.setComCountryCode(rs.getString("COM_COUNTRY_CODE"));
				company.setComCountryName(rs.getString("COM_COUNTRY_NAME"));
				company.setComRegion(rs.getString("COM_REGION"));
				company.setComStatus(rs.getInt("COM_STATUS"));
				return company;
			}	   
	   });
	   return ddpCompanys;
   }
   /**
   *
   * @param company
   * @return
   */
  public List<DdpBranch> findBranchbyCompany(String company) {
      List<DdpBranch> ddpBranchName = this.jdbcTemplate.query(Constant.SELECT_BRANCH_BY_COMPANY, new Object[] { company }, new RowMapper<DdpBranch>() {

          public DdpBranch mapRow(ResultSet rs, int rowNum) throws SQLException {
              DdpBranch ddpBranch = new DdpBranch();
              ddpBranch.setBrnBranchCode(rs.getString("brn_branch_code"));
              ddpBranch.setBrnBranchName(rs.getString("brn_branch_name"));
              ddpBranch.setBrnCompnayCode(rs.getString("BRN_COMPNAY_CODE"));
              ddpBranch.setBrnStatus(rs.getInt("brn_status"));
              return ddpBranch;
          }
      });
      return ddpBranchName;
  }
  
   /**
    * 
    * @return list of All DocTypes whose status is active
    */
   public List<DdpDoctype> getAllDocTypes()
   {
	   List<DdpDoctype> ddpDoctypes = this.jdbcTemplate.query(Constant.SELECT_DOCTYPE_BY_STATUS,new Object[]{"GIL"}, new RowMapper<DdpDoctype>(){
		   @Override
		public DdpDoctype mapRow(ResultSet rs, int rowNum) throws SQLException {
			DdpDoctype ddpDoctype = new DdpDoctype();
			ddpDoctype.setDtyDocTypeCode(rs.getString("DTY_DOC_TYPE_CODE"));
			ddpDoctype.setDtyDocTypeName(rs.getString("DTY_DOC_TYPE_NAME"));
			ddpDoctype.setDtySource(rs.getString("DTY_SOURCE"));
			ddpDoctype.setDtySource(rs.getString("DTY_COMPANY_CODE"));
			ddpDoctype.setDtyStatus(rs.getInt("DTY_STATUS"));
			return ddpDoctype;
		}
	   });
	   return ddpDoctypes;
   }
   public List<DdpDoctype> getDocTypesByCompany(String company)
   {
	   List<DdpDoctype> ddpDoctypes = this.jdbcTemplate.query(Constant.SELECT_DOCTYPE_BY_STATUS, new Object[]{company},new RowMapper<DdpDoctype>(){
		   @Override
		public DdpDoctype mapRow(ResultSet rs, int rowNum) throws SQLException {
			   DdpDoctype ddpDoctype = new DdpDoctype();
				ddpDoctype.setDtyDocTypeCode(rs.getString("DTY_DOC_TYPE_CODE"));
				ddpDoctype.setDtyDocTypeName(rs.getString("DTY_DOC_TYPE_NAME"));
				ddpDoctype.setDtySource(rs.getString("DTY_SOURCE"));
				ddpDoctype.setDtySource(rs.getString("DTY_COMPANY_CODE"));
				ddpDoctype.setDtyStatus(rs.getInt("DTY_STATUS"));
				return ddpDoctype;
		}
	   });
	   return ddpDoctypes;
   }
   /**
    * 
    * @return list of All Partys whose status is active
    */
   public List<DdpParty> getAllPartys()
   {
	   List<DdpParty> ddpPartys = this.jdbcTemplate.query(Constant.SELECT_PARTY_BY_STATUS, new RowMapper<DdpParty>(){
		   @Override
		public DdpParty mapRow(ResultSet rs, int rowNum) throws SQLException {
			   DdpParty party = ddpPartyService.findDdpParty(rs.getString("PTY_PARTY_CODE"));
			return party;
		}
	   });
	   return ddpPartys;
   }
   public List<DdpRateLkp> getAllRateLkp()
   {
	   List<DdpRateLkp> ddpRateLkps = this.jdbcTemplate.query("SELECT * FROM DDP_RATE_LKP WHERE RTL_STATUS=0", new RowMapper<DdpRateLkp>(){
			@Override
			public DdpRateLkp mapRow(ResultSet rs, int rowNum) throws SQLException {
				DdpRateLkpPK rateLkpPk = new DdpRateLkpPK(rs.getInt("RTL_ID"),rs.getString("RTL_OPTION"),rs.getString("RTL_DESCRIPTION"),rs.getInt("RTL_STATUS"));
				DdpRateLkp ddpRateLkp = new DdpRateLkp();
				ddpRateLkp.setId(rateLkpPk);
				return ddpRateLkp;
			}
	   });
	   return ddpRateLkps;
   }
   public List<DdpGenSourceLkp> getAllGenSourceLkp()
   {
	   List<DdpGenSourceLkp> ddpGenSourceLkpLst = this.jdbcTemplate.query("SELECT * FROM DDP_GEN_SOURCE_LKP WHERE GSL_STATUS=0",new RowMapper<DdpGenSourceLkp>(){
			   @Override
				public DdpGenSourceLkp mapRow(ResultSet rs, int rowNum)	throws SQLException {
					   DdpGenSourceLkpPK genSourceLkpPK = new DdpGenSourceLkpPK(rs.getInt("GSL_ID"),rs.getString("GSL_OPTION"),rs.getString("GSL_DESCRIPTION"),rs.getInt("GSL_STATUS"));
					   DdpGenSourceLkp ddpGenSourceLkp = new DdpGenSourceLkp();
					   ddpGenSourceLkp.setId(genSourceLkpPK);
					return ddpGenSourceLkp;
				}
		   });
	   return ddpGenSourceLkpLst;
   }
   public List<DdpCommOptionsLkp> getAllCommOptionsLkp()
   {
	   List<DdpCommOptionsLkp> ddpCommOptionsLkps = this.jdbcTemplate.query("SELECT * FROM DDP_COMM_OPTIONS_LKP WHERE CPL_STATUS=0", new RowMapper<DdpCommOptionsLkp>(){
		   @Override
		   public DdpCommOptionsLkp mapRow(ResultSet rs, int rowNum) throws SQLException {
			   DdpCommOptionsLkpPK commOptionsLkpPK = new DdpCommOptionsLkpPK(rs.getInt("CPL_ID"), rs.getString("CPL_OPTION"),rs.getString("CPL_DESCRIPTION"), rs.getInt("CPL_STATUS"));
			   DdpCommOptionsLkp commOptionsLkp = new DdpCommOptionsLkp();
			   commOptionsLkp.setId(commOptionsLkpPK);
			   return commOptionsLkp;
		   }
	   });
	   return ddpCommOptionsLkps;
   }
   public List<DdpExportVersionLkp> getExportVersionLkp()
   {
	   List<DdpExportVersionLkp> ddpExportVersionLkps = this.jdbcTemplate.query("SELECT * FROM DDP_EXPORT_VERSION_LKP WHERE EVL_STATUS=0",new RowMapper<DdpExportVersionLkp>(){
			   @Override
			public DdpExportVersionLkp mapRow(ResultSet rs, int rowNum)	throws SQLException {
				DdpExportVersionLkpPK exportVersionLkpPK = new DdpExportVersionLkpPK(rs.getInt("EVL_ID"),rs.getString("EVL_OPTION"),rs.getString("EVL_DESCRIPTION"),rs.getInt("EVL_STATUS"));
				DdpExportVersionLkp ddpExportVersionLkp = new DdpExportVersionLkp();
				ddpExportVersionLkp.setId(exportVersionLkpPK);
				return ddpExportVersionLkp;
			}
	   });
	   return ddpExportVersionLkps;
   }
   
   public String findGenSourceDescriptionByOption(String gssOption) {
	   	String gssDescription = "Not Applicable";
	   	try{
	   		gssDescription = this.jdbcTemplate.queryForObject(Constant.SELECT_GEN_DESC_BY_OPTION, new Object[]{gssOption},String.class);
	   	}catch(Exception emptyResultEx)
	   	{
	   		logger.error("Exception occured while accessing Generating Source Decription",emptyResultEx.getMessage());
	   	}
		return gssDescription;
	}
   public String findRateDescriptionByOption(String Option) {
	   	String rateDescription="Not Applicable";
	   	try{
	   		rateDescription = this.jdbcTemplate.queryForObject(Constant.SELECT_RATE_DESC_BY_OPTION, new Object[]{Option},String.class);
	   	}catch(Exception emptyResultEx)
	   	{
	   		logger.error("Exception occured while accessing Rate Decription",emptyResultEx.getMessage());
	   	}
		return rateDescription;
	}
   public List<DdpRuleDetail> getRuleDetailsByCompany(String company,String ruleType)
   {
	   List<DdpRuleDetail> ruleDetails = this.jdbcTemplate.query("SELECT RDT_ID FROM DDP_RULE_DETAIL WHERE RDT_RULE_TYPE=? AND RDT_COMPANY=?",new Object[]{ruleType,company},new RowMapper<DdpRuleDetail>(){
			@Override
			public DdpRuleDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
				DdpRuleDetail ruleDetail = ddpRuleDetailService.findDdpRuleDetail(rs.getInt("RDT_ID"));
				return ruleDetail;
			}
	   });
	   return ruleDetails;
   }
  
   public List<String> getBranchesByRuleId(String strCompanyCode,int ruleId){
   	
  	 List<String> brachesLst = this.jdbcTemplate.query(Constant.BRANCHESBYRULEID,new Object[]{strCompanyCode,ruleId}, new RowMapper<String>(){

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String strBranch = rs.getString("RDT_BRANCH");
				return strBranch;
			}
  	 });
  	return brachesLst;
  }
   
   /**
    * 
    * @param strCompanyCode
    * @return
    */
   public List<RuleDao> getRuleDetailForCompanyCode(String strCompanyCode){
   	logger.info("DdpAedRuleController.getRuleDetailForCompanyCode(String strCompanyCode) Method Invoked.");
   	List<RuleDao> ruleDaoLst = new ArrayList<RuleDao>();
       TypedQuery<DdpRuleDetail> typRuleDetails = DdpRuleDetail.findDdpRuleDetailsByRdtCompany(ddpCompanyService.findDdpCompany(strCompanyCode));
       List<DdpRuleDetail> lstRuleDetail = getRuleDetailsByCompany(strCompanyCode, "AED_RULE");
       ArrayList<String> rulList = new ArrayList<String>();
       for(DdpRuleDetail ddpRuleDetail : lstRuleDetail){
       	
       	 int ruleId = ddpRuleDetail.getRdtRuleId().getRulId();
       	 
       	if( ddpRuleDetail.getRdtRuleId().getRulStatus()==0 && !rulList.contains(ruleId+"-"+ddpRuleDetail.getRdtDocType().getDtyDocTypeCode()+"-"+ddpRuleDetail.getRdtPartyCode().getPtyPartyCode()) ){
       		RuleDao ruleDao = new RuleDao();
              
              
               //getBranches for RuleIds
               List<String> brnLst = getBranchesByRuleId(strCompanyCode,ruleId);
               String strBrnLst = "";
               for(String strBranch:brnLst){
                     strBrnLst+=strBranch+",";
               }
               strBrnLst = strBrnLst.substring(0,strBrnLst.lastIndexOf(","));
               String printing = "N";
               String emailing = "Y";
               TypedQuery<DdpCommOptionsSetup> typDdpCommOption =  DdpCommOptionsSetup.findDdpCommOptionsSetupsByCopRdtId(ddpRuleDetail);
               List<DdpCommOptionsSetup> commOptions = typDdpCommOption.getResultList();
               for(DdpCommOptionsSetup commOptionsSetup : commOptions){
                      if("Printing".equalsIgnoreCase(commOptionsSetup.getCopOption())){
                            printing = "Y";
                      }
//                      else if("Emailing".equalsIgnoreCase(commOptionsSetup.getCopOption())){
//                            emailing = "Y";
//                      }
               }
             ruleDao.setRdtId(ddpRuleDetail.getRdtId());
             ruleDao.setRdtCompanyCode(strCompanyCode);
             TypedQuery<DdpGenSourceSetup> qryGen = DdpGenSourceSetup.findDdpGenSourceSetupsByGssRdtId(ddpRuleDetail);
             List<DdpGenSourceSetup> genSource = qryGen.getResultList();
             ruleDao.setGenSource((genSource.isEmpty()) ? "" : genSource.get(0).getGssOption());
             
             //get the key value from HashMap
            // String branches = (String)ruleIdBranches.get(ddpRuleDetail.getRdtRuleId());
             ruleDao.setRdtBranch(strBrnLst);
             ruleDao.setRdtPartyCode(ddpRuleDetail.getRdtPartyCode().getPtyPartyCode());
             ruleDao.setRdtDocType(ddpRuleDetail.getRdtDocType().getDtyDocTypeCode());
             DdpCommEmail commEmail = new DdpCommEmail();
             commEmail = ddpCommEmailService.findDdpCommEmail(Integer.parseInt(ddpRuleDetail.getRdtCommunicationId().getCmsProtocolSettingsId()));
             
             ruleDao.setCemEmailFrom((commEmail == null) ? "" : commEmail.getCemEmailFrom());
             ruleDao.setCemEmailTo((commEmail == null) ? "" :commEmail.getCemEmailTo());
             ruleDao.setCemEmailCc((commEmail == null) ? "" :commEmail.getCemEmailCc());
             ruleDao.setCemSubject((commEmail == null) ? "" :commEmail.getCemEmailSubject());
             ruleDao.setCemBody((commEmail == null) ? "" :commEmail.getCemEmailBody());
             ruleDao.setCopEmail(emailing);
             ruleDao.setCopPrint(printing);
             ruleDao.setRdtPartyId(ddpRuleDetail.getRdtPartyId());
             ruleDao.setRdtStatus(ddpRuleDetail.getRdtStatus());
             int ddpRuleId  = ddpRuleDetail.getRdtRuleId().getRulId();
             ruleDao.setRid(ddpRuleId);
             
             //Add to the List<RuleDao>
             ruleDaoLst.add(ruleDao);
             rulList.add(ddpRuleId+"-"+ddpRuleDetail.getRdtDocType().getDtyDocTypeCode()+"-"+ddpRuleDetail.getRdtPartyCode().getPtyPartyCode());
       	}
       }
   	logger.info("DdpAedRuleController.getRuleDetailForCompanyCode(String strCompanyCode) Executed Successfully.");
       return ruleDaoLst;

    }
   public void createXml(Object[] objects)
   {
	   DdpUser ddpUser = (DdpUser)objects[0];
	   DdpCompany ddpcompany = (DdpCompany)objects[1];
	   Scheduler scheduler = (Scheduler)objects[2];
	   try {
		   List<RuleDao> ruleDaos = getRuleDetailForCompanyCode(ddpcompany.getComCompanyCode());
		   boolean moveFlag = new CreateMailXML().createXML(ruleDaos, ddpcompany.getComCompanyCode(),ddpUser.getUsrEmailAddress(),ddpcompany.getComRegion());
		   if (moveFlag)
	           logger.info("xml Moved to Server");
		   else
			   logger.error("error occured while create xml file for company:"+ddpcompany.getComCompanyCode());
	   } finally {
		   try {
		   if (scheduler != null)
			   scheduler.shutdown();
		   } catch (Exception ex) {
			   logger.error("RuleUtil.createXml() - unable to release scheduler thread", ex);
		   }
	   }
   }
   
   public void updateCategorizedRdt(Map<Integer,Integer> replaceKey,int ruleId,String ruletype)
   {
	   
//	   List<DdpCategorizationHolder> categorizationHolders = getNotProcessedRdtinChl(ruleId);
//	   for(DdpCategorizationHolder categorizationHolder: categorizationHolders)
//	   {
//		   if(replaceKey.containsKey(categorizationHolder.getChlRulId()))
//		   {
//			   categorizationHolder.setChlRulId(replaceKey.get(categorizationHolder.getChlRulId()));
//			   ddpCategorizationHolderService.updateDdpCategorizationHolder(categorizationHolder);
//		   }
//	   }
	   List<DdpCategorizedDocs> categorizedDocsLst = getNotProcessedRdts(ruleId,ruletype);
	   for(DdpCategorizedDocs categorizedDocs:categorizedDocsLst)
	   {
		   if(replaceKey.containsKey(categorizedDocs.getCatRdtId()))
		   {
			   categorizedDocs.setCatRdtId(replaceKey.get(categorizedDocs.getCatRdtId()));
			   ddpCategorizedDocsService.updateDdpCategorizedDocs(categorizedDocs);
		   }
	   }
   }
   private List<DdpCategorizationHolder> getNotProcessedRdtinChl(Set<Integer> rdtIds,String ruletype)
   {
	   String dynamiccondition = "";
	   for(int rdtID: rdtIds)
	   {
		   dynamiccondition = rdtID+",";
	   }
	   dynamiccondition = dynamiccondition.substring(0,dynamiccondition.lastIndexOf(','));
	   String query= Constant.SELECT_NOT_PROCESSED_RDT_IDS.replaceAll("dynamiccondition", dynamiccondition);
	   List<DdpCategorizationHolder> categorizationHolders = null;
	   
	   categorizationHolders = this.jdbcTemplate.query(query, new Object[]{},new RowMapper<DdpCategorizationHolder>(){
			@Override
			public DdpCategorizationHolder mapRow(ResultSet rs, int rowNum) throws SQLException {
				return ddpCategorizationHolderService.findDdpCategorizationHolder(Integer.parseInt(rs.getString("CHL_ID")));
			}
	   });
	   return categorizationHolders;
   }
   private List<DdpCategorizedDocs> getNotProcessedRdts(int ruleId,String ruletype) 
   {
//	   String ruletype="MULTI_AED_RULE";
	   List<DdpCategorizedDocs> ddpCategorizedDocs = this.jdbcTemplate.query(Constant.SELECT_NOT_PROCESSED_CATEGORY_DOCS,new Object[]{ruleId,ruletype},new RowMapper<DdpCategorizedDocs>() {

		@Override
		public DdpCategorizedDocs mapRow(ResultSet rs, int rowNum) throws SQLException {
			return ddpCategorizedDocsService.findDdpCategorizedDocs(rs.getInt("CAT_ID"));
		}
	});
	   return ddpCategorizedDocs;
   }
   
   public List<DdpMultiEmails> getMultiEmailsByCmsID(Integer communicationID)
   {
	   List<DdpMultiEmails> multiEmails= this.jdbcTemplate.query("SELECT * FROM DDP_MULTI_EMAILS WHERE MES_CMS_ID = ? ",new Object[]{communicationID.intValue()}, new RowMapper<DdpMultiEmails>(){

		@Override
		public DdpMultiEmails mapRow(ResultSet rs, int rowNum) throws SQLException {
			DdpMultiEmails multiEmails = new DdpMultiEmails();
			multiEmails.setMesEmailId(rs.getInt("MES_EMAIL_ID"));
			multiEmails.setMesEmailTo(rs.getString("MES_EMAIL_TO"));
			multiEmails.setMesEmailCc(rs.getString("MES_EMAIL_CC"));
			multiEmails.setMesDestCompany(rs.getString("MES_DEST_COMPANY"));
			multiEmails.setMesDestRegion(rs.getString("MES_DEST_REGION"));
			return multiEmails;
		}
		   
	   });
	   return multiEmails;
   }
   public int checkDuplicateRule(String ruleType,String company,List<String> brnList,String clientID,LinkedList<Object> partyCodes,LinkedList<Object> docList,Integer existingRuleID)
   {
	   String col= "RDT_PARTY_CODE=";
	   	String partycodes = "";
	   	for(int indexI=0;indexI<docList.size();indexI++)
	   	{
	   		partycodes +="(RDT_PARTY_CODE='"+partyCodes.get(0)+"' AND RDT_DOC_TYPE='"+docList.get(indexI)+"') or ";
	   	}
	   	partycodes = partycodes.substring(0, partycodes.lastIndexOf(" or"));
	   	String conditionBranch = "";
    	for(int indexI=0;indexI<brnList.size();indexI++)
    	{
    		conditionBranch +="RDT_BRANCH='"+brnList.get(indexI)+"' or ";
    	}
    	conditionBranch = conditionBranch.substring(0, conditionBranch.lastIndexOf(" or"));
	   	String query = "SELECT RDT_RULE_ID FROM DDP_RULE_DETAIL WHERE RDT_COMPANY= '"+company+"' AND RDT_STATUS=0 AND RDT_PARTY_ID = '"+clientID+"' AND ("+partycodes+") AND ("+conditionBranch+") AND RDT_FORCED_TYPE=1 AND RDT_RULE_TYPE='MULTI_AED_RULE'";
	   	if(existingRuleID != null)
    		query = query.concat(" AND RDT_RULE_ID NOT IN ("+existingRuleID+")");
	   	List<Integer> rIDList = this.jdbcTemplate.query(query, new Object[] {}, new RowMapper<Integer>(){
	   		@Override
	   		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
	   			int rId;
	   			rId= rs.getInt("RDT_RULE_ID");
	   			return rId;
	   		}
	   	});
	   return rIDList.size();
   }
   public String getCompanyCodesAsString()
	{
		String strCompanyLst = "";
		List<DdpCompany> ddpCompanies = ddpCompanyService.findAllDdpCompanys();
		for(DdpCompany ddpCompany: ddpCompanies)
			strCompanyLst +=ddpCompany.getComCompanyCode()+",";
		strCompanyLst = strCompanyLst.substring(0, strCompanyLst.lastIndexOf(","));
		return strCompanyLst;
	}
   public List<ReportWrapper> getReportWrapperForSelectedCondition(String ruleType,String companies,String doctypes,String clientIDs,String statusLst,Calendar fromDate,Calendar toDate)
   {
	   List<ReportWrapper> reportWrappers = null;
	   String query="";
	   if(clientIDs.equals(""))
	   {
		   query="SELECT cat.CAT_ID,cat.CAT_SYN_ID,ddd.DDD_COMPANY_SOURCE,ddd.DDD_BRANCH_SOURCE,(select RUL_NAME from DDP_RULE"
					+ " where RUL_ID=cat.CAT_rul_ID) as 'Client ID',ddd.DDD_CONTROL_DOC_TYPE,ddd.DDD_CONSIGNMENT_ID,ddd.DDD_JOB_NUMBER,ddd.DDD_DOC_REF,"
					+ "cat.CAT_RUL_ID,cat.CAT_CREATED_DATE,cat.CAT_STATUS FROM DDP_CATEGORIZED_DOCS cat,DDP_DMS_DOCS_DETAIL ddd "
					+ "WHERE cat.CAT_DTX_ID = ddd.DDD_DTX_ID and cat.CAT_RULE_TYPE='"+ruleType+"'";
	   }
	   else
	   {
		   query = "SELECT cat.CAT_ID,cat.CAT_SYN_ID,ddd.DDD_COMPANY_SOURCE,ddd.DDD_BRANCH_SOURCE,(select RUL_NAME from DDP_RULE where RUL_ID=cat.CAT_rul_ID) as 'Client ID',"
				   + "rdt.RDT_PARTY_ID,ddd.DDD_CONTROL_DOC_TYPE,ddd.DDD_CONSIGNMENT_ID,ddd.DDD_JOB_NUMBER,ddd.DDD_DOC_REF,cat.CAT_RUL_ID,"
				   + "cat.CAT_CREATED_DATE,cat.CAT_STATUS FROM DDP_CATEGORIZED_DOCS cat,DDP_DMS_DOCS_DETAIL ddd,DDP_RULE_DETAIL rdt "
				   + "WHERE rdt.RDT_ID=cat.CAT_RDT_ID AND cat.CAT_DTX_ID = ddd.DDD_DTX_ID AND cat.CAT_RULE_TYPE='"+ruleType+"'";
	   }
	   if(! companies.equals(""))
		   query=query.concat(" and ("+companies+")");
	   if(! doctypes.equals(""))
		   query=query.concat(" and ("+doctypes+")");
	   if(! clientIDs.equals(""))
		   query=query.concat(" and ("+clientIDs+")");
	   if(! statusLst.equals(""))
		   query=query.concat(" and ("+statusLst+")");
	   query=query.concat(" and cat.CAT_CREATED_DATE >= ? AND cat.CAT_CREATED_DATE <= ? ORDER BY cat.CAT_ID DESC ");
	   try{
		   
		   	reportWrappers = this.jdbcTemplate.query(query,new Object[]{fromDate,toDate}, new RowMapper<ReportWrapper>(){
				@Override
				public ReportWrapper mapRow(ResultSet rs, int arg1) throws SQLException {
					ReportWrapper reportWrapper = new ReportWrapper();
					reportWrapper.setStrCatID(rs.getString("CAT_ID"));
					reportWrapper.setStrSynID(rs.getString("CAT_SYN_ID"));
					reportWrapper.setStrClientID(getSubstringAfterNthOccurance(rs.getString("Client ID"),'_',2));
					reportWrapper.setStrDddCompanySource(rs.getString("DDD_COMPANY_SOURCE"));
					reportWrapper.setStrDddBranchSource(rs.getString("DDD_BRANCH_SOURCE"));
					reportWrapper.setStrDddControlDocumentType(rs.getString("DDD_CONTROL_DOC_TYPE"));
					reportWrapper.setStrDddConsignmentID(rs.getString("DDD_CONSIGNMENT_ID"));
					reportWrapper.setStrDddJobNumber(rs.getString("DDD_JOB_NUMBER"));
					reportWrapper.setStrRuleID(rs.getString("CAT_RUL_ID"));;
					reportWrapper.setStrrDddDocRef(rs.getString("DDD_DOC_REF"));
					Calendar catCreatedCalender = Calendar.getInstance();
					catCreatedCalender.setTime(rs.getTimestamp("CAT_CREATED_DATE"));
					reportWrapper.setStrCatCreatedDate(catCreatedCalender);
					reportWrapper.setStrCatStatus(rs.getString("CAT_STATUS"));
					return reportWrapper;
				}
			   });
	   }catch(Exception e)
	   {
		   e.printStackTrace();
	   }
	   return reportWrappers;
   }
   
   public List<RulesetupReportWrapper> getRulesetupreportwrapper(String ruleType,String companies,String doctypes,String clientIDs,String statusLst)
   {
	   String query="SELECT rul.RUL_ID,rul.RUL_NAME,rul.RUL_DESCRIPTION,rdt.RDT_STATUS,rdt.RDT_COMPANY,rdt.RDT_BRANCH,rdt.RDT_DOC_TYPE,rdt.RDT_PARTY_CODE,rdt.RDT_PARTY_ID,"
			   		+ "gss.GSS_OPTION,rts.RTS_OPTION,cem.CEM_EMAIL_TO,cem.CEM_EMAIL_CC,rdt.RDT_ACTIVATION_DATE,rdt.RDT_CREATED_BY,rdt.RDT_CREATED_DATE,rdt.RDT_MODIFIED_BY,"
			   		+ "rdt.RDT_MODIFIED_DATE FROM DDP_RULE RUL, DDP_RULE_DETAIL RDT,DDP_COMMUNICATION_SETUP cms,DDP_COMM_EMAIL cem,DDP_GEN_SOURCE_SETUP gss,DDP_RATE_SETUP rts "
			   		+ "WHERE rts.RTS_RDT_ID=rdt.RDT_ID AND gss.GSS_RDT_ID=rdt.RDT_ID AND rdt.RDT_COMMUNICATION_ID=cms.CMS_COMMUNICATION_ID and "
			   		+ "cms.CMS_PROTOCOL_SETTINGS_ID=cem.CEM_EMAIL_ID and rdt.RDT_RULE_TYPE='"+ruleType+"' AND rul.RUL_ID=rdt.RDT_RULE_ID";
	   
	   if(! companies.equals(""))
		   query=query.concat(" and ("+companies+")");
	   if(! doctypes.equals(""))
		   query=query.concat(" and ("+doctypes+")");
	   if(! clientIDs.equals(""))
		   query=query.concat(" and ("+clientIDs+")");
	   if(! statusLst.equals(""))
		   query=query.concat(" and ("+statusLst+")");
	   
	   query=query.concat(" ORDER BY rul.RUL_ID DESC ");
	   
	   List<RulesetupReportWrapper> rulesetupReportWrappers = this.jdbcTemplate.query(query, new Object[]{},new RowMapper<RulesetupReportWrapper>(){
			@Override
			public RulesetupReportWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
				RulesetupReportWrapper rulesetupReportWrapper = new RulesetupReportWrapper();
				rulesetupReportWrapper.setStrRuleID(rs.getString("RUL_ID"));
				rulesetupReportWrapper.setStrRuleName(rs.getString("RUL_NAME"));
				rulesetupReportWrapper.setStrRuleDescription(rs.getString("RUL_DESCRIPTION"));
				rulesetupReportWrapper.setStrRuleStatus(rs.getString("RDT_STATUS"));
				rulesetupReportWrapper.setStrCompany(rs.getString("RDT_COMPANY"));
				rulesetupReportWrapper.setStrBranch(rs.getString("RDT_BRANCH"));
				rulesetupReportWrapper.setStrDocumenttype(rs.getString("RDT_DOC_TYPE"));
				rulesetupReportWrapper.setStrPartyCode(rs.getString("RDT_PARTY_CODE"));
				rulesetupReportWrapper.setStrPartyID(rs.getString("RDT_PARTY_ID"));
				rulesetupReportWrapper.setStrGenSource(rs.getString("GSS_OPTION"));
				rulesetupReportWrapper.setStrRateOption(rs.getString("RTS_OPTION"));
				rulesetupReportWrapper.setStrToRecipients(rs.getString("CEM_EMAIL_TO"));
				rulesetupReportWrapper.setStrCcRecipients(rs.getString("CEM_EMAIL_CC"));
				Calendar activatonDate = Calendar.getInstance();
				activatonDate.setTime(rs.getTimestamp("RDT_ACTIVATION_DATE"));
				rulesetupReportWrapper.setCalActivationDate(activatonDate);
				rulesetupReportWrapper.setStrCreatedBy(rs.getString("RDT_CREATED_BY"));
				Calendar createdDate = Calendar.getInstance();
				createdDate.setTime(rs.getTimestamp("RDT_CREATED_DATE"));
				rulesetupReportWrapper.setCalCreatedDate(createdDate);
				rulesetupReportWrapper.setStrModifiedBy(rs.getString("RDT_MODIFIED_BY"));
				Calendar modifiedDate = Calendar.getInstance();
				modifiedDate.setTime(rs.getTimestamp("RDT_MODIFIED_DATE"));
				rulesetupReportWrapper.setCalModifiedDate(modifiedDate);
				return rulesetupReportWrapper;
			}
	   });
	   return rulesetupReportWrappers;
   }
   public List<RulesetupReportWrapper> getRulesetupreportwrapperForExport(String ruleType,String companies,String doctypes,String clientIDs,String statusLst)
   {
	   String query="SELECT rdt.RDT_RULE_ID,rdt.RDT_COMPANY,rdt.RDT_BRANCH,rdt.RDT_PARTY_CODE,rdt.RDT_DOC_TYPE,gss.GSS_OPTION,rts.RTS_OPTION,rdt.RDT_PARTY_ID,"
	   		+ "sch.SCH_CRON_EXPRESSIONS,cms.CMS_COMMUNICATION_PROTOCOL,rdt.RDT_STATUS,rdt.RDT_CREATED_BY,rdt.RDT_CREATED_DATE,rdt.RDT_ACTIVATION_DATE,"
	   		+ "rdt.RDT_MODIFIED_BY,rdt.RDT_MODIFIED_DATE FROM DDP_RULE_DETAIL rdt,DDP_SCHEDULER sch,DDP_EXPORT_RULE exp,DDP_COMMUNICATION_SETUP CMS,DDP_GEN_SOURCE_SETUP gss,"
	   		+ "DDP_RATE_SETUP rts WHERE rdt.RDT_RULE_TYPE='EXPORT_RULE' AND cms.CMS_COMMUNICATION_ID=rdt.RDT_COMMUNICATION_ID AND "
	   		+ "rts.RTS_RDT_ID=rdt.RDT_ID AND gss.GSS_RDT_ID=rdt.RDT_ID AND exp.EXP_RULE_ID=rdt.RDT_RULE_ID AND sch.SCH_ID=exp.EXP_SCHEDULER_ID";
	   
	   if(! companies.equals(""))
		   query=query.concat(" and ("+companies+")");
	   if(! doctypes.equals(""))
		   query=query.concat(" and ("+doctypes+")");
	   if(! clientIDs.equals(""))
		   query=query.concat(" and ("+clientIDs+")");
	   if(! statusLst.equals(""))
		   query=query.concat(" and ("+statusLst+")");
	   
	   query=query.concat(" ORDER BY exp.EXP_RULE_ID DESC ");
	   
	   List<RulesetupReportWrapper> rulesetupReportWrappers = this.jdbcTemplate.query(query, new Object[]{},new RowMapper<RulesetupReportWrapper>(){
			@Override
			public RulesetupReportWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
				RulesetupReportWrapper rulesetupReportWrapper = new RulesetupReportWrapper();
				rulesetupReportWrapper.setStrRuleID(rs.getString("RDT_RULE_ID"));
				rulesetupReportWrapper.setStrRuleStatus(rs.getString("RDT_STATUS"));
				rulesetupReportWrapper.setStrCompany(rs.getString("RDT_COMPANY"));
				rulesetupReportWrapper.setStrBranch(rs.getString("RDT_BRANCH"));
				rulesetupReportWrapper.setStrDocumenttype(rs.getString("RDT_DOC_TYPE"));
				rulesetupReportWrapper.setStrPartyCode(rs.getString("RDT_PARTY_CODE"));
				rulesetupReportWrapper.setStrPartyID(rs.getString("RDT_PARTY_ID"));
				rulesetupReportWrapper.setStrGenSource(rs.getString("GSS_OPTION"));
				rulesetupReportWrapper.setStrRateOption(rs.getString("RTS_OPTION"));
				rulesetupReportWrapper.setStrCronExpression(rs.getString("SCH_CRON_EXPRESSIONS"));
				rulesetupReportWrapper.setStrExportProtocol(rs.getString("CMS_COMMUNICATION_PROTOCOL"));
				Calendar activatonDate = Calendar.getInstance();
				activatonDate.setTime(rs.getTimestamp("RDT_ACTIVATION_DATE"));
				rulesetupReportWrapper.setCalActivationDate(activatonDate);
				rulesetupReportWrapper.setStrCreatedBy(rs.getString("RDT_CREATED_BY"));
				Calendar createdDate = Calendar.getInstance();
				createdDate.setTime(rs.getTimestamp("RDT_CREATED_DATE"));
				rulesetupReportWrapper.setCalCreatedDate(createdDate);
				rulesetupReportWrapper.setStrModifiedBy(rs.getString("RDT_MODIFIED_BY"));
				Calendar modifiedDate = Calendar.getInstance();
				modifiedDate.setTime(rs.getTimestamp("RDT_MODIFIED_DATE"));
				rulesetupReportWrapper.setCalModifiedDate(modifiedDate);
				return rulesetupReportWrapper;
			}
	   });
	   return rulesetupReportWrappers;
   }
   
   public List<MultiAEDReportWrapper> getReportWrapperForConsolidatedRules(String ruleType,String companies,String doctypes,String clientIDs,String statusLst,Calendar fromDate,Calendar toDate)
   {
	   List<MultiAEDReportWrapper> multiAEDReportWrappers = null;
	   String query="";
	   query="SELECT maedr.MAEDR_RULE_ID,rdt.RDT_PARTY_ID,maedr.MAEDR_DOC_TYPE,maedr.MAEDR_JOB_NUMBER,maedr.MAEDR_CONSIGNMENT_ID,maedr.MAEDR_DOC_REF,"
	   		+ " maedr.MAEDR_CONSOLIDATE_DATE,maedr.MAEDR_CONSOLIDATE_TYPE,maedr.MAEDR_SERVICE_TYPE,cat.CAT_STATUS FROM DDP_MULTI_AED_SUCCESS_REPORT maedr,"
	   		+ "DDP_RULE_DETAIL rdt,DDP_CATEGORIZED_DOCS cat WHERE cat.CAT_ID=maedr.MAEDR_CAT_ID AND rdt.RDT_RULE_TYPE='"+ruleType+"' AND "
	   		+ "rdt.RDT_RULE_ID=maedr.MAEDR_RULE_ID";
	   if(! companies.equals(""))
		   query=query.concat(" and ("+companies+")");
	   if(! doctypes.equals(""))
		   query=query.concat(" and ("+doctypes+")");
	   if(! clientIDs.equals(""))
		   query=query.concat(" and ("+clientIDs+")");
	   if(! statusLst.equals(""))
		   query=query.concat(" and ("+statusLst+")");
	   query=query.concat(" and maedr.MAEDR_CONSOLIDATE_DATE >= ? AND maedr.MAEDR_CONSOLIDATE_DATE <= ? ORDER BY maedr.MAEDR_ID DESC ");
	   try{
		   
		   multiAEDReportWrappers = this.jdbcTemplate.query(query,new Object[]{fromDate,toDate}, new RowMapper<MultiAEDReportWrapper>(){
				@Override
				public MultiAEDReportWrapper mapRow(ResultSet rs, int arg1) throws SQLException {
					MultiAEDReportWrapper multiMedReportWrapper = new MultiAEDReportWrapper();
					multiMedReportWrapper.setStrRuleID(rs.getString("MAEDR_RULE_ID"));
					multiMedReportWrapper.setStrPartyID(rs.getString("RDT_PARTY_ID"));
					multiMedReportWrapper.setStrDocType(rs.getString("MAEDR_DOC_TYPE"));
					multiMedReportWrapper.setStrJobNumber(rs.getString("MAEDR_JOB_NUMBER"));
					multiMedReportWrapper.setStrConsignmentID(rs.getString("MAEDR_CONSIGNMENT_ID"));
					multiMedReportWrapper.setStrDocRef(rs.getString("MAEDR_DOC_REF"));
					Calendar consolidatedDate = Calendar.getInstance();
					consolidatedDate.setTime(rs.getTimestamp("MAEDR_CONSOLIDATE_DATE"));
					multiMedReportWrapper.setCalConsolidatedDate(consolidatedDate);
					multiMedReportWrapper.setStrConsolidatedType(rs.getString("MAEDR_CONSOLIDATE_TYPE"));
					multiMedReportWrapper.setStrServiceType(rs.getString("MAEDR_SERVICE_TYPE"));
					multiMedReportWrapper.setStrCatStatus(rs.getString("CAT_STATUS"));
					return multiMedReportWrapper;
				}
			   });
	   }catch(Exception e)
	   {
		   e.printStackTrace();
	   }
	   return multiAEDReportWrappers;
   }
   public List<ExportReportWrapper> getReportWrapperForExportRules(String ruleType,String companies,String doctypes,String clientIDs,List<String> statusLst,Calendar fromDate,Calendar toDate)
   {
	   List<ExportReportWrapper> exportReportWrappers = new ArrayList<ExportReportWrapper>();
	   String query="";
	   
	   if(statusLst.contains("1"))
	   {
		   query = "SELECT esr.ESR_MIS_ID,esr.ESR_RULE_ID as RULE_ID, rdt.RDT_COMPANY, rdt.RDT_PARTY_ID, esr.ESR_FILE_NAME, esr.ESR_JOB_NUMBER,"
		   		+ " esr.ESR_CONSIGNMENT_ID,esr.ESR_EXPORT_TIME,null AS CAT_CREATED_DATE, '1' as CAT_STATUS FROM DDP_EXPORT_SUCCESS_REPORT esr,"
		   		+ "DDP_RULE_DETAIL rdt WHERE rdt.RDT_RULE_TYPE='EXPORT_RULE' AND rdt.RDT_RULE_ID=esr.ESR_RULE_ID";
			
		   if(! companies.equals(""))
			   query=query.concat(" and ("+companies+")");
		   if(! clientIDs.equals(""))
			   query=query.concat(" and ("+clientIDs+")");
		   
		   query=query.concat(" and esr.ESR_EXPORT_TIME >= ? AND esr.ESR_EXPORT_TIME <= ? ORDER BY esr.ESR_ID DESC ");
		   exportReportWrappers.addAll(this.jdbcTemplate.query(query,new Object[]{ fromDate,toDate },new ExportReportWrapper()));
	   }
	   if(statusLst.contains("0"))
	   {
		   query = "SELECT '-' as ESR_MIS_ID,rdt.RDT_RULE_ID as RULE_ID,rdt.RDT_COMPANY,rdt.RDT_PARTY_ID,'-' AS ESR_FILE_NAME,"
		   		+ "(select ddd.DDD_JOB_NUMBER FROM DDP_DMS_DOCS_DETAIL ddd WHERE ddd.DDD_DTX_ID=cat.CAT_DTX_ID) AS ESR_JOB_NUMBER,"
		   		+ "(select ddd.DDD_CONSIGNMENT_ID FROM DDP_DMS_DOCS_DETAIL ddd WHERE ddd.DDD_DTX_ID=cat.CAT_DTX_ID) AS ESR_CONSIGNMENT_ID,"
		   		+ "null AS ESR_EXPORT_TIME, cat.CAT_CREATED_DATE, cat.CAT_STATUS FROM DDP_CATEGORIZED_DOCS cat,DDP_RULE_DETAIL rdt "
		   		+ "WHERE cat.CAT_DTX_ID=cat.CAT_DTX_ID AND cat.CAT_STATUS=0 AND rdt.RDT_RULE_TYPE='EXPORT_RULE' AND rdt.RDT_RULE_ID=cat.CAT_RUL_ID";
				
			   if(! companies.equals(""))
				   query=query.concat(" and ("+companies+")");
			   if(! clientIDs.equals(""))
				   query=query.concat(" and ("+clientIDs+")");
			   
			   query=query.concat(" and cat.CAT_CREATED_DATE >= ? AND cat.CAT_CREATED_DATE <= ? ORDER BY cat.CAT_ID DESC ");
			   
			   exportReportWrappers.addAll(this.jdbcTemplate.query(query,new Object[]{ fromDate,toDate },new ExportReportWrapper()));
	   }
	  
	   return exportReportWrappers;
   }
   	@RequestMapping(value="/valuesByCountryAsString",method=RequestMethod.GET,headers="Accept=Application/json")
	@ResponseBody
	public Map<String,String> valuesByCountryAsString(HttpServletRequest httpServletRequest)
	{
		Map<String,String> map = new HashMap<String,String>();
		String companyId = httpServletRequest.getParameter("companyID");
		List<DdpBranch> branches = new ArrayList<DdpBranch>();
		 if(! companyId.equalsIgnoreCase("GIL"))
	     {	
	    	 branches = findBranchbyCompany("GIL");
	     }
		branches.addAll(findBranchbyCompany(companyId));
		if(companyId.equalsIgnoreCase("USC"))
			branches.addAll(findBranchbyCompany("LUS"));
		String strBranch = "";
	   	 for(DdpBranch branch:branches){
	   		 strBranch+=branch.getBrnBranchCode()+",";
	   	 }
	   	 if (strBranch.trim().length() > 0)
	   		 strBranch = strBranch.substring(0, strBranch.lastIndexOf(","));
//		 return res;
	   	List<DdpDoctype> allDoctype = getAllDocTypes();
	   	allDoctype.addAll(getDocTypesByCompany(companyId));
	   	String strDoctype = "";
	   	for(DdpDoctype doctype:allDoctype)
	   		strDoctype += doctype.getDtyDocTypeCode()+',';
	   	if(strDoctype.trim().length() > 0)
	   		strDoctype = strDoctype.substring(0,strDoctype.lastIndexOf(","));
	   	
	   	map.put("branchs", strBranch);
	   	map.put("doctypes", strDoctype);
	   	 
	   	return map;
	}
   	
   	@RequestMapping(value="/distinctClientIDByCompany",method=RequestMethod.GET,headers="Accept=Application/json")
	@ResponseBody
	public Map<String,String> distinctClientIDByCompany(HttpServletRequest httpServletRequest)
	{
		Map<String,String> map = new HashMap<String,String>();
		String comapnycode = httpServletRequest.getParameter("comapnycode");
		String ruletype = httpServletRequest.getParameter("ruletype");
		List<String> distinctClientIDs = findDistinctClientIDsByCompany(comapnycode,ruletype);
		
		String strClientID= "";
	   	 for(String clientID:distinctClientIDs){
	   		strClientID+=clientID+",";
	   	 }
	   	 if (strClientID.trim().length() > 0)
	   		strClientID = strClientID.substring(0, strClientID.lastIndexOf(","));
//		 return res;
	   	
	   	map.put("varDistinctClientIDs", strClientID);
	   	 
	   	return map;
	}
   	
   	private List<String> findDistinctClientIDsByCompany(String comapnycode, String ruletype) {
		List<String> distinctClientIDs = this.jdbcTemplate.query("SELECT DISTINCT RDT_PARTY_ID FROM DDP_RULE_DETAIL WHERE RDT_COMPANY=? AND RDT_RULE_TYPE=?", new Object[]{comapnycode,ruletype},new RowMapper<String>(){

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("RDT_PARTY_ID");
			}
		});
		return distinctClientIDs;
	}
	@RequestMapping(value="/valuesByCountryAsList",method=RequestMethod.GET,headers="Accept=Application/json")
	@ResponseBody
	public Map<String,Object> branchByCountryAsList(Model uiModel,HttpServletRequest httpServletRequest)
	{
   		Map<String,Object> map = new HashMap<String,Object>();
		String companyId = httpServletRequest.getParameter("companyID");
		List<DdpBranch> branches = findBranchbyCompany("GIL");
		branches.addAll(findBranchbyCompany(companyId));
		if(companyId.equalsIgnoreCase("USC"))
			branches.addAll(findBranchbyCompany("LUS"));
		List<DdpDoctype> allDoctype = getAllDocTypes();
	   	allDoctype.addAll(getDocTypesByCompany(companyId));
		map.put("branchLst", branches);
		map.put("dtyLst", allDoctype);
		return map;
	}
	public File generateExcelReport(List<ReportWrapper> reports,File generatedFile,String ruletype,Calendar startDate,Calendar endDate) 
	{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyLocalizedPattern("yyyy-MMM-dd HH-mm-ss");
		
		try {
				
				String tempFilePath = env.getProperty("xml.file.location");
				String tempFolderPath = null;
								
//				tempFolderPath = tempFilePath + "/temp_" + ruletype + "_report_" + dateFormat.format(new Date()); 
				generatedFile = new File(tempFilePath+File.separator+ruletype.toUpperCase()+"_REPORT_"+dateFormat.format(startDate.getTime())+"-"+dateFormat.format(endDate.getTime())+".csv");
				
				FileWriter csvFile = new FileWriter(tempFilePath+File.separator+ruletype.toUpperCase()+"_REPORT_"+dateFormat.format(startDate.getTime())+"-"+dateFormat.format(endDate.getTime())+".csv");
				csvFile.append(env.getProperty("report."+ruletype+".header.catid"));
				csvFile.append(",");
				csvFile.append(env.getProperty("report."+ruletype+".header.synid"));
				csvFile.append(",");
				csvFile.append(env.getProperty("report."+ruletype+".header.company"));
				csvFile.append(",");
				csvFile.append(env.getProperty("report."+ruletype+".header.branch"));
				csvFile.append(",");
				csvFile.append(env.getProperty("report."+ruletype+".header.clientid"));
				csvFile.append(",");
				csvFile.append(env.getProperty("report."+ruletype+".header.doctype"));
				csvFile.append(",");
				csvFile.append(env.getProperty("report."+ruletype+".header.consgid"));
				csvFile.append(",");
				csvFile.append(env.getProperty("report."+ruletype+".header.jobnum"));
				csvFile.append(",");
				csvFile.append(env.getProperty("report."+ruletype+".header.docref"));
				csvFile.append(",");
				csvFile.append(env.getProperty("report."+ruletype+".header.ruleid"));
				csvFile.append(",");
				csvFile.append(env.getProperty("report."+ruletype+".header.date"));
				csvFile.append(",");
				csvFile.append(env.getProperty("report."+ruletype+".header.status"));
				csvFile.append("\n");
				for (ReportWrapper report : reports) {
					
					csvFile.append(report.getStrCatID());
					csvFile.append(",");
					csvFile.append(report.getStrSynID());
					csvFile.append(",");
					csvFile.append(report.getStrDddCompanySource());
					csvFile.append(",");
					csvFile.append(report.getStrDddBranchSource());
					csvFile.append(",");
					csvFile.append(report.getStrClientID());
					csvFile.append(",");
					csvFile.append(report.getStrDddControlDocumentType());
					csvFile.append(",");
					csvFile.append(report.getStrDddConsignmentID());
					csvFile.append(",");
					csvFile.append(report.getStrDddJobNumber());
					csvFile.append(",");
					csvFile.append(report.getStrrDddDocRef());
					csvFile.append(",");
					csvFile.append(report.getStrRuleID());
					csvFile.append(",");
					csvFile.append(dateFormat.format(report.getStrCatCreatedDate().getTime()));
					csvFile.append(",");
					csvFile.append(env.getProperty("report."+ruletype+".status.value."+report.getStrCatStatus(),"DEFAULT")+"");
					csvFile.append("\n");
				}
				csvFile.flush();
				csvFile.close();
		} catch (Exception ex) {
			logger.error("RuleUtil.generateExcelReport() - Unable to generate excel file", ex);
		}
//		File reportFile = new File(reportsFolder+"/"+ruletype.toUpperCase()+"_REPORT_"+dateFormat.format(startDate.getTime())+"-"+dateFormat.format(endDate.getTime())+".csv");
//		return reportsFolder;
		return generatedFile;
	}
	
	public File generateExcelReportForConsolidation(List<MultiAEDReportWrapper> multiAEDReportWrappers,File generatedFile,String ruletype,Calendar startDate,Calendar endDate) 
	{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyLocalizedPattern("yyyy-MMM-dd HH-mm-ss");
		
		try {
				
				String tempFilePath = env.getProperty("xml.file.location");
				String tempFolderPath = null;
								
//				tempFolderPath = tempFilePath + "/temp_" + ruletype + "_report_" + dateFormat.format(new Date()); 
				generatedFile = new File(tempFilePath+File.separator+ruletype.toUpperCase()+"_REPORT_"+dateFormat.format(startDate.getTime())+"-"+dateFormat.format(endDate.getTime())+".csv");
				
				FileWriter csvFile = new FileWriter(tempFilePath+File.separator+ruletype.toUpperCase()+"_REPORT_"+dateFormat.format(startDate.getTime())+"-"+dateFormat.format(endDate.getTime())+".csv");
				csvFile.append(env.getProperty("report."+ruletype+".header.ruleid"));
				csvFile.append(",");
				String header = "report."+ruletype+".header.partyID";
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.partyID"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.documenttype"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.jobnumber"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.consignmentid"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.documentref"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.consolidateddate"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.consolidatedtype"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.servicetype"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.status"));
				csvFile.append("\n");
				for (MultiAEDReportWrapper multiAEDReportWrapper : multiAEDReportWrappers) {
					
					csvFile.append(multiAEDReportWrapper.getStrRuleID());
					csvFile.append(",");
					csvFile.append(multiAEDReportWrapper.getStrPartyID());
					csvFile.append(",");
					csvFile.append(multiAEDReportWrapper.getStrDocType());
					csvFile.append(",");
					csvFile.append(multiAEDReportWrapper.getStrJobNumber());
					csvFile.append(",");
					csvFile.append(multiAEDReportWrapper.getStrConsignmentID());
					csvFile.append(",");
					csvFile.append(multiAEDReportWrapper.getStrDocRef());
					csvFile.append(",");
					csvFile.append(dateFormat.format(multiAEDReportWrapper.getCalConsolidatedDate().getTime()));
					csvFile.append(",");
					csvFile.append(env.getProperty("rules.report.multi_aed.consolidatetype."+multiAEDReportWrapper.getStrConsolidatedType()));
					csvFile.append(",");
					csvFile.append(env.getProperty("rules.report.multi_aed.servicetype."+multiAEDReportWrapper.getStrServiceType()) );
					csvFile.append(",");
					csvFile.append(env.getProperty("rules.report.multi_aed.status."+multiAEDReportWrapper.getStrCatStatus()));
					csvFile.append("\n");
				}
				csvFile.flush();
				csvFile.close();
		} catch (Exception ex) {
			logger.error("RuleUtil.generateExcelReportForConsolidation() - Unable to generate excel file", ex);
		}
//		File reportFile = new File(reportsFolder+"/"+ruletype.toUpperCase()+"_REPORT_"+dateFormat.format(startDate.getTime())+"-"+dateFormat.format(endDate.getTime())+".csv");
//		return reportsFolder;
		return generatedFile;
	}
	public File generateExcelReportForExport(List<ExportReportWrapper> exportReportWrappers,File generatedFile,String ruletype,Calendar startDate,Calendar endDate)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyLocalizedPattern("yyyy-MMM-dd HH-mm-ss");
		String tempFilePath = env.getProperty("xml.file.location");
		try{
			String tempFolderPath = null;
			generatedFile = new File(tempFilePath+File.separator+ruletype.toUpperCase()+"_REPORT_"+dateFormat.format(startDate.getTime())+"-"+dateFormat.format(endDate.getTime())+".csv");
			FileWriter csvFile = new FileWriter(tempFilePath+File.separator+ruletype.toUpperCase()+"_REPORT_"+dateFormat.format(startDate.getTime())+"-"+dateFormat.format(endDate.getTime())+".csv");
			csvFile.append(env.getProperty("rules.report.export.header.ruleid"));
			csvFile.append(",");
			csvFile.append(env.getProperty("rules.report.export.header.company"));
			csvFile.append(",");
			csvFile.append(env.getProperty("rules.report.export.header.partyID"));
			csvFile.append(",");
			csvFile.append(env.getProperty("rules.report.export.header.filename"));
			csvFile.append(",");
			csvFile.append(env.getProperty("report.export.header.jobnumber"));
			csvFile.append(",");
			csvFile.append(env.getProperty("report.export.header.consgid"));
			csvFile.append(",");
			csvFile.append(env.getProperty("report.export.header.exporteddate"));
			csvFile.append(",");
			csvFile.append(env.getProperty("report.export.header.createddate"));
			csvFile.append(",");
			csvFile.append(env.getProperty("report.export.header.status"));
			csvFile.append("\n");
			
			for(ExportReportWrapper exportReportWrapper: exportReportWrappers)
			{
				csvFile.append(exportReportWrapper.getStrRuleID());
				csvFile.append(",");
				csvFile.append(exportReportWrapper.getStrCompany());
				csvFile.append(",");
				csvFile.append(exportReportWrapper.getStrClientID());
				csvFile.append(",");
				csvFile.append(exportReportWrapper.getStrFilename());
				csvFile.append(",");
				csvFile.append(exportReportWrapper.getStrJobnumber());
				csvFile.append(",");
				csvFile.append(exportReportWrapper.getStrConsignmentID());
				csvFile.append(",");
				csvFile.append((exportReportWrapper.getCalExportedDate() == null) ? "-" : dateFormat.format(exportReportWrapper.getCalExportedDate().getTime()));
				csvFile.append(",");
				csvFile.append((exportReportWrapper.getCalCreatedDate() == null) ? "-" : dateFormat.format(exportReportWrapper.getCalCreatedDate().getTime()));
				csvFile.append(",");
				csvFile.append(env.getProperty("report.export.status.value."+exportReportWrapper.getStatus()));
				csvFile.append("\n");
			}
			csvFile.flush();
			csvFile.close();
		}catch(Exception ex){ ex.printStackTrace(); }
		
		return generatedFile;
	}
	public File generateExcellReportForRulesetups(List<RulesetupReportWrapper> rulesetupReportWrappers,File generatedFile,String ruletype)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyLocalizedPattern("yyyy-MMM-dd HH-mm-ss");
		
		Calendar today = Calendar.getInstance();
		try {
				
				String tempFilePath = env.getProperty("xml.file.location");
				String tempFolderPath = null;
								
				generatedFile = new File(tempFilePath+File.separator+ruletype.toUpperCase()+"_RULES_SETUP_REPORT_"+dateFormat.format(today.getTime())+".csv");
				
				FileWriter csvFile = new FileWriter(tempFilePath+File.separator+ruletype.toUpperCase()+"_RULES_SETUP_REPORT_"+dateFormat.format(today.getTime())+".csv");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.ruleid"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.rulename"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.ruledesc"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.rulestatus"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.company"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.branch"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.documenttype"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.partycode"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.partyID"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.gensource"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.rateoption"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.torecipients"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.ccrecipients"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.activation"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.createdby"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.creationdate"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.modifiedby"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.modifieddate"));
				csvFile.append("\n");
				for (RulesetupReportWrapper report : rulesetupReportWrappers) {
					
					csvFile.append(report.getStrRuleID());
					csvFile.append(",");
					csvFile.append(report.getStrRuleName());
					csvFile.append(",");
					csvFile.append(report.getStrRuleDescription());
					csvFile.append(",");
					csvFile.append(env.getProperty("rules.report."+ruletype+".status.value."+report.getStrRuleStatus()));
					csvFile.append(",");
					csvFile.append(report.getStrCompany());
					csvFile.append(",");
					csvFile.append(report.getStrBranch());
					csvFile.append(",");
					csvFile.append(report.getStrDocumenttype());
					csvFile.append(",");
					csvFile.append(env.getProperty("rules.partycode."+report.getStrPartyCode().toUpperCase()+".desc"));
					csvFile.append(",");
					csvFile.append(report.getStrPartyID());
					csvFile.append(",");
					csvFile.append(report.getStrGenSource());
					csvFile.append(",");
					csvFile.append(report.getStrRateOption());
					csvFile.append(",");
					csvFile.append("\""+report.getStrToRecipients()+"\"");
					csvFile.append(",");
					csvFile.append("\""+report.getStrCcRecipients()+"\"");
					csvFile.append(",");
					csvFile.append(dateFormat.format(report.getCalActivationDate().getTime()));
					csvFile.append(",");
					csvFile.append(report.getStrCreatedBy());
					csvFile.append(",");
					csvFile.append(dateFormat.format(report.getCalCreatedDate().getTime()));
					csvFile.append(",");
					csvFile.append(report.getStrModifiedBy());
					csvFile.append(",");
					csvFile.append(dateFormat.format(report.getCalModifiedDate().getTime()));
					csvFile.append("\n");
				}
				csvFile.flush();
				csvFile.close();
		} catch (Exception ex) {
			logger.error("RuleUtil.generateExcelReport() - Unable to generate excel file", ex);
		}
		return generatedFile;
	}
	public File generateExcellReportForExportRulesetups(List<RulesetupReportWrapper> rulesetupReportWrappers,File generatedFile,String ruletype)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyLocalizedPattern("yyyy-MMM-dd HH-mm-ss");
		
		Calendar today = Calendar.getInstance();
		try {
				
				String tempFilePath = env.getProperty("xml.file.location");
				String tempFolderPath = null;
								
				generatedFile = new File(tempFilePath+File.separator+ruletype.toUpperCase()+"_RULES_SETUP_REPORT_"+dateFormat.format(today.getTime())+".csv");
				
				FileWriter csvFile = new FileWriter(tempFilePath+File.separator+ruletype.toUpperCase()+"_RULES_SETUP_REPORT_"+dateFormat.format(today.getTime())+".csv");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.ruleid"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.company"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.branch"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.rulestatus"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.documenttype"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.partycode"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.partyID"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.gensource"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.rateoption"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.cronexpresion"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.protocol"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.activation"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.createdby"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.creationdate"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.modifiedby"));
				csvFile.append(",");
				csvFile.append(env.getProperty("rules.report."+ruletype+".header.modifieddate"));
				csvFile.append("\n");
				for (RulesetupReportWrapper report : rulesetupReportWrappers) {
					
					csvFile.append(report.getStrRuleID());
					csvFile.append(",");
					csvFile.append(report.getStrCompany());
					csvFile.append(",");
					csvFile.append(report.getStrBranch());
					csvFile.append(",");
					csvFile.append(env.getProperty("rules.report."+ruletype+".status.value."+report.getStrRuleStatus()));
					csvFile.append(",");
					csvFile.append(report.getStrDocumenttype());
					csvFile.append(",");
					csvFile.append(env.getProperty("rules.partycode."+report.getStrPartyCode().toUpperCase()+".desc"));
					csvFile.append(",");
					csvFile.append(report.getStrPartyID());
					csvFile.append(",");
					csvFile.append(report.getStrGenSource());
					csvFile.append(",");
					csvFile.append(report.getStrRateOption());
					csvFile.append(",");
					csvFile.append("\""+report.getStrCronExpression()+"\"");
					csvFile.append(",");
					csvFile.append("\""+report.getStrExportProtocol()+"\"");
					csvFile.append(",");
					csvFile.append(dateFormat.format(report.getCalActivationDate().getTime()));
					csvFile.append(",");
					csvFile.append(report.getStrCreatedBy());
					csvFile.append(",");
					csvFile.append(dateFormat.format(report.getCalCreatedDate().getTime()));
					csvFile.append(",");
					csvFile.append(report.getStrModifiedBy());
					csvFile.append(",");
					csvFile.append(dateFormat.format(report.getCalModifiedDate().getTime()));
					csvFile.append("\n");
				}
				csvFile.flush();
				csvFile.close();
		} catch (Exception ex) {
			logger.error("RuleUtil.generateExcelReport() - Unable to generate excel file", ex);
		}
		return generatedFile;
	}
	public File getGeneratedExcelFie(String exportQuery,File generatedFile)
	{
		String tempFilePath = env.getProperty("xml.file.location");
		generatedFile = new File(tempFilePath+File.separator+"temp_query_"+new Date().getTime()+".csv");
		FileWriter csvFile;
		try {
			csvFile = new FileWriter(tempFilePath+File.separator+"temp_query_"+new Date().getTime()+".csv");
			csvFile.append("Export Query");
			csvFile.append("\n");
			csvFile.append(exportQuery+"");
			csvFile.append("\n");
			csvFile.flush();
			csvFile.close();
		} catch (IOException e) {
			logger.error("RuleUtil.generateExcelReport() - Unable to generate excel file", e);
		}
		return generatedFile;
	}
	public File getGeneratedTxtFile(String exportQuery,File generatedFile)
	{
		String tempFilePath = env.getProperty("xml.file.location");
		generatedFile = new File(tempFilePath, "temp_query_"+new Date().getTime()+".txt");
		if (!generatedFile.exists()) {
		    try {
		    	generatedFile.createNewFile();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		try {
		    BufferedWriter bw = new BufferedWriter(new FileWriter(generatedFile.getAbsoluteFile()));
		    bw.write(exportQuery);
		    bw.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return generatedFile;
	}
	/**
	 * 
	 * @param string
	 * @param ch
	 * @param n
	 * @return
	 */
	private static String getSubstringAfterNthOccurance(String string,char ch,int n)
	{
		for(int i=0; i<n; i++)
		{
			string = string.substring(string.indexOf(ch) +1, string.length());
		}
		return string;
	}
	
}
