package com.agility.ddp.view.web;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.agility.ddp.core.components.ApplicationProperties;
import com.agility.ddp.core.components.DdpFTPClient;
import com.agility.ddp.core.components.DdpFTPSClient;
import com.agility.ddp.core.components.DdpSFTPClient;
import com.agility.ddp.core.components.DdpUNCClient;
import com.agility.ddp.core.logger.AuditLog;
import com.agility.ddp.core.quartz.DdpRuleSchedulerJob;
import com.agility.ddp.core.task.DdpCreateSchedulerTask;
import com.agility.ddp.core.task.DdpSchedulerJob;
import com.agility.ddp.core.util.CommonUtil;
import com.agility.ddp.core.util.SchedulerJobUtil;
import com.agility.ddp.dao.RuleDao;
import com.agility.ddp.data.domain.DdpBranch;
import com.agility.ddp.data.domain.DdpBranchService;
import com.agility.ddp.data.domain.DdpCommEmail;
import com.agility.ddp.data.domain.DdpCommEmailService;
import com.agility.ddp.data.domain.DdpCommFtp;
import com.agility.ddp.data.domain.DdpCommFtpService;
import com.agility.ddp.data.domain.DdpCommUnc;
import com.agility.ddp.data.domain.DdpCommUncService;
import com.agility.ddp.data.domain.DdpCommunicationSetup;
import com.agility.ddp.data.domain.DdpCompany;
import com.agility.ddp.data.domain.DdpCompanyService;
import com.agility.ddp.data.domain.DdpCompressionSetup;
import com.agility.ddp.data.domain.DdpDocnameConv;
import com.agility.ddp.data.domain.DdpDoctype;
import com.agility.ddp.data.domain.DdpDoctypeService;
import com.agility.ddp.data.domain.DdpExportQuery;
import com.agility.ddp.data.domain.DdpExportQueryService;
import com.agility.ddp.data.domain.DdpExportQueryUi;
import com.agility.ddp.data.domain.DdpExportQueryUiService;
import com.agility.ddp.data.domain.DdpExportRule;
import com.agility.ddp.data.domain.DdpExportVersionLkpService;
import com.agility.ddp.data.domain.DdpExportVersionSetup;
import com.agility.ddp.data.domain.DdpExportVersionSetupService;
import com.agility.ddp.data.domain.DdpGenSourceLkpService;
import com.agility.ddp.data.domain.DdpGenSourceSetup;
import com.agility.ddp.data.domain.DdpGenSourceSetupService;
import com.agility.ddp.data.domain.DdpNotification;
import com.agility.ddp.data.domain.DdpParty;
import com.agility.ddp.data.domain.DdpPartyService;
import com.agility.ddp.data.domain.DdpRateSetup;
import com.agility.ddp.data.domain.DdpRateSetupService;
import com.agility.ddp.data.domain.DdpRule;
import com.agility.ddp.data.domain.DdpRuleDetail;
import com.agility.ddp.data.domain.DdpRuleDetailService;
import com.agility.ddp.data.domain.DdpScheduler;
import com.agility.ddp.data.domain.DdpUser;
import com.agility.ddp.data.domain.DdpUserService;
import com.agility.ddp.view.util.ApplicationUIProperties;
import com.agility.ddp.view.util.Constant;
import com.agility.ddp.view.util.CreateMailXML;
import com.agility.ddp.view.util.ExportRuleWrapper;
import com.agility.ddp.view.util.RuleUtil;
import com.agility.ddp.view.util.SchedulerRuleWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.hibernate.validator.internal.xml.GetterType;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;


@RooWebJson(jsonObject = DdpExportRule.class)
@Controller
@RequestMapping("/ddpexportrules/list")
@RooWebScaffold(path = "ddpexportrules", formBackingObject = DdpExportRule.class)
public class DdpExportRuleController {
	
	private static final Logger logger = LoggerFactory.getLogger(DdpExportRuleController.class);
	
	@Autowired
	DdpCompanyService ddpCompanyService;
	
	@Autowired
	DdpBranchService ddpBranchService;
	
	@Autowired
	DdpPartyService ddpPartyService;
	
	@Autowired
	DdpDoctypeService ddpDoctypeService;
	
	@Autowired
	DdpCommFtpService ddpCommFtpService;
	
	@Autowired
	DdpCommUncService ddpCommUncService;
	
	@Autowired
	DdpGenSourceLkpService ddpGenSourceLkpService;
	
	@Autowired
	DdpRuleDetailService ddpRuleDetailService;
	
	@Autowired
	DdpGenSourceSetupService ddpGenSourceSetupService;
	
	@Autowired
	DdpExportVersionSetupService ddpExportVersionSetupService;
	
	@Autowired
	DdpRateSetupService ddpRateSetupService;
	
	@Autowired
	DdpExportVersionLkpService ddpExportVersionLkpService;
	
	@Autowired
	DdpExportQueryService ddpExportQueryService;
	
	@Autowired
	DdpExportQueryUiService ddpExportQueryUiService;
	
	@Autowired
	DdpCommEmailService ddpCommEmailService;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ApplicationUIProperties env;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	RuleUtil ruleUtil;
	
	@Autowired
	DdpUserService ddpUserService; 
	
	@Autowired
	private CommonUtil commonUtil;
	
	public Calendar currentDate ; 

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "mediator",method=RequestMethod.GET)
	public String mediatorController(Model uiModel)
	{
		logger.info("DdpExportRuleController.mediatorController Method Invoked.");
    	String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
    	Integer activeFlag = 1;
    	DdpUser ddpUser = null;
    	List<DdpUser> ddpUsersLst = ruleUtil.checkUserExististance(strUserName);
		if( ! ddpUsersLst.isEmpty())
		{
			ddpUser = ddpUsersLst.get(0);
			activeFlag = 0;
			//check user company and status
			DdpCompany company=ddpCompanyService.findDdpCompany(ddpUser.getUsrCompanyCode().toString());
			if(company == null || company.getComStatus() == 1 )
			{
				activeFlag = -1;
			}
		}
    	if(activeFlag==0)
    	{
    		List<String> listCompanies = new ArrayList<String>();
    		List<DdpCompany> ddpCompanies = ruleUtil.getAccessibleCompaniesLst(strUserName,env.getProperty("rule.exp.firstchars"));
    		 for(DdpCompany company: ddpCompanies ){
    			 listCompanies.add(company.getComCompanyCode());
    	    }
    		ddpCompanies.removeAll(ruleUtil.getListOfReadOnlyAccessibleCompanies(strUserName, env.getProperty("rule.exp.firstchars")));
			//Get the branches for the company
			List<DdpBranch>  branchs =  ruleUtil.findBranchbyCompany(ddpUser.getUsrCompanyCode().toString());
			List<String> listBranches = new ArrayList<String>();
			for(DdpBranch strbrn :branchs){
				listBranches.add(strbrn.getBrnBranchCode());	
			}
			//userCompanies.add(userCompany);
			uiModel.addAttribute("accessToCreate",ddpCompanies.size());
			uiModel.addAttribute("userCompany",listCompanies);
			uiModel.addAttribute("userBranch",listBranches);
			
    	}
    	else if(activeFlag==-1)
    	{
    		uiModel.addAttribute("activeFlag","User Country");
    	}
    	else
    	{
    		uiModel.addAttribute("activeFlag","User");
    	}
    	logger.info("DdpExportRuleController.mediatorController Method Executed Successfully.");
		return "ddpexportrules/display";
	}
	
	/**
	 * 
	 * @param search
	 * @param page
	 * @param size
	 * @param filters
	 * @param sort
	 * @param sortdir
	 * @return
	 */
	@RequestMapping(value="listExportRules",headers="Accept=Application/json",method=RequestMethod.GET)
	@ResponseBody
	public Map listExportRules(@RequestParam(value = "_search", required = false) String search,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "filters", required = false) String filters,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "sortdir", required = false) String sortdir)
	{
		
		logger.info("DdpExportRuleController.listExportRules Method Invoked.");
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<Object> records = new ArrayList<Object>(50);
		List<DdpExportRule> result = null;
		List<ExportRuleWrapper> ExportRuleWrappers = new ArrayList<ExportRuleWrapper>();
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json;charset=utf-8");
		String[] compnies ;
		String userGroup = "";
		String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		List<String> authoritiesLst = new ArrayList<String>();
		Set<String> multiCountries = new HashSet<String>();
		for(GrantedAuthority authority:authorities)
		{
			authoritiesLst.add(authority.getAuthority());
			if(authority.getAuthority().toLowerCase().startsWith(env.getProperty("rule.exp.firstchars").toLowerCase()+"_") ||
					  authority.getAuthority().toLowerCase().startsWith(env.getProperty("role_sub_all").toLowerCase()+"_") )
			{
				String strCompany = authority.getAuthority().split("_")[1];
				DdpCompany company = ddpCompanyService.findDdpCompany(strCompany);
				if(company != null && company.getComStatus()==0 )
					multiCountries.add(strCompany.toUpperCase()); 
			}
			if(authority.getAuthority().toLowerCase().startsWith(env.getProperty("Read_only_start").toLowerCase()))
			{
				if(authority.getAuthority().split("_")[2].equalsIgnoreCase(env.getProperty("read_only_region_sub")))
				  {
					  if(authority.getAuthority().split("_")[4].equalsIgnoreCase(env.getProperty("rule.exp.firstchars")) || 
							  authority.getAuthority().split("_")[4].equalsIgnoreCase(env.getProperty("role_sub_all")))
					  {
						  List<DdpCompany> readonlycompanies = new ArrayList<DdpCompany>();
						  try{
							  readonlycompanies = ruleUtil.getCompaniesByRegionJdbcTemplate(authority.getAuthority().split("_")[3]);
						  }catch(Exception ex){ logger.error("Unable to find the Region {}",ex); }
						  for(DdpCompany company:readonlycompanies)
							  multiCountries.add(company.getComCompanyCode());
					  }
				  }
				  else
				  {
						if(authority.getAuthority().toLowerCase().split("_")[3].equalsIgnoreCase(env.getProperty("rule.exp.firstchars")) || 
								  authority.getAuthority().split("_")[3].equalsIgnoreCase(env.getProperty("role_sub_all")) )
						{
							multiCountries.add(authority.getAuthority().toUpperCase().split("_")[2].trim()); 
						}
				  }
			}
		}
		if(authoritiesLst.contains(env.getProperty("Admin_Role")))
		{
			ExportRuleWrappers = getAllActiveExportRules("admin_role", null, null,authoritiesLst,null);
		}
		else
		{
			if(authoritiesLst.contains(env.getProperty("Region_Role")))
	    	{ 
	        	String userRegion = ruleUtil.getUserRegion(strUserName);
	        	ExportRuleWrappers.addAll(getAllActiveExportRules("Region_role",userRegion,null,authoritiesLst,null));
	    	}
	    	if(! multiCountries.isEmpty())
	    	{
	    		compnies = new String[multiCountries.size()+1];
	    		Iterator<String> iterator = multiCountries.iterator();
	    		int i=0;
	    		while(iterator.hasNext())
	    		{
	    			compnies[i] = iterator.next();
	    			i++;
	    		}
	    		String userCompany = ruleUtil.getUserCompany(strUserName);
	    		compnies[multiCountries.size()]=userCompany;
	    		ExportRuleWrappers.addAll(getAllActiveExportRules("multi",null,null,authoritiesLst,compnies));
	    	}
	    	else
	    	{
	    		String userCompany = ruleUtil.getUserCompany(strUserName);
	        	compnies = new String[1];
	        	compnies[0] = userCompany;
	        	ExportRuleWrappers.addAll(getAllActiveExportRules("local",null,userCompany,authoritiesLst,null));
	    	}  
	    	
	    	 if(authoritiesLst.contains(env.getProperty("export.rulebyquery.role")))
			   {
	    		 ExportRuleWrappers.addAll(getAllExportRulesByQuery());
			   }
			   if(authoritiesLst.contains(env.getProperty("export.rulebyquery.property.role")))
			   {
				   ExportRuleWrappers.addAll(getAllExportRulesByQueryWithProps());
			   }
	    	if(! ExportRuleWrappers.isEmpty())
			  {
				  Map<String,ExportRuleWrapper> exportRuleWrapperMap = new LinkedHashMap<String,ExportRuleWrapper>();
			         for(ExportRuleWrapper exportRuleWrapper:ExportRuleWrappers)
			        	 exportRuleWrapperMap.put(exportRuleWrapper.getExpRuleId(), exportRuleWrapper);
			         ExportRuleWrappers.clear();
			         ExportRuleWrappers.addAll(exportRuleWrapperMap.values());
			  }
		}
			  
			  
			  
			 
		HashMap<Object, Object>[] rowdata = new HashMap[ExportRuleWrappers.size()];
		map.put("rows",ExportRuleWrappers);
		map.put("page", page);
		logger.info("DdpExportRuleController.listExportRules Method Executed Successfully.");
		return map;
	}
	
	@RequestMapping(value = "/list/{expRuleId}", produces = "text/html")
    public String show(@PathVariable("expRuleId") Integer expRuleId, Model uiModel) {
		logger.info("DdpExportRuleController.show Method Invoked.");
        addDateTimeFormatPatterns(uiModel);
        DdpExportRule ddpExportRule = ddpExportRuleService.findDdpExportRule(expRuleId);
        DdpRule ddpRule = ddpRuleService.findDdpRule(expRuleId);
        uiModel.addAttribute("expRule",ddpExportRule);
        uiModel.addAttribute("itemId", expRuleId);
        uiModel.addAttribute("itemName", ddpRule.getRulDescription());
        uiModel.addAttribute("commConfig", ddpExportRule.getExpCommunicationId().getCmsCommunicationProtocol());
        int restrictQuickLinks = 0; 
        List<DdpCompany> userCompanies = new ArrayList<DdpCompany>();
        String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
        userCompanies = ruleUtil.getAccessibleCompaniesLst(strUserName, env.getProperty("rule.exp.firstchars"));
        List<DdpCompany> readonlycompanies = ruleUtil.getListOfReadOnlyAccessibleCompanies(strUserName, env.getProperty("rule.exp.firstchars"));
        userCompanies.removeAll(readonlycompanies);
        DdpCommunicationSetup communicationSetup = ddpExportRule.getExpCommunicationId();
        
        String cronExpression = ddpExportRule.getExpSchedulerId().getSchCronExpressions();
        String description = ""+cronExpression;
        uiModel.addAttribute("scheduler", description);
        
    	//check protocol
        List<RuleDao> communicationDetails = new ArrayList<RuleDao>();
        RuleDao ruleDao = new RuleDao();
        communicationDetails.add(getCommunicationDetais(communicationSetup.getCmsCommunicationProtocol(),communicationSetup.getCmsProtocolSettingsId()));
        if(communicationSetup.getCmsCommunicationProtocol2() != null)
        	communicationDetails.add(getCommunicationDetais(communicationSetup.getCmsCommunicationProtocol2(),communicationSetup.getCmsProtocolSettingsId2()));
        if(communicationSetup.getCmsCommunicationProtocol3() != null)
        	communicationDetails.add(getCommunicationDetais(communicationSetup.getCmsCommunicationProtocol3(),communicationSetup.getCmsProtocolSettingsId3()));
    	uiModel.addAttribute("communicationDetails", communicationDetails); // Communication Details
		if(ddpExportRule.getExpSchedulerId().getSchChoosenType() == null || ddpExportRule.getExpSchedulerId().getSchChoosenType() == 0) //Rule Details available
        {
			List<RuleDao> ruleDaos =  getRuleDetailForRuleIdShow(expRuleId);
            uiModel.addAttribute("ddpexportrule", ruleDaos);
            if(readonlycompanies.contains(ddpCompanyService.findDdpCompany(ruleDaos.get(0).getRdtCompanyCode())))
        		restrictQuickLinks = 1;
        }
        else
        {
        	uiModel.addAttribute("ddpexportrule", "none");
        	//Document Details reading from Properties file.
        }
		uiModel.addAttribute("restrictQuickLinks",restrictQuickLinks);
		uiModel.addAttribute("accessToCreate",userCompanies.size());
		logger.info("DdpExportRuleController.show Method Executed Successfully.");
        return "ddpexportrules/show";
    }
	
	public RuleDao getCommunicationDetais(String protocol,String protocolSettingsID)
	{
		RuleDao ruleDao = new RuleDao();
		ruleDao.setCommProtocol(protocol);
		if(protocol.equalsIgnoreCase("FTP"))
    	{
    		DdpCommFtp commFtp =  ddpCommFtpService.findDdpCommFtp(Long.parseLong(protocolSettingsID));
        	ruleDao.setFtpLocation((commFtp==null)?"":commFtp.getCftFtpLocation());
        	ruleDao.setFtpUsername((commFtp==null)?"":commFtp.getCftFtpUserName());
        	ruleDao.setFtpPassword((commFtp==null)?"":commFtp.getCftFtpPassword());
        	ruleDao.setFtpPort((commFtp==null)?0:commFtp.getCftFtpPort());
    	}
    	else if(protocol.equalsIgnoreCase("UNC"))
    	{
    		DdpCommUnc commUnc = ddpCommUncService.findDdpCommUnc(Long.parseLong(protocolSettingsID));
    		ruleDao.setUncPath( (commUnc==null) ? "" : commUnc.getCunUncPath() );
    		ruleDao.setUncUsername((commUnc==null) ? "" : commUnc.getCunUncUserName() );
    		ruleDao.setUncPassword((commUnc==null) ? "" : commUnc.getCunUncPassword() );
    	}
    	else if(protocol.equalsIgnoreCase("SMTP"))
    	{
    		DdpCommEmail commEmail = ddpCommEmailService.findDdpCommEmail(Integer.parseInt(protocolSettingsID));
    		ruleDao.setCemEmailTo(commEmail.getCemEmailTo());
    		ruleDao.setCemEmailCc(commEmail.getCemEmailCc());
    	}
		return ruleDao;
	}
	
	@RequestMapping(value = "/onDemandShow/{expRuleId}", produces = "text/html")
    public String onDemandshow(@PathVariable("expRuleId") Integer expRuleId, Model uiModel) {
		
		logger.info("DdpExportRuleController.onDemandshow Method Invoked.");
       // addDateTimeFormatPatterns(uiModel);
        OnDemandExportWrapper onDemandExportWrapper = new OnDemandExportWrapper();
        populateDateForOnDemandService(uiModel,onDemandExportWrapper,expRuleId);
        logger.info("DdpExportRuleController.onDemandshow Method Executed Successfully.");
        return "ddpexportrules/onDemandExport";
    }

	private void populateDateForOnDemandService(Model uiModel, OnDemandExportWrapper onDemandExportWrapper,Integer expRuleId) {
		
			DdpExportRule ddpExportRule = ddpExportRuleService.findDdpExportRule(expRuleId);
	        DdpRule ddpRule = ddpRuleService.findDdpRule(expRuleId);
	        uiModel.addAttribute("expRule",ddpExportRule);
	        uiModel.addAttribute("itemId", expRuleId);
	        uiModel.addAttribute("itemName", ddpRule.getRulDescription());	
	        uiModel.addAttribute("commConfig", ddpExportRule.getExpCommunicationId().getCmsCommunicationProtocol());
	        RuleDao ruleDao = new RuleDao();
	        DdpCommunicationSetup communicationSetup = ddpExportRule.getExpCommunicationId();
	        if(communicationSetup.getCmsCommunicationProtocol().equalsIgnoreCase("FTP"))
	    	{
	    		DdpCommFtp commFtp =  ddpCommFtpService.findDdpCommFtp(Long.parseLong(communicationSetup.getCmsProtocolSettingsId()));
	        	ruleDao.setFtpLocation((commFtp==null)?"":commFtp.getCftFtpLocation());
	        	ruleDao.setFtpUsername((commFtp==null)?"":commFtp.getCftFtpUserName());
	        	ruleDao.setFtpPassword((commFtp==null)?"":commFtp.getCftFtpPassword());
	        	ruleDao.setFtpPort((commFtp==null)?0:commFtp.getCftFtpPort());
	    	}
	    	else if(communicationSetup.getCmsCommunicationProtocol().equalsIgnoreCase("UNC"))
	    	{
	    		DdpCommUnc commUnc = ddpCommUncService.findDdpCommUnc(Long.parseLong(communicationSetup.getCmsProtocolSettingsId()));
	    		ruleDao.setUncPath( (commUnc==null) ? "" : commUnc.getCunUncPath() );
	    		ruleDao.setUncUsername((commUnc==null) ? "" : commUnc.getCunUncUserName() );
	    		ruleDao.setUncPassword((commUnc==null) ? "" : commUnc.getCunUncPassword() );
	    	}
	    	else if(communicationSetup.getCmsCommunicationProtocol().equalsIgnoreCase("SMTP"))
	    	{
	    		DdpCommEmail commEmail = ddpCommEmailService.findDdpCommEmail(Integer.parseInt(communicationSetup.getCmsProtocolSettingsId()));
	    		ruleDao.setCemEmailTo(commEmail.getCemEmailTo());
	    		ruleDao.setCemEmailCc(commEmail.getCemEmailCc());
	    	}
	    	uiModel.addAttribute("communicationDetails", ruleDao); // Communication Details
	    	if(ddpExportRule.getExpSchedulerId().getSchChoosenType() == null || ddpExportRule.getExpSchedulerId().getSchChoosenType() == 0) //Rule Details available
	        {
				List<RuleDao> ruleDaos =  getRuleDetailForRuleIdShow(expRuleId);
	            uiModel.addAttribute("ddpexportrule", ruleDaos);
	        }
	        else
	        {
	        	uiModel.addAttribute("ddpexportrule", "none");
	        	//Document Details reading from Properties file.
	        }
	        
	        uiModel.addAttribute("onDemandExportWrapper", onDemandExportWrapper);
	       // uiModel.addAttribute("ddp_export_from_date", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
	       // uiModel.addAttribute("ddp_export_to_date", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
	        uiModel.addAttribute("standard_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
	}
	
	@RequestMapping(value = "form", produces = "text/html")
    public String createForm(@RequestParam(value="customerId",required=false) String customerId,Model uiModel,HttpServletRequest request) {
		String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
		// Get the logged in user company
        String userCompany = ruleUtil.getUserCompany(strUserName);
        RuleWrapper ruleWrapper = new RuleWrapper();
        List<DdpCommFtp> commFtps = new ArrayList<DdpCommFtp>();
        List<DdpCommUnc> commUncs = new ArrayList<DdpCommUnc>();
        List<String> ftpFolders = new ArrayList<String>();
        for(int i = 0;i<3;i++)
        {
        	commFtps.add(new DdpCommFtp());
        	commUncs.add(new DdpCommUnc());
        	ftpFolders.add(new String());
        }
        ruleWrapper.setDdpCommFtp(commFtps);
        ruleWrapper.setDdpCommUnc(commUncs);
        ruleWrapper.setFtpFolder(ftpFolders);
        if(customerId != null)
        {
        	uiModel.addAttribute("duplicateCustID",customerId);
        }
        ruleWrapper.getDdpExportRule().setExpActivationDate(Calendar.getInstance());
        uiModel.addAttribute("rulewrapper", ruleWrapper);
        populateEditForm(uiModel, new DdpExportRule(),userCompany,"createForm");
        return "ddpexportrules/create";
    }
	
	@RequestMapping(value="/template",params = "form", produces = "text/html")
    public String templateForm(@RequestParam(value="customerId",required=false) String customerId,Model uiModel,HttpServletRequest request) {
		String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
		// Get the logged in user company
        String userCompany = ruleUtil.getUserCompany(strUserName);
        RuleWrapper ruleWrapper = new RuleWrapper();
        if(customerId != null)
        {
        	uiModel.addAttribute("duplicateCustID",customerId);
        }
        uiModel.addAttribute("rulewrapper", ruleWrapper);
        populateEditForm(uiModel, new DdpExportRule(),userCompany,"templateForm");
        return "ddpexportrules/template";
    }
	@RequestMapping(value="/branchByCountry",method=RequestMethod.POST,produces="text/html")
	public String branchByCountry(Model uiModel,HttpServletRequest httpServletRequest)
	{
		String companyId = httpServletRequest.getParameter("companyID");
		uiModel.addAttribute("rulewrapper", new RuleWrapper());
		 populateEditForm(uiModel, new DdpExportRule(),companyId.toString(),"branchByCountry");
		 return "ddpexportrules/create";
	}
	
	@RequestMapping(value="/branchByCountryAsList",method=RequestMethod.POST,headers="Accept=Application/json")
	@ResponseBody
	public List<DdpBranch> branchByCountryAsList(Model uiModel,HttpServletRequest httpServletRequest)
	{
		String companyId = httpServletRequest.getParameter("companyID");
		List<DdpBranch> branches = ruleUtil.findBranchbyCompany(companyId);
		return branches;
	}
	@RequestMapping(value="/branchByCountryAsString",method=RequestMethod.POST,headers="Accept=Application/json")
	@ResponseBody
	public String branchByCountryAsString(HttpServletRequest httpServletRequest)
	{
		String companyId = httpServletRequest.getParameter("companyID");
		List<DdpBranch> branches = ruleUtil.findBranchbyCompany(companyId);
		String res = "";
	   	 for(DdpBranch branch:branches){
	   		 res+=branch.getBrnBranchCode()+",";
	   	 }
	   	 res = res.substring(0, res.lastIndexOf(","));
		 return res;
	}
	
	@AuditLog(message = "Export Rule Created.")
	@Transactional
	@RequestMapping(params={"create"},method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid RuleWrapper ruleWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		
		logger.info("DdpExportRuleController.create Method Invoked.");
		String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
		// Get the logged in user company
        String userCompany = ruleUtil.getUserCompany(strUserName);
        DdpExportRule ddpExportRule=null;
		if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpExportRule,userCompany,"create");
            return "ddpexportrules/create";
        }
		String rowCount = httpServletRequest.getParameter("rowscount");
		LinkedList<Object> docList = new LinkedList<Object>();
		LinkedList<Object> genSourceList = new LinkedList<Object>();
		 LinkedList<Object> rateList = new LinkedList<Object>();
		LinkedList<Object> exportVersionList = new LinkedList<Object>();
		LinkedList<Object> relevantTypesList = new LinkedList<Object>();
		LinkedList<Object> reqFlagList = new LinkedList<Object>();
		LinkedList<Object> sequenceList = new LinkedList<Object>();
		int rCount = Integer.parseInt(rowCount);
        String docTypefalg = null;
        String genSourcefalg = null;
        String ratefalg = null;
        String exportVersionfalg = null;
        String relevantTypesfalg = null;
        String reqFlagfalg = null;
        String sequencefalg = null;
        for (int i = 1; i <= rCount ; i++) {
            int count = i;
            docTypefalg = httpServletRequest.getParameter("selectDdpDoctype" + count);
            genSourcefalg = httpServletRequest.getParameter("selectDdpGenSource"+ count);
            ratefalg = httpServletRequest.getParameter("selectDdpRate"+ count);
            exportVersionfalg = httpServletRequest.getParameter("selectVersion"+ count);
            relevantTypesfalg = httpServletRequest.getParameter("selectRelType"+ count);
            reqFlagfalg = httpServletRequest.getParameter("selectReqFlag"+ count);
            sequencefalg = httpServletRequest.getParameter("selectSequence"+ count);
            if (docTypefalg != null) {
                docList.add(docTypefalg);
                genSourceList.add(genSourcefalg);
                rateList.add(ratefalg);
                exportVersionList.add(exportVersionfalg);
                relevantTypesList.add(relevantTypesfalg);
                reqFlagList.add(reqFlagfalg);
                sequenceList.add(sequencefalg);
                rCount = rCount + 1;
            }
        }
        
        String[] selectedBranchList = httpServletRequest.getParameterValues("lstBranch");
		//check whether Custom Export Rule
		String ruleCategory = httpServletRequest.getParameter("radioButtonforRuleType");
		//reading Configuration FTP/UNC
		String communicationType0 = httpServletRequest.getParameter("radioButton0");
		String communicationType1 = httpServletRequest.getParameter("radioButton1");
		String communicationType2 = httpServletRequest.getParameter("radioButton2");
		List<String> ftpFolder = ruleWrapper.getFtpFolder();
		String everyMinute = httpServletRequest.getParameter("minutespan");
		String everyHour = httpServletRequest.getParameter("hourspan");
		String dayofMonth = httpServletRequest.getParameter("dayOfMonth");
		String dayofWeek = httpServletRequest.getParameter("dayOfWeek");
		String hours = httpServletRequest.getParameter("hour");
		String mins = httpServletRequest.getParameter("minute");
		String weekflag= httpServletRequest.getParameter("weekflag");
		String monthflag= httpServletRequest.getParameter("monthflag");
		String cronExpressionFreqReport = "" ;
		String schEmailTo = "";
		String schEmailCc = "";
		if( ! httpServletRequest.getParameter("frequencyFreqReport").equals("none")){
			String everyMinuteFreqReport = httpServletRequest.getParameter("minutespanFreqReport");
			String everyHourFreqReport = httpServletRequest.getParameter("hourspanFreqReport");
			String dayofMonthFreqReport = httpServletRequest.getParameter("dayOfMonthFreqReport");
			String dayofWeekFreqReport = httpServletRequest.getParameter("dayOfWeekFreqReport");
			String hoursFreqReport = httpServletRequest.getParameter("hourFreqReport");
			String minsFreqReport = httpServletRequest.getParameter("minuteFreqReport");
			String weekflagFreqReport = httpServletRequest.getParameter("weekflagFreqReport");
			String monthflagFreqReport = httpServletRequest.getParameter("monthflagFreqReport");
			if(weekflagFreqReport.equals("0") && monthflagFreqReport.equals("0"))
			{
				dayofMonthFreqReport="*";
				dayofWeekFreqReport="?";
			}
			if(weekflagFreqReport.equals("0") && monthflagFreqReport.equals("1"))
			{
				dayofWeekFreqReport="?";
			}
			if(monthflagFreqReport.equals("0") && weekflagFreqReport.equals("1"))
			{
				dayofMonthFreqReport="?";
			}
			if(! everyMinuteFreqReport.equals("0"))
				minsFreqReport=minsFreqReport+"/"+everyMinuteFreqReport;
			if(! everyHourFreqReport.equals("0"))
				hoursFreqReport=hoursFreqReport+"/"+everyHourFreqReport;
			
			cronExpressionFreqReport = "0 "+minsFreqReport+" "+hoursFreqReport+" "+dayofMonthFreqReport+" * "+dayofWeekFreqReport;
			schEmailTo = ruleWrapper.getSchReportEmailTo().trim();
			schEmailCc = ruleWrapper.getSchReportEmailCc().trim();
		}
		String custompartyId = httpServletRequest.getParameter("customeClientId");
		Integer docChoosenType = Integer.parseInt(httpServletRequest.getParameter("selDocSel"));
		int exportQuerySource = 0; // 0 : from properties ; 1 : from UI ; 2 : from Text Area
		String mailBody = "";
        DdpCompany ddpCompany = null;
		String ruleName = "";
		String ruleDescription = "";
		List<String> brnList = new ArrayList<String>();
		if(! ruleCategory.equalsIgnoreCase("byQryRB"))
		{
			docChoosenType = 0;
			boolean flagAllBranches = false;
	        // Check for the All branch selected, if selected get all the branch and
	        // iterate the loop and save the rule details
	        for (String str : selectedBranchList)
	        {
	            if (str.equalsIgnoreCase("ALL")) 
	            {
	                flagAllBranches = true;
	                break;
	            }
	        }
	        brnList = Arrays.asList(selectedBranchList);
	        if (flagAllBranches == true) 
	        {
	            brnList = new ArrayList<String>();
	            brnList.add("ALL");
	        }
			ddpCompany = ddpCompanyService.findDdpCompany(ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode());
		    ruleName = ddpCompany.getComRegion() + "_" + ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode() + "_" + ruleWrapper.getDdpRuleDetail().getRdtPartyId();
		    if(ruleWrapper.getDdpRule().getRulDescription().equals(""))
	        	ruleDescription = ruleWrapper.getDdpRuleDetail().getRdtPartyId();
	        else
	        	ruleDescription = ruleWrapper.getDdpRule().getRulDescription();
		    
		    ruleDescription = ddpCompany.getComRegion() + "_" + ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode() + "_" + ruleDescription;
		    
		    //get partyId and check for duplicate Rule with same ClientID
	    	String clientId = ruleWrapper.getDdpRuleDetail().getRdtPartyId();
	    	String companycode = ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode();
	    	String partyCodeValue = ruleWrapper.getDdpRuleDetail().getRdtPartyCode().getPtyPartyCode();
	    	if (clientId != null && clientId.endsWith(","))
	    		clientId = clientId.substring(0, clientId.length()-1);
	    	String[] clients = clientId.split(",");
	    	for (String client : clients) {
	    		//check this party id is available or not.
	    		List<Integer> rids = getRuleIdsByPartyCode(client,companycode,brnList,docList,partyCodeValue,null);
		    	if(rids.size() > 0)
		    	{
		    		if (client.startsWith("#"))
		    			client = client.substring(1, client.length());
		    		return "redirect:/ddpexportrules/list/form?customerId="+client+"";
		    	}
	    	}
	    	String strPartyDetaiils = env.getProperty("email.export.create.confirm.body.partydetails");
	    	strPartyDetaiils = strPartyDetaiils.replaceAll("%%EXPORTCLIENTID%%", clientId);
	    	strPartyDetaiils = strPartyDetaiils.replaceAll("%%RULEDESCRIPTION%%", ruleDescription);
	    	mailBody = mailBody.concat(strPartyDetaiils);
	    	String strRegionDetails = env.getProperty("email.export.create.confirm.body.regiondetails");
	    	strRegionDetails = strRegionDetails.replaceAll("%%EXPORTPARTYCODE%%", ruleWrapper.getDdpRuleDetail().getRdtPartyCode().getPtyPartyName());
	    	strRegionDetails = strRegionDetails.replaceAll("%%EXPORTCOMPANY%%", companycode);
	    	strRegionDetails = strRegionDetails.replaceAll("%%EXPORTBRANCH%%", brnList.toString());
	    	mailBody = mailBody.concat(strRegionDetails);
		}
		else
		{
			//check for duplicate Rule with same Custom ClientID.
    		List<Integer> rids = getRuleIdsByPartyCode(custompartyId);
	    	if(rids.size() > 0)
	    	{
	    		if (custompartyId.startsWith("#"))
	    			custompartyId = custompartyId.substring(1, custompartyId.length());
	    		return "redirect:/ddpexportrules/list/form?customerId="+custompartyId+"";
	    	}
			ruleName = "DDP_CUSTOM_"+custompartyId;
			ruleDescription = "DDP_"+custompartyId;
			if(! ruleWrapper.getDdpRule().getRulDescription().equals(""))
			{
				ruleDescription=ruleDescription+"_"+ruleWrapper.getDdpRule().getRulDescription();
			}
			String strPartyDetaiils = env.getProperty("email.export.create.confirm.body.partydetails");
			strPartyDetaiils = strPartyDetaiils.replaceAll("%%EXPORTCLIENTID%%", custompartyId);
			strPartyDetaiils = strPartyDetaiils.replaceAll("%%RULEDESCRIPTION%%", ruleDescription);
	    	mailBody = mailBody.concat(strPartyDetaiils);
		}
        
    	
		if(weekflag.equals("0") && monthflag.equals("0"))
		{
			dayofMonth="*";
			dayofWeek="?";
		}
		if(weekflag.equals("0") && monthflag.equals("1"))
		{
			dayofWeek="?";
		}
		if(monthflag.equals("0") && weekflag.equals("1"))
		{
			dayofMonth="?";
		}
		if(! everyMinute.equals("0"))
			mins=mins+"/"+everyMinute;
		if(! everyHour.equals("0"))
			hours=hours+"/"+everyHour;
		String cronExpression = "0 "+mins+" "+hours+" "+dayofMonth+" * "+dayofWeek;
		String strcommunicationdetails = env.getProperty("email.export.create.confirm.body.communicationdetails");
		String strExpCommDetails = "";
		//check communication type and store data into tables accordingly
        if(communicationType0.equalsIgnoreCase("ftpConfig"))
        {
			//save DdpCommFtp
        	String ftpserver = ruleWrapper.getDdpCommFtp().get(0).getCftFtpLocation();
        	String secureProtocol = httpServletRequest.getParameter("secureType0");
        	String location = getFtpLocation(ftpserver,ftpFolder.get(0),secureProtocol);
        	ruleWrapper.getDdpCommFtp().get(0).setCftFtpSecure(secureProtocol);
        	ruleWrapper.getDdpCommFtp().get(0).setCftFtpLocation(location);
			ddpCommFtpService.saveDdpCommFtp(ruleWrapper.getDdpCommFtp().get(0));
			Long commFtpId = ruleWrapper.getDdpCommFtp().get(0).getCftFtpId();
			ruleWrapper.getDdpCommunicationSetup().setCmsCommunicationProtocol("FTP");
			ruleWrapper.getDdpCommunicationSetup().setCmsProtocolSettingsId(commFtpId.toString());
			strExpCommDetails+="<tr><td>"+secureProtocol+"</td><td>"+location+"</td></tr>";
        }
        else if(communicationType0.equalsIgnoreCase("uncConfig"))
        {
        	DdpCommUnc ddpCommUnc = ruleWrapper.getDdpCommUnc().get(0);
        	String uncPath = getUncPath(ddpCommUnc.getCunUncPath());
        	ddpCommUnc.setCunUncPath(uncPath);
        	//save DdpCommUnc
        	ddpCommUncService.saveDdpCommUnc(ddpCommUnc);
        	Long commUncId = ddpCommUnc.getCunUncId();
        	ruleWrapper.getDdpCommunicationSetup().setCmsCommunicationProtocol("UNC");
        	ruleWrapper.getDdpCommunicationSetup().setCmsProtocolSettingsId(commUncId.toString());
        	strExpCommDetails+="<tr><td>UNC</td><td>"+uncPath+"</td></tr>";
        }
        else if(communicationType0.equalsIgnoreCase("emailConfig"))
        {
        	DdpCommEmail commEmail = ruleWrapper.getDdpCommEmail();
        	ddpCommEmailService.saveDdpCommEmail(commEmail);
        	ruleWrapper.getDdpCommunicationSetup().setCmsCommunicationProtocol("SMTP");
        	ruleWrapper.getDdpCommunicationSetup().setCmsProtocolSettingsId(commEmail.getCemEmailId().toString());
        	strExpCommDetails+="<tr><td>SMTP</td><td>"+commEmail.getCemEmailTo()+"</td></tr>";
        }
        if(httpServletRequest.getParameter("isDiv1Selected").equals("0"))
		{
        	 if(communicationType1.equalsIgnoreCase("ftpConfig"))
             {
     			//save DdpCommFtp
             	String ftpserver = ruleWrapper.getDdpCommFtp().get(1).getCftFtpLocation();
             	String secureProtocol = httpServletRequest.getParameter("secureType1");
             	String location = getFtpLocation(ftpserver,ftpFolder.get(1),secureProtocol);
             	ruleWrapper.getDdpCommFtp().get(1).setCftFtpSecure(secureProtocol);
             	ruleWrapper.getDdpCommFtp().get(1).setCftFtpLocation(location);
     			ddpCommFtpService.saveDdpCommFtp(ruleWrapper.getDdpCommFtp().get(1));
     			Long commFtpId = ruleWrapper.getDdpCommFtp().get(1).getCftFtpId();
     			ruleWrapper.getDdpCommunicationSetup().setCmsCommunicationProtocol2("FTP");
     			ruleWrapper.getDdpCommunicationSetup().setCmsProtocolSettingsId2(commFtpId.toString());
     			strExpCommDetails+="<tr><td>"+secureProtocol+"</td><td>"+location+"</td></tr>";
             }
             else if(communicationType1.equalsIgnoreCase("uncConfig"))
             {
             	DdpCommUnc ddpCommUnc = ruleWrapper.getDdpCommUnc().get(1);
             	String uncPath = getUncPath(ddpCommUnc.getCunUncPath());
             	ddpCommUnc.setCunUncPath(uncPath);
             	//save DdpCommUnc
             	ddpCommUncService.saveDdpCommUnc(ddpCommUnc);
             	Long commUncId = ddpCommUnc.getCunUncId();
             	ruleWrapper.getDdpCommunicationSetup().setCmsCommunicationProtocol2("UNC");
             	ruleWrapper.getDdpCommunicationSetup().setCmsProtocolSettingsId2(commUncId.toString());
             	strExpCommDetails+="<tr><td>UNC</td><td>"+uncPath+"</td></tr>";
             }
		}
        if(httpServletRequest.getParameter("isDiv2Selected").equals("0"))
		{
        	if(communicationType2.equalsIgnoreCase("ftpConfig"))
            {
    			//save DdpCommFtp
            	String ftpserver = ruleWrapper.getDdpCommFtp().get(2).getCftFtpLocation();
            	String secureProtocol = httpServletRequest.getParameter("secureType2");
            	String location = getFtpLocation(ftpserver,ftpFolder.get(2),secureProtocol);
            	ruleWrapper.getDdpCommFtp().get(2).setCftFtpSecure(secureProtocol);
            	ruleWrapper.getDdpCommFtp().get(2).setCftFtpLocation(location);
    			ddpCommFtpService.saveDdpCommFtp(ruleWrapper.getDdpCommFtp().get(2));
    			Long commFtpId = ruleWrapper.getDdpCommFtp().get(2).getCftFtpId();
    			ruleWrapper.getDdpCommunicationSetup().setCmsCommunicationProtocol3("FTP");
    			ruleWrapper.getDdpCommunicationSetup().setCmsProtocolSettingsId3(commFtpId.toString());
    			strExpCommDetails+="<tr><td>"+secureProtocol+"</td><td>"+location+"</td></tr>";
            }
            else if(communicationType2.equalsIgnoreCase("uncConfig"))
            {
            	DdpCommUnc ddpCommUnc = ruleWrapper.getDdpCommUnc().get(2);
            	String uncPath = getUncPath(ddpCommUnc.getCunUncPath());
            	ddpCommUnc.setCunUncPath(uncPath);
            	//save DdpCommUnc
            	ddpCommUncService.saveDdpCommUnc(ddpCommUnc);
            	Long commUncId = ddpCommUnc.getCunUncId();
            	ruleWrapper.getDdpCommunicationSetup().setCmsCommunicationProtocol3("UNC");
            	ruleWrapper.getDdpCommunicationSetup().setCmsProtocolSettingsId3(commUncId.toString());
            	strExpCommDetails+="<tr><td>UNC</td><td>"+uncPath+"</td></tr>";
            }
		}
        strcommunicationdetails = strcommunicationdetails.replaceAll("%%EXPORTCOMMDETAILS%%", strExpCommDetails);
        strcommunicationdetails = strcommunicationdetails.replaceAll("%%EXPORTCRONEXPRESSION%%", cronExpression);
        mailBody = mailBody.concat(strcommunicationdetails);
        
		//save DdpCommunicationSetup
		currentDate = Calendar.getInstance();
		//ruleWrapper.getDdpCommunicationSetup().setCmsCommunicationProtocol("FTP");
		ruleWrapper.getDdpCommunicationSetup().setCmsCreatedBy(strUserName);
		ruleWrapper.getDdpCommunicationSetup().setCmsCreatedDate(currentDate);
		ruleWrapper.getDdpCommunicationSetup().setCmsModifiedBy(strUserName);
		ruleWrapper.getDdpCommunicationSetup().setCmsModifiedDate(currentDate);
//		ruleWrapper.getDdpCommunicationSetup().setCmsProtocolSettingsId(commFtpId.toString());
		ruleWrapper.getDdpCommunicationSetup().setCmsStatus(0);
		ddpCommunicationSetupService.saveDdpCommunicationSetup(ruleWrapper.getDdpCommunicationSetup());
		Integer commSetupId = ruleWrapper.getDdpCommunicationSetup().getCmsCommunicationId();
		
			//save DdpNotification
		DdpNotification ddpNotification = ruleWrapper.getDdpNotification(); 
		ddpNotification.setNotCreatedBy(strUserName);
		ddpNotification.setNotCreatedDate(currentDate);
		ddpNotification.setNotModifiedBy(strUserName);
		ddpNotification.setNotModifiedDate(currentDate);
		ddpNotification.setNotStatus(0);
		ddpNotificationService.saveDdpNotification(ddpNotification);
		
		DdpCompressionSetup ddpCompressionSetup = new DdpCompressionSetup();
		//save DDP  COMPRESSION
		String strExpCompressionLvl = httpServletRequest.getParameter("selExpCompressionLvl");
		ddpCompressionSetup.setCtsCompressionLevel(strExpCompressionLvl);
		ddpCompressionSetupService.saveDdpCompressionSetup(ddpCompressionSetup);
	    //save DdpDocnameConvention
		DdpDocnameConv ddpDocnameConv = ruleWrapper.getDdpDocnameConv();
		if(ddpDocnameConv == null)
		{
			ddpDocnameConv =  new DdpDocnameConv();
			ddpDocnameConv.setDcvGenNamingConv(""); 
		}
        ddpDocnameConv.setDcvCompanyCode("GIL");
        ddpDocnameConv.setDcvBranchCode("ALL");
        ddpDocnameConv.setDcvDoctypeCode("SALESINV");
        ddpDocnameConv.setDcvSaveType("Folder");
        ddpDocnameConv.setDcvStatus(0);
        ddpDocnameConv.setDcvCreatedBy(strUserName);
        ddpDocnameConv.setDcvCreatedDate(currentDate);
        ddpDocnameConv.setDcvModifiedBy(strUserName);
        ddpDocnameConv.setDcvModifiedDate(currentDate);
        ddpDocnameConvService.saveDdpDocnameConv(ddpDocnameConv);
        Integer dvcId = ddpDocnameConv.getDcvId();
//        Integer dvcId = ddpDocnameConv.getDcvId();
        
	    //save DdpRule and set status as active(zero) 
	    ruleWrapper.getDdpRule().setRulName((ruleName.length() > 64)? ruleName.substring(0, 63): ruleName);//fix length 64 chars
        ruleWrapper.getDdpRule().setRulDescription((ruleDescription.length() > 120)? ruleDescription.substring(0, 119) : ruleDescription );//fix length 120 chars
        ruleWrapper.getDdpRule().setRulStatus(0);
        ruleWrapper.getDdpRule().setRulCreatedBy(strUserName);
        ruleWrapper.getDdpRule().setRulCreatedDate(currentDate);
        ruleWrapper.getDdpRule().setRulModifiedBy(strUserName);
        ruleWrapper.getDdpRule().setRulModifiedDate(currentDate);
        // Save Rule
        ddpRuleService.saveDdpRule(ruleWrapper.getDdpRule());
        // Get Rule Id
        Integer ruleId = ruleWrapper.getDdpRule().getRulId();
        
        
        Integer delayCount = Integer.parseInt(httpServletRequest.getParameter("delayID"));
        //save DdpScheduler
        DdpScheduler ddpScheduler = new DdpScheduler();
        ddpScheduler.setSchTimeInterval("");
        ddpScheduler.setSchNotificationId(ddpNotification.getNotId());
        ddpScheduler.setSchStartDate(currentDate);
        ddpScheduler.setSchStatus(0);
        ddpScheduler.setSchType("CRON");
        ddpScheduler.setSchRuleType("EXPORT_RULE");
        ddpScheduler.setSchCreatedBy(strUserName);
        ddpScheduler.setSchCreatedDate(currentDate);
        ddpScheduler.setSchCronExpressions(cronExpression);
        ddpScheduler.setSchModifiedBy(strUserName);
        ddpScheduler.setSchModifiedDate(currentDate);
        ddpScheduler.setSchChoosenType(docChoosenType);
        ddpScheduler.setSchDelayCount(delayCount);
        if(ruleCategory.equalsIgnoreCase("byQryRB"))
        {
        	ddpScheduler.setSchRuleCategory(custompartyId);
        	ddpScheduler.setSchBatchingCriteria(httpServletRequest.getParameter("selBatchingCriteria"));
        	// Read the source of Export Query
        	exportQuerySource = Integer.parseInt(httpServletRequest.getParameter("querysource"));
        	ddpScheduler.setSchQuerySource(exportQuerySource);
        }
    	ddpScheduler.setSchReportFrequency(cronExpressionFreqReport);
    	ddpScheduler.setSchReportEmailTo(schEmailTo);
    	ddpScheduler.setSchReportEmailCc(schEmailCc);
        ddpSchedulerService.saveDdpScheduler(ddpScheduler);
        //save DdpExportRule
        ruleWrapper.getDdpExportRule().setExpRuleId(ruleId);
        ruleWrapper.getDdpExportRule().setExpSchedulerId(ddpScheduler);
        ruleWrapper.getDdpExportRule().setExpNotificationId(ddpNotification);
        if( ruleCategory.equalsIgnoreCase("byQryRB"))
        	ruleWrapper.getDdpExportRule().setExpCompressionId(ddpCompressionSetup);
        ruleWrapper.getDdpExportRule().setExpCommunicationId(ddpCommunicationSetupService.findDdpCommunicationSetup(commSetupId));
        //storing DocnameConv
        ruleWrapper.getDdpExportRule().setExpDocnameConvId(ddpDocnameConv);
        //corruption check should get from UI
        ruleWrapper.getDdpExportRule().setExpCorruptionCheck(1);
        ruleWrapper.getDdpExportRule().setExpIsPartyCheckRequired(0);
        ruleWrapper.getDdpExportRule().setExpStatus(0);
        ruleWrapper.getDdpExportRule().setExpCreatedBy(strUserName);
        ruleWrapper.getDdpExportRule().setExpCreatedDate(currentDate);
        ruleWrapper.getDdpExportRule().setExpModifiedBy(strUserName);
        ruleWrapper.getDdpExportRule().setExpModifiedDate(currentDate);
        if(ruleWrapper.getDdpExportRule().getExpActivationDate() == null || ruleWrapper.getDdpExportRule().getExpActivationDate().equals("") )
        	ruleWrapper.getDdpExportRule().setExpActivationDate(currentDate);
        else
        	ruleWrapper.getDdpExportRule().setExpActivationDate(ruleWrapper.getDdpExportRule().getExpActivationDate());
        
        ddpExportRuleService.saveDdpExportRule(ruleWrapper.getDdpExportRule());
        
        String department = ((ruleWrapper.getDdpRuleDetail().getRdtDepartment() != null && ruleWrapper.getDdpRuleDetail().getRdtDepartment().trim().length() > 0)? ruleWrapper.getDdpRuleDetail().getRdtDepartment().trim() : null );
        if (department != null && department.endsWith(","))
        	department = department.substring(0, department.length()-1);
        
        
        if(ruleCategory.equalsIgnoreCase("byQryRB"))
		{
        	String strQuerySourceDetails = env.getProperty("email.export.create.confirm.body.exportquery");
        	String strExportQuery = "";
        	//check the Source of Export Query and Store to Database
        	if(exportQuerySource == 0) //if Export Query Source from Properties
        	{
        		strExportQuery = env.getProperty("export.rule."+custompartyId+".customQuery",env.getProperty("email.export.create.confirm.body.exportquery.noqueryinprop"));
        	}
        	if(exportQuerySource == 1) //if Export Query Source from UI
        	{
        		String partyCode = null;
        		LinkedList<DdpExportQueryUi> exportQueryUILst = new LinkedList<DdpExportQueryUi>();
        		int exportqueryrowscount = Integer.parseInt(httpServletRequest.getParameter("exportqueryrowscount"));
        		LinkedList<Object> operatorList = new LinkedList<Object>();
        		LinkedList<Object> partyCodesList = new LinkedList<Object>();
        		LinkedList<Object> PartyValueList = new LinkedList<Object>();
        		//Reading 
        		int lineSeq = 0;
        		for(int indexI=0; indexI<=exportqueryrowscount; indexI++)
        		{
        			partyCode = httpServletRequest.getParameter("partyCode"+indexI);
        			if(partyCode !=null)
        			{
        				String partyValue = httpServletRequest.getParameter("partyValue"+indexI).trim();
        				String operator = httpServletRequest.getParameter("operator"+indexI);
        				DdpExportQueryUi exportQueryUi = new DdpExportQueryUi();
        				exportQueryUi.setEqiExpRuleId(ruleId);
        				exportQueryUi.setEqiSchId(ddpScheduler.getSchId());
        				exportQueryUi.setEqiLineSeq(lineSeq);
        				exportQueryUi.setEqiPartyCode(partyCode);
        				exportQueryUi.setEqiPartyValue(partyValue);
        				exportQueryUi.setEqiOperator(operator);
        				exportQueryUi.setEqiStatus(0);
        				exportQueryUILst.add(exportQueryUi);
        				ddpExportQueryUiService.saveDdpExportQueryUi(exportQueryUi);
        				lineSeq++;
        			}
        		}
        		strExportQuery = commonUtil.constructQueryWithExportQueryUIs(exportQueryUILst);
        	}
        	if(exportQuerySource == 2) //if Export Query Source from Text area
        	{
        		String query = ruleWrapper.getExportQueryfromTextarea().trim();
        		query = commonUtil.constructQueryFromTXT(query);
        		DdpExportQuery exportQuery = new DdpExportQuery();
        		exportQuery.setExqExpRuleId(ruleId);
        		exportQuery.setExqSchId(ddpScheduler.getSchId());
        		exportQuery.setExqQuery(query);
        		exportQuery.setExqStatus(0);
        		ddpExportQueryService.saveDdpExportQuery(exportQuery);
        		strExportQuery = query;
        	}
        	
        	strQuerySourceDetails = strQuerySourceDetails.replaceAll("%%EXPORTQUERY%%", strExportQuery);
        	mailBody = mailBody.concat(strQuerySourceDetails);
        	
        	String strdocumentsdetails = env.getProperty("email.export.create.confirm.body.documentsdetails");
    		String docDetails = "";
        	//check whether RuleDetails(Document Details) should store into DB or Reading from Properties file
        	if(docChoosenType == 0 )
        	{
	        	//get DocType and Generating Source
	        	for(int i=0; i < docList.size(); i++)
	        	{
	        		 DdpRuleDetail ddpRuleDetail = new DdpRuleDetail();
	        		 ddpRuleDetail.setRdtRuleId(ddpRuleService.findDdpRule(ruleId));
	        		 ddpRuleDetail.setRdtDocType(ddpDoctypeService.findDdpDoctype(docList.get(i).toString()));
	        		 ddpRuleDetail.setRdtCommunicationId(ddpCommunicationSetupService.findDdpCommunicationSetup(commSetupId));
	        		 ddpRuleDetail.setRdtStatus(0);
	        		 ddpRuleDetail.setRdtCreatedBy(strUserName);
	        		 ddpRuleDetail.setRdtCreatedDate(currentDate);
	        		 ddpRuleDetail.setRdtModifiedBy(strUserName);
	        		 ddpRuleDetail.setRdtModifiedDate(currentDate);
	        		 if(ruleWrapper.getDdpExportRule().getExpActivationDate() == null || ruleWrapper.getDdpExportRule().getExpActivationDate().equals("") )
	        			 ddpRuleDetail.setRdtActivationDate(currentDate);
	        		 else
	        			 ddpRuleDetail.setRdtActivationDate(ruleWrapper.getDdpExportRule().getExpActivationDate());
	        		 ddpRuleDetail.setRdtRuleType(env.getProperty("rule.type.export"));
	        		 ddpRuleDetail.setRdtDepartment(department);
	        		 ddpRuleDetail.setRdtNotificationId(ddpNotification);
	        		 if(i==0)
	        		 	ddpRuleDetail.setRdtRelavantType(1);//primary 
	        		 else
	        			 ddpRuleDetail.setRdtRelavantType(2);//Secondary
	        		 ddpRuleDetail.setRdtForcedType(Integer.parseInt(reqFlagList.get(i).toString()));
	        		 ddpRuleDetail.setRdtDocSequence(i+1);
//	        		 if(! sequenceList.get(i).equals("none"))
//	        		 	ddpRuleDetail.setRdtDocSequence(Integer.parseInt(sequenceList.get(i).toString()));
	        		 //save RuleDetail
	        		 ddpRuleDetailService.saveDdpRuleDetail(ddpRuleDetail);
	        		 Integer ruleDetailId = ddpRuleDetail.getRdtId();
	        		 
	        		 //save generating source setup
	        		 DdpGenSourceSetup ddpGenSourceSetup = new DdpGenSourceSetup();
	        		 ddpGenSourceSetup.setGssRdtId(ddpRuleDetail);
	        		 ddpGenSourceSetup.setGssOption(genSourceList.get(i).toString());
	        		 ddpGenSourceSetup.setGssCreatedBy(strUserName);
	        		 ddpGenSourceSetup.setGssCreatedDate(currentDate);
	        		 ddpGenSourceSetup.setGssModifiedBy(strUserName);
	        		 ddpGenSourceSetup.setGssModifiedDate(currentDate);
	        		 ddpGenSourceSetupService.saveDdpGenSourceSetup(ddpGenSourceSetup);
	        		 
	        		//Save Rated/Not Rated document
	                 DdpRateSetup ddpRateSetup = new DdpRateSetup();
	                 ddpRateSetup.setRtsRdtId(ddpRuleDetail);
	                 ddpRateSetup.setRtsOption(rateList.get(i).toString());
	                 ddpRateSetup.setRtsCreatedBy(strUserName);
	                 ddpRateSetup.setRtsCreatedDate(currentDate);
	                 ddpRateSetup.setRtsModifiedBy(strUserName);
	                 ddpRateSetup.setRtsModifiedDate(currentDate);
	                 ddpRateSetupService.saveDdpRateSetup(ddpRateSetup);
	                 
	        		 //save Export Version Setup
	        		 DdpExportVersionSetup ddpExportVersionSetup = new DdpExportVersionSetup();
	        		 ddpExportVersionSetup.setEvsRdtId(ddpRuleDetail);
	        		 ddpExportVersionSetup.setEvsOption(exportVersionList.get(i).toString());
	        		 ddpExportVersionSetup.setEvsCreatedBy(strUserName);
	        		 ddpExportVersionSetup.setEvsCreatedDate(currentDate);
	        		 ddpExportVersionSetup.setEvsModifiedBy(strUserName);
	        		 ddpExportVersionSetup.setEvsModifiedDate(currentDate);
	        		 ddpExportVersionSetupService.saveDdpExportVersionSetup(ddpExportVersionSetup);
	        		 docDetails = docDetails.concat("<tr><td>"+docList.get(i).toString()+"</td><td>"+genSourceList.get(i).toString()+"</td></tr>");
	        	}
        	}
        	strdocumentsdetails = strdocumentsdetails.replaceAll("%%EXPORTDOCUMENTDETAILS%%", docDetails);
        	mailBody = mailBody.concat(strdocumentsdetails);
		}//end if for custom Rule
        else//for RuleByClientID rule
        {
	        DdpParty ddpParty = ruleWrapper.getDdpRuleDetail().getRdtPartyCode();
	        String partyId = ruleWrapper.getDdpRuleDetail().getRdtPartyId().trim();
	        String strdocumentsdetails = env.getProperty("email.export.create.confirm.body.documentsdetails");
    		String docDetails = "";
	        //storing DdpRuleDetail over loop
	        for(String strBranch : brnList)
	        {
	        	//get DocType and Generating Source
	        	for(int i=0; i < docList.size(); i++)
	        	{
	        		 DdpRuleDetail ddpRuleDetail = new DdpRuleDetail();
	        		 ddpRuleDetail.setRdtRuleId(ddpRuleService.findDdpRule(ruleId));
	        		 ddpRuleDetail.setRdtCompany(ddpCompany);
	        		 ddpRuleDetail.setRdtBranch(ddpBranchService.findDdpBranch(strBranch));
	        		 ddpRuleDetail.setRdtDocType(ddpDoctypeService.findDdpDoctype(docList.get(i).toString()));
	        		 ddpRuleDetail.setRdtPartyCode(ddpParty);
	        		 ddpRuleDetail.setRdtCommunicationId(ddpCommunicationSetupService.findDdpCommunicationSetup(commSetupId));
	        		 ddpRuleDetail.setRdtStatus(0);
	        		 ddpRuleDetail.setRdtPartyId(partyId);
	        		 ddpRuleDetail.setRdtCreatedBy(strUserName);
	        		 ddpRuleDetail.setRdtCreatedDate(currentDate);
	        		 ddpRuleDetail.setRdtModifiedBy(strUserName);
	        		 ddpRuleDetail.setRdtModifiedDate(currentDate);
	        		 if(ruleWrapper.getDdpExportRule().getExpActivationDate() == null || ruleWrapper.getDdpExportRule().getExpActivationDate().equals("") )
	        			 ddpRuleDetail.setRdtActivationDate(currentDate);
	        		 else
	        			 ddpRuleDetail.setRdtActivationDate(ruleWrapper.getDdpExportRule().getExpActivationDate());
	        		 ddpRuleDetail.setRdtRuleType(env.getProperty("rule.type.export"));
	        		 ddpRuleDetail.setRdtDepartment(department);
	        		 ddpRuleDetail.setRdtNotificationId(ddpNotification);
	        		 if(i==0)
		        		 	ddpRuleDetail.setRdtRelavantType(1);//primary
		        		 else
		        			 ddpRuleDetail.setRdtRelavantType(2);//Secondary
	        		 ddpRuleDetail.setRdtForcedType(Integer.parseInt(reqFlagList.get(i).toString()));
	        		 //ddpRuleDetail.setRdtDocSequence(Integer.parseInt(sequenceList.get(i).toString()));
//	        		 if(! sequenceList.get(i).equals("none"))
//	        			 ddpRuleDetail.setRdtDocSequence(Integer.parseInt(sequenceList.get(i).toString()));
	        		 //save RuleDetail
	        		 ddpRuleDetailService.saveDdpRuleDetail(ddpRuleDetail);
	        		 Integer ruleDetailId = ddpRuleDetail.getRdtId();
	        		 //save generating source setup
	        		 DdpGenSourceSetup ddpGenSourceSetup = new DdpGenSourceSetup();
	        		 ddpGenSourceSetup.setGssRdtId(ddpRuleDetailService.findDdpRuleDetail(ruleDetailId));
	        		 ddpGenSourceSetup.setGssOption(genSourceList.get(i).toString());
	        		 ddpGenSourceSetup.setGssCreatedBy(strUserName);
	        		 ddpGenSourceSetup.setGssCreatedDate(currentDate);
	        		 ddpGenSourceSetup.setGssModifiedBy(strUserName);
	        		 ddpGenSourceSetup.setGssModifiedDate(currentDate);
	        		 ddpGenSourceSetupService.saveDdpGenSourceSetup(ddpGenSourceSetup);
	        		 //save Export Version Setup
	        		 DdpExportVersionSetup ddpExportVersionSetup = new DdpExportVersionSetup();
	        		 ddpExportVersionSetup.setEvsRdtId(ddpRuleDetailService.findDdpRuleDetail(ruleDetailId));
	        		 ddpExportVersionSetup.setEvsOption(exportVersionList.get(i).toString());
	        		 ddpExportVersionSetup.setEvsCreatedBy(strUserName);
	        		 ddpExportVersionSetup.setEvsCreatedDate(currentDate);
	        		 ddpExportVersionSetup.setEvsModifiedBy(strUserName);
	        		 ddpExportVersionSetup.setEvsModifiedDate(currentDate);
	        		 ddpExportVersionSetupService.saveDdpExportVersionSetup(ddpExportVersionSetup);
	        	}
	        }
	        for(int i=0; i < docList.size(); i++)
        	{
	        	 docDetails = docDetails.concat("<tr><td>"+docList.get(i).toString()+"</td><td>"+genSourceList.get(i).toString()+"</td></tr>");
        	}
	        strdocumentsdetails = strdocumentsdetails.replaceAll("%%EXPORTDOCUMENTDETAILS%%", docDetails);
        	mailBody = mailBody.concat(strdocumentsdetails);
        }//end else for RuleByClientID rule
//        uiModel.asMap().clear();
        
        //Trigger Mail with Created Details
        DdpUser ddpUser = ruleUtil.getDdpUser(strUserName);
        String maiilSubject = env.getProperty("email.export.create.confirm.subject") +"-"+env.getProperty("app.evn");
        
        CreateMailXML createMailXML = new CreateMailXML();
        String mailCCAddress = env.getProperty("email.cc");
        if( ! ddpUser.getUsrRegion().isEmpty())
        	mailCCAddress = env.getProperty("email."+ddpUser.getUsrRegion()+".cc",env.getProperty("email.cc"));
        createMailXML.sendMail(env.getProperty("smtp.host"), env.getProperty("email.from"), ddpUser.getUsrEmailAddress(), mailCCAddress, maiilSubject, mailBody, null);
        //run scheduler dynamically
        DdpCreateSchedulerTask ddpCreateSchedulerTask =  applicationContext.getBean("ddpCreateSchedulerTask",DdpCreateSchedulerTask.class);
        ddpCreateSchedulerTask.createNewScheduler(ddpScheduler);
        
        logger.info("DdpExportRuleController.create Method Executed Successfully.");
        return "redirect:/ddpexportrules/list/list/"+ruleId;
      
    }

	@RequestMapping(value = "/{expRuleId}/form", produces = "text/html")
    public String updateForm(@PathVariable("expRuleId") Integer expRuleId, Model uiModel,@RequestParam(value="customerId",required=false) String customerId) {
		String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
		// Get the logged in user company
        String userCompany = ruleUtil.getUserCompany(strUserName);
        RuleWrapper ruleWrapper = new RuleWrapper();
        if(customerId != null)
        {
        	uiModel.addAttribute("duplicateCustID",customerId);
        }
        DdpRule ddpRule = ddpRuleService.findDdpRule(expRuleId);
        //add ddpRule to RuleWrapper
        ruleWrapper.setDdpRule(ddpRule);
        
        DdpExportRule ddpExportRule = ddpExportRuleService.findDdpExportRule(expRuleId);
        //add ddpExportRule to RuleWrapper
        ruleWrapper.setDdpExportRule(ddpExportRule);
        
        //add ddpCompression to uiModel
        if(ddpExportRule.getExpCompressionId() != null)
        	uiModel.addAttribute("ddpCompressionSetup", ddpExportRule.getExpCompressionId());
        
        //add ddpNotification to RuleWrapper
        Set<DdpExportRule> setExpRules = new HashSet<DdpExportRule>();
        setExpRules.add(ddpExportRule);
        TypedQuery<DdpNotification> notifyquery = DdpNotification.findDdpNotificationsByDdpExportRules(setExpRules);
        List<DdpNotification> resultSet = notifyquery.getResultList();
        ruleWrapper.setDdpNotification(resultSet.get(0));
        
        //add ddpDocnameConv to RuleWrapper
        TypedQuery<DdpDocnameConv> typedQuery = DdpDocnameConv.findDdpDocnameConvsByDdpExportRules(setExpRules);
        List<DdpDocnameConv> ddpDocnameConvs = typedQuery.getResultList();
        ruleWrapper.setDdpDocnameConv((ddpDocnameConvs.size()>0)?ddpDocnameConvs.get(0) : null);
        
        TypedQuery<DdpRuleDetail> query = DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(ddpRule);
        List<DdpRuleDetail> ddpRuleDetails = query.getResultList();
        DdpRuleDetail selectRuleDetail = null;
        //add ddpRuleDetail to ruleWrapper
        if( ! ddpRuleDetails.isEmpty() ) 
        	selectRuleDetail = ddpRuleDetails.get(0); 
        //set default company,branch and partycode and partyid to rule Detail to avoid null values in wrapper.
        if(selectRuleDetail == null || selectRuleDetail.getRdtPartyId() == null)
        {
        	DdpRuleDetail dummyRuleDetail = new DdpRuleDetail();
        	dummyRuleDetail.setRdtCompany(ddpCompanyService.findDdpCompany("GIL"));
        	dummyRuleDetail.setRdtBranch(ddpBranchService.findDdpBranch("All"));
        	dummyRuleDetail.setRdtPartyCode(ddpPartyService.findDdpParty("CGNE"));
        	dummyRuleDetail.setRdtPartyId("DUMMY");
        	selectRuleDetail = dummyRuleDetail;
        	DdpBranch branch = ddpBranchService.findDdpBranch("All");
        	Set<DdpBranch> sBranch = new LinkedHashSet<DdpBranch>();
        	sBranch.add(branch);
        	uiModel.addAttribute("selectedBranchList", sBranch);
        	List<DdpBranch> ddpBranchs = new ArrayList<DdpBranch>();
        	ddpBranchs.addAll(sBranch);
        	ruleWrapper.setLstBranch(ddpBranchs);
        }
        ruleWrapper.setDdpRuleDetail(selectRuleDetail);
        //set Row Count
        Integer rowcount = ddpRuleDetails.size();
        ruleWrapper.setRowcount(rowcount);
        List<DdpCommFtp> commFtps = new ArrayList<DdpCommFtp>();
        List<DdpCommUnc> commUncs = new ArrayList<DdpCommUnc>();
        List<String> folder = new ArrayList<String>();
        DdpCommunicationSetup ddpCommunicationSetup = ddpExportRule.getExpCommunicationId();
        /**  Primary Communication Details    **/
        if(ddpCommunicationSetup.getCmsCommunicationProtocol().equalsIgnoreCase("FTP"))
        {
        	//set Ftp Setup
        	DdpCommFtp ddpCommFtp = ddpCommFtpService.findDdpCommFtp(Long.parseLong(ddpCommunicationSetup.getCmsProtocolSettingsId()));
        	DdpCommFtp dummyCommFtp = new DdpCommFtp();
        	//get ddpCommFtp data and split ftp site and folder.
        	String fullPath = ddpCommFtp.getCftFtpLocation();
        	fullPath = fullPath.replace("\\","/");
        	String location ="";
        	int index = nthOccurrence(fullPath, '/', 2);
        	if(index != -1){
        		location = fullPath.substring(0,index);
        		folder.add(fullPath.substring(index+1));
        	}
        	else{
        		location = fullPath;
        		folder.add("");
        	}
        	//WRITE LOGIC TO AVOID AUTO FLUSH DATA OBJECT(DIRTY OBJECT)
        	
        	dummyCommFtp.setCftFtpLocation(location);
        	dummyCommFtp.setCftFtpUserName(ddpCommFtp.getCftFtpUserName());
        	dummyCommFtp.setCftFtpPassword(ddpCommFtp.getCftFtpPassword());
        	if( ddpCommFtp.getCftFtpSecure() != null)
        		dummyCommFtp.setCftFtpSecure(ddpCommFtp.getCftFtpSecure());
        	dummyCommFtp.setCftFtpPort(ddpCommFtp.getCftFtpPort());
        	commFtps.add(dummyCommFtp);
            //temp solution
        	commUncs.add(new DdpCommUnc());
        	ruleWrapper.setDdpCommEmail(null);
        }
        else if(ddpCommunicationSetup.getCmsCommunicationProtocol().equalsIgnoreCase("UNC"))
        {
        	//set UNC Setup
        	DdpCommUnc ddpCommUnc = ddpCommUncService.findDdpCommUnc(Long.parseLong(ddpCommunicationSetup.getCmsProtocolSettingsId()));
        	commUncs.add(ddpCommUnc);
          //temp solution
            commFtps.add(new DdpCommFtp());
            folder.add("");
            ruleWrapper.setDdpCommEmail(null);
        }
        else if(ddpCommunicationSetup.getCmsCommunicationProtocol().equalsIgnoreCase("SMTP"))
        {
        	  commUncs.add(new DdpCommUnc());
              commFtps.add(new DdpCommFtp());
              folder.add("");
        	DdpCommEmail commEmail = ddpCommEmailService.findDdpCommEmail(Integer.parseInt(ddpCommunicationSetup.getCmsProtocolSettingsId()));
        	ruleWrapper.setDdpCommEmail(commEmail);
        }
        /**  Second Communication Details    **/
        if(ddpCommunicationSetup.getCmsProtocolSettingsId2() == null){
        	uiModel.addAttribute("isDiv1Selected", 1);
        	DdpCommFtp commFtp2 = new DdpCommFtp();
        	commFtps.add(commFtp2);
        	DdpCommUnc commUnc2 = new DdpCommUnc();
        	commUncs.add(commUnc2);
        	folder.add("");
        }else
        {
        	 uiModel.addAttribute("isDiv1Selected", 0);
        	 if(ddpCommunicationSetup.getCmsCommunicationProtocol2().equalsIgnoreCase("FTP"))
             {
             	//set Ftp Setup
             	DdpCommFtp ddpCommFtp = ddpCommFtpService.findDdpCommFtp(Long.parseLong(ddpCommunicationSetup.getCmsProtocolSettingsId2()));
             	DdpCommFtp dummyCommFtp = new DdpCommFtp();
             	//get ddpCommFtp data and split ftp site and folder.
             	String fullPath = ddpCommFtp.getCftFtpLocation();
             	fullPath = fullPath.replace("\\","/");
             	String location ="";
             	int index = nthOccurrence(fullPath, '/', 2);
             	if(index != -1){
             		location = fullPath.substring(0,index);
             		folder.add(fullPath.substring(index+1));
             	}
             	else{
             		location = fullPath;
             		folder.add("");
             	}
             	//WRITE LOGIC TO AVOID AUTO FLUSH DATA OBJECT(DIRTY OBJECT)
             	
             	dummyCommFtp.setCftFtpLocation(location);
             	dummyCommFtp.setCftFtpUserName(ddpCommFtp.getCftFtpUserName());
             	dummyCommFtp.setCftFtpPassword(ddpCommFtp.getCftFtpPassword());
             	if( ddpCommFtp.getCftFtpSecure() != null)
             		dummyCommFtp.setCftFtpSecure(ddpCommFtp.getCftFtpSecure());
             	dummyCommFtp.setCftFtpPort(ddpCommFtp.getCftFtpPort());
             	commFtps.add(dummyCommFtp);
                 //temp solution
             	commUncs.add(new DdpCommUnc());
             }
             else if(ddpCommunicationSetup.getCmsCommunicationProtocol2().equalsIgnoreCase("UNC"))
             {
             	//set UNC Setup
             	DdpCommUnc ddpCommUnc = ddpCommUncService.findDdpCommUnc(Long.parseLong(ddpCommunicationSetup.getCmsProtocolSettingsId2()));
             	commUncs.add(ddpCommUnc);
               //temp solution
                 commFtps.add(new DdpCommFtp());
                 folder.add("");
             }
        }
        /**  Third Communication Details    **/
        if(ddpCommunicationSetup.getCmsProtocolSettingsId3() == null){
        	uiModel.addAttribute("isDiv2Selected", 1);
        	DdpCommFtp commFtp3 = new DdpCommFtp();
        	commFtps.add(commFtp3);
        	DdpCommUnc commUnc3 = new DdpCommUnc();
        	commUncs.add(commUnc3);
        	folder.add("");
        }else
        {
        	 uiModel.addAttribute("isDiv2Selected", 0);
        	 if(ddpCommunicationSetup.getCmsCommunicationProtocol3().equalsIgnoreCase("FTP"))
             {
             	//set Ftp Setup
             	DdpCommFtp ddpCommFtp = ddpCommFtpService.findDdpCommFtp(Long.parseLong(ddpCommunicationSetup.getCmsProtocolSettingsId3()));
             	DdpCommFtp dummyCommFtp = new DdpCommFtp();
             	//get ddpCommFtp data and split ftp site and folder.
             	String fullPath = ddpCommFtp.getCftFtpLocation();
             	fullPath = fullPath.replace("\\","/");
             	String location ="";
             	int index = nthOccurrence(fullPath, '/', 2);
             	if(index != -1){
             		location = fullPath.substring(0,index);
             		folder.add(fullPath.substring(index+1));
             	}
             	else{
             		location = fullPath;
             		folder.add("");
             	}
             	//WRITE LOGIC TO AVOID AUTO FLUSH DATA OBJECT(DIRTY OBJECT)
             	
             	dummyCommFtp.setCftFtpLocation(location);
             	dummyCommFtp.setCftFtpUserName(ddpCommFtp.getCftFtpUserName());
             	dummyCommFtp.setCftFtpPassword(ddpCommFtp.getCftFtpPassword());
             	if( ddpCommFtp.getCftFtpSecure() != null)
             		dummyCommFtp.setCftFtpSecure(ddpCommFtp.getCftFtpSecure());
             	dummyCommFtp.setCftFtpPort(ddpCommFtp.getCftFtpPort());
             	commFtps.add(dummyCommFtp);
                 //temp solution
             	commUncs.add(new DdpCommUnc());
             }
             else if(ddpCommunicationSetup.getCmsCommunicationProtocol3().equalsIgnoreCase("UNC"))
             {
             	//set UNC Setup
             	DdpCommUnc ddpCommUnc = ddpCommUncService.findDdpCommUnc(Long.parseLong(ddpCommunicationSetup.getCmsProtocolSettingsId3()));
             	commUncs.add(ddpCommUnc);
               //temp solution
                 commFtps.add(new DdpCommFtp());
                 folder.add("");
             }
        }
        ruleWrapper.setDdpCommFtp(commFtps);
        ruleWrapper.setDdpCommUnc(commUncs);
        ruleWrapper.setFtpFolder(folder);
        //set company code
        populateEditForm(uiModel, ddpExportRule, (selectRuleDetail.getRdtCompany()!= null) ? selectRuleDetail.getRdtCompany().getComCompanyCode(): "GIL","updateForm");
        //set branch
        Set<DdpBranch> ddpBranchs = new LinkedHashSet<DdpBranch>();
        for(DdpRuleDetail ruleDetail:ddpRuleDetails)
        {
        	ddpBranchs.add(ruleDetail.getRdtBranch());
        }
        List<DdpBranch> lstBranch = new ArrayList<DdpBranch>();
        lstBranch.addAll(ddpBranchs);
        if( !ddpRuleDetails.isEmpty() )
        {
        	if(ddpRuleDetails.get(0).getRdtBranch() != null)
        	{
        		uiModel.addAttribute("selectedBranchList", ddpBranchs);
            	ruleWrapper.setLstBranch(lstBranch);
        	}
        	
        }
        //set doc type and generating system
        List<RuleDao> ruleDaos = null;
        if(ddpExportRule.getExpSchedulerId().getSchChoosenType() == null || ddpExportRule.getExpSchedulerId().getSchChoosenType() == 0) //Rule Details available
        {
        	ruleDaos = getRuleDetailForRuleId(expRuleId);
        }
       
        //getting cronExpression
        DdpScheduler ddpScheduler = ddpExportRule.getExpSchedulerId(); 
        String cronExpression = ddpScheduler.getSchCronExpressions().toString();
        String[] cronEx = cronExpression.split(" ");
		String dayofMonth = cronEx[3];
		String dayofWeek = cronEx[5];
		String hours = cronEx[2]; 
		String mins = cronEx[1];
		//check for every min, hour
		String minuteSpan = null;
		String hourSpan = null;
		if(cronEx[1].contains("/"))
		{
			String[] strMin = cronEx[1].split("/");
			mins = strMin[0];
			minuteSpan = strMin[1];
		}
		if(cronEx[2].contains("/"))
		{
			String[] strHour = cronEx[2].split("/");
			hours = strHour[0];
			hourSpan = strHour[1];
		}
        uiModel.addAttribute("ddpexportrule", ruleDaos);
        uiModel.addAttribute("itemId", expRuleId);
        uiModel.addAttribute("mins", mins);
        uiModel.addAttribute("hours", hours);
        uiModel.addAttribute("dayofMonth", dayofMonth);
        uiModel.addAttribute("dayofWeek", dayofWeek);
        if(minuteSpan != null)
        {
        	 uiModel.addAttribute("minuteSpan", minuteSpan);
        }
        if(hourSpan != null)
        {
        	 uiModel.addAttribute("hourSpan", hourSpan);
        }
        String dayofMonthFreqReport = "";
        String dayofWeekFreqReport = "";
        String hoursFreqReport = "";
        String minsFreqReport = "";
        String minuteSpanFreqReport = null;
        String hourSpanFreqReport = null;
        if( ddpScheduler.getSchReportFrequency() != null)
        {
        	if(! ddpScheduler.getSchReportFrequency().equals(""))
        	{
        		//getting cronExpression for Reports
                String repcronExpression = ddpScheduler.getSchReportFrequency().toString();
                String[] repcronEx = repcronExpression.split(" ");
        		dayofMonthFreqReport = repcronEx[3];
        		dayofWeekFreqReport = repcronEx[5];
        		hoursFreqReport = repcronEx[2]; 
        		minsFreqReport = repcronEx[1];
        		//check for every min, hour
        		minuteSpanFreqReport = null;
        		hourSpanFreqReport = null;
        		if(repcronEx[1].contains("/"))
        		{
        			String[] strMin = repcronEx[1].split("/");
        			minsFreqReport = strMin[0];
        			minuteSpanFreqReport = strMin[1];
        		}
        		if(repcronEx[2].contains("/"))
        		{
        			String[] strHour = repcronEx[2].split("/");
        			hoursFreqReport = strHour[0];
        			hourSpanFreqReport = strHour[1];
        		}
        		ruleWrapper.setSchReportEmailTo(ddpScheduler.getSchReportEmailTo());
        		ruleWrapper.setSchReportEmailCc(ddpScheduler.getSchReportEmailCc());
        	}
        }
        if(minuteSpanFreqReport != null)
        {
        	 uiModel.addAttribute("minuteSpanFreqReport", minuteSpanFreqReport);
        }
        if(hourSpanFreqReport != null)
        {
        	 uiModel.addAttribute("hourSpanFreqReport", hourSpanFreqReport);
        }
        /*** Setting up Export Query Details ***/
        Integer querySourceInt = ddpScheduler.getSchQuerySource();
        if(querySourceInt == null || querySourceInt.intValue() == 0)
        {
        	
        }
        else if(querySourceInt.intValue() == 1)
        {
        	List<DdpExportQueryUi> exportQueryUis = commonUtil.getExportQueryUiByRuleID(expRuleId);
        	uiModel.addAttribute("exportQueryUis",exportQueryUis);
        	 Gson gson = new Gson();
             String queryDetailsjson = gson.toJson(exportQueryUis);
			 uiModel.addAttribute("queryDetails", queryDetailsjson);
        }
        else if(querySourceInt.intValue() == 2)
        {
        	List<DdpExportQuery> exportQuerys = commonUtil.getExportQueryByRuleID(expRuleId);
        	ruleWrapper.setExportQueryfromTextarea(exportQuerys.get(0).getExqQuery().trim());
        }
        /******* Export Query Details End ***********/
        uiModel.addAttribute("minsFreqReport", minsFreqReport);
        uiModel.addAttribute("hoursFreqReport", hoursFreqReport);
        uiModel.addAttribute("dayofMonthFreqReport", dayofMonthFreqReport);
        uiModel.addAttribute("dayofWeekFreqReport", dayofWeekFreqReport);
        uiModel.addAttribute("rulewrapper", ruleWrapper);
        return "ddpexportrules/update";
    }
	@AuditLog(message = "Export Rule Updated.")
	@Transactional
	@RequestMapping(params = {"update"} , method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid RuleWrapper ruleWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		
		logger.info("DdpExportRuleController.update Method Invoked.");	
		String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
		// Get the logged in user company
        String userCompany = ruleUtil.getUserCompany(strUserName);
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ruleWrapper.getDdpExportRule(),userCompany,"update");
            return "ddpexportrules/update";
        }
        String rowCount = httpServletRequest.getParameter("rowscount");
		LinkedList<Object> docList = new LinkedList<Object>();
		LinkedList<Object> genSourceList = new LinkedList<Object>();
		LinkedList<Object> rateList = new LinkedList<Object>();
		LinkedList<Object> exportVersionList = new LinkedList<Object>();
		LinkedList<Object> relevantTypesList = new LinkedList<Object>();
		LinkedList<Object> reqFlagList = new LinkedList<Object>();
		LinkedList<Object> sequenceList = new LinkedList<Object>();
		
		int rCount = Integer.parseInt(rowCount);
        String docTypefalg = null;
        String genSourcefalg = null;
        String exportVersionfalg = null;
        String relevantTypesfalg = null;
        String reqFlagfalg = null;
        String sequencefalg = null;
        for (int i = 1; i <= rCount ; i++) {
            int count = i;
            docTypefalg = httpServletRequest.getParameter("selectDdpDoctype" + count);
            genSourcefalg = httpServletRequest.getParameter("selectDdpGenSource"+ count);
            String ratefalg = httpServletRequest.getParameter("selectDdpRate" + count);
            exportVersionfalg = httpServletRequest.getParameter("selectVersion"+ count);
            relevantTypesfalg = httpServletRequest.getParameter("selectRelType"+ count);
            reqFlagfalg = httpServletRequest.getParameter("selectReqFlag"+ count);
            sequencefalg = httpServletRequest.getParameter("selectSequence"+ count);
            if (docTypefalg != null) {
                docList.add(docTypefalg);
                genSourceList.add(genSourcefalg);
                rateList.add(ratefalg);
                exportVersionList.add(exportVersionfalg);
                relevantTypesList.add(relevantTypesfalg);
                reqFlagList.add(reqFlagfalg);
                sequenceList.add(sequencefalg);
                rCount = rCount + 1;
            }
        }
        
        String mailBody = "";
        String custompartyId = httpServletRequest.getParameter("customeClientId");
        Integer expruleId = ruleWrapper.getDdpExportRule().getExpRuleId();
        DdpExportRule ddpExportRule = ddpExportRuleService.findDdpExportRule(expruleId);
        DdpScheduler ddpScheduler = ddpSchedulerService.findDdpScheduler(ddpExportRule.getExpSchedulerId().getSchId());
        List<String> brnList = new ArrayList<String>();
        if(ddpScheduler.getSchRuleCategory() == null )
        {
        	//Rule by Client ID
        	/*
            verify whether partyId value is modified.
            if modified check whether new partyId value is already exist. 
            */
            TypedQuery<DdpRuleDetail> rdtquery = DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(ddpRuleService.findDdpRule(ruleWrapper.getDdpExportRule().getExpRuleId()));
            List<DdpRuleDetail> lii = rdtquery.getResultList();
            String newPartyId = ruleWrapper.getDdpRuleDetail().getRdtPartyId().toString().trim();
            if (newPartyId != null && newPartyId.endsWith(","))
            	newPartyId = newPartyId.substring(0, newPartyId.length()-1);
            List<String> newPartyIDs = Arrays.asList(newPartyId.split(","));
            String[] selectedBranchList = httpServletRequest.getParameterValues("lstBranch");
            brnList = Arrays.asList(selectedBranchList);	
			// Get the selected branch
	        if(brnList.contains("ALL"))
	        {
	        	brnList = new ArrayList<String>();
	        	brnList.add("ALL");
	        }
            for (String partyid : newPartyIDs) 
            {
            	String oldPartyId = lii.get(0).getRdtPartyId().trim();
            	List<String> oldPartyIDs = Arrays.asList(oldPartyId.split(","));
            	 if(! oldPartyIDs.contains(partyid))
                 {
          	       	String companycode = ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode();
          	       	String partyCodeValue = ruleWrapper.getDdpRuleDetail().getRdtPartyCode().getPtyPartyCode();
          	       	//check this party id is available or not.
          	       	List<Integer> rids = getRuleIdsByPartyCode(partyid,companycode,brnList,docList,partyCodeValue,expruleId);
          	       	if(rids.size() > 0)
          	       	{
          	       		if (partyid.startsWith("#"))
          	       			partyid = partyid.substring(1,partyid.length());
          	       		return "redirect:/ddpexportrules/list/"+ruleWrapper.getDdpExportRule().getExpRuleId().toString()+"/form?customerId="+partyid;
          	       	}
                 }
            }
        }
        else
        {
        	//Rule by DQL Query
        	if(! custompartyId.equalsIgnoreCase(ddpScheduler.getSchRuleCategory()))
        	{
        		List<Integer> rids = getRuleIdsByPartyCode(custompartyId);
        		if(rids.size() > 0)
      	       	{
      	       		if (custompartyId.startsWith("#"))
      	       			custompartyId = custompartyId.substring(1,custompartyId.length());
      	       		return "redirect:/ddpexportrules/list/"+ruleWrapper.getDdpExportRule().getExpRuleId().toString()+"/form?customerId="+custompartyId;
      	       	}
        	}
        	
        }
        
        String ruleCategory = httpServletRequest.getParameter("radioButtonforRuleType");
        
        //reading Configuration FTP/UNC
      	String communicationType0 = httpServletRequest.getParameter("radioButton0");
      	String communicationType1 = httpServletRequest.getParameter("radioButton1");
      	String communicationType2 = httpServletRequest.getParameter("radioButton2");
		List<String> ftpFolder = ruleWrapper.getFtpFolder();
		String everyMinute = httpServletRequest.getParameter("minutespan");
		String everyHour = httpServletRequest.getParameter("hourspan");
		String dayofMonth = httpServletRequest.getParameter("dayOfMonth");
		String dayofWeek = httpServletRequest.getParameter("dayOfWeek");
		String hours = httpServletRequest.getParameter("hour");
		String mins = httpServletRequest.getParameter("minute");
		String weekflag= httpServletRequest.getParameter("weekflag");
		String monthflag= httpServletRequest.getParameter("monthflag");
		if(weekflag.equals("0") && monthflag.equals("0"))
		{
			dayofMonth="*";
			dayofWeek="?";
		}
		if(weekflag.equals("0") && monthflag.equals("1"))
		{
			dayofWeek="?";
		}
		if(monthflag.equals("0") && weekflag.equals("1"))
		{
			dayofMonth="?";
		}
		if(! everyMinute.equals("0"))
			mins=mins+"/"+everyMinute;
		if(! everyHour.equals("0"))
			hours=hours+"/"+everyHour;
		String cronExpression = "0 "+mins+" "+hours+" "+dayofMonth+" * "+dayofWeek;
		
		String cronExpressionFreqReport = "" ;
		String schEmailTo = "";
		String schEmailCc = "";
		if( ! httpServletRequest.getParameter("frequencyFreqReport").equals("none")){
			String everyMinuteFreqReport = httpServletRequest.getParameter("minutespanFreqReport");
			String everyHourFreqReport = httpServletRequest.getParameter("hourspanFreqReport");
			String dayofMonthFreqReport = httpServletRequest.getParameter("dayOfMonthFreqReport");
			String dayofWeekFreqReport = httpServletRequest.getParameter("dayOfWeekFreqReport");
			String hoursFreqReport = httpServletRequest.getParameter("hourFreqReport");
			String minsFreqReport = httpServletRequest.getParameter("minuteFreqReport");
			String weekflagFreqReport = httpServletRequest.getParameter("weekflagFreqReport");
			String monthflagFreqReport = httpServletRequest.getParameter("monthflagFreqReport");
			if(weekflagFreqReport.equals("0") && monthflagFreqReport.equals("0"))
			{
				dayofMonthFreqReport="*";
				dayofWeekFreqReport="?";
			}
			if(weekflagFreqReport.equals("0") && monthflagFreqReport.equals("1"))
			{
				dayofWeekFreqReport="?";
			}
			if(monthflagFreqReport.equals("0") && weekflagFreqReport.equals("1"))
			{
				dayofMonthFreqReport="?";
			}
			if(! everyMinuteFreqReport.equals("0"))
				minsFreqReport=minsFreqReport+"/"+everyMinuteFreqReport;
			if(! everyHourFreqReport.equals("0"))
				hoursFreqReport=hoursFreqReport+"/"+everyHourFreqReport;
			
			cronExpressionFreqReport = "0 "+minsFreqReport+" "+hoursFreqReport+" "+dayofMonthFreqReport+" * "+dayofWeekFreqReport;
			schEmailTo = ruleWrapper.getSchReportEmailTo().trim();
			schEmailCc = ruleWrapper.getSchReportEmailCc().trim();
		}
		
		 int exportQuerySource = 0; // 0 : from properties ; 1 : from UI ; 2 : from Text Area
		 DdpCompany ddpCompany = null;
         String ruleName = "";
         String ruleDescription = "";
         Integer docChoosenType = Integer.parseInt(httpServletRequest.getParameter("selDocSel")); 
         
         if(! ruleCategory.equalsIgnoreCase("byQryRB"))
 		  {
        	 	docChoosenType = 0;
		        ddpCompany = ddpCompanyService.findDdpCompany(ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode());
		  	    ruleName = ddpCompany.getComRegion() + "_" + ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode() + "_" + ruleWrapper.getDdpRuleDetail().getRdtPartyId();
		  	  if(ruleWrapper.getDdpRule().getRulDescription().equals(""))
		        	ruleDescription = ruleName;
		        else 
		        	ruleDescription = ruleWrapper.getDdpRule().getRulDescription();
		  	  
		  	String strPartyDetaiils = env.getProperty("email.export.create.confirm.body.partydetails");
		  	strPartyDetaiils = strPartyDetaiils.replaceAll("%%EXPORTCLIENTID%%", ruleWrapper.getDdpRuleDetail().getRdtPartyId());
		  	strPartyDetaiils = strPartyDetaiils.replaceAll("%%RULEDESCRIPTION%%", ruleDescription);
	    	mailBody = mailBody.concat(strPartyDetaiils);
	    	String strRegionDetails = env.getProperty("email.export.create.confirm.body.regiondetails");
	    	strRegionDetails = strRegionDetails.replaceAll("%%EXPORTPARTYCODE%%", ruleWrapper.getDdpRuleDetail().getRdtPartyCode().getPtyPartyName());
	    	strRegionDetails = strRegionDetails.replaceAll("%%EXPORTCOMPANY%%", ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode());
	    	strRegionDetails = strRegionDetails.replaceAll("%%EXPORTBRANCH%%", brnList.toString());
	    	mailBody = mailBody.concat(strRegionDetails);
 		  }
         else
   		{
   			ruleName = "DDP_CUSTOM_"+custompartyId;
   			if(ruleWrapper.getDdpRule().getRulDescription().equals(""))
   	        	ruleDescription = ruleName;
   	        else 
   	        	ruleDescription = ruleWrapper.getDdpRule().getRulDescription();
   			
   			String strPartyDetaiils = env.getProperty("email.export.create.confirm.body.partydetails");
   			strPartyDetaiils = strPartyDetaiils.replaceAll("%%EXPORTCLIENTID%%", custompartyId);
   			strPartyDetaiils = strPartyDetaiils.replaceAll("%%RULEDESCRIPTION%%", ruleDescription);
	    	mailBody = mailBody.concat(strPartyDetaiils);
   		}
        // Get Status
        int status = Integer.parseInt(httpServletRequest.getParameter("status"));
        // Prepare RuleDetail
        Map<String, Integer> oldDocTypeRdtIDMap = new HashMap<String, Integer>();
		Map<String, Integer> newDocTypeRdtIDMap = new HashMap<String, Integer>(); 
		
        TypedQuery<DdpRuleDetail> query = DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(ddpRuleService.findDdpRule(expruleId));
        List<DdpRuleDetail> ddpRuleDetails = query.getResultList();
        
        /******
         * Get the created date and created by user name before deleting the
         * records in DDPRuleDetail table
         ***********/
        String crtedUserName = ddpExportRule.getExpCommunicationId().getCmsCreatedBy();
        Calendar crtedDateAndTime = ddpExportRule.getExpCommunicationId().getCmsCreatedDate();
        /*****************************************************************************************************************/
        /***************** Prepare CommunicationSetUp  ****************************************************************/
        DdpCommunicationSetup ddpCommSetup = ddpExportRule.getExpCommunicationId();
        String comprotoco = ddpCommSetup.getCmsProtocolSettingsId();
        /*****************************************************************************************************************/
       
        /*****************************Updating FTP/UNC details**********************************************************/
      //check communication type and store data into tables accordingly
        String strcommunicationdetails = env.getProperty("email.export.create.confirm.body.communicationdetails");
        String strExpCommDetails = "";
        /**** Primary Communication Details ***/
        if(communicationType0.equalsIgnoreCase("ftpConfig"))
        {
        	if(ddpCommSetup.getCmsCommunicationProtocol().equalsIgnoreCase("FTP"))
        	{
        		//update existing record
        		DdpCommFtp ddpCommFtp = ddpCommFtpService.findDdpCommFtp(Long.parseLong(comprotoco));
        		
        		//inserting correct ftp location
        		String ftpserver = ruleWrapper.getDdpCommFtp().get(0).getCftFtpLocation();
        		String secureProtocol = httpServletRequest.getParameter("secureType0");
        		String location = getFtpLocation(ftpserver, ftpFolder.get(0),secureProtocol);
                ddpCommFtp.setCftFtpLocation(location);
                ddpCommFtp.setCftFtpSecure(secureProtocol);
                ddpCommFtp.setCftFtpUserName(ruleWrapper.getDdpCommFtp().get(0).getCftFtpUserName());
                ddpCommFtp.setCftFtpPassword(ruleWrapper.getDdpCommFtp().get(0).getCftFtpPassword());
                ddpCommFtp.setCftFtpPort(ruleWrapper.getDdpCommFtp().get(0).getCftFtpPort());
                ddpCommFtpService.updateDdpCommFtp(ddpCommFtp);
                strExpCommDetails += "<tr><td>"+secureProtocol.toUpperCase()+"</td><td>"+location+"</td></tr>";
        	}
        	else{
				//save DdpCommFtp
	//			String location = ruleWrapper.getDdpCommFtp().getCftFtpLocation().concat("\\"+ftpFolder);
	//			ruleWrapper.getDdpCommFtp().setCftFtpLocation(location);
        		//inserting correct ftp location
        		String ftpserver = ruleWrapper.getDdpCommFtp().get(0).getCftFtpLocation();
        		String secureProtocol = httpServletRequest.getParameter("secureType0");
        		String location = getFtpLocation(ftpserver, ftpFolder.get(0),secureProtocol);
        		ruleWrapper.getDdpCommFtp().get(0).setCftFtpSecure(secureProtocol);
    			ruleWrapper.getDdpCommFtp().get(0).setCftFtpLocation(location);
				ddpCommFtpService.saveDdpCommFtp(ruleWrapper.getDdpCommFtp().get(0));
				Long commFtpId = ruleWrapper.getDdpCommFtp().get(0).getCftFtpId();
				ddpCommSetup.setCmsCommunicationProtocol("FTP");
				ddpCommSetup.setCmsProtocolSettingsId(commFtpId.toString());
				strExpCommDetails += "<tr><td>"+secureProtocol.toUpperCase()+"</td><td>"+location+"</td></tr>";
        	}
        	
        }
        else if(communicationType0.equalsIgnoreCase("uncConfig"))
        {
        	if(ddpCommSetup.getCmsCommunicationProtocol().equalsIgnoreCase("UNC"))
        	{
        		DdpCommUnc ddpCommUnc = ddpCommUncService.findDdpCommUnc(Long.parseLong(comprotoco));
        		ddpCommUnc.setCunUncPath(getUncPath(ruleWrapper.getDdpCommUnc().get(0).getCunUncPath()));
        		ddpCommUnc.setCunUncUserName(ruleWrapper.getDdpCommUnc().get(0).getCunUncUserName());
        		ddpCommUnc.setCunUncPassword(ruleWrapper.getDdpCommUnc().get(0).getCunUncPassword());
        		ddpCommUncService.updateDdpCommUnc(ddpCommUnc);
        		strExpCommDetails += "<tr><td>UNC</td><td>"+getUncPath(ruleWrapper.getDdpCommUnc().get(0).getCunUncPath())+"</td></tr>";
        	}
        	else{
        		DdpCommUnc commUnc = ruleWrapper.getDdpCommUnc().get(0);
        		String uncPath = getUncPath(commUnc.getCunUncPath());
        		commUnc.setCunUncPath(uncPath);
        		//save DdpCommUnc
        		ddpCommUncService.saveDdpCommUnc(commUnc);
	        	Long commUncId = commUnc.getCunUncId();
	        	ddpCommSetup.setCmsCommunicationProtocol("UNC");
	        	ddpCommSetup.setCmsProtocolSettingsId(commUncId.toString());
	        	strExpCommDetails += "<tr><td>UNC</td><td>"+uncPath+"</td></tr>";
	        }
        }
        else if(communicationType0.equalsIgnoreCase("emailConfig"))
        {
        	if(ddpCommSetup.getCmsCommunicationProtocol().equalsIgnoreCase("SMTP"))
        	{
        		DdpCommEmail commEmail = ddpCommEmailService.findDdpCommEmail(Integer.parseInt(comprotoco));
        		commEmail.setCemEmailFrom(ruleWrapper.getDdpCommEmail().getCemEmailFrom());
        		commEmail.setCemEmailTo(ruleWrapper.getDdpCommEmail().getCemEmailTo());
        		commEmail.setCemEmailCc(ruleWrapper.getDdpCommEmail().getCemEmailCc());
        		commEmail.setCemEmailBcc(ruleWrapper.getDdpCommEmail().getCemEmailBcc());
        		commEmail.setCemEmailReplyTo(ruleWrapper.getDdpCommEmail().getCemEmailReplyTo());
        		ddpCommEmailService.updateDdpCommEmail(commEmail);
        		strExpCommDetails += "<tr><td>SMTP</td><td>"+commEmail.getCemEmailTo()+"</td></tr>";
        	}
        	else{
        		DdpCommEmail commEmail = ruleWrapper.getDdpCommEmail();
            	ddpCommEmailService.saveDdpCommEmail(commEmail);
            	ddpCommSetup.setCmsCommunicationProtocol("SMTP");
            	ddpCommSetup.setCmsProtocolSettingsId(commEmail.getCemEmailId().toString());
            	strExpCommDetails+="<tr><td>SMTP</td><td>"+commEmail.getCemEmailTo()+"</td></tr>";
        	}
        }
        /***** Second Communication Details ******/
        if(httpServletRequest.getParameter("isDiv1Selected").equals("0"))
        {
        	//check second communication available
        	if(ddpCommSetup.getCmsProtocolSettingsId2()==null)
        	{
        		 if(communicationType1.equalsIgnoreCase("ftpConfig"))
	    	        {
	    				//save DdpCommFtp
	    	        	String ftpserver = ruleWrapper.getDdpCommFtp().get(1).getCftFtpLocation();
	    	        	String secureProtocol = httpServletRequest.getParameter("secureType1");
	    	        	String location = getFtpLocation(ftpserver,ftpFolder.get(1),secureProtocol);
	    	        	ruleWrapper.getDdpCommFtp().get(1).setCftFtpSecure(secureProtocol);
	    	        	ruleWrapper.getDdpCommFtp().get(1).setCftFtpLocation(location);
	    				ddpCommFtpService.saveDdpCommFtp(ruleWrapper.getDdpCommFtp().get(1));
	    				Long commFtpId = ruleWrapper.getDdpCommFtp().get(1).getCftFtpId();
	    				ddpCommSetup.setCmsCommunicationProtocol2("FTP");
	    				ddpCommSetup.setCmsProtocolSettingsId2(commFtpId.toString());
	    				strExpCommDetails+="<tr><td>"+secureProtocol.toUpperCase()+"</td><td>"+location+"</td></tr>";
	    	        }
	    	        else if(communicationType1.equalsIgnoreCase("uncConfig"))
	    	        {
	    	        	DdpCommUnc ddpCommUnc = ruleWrapper.getDdpCommUnc().get(1);
	    	        	String uncPath = getUncPath(ddpCommUnc.getCunUncPath());
	    	        	ddpCommUnc.setCunUncPath(uncPath);
	    	        	//save DdpCommUnc
	    	        	ddpCommUncService.saveDdpCommUnc(ddpCommUnc);
	    	        	Long commUncId = ddpCommUnc.getCunUncId();
	    	        	ddpCommSetup.setCmsCommunicationProtocol2("UNC");
	    	        	ddpCommSetup.setCmsProtocolSettingsId2(commUncId.toString());
	    	        	strExpCommDetails+="<tr><td>UNC</td><td>"+uncPath+"</td></tr>";
	    	        }
        	}
        	else{
	        	 if(communicationType1.equalsIgnoreCase("ftpConfig"))
	             {
	             	if(ddpCommSetup.getCmsCommunicationProtocol2().equalsIgnoreCase("FTP"))
	             	{
	             		//update existing record
	             		DdpCommFtp ddpCommFtp = ddpCommFtpService.findDdpCommFtp(Long.parseLong(ddpCommSetup.getCmsProtocolSettingsId2()));
	             		
	             		//inserting correct ftp location
	             		String ftpserver = ruleWrapper.getDdpCommFtp().get(1).getCftFtpLocation();
	             		String secureProtocol = httpServletRequest.getParameter("secureType1");
	             		String location = getFtpLocation(ftpserver, ftpFolder.get(1),secureProtocol);
	                     ddpCommFtp.setCftFtpLocation(location);
	                     ddpCommFtp.setCftFtpSecure(secureProtocol);
	                     ddpCommFtp.setCftFtpUserName(ruleWrapper.getDdpCommFtp().get(1).getCftFtpUserName());
	                     ddpCommFtp.setCftFtpPassword(ruleWrapper.getDdpCommFtp().get(1).getCftFtpPassword());
	                     ddpCommFtp.setCftFtpPort(ruleWrapper.getDdpCommFtp().get(1).getCftFtpPort());
	                     ddpCommFtpService.updateDdpCommFtp(ddpCommFtp);
	                     strExpCommDetails+="<tr><td>"+secureProtocol.toUpperCase()+"</td><td>"+location+"</td></tr>";
	             	}else{
	             		String ftpserver = ruleWrapper.getDdpCommFtp().get(1).getCftFtpLocation();
	             		String secureProtocol = httpServletRequest.getParameter("secureType1");
	             		String location = getFtpLocation(ftpserver, ftpFolder.get(1),secureProtocol);
	             		ruleWrapper.getDdpCommFtp().get(1).setCftFtpSecure(secureProtocol);
	         			ruleWrapper.getDdpCommFtp().get(1).setCftFtpLocation(location);
	     				ddpCommFtpService.saveDdpCommFtp(ruleWrapper.getDdpCommFtp().get(1));
	     				Long commFtpId = ruleWrapper.getDdpCommFtp().get(1).getCftFtpId();
	     				ddpCommSetup.setCmsCommunicationProtocol2("FTP");
	     				ddpCommSetup.setCmsProtocolSettingsId2(commFtpId.toString());
	     				strExpCommDetails+="<tr><td>"+secureProtocol.toUpperCase()+"</td><td>"+location+"</td></tr>";
	             	}
	             }
	             else if(communicationType1.equalsIgnoreCase("uncConfig"))
	             {
	             	if(ddpCommSetup.getCmsCommunicationProtocol2().equalsIgnoreCase("UNC"))
	             	{
	             		DdpCommUnc ddpCommUnc = ddpCommUncService.findDdpCommUnc(Long.parseLong(ddpCommSetup.getCmsProtocolSettingsId2()));
	             		ddpCommUnc.setCunUncPath(getUncPath(ruleWrapper.getDdpCommUnc().get(1).getCunUncPath()));
	             		ddpCommUnc.setCunUncUserName(ruleWrapper.getDdpCommUnc().get(1).getCunUncUserName());
	             		ddpCommUnc.setCunUncPassword(ruleWrapper.getDdpCommUnc().get(1).getCunUncPassword());
	             		ddpCommUncService.updateDdpCommUnc(ddpCommUnc);
	             		strExpCommDetails += "<tr><td>UNC</td><td>"+getUncPath(ruleWrapper.getDdpCommUnc().get(1).getCunUncPath())+"</td></tr>";
	             	}
	             	else{
	             		DdpCommUnc commUnc = ruleWrapper.getDdpCommUnc().get(1);
	             		String uncPath = getUncPath(commUnc.getCunUncPath());
	             		commUnc.setCunUncPath(uncPath);
	             		//save DdpCommUnc
	             		ddpCommUncService.saveDdpCommUnc(commUnc);
	     	        	Long commUncId = commUnc.getCunUncId();
	     	        	ddpCommSetup.setCmsCommunicationProtocol2("UNC");
	     	        	ddpCommSetup.setCmsProtocolSettingsId2(commUncId.toString());
	     	        	strExpCommDetails +="<tr><td>UNC</td><td>"+uncPath+"</td></tr>";
	     	        }
	             }
        	}
        }else{
        	ddpCommSetup.setCmsCommunicationProtocol2(null);
        	ddpCommSetup.setCmsProtocolSettingsId2(null);
        }
        /***** Third Communication Details ******/
        if(httpServletRequest.getParameter("isDiv2Selected").equals("0"))
        {
        	//check third communication availability
        	if(ddpCommSetup.getCmsProtocolSettingsId3()==null)
        	{
        		if(communicationType2.equalsIgnoreCase("ftpConfig"))
    	        {
    				//save DdpCommFtp
    	        	String ftpserver = ruleWrapper.getDdpCommFtp().get(2).getCftFtpLocation();
    	        	String secureProtocol = httpServletRequest.getParameter("secureType2");
    	        	String location = getFtpLocation(ftpserver,ftpFolder.get(2),secureProtocol);
    	        	ruleWrapper.getDdpCommFtp().get(2).setCftFtpSecure(secureProtocol);
    	        	ruleWrapper.getDdpCommFtp().get(2).setCftFtpLocation(location);
    				ddpCommFtpService.saveDdpCommFtp(ruleWrapper.getDdpCommFtp().get(2));
    				Long commFtpId = ruleWrapper.getDdpCommFtp().get(2).getCftFtpId();
    				ddpCommSetup.setCmsCommunicationProtocol3("FTP");
    				ddpCommSetup.setCmsProtocolSettingsId3(commFtpId.toString());
    				strExpCommDetails+="<tr><td>"+secureProtocol.toUpperCase()+"</td><td>"+location+"</td></tr>";
    	        }
    	        else if(communicationType2.equalsIgnoreCase("uncConfig"))
    	        {
    	        	DdpCommUnc ddpCommUnc = ruleWrapper.getDdpCommUnc().get(2);
    	        	String uncPath = getUncPath(ddpCommUnc.getCunUncPath());
    	        	ddpCommUnc.setCunUncPath(uncPath);
    	        	//save DdpCommUnc
    	        	ddpCommUncService.saveDdpCommUnc(ddpCommUnc);
    	        	Long commUncId = ddpCommUnc.getCunUncId();
    	        	ddpCommSetup.setCmsCommunicationProtocol3("UNC");
    	        	ddpCommSetup.setCmsProtocolSettingsId3(commUncId.toString());
    	        	strExpCommDetails+="<tr><td>UNC</td><td>"+uncPath+"</td></tr>";
    	        }
        	}else{
	        	 if(communicationType2.equalsIgnoreCase("ftpConfig"))
	             {
	             	if(ddpCommSetup.getCmsCommunicationProtocol().equalsIgnoreCase("FTP"))
	             	{
	             		//update existing record
	             		DdpCommFtp ddpCommFtp = ddpCommFtpService.findDdpCommFtp(Long.parseLong(ddpCommSetup.getCmsProtocolSettingsId3()));
	             		
	             		//inserting correct ftp location
	             		String ftpserver = ruleWrapper.getDdpCommFtp().get(2).getCftFtpLocation();
	             		String secureProtocol = httpServletRequest.getParameter("secureType2");
	             		String location = getFtpLocation(ftpserver, ftpFolder.get(2),secureProtocol);
	                     ddpCommFtp.setCftFtpLocation(location);
	                     ddpCommFtp.setCftFtpSecure(secureProtocol);
	                     ddpCommFtp.setCftFtpUserName(ruleWrapper.getDdpCommFtp().get(2).getCftFtpUserName());
	                     ddpCommFtp.setCftFtpPassword(ruleWrapper.getDdpCommFtp().get(2).getCftFtpPassword());
	                     ddpCommFtp.setCftFtpPort(ruleWrapper.getDdpCommFtp().get(2).getCftFtpPort());
	                     ddpCommFtpService.updateDdpCommFtp(ddpCommFtp);
	                     strExpCommDetails+="<tr><td>"+secureProtocol.toUpperCase()+"</td><td>"+location+"</td></tr>";
	             	}
	             	else{
	     				//save DdpCommFtp
	             		//inserting correct ftp location
	             		String ftpserver = ruleWrapper.getDdpCommFtp().get(2).getCftFtpLocation();
	             		String secureProtocol = httpServletRequest.getParameter("secureType2");
	             		String location = getFtpLocation(ftpserver, ftpFolder.get(2),secureProtocol);
	             		ruleWrapper.getDdpCommFtp().get(2).setCftFtpSecure(secureProtocol);
	         			ruleWrapper.getDdpCommFtp().get(2).setCftFtpLocation(location);
	     				ddpCommFtpService.saveDdpCommFtp(ruleWrapper.getDdpCommFtp().get(2));
	     				Long commFtpId = ruleWrapper.getDdpCommFtp().get(2).getCftFtpId();
	     				ddpCommSetup.setCmsCommunicationProtocol3("FTP");
	     				ddpCommSetup.setCmsProtocolSettingsId3(commFtpId.toString());
	     				strExpCommDetails+="<tr><td>"+secureProtocol.toUpperCase()+"</td><td>"+location+"</td></tr>";
	             	}
	             	
	             }
	             else if(communicationType2.equalsIgnoreCase("uncConfig"))
	             {
	             	if(ddpCommSetup.getCmsCommunicationProtocol().equalsIgnoreCase("UNC"))
	             	{
	             		DdpCommUnc ddpCommUnc = ddpCommUncService.findDdpCommUnc(Long.parseLong(ddpCommSetup.getCmsProtocolSettingsId3()));
	             		ddpCommUnc.setCunUncPath(getUncPath(ruleWrapper.getDdpCommUnc().get(2).getCunUncPath()));
	             		ddpCommUnc.setCunUncUserName(ruleWrapper.getDdpCommUnc().get(2).getCunUncUserName());
	             		ddpCommUnc.setCunUncPassword(ruleWrapper.getDdpCommUnc().get(2).getCunUncPassword());
	             		ddpCommUncService.updateDdpCommUnc(ddpCommUnc);
	             		strExpCommDetails+="<tr><td>UNC</td><td>"+getUncPath(ruleWrapper.getDdpCommUnc().get(2).getCunUncPath())+"</td></tr>";
	             	}
	             	else{
	             		DdpCommUnc commUnc = ruleWrapper.getDdpCommUnc().get(2);
	             		String uncPath = getUncPath(commUnc.getCunUncPath());
	             		commUnc.setCunUncPath(uncPath);
	             		//save DdpCommUnc
	             		ddpCommUncService.saveDdpCommUnc(commUnc);
	     	        	Long commUncId = commUnc.getCunUncId();
	     	        	ddpCommSetup.setCmsCommunicationProtocol3("UNC");
	     	        	ddpCommSetup.setCmsProtocolSettingsId3(commUncId.toString());
	     	        	strExpCommDetails+="<tr><td>UNC</td><td>"+uncPath+"</td></tr>";
	     	        }
	             }
        	}
        }else{
        	ddpCommSetup.setCmsCommunicationProtocol3(null);
        	ddpCommSetup.setCmsProtocolSettingsId3(null);
        }
        strcommunicationdetails = strcommunicationdetails.replaceAll("%%EXPORTCOMMDETAILS%%", strExpCommDetails);
        strcommunicationdetails = strcommunicationdetails.replaceAll("%%EXPORTCRONEXPRESSION%%", cronExpression);
        mailBody = mailBody.concat(strcommunicationdetails);
        
       /******************************updating FTP/UNC detail Ends**************************************/ 
        
        /**************** Update Communication Setup Services **************************************************************/
        //set date
      	  currentDate = Calendar.getInstance();
          ddpCommSetup.setCmsModifiedBy(strUserName);
          ddpCommSetup.setCmsModifiedDate(currentDate);
          ddpCommunicationSetupService.updateDdpCommunicationSetup(ddpCommSetup);
          // Get the cms_communication_id
          Integer commSetupId = ddpCommSetup.getCmsCommunicationId();
          /*****************************************************************************************************************/
          
          /********************** Updating DdpNotification *****************************************************************/
          DdpNotification ddpNotification = ddpExportRule.getExpNotificationId();
          ddpNotification.setNotStatus(0);
          ddpNotification.setNotSuccessEmailAddress(ruleWrapper.getDdpNotification().getNotSuccessEmailAddress());
          ddpNotification.setNotFailureEmailAddress(ruleWrapper.getDdpNotification().getNotFailureEmailAddress());
          ddpNotification.setNotModifiedBy(strUserName);
          ddpNotification.setNotModifiedDate(currentDate);
          ddpNotificationService.updateDdpNotification(ddpNotification);
          
          /***************** updating DDP_COMPRESSION_SETUP ***********************************/
          
          DdpCompressionSetup compressionSetup = ddpExportRule.getExpCompressionId();
          if(compressionSetup != null)
          {
        		  String strExpCompressionLvl = httpServletRequest.getParameter("selExpCompressionLvl");
            	  compressionSetup.setCtsCompressionLevel(strExpCompressionLvl);
                  ddpCompressionSetupService.updateDdpCompressionSetup(compressionSetup);
          }
         
          else
          {
        		  DdpCompressionSetup newcompressionSetup = new DdpCompressionSetup();
        		  String strExpCompressionLvl = httpServletRequest.getParameter("selExpCompressionLvl");
        		  newcompressionSetup.setCtsCompressionLevel(strExpCompressionLvl);
                  ddpCompressionSetupService.saveDdpCompressionSetup(newcompressionSetup);
                  compressionSetup = newcompressionSetup;
          }
          
          for(DdpRuleDetail ddpRuleDetail:ddpRuleDetails)
          {
        	  	String comCompanyCode = "";
        	  	String brnBranchCode = "";
        	  	String strPartyCode = "";
        	  	String strPartyID = "";
        	  	if(! (ddpRuleDetail.getRdtPartyId() == null || ddpRuleDetail.getRdtPartyId().equals("")))
        	  	{
        	  		comCompanyCode = ddpRuleDetail.getRdtCompany().getComCompanyCode();
        	  		brnBranchCode = ddpRuleDetail.getRdtBranch().getBrnBranchCode();
        	  		strPartyCode = ddpRuleDetail.getRdtPartyCode().getPtyPartyCode();
        	  		strPartyID = ddpRuleDetail.getRdtPartyId();
        	  	}
        	  	String mappKey = "";
    			if (brnList.contains("ALL")) {
    				mappKey = ddpRuleDetail.getRdtId() + ":";
    				brnBranchCode = "ALL";
    			}
    			mappKey = mappKey + strPartyID + "-"+ comCompanyCode + "-"+ brnBranchCode + "-"
    					+ ddpRuleDetail.getRdtDocType().getDtyDocTypeCode() + "-"
    					+ strPartyCode;
	          	//Delete DdpGenSourceSetup
	          	TypedQuery<DdpGenSourceSetup> query2 = DdpGenSourceSetup.findDdpGenSourceSetupsByGssRdtId(ddpRuleDetail);
	          	for(DdpGenSourceSetup ddpGenSourceSetup:query2.getResultList())
	          	{
	          		mappKey = mappKey.concat("-" + ddpGenSourceSetup.getGssOption());
	          		ddpGenSourceSetupService.deleteDdpGenSourceSetup(ddpGenSourceSetup);
	          	}
	          	
	          	 //Delete Rate/Not Rate
	            TypedQuery<DdpRateSetup> ratelst = DdpRateSetup.findDdpRateSetupsByDdpRuleDetail(ddpRuleDetail);
	            for (DdpRateSetup strOptionId : ratelst.getResultList()) {
	            	mappKey = mappKey.concat("-" + strOptionId.getRtsOption());
	                ddpRateSetupService.deleteDdpRateSetup(strOptionId);
	            }
	          //Delete Export Version Setup
	         	TypedQuery<DdpExportVersionSetup> query3 = DdpExportVersionSetup.findDdpExportVersionSetupsByEvsRdtId(ddpRuleDetail);
	         	for(DdpExportVersionSetup ddpExportVersionSetup:query3.getResultList())
	         	{
	         		ddpExportVersionSetupService.deleteDdpExportVersionSetup(ddpExportVersionSetup);
	         	}
	         	oldDocTypeRdtIDMap.put(mappKey, ddpRuleDetail.getRdtId());
	          	//delete ruleDetail
	          	ddpRuleDetailService.deleteDdpRuleDetail(ddpRuleDetail);
          }
        
        Integer delayCount = Integer.parseInt(httpServletRequest.getParameter("delayID"));
  	    DdpParty ddpParty = ruleWrapper.getDdpRuleDetail().getRdtPartyCode();
        String partyId = ruleWrapper.getDdpRuleDetail().getRdtPartyId().trim();
        String department = ((ruleWrapper.getDdpRuleDetail().getRdtDepartment() != null && ruleWrapper.getDdpRuleDetail().getRdtDepartment().trim().length() > 0)? ruleWrapper.getDdpRuleDetail().getRdtDepartment().trim() : null );
        if (department != null && department.endsWith(","))
        	department = department.substring(0, department.length()-1);
        
        if(ruleCategory.equalsIgnoreCase("byQryRB"))
		{
        	String strQuerySourceDetails = env.getProperty("email.export.create.confirm.body.exportquery");
        	String strExportQuery = "";
        	// Read the source of Export Query
        	exportQuerySource = Integer.parseInt(httpServletRequest.getParameter("querysource"));
        	//check the Source of Export Query and Store to Database
        	if(exportQuerySource == 0) //if Export Query Source from Properties
        	{
        		strExportQuery = env.getProperty("export.rule."+custompartyId+".customQuery");
        		if(strExportQuery == "" || strExportQuery == null)
        			strExportQuery = env.getProperty("email.export.create.confirm.body.exportquery.noqueryinprop");
        	}
        	if(exportQuerySource == 1) //if Export Query Source from UI
        	{
        		//check the previous DdpExportQueryUi object and update status as inactive(1)
        		List<DdpExportQueryUi> exportQueryUis = commonUtil.getExportQueryUiByRuleID(expruleId);
        		for(DdpExportQueryUi exportQueryUi: exportQueryUis)
        		{
        			exportQueryUi.setEqiStatus(1);
        			ddpExportQueryUiService.updateDdpExportQueryUi(exportQueryUi);
        		}
        		String exportquery = "";
        		String partyCode = null;
        		int exportqueryrowscount = Integer.parseInt(httpServletRequest.getParameter("exportqueryrowscount"));
        		LinkedList<DdpExportQueryUi> exportQueryUILst = new LinkedList<DdpExportQueryUi>();
        		LinkedList<Object> operatorList = new LinkedList<Object>();
        		LinkedList<Object> partyCodesList = new LinkedList<Object>();
        		LinkedList<Object> PartyValueList = new LinkedList<Object>();
        		//Reading 
        		int lineSeq = 0;
        		for(int indexI=0; indexI<=exportqueryrowscount; indexI++)
        		{
        			partyCode = httpServletRequest.getParameter("partyCode"+indexI);
        			if(partyCode !=null)
        			{
        				String partyValue = httpServletRequest.getParameter("partyValue"+indexI).trim();
        				String operator = httpServletRequest.getParameter("operator"+indexI);
        				DdpExportQueryUi exportQueryUi = new DdpExportQueryUi();
        				exportQueryUi.setEqiExpRuleId(expruleId);
        				exportQueryUi.setEqiSchId(ddpScheduler.getSchId());
        				exportQueryUi.setEqiLineSeq(lineSeq);
        				exportQueryUi.setEqiPartyCode(partyCode);
        				exportQueryUi.setEqiPartyValue(partyValue);
        				exportQueryUi.setEqiOperator(operator);
        				exportQueryUi.setEqiStatus(0);
        				exportQueryUILst.add(exportQueryUi);
        				ddpExportQueryUiService.saveDdpExportQueryUi(exportQueryUi);
        				lineSeq++;
        			}
        		}
        		strExportQuery = commonUtil.constructQueryWithExportQueryUIs(exportQueryUILst);
        	}
        	if(exportQuerySource == 2) //if Export Query Source from Text area
        	{
        		strExportQuery = commonUtil.constructQueryFromTXT(ruleWrapper.getExportQueryfromTextarea().trim());
        		//check the previous DdpExportQuery object and update status as inactive(1)
        		List<DdpExportQuery> exportQuerys = commonUtil.getExportQueryByRuleID(expruleId);
        		for(DdpExportQuery exportQuery: exportQuerys)
        		{
        			exportQuery.setExqStatus(1);
        			ddpExportQueryService.updateDdpExportQuery(exportQuery);
        		}
        		String exportquery = ruleWrapper.getExportQueryfromTextarea();
        		DdpExportQuery exportQuery = new DdpExportQuery();
        		exportQuery.setExqExpRuleId(expruleId);
        		exportQuery.setExqSchId(ddpScheduler.getSchId());
        		exportQuery.setExqQuery(exportquery);
        		exportQuery.setExqStatus(0);
        		ddpExportQueryService.saveDdpExportQuery(exportQuery);
        	}
        	strQuerySourceDetails = strQuerySourceDetails.replaceAll("%%EXPORTQUERY%%",strExportQuery);
        	mailBody = mailBody.concat(strQuerySourceDetails);
        	
        	String strdocumentsdetails = env.getProperty("email.export.create.confirm.body.documentsdetails");
        	String docDetails = "";
        	if(docChoosenType == 0)
        	{
        		//get DocType and Generating Source
            	for(int i=0; i < docList.size(); i++)
            	{
            		 DdpRuleDetail ddpRuleDetail = new DdpRuleDetail();
            		 ddpRuleDetail.setRdtRuleId(ddpRuleService.findDdpRule(expruleId));
            		 ddpRuleDetail.setRdtDocType(ddpDoctypeService.findDdpDoctype(docList.get(i).toString()));
            		 ddpRuleDetail.setRdtCommunicationId(ddpCommunicationSetupService.findDdpCommunicationSetup(commSetupId));
            		 ddpRuleDetail.setRdtStatus(status);
            		 ddpRuleDetail.setRdtCreatedBy(crtedUserName);
            		 ddpRuleDetail.setRdtCreatedDate(crtedDateAndTime);
            		 ddpRuleDetail.setRdtModifiedBy(strUserName);
            		 ddpRuleDetail.setRdtModifiedDate(currentDate);
            		 if(ruleWrapper.getDdpExportRule().getExpActivationDate() == null || ruleWrapper.getDdpExportRule().getExpActivationDate().equals("") )
            			 ddpRuleDetail.setRdtActivationDate(currentDate);
            		 else
            			 ddpRuleDetail.setRdtActivationDate(ruleWrapper.getDdpExportRule().getExpActivationDate());
            		 ddpRuleDetail.setRdtRuleType(env.getProperty("rule.type.export"));
            		 ddpRuleDetail.setRdtDepartment(department);
            		 ddpRuleDetail.setRdtNotificationId(ddpNotification);
            		 if(i==0)
 	        		 	ddpRuleDetail.setRdtRelavantType(1);//primary
 	        		 else
 	        			 ddpRuleDetail.setRdtRelavantType(2);//Secondary
	        		 ddpRuleDetail.setRdtForcedType(Integer.parseInt(reqFlagList.get(i).toString()));
	        		 ddpRuleDetail.setRdtDocSequence(i+1);
//	        		 if(! sequenceList.get(i).equals("null"))
//	        		 	ddpRuleDetail.setRdtDocSequence(Integer.parseInt(sequenceList.get(i).toString()));
            		 //save RuleDetail
            		 ddpRuleDetailService.saveDdpRuleDetail(ddpRuleDetail);
            		 Integer ruleDetailId = ddpRuleDetail.getRdtId();
            		 //save generating source setup
            		 DdpGenSourceSetup ddpGenSourceSetup = new DdpGenSourceSetup();
            		 ddpGenSourceSetup.setGssRdtId(ddpRuleDetail);
            		 ddpGenSourceSetup.setGssOption(genSourceList.get(i).toString());
            		 ddpGenSourceSetup.setGssCreatedBy(crtedUserName);
            		 ddpGenSourceSetup.setGssCreatedDate(crtedDateAndTime);
            		 ddpGenSourceSetup.setGssModifiedBy(strUserName);
            		 ddpGenSourceSetup.setGssModifiedDate(currentDate);
            		 ddpGenSourceSetupService.saveDdpGenSourceSetup(ddpGenSourceSetup);
            		 
            		 	//	Save Rated/Not Rated document
                     DdpRateSetup ddpRateSetup = new DdpRateSetup();
                     ddpRateSetup.setRtsRdtId(ddpRuleDetail);
                     ddpRateSetup.setRtsOption(rateList.get(i).toString());
                     ddpRateSetup.setRtsCreatedBy(strUserName);
                     ddpRateSetup.setRtsCreatedDate(currentDate);
                     ddpRateSetup.setRtsModifiedBy(strUserName);
                     ddpRateSetup.setRtsModifiedDate(currentDate);
                     ddpRateSetupService.saveDdpRateSetup(ddpRateSetup);
                     
            		 //save Export Version Setup
            		 DdpExportVersionSetup ddpExportVersionSetup = new DdpExportVersionSetup();
            		 ddpExportVersionSetup.setEvsRdtId(ddpRuleDetail);
            		 ddpExportVersionSetup.setEvsOption(exportVersionList.get(i).toString());
            		 ddpExportVersionSetup.setEvsCreatedBy(crtedUserName);
            		 ddpExportVersionSetup.setEvsCreatedDate(crtedDateAndTime);
            		 ddpExportVersionSetup.setEvsModifiedBy(strUserName);
            		 ddpExportVersionSetup.setEvsModifiedDate(currentDate);
            		 ddpExportVersionSetupService.saveDdpExportVersionSetup(ddpExportVersionSetup);
            		 
            		 String mappKey = "" + "-" + ""	+ "-" + "" + "-" + ddpRuleDetail.getRdtDocType().getDtyDocTypeCode()+ "-" + "" + "-" + genSourceList.get(i).toString()+ "-" + rateList.get(i).toString();
 	 				 newDocTypeRdtIDMap.put(mappKey, ddpRuleDetail.getRdtId());
            		 docDetails = docDetails.concat("<tr><td>"+docList.get(i).toString()+"</td><td>"+genSourceList.get(i).toString()+"</td></tr>");
            	}
        	}
        	strdocumentsdetails = strdocumentsdetails.replaceAll("%%EXPORTDOCUMENTDETAILS%%", docDetails);
        	mailBody = mailBody.concat(strdocumentsdetails);
        }
        else
        {
        	String strdocumentsdetails = env.getProperty("email.export.create.confirm.body.documentsdetails");
    		String docDetails = "";
        	for(String strBranch : brnList)
	        {
	        	//get DocType and Generating Source
	        	for(int i=0; i < docList.size(); i++)
	        	{
	        		 DdpRuleDetail ddpRuleDetail = new DdpRuleDetail();
	        		 ddpRuleDetail.setRdtRuleId(ddpRuleService.findDdpRule(expruleId));
	        		 ddpRuleDetail.setRdtCompany(ddpCompany);
	        		 ddpRuleDetail.setRdtBranch(ddpBranchService.findDdpBranch(strBranch));
	        		 ddpRuleDetail.setRdtDocType(ddpDoctypeService.findDdpDoctype(docList.get(i).toString()));
	        		 ddpRuleDetail.setRdtPartyCode(ddpParty);
	        		 ddpRuleDetail.setRdtCommunicationId(ddpCommunicationSetupService.findDdpCommunicationSetup(commSetupId));
	        		 ddpRuleDetail.setRdtStatus(status);
	        		 ddpRuleDetail.setRdtPartyId(partyId);
	        		 ddpRuleDetail.setRdtCreatedBy(crtedUserName);
	        		 ddpRuleDetail.setRdtCreatedDate(crtedDateAndTime);
	        		 ddpRuleDetail.setRdtModifiedBy(strUserName);
	        		 ddpRuleDetail.setRdtModifiedDate(currentDate);
	        		 if(ruleWrapper.getDdpExportRule().getExpActivationDate() == null || ruleWrapper.getDdpExportRule().getExpActivationDate().equals("") )
	        			 ddpRuleDetail.setRdtActivationDate(currentDate);
	        		 else
	        			 ddpRuleDetail.setRdtActivationDate(ruleWrapper.getDdpExportRule().getExpActivationDate());
	        		 ddpRuleDetail.setRdtRuleType(env.getProperty("rule.type.export"));
	        		 ddpRuleDetail.setRdtDepartment(department);
	        		 ddpRuleDetail.setRdtNotificationId(ddpNotification);
	        		 if(i==0)
		        		 	ddpRuleDetail.setRdtRelavantType(1);//primary
		        		 else
		        			 ddpRuleDetail.setRdtRelavantType(2);//Secondary
	        		 ddpRuleDetail.setRdtForcedType(Integer.parseInt(reqFlagList.get(i).toString()));
	        		 //ddpRuleDetail.setRdtDocSequence(i+1);
//	        		 if(! sequenceList.get(i).equals("null"))
//	        		 	ddpRuleDetail.setRdtDocSequence(Integer.parseInt(sequenceList.get(i).toString()));
	        		 //save RuleDetail
	        		 ddpRuleDetailService.saveDdpRuleDetail(ddpRuleDetail);
	        		 Integer ruleDetailId = ddpRuleDetail.getRdtId();
	        		 //save generating source setup
	        		 DdpGenSourceSetup ddpGenSourceSetup = new DdpGenSourceSetup();
	        		 ddpGenSourceSetup.setGssRdtId(ddpRuleDetail);
	        		 ddpGenSourceSetup.setGssOption(genSourceList.get(i).toString());
	        		 ddpGenSourceSetup.setGssCreatedBy(crtedUserName);
	        		 ddpGenSourceSetup.setGssCreatedDate(crtedDateAndTime);
	        		 ddpGenSourceSetup.setGssModifiedBy(strUserName);
	        		 ddpGenSourceSetup.setGssModifiedDate(currentDate);
	        		 ddpGenSourceSetupService.saveDdpGenSourceSetup(ddpGenSourceSetup);
	        		 
	        		 	//	Save Rated/Not Rated document
	                 DdpRateSetup ddpRateSetup = new DdpRateSetup();
	                 ddpRateSetup.setRtsRdtId(ddpRuleDetail);
	                 ddpRateSetup.setRtsOption(rateList.get(i).toString());
	                 ddpRateSetup.setRtsCreatedBy(strUserName);
	                 ddpRateSetup.setRtsCreatedDate(currentDate);
	                 ddpRateSetup.setRtsModifiedBy(strUserName);
	                 ddpRateSetup.setRtsModifiedDate(currentDate);
	                 ddpRateSetupService.saveDdpRateSetup(ddpRateSetup);
	                 
	        		 //save Export Version Setup
	        		 DdpExportVersionSetup ddpExportVersionSetup = new DdpExportVersionSetup();
	        		 ddpExportVersionSetup.setEvsRdtId(ddpRuleDetail);
	        		 ddpExportVersionSetup.setEvsOption(exportVersionList.get(i).toString());
	        		 ddpExportVersionSetup.setEvsCreatedBy(crtedUserName);
	        		 ddpExportVersionSetup.setEvsCreatedDate(crtedDateAndTime);
	        		 ddpExportVersionSetup.setEvsModifiedBy(strUserName);
	        		 ddpExportVersionSetup.setEvsModifiedDate(currentDate);
	        		 ddpExportVersionSetupService.saveDdpExportVersionSetup(ddpExportVersionSetup);
	        		 
	        		 String mappKey = ddpRuleDetail.getRdtPartyId() + "-"
	 						+ ddpRuleDetail.getRdtCompany().getComCompanyCode()
	 						+ "-" + ddpRuleDetail.getRdtBranch().getBrnBranchCode()
	 						+ "-" + ddpRuleDetail.getRdtDocType().getDtyDocTypeCode()
	 						+ "-" + ddpRuleDetail.getRdtPartyCode().getPtyPartyCode()
	 						+ "-" + genSourceList.get(i).toString();
	 				newDocTypeRdtIDMap.put(mappKey, ddpRuleDetail.getRdtId());
	        	}
	        } 
        	for(int i=0; i < docList.size(); i++)
        	{
        		docDetails = docDetails.concat("<tr><td>"+docList.get(i).toString()+"</td><td>"+genSourceList.get(i).toString()+"</td></tr>");
        	}
        	strdocumentsdetails = strdocumentsdetails.replaceAll("%%EXPORTDOCUMENTDETAILS%%", docDetails);
        	mailBody = mailBody.concat(strdocumentsdetails);
        }
      //updating DDP Rule
        DdpRule ddpRule = ddpRuleService.findDdpRule(expruleId);
        ddpRule.setRulName((ruleName.length() > 64)? ruleName.substring(0, 63): ruleName);
        ddpRule.setRulDescription((ruleDescription.length() > 120)? ruleDescription.substring(0, 119) : ruleDescription );
//        ddpRule.setRulStatus(status);
        ddpRule.setRulModifiedBy(strUserName);
        ddpRule.setRulModifiedDate(currentDate);
        ddpRuleService.updateDdpRule(ddpRule);
        
      //updating DdpExportRule
        ddpExportRule.setExpCommunicationId(ddpCommunicationSetupService.findDdpCommunicationSetup(commSetupId));
        ddpExportRule.setExpCompressionId(compressionSetup);
        ddpExportRule.setExpNotificationId(ddpNotification);
        ddpExportRule.setExpStatus(status);
        //corruption check should get from UI
        ddpExportRule.setExpModifiedBy(strUserName);
        ddpExportRule.setExpModifiedDate(currentDate);
        //set schedulerId
        if(ruleWrapper.getDdpExportRule().getExpActivationDate() == null || ruleWrapper.getDdpExportRule().getExpActivationDate().equals("") )
        	 ddpExportRule.setExpActivationDate(currentDate);
        else
        	 ddpExportRule.setExpActivationDate(ruleWrapper.getDdpExportRule().getExpActivationDate());
        
        //update export rule
        ddpExportRuleService.updateDdpExportRule(ddpExportRule);
        int expid = ddpExportRule.getExpRuleId();
        
        /************  Updating Document Naming Convention  ****************/
        String genNameConvention = "";
        if(ruleWrapper.getDdpDocnameConv() != null)
        {
        	if(ruleWrapper.getDdpDocnameConv().getDcvGenNamingConv() != null)
        	{
        		genNameConvention = ruleWrapper.getDdpDocnameConv().getDcvGenNamingConv();
        	}
        }
        DdpDocnameConv ddpDocnameConv = ddpDocnameConvService.findDdpDocnameConv(ddpExportRule.getExpDocnameConvId().getDcvId());
        ddpDocnameConv.setDcvGenNamingConv((ruleWrapper.getDdpDocnameConv()!= null)?genNameConvention:"");
        ddpDocnameConv.setDcvDupDocNamingConv((ruleWrapper.getDdpDocnameConv()!= null)?ruleWrapper.getDdpDocnameConv().getDcvDupDocNamingConv():"");
        ddpDocnameConv.setDcvModifiedBy(strUserName);
        ddpDocnameConv.setDcvModifiedDate(currentDate);
        ddpDocnameConvService.updateDdpDocnameConv(ddpDocnameConv);
        /*******************************************************************/
      //updating DdpScheduler
        boolean isCronExpChanged = false;
        if (!cronExpression.equals(ddpScheduler.getSchCronExpressions()))
        	isCronExpChanged = true;
        if((ddpScheduler.getSchReportFrequency() != null && !(cronExpressionFreqReport.equals(ddpScheduler.getSchReportFrequency()))) || (ddpScheduler.getSchStatus() == 1 && status == 0))
    	{
        	isCronExpChanged = true;
    	}
		ddpScheduler.setSchReportFrequency(cronExpressionFreqReport);
    	ddpScheduler.setSchReportEmailTo(schEmailTo);
    	ddpScheduler.setSchReportEmailCc(schEmailCc);
        ddpScheduler.setSchCronExpressions(cronExpression);
        ddpScheduler.setSchStatus(status);
        ddpScheduler.setSchChoosenType(docChoosenType);
        ddpScheduler.setSchDelayCount(delayCount);
        if(ruleCategory.equalsIgnoreCase("byQryRB"))
        {
        	ddpScheduler.setSchRuleCategory(custompartyId);
        	ddpScheduler.setSchBatchingCriteria(httpServletRequest.getParameter("selBatchingCriteria"));
        	ddpScheduler.setSchQuerySource(exportQuerySource);
        }
        ddpScheduler.setSchModifiedBy(strUserName);
        ddpScheduler.setSchModifiedDate(currentDate);
        ddpSchedulerService.updateDdpScheduler(ddpScheduler);
        
        	// DDP_CATEGORIZATION_HOLDER(CHL_RUL_ID),DDP_CATEGORIZED_DOCS(CAT_RDT_ID)
     		// . if relevant documents are there in DDP_CATEGORIZED_DOCS and
     		// DDP_CATEGORIZATION_HOLDER
 		Map<Integer, Integer> replaceRdts = new HashMap<Integer, Integer>();
 		Set<String> oldRdtKeys = oldDocTypeRdtIDMap.keySet();
 		for (String oldKey : oldRdtKeys) {
 			if (brnList.contains("ALL")) {
 				if (newDocTypeRdtIDMap.containsKey(oldKey.split(":")[1]))
 					replaceRdts.put(Integer.parseInt(oldKey.split(":")[0]),
 							newDocTypeRdtIDMap.get(oldKey.split(":")[1]));
 			} else {
 				if (newDocTypeRdtIDMap.containsKey(oldKey))
 					replaceRdts.put(oldDocTypeRdtIDMap.get(oldKey),
 							newDocTypeRdtIDMap.get(oldKey));
 			}
 		}
 		ruleUtil.updateCategorizedRdt(replaceRdts,expid,env.getProperty("rule.type.export"));
        uiModel.asMap().clear();
        
        if (isCronExpChanged) {
        	DdpCreateSchedulerTask ddpCreateSchedulerTask =  applicationContext.getBean("ddpCreateSchedulerTask",DdpCreateSchedulerTask.class);
            ddpCreateSchedulerTask.updateSchedularJob(ddpScheduler);
        }
        DdpUser ddpUser = ruleUtil.getDdpUser(strUserName);
        CreateMailXML createMailXML = new CreateMailXML();
        String mailSubject = env.getProperty("email.export.update.confirm.subject")+"-"+env.getProperty("app.evn");
        String mailCCAddress = env.getProperty("email.cc");
        if( ! ddpUser.getUsrRegion().isEmpty())
        	mailCCAddress = env.getProperty("email."+ddpUser.getUsrRegion()+".cc",env.getProperty("email.cc"));
        createMailXML.sendMail(env.getProperty("smtp.host"), env.getProperty("email.from"), ddpUser.getUsrEmailAddress(), mailCCAddress, mailSubject, mailBody, null);
        
        logger.info("DdpExportRuleController.update Method Executed Successfully.");
//      ddpExportRuleService.updateDdpExportRule(ddpExportRule);
//      return "redirect:/ddpexportrules/" + encodeUrlPathSegment(ddpExportRule.getExpRuleId().toString(), httpServletRequest);
      return "redirect:/ddpexportrules/list/list/"+expid;
    }
	
	@AuditLog(message = "Export Rule Deleted.")
	@Transactional
	@RequestMapping(value = "/{expRuleId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("expRuleId") Integer expRuleId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
      
		logger.info("DdpExportRuleController.delete Method Invoked.");
		//find Ddp Rule Details
		 TypedQuery<DdpRuleDetail> query = DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(ddpRuleService.findDdpRule(expRuleId));
	     List<DdpRuleDetail> ddpRuleDetails = query.getResultList();
		
	     for(DdpRuleDetail ddpRuleDetail:ddpRuleDetails)
         {
         	//Delete DdpGenSourceSetup
         	TypedQuery<DdpGenSourceSetup> query2 = DdpGenSourceSetup.findDdpGenSourceSetupsByGssRdtId(ddpRuleDetail);
         	for(DdpGenSourceSetup ddpGenSourceSetup:query2.getResultList())
         	{
         		ddpGenSourceSetupService.deleteDdpGenSourceSetup(ddpGenSourceSetup);
         	}
         	//Delete Rate/Not Rate
            TypedQuery<DdpRateSetup> ratelst = DdpRateSetup.findDdpRateSetupsByDdpRuleDetail(ddpRuleDetail);
            for (DdpRateSetup strOptionId : ratelst.getResultList()) {
                ddpRateSetupService.deleteDdpRateSetup(strOptionId);
            }
         	//Delete Export Version Setup
         	TypedQuery<DdpExportVersionSetup> query3 = DdpExportVersionSetup.findDdpExportVersionSetupsByEvsRdtId(ddpRuleDetail);
         	for(DdpExportVersionSetup ddpExportVersionSetup:query3.getResultList())
         	{
         		ddpExportVersionSetupService.deleteDdpExportVersionSetup(ddpExportVersionSetup);
         	}
         	//delete ruleDetail
         	ddpRuleDetailService.deleteDdpRuleDetail(ddpRuleDetail);
         }
		
	     //changing status in DdpExportRule
		DdpExportRule ddpExportRule = ddpExportRuleService.findDdpExportRule(expRuleId);
		ddpExportRule.setExpStatus(1);
		ddpExportRuleService.updateDdpExportRule(ddpExportRule);
		
		//change the status DocnameConvention
		DdpDocnameConv ddpDocnameConv = ddpExportRule.getExpDocnameConvId();
		ddpDocnameConv.setDcvStatus(1);
		ddpDocnameConvService.updateDdpDocnameConv(ddpDocnameConv);
		
		// change the status scheduler
		DdpScheduler ddpScheduler = ddpExportRule.getExpSchedulerId();
		ddpScheduler.setSchStatus(1);
		ddpSchedulerService.updateDdpScheduler(ddpScheduler);
		
		//changing status in DdpRule
		DdpRule ddpRule = ddpRuleService.findDdpRule(expRuleId);
		ddpRule.setRulStatus(1);
		ddpRuleService.updateDdpRule(ddpRule);
		
		uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        logger.info("DdpExportRuleController.delete Method Exceuted Successfully.");
		return "redirect:/ddpexportrules/list"+"/mediator";
    }
	
	@AuditLog(message = "Exporting As On Demand")
	@RequestMapping(value="onDemandRun/{itemId}",method = RequestMethod.POST)
	public String onDemandExport(@PathVariable("itemId") Integer expRuleId,@Valid OnDemandExportWrapper onDemandExportWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest request)
	{
		logger.info("DdpExportRuleController.onDemandExport Method Invoked.");
		logger.info("DdpExportRuleController.onDemandExport	:  On Demnad Exporting Started for Rule ID"+expRuleId);
		if (bindingResult.hasErrors()) {
			populateDateForOnDemandService(uiModel,onDemandExportWrapper,expRuleId);
			return "ddpexportrules/onDemandExport";
		}
		//System.out.println("From Date : "+onDemandExportWrapper.getFromDate().getTime());
		//System.out.println("To Date : "+onDemandExportWrapper.getToDate().getTime());
		
		DdpRuleSchedulerJob schedulerJob = applicationContext.getBean("schedulerJob", DdpRuleSchedulerJob.class);
		 DdpScheduler ddpScheduler = schedulerJob.getSchedulerDetails(expRuleId);
			String strBeanName = applicationProperties.getProperty("export.rule."+ddpScheduler.getSchRuleCategory()+".beanName");
			String strClassName = applicationProperties.getProperty("export.rule."+ddpScheduler.getSchRuleCategory()+".className");
          
          if(ddpScheduler.getSchRuleCategory() != null && strBeanName != null && strClassName != null) 
          {
        	  try {
	        	  DdpSchedulerJob job = (DdpSchedulerJob)applicationContext.getBean(strBeanName, Class.forName(strClassName));
	        	  job.runOnDemandRuleJob(ddpScheduler,onDemandExportWrapper.getFromDate(),onDemandExportWrapper.getToDate());
	          	logger.info("DdpCreateSchedulerTask.execute(String jobName) - Loaded Scheduler Bean and Class [{}] [{}] for [{}].",strBeanName,strClassName,"SCHEDULER : "+ddpScheduler.getSchId());
        	  } catch (Exception ex) {
        		  ex.printStackTrace();
        	  }
          }
          else
          {
        	  	schedulerJob.runOnDemandRuleJob(ddpScheduler,onDemandExportWrapper.getFromDate(),onDemandExportWrapper.getToDate());
          }
          logger.info("DdpExportRuleController.onDemandExport Method Executed Successfully.");
		 return "redirect:/ddpexportrules/list"+"/mediator";
	}
	
	@AuditLog(message = "On Demand for Pre-Validate report")
	@RequestMapping(value="prevalidate/{itemId}",method = RequestMethod.POST)
	public String onDemandPreVaildate(@PathVariable("itemId") Integer expRuleId,@Valid OnDemandExportWrapper onDemandExportWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest request,HttpServletResponse response)
	{
		logger.info("DdpExportRuleController.onDemandPreVaildate Method Invoked.");
		logger.info("DdpExportRuleController.onDemandPreVaildate	:  On Demnad Exporting Started for Rule ID"+expRuleId);
		if (bindingResult.hasErrors()) {
			populateDateForOnDemandService(uiModel,onDemandExportWrapper,expRuleId);
			return "ddpexportrules/onDemandExport";
		}
		//System.out.println("From Date : "+onDemandExportWrapper.getFromDate().getTime());
		//System.out.println("To Date : "+onDemandExportWrapper.getToDate().getTime());
		
		DdpRuleSchedulerJob schedulerJob = applicationContext.getBean("schedulerJob", DdpRuleSchedulerJob.class);
		 DdpScheduler ddpScheduler = schedulerJob.getSchedulerDetails(expRuleId);
			String strBeanName = applicationProperties.getProperty("export.rule."+ddpScheduler.getSchRuleCategory()+".beanName");
			String strClassName = applicationProperties.getProperty("export.rule."+ddpScheduler.getSchRuleCategory()+".className");
			 File generatedReportFolder = null;
          if(ddpScheduler.getSchRuleCategory() != null && strBeanName != null && strClassName != null) 
          {
        	  try {
	        	  DdpSchedulerJob job = (DdpSchedulerJob)applicationContext.getBean(strBeanName, Class.forName(strClassName));
	        	  generatedReportFolder = job.runOnDemandRulForReports(ddpScheduler,onDemandExportWrapper.getFromDate(),onDemandExportWrapper.getToDate(),com.agility.ddp.core.util.Constant.EXECUTION_STATUS_FAILED);
	          	logger.info("DdpCreateSchedulerTask.execute(String jobName) - Loaded Scheduler Bean and Class [{}] [{}] for [{}].",strBeanName,strClassName,"SCHEDULER : "+ddpScheduler.getSchId());
        	  } catch (Exception ex) {
        		  ex.printStackTrace();
        	  }
          }
          else
          {
        	  generatedReportFolder = schedulerJob.runOnDemandRulForReports(ddpScheduler, onDemandExportWrapper.getFromDate(),onDemandExportWrapper.getToDate(),com.agility.ddp.core.util.Constant.EXECUTION_STATUS_FAILED);
        	  
          }
          
          if (generatedReportFolder != null) {
        	  File downloadFile = null;
        	  for (File file : generatedReportFolder.listFiles()) {
        		  downloadFile = file;
        		  break;
        	  }
        	 
        	  try {
	        	  if (downloadFile != null) {
	        		  
	        		  	FileInputStream inputStream = new FileInputStream(downloadFile);
	        		  	response.setContentType("text/csv");
	        		  	response.setContentLength((int) downloadFile.length());
	        		  	
	        		 // set headers for the response
	        	        String headerKey = "Content-Disposition";
	        	        String headerValue = String.format("attachment; filename=\"%s\"",
	        	                downloadFile.getName());
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
	        	       // response.setHeader("Refresh", "10");
	        	       // response.addHeader("Refresh","10");
	        	        //response.addHeader("Refreash","10;url=http://localhost:8080/view/ddpexportrules/list"+"?mediator");
	        	       // String path =request.getContextPath();
	        	       // response.sendRedirect("/ddpexportrules/list?mediator");
	        	        //System.out.println("Request Path : "+path);
	        	  }
        	  
        	  } catch (Exception  ex) {
    			  ex.printStackTrace();
    		  } finally {
    			  if (generatedReportFolder != null) {
    					SchedulerJobUtil.deleteFolder(generatedReportFolder);
    				}
    		  }
          }
          logger.info("DdpExportRuleController.onDemandForReports Method Executed Successfully.");
		 return "redirect:/ddpexportrules/list"+"/mediator";
	}
	
	@AuditLog(message = "Exporting As On Demand Based on JobNumber")
	@RequestMapping(value="onDemandJobNumberRun/{itemId}",method = RequestMethod.POST)
	public String onDemandBasedOnJobNumberExport(@PathVariable("itemId") Integer expRuleId,@Valid OnDemandExportWrapper onDemandExportWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest request)
	{
		logger.info("DdpExportRuleController.onDemandBasedOnJobNumberExport Method Invoked.");
		logger.info("DdpExportRuleController.onDemandBasedOnJobNumberExport	:  On Demnad Exporting Started for Rule ID"+expRuleId);
		
		
		if (bindingResult.hasErrors()) {
			populateDateForOnDemandService(uiModel,onDemandExportWrapper,expRuleId);
			return "ddpexportrules/onDemandExport";
		}
		//System.out.println("From Date : "+onDemandExportWrapper.getFromDate().getTime());
		//System.out.println("To Date : "+onDemandExportWrapper.getToDate().getTime());
		String jobNumbers = onDemandExportWrapper.getJobNumber().trim();
		if(jobNumbers.contains(" "))
			jobNumbers = jobNumbers.replaceAll(" ", ",");
		if(jobNumbers.contains("\n"))
			jobNumbers = jobNumbers.replaceAll("\n", ",");
		if(jobNumbers.contains("\r"))
			jobNumbers = jobNumbers.replaceAll("\r", ",");
		if(jobNumbers.contains("\t"))
			jobNumbers = jobNumbers.replaceAll("\t", ",");
		jobNumbers = jobNumbers.replaceAll(",+",",").toUpperCase();
		if(jobNumbers.endsWith(","))
			jobNumbers = jobNumbers.substring(0, jobNumbers.length()-1);
		DdpRuleSchedulerJob schedulerJob = applicationContext.getBean("schedulerJob", DdpRuleSchedulerJob.class);
		 DdpScheduler ddpScheduler = schedulerJob.getSchedulerDetails(expRuleId);
			String strBeanName = applicationProperties.getProperty("export.rule."+ddpScheduler.getSchRuleCategory()+".beanName");
			String strClassName = applicationProperties.getProperty("export.rule."+ddpScheduler.getSchRuleCategory()+".className");
          
          if(ddpScheduler.getSchRuleCategory() != null && strBeanName != null && strClassName != null) 
          {
        	  try {
	        	  DdpSchedulerJob job = (DdpSchedulerJob)applicationContext.getBean(strBeanName, Class.forName(strClassName));
	        	  job.runOnDemandRuleBasedJobNumber(ddpScheduler,onDemandExportWrapper.getJobFromDate(),onDemandExportWrapper.getJobToDate(),jobNumbers);
	          	logger.info("DdpCreateSchedulerTask.execute(String jobName) - Loaded Scheduler Bean and Class [{}] [{}] for [{}].",strBeanName,strClassName,"SCHEDULER : "+ddpScheduler.getSchId());
        	  } catch (Exception ex) {
        		  ex.printStackTrace();
        	  }
          }
          else
          {
        	  	schedulerJob.runOnDemandRuleBasedJobNumber(ddpScheduler,onDemandExportWrapper.getJobFromDate(),onDemandExportWrapper.getJobToDate(),jobNumbers);
          }
          logger.info("DdpExportRuleController.onDemandBasedOnJobNumberExport Method Executed Successfully.");
		 return "redirect:/ddpexportrules/list"+"/mediator";
	}
	
	@AuditLog(message = "Exporting As On Demand Based on ConsignmentID")
	@RequestMapping(value="onDemandConsignmentIDRun/{itemId}",method = RequestMethod.POST)
	public String onDemandBasedOnConsignmentIDExport(@PathVariable("itemId") Integer expRuleId,@Valid OnDemandExportWrapper onDemandExportWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest request)
	{
		logger.info("DdpExportRuleController.onDemandBasedOnConsignmentIDExport Method Invoked.");
		logger.info("DdpExportRuleController.onDemandBasedOnConsignmentIDExport	:  On Demnad Exporting Started for Rule ID"+expRuleId);
		if (bindingResult.hasErrors()) {
			populateDateForOnDemandService(uiModel,onDemandExportWrapper,expRuleId);
			return "ddpexportrules/onDemandExport";
		}
		//System.out.println("From Date : "+onDemandExportWrapper.getFromDate().getTime());
		//System.out.println("To Date : "+onDemandExportWrapper.getToDate().getTime());
		
		String consignmentIDs = onDemandExportWrapper.getConsignmentID().trim();
		if(consignmentIDs.contains(" "))
			consignmentIDs = consignmentIDs.replaceAll(" ", ",");
		if(consignmentIDs.contains("\n"))
			consignmentIDs = consignmentIDs.replaceAll("\n", ",");
		if(consignmentIDs.contains("\t"))
			consignmentIDs = consignmentIDs.replaceAll("\t", ",");
		if(consignmentIDs.contains("\r"))
			consignmentIDs = consignmentIDs.replaceAll("\r", ",");
		consignmentIDs = consignmentIDs.replaceAll(",+",",").toUpperCase();
		if(consignmentIDs.endsWith(","))
			consignmentIDs = consignmentIDs.substring(0, consignmentIDs.length()-1);
		
		DdpRuleSchedulerJob schedulerJob = applicationContext.getBean("schedulerJob", DdpRuleSchedulerJob.class);
		 DdpScheduler ddpScheduler = schedulerJob.getSchedulerDetails(expRuleId);
			String strBeanName = applicationProperties.getProperty("export.rule."+ddpScheduler.getSchRuleCategory()+".beanName");
			String strClassName = applicationProperties.getProperty("export.rule."+ddpScheduler.getSchRuleCategory()+".className");
          
          if(ddpScheduler.getSchRuleCategory() != null && strBeanName != null && strClassName != null) 
          {
        	  try {
	        	  DdpSchedulerJob job = (DdpSchedulerJob)applicationContext.getBean(strBeanName, Class.forName(strClassName));
	        	  job.runOnDemandRuleBasedConsignmentId(ddpScheduler,onDemandExportWrapper.getConsFromDate(),onDemandExportWrapper.getConsToDate(),consignmentIDs);
	          	logger.info("DdpCreateSchedulerTask.execute(String jobName) - Loaded Scheduler Bean and Class [{}] [{}] for [{}].",strBeanName,strClassName,"SCHEDULER : "+ddpScheduler.getSchId());
        	  } catch (Exception ex) {
        		  ex.printStackTrace();
        	  }
          }
          else
          {
        	  	schedulerJob.runOnDemandRuleBasedConsignmentId(ddpScheduler,onDemandExportWrapper.getConsFromDate(),onDemandExportWrapper.getConsToDate(),consignmentIDs);
          }
          logger.info("DdpExportRuleController.onDemandBasedOnConsignmentIDExport Method Executed Successfully.");
		 return "redirect:/ddpexportrules/list"+"/mediator";
	}
	
	@AuditLog(message = "Exporting As On Demand Based on Document Reference")
	@RequestMapping(value="onDemandDocRefRun/{itemId}",method = RequestMethod.POST)
	public String onDemandBasedOnDocumentReferenceExport(@PathVariable("itemId") Integer expRuleId,@Valid OnDemandExportWrapper onDemandExportWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest request)
	{
		logger.info("DdpExportRuleController.onDemandBasedOnDocumentReferenceExport Method Invoked.");
		logger.info("DdpExportRuleController.onDemandBasedOnDocumentReferenceExport	:  On Demnad Exporting Started for Rule ID"+expRuleId);
		if (bindingResult.hasErrors()) {
			populateDateForOnDemandService(uiModel,onDemandExportWrapper,expRuleId);
			return "ddpexportrules/onDemandExport";
		}
		//System.out.println("From Date : "+onDemandExportWrapper.getFromDate().getTime());
		//System.out.println("To Date : "+onDemandExportWrapper.getToDate().getTime());
		
		DdpRuleSchedulerJob schedulerJob = applicationContext.getBean("schedulerJob", DdpRuleSchedulerJob.class);
		 DdpScheduler ddpScheduler = schedulerJob.getSchedulerDetails(expRuleId);
			String strBeanName = applicationProperties.getProperty("export.rule."+ddpScheduler.getSchRuleCategory()+".beanName");
			String strClassName = applicationProperties.getProperty("export.rule."+ddpScheduler.getSchRuleCategory()+".className");
          
          if(ddpScheduler.getSchRuleCategory() != null && strBeanName != null && strClassName != null) 
          {
        	  try {
	        	  DdpSchedulerJob job = (DdpSchedulerJob)applicationContext.getBean(strBeanName, Class.forName(strClassName));
	        	  job.runOnDemandRuleBasedDocRef(ddpScheduler,onDemandExportWrapper.getDocRefFromDate(),onDemandExportWrapper.getDocRefToDate(),onDemandExportWrapper.getDocRef().toUpperCase());
	          	logger.info("DdpCreateSchedulerTask.execute(String jobName) - Loaded Scheduler Bean and Class [{}] [{}] for [{}].",strBeanName,strClassName,"SCHEDULER : "+ddpScheduler.getSchId());
        	  } catch (Exception ex) {
        		  ex.printStackTrace();
        	  }
          }
          else
          {
        	  	schedulerJob.runOnDemandRuleBasedDocRef(ddpScheduler,onDemandExportWrapper.getDocRefFromDate(),onDemandExportWrapper.getDocRefToDate(),onDemandExportWrapper.getDocRef().toUpperCase());
          }
          logger.info("DdpExportRuleController.onDemandBasedOnDocumentReferenceExport Method Executed Successfully.");
		 return "redirect:/ddpexportrules/list"+"/mediator";
	}
	
	
	
	@AuditLog(message = "OnDemand for Reports")
	@RequestMapping(value="onDemandReportsRun/{itemId}",method = RequestMethod.POST)
	public String onDemandForReports(@PathVariable("itemId") Integer expRuleId,@Valid OnDemandExportWrapper onDemandExportWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest request,HttpServletResponse response)
	{
		logger.info("DdpExportRuleController.onDemandForReports Method Invoked.");
		logger.info("DdpExportRuleController.onDemandForReports	:  On Demnad Exporting Started for Rule ID"+expRuleId);
		if (bindingResult.hasErrors()) {
			populateDateForOnDemandService(uiModel,onDemandExportWrapper,expRuleId);
			return "ddpexportrules/onDemandExport";
		}
		//System.out.println("From Date : "+onDemandExportWrapper.getFromDate().getTime());
		//System.out.println("To Date : "+onDemandExportWrapper.getToDate().getTime());
		
		DdpRuleSchedulerJob schedulerJob = applicationContext.getBean("schedulerJob", DdpRuleSchedulerJob.class);
		 DdpScheduler ddpScheduler = schedulerJob.getSchedulerDetails(expRuleId);
			String strBeanName = applicationProperties.getProperty("export.rule."+ddpScheduler.getSchRuleCategory()+".beanName");
			String strClassName = applicationProperties.getProperty("export.rule."+ddpScheduler.getSchRuleCategory()+".className");
			 File generatedReportFolder = null;
          if(ddpScheduler.getSchRuleCategory() != null && strBeanName != null && strClassName != null) 
          {
        	  try {
	        	  DdpSchedulerJob job = (DdpSchedulerJob)applicationContext.getBean(strBeanName, Class.forName(strClassName));
	        	  generatedReportFolder = job.runOnDemandRulForReports(ddpScheduler,onDemandExportWrapper.getReportsFromDate(),onDemandExportWrapper.getReportsToDate(),com.agility.ddp.core.util.Constant.EXECUTION_STATUS_SUCCESS);
	          	logger.info("DdpCreateSchedulerTask.execute(String jobName) - Loaded Scheduler Bean and Class [{}] [{}] for [{}].",strBeanName,strClassName,"SCHEDULER : "+ddpScheduler.getSchId());
        	  } catch (Exception ex) {
        		  ex.printStackTrace();
        	  }
          }
          else
          {
        	  generatedReportFolder = schedulerJob.runOnDemandRulForReports(ddpScheduler, onDemandExportWrapper.getReportsFromDate(), onDemandExportWrapper.getReportsToDate(),com.agility.ddp.core.util.Constant.EXECUTION_STATUS_SUCCESS);
        	  
          }
          
          if (generatedReportFolder != null) {
        	  File downloadFile = null;
        	  for (File file : generatedReportFolder.listFiles()) {
        		  downloadFile = file;
        		  break;
        	  }
        	 
        	  try {
	        	  if (downloadFile != null) {
	        		  
	        		  	FileInputStream inputStream = new FileInputStream(downloadFile);
	        		  	response.setContentType("text/csv");
	        		  	response.setContentLength((int) downloadFile.length());
	        		  	
	        		 // set headers for the response
	        	        String headerKey = "Content-Disposition";
	        	        String headerValue = String.format("attachment; filename=\"%s\"",
	        	                downloadFile.getName());
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
	        	       // response.setHeader("Refresh", "10");
	        	       // response.addHeader("Refresh","10");
	        	        //response.addHeader("Refreash","10;url=http://localhost:8080/view/ddpexportrules/list"+"?mediator");
	        	       // String path =request.getContextPath();
	        	       // response.sendRedirect("/ddpexportrules/list?mediator");
	        	        //System.out.println("Request Path : "+path);
	        	  }
        	  
        	  } catch (Exception  ex) {
    			  ex.printStackTrace();
    		  } finally {
    			  if (generatedReportFolder != null) {
    					SchedulerJobUtil.deleteFolder(generatedReportFolder);
    				}
    		  }
          }
          logger.info("DdpExportRuleController.onDemandForReports Method Executed Successfully.");
		 return "redirect:/ddpexportrules/list"+"/mediator";
	}
	
	@RequestMapping(value="onDemandCancel/{itemId}",method = RequestMethod.GET)
	public String onDemandCancel(@PathVariable("itemId") Integer expRuleId)
	{
		 return "redirect:/ddpexportrules/list"+"/mediator";
	}
	
	void populateEditForm(Model uiModel, DdpExportRule ddpExportRule,String userCompany,String formtype) {
        uiModel.addAttribute("ddpExportRule", ddpExportRule);
        addDateTimeFormatPatterns(uiModel);
        String crUser = SecurityContextHolder.getContext().getAuthentication().getName();
    	List<DdpCompany> ddpCompanies = ruleUtil.getAccessibleCompaniesLst(crUser,env.getProperty("rule.exp.firstchars"));
    	ddpCompanies.removeAll(ruleUtil.getListOfReadOnlyAccessibleCompanies(crUser, env.getProperty("rule.exp.firstchars")));
    	//sort companies
        Collections.sort(ddpCompanies,new Comparator<DdpCompany>() {
			@Override
			public int compare(DdpCompany company1, DdpCompany company2) {
				return company1.getComCompanyCode().compareTo(company2.getComCompanyCode());
			}	
		});
       	uiModel.addAttribute("ddpcompanys", ddpCompanies);
        // Get the list of group the company id
        List<DdpBranch> lstBranch = new ArrayList<DdpBranch>();
        if(! ddpCompanies.isEmpty())
        {
        	if(! formtype.equalsIgnoreCase("updateForm"))
        	{
//        		if(! ddpCompanies.get(0).getComCompanyCode().equalsIgnoreCase(userCompany))
//            	{
//            		if(ddpCompanies.contains(ddpCompanyService.findDdpCompany(userCompany)))
//            		{
//            			ddpCompanies.remove(ddpCompanyService.findDdpCompany(userCompany));
//            			ddpCompanies.add(0,ddpCompanyService.findDdpCompany(userCompany));
//            		}
//            	}
            	userCompany = ddpCompanies.get(0).getComCompanyCode();
        	}
        	lstBranch = ruleUtil.findBranchbyCompany(userCompany);
        }
        	
        if(userCompany.equalsIgnoreCase("USC"))
        {
        	lstBranch.addAll(ruleUtil.findBranchbyCompany("LUS"));
        }
        //Sort Branches
        Collections.sort(lstBranch,new Comparator<DdpBranch>() {
			@Override
			public int compare(DdpBranch branch1, DdpBranch branch2) {
				return branch1.getBrnBranchCode().compareTo(branch2.getBrnBranchCode()); 
			}
		});
        if(! userCompany.equalsIgnoreCase("GIL"))
        {	
	        // Add All for the list at 0th index of the list
	        List<DdpBranch> allBranchLst = ruleUtil.findBranchbyCompany("GIL");
	        if(! allBranchLst.isEmpty()) 
	        	lstBranch.add(0, allBranchLst.get(0));
        }
        List<DdpDoctype> doctypeLst = ruleUtil.getAllDocTypes();
        doctypeLst.addAll(ruleUtil.getDocTypesByCompany(userCompany));
        uiModel.addAttribute("ddppartys", ruleUtil.getAllPartys());
        uiModel.addAttribute("ddpbranches", lstBranch);
        uiModel.addAttribute("ddpdoctypes", doctypeLst);
        uiModel.addAttribute("ddpGenSourceLkp", ruleUtil.getAllGenSourceLkp());
        uiModel.addAttribute("ddpRatedLkp", ruleUtil.getAllRateLkp());
        uiModel.addAttribute("ddpExpVersionLkp", ruleUtil.getExportVersionLkp());
        uiModel.addAttribute("noncontroldocs", env.getProperty("gensource.3rdparty"));
        uiModel.addAttribute("clientIDs", env.getProperty("export.ruleByQry.clientID"));
        uiModel.addAttribute("clientIDsDocSelMap", env.getProperty("export.ruleByQry.clientID.docsSource"));
    }
	@RequestMapping(value = { "testFTPConnection", "list\testFTPConnection" })
	@ResponseBody
	public int ftpTestConnection(HttpServletRequest httpServletRequest,
			@RequestParam(value = "protorype", required = false) String protorype,
			@RequestParam(value = "ftpserver", required = false) String ftpserver,
			@RequestParam(value = "folder", required = false) String folder,
			@RequestParam(value = "port", required = false) String port,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password)
	{
		String ftpLocation = getFtpLocation(ftpserver, folder, protorype);
		int strConnected= 1 ;
		boolean isConnected = false;
		if(protorype.equalsIgnoreCase("ftp"))
		{
			DdpFTPClient ftpClient = new DdpFTPClient();
			ftpLocation = ftpLocation.substring(nthOccurrence(ftpLocation,'/',1)+1, nthOccurrence(ftpLocation,'/',2));
			isConnected = ftpClient.testFTPConnection(ftpLocation, Integer.parseInt(port), username, password);
		}
		else if(protorype.equalsIgnoreCase("sftp"))
		{
			DdpSFTPClient ddpSFTPClient = new DdpSFTPClient();
			ftpLocation = ftpLocation.substring(nthOccurrence(ftpLocation,'/',1)+1, nthOccurrence(ftpLocation,'/',2));
			isConnected = ddpSFTPClient.testSFTPConnection(ftpLocation, username, password, Integer.parseInt(port));
		}
		else if(protorype.equalsIgnoreCase("ftps"))
		{
			DdpFTPSClient ddpFTPSClient = new DdpFTPSClient();
			ftpLocation = ftpLocation.substring(nthOccurrence(ftpLocation,'/',1)+1, nthOccurrence(ftpLocation,'/',2));
			isConnected = ddpFTPSClient.testFTPSConnection(ftpLocation, Integer.parseInt(port), username, password);
		}
		if(isConnected)
			strConnected = 0 ;
		return strConnected;
	}
	@RequestMapping(value = { "testUNCConnection", "list\testUNCConnection" })
	@ResponseBody
	public int uncTestConnection(HttpServletRequest httpServletRequest,
			@RequestParam(value = "uncpath", required = false) String uncpath,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password)
	{
//		String uncPath = httpServletRequest.getParameter("cunUncPath");
//		String userName = httpServletRequest.getParameter("cunUncUserName");
//		String password = httpServletRequest.getParameter("cunUncUserName");
		String uncPathString = getUncPath(uncpath);
		int strConnected= 1;
		DdpUNCClient uncClient = new DdpUNCClient();
		boolean isConnected = uncClient.testUNCConnection(username ,password, uncPathString);
		if(isConnected)
			strConnected = 0;
		return strConnected;
	}
   public List<RuleDao> getRuleDetailForRuleIdShow(Integer expRuleId) {
		List<RuleDao> ruleDaoLst = new ArrayList<RuleDao>();
		TypedQuery<DdpRuleDetail> typedRuleDetails = DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(ddpRuleService.findDdpRule(expRuleId));
		List<DdpRuleDetail> lstRuleDetail = typedRuleDetails.getResultList();
		Set<String> brnLst = new LinkedHashSet<String>();
		for(DdpRuleDetail ruleDetail: lstRuleDetail)
		{
			brnLst.add((ruleDetail.getRdtBranch()!=null) ? ruleDetail.getRdtBranch().getBrnBranchCode() : "");
		}
		String strBrnLst = "";
        for(String strBranch:brnLst){
              strBrnLst+=strBranch+",";
        }
        strBrnLst = strBrnLst.substring(0,strBrnLst.lastIndexOf(","));
       //counting distinct DocTypes and GenSource
        Set<String> docTypePartyCodeSet = new LinkedHashSet<String>();
        for(DdpRuleDetail ddpRuleDetail: lstRuleDetail)
        {
        	String rdtDoctype = ddpRuleDetail.getRdtDocType().getDtyDocTypeCode();
        	TypedQuery<DdpGenSourceSetup> typedGenSources = DdpGenSourceSetup.findDdpGenSourceSetupsByGssRdtId(ddpRuleDetail);
        	List<DdpGenSourceSetup> ddpGenSourceSetups = typedGenSources.getResultList();
        	String rdtGensounce = ddpGenSourceSetups.get(0).getGssOption();
        	docTypePartyCodeSet.add(rdtDoctype+"-"+rdtGensounce);
        }
        
        int ruleSize=docTypePartyCodeSet.size();
        //iterate should be distinct doc types 
        
        for(int i=0;i<ruleSize;i++)
        {
        	RuleDao ruleDao = new RuleDao();
        	
	    	//setting generating system to ruleDao
	    	TypedQuery<DdpGenSourceSetup> typedGenSources = DdpGenSourceSetup.findDdpGenSourceSetupsByGssRdtId(lstRuleDetail.get(i));
	    	List<DdpGenSourceSetup> ddpGenSourceSetups = typedGenSources.getResultList();
	    	ruleDao.setGenSource((ddpGenSourceSetups.size()>0)? ruleUtil.findGenSourceDescriptionByOption(ddpGenSourceSetups.get(0).getGssOption()) : "");
	    	//set Export Version Setup
		   	TypedQuery<DdpExportVersionSetup> query2 = DdpExportVersionSetup.findDdpExportVersionSetupsByEvsRdtId(lstRuleDetail.get(i));
		 	List<DdpExportVersionSetup> ddpExportVersionSetups = query2.getResultList();
		 	ruleDao.setExpVersionSetup((ddpExportVersionSetups.size()>0)?ddpExportVersionSetups.get(0).getEvsOption():"");
	    	//setting RdtId
        	ruleDao.setRdtId(lstRuleDetail.get(i).getRdtId());
        	//setting Rdt company code
            ruleDao.setRdtCompanyCode((lstRuleDetail.get(i).getRdtCompany()!= null) ?lstRuleDetail.get(i).getRdtCompany().getComCompanyCode() : "");
            //setting branch as comma separator
            ruleDao.setRdtBranch(strBrnLst);
            //setting party code 
            ruleDao.setRdtPartyCode((lstRuleDetail.get(i).getRdtPartyCode()!=null) ? lstRuleDetail.get(i).getRdtPartyCode().getPtyPartyCode() : "");
            ruleDao.setRdtPartyName((lstRuleDetail.get(i).getRdtPartyCode()!=null) ? lstRuleDetail.get(i).getRdtPartyCode().getPtyPartyName() : "");
            //setting document type
            ruleDao.setRdtDocType(lstRuleDetail.get(i).getRdtDocType().getDtyDocTypeCode());
            //setting party id
            ruleDao.setRdtPartyId(lstRuleDetail.get(i).getRdtPartyId());
            ruleDao.setManditoryFlag((lstRuleDetail.get(i).getRdtForcedType()!=null) ? lstRuleDetail.get(i).getRdtForcedType().toString() : "");
            
            String strRateSetup = "";
 		   TypedQuery<DdpRateSetup> typedDdpRateSetup = DdpRateSetup.findDdpRateSetupsByDdpRuleDetail(lstRuleDetail.get(i));
 	       List<DdpRateSetup> rateSetup = typedDdpRateSetup.getResultList();
 	       if( ! rateSetup.isEmpty())
 	    	   strRateSetup = ruleUtil.findRateDescriptionByOption(rateSetup.get(0).getRtsOption());
 		   ruleDao.setStrRate(strRateSetup);
 		   
            ruleDao.setDocumentSequence((lstRuleDetail.get(i).getRdtDocSequence() == null) ? "none" : lstRuleDetail.get(i).getRdtDocSequence().toString());
            ruleDao.setRelevantType((lstRuleDetail.get(i).getRdtRelavantType() != null) ? lstRuleDetail.get(i).getRdtRelavantType().toString() : "");
            int ddpRuleId  = lstRuleDetail.get(i).getRdtRuleId().getRulId();
            ruleDao.setRid(ddpRuleId);
        	
            ruleDaoLst.add(ruleDao);
        	
        }
   
		return ruleDaoLst;
	}
   
   
   public List<RuleDao> getRuleDetailForRuleId(Integer expRuleId) 
   {
	   List<RuleDao> ruleDaos = new ArrayList<RuleDao>();
	   TypedQuery<DdpRuleDetail> query = DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(ddpRuleService.findDdpRule(expRuleId));
	   List<DdpRuleDetail> ddpRuleDetails = query.getResultList();
	   for(DdpRuleDetail ruleDetail:ddpRuleDetails)
	   {
		   RuleDao ruleDao = new RuleDao();
		   //set Generating Source to Rule Dao
		   TypedQuery<DdpGenSourceSetup> typedQuery = DdpGenSourceSetup.findDdpGenSourceSetupsByGssRdtId(ruleDetail);
		   List<DdpGenSourceSetup> ddpGenSourceSetups = typedQuery.getResultList();
		   ruleDao.setGenSource((ddpGenSourceSetups.size() > 0) ? ddpGenSourceSetups.get(0).getGssOption() : "" );
		   
		   //set Export Version Setup
		   TypedQuery<DdpExportVersionSetup> query2 = DdpExportVersionSetup.findDdpExportVersionSetupsByEvsRdtId(ruleDetail);
		   List<DdpExportVersionSetup> ddpExportVersionSetups = query2.getResultList();
		   ruleDao.setExpVersionSetup((ddpExportVersionSetups.size()>0) ? ddpExportVersionSetups.get(0).getEvsOption() : "");
		   
		   //set Rdt Id
		   ruleDao.setRdtId(ruleDetail.getRdtId());
		   //set company code
		   ruleDao.setRdtCompanyCode((ruleDetail.getRdtCompany()!=null) ? ruleDetail.getRdtCompany().getComCompanyCode(): "");
		   //set Branch
		   ruleDao.setRdtBranch((ruleDetail.getRdtBranch()!=null) ? ruleDetail.getRdtBranch().getBrnBranchCode() : "");
		   //set party code
		   ruleDao.setRdtPartyCode((ruleDetail.getRdtPartyCode()!= null) ? ruleDetail.getRdtPartyCode().getPtyPartyCode() : "");
		   //set Doc Type 
		   ruleDao.setRdtDocType(ruleDetail.getRdtDocType().getDtyDocTypeCode());
		   //set party Id
		   ruleDao.setRdtPartyId(ruleDetail.getRdtPartyId());
		   ruleDao.setManditoryFlag((ruleDetail.getRdtForcedType() != null) ? ruleDetail.getRdtForcedType().toString() : "");
		   ruleDao.setDocumentSequence((ruleDetail.getRdtDocSequence() == null) ? "none" : ruleDetail.getRdtDocSequence().toString());
		   String strRateSetup = "";
		   TypedQuery<DdpRateSetup> typedDdpRateSetup = DdpRateSetup.findDdpRateSetupsByDdpRuleDetail(ruleDetail);
	       List<DdpRateSetup> rateSetup = typedDdpRateSetup.getResultList();
	       if( ! rateSetup.isEmpty())
	    	   strRateSetup = ruleUtil.findRateDescriptionByOption(rateSetup.get(0).getRtsOption());
		   ruleDao.setStrRate(strRateSetup);
		   ruleDao.setRelevantType((ruleDetail.getRdtRelavantType() != null ) ? ruleDetail.getRdtRelavantType().toString() : "");
		   //set RuleId as integer
		   ruleDao.setRdtId(ruleDetail.getRdtRuleId().getRulId());
		   ruleDaos.add(ruleDao);
	   }
		return ruleDaos;
	}
   
   /**
    * 
    * @param ddpCompany
    * @return
    */
   public List<DdpExportRule> getRuleIdByCompany(String... ddpCompany) {
   	String cmpLst = "";
   	
   	String col= "drd.RDT_COMPANY=";
   	for(String company:ddpCompany){
   		cmpLst  +=col+"'"+company+"' OR ";
   	}
   	cmpLst = cmpLst.substring(0, cmpLst.lastIndexOf(" OR"));
       List<DdpExportRule> ddpExportrules = this.jdbcTemplate.query("SELECT DISTINCT dar.EXP_RULE_ID FROM dbo.DDP_EXPORT_RULE dar,dbo.DDP_RULE_DETAIL drd WHERE ("+cmpLst+") and dar.EXP_RULE_ID=drd.RDT_RULE_ID", new Object[] {  }, new RowMapper<DdpExportRule>() {

           public DdpExportRule mapRow(ResultSet rs, int rowNum) throws SQLException {
           	
           	DdpExportRule ddpExportRule = ddpExportRuleService.findDdpExportRule(rs.getInt("EXP_RULE_ID"));
               return ddpExportRule;
           }
       });
       return ddpExportrules;
   }
   /**
    * 
    * @param ddpCompany
    * @param role
    * @return
    */
   public List<DdpExportRule> getRuleIdByCompany(String role,String... ddpCompany) {
   	String cmpLst = "";
   	
   	String col= "drd.RDT_COMPANY=";
   	for(String company:ddpCompany){
   		cmpLst  +=col+"'"+company+"' OR ";
   	}
   	cmpLst = cmpLst.substring(0, cmpLst.lastIndexOf(" OR")).concat(" OR drd.RDT_PARTY_ID IS NULL");
       List<DdpExportRule> ddpExportrules = this.jdbcTemplate.query("SELECT DISTINCT dar.EXP_RULE_ID FROM dbo.DDP_EXPORT_RULE dar,dbo.DDP_RULE_DETAIL drd WHERE ("+cmpLst+") and dar.EXP_RULE_ID=drd.RDT_RULE_ID", new Object[] {  }, new RowMapper<DdpExportRule>() {

           public DdpExportRule mapRow(ResultSet rs, int rowNum) throws SQLException {
           	
           	DdpExportRule ddpExportRule = ddpExportRuleService.findDdpExportRule(rs.getInt("EXP_RULE_ID"));
               return ddpExportRule;
           }
       });
       return ddpExportrules;
   }
   
   public List<ExportRuleWrapper> getAllActiveExportRules(String role,String regionName,String localCompany,List<String> authoritiesLst,String... companys)
   {
	   String ruletype="EXPORT_RULE";
	   String cmpLst = "";
	   List<ExportRuleWrapper> ruleWrappers = null;
	   String query = "";
	   if(role.equalsIgnoreCase(env.getProperty("Admin_Role")))
	   {
		   query=Constant.SELECT_ALL_ACTIVE_EXPORT_RULES;
		   ruleWrappers = this.jdbcTemplate.query(query,new Object[]{ruletype}, new ExportRuleWrapper());
		   ruleWrappers.addAll(this.jdbcTemplate.query(Constant.SELECT_ACTIVE_EXPORT_RULE_FROM_PROP, new ExportRuleWrapper()));
	   }
	   else if(role.equalsIgnoreCase(env.getProperty("Region_Role")))
	   {
			   query=Constant.SELECT_ACTIVE_EXPORT_RULES_REGEION;
			   ruleWrappers = this.jdbcTemplate.query(query,new Object[]{ruletype,regionName}, new ExportRuleWrapper());
	   }
	   else if(role.equalsIgnoreCase("multi"))
	   {
		   if(companys != null)
		   {
			   String col= "dc.COM_COMPANY_CODE=";
		    	for(String company:companys){
		    		cmpLst  +=col+"'"+company+"' or ";
		    	}
		    	cmpLst = cmpLst.substring(0, cmpLst.lastIndexOf(" or"));
		   }
		   query=Constant.SELECT_ACTIVE_EXPORT_RULE_MULTI_COMPANY.replaceAll("dynamiccondition", cmpLst);
		   ruleWrappers = this.jdbcTemplate.query(query,new Object[]{ruletype}, new ExportRuleWrapper());
	   }
	   else
	   {
		   query=Constant.SELECT_ACTIVE_EXPORT_RULE_LOCAL;
		   ruleWrappers = this.jdbcTemplate.query(query,new Object[]{ruletype,localCompany}, new ExportRuleWrapper());
	   } 
	   return ruleWrappers;
   }
   public List<ExportRuleWrapper> getAllExportRulesByQuery()
   {
	   String ruletype="EXPORT_RULE";
	   List<ExportRuleWrapper> exporRuleWrappers = null;
	   exporRuleWrappers = this.jdbcTemplate.query(Constant.SELECT_ACTIVE_EXPORT_RULE_BY_QUERY, new Object[]{ruletype},new ExportRuleWrapper());
	   return exporRuleWrappers;
   }
   public List<ExportRuleWrapper> getAllExportRulesByQueryWithProps()
   {
	   String ruletype="EXPORT_RULE";
	   List<ExportRuleWrapper> exporRuleWrappers = null;
	   exporRuleWrappers = this.jdbcTemplate.query(Constant.SELECT_ACTIVE_EXPORT_RULE_FROM_PROP, new ExportRuleWrapper());
	   return exporRuleWrappers;
   }
   /**
    * 
    * @param partyId
    * @param companyCode
    * @param partyCodes
    * @return
    */
	public List<Integer> getRuleIdsByPartyCode(String partyId,String companyCode,List<String> brnList,LinkedList<Object> docTypes,String partyCode,Integer existingRuleID)
	{
		String dynamicCon = "";
    	for(int in=0;in<docTypes.size();in++)
    	{
    		dynamicCon +="(RDT_PARTY_CODE='"+partyCode+"' AND RDT_DOC_TYPE='"+docTypes.get(in)+"') or ";
    	}
    	dynamicCon = dynamicCon.substring(0, dynamicCon.lastIndexOf(" or"));
    	String conditionBranch = "";
    	for(int indexI=0;indexI<brnList.size();indexI++)
    	{
    		conditionBranch +="RDT_BRANCH='"+brnList.get(indexI)+"' or ";
    	}
    	conditionBranch = conditionBranch.substring(0, conditionBranch.lastIndexOf(" or"));
    	String query = "SELECT RDT_RULE_ID FROM DDP_RULE_DETAIL WHERE RDT_COMPANY= '"+companyCode+"' AND RDT_STATUS=0 AND RDT_PARTY_ID = '"+partyId+"' AND ("+dynamicCon+") AND ("+conditionBranch+") AND RDT_RULE_TYPE='EXPORT_RULE'";
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
		return rIDList;
	}
	 /**
	    * 
	    * @param CUSTOMId
	    * @return RULEID
	    */
		public List<Integer> getRuleIdsByPartyCode(String customID)
		{
			List<Integer> rIDList = this.jdbcTemplate.query(Constant.SELECT_EXP_ID_OF_SAME_CLIENT_ID, new Object[] {customID}, new RowMapper<Integer>(){
				@Override
				public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
					int rId;
					rId= rs.getInt("EXP_RULE_ID");
					return rId;
				}
			});
			return rIDList;
		}
//	@RequestMapping(value = "/{expRuleId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<String> deleteFromJson(@PathVariable("expRuleId") Integer expRuleId) {
//        DdpExportRule ddpExportRule = ddpExportRuleService.findDdpExportRule(expRuleId);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (ddpExportRule == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        ddpExportRuleService.deleteDdpExportRule(ddpExportRule);
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
   /**
    * 
    * @param str
    * @param c
    * @param n
    * @return
    */
   public static int nthOccurrence(String str, char c, int n) {
	    int pos = str.indexOf(c, 0);
	    while (n-- > 0 && pos != -1)
	        pos = str.indexOf(c, pos+1);
	    return pos;
	}
   /**
    * 
    * @param ftpServer
    * @return
    */
   public String getFtpLocation(String ftpServer,String ftpFolder,String secureProtocol)
   {
	   ftpServer = ftpServer.replace("\\", "/");
	   ftpFolder = ftpFolder.replace("\\", "/");
		String location = "";
		//insert correct format of ftp server
		if(ftpServer.startsWith("//"))
		{
			if (ftpFolder.startsWith("/"))
				location = secureProtocol+":"+ftpServer.concat(ftpFolder);
			else
				location = secureProtocol+":"+ftpServer+"/"+ftpFolder;
		}
		else if(ftpServer.startsWith(secureProtocol+"://"))
		{
			if (ftpFolder.startsWith("/") || ftpServer.endsWith("/")) 
				 location = ftpServer.concat(ftpFolder);
			 else 
				 location = ftpServer+"/"+ftpFolder;
		}
		else
		{
			if (ftpFolder.startsWith("/") || ftpServer.endsWith("/")) 
				 location = secureProtocol+"://"+ftpServer.concat(ftpFolder);
			 else 
				 location = secureProtocol+"://"+ftpServer+"/"+ftpFolder;
		}
		location = location.replace('\\', '/');
	  return location;
   }
   /**
    * 
    * @param uncPath
    * @return
    */
   public String getUncPath(String uncPath)
   {
	   uncPath = uncPath.replace("\\", "/");
		if(! uncPath.startsWith("//"))
		{
			uncPath = "//"+uncPath;
		}
	   return uncPath;
   }
   
   @RequestMapping(value = { "downloadQuery" },method = RequestMethod.POST)
   @ResponseBody
   public String getExportQuery(HttpServletRequest httpServletRequest,HttpServletResponse response,
		   						@RequestParam(name="querySourcevalue",required= false) int querySourcevalue,
		   						@RequestParam(name="customClientID",required= false) String customClientID,
		   						@RequestParam(name="exportqueryrowscount",required= false) int exportqueryrowscount,
		   						@RequestParam(name="exportQueryFromTxt",required= false) String exportQueryFromTxt,
		   						@RequestParam(name="partyCodeArr",required= false) String partyCodeArr,
		   						@RequestParam String[] partyValueArr,
		   						@RequestParam String jsonObj,
		   						@RequestParam(name="operatorArr",required= false) String operatorArr)
   {
	   Gson gson = new Gson();
	   Map<String, Object> retMap = new Gson().fromJson(jsonObj, new TypeToken<HashMap<String, Object>>() {}.getType());
	   List<String> lstPartyValue = new ArrayList<String>();
	   for(Object objkey: retMap.keySet())
		   lstPartyValue.add(retMap.get(objkey).toString());
	   
	   File generatedFile = null;
	   String strExportQuery = "";
	   if(querySourcevalue == 0)
	   {
		   strExportQuery = env.getProperty("export.rule."+customClientID+".customQuery");
		   strExportQuery = strExportQuery.replaceAll("\\.", ".");
	   }
	   if(querySourcevalue == 1)
	   {
		   partyCodeArr = partyCodeArr.substring(1,partyCodeArr.length()-1);
		   List<String> partyCodes = Arrays.asList(partyCodeArr.split(","));
		   
//		   partyValueArr = partyValueArr.substring(1,partyValueArr.length()-1);
//		   List<String> partyValues = Arrays.asList(partyValueArr.split(","));
		   
		   List<String> partyValues = null;
		   operatorArr = operatorArr.substring(1,operatorArr.length()-1);
		   List<String> operators = Arrays.asList(operatorArr.split(","));
		   
		   String partyCode="";
		    LinkedList<DdpExportQueryUi> exportQueryUILst = new LinkedList<DdpExportQueryUi>();
//	   		//Reading 
	   		int lineSeq = 0;
	   		for(int indexI=0; indexI<=exportqueryrowscount; indexI++)
	   		{
	   			partyCode = partyCodes.get(indexI).replaceAll("\"", "");
	   			String operator = null;
	   			if(partyCode !=null)
	   			{
	   				if(lineSeq != 0)
	   					operator = operators.get(indexI).replaceAll("\"", "");
	   				DdpExportQueryUi exportQueryUi = new DdpExportQueryUi();
	   				exportQueryUi.setEqiLineSeq(lineSeq);
	   				exportQueryUi.setEqiPartyCode(partyCode);
	   				exportQueryUi.setEqiPartyValue(lstPartyValue.get(indexI).toString());
	   				exportQueryUi.setEqiOperator(operator);
	   				exportQueryUILst.add(exportQueryUi);
	   				lineSeq++;
	   			}
	   		}
	   		strExportQuery = commonUtil.constructQueryWithExportQueryUIs(exportQueryUILst);
	   }
	   if(querySourcevalue == 2)
	   {
		   strExportQuery = commonUtil.constructQueryFromTXT(exportQueryFromTxt);
	   }
	   
	   generatedFile = ruleUtil.getGeneratedTxtFile(strExportQuery,generatedFile);
	   
	   return generatedFile.getAbsolutePath();
   }
   @RequestMapping(value = { "downloadFile" })
   public void downloadlFile(HttpServletRequest request,HttpServletResponse response)
   {
	   String filePath = request.getParameter("filePath");
	   File generatedFile = new File(filePath);
	 //write logic to download csv file
	   
	   if (generatedFile != null) 
		 {
	       	  File downloadFile = null;
		      downloadFile = generatedFile;
	       	 
	       	  try {
		        	  if (downloadFile != null) {
		        		  
		        		  	FileInputStream inputStream = new FileInputStream(downloadFile);
		        		  	response.setContentType("text/plain");
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
	 //write logic to download csv file Ends
   }
}
