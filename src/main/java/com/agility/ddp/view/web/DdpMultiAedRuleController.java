package com.agility.ddp.view.web;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.agility.ddp.core.logger.AuditLog;
import com.agility.ddp.core.task.DdpCreateMultiAedSchedulerTask;
import com.agility.ddp.dao.RuleDao;
import com.agility.ddp.data.domain.DdpBranch;
import com.agility.ddp.data.domain.DdpBranchService;
import com.agility.ddp.data.domain.DdpCommEmail;
import com.agility.ddp.data.domain.DdpCommEmailService;
import com.agility.ddp.data.domain.DdpCommOptionsLkpService;
import com.agility.ddp.data.domain.DdpCommOptionsSetup;
import com.agility.ddp.data.domain.DdpCommOptionsSetupService;
import com.agility.ddp.data.domain.DdpCommunicationSetup;
import com.agility.ddp.data.domain.DdpCommunicationSetupService;
import com.agility.ddp.data.domain.DdpCompany;
import com.agility.ddp.data.domain.DdpCompanyService;
import com.agility.ddp.data.domain.DdpCompressionSetup;
import com.agility.ddp.data.domain.DdpCompressionSetupService;
import com.agility.ddp.data.domain.DdpDocnameConvService;
import com.agility.ddp.data.domain.DdpDoctype;
import com.agility.ddp.data.domain.DdpDoctypeService;
import com.agility.ddp.data.domain.DdpEmailTriggerSetup;
import com.agility.ddp.data.domain.DdpEmailTriggerSetupService;
import com.agility.ddp.data.domain.DdpExportVersionSetup;
import com.agility.ddp.data.domain.DdpExportVersionSetupService;
import com.agility.ddp.data.domain.DdpGenSourceLkpService;
import com.agility.ddp.data.domain.DdpGenSourceSetup;
import com.agility.ddp.data.domain.DdpGenSourceSetupService;
import com.agility.ddp.data.domain.DdpMergeSetupService;
import com.agility.ddp.data.domain.DdpMultiAedRule;
import com.agility.ddp.data.domain.DdpMultiAedRuleService;
import com.agility.ddp.data.domain.DdpMultiEmails;
import com.agility.ddp.data.domain.DdpMultiEmailsService;
import com.agility.ddp.data.domain.DdpNotification;
import com.agility.ddp.data.domain.DdpNotificationService;
import com.agility.ddp.data.domain.DdpParty;
import com.agility.ddp.data.domain.DdpPartyService;
import com.agility.ddp.data.domain.DdpRateSetup;
import com.agility.ddp.data.domain.DdpRateSetupService;
import com.agility.ddp.data.domain.DdpRule;
import com.agility.ddp.data.domain.DdpRuleDetail;
import com.agility.ddp.data.domain.DdpRuleDetailService;
import com.agility.ddp.data.domain.DdpRuleService;
import com.agility.ddp.data.domain.DdpSchedulerService;
import com.agility.ddp.data.domain.DdpUser;
import com.agility.ddp.data.domain.DdpUserService;
import com.agility.ddp.view.util.AedRuleWrapper;
import com.agility.ddp.view.util.ApplicationUIProperties;
import com.agility.ddp.view.util.Constant;
import com.agility.ddp.view.util.RuleUtil;
import com.agility.ddp.view.util.SchedulerRuleWrapper;
import com.google.gson.Gson;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@RooWebJson(jsonObject = DdpMultiAedRule.class)
@Controller
@RequestMapping("/ddpmultiaedrules/list")
@RooWebScaffold(path = "ddpmultiaedrules", formBackingObject = DdpMultiAedRule.class)
public class DdpMultiAedRuleController {

	private static final Logger logger = LoggerFactory
			.getLogger(DdpMultiAedRuleController.class);

	@Autowired
	private ApplicationUIProperties env;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	DdpMultiAedRuleService ddpMultiAedRuleService;

	@Autowired
	DdpUserService ddpUserService;

	@Autowired
	DdpCommunicationSetupService ddpCommunicationSetupService;

	@Autowired
	DdpCompressionSetupService ddpCompressionSetupService;

	@Autowired
	DdpDocnameConvService ddpDocnameConvService;

	@Autowired
	DdpMergeSetupService ddpMergeSetupService;

	@Autowired
	DdpNotificationService ddpNotificationService;

	@Autowired
	DdpRuleService ddpRuleService;

	@Autowired
	DdpSchedulerService ddpSchedulerService;

	@Autowired
	DdpRuleDetailService ddpRuleDetailService;

	@Autowired
	DdpCommOptionsSetupService ddpCommOptionsSetupService;

	@Autowired
	DdpGenSourceSetupService ddpGenSourceSetupService;
	
	@Autowired
	DdpExportVersionSetupService ddpExportVersionSetupService;

	@Autowired
	DdpRateSetupService ddpRateSetupService;

	@Autowired
	DdpCompanyService ddpCompanyService;

	@Autowired
	DdpBranchService ddpBranchService;

	@Autowired
	DdpCommOptionsLkpService ddpCommOptionsLkpService;

	@Autowired
	DdpGenSourceLkpService ddpGenSourceLkpService;

	@Autowired
	DdpDoctypeService ddpDoctypeService;

	@Autowired
	DdpPartyService ddpPartyService;

	@Autowired
	DdpCommEmailService ddpCommEmailService;

	@Autowired
	DdpEmailTriggerSetupService ddpEmailTriggerSetupService;

	@Autowired
	DdpMultiEmailsService ddpMultiEmailsService;
	
	@Autowired
	RuleUtil ruleUtil;

	Calendar currentDate;

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "mediator", method = RequestMethod.GET)
	public String mediatorController(Model uiModel) {
		logger.info("DdpMultiAedRuleController.mediatorController Method Invoked.");
		List<DdpCompany> userCompanies = new ArrayList<DdpCompany>();
		String strUserName = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		Integer activeFlag = 1;
		DdpUser ddpUser = null;
		List<DdpUser> ddpUserLst = ruleUtil.checkUserExististance(strUserName);
		if (!ddpUserLst.isEmpty()) {
			ddpUser = ddpUserLst.get(0);
			activeFlag = 0;
			// check user company and status
			DdpCompany company = ddpCompanyService.findDdpCompany(ddpUser
					.getUsrCompanyCode().toString());
			if (company == null || company.getComStatus() == 1) {
				activeFlag = -1;
			}
		}
		if (activeFlag == 0) {
			List<String> listCompanies = new ArrayList<String>();
			userCompanies = ruleUtil.getAccessibleCompaniesLst(strUserName,env.getProperty("rule.maed.firstchars"));
			for (DdpCompany company : userCompanies) {
				listCompanies.add(company.getComCompanyCode());
			}
			 userCompanies.removeAll(ruleUtil.getListOfReadOnlyAccessibleCompanies(strUserName, env.getProperty("rule.maed.firstchars")));
			// Get the branches for the company
			List<DdpBranch> branchs = ruleUtil.findBranchbyCompany(ddpUser.getUsrCompanyCode());
			List<String> listBranches = new ArrayList<String>();
			for (DdpBranch strbrn : branchs) {
				listBranches.add(strbrn.getBrnBranchCode());
			}
			// userCompanies.add(userCompany);
			uiModel.addAttribute("accessToCreate",userCompanies.size());
			uiModel.addAttribute("userCompany", listCompanies);
			uiModel.addAttribute("userBranch", listBranches);

		} else if (activeFlag == -1) {
			uiModel.addAttribute("activeFlag", "User Country");
		} else {
			uiModel.addAttribute("activeFlag", "User");
		}
		logger.info("DdpMultiAedRuleController.mediatorController Method Executed Successfully.");
		return "ddpmultiaedrules/display";
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
	@RequestMapping(value = "listMultiAedRules", headers = "Accept=Application/json", method = RequestMethod.GET)
	@ResponseBody
	public String listMultiAedRules(
			@RequestParam(value = "_search", required = false) String search,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "filters", required = false) String filters,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "sortdir", required = false) String sortdir) {

		logger.info("DdpMultiAedRuleController.listMultiAedRules Method Invoked.");
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> records = new ArrayList<Object>(50);
		List<DdpMultiAedRule> result = null;
		List<SchedulerRuleWrapper> consolidateWrappers = new ArrayList<SchedulerRuleWrapper>();
		HashMap<Object, Object>[] rowdata;
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json;charset=utf-8");
		String[] compnies;
		String strUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		List<String> authoritiesLst = new ArrayList<String>();
		Set<String> multiCountries = new HashSet<String>();
		for (GrantedAuthority authority : authorities) {
			authoritiesLst.add(authority.getAuthority());
			if (authority.getAuthority().toLowerCase().startsWith(env.getProperty("rule.maed.firstchars").toLowerCase()+"_") ||
					  authority.getAuthority().toLowerCase().startsWith(env.getProperty("role_sub_all").toLowerCase()+"_") ) {
				String strCompany = authority.getAuthority().split("_")[1];
				DdpCompany company = ddpCompanyService.findDdpCompany(strCompany);
				if (company != null && company.getComStatus() == 0)
					multiCountries.add(strCompany.toUpperCase());
			}
			 if(authority.getAuthority().toLowerCase().startsWith(env.getProperty("Read_only_start").toLowerCase()))
			  {
				 if(authority.getAuthority().split("_")[2].equalsIgnoreCase(env.getProperty("read_only_region_sub")))
				  {
					  if(authority.getAuthority().split("_")[4].equalsIgnoreCase(env.getProperty("rule.maed.firstchars")) ||
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
					  if(authority.getAuthority().toLowerCase().split("_")[3].equalsIgnoreCase(env.getProperty("rule.maed.firstchars")) || 
							  authority.getAuthority().split("_")[3].equalsIgnoreCase(env.getProperty("role_sub_all")) )
					  {
						  multiCountries.add(authority.getAuthority().toUpperCase().split("_")[2].trim()); 
					  }
				  }
			  }
		}
		if (authoritiesLst.contains(env.getProperty("Admin_Role"))) {
			consolidateWrappers = getAllActiveAedRules("admin_role", null, null, null);
		}
		else
		{
			if(authoritiesLst.contains(env.getProperty("Region_Role"))) {
				String userRegion = ruleUtil.getUserRegion(strUserName);
				consolidateWrappers.addAll(getAllActiveAedRules("Region_role", userRegion, null,null));
			}
			if(!multiCountries.isEmpty()) {
				compnies = new String[multiCountries.size() + 1];
				Iterator<String> iterator = multiCountries.iterator();
				int i = 0;
				while (iterator.hasNext()) {
					compnies[i] = iterator.next();
					i++;
				}
				String userCompany = ruleUtil.getUserCompany(strUserName);
				compnies[multiCountries.size()] = userCompany;
				consolidateWrappers.addAll(getAllActiveAedRules("multi", null, null, compnies));
			}
			else {
				String userCompany = ruleUtil.getUserCompany(strUserName);
				compnies = new String[1];
				compnies[0] = userCompany;
				consolidateWrappers.addAll(getAllActiveAedRules("local", null, userCompany, null));
			}
			 if(! consolidateWrappers.isEmpty())
			  {
				  Map<Integer,SchedulerRuleWrapper> consolidateWrapperMap = new LinkedHashMap<Integer,SchedulerRuleWrapper>();
			         for(SchedulerRuleWrapper schedulerRuleWrapper:consolidateWrappers)
			        	 consolidateWrapperMap.put(schedulerRuleWrapper.getMaedRuleId(), schedulerRuleWrapper);
			         consolidateWrappers.clear();
			         consolidateWrappers.addAll(consolidateWrapperMap.values());
			  }
		}
		
		
		rowdata = new HashMap[consolidateWrappers.size()];
		map.put("rows", consolidateWrappers);
		map.put("page", page);

		Gson gson = new Gson();
		String json = gson.toJson(map);
		logger.info("DdpMultiAedRuleController.listMultiAedRules Method Executed Successfully.");
		return json;
	}

	@RequestMapping(value = "form", produces = "text/html")
	public String createForm(
			Model uiModel,
			@RequestParam(value = "customerId", required = false) String customerId) {

		logger.debug("DdpMultiAedRuleController.createForm : Method Invoked.)");
		String strUserName = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String strUserCompany = ruleUtil.getUserCompany(strUserName);
		RuleWrapper ruleWrapper = new RuleWrapper();
		if (customerId != null) {
			uiModel.addAttribute("duplicateCustID", customerId);
		}
		uiModel.addAttribute("rulewrapper", ruleWrapper);
		populateEditForm(uiModel, new DdpMultiAedRule(), strUserCompany,"create");
		return "ddpmultiaedrules/create";
	}

	/**
	 * 
	 * @param uiModel
	 * @param httpServletRequest
	 * @return
	 */
	@RequestMapping(value = "/branchbycountry", method = RequestMethod.POST, produces = "text/html")
	public String branchbyCountry(Model uiModel,
			HttpServletRequest httpServletRequest) {
		logger.info("DdpMultiAedRuleController.branchbyCountry(Model uiModel, HttpServletRequest httpServletRequest) Method Inlvoked.");
		// Get the company ID
		String companyID1 = httpServletRequest.getParameter("companyID");
		uiModel.addAttribute("rulewrapper", new RuleWrapper());
		// send company code in parameter to populate Branch for the company
		// code
		populateEditForm(uiModel, new DdpMultiAedRule(), companyID1.toString(),"branchbyCountry");
		logger.info("DdpMultiAedRuleController.branchbyCountry(Model uiModel, HttpServletRequest httpServletRequest) Executed Successfully.");
		return "ddpaedrules/create";
	}

	@RequestMapping(value = "/branchByCountryAsString", method = RequestMethod.POST, headers = "Accept=Application/json")
	@ResponseBody
	public String branchByCountryAsString(HttpServletRequest httpServletRequest) {
		String companyId = httpServletRequest.getParameter("companyID");
		List<DdpBranch> branches = ruleUtil.findBranchbyCompany(companyId);
		String res = "";
		for (DdpBranch branch : branches) {
			res += branch.getBrnBranchCode() + ",";
		}
		if (res.trim().length() > 0)
			res = res.substring(0, res.lastIndexOf(","));
		return res;
	}

	@RequestMapping(value = { "", "list" }, params = "checkDupRule")
	@ResponseBody
	public int checkDuplicateRule(
			HttpServletRequest httpServletRequest,
			@RequestParam(value = "company", required = false) String company,
			@RequestParam(value = "clientID", required = false) String clientID,
			@RequestParam(value = "partyCode", required = false) String partyCode,
			@RequestParam(value = "documentsArr[]", required = false) String[] documentsArr,
			@RequestParam(value = "branchList[]", required = false) String[] branchList) {
		// Enumeration enumeration = httpServletRequest.getParameterNames();
		// while(enumeration.hasMoreElements())
		// {
		// System.out.println((String)enumeration.nextElement()+" - "+httpServletRequest.getParameter((String)enumeration.nextElement()));
		// }
		LinkedList<Object> partyCodes = new LinkedList<Object>();
		partyCodes.add(partyCode);
		LinkedList<Object> documentsList = new LinkedList<Object>();
		documentsList.addAll(Arrays.asList(documentsArr[0]));
		int ruleSize = ruleUtil.checkDuplicateRule("MULTI_AED_RULE", company, Arrays.asList(branchList),clientID, partyCodes,documentsList, null);
		if (ruleSize > 0)
			return 0;
		else
			return 1;
	}

	@AuditLog(message = "Created")
	@Transactional
	@RequestMapping(params = { "create" }, method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid RuleWrapper ruleWrapper,BindingResult bindingResult, Model uiModel,HttpServletRequest httpServletRequest) {

		logger.info("DdpMultiAedRuleController.create Method Invoked.");
		String strUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		String strUserCompany = ruleUtil.getUserCompany(strUserName);
		DdpMultiAedRule ddpMultiAedRule = null;
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, ddpMultiAedRule, strUserCompany,"create");
			return "ddpmultiaedrules/create";
		}

		String rowCount = httpServletRequest.getParameter("rowscount");
		LinkedList<Object> docList = new LinkedList<Object>();
		LinkedList<Object> partyList = new LinkedList<Object>();
		LinkedList<Object> genSourceList = new LinkedList<Object>();
		LinkedList<Object> rateSetupList = new LinkedList<Object>();
		LinkedList<Object> reqFlagList = new LinkedList<Object>();
		LinkedList<Object> exportVersionList = new LinkedList<Object>();
		LinkedList<Object> excludeList = new LinkedList<Object>();
		LinkedList<Object> sequenceList = new LinkedList<Object>();

		int rCount = Integer.parseInt(rowCount);
		String docTypefalg = null;
		String genSourcefalg = null;
		String rateSetupfalg = null;
		String reqFlag = null;
		String exportVersionfalg = null;
		String excludeFalg = null;
		String sequencefalg = null;

		for (int i = 1; i <= rCount; i++) {
			int count = i;
			docTypefalg = httpServletRequest.getParameter("selectDdpDoctype"+ count);
			genSourcefalg = httpServletRequest.getParameter("selectDdpGenSource" + count);
			rateSetupfalg = httpServletRequest.getParameter("selectDdpRate"+ count);
			reqFlag = httpServletRequest.getParameter("selectReqFlag" + count);
			exportVersionfalg = httpServletRequest.getParameter("selectVersion"+ count);
			excludeFalg = httpServletRequest.getParameter("selectExclude"+ count);
			sequencefalg = httpServletRequest.getParameter("sequenceNum"+ count);
			if (docTypefalg != null) {
				docList.add(docTypefalg);
				genSourceList.add(genSourcefalg);
				reqFlagList.add(reqFlag);
				rateSetupList.add(rateSetupfalg);
				exportVersionList.add(exportVersionfalg);
				excludeList.add(excludeFalg);
				sequenceList.add(sequencefalg);
				rCount = rCount + 1;
			}
		}

		// get partyId
		String partyId = ruleWrapper.getDdpRuleDetail().getRdtPartyId();
		DdpParty ddpParty = ruleWrapper.getDdpRuleDetail().getRdtPartyCode();
		partyList.add(ddpParty.getPtyPartyCode());
		String companycode = ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode();
		String[] selectedBranchList = httpServletRequest.getParameterValues("lstBranch");
		// Flag to check the ALL Branches selected
		boolean flgAllBranches = false;
		// Check for the All branch selected, if selected get all the branch and
		// iterate the loop and save the rule details
		for (String str : selectedBranchList) {
			if (str.equalsIgnoreCase("ALL")) {
				flgAllBranches = true;
				break;
			}
		}
		List<String> brnList = Arrays.asList(selectedBranchList);
		if (brnList.contains("ALL")) {
			brnList = new ArrayList<String>();
			brnList.add("ALL");
		}
		// check this party id is available or not.
//		int ruleSize = ruleUtil.checkDuplicateRule("MULTI_AED_RULE",companycode, brnList, partyId, partyList,docList, null);
//		if (ruleSize > 0) {
//			return "redirect:/ddpmultiaedrules/list/form?customerId=" + partyId;
//		}

		String triggerSpec = httpServletRequest.getParameter("triggerSpec");
		/**
		 * ##### Save DDp Communication Email ######
		 * 
		 * 1) Save DDPCommunication Email 2) Get the saved communication email
		 * id
		 * 
		 */
		// Save DDp Communication Email
		// set unc path to ddpcommEmail
		String evn = env.getProperty("app.evn").trim();
		String uncPath = env.getProperty("rule.unc.path." + evn);
		String radioButtonforEmailSetup = httpServletRequest.getParameter("radioButtonforEmailSetup");
		Integer commEmailId = 0;
		String strProtocol = null;
		boolean isEmailSetupByDest = false;
		if(radioButtonforEmailSetup.equalsIgnoreCase("emailSetupBySourceRB"))
		{
			ruleWrapper.getDdpCommEmail().setCemUncPath(uncPath);
			ddpCommEmailService.saveDdpCommEmail(ruleWrapper.getDdpCommEmail());
			// Get the auto increments id commEmailId
			commEmailId = ruleWrapper.getDdpCommEmail().getCemEmailId();
			strProtocol = env.getProperty("rule.comm.prototocol");
		}
		else
		{
			isEmailSetupByDest = true;
			strProtocol = env.getProperty("rule.muti.prototocol");
		}
		/**********************************************************************************************************/
		/**
		 * ##### Save DDPComminucation setup values ######
		 * 
		 * 1) Save DDPComminucation setup 2) Get the saved DDPComminucation
		 * setup Id
		 * 
		 */
		// Set the DDPComminucation setup values
		// Protocol hardcoded
		// set date
		currentDate = Calendar.getInstance();
		ruleWrapper.getDdpCommunicationSetup().setCmsCommunicationProtocol(strProtocol);
		ruleWrapper.getDdpCommunicationSetup().setCmsProtocolSettingsId(commEmailId.toString());
		ruleWrapper.getDdpCommunicationSetup().setCmsStatus(0);
		ruleWrapper.getDdpCommunicationSetup().setCmsCreatedBy(strUserName);
		// ruleWrapper.getDdpCommunicationSetup().setCmsCreatedDate(Constant.CURRENTDATE);
		ruleWrapper.getDdpCommunicationSetup().setCmsCreatedDate(currentDate);
		ruleWrapper.getDdpCommunicationSetup().setCmsModifiedBy(strUserName);
		// ruleWrapper.getDdpCommunicationSetup().setCmsModifiedDate(Constant.CURRENTDATE);
		ruleWrapper.getDdpCommunicationSetup().setCmsModifiedDate(currentDate);
		// Save DDPCommunicationSetup
		ddpCommunicationSetupService.saveDdpCommunicationSetup(ruleWrapper.getDdpCommunicationSetup());
		// Get the cms_communication_id
		Integer comSetupId = ruleWrapper.getDdpCommunicationSetup().getCmsCommunicationId();
		/**********************************************************************************************************/
		
		if(isEmailSetupByDest)
		{
			// Read Recipient  and store into table 
			String sourceType = null;
			int multiEmailsCount = Integer.parseInt(httpServletRequest.getParameter("reciepantRowCount"));
			for(int indexI=0; indexI <= multiEmailsCount; indexI++)
			{
				sourceType = httpServletRequest.getParameter("sourceType"+indexI);
				if(sourceType != null)
				{
					String sourceValue = httpServletRequest.getParameter("sourceValue"+indexI);
					String toAddress = httpServletRequest.getParameter("toAddress"+indexI);
					String ccAddress = httpServletRequest.getParameter("ccAddress"+indexI);
					DdpMultiEmails ddpMultiEmails = new DdpMultiEmails();
					if(sourceType.equalsIgnoreCase("company"))
						ddpMultiEmails.setMesDestCompany(sourceValue.trim());
					if(sourceType.equalsIgnoreCase("Region"))
						ddpMultiEmails.setMesDestRegion(sourceValue.trim());
					ddpMultiEmails.setMesEmailTo(toAddress.trim());
					ddpMultiEmails.setMesEmailCc(ccAddress.trim());
					//set source, and value
					ddpMultiEmails.setMesCmsId(ruleWrapper.getDdpCommunicationSetup());
					ddpMultiEmailsService.saveDdpMultiEmails(ddpMultiEmails);
				}
			}
		}

		/************* saving DDP_COMPRESSION_SETUP ********************************/
		DdpCompressionSetup ddpCompressionSetup = new DdpCompressionSetup();

		String attachmentLmt = httpServletRequest.getParameter("attachmentLmt");
		String attachCount = httpServletRequest.getParameter("attachCount");
		ddpCompressionSetup.setCtsEmailAttachmentLimit(Integer
				.parseInt(attachmentLmt));
		ddpCompressionSetup.setCtsNoOfFilesAttached(Integer
				.parseInt(attachCount));
		if (attachCount.equals("1")) {
			String fileCompression = httpServletRequest
					.getParameter("fileCompression");
			ddpCompressionSetup.setCtsCompressionLevel(fileCompression);
			if (fileCompression.equals("zip")) {
				String eachZipSize = httpServletRequest
						.getParameter("valZipSize");
				ddpCompressionSetup.setCtsCompressionSize(Integer
						.parseInt(eachZipSize));
			}
		}
		ddpCompressionSetupService.saveDdpCompressionSetup(ddpCompressionSetup);

		/************* DDP_COMPRESSION_SETUP saved ********************************/

		// save DdpNotification
		DdpNotification ddpNotification = ruleWrapper.getDdpNotification();
		if (ddpNotification.getNotFailureEmailAddress() == null
				|| ddpNotification.getNotFailureEmailAddress().equals("")) {
			ddpNotification.setNotFailureEmailAddress(env
					.getProperty("email.cc"));
		}
		ddpNotification.setNotSuccessEmailAddress(env.getProperty("email.cc"));
		ddpNotification.setNotCreatedBy(strUserName);
		ddpNotification.setNotCreatedDate(currentDate);
		ddpNotification.setNotModifiedBy(strUserName);
		ddpNotification.setNotModifiedDate(currentDate);
		ddpNotification.setNotStatus(0);
		ddpNotificationService.saveDdpNotification(ddpNotification);

		/**********************************************************************************************************/
		/**
		 * ##### Constructing Rule name ######
		 * 
		 * 1) Get the Selected Country and get the Region, Company, Branch 2)
		 * Uploded the user entered document type.
		 * 
		 */
		String ruleDescription = "";
		DdpCompany ddpCompany = ddpCompanyService.findDdpCompany(ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode());
		String ruleName = ddpCompany.getComRegion()	+ "_"+ ruleWrapper.getDdpRuleDetail().getRdtCompany()
						.getComCompanyCode() + "_"	+ ruleWrapper.getDdpRuleDetail().getRdtPartyId();
		 if(ruleWrapper.getDdpRule().getRulDescription().equals(""))
	        ruleDescription = ruleWrapper.getDdpRuleDetail().getRdtPartyId();
	     else
	        ruleDescription = ruleWrapper.getDdpRule().getRulDescription();
		 ruleDescription = ddpCompany.getComRegion()+ "_"+ ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode() + "_"	+ ruleDescription;
		/**********************************************************************************************************/
		/**
		 * ##### Save Rule table ######
		 * 
		 * 1) Save Rule 2) Get the saved Rule Id
		 * 
		 */
		// Set Rule and set status as active as Active(0)
		ruleWrapper.getDdpRule().setRulName(ruleName);
		ruleWrapper.getDdpRule().setRulDescription(ruleDescription);
		ruleWrapper.getDdpRule().setRulStatus(0);
		ruleWrapper.getDdpRule().setRulCreatedBy(strUserName);
		ruleWrapper.getDdpRule().setRulCreatedDate(currentDate);
		ruleWrapper.getDdpRule().setRulModifiedBy(strUserName);
		ruleWrapper.getDdpRule().setRulModifiedDate(currentDate);
		// Save Rule
		ddpRuleService.saveDdpRule(ruleWrapper.getDdpRule());
		// Get Rule Id
		Integer ruleId = ruleWrapper.getDdpRule().getRulId();
		/**********************************************************************************************************/
		/**
		 * ##### Save AED Rule table ######
		 * 
		 * 1) Save AED Rule 2) Get the saved AED Rule Id
		 * 
		 */
		// Set Value for MAED Rule Id
		ruleWrapper.getDdpMultiAedRule().setMaedCommunicationId(
				ddpCommunicationSetupService
						.findDdpCommunicationSetup(comSetupId));
		// Default value is 0/1 , 0-yes(is required), 1-No(is not required)
		// 0 - Yes, 1-No
		ruleWrapper.getDdpMultiAedRule().setMaedIsPartyCheckRequired(0);
		// set compression
		ruleWrapper.getDdpMultiAedRule().setMaedNotificationId(ddpNotification);
		ruleWrapper.getDdpMultiAedRule().setMaedCompressionId(
				ddpCompressionSetup);
		ruleWrapper.getDdpMultiAedRule().setMaedStatus(0);
		// Set the Activation date , activate the rule on date,
		// if no date is mentioned by the user, activate the rule at the time of
		// creation
		if (null == ruleWrapper.getDdpRuleDetail().getRdtActivationDate()
				|| "".equals(ruleWrapper.getDdpRuleDetail()
						.getRdtActivationDate())) {
			ruleWrapper.getDdpMultiAedRule().setMaedActivationDate(currentDate);
		} else {
			ruleWrapper.getDdpMultiAedRule().setMaedActivationDate(
					ruleWrapper.getDdpRuleDetail().getRdtActivationDate());
		}
		ruleWrapper.getDdpMultiAedRule().setMaedCreatedBy(strUserName);
		ruleWrapper.getDdpMultiAedRule().setMaedCreatedDate(currentDate);
		ruleWrapper.getDdpMultiAedRule().setMaedModifiedBy(strUserName);
		ruleWrapper.getDdpMultiAedRule().setMaedModifiedDate(currentDate);
		// Insert Rule
		ruleWrapper.getDdpMultiAedRule().setMaedRuleId(ruleId);
		// Save MAED Rule
		ddpMultiAedRuleService.saveDdpMultiAedRule(ruleWrapper
				.getDdpMultiAedRule());
		/**********************************************************************************************************/

		/************* save DDP_EMAIL_TRIGGER_SETUP ******************************/
		DdpEmailTriggerSetup ddpEmailTriggerSetup = new DdpEmailTriggerSetup();
		ddpEmailTriggerSetup.setEtrRuleId(ruleWrapper.getDdpRule());
		ddpEmailTriggerSetup.setEtrTriggerName(httpServletRequest.getParameter("selTgrCriteria"));
		ddpEmailTriggerSetup.setEtrDocTypes(docList.get(0).toString());
		ddpEmailTriggerSetup.setEtrRetries(Integer.parseInt(httpServletRequest.getParameter("noOfRetries")));
		if(httpServletRequest.getParameter("selTgrCriteria").trim().equals("consignmentID"))
		{
			ddpEmailTriggerSetup.setEtrInclude(httpServletRequest.getParameter("selinclude"));
		}
		// if(triggerSpec.equalsIgnoreCase(env.getProperty("emailtrigger.sDocs")))
		// {
		// ddpEmailTriggerSetup.setEtrTriggerName(com.agility.ddp.core.util.Constant.TRIGGER_NAME_SPECIFIC_DOCS);
		// String[] selectedDocsList =
		// httpServletRequest.getParameterValues("lstDocs");
		// String docsLst = "";
		// for(String docs:selectedDocsList)
		// {
		// docsLst = docsLst.concat(docs+",");
		// }
		// docsLst = docsLst.substring(0,docsLst.lastIndexOf(","));
		// ddpEmailTriggerSetup.setEtrDocTypes(docsLst);
		// }
		// else
		// if(triggerSpec.equalsIgnoreCase(env.getProperty("emailtrigger.aMin")))
		// {
		// ddpEmailTriggerSetup.setEtrTriggerName(com.agility.ddp.core.util.Constant.TRIGGER_NAME_ALL_OR_MINIMUM);
		// ddpEmailTriggerSetup.setEtrDocSelection(httpServletRequest.getParameter("selAMinDocs").toString());
		// }
		// else
		// if(triggerSpec.equalsIgnoreCase(env.getProperty("emailtrigger.sTime")))
		// {
		// /**set the cron Expression**/
		// String everyMinute = httpServletRequest.getParameter("minutespan");
		// String everyHour = httpServletRequest.getParameter("hourspan");
		// String dayofMonth = httpServletRequest.getParameter("dayOfMonth");
		// String dayofWeek = httpServletRequest.getParameter("dayOfWeek");
		// String weekflag= httpServletRequest.getParameter("weekflag");
		// String monthflag= httpServletRequest.getParameter("monthflag");
		// String hours = httpServletRequest.getParameter("hour");
		// String mins = httpServletRequest.getParameter("minute");
		// if(weekflag.equals("0") && monthflag.equals("0"))
		// {
		// dayofMonth="*";
		// dayofWeek="?";
		// }
		// if(weekflag.equals("0") && monthflag.equals("1"))
		// {
		// dayofWeek="?";
		// }
		// if(monthflag.equals("0") && weekflag.equals("1"))
		// {
		// dayofMonth="?";
		// }
		// if(! everyMinute.equals("0"))
		// mins=mins+"/"+everyMinute;
		// if(! everyHour.equals("0"))
		// hours=hours+"/"+everyHour;
		// String cronExpression =
		// "0 "+mins+" "+hours+" "+dayofMonth+" * "+dayofWeek;
		//
		// ddpEmailTriggerSetup.setEtrTriggerName(com.agility.ddp.core.util.Constant.TRIGGER_NAME_SPECIFIC_TIME);
		// ddpEmailTriggerSetup.setEtrCronExpression(cronExpression);
		// }
		// else
		// {
		// ddpEmailTriggerSetup.setEtrTriggerName(com.agility.ddp.core.util.Constant.TRIGGER_NAME_IMMEDIATE);
		// }
		ddpEmailTriggerSetupService
				.saveDdpEmailTriggerSetup(ddpEmailTriggerSetup);

		/************* DDP_EMAIL_TRIGGER_SETUP saved ******************************/

		// start scheduler dynamically if specific time selected
		if (triggerSpec.equalsIgnoreCase(env.getProperty("emailtrigger.sTime"))) {
			DdpCreateMultiAedSchedulerTask ddpCreateMultiAedSchedulerTask = applicationContext
					.getBean("ddpCreateMultiAedSchedulerTask",
							DdpCreateMultiAedSchedulerTask.class);
			if (ddpCreateMultiAedSchedulerTask
					.createNewScheduler(ddpEmailTriggerSetup))
				;
			logger.info("DdpMultiAedRuleController.create : new Scheduler created for Rule Id :"
					+ ruleId);
		}


		/*******************************************************************************/
		/**
		 * ##### Save Rule Details table ######
		 * 
		 * 1) Loop all the selected branches 2) Inner Loop will be selected
		 * document types, party code for that branches.
		 * 
		 */
		/****************************************************************************/
		String department = ((ruleWrapper.getDdpRuleDetail().getRdtDepartment() != null && ruleWrapper
				.getDdpRuleDetail().getRdtDepartment().trim().length() > 0) ? ruleWrapper
				.getDdpRuleDetail().getRdtDepartment().trim()
				: null);

		if (department != null && department.endsWith(","))
			department = department.substring(0, department.length() - 1);

		// Loop the branch and add the rule details
		for (String strBranch : brnList) {
			// get the document type and party code
			for (int i = 0; i < docList.size(); i++) {
				DdpRuleDetail ddpRuleDetail = new DdpRuleDetail();
				DdpRule ddpRules = ddpRuleService.findDdpRule(ruleId);
				ddpRuleDetail.setRdtRuleId(ddpRules);
				// Set Company code
				ddpRuleDetail.setRdtCompany(ddpCompany);
				// Get Branch Detail based on selected branch code and country
				// code.
				DdpBranch ddpBranch = ddpBranchService.findDdpBranch(strBranch);
				ddpRuleDetail.setRdtBranch(ddpBranch);
				// Get DocTypeDoc
				DdpDoctype ddpDoctypes = ddpDoctypeService
						.findDdpDoctype(docList.get(i).toString());
				ddpRuleDetail.setRdtDocType(ddpDoctypes);
				ddpRuleDetail.setRdtDocSequence(Integer.parseInt(sequenceList
						.get(i).toString()));
				// Get PartyBean
				ddpRuleDetail.setRdtPartyCode(ddpParty);
				// Generate system null
				// Schedular id null
				// Save the communication lookup id in RuleDetail table
				DdpCommunicationSetup ddpCommunicationSetup = ddpCommunicationSetupService
						.findDdpCommunicationSetup(comSetupId);
				ddpRuleDetail.setRdtCommunicationId(ddpCommunicationSetup);
				// Set status as active for creating rule
				ddpRuleDetail.setRdtStatus(0);
				// Set Party code
				ddpRuleDetail.setRdtPartyId(ruleWrapper.getDdpRuleDetail()
						.getRdtPartyId().trim());
				// set created by , modified by and created date and modified
				// date
				ddpRuleDetail.setRdtCreatedBy(strUserName);
				ddpRuleDetail.setRdtCreatedDate(currentDate);
				ddpRuleDetail.setRdtModifiedBy(strUserName);
				ddpRuleDetail.setRdtModifiedDate(currentDate);
				ddpRuleDetail.setRdtDepartment(department);
				// set updated Activation date
				if (null == ruleWrapper.getDdpRuleDetail()
						.getRdtActivationDate()
						|| "".equals(ruleWrapper.getDdpRuleDetail()
								.getRdtActivationDate())) {
					ddpRuleDetail.setRdtActivationDate(currentDate);
				} else {
					ddpRuleDetail.setRdtActivationDate(ruleWrapper
							.getDdpRuleDetail().getRdtActivationDate());
				}
				// Set Rule Type
				ddpRuleDetail.setRdtRuleType(env
						.getProperty("rule.type.consolidate"));
				// set Force Type
				ddpRuleDetail.setRdtForcedType(Integer.parseInt(reqFlagList
						.get(i).toString()));
				int relevantType = (i == 0) ? 1 : 0;
				ddpRuleDetail.setRdtRelavantType(relevantType);
				ddpRuleDetail.setRdtInclude(excludeList.get(i).toString());
				// Save rule details
				ddpRuleDetailService.saveDdpRuleDetail(ddpRuleDetail);
				// Get save rule detail id
				Integer ruleDetailId = ddpRuleDetail.getRdtId();

				// Save Rated/Not Rated document
				DdpRateSetup ddpRateSetup = new DdpRateSetup();
				ddpRateSetup.setRtsRdtId(ddpRuleDetail);
				ddpRateSetup.setRtsOption(rateSetupList.get(i).toString());
				ddpRateSetup.setRtsCreatedBy(strUserName);
				ddpRateSetup.setRtsCreatedDate(currentDate);
				ddpRateSetup.setRtsModifiedBy(strUserName);
				ddpRateSetup.setRtsModifiedDate(currentDate);
				ddpRateSetupService.saveDdpRateSetup(ddpRateSetup);

				// save Generate System
				DdpGenSourceSetup ddpGenSourceSetup = new DdpGenSourceSetup();
				ddpGenSourceSetup.setGssRdtId(ddpRuleDetail);
				ddpGenSourceSetup.setGssOption(genSourceList.get(i).toString());
				ddpGenSourceSetup.setGssCreatedBy(strUserName);
				ddpGenSourceSetup.setGssCreatedDate(currentDate);
				ddpGenSourceSetup.setGssModifiedBy(strUserName);
				ddpGenSourceSetup.setGssModifiedDate(currentDate);
				ddpGenSourceSetupService.saveDdpGenSourceSetup(ddpGenSourceSetup);
				
					 //save Export Version Setup
	       		 DdpExportVersionSetup ddpExportVersionSetup = new DdpExportVersionSetup();
	       		 ddpExportVersionSetup.setEvsRdtId(ddpRuleDetail);
	       		 ddpExportVersionSetup.setEvsOption(exportVersionList.get(i).toString());
	       		 ddpExportVersionSetup.setEvsCreatedBy(strUserName);
	       		 ddpExportVersionSetup.setEvsCreatedDate(currentDate);
	       		 ddpExportVersionSetup.setEvsModifiedBy(strUserName);
	       		 ddpExportVersionSetup.setEvsModifiedDate(currentDate);
	       		 ddpExportVersionSetupService.saveDdpExportVersionSetup(ddpExportVersionSetup);

			}
		}
		uiModel.asMap().clear();

		logger.info("DdpMultiAedRuleController.create Method Executed Successfully.");
		return "redirect:/ddpmultiaedrules/list/list/" + ruleId;
	}

	@RequestMapping(value = "/list/{maedRuleId}", produces = "text/html")
	public String show(@PathVariable("maedRuleId") Integer maedRuleId, Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		List<RuleDao> ruleDaos = getRuleDetailForRuleIdShow(maedRuleId.toString());
		DdpMultiAedRule multiAedRule = ddpMultiAedRuleService.findDdpMultiAedRule(maedRuleId);
		List<DdpEmailTriggerSetup> ddpEmailTriggerSetups = findDdpTriggerSetupByRuleId(maedRuleId);
		DdpRule ddpRule = ddpRuleService.findDdpRule(maedRuleId);
		// if(ddpRule.getRulStatus()!=1){
		uiModel.addAttribute("ddpmaedrule", ruleDaos);
		uiModel.addAttribute("itemId", maedRuleId);
		uiModel.addAttribute("itemName", ddpRule.getRulDescription());
		uiModel.addAttribute("isActive", multiAedRule.getMaedStatus().toString());
		uiModel.addAttribute("notification", multiAedRule.getMaedNotificationId());
		uiModel.addAttribute("compression", multiAedRule.getMaedCompressionId());
		uiModel.addAttribute("ddpEmailTriggerSetup", ddpEmailTriggerSetups.get(0));
		uiModel.addAttribute("isActive", multiAedRule.getMaedStatus().toString());
		List<DdpCompany> userCompanies = new ArrayList<DdpCompany>();
	    String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
	    userCompanies = ruleUtil.getAccessibleCompaniesLst(strUserName, env.getProperty("rule.maed.firstchars"));
	    List<DdpCompany> readonlycompanies = ruleUtil.getListOfReadOnlyAccessibleCompanies(strUserName, env.getProperty("rule.maed.firstchars"));
	    userCompanies.removeAll(readonlycompanies);
	    int restrictQuickLinks = 0; 
	    if(! ruleDaos.isEmpty())
        {
        	if(readonlycompanies.contains(ddpCompanyService.findDdpCompany(ruleDaos.get(0).getRdtCompanyCode())))
        		restrictQuickLinks = 1;
        }
	    uiModel.addAttribute("restrictQuickLinks",restrictQuickLinks);
	    uiModel.addAttribute("accessToCreate",userCompanies.size());
		// }
		return "ddpmultiaedrules/show";
	}

	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			uiModel.addAttribute("ddpmultiaedrules", ddpMultiAedRuleService
					.findDdpMultiAedRuleEntries(firstResult, sizeNo));
			float nrOfPages = (float) ddpMultiAedRuleService
					.countAllDdpMultiAedRules() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("ddpmultiaedrules",
					ddpMultiAedRuleService.findAllDdpMultiAedRules());
		}
		addDateTimeFormatPatterns(uiModel);
		return "ddpmultiaedrules/list";
	}

	@AuditLog(message = "Updated")
	@Transactional
	@RequestMapping(params = { "update" }, method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid RuleWrapper ruleWrapper,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {

		logger.info("DdpMultiAedRuleController.update Method Invoked.");
		String strUserName = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String strUserCompany = ruleUtil.getUserCompany(strUserName);
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, ruleWrapper.getDdpMultiAedRule(),strUserCompany,"update");
			return "ddpmultiaedrules/update";
		}

		String rowCount = httpServletRequest.getParameter("rowscount");
		LinkedList<Object> docList = new LinkedList<Object>();
		LinkedList<Object> partyList = new LinkedList<Object>();
		LinkedList<Object> genSourceList = new LinkedList<Object>();
		LinkedList<Object> rateSetupList = new LinkedList<Object>();
		LinkedList<Object> reqFlagList = new LinkedList<Object>();
		LinkedList<Object> exportVersionList = new LinkedList<Object>();
		LinkedList<Object> excludeList = new LinkedList<Object>();
		LinkedList<Object> sequenceList = new LinkedList<Object>();

		int rCount = Integer.parseInt(rowCount);
		String docTypefalg = null;
		String genSourcefalg = null;
		String rateSetupfalg = null;
		String reqFlag = null;
		String exportVersionfalg = null;
		String excludefalg = null;
		String sequencefalg = null;

		for (int i = 1; i <= rCount; i++) {
			int count = i;
			docTypefalg = httpServletRequest.getParameter("selectDdpDoctype"+ count);
			genSourcefalg = httpServletRequest.getParameter("selectDdpGenSource" + count);
			rateSetupfalg = httpServletRequest.getParameter("selectDdpRate"	+ count);
			reqFlag = httpServletRequest.getParameter("selectReqFlag" + count);
			exportVersionfalg = httpServletRequest.getParameter("selectVersion"+ count);
			excludefalg = httpServletRequest.getParameter("selectExclude"+ count);
			sequencefalg = httpServletRequest.getParameter("sequenceNum"+ count);
			if (docTypefalg != null) {
				docList.add(docTypefalg);
				genSourceList.add(genSourcefalg);
				rateSetupList.add(rateSetupfalg);
				reqFlagList.add(reqFlag);
				exportVersionList.add(exportVersionfalg);
				excludeList.add(excludefalg);
				sequenceList.add(sequencefalg);
				rCount = rCount + 1;
			}
		}
		String ruleStatus = httpServletRequest.getParameter("status");
		String[] selectedBranchList = httpServletRequest.getParameterValues("lstBranch");
		List<String> brnList = Arrays.asList(selectedBranchList);
		if (brnList.contains("ALL")) {
			brnList = new ArrayList<String>();
			brnList.add("ALL");
		}
		String radioButtonforEmailSetup = httpServletRequest.getParameter("radioButtonforEmailSetup");
		String communicationProtocol = null;
		boolean isEmailSetupByDest = false;
		if(radioButtonforEmailSetup.equalsIgnoreCase("emailSetupBySourceRB"))
		{
			communicationProtocol = env.getProperty("rule.comm.prototocol");
		}
		else
		{
			communicationProtocol = env.getProperty("rule.muti.prototocol");
			isEmailSetupByDest = true;
		}
		// Get Status
		String status = httpServletRequest.getParameter("status");
		String triggerSpec = httpServletRequest.getParameter("triggerSpec");

		// Prepare RuleDetail
		Integer maedRuleId = ruleWrapper.getDdpMultiAedRule().getMaedRuleId();

		/*
		 * verify whether partyId value is modified. if modified check whether
		 * new partyId value is already exist.
		 */
		TypedQuery<DdpRuleDetail> rdtquery = DdpRuleDetail
				.findDdpRuleDetailsByRdtRuleId(ddpRuleService
						.findDdpRule(maedRuleId));
		List<DdpRuleDetail> lii = rdtquery.getResultList();
		String newPartyId = ruleWrapper.getDdpRuleDetail().getRdtPartyId()
				.toString().trim();
		DdpParty ddpParty = ruleWrapper.getDdpRuleDetail().getRdtPartyCode();
		partyList.add(ddpParty);
		String companycode = ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode();
//		Integer ruleSize = ruleUtil.checkDuplicateRule("MULTI_AED_RULE", companycode, brnList, newPartyId, partyList, docList, maedRuleId);
//		if (ruleSize > 0)
//			return "redirect:/ddpmultiaedrules/list/"+ maedRuleId.toString() + "/form?customerId="+ newPartyId;
		String oldPartyId = lii.get(0).getRdtPartyId().trim();

		TypedQuery<DdpRuleDetail> query = DdpRuleDetail
				.findDdpRuleDetailsByRdtRuleId(ddpRuleService
						.findDdpRule(maedRuleId));
		List<DdpRuleDetail> ddpRuleDetails = query.getResultList();

		Map<String, Integer> oldDocTypeRdtIDMap = new HashMap<String, Integer>();
		Map<String, Integer> newDocTypeRdtIDMap = new HashMap<String, Integer>();

		/******
		 * Get the created date and created by user name before deleting the
		 * records in DDPRuleDetail table
		 ***********/
		String crtedUserName = ddpRuleDetails.get(0).getRdtCreatedBy();
		Calendar crtedDateAndTime = ddpRuleDetails.get(0).getRdtCreatedDate();
		/*****************************************************************************************************************/
		/***************** Prepare CommunicationSetUp ****************************************************************/
		DdpCommunicationSetup ddpCommSetup = ddpRuleDetails.get(0).getRdtCommunicationId();
		String previousProtocol = ddpCommSetup.getCmsCommunicationProtocol();
		String comprotoco = ddpCommSetup.getCmsProtocolSettingsId();
		/*****************************************************************************************************************/
		/***************** Get the Communication Email and Update Communication Email Services *****************************/
		Integer commEmailId = 0;
		if(! isEmailSetupByDest)
		{
			communicationProtocol = env.getProperty("rule.comm.prototocol");
			DdpCommEmail ddpcommEmail = ddpCommEmailService.findDdpCommEmail(Integer.parseInt(comprotoco));
			if(ddpcommEmail == null)
			{
				String evn = env.getProperty("app.evn").trim();
				String uncPath = env.getProperty("rule.unc.path." + evn);
				ruleWrapper.getDdpCommEmail().setCemUncPath(uncPath);
				ddpCommEmailService.saveDdpCommEmail(ruleWrapper.getDdpCommEmail());
				// Get the auto increments id commEmailId
			}
			else
			{
				ruleWrapper.getDdpCommEmail().setCemEmailId(ddpcommEmail.getCemEmailId());
				ruleWrapper.getDdpCommEmail().setCemUncPath(ddpcommEmail.getCemUncPath());
				ddpCommEmailService.updateDdpCommEmail(ruleWrapper.getDdpCommEmail());
			}
			commEmailId = ruleWrapper.getDdpCommEmail().getCemEmailId();
		}
		/**************** Update Communication Setup Services **************************************************************/
		// set date
		currentDate = Calendar.getInstance();
		ddpCommSetup.setCmsModifiedBy(strUserName);
		ddpCommSetup.setCmsModifiedDate(currentDate);
		ddpCommSetup.setCmsCommunicationProtocol(communicationProtocol);
		ddpCommSetup.setCmsProtocolSettingsId(commEmailId.toString());
		ddpCommunicationSetupService.updateDdpCommunicationSetup(ddpCommSetup);
		// Get the cms_communication_id
		Integer comSetupId = ddpCommSetup.getCmsCommunicationId();
		if(isEmailSetupByDest)
		{
			commEmailId = 0;
			communicationProtocol = env.getProperty("rule.muti.prototocol");
			List<DdpMultiEmails> ddpMultiEmails = ruleUtil.getMultiEmailsByCmsID(ddpCommSetup.getCmsCommunicationId());
			for(DdpMultiEmails multiEmails:ddpMultiEmails)
				ddpMultiEmailsService.deleteDdpMultiEmails(ddpMultiEmailsService.findDdpMultiEmails(multiEmails.getMesEmailId()));
			// Read Recipient  and store into table 
			String sourceType = null;
			int multiEmailsCount = Integer.parseInt(httpServletRequest.getParameter("reciepantRowCount"));
			for(int indexI=0; indexI <= multiEmailsCount; indexI++)
			{
				sourceType = httpServletRequest.getParameter("sourceType"+indexI);
				if(sourceType != null)
				{
					String sourceValue = httpServletRequest.getParameter("sourceValue"+indexI);
					String toAddress = httpServletRequest.getParameter("toAddress"+indexI);
					String ccAddress = httpServletRequest.getParameter("ccAddress"+indexI);
					DdpMultiEmails multiEmailsModel = new DdpMultiEmails();
					if(sourceType.equalsIgnoreCase("company"))
						multiEmailsModel.setMesDestCompany(sourceValue.trim());
					if(sourceType.equalsIgnoreCase("Region"))
						multiEmailsModel.setMesDestRegion(sourceValue.trim());
					multiEmailsModel.setMesEmailTo(toAddress.trim());
					multiEmailsModel.setMesEmailCc(ccAddress.trim());
					//set source, and value
					multiEmailsModel.setMesCmsId(ddpCommSetup);
					ddpMultiEmailsService.saveDdpMultiEmails(multiEmailsModel);
				}
			}

		}
		
		/*****************************************************************************************************************/
		
		/*****************************************************************************************************************/
		for (DdpRuleDetail ddpRuleDetail : ddpRuleDetails) {
			// String setBranchKey = (brnList.contains("ALL")) ? "ALL" :
			// ddpRuleDetail.getRdtBranch().getBrnBranchCode();
			String mappKey = "";
			String branchCode = ddpRuleDetail.getRdtBranch().getBrnBranchCode();
			if (brnList.contains("ALL")) {
				mappKey = ddpRuleDetail.getRdtId() + ":";
				branchCode = "ALL";
			}
			mappKey = mappKey + ddpRuleDetail.getRdtPartyId() + "-"
					+ ddpRuleDetail.getRdtCompany().getComCompanyCode() + "-"
					+ branchCode + "-"
					+ ddpRuleDetail.getRdtDocType().getDtyDocTypeCode() + "-"
					+ ddpRuleDetail.getRdtPartyCode().getPtyPartyCode();
			// Delete Communication Option
			TypedQuery<DdpCommOptionsSetup> query1 = DdpCommOptionsSetup
					.findDdpCommOptionsSetupsByCopRdtId(ddpRuleDetail);
			for (DdpCommOptionsSetup strOptionId : query1.getResultList()) {
				ddpCommOptionsSetupService
						.deleteDdpCommOptionsSetup(strOptionId);
			}
			// Delete DdpGenSourceSetup
			TypedQuery<DdpGenSourceSetup> query2 = DdpGenSourceSetup
					.findDdpGenSourceSetupsByGssRdtId(ddpRuleDetail);
			for (DdpGenSourceSetup ddpGenSourceSetup : query2.getResultList()) {
				mappKey = mappKey
						.concat("-" + ddpGenSourceSetup.getGssOption());
				ddpGenSourceSetupService
						.deleteDdpGenSourceSetup(ddpGenSourceSetup);
			}
			
			// Delete Version Setup
			TypedQuery<DdpExportVersionSetup> typedDdpExpVrs = DdpExportVersionSetup.findDdpExportVersionSetupsByEvsRdtId(ddpRuleDetail);
		 	for(DdpExportVersionSetup ddpExportVersionSetup: typedDdpExpVrs.getResultList())
		 	{
		 		ddpExportVersionSetupService.deleteDdpExportVersionSetup(ddpExportVersionSetup);
		 	}
		 	
			// Delete Rate/Not Rate
			TypedQuery<DdpRateSetup> query3 = DdpRateSetup
					.findDdpRateSetupsByDdpRuleDetail(ddpRuleDetail);
			for (DdpRateSetup rateSetup : query3.getResultList()) {
				mappKey = mappKey.concat("-" + rateSetup.getRtsOption());
				ddpRateSetupService.deleteDdpRateSetup(rateSetup);
			}
			oldDocTypeRdtIDMap.put(mappKey, ddpRuleDetail.getRdtId());
			// delete ruleDetail
			ddpRuleDetailService.deleteDdpRuleDetail(ddpRuleDetail);
		}

		/*******************************************************************************/
		/**
		 * ##### Save Rule Details table ######
		 * 
		 * 1) Loop all the seleceted branches 2) Inner Loop will be selected
		 * document types, party code for that branches.
		 * 
		 */
		/****************************************************************************/
		String department = ((ruleWrapper.getDdpRuleDetail().getRdtDepartment() != null && ruleWrapper
				.getDdpRuleDetail().getRdtDepartment().trim().length() > 0) ? ruleWrapper
				.getDdpRuleDetail().getRdtDepartment().trim()
				: null);

		if (department != null && department.endsWith(","))
			department = department.substring(0, department.length() - 1);
		// Loop the branch and add the rule details
		for (String strBranch : brnList) {

			// get the document type and party code
			for (int i = 0; i < docList.size(); i++) {
				DdpRuleDetail ddpRuleDetail = new DdpRuleDetail();
				DdpRule ddpRules = ddpRuleService.findDdpRule(ruleWrapper
						.getDdpMultiAedRule().getMaedRuleId());
				ddpRuleDetail.setRdtRuleId(ddpRules);
				// Set Company code
				ddpRuleDetail.setRdtCompany(ddpCompanyService
						.findDdpCompany(ruleWrapper.getDdpRuleDetail()
								.getRdtCompany().getComCompanyCode()));
				// Get Branch Detail based on selected branch code and country
				// code.
				DdpBranch ddpBranch = ddpBranchService.findDdpBranch(strBranch);
				ddpRuleDetail.setRdtBranch(ddpBranch);
				// Get DocTypeDoc
				DdpDoctype ddpDoctypes = ddpDoctypeService
						.findDdpDoctype(docList.get(i).toString());
				ddpRuleDetail.setRdtDocType(ddpDoctypes);
				// Get PartyBean
				ddpRuleDetail.setRdtPartyCode(ddpParty);
				// Generate system null
				// Schedular id null
				// Save the communication lookup id in RuleDetail table
				DdpCommunicationSetup ddpCommunicationSetup = ddpCommunicationSetupService
						.findDdpCommunicationSetup(comSetupId);
				ddpRuleDetail.setRdtCommunicationId(ddpCommunicationSetup);
				// Set status
				ddpRuleDetail.setRdtStatus(Integer.parseInt(status));
				// Set Party code
				ddpRuleDetail.setRdtPartyId(ruleWrapper.getDdpRuleDetail()
						.getRdtPartyId().trim());
				// set created by , modified by and created date and modified
				// date
				ddpRuleDetail.setRdtCreatedBy(crtedUserName);
				ddpRuleDetail.setRdtCreatedDate(crtedDateAndTime);
				ddpRuleDetail.setRdtModifiedBy(strUserName);
				ddpRuleDetail.setRdtModifiedDate(currentDate);
				// set updated Activation date
				if (null == ruleWrapper.getDdpRuleDetail()
						.getRdtActivationDate()
						|| "".equals(ruleWrapper.getDdpRuleDetail()
								.getRdtActivationDate())) {
					ddpRuleDetail.setRdtActivationDate(currentDate);
				} else {
					ddpRuleDetail.setRdtActivationDate(ruleWrapper
							.getDdpRuleDetail().getRdtActivationDate());
				}
				// Set Rule Type
				ddpRuleDetail.setRdtRuleType(env
						.getProperty("rule.type.consolidate"));
				// set Force Type
				ddpRuleDetail.setRdtForcedType(Integer.parseInt(reqFlagList
						.get(i).toString()));
				ddpRuleDetail.setRdtDocSequence(Integer.parseInt(sequenceList
						.get(i).toString()));
				int relevantType = (i == 0) ? 1 : 0;
				ddpRuleDetail.setRdtRelavantType(relevantType);
				ddpRuleDetail.setRdtDepartment(department);
				ddpRuleDetail.setRdtInclude(excludeList.get(i).toString());
				// Save rule details
				ddpRuleDetailService.saveDdpRuleDetail(ddpRuleDetail);
				// Get save rule detail id
				Integer ruleDetailId = ddpRuleDetail.getRdtId();

				// Save Rated/Not Rated document
				DdpRateSetup ddpRateSetup = new DdpRateSetup();
				ddpRateSetup.setRtsRdtId(ddpRuleDetail);
				ddpRateSetup.setRtsOption(rateSetupList.get(i).toString());
				ddpRateSetup.setRtsCreatedBy(strUserName);
				ddpRateSetup.setRtsCreatedDate(currentDate);
				ddpRateSetup.setRtsModifiedBy(strUserName);
				ddpRateSetup.setRtsModifiedDate(currentDate);
				ddpRateSetupService.saveDdpRateSetup(ddpRateSetup);

				// save Generate System
				DdpGenSourceSetup ddpGenSourceSetup = new DdpGenSourceSetup();
				ddpGenSourceSetup.setGssRdtId(ddpRuleDetail);
				ddpGenSourceSetup.setGssOption(genSourceList.get(i).toString());
				ddpGenSourceSetup.setGssCreatedBy(strUserName);
				ddpGenSourceSetup.setGssCreatedDate(currentDate);
				ddpGenSourceSetup.setGssModifiedBy(strUserName);
				ddpGenSourceSetup.setGssModifiedDate(currentDate);
				ddpGenSourceSetupService.saveDdpGenSourceSetup(ddpGenSourceSetup);
				
				 //save Export Version Setup
	       		 DdpExportVersionSetup ddpExportVersionSetup = new DdpExportVersionSetup();
	       		 ddpExportVersionSetup.setEvsRdtId(ddpRuleDetail);
	       		 ddpExportVersionSetup.setEvsOption(exportVersionList.get(i).toString());
	       		 ddpExportVersionSetup.setEvsCreatedBy(strUserName);
	       		 ddpExportVersionSetup.setEvsCreatedDate(currentDate);
	       		 ddpExportVersionSetup.setEvsModifiedBy(strUserName);
	       		 ddpExportVersionSetup.setEvsModifiedDate(currentDate);
	       		 ddpExportVersionSetupService.saveDdpExportVersionSetup(ddpExportVersionSetup);
				
	       		 String mappKey = ddpRuleDetail.getRdtPartyId() + "-"
						+ ddpRuleDetail.getRdtCompany().getComCompanyCode()
						+ "-" + ddpRuleDetail.getRdtBranch().getBrnBranchCode()
						+ "-"
						+ ddpRuleDetail.getRdtDocType().getDtyDocTypeCode()
						+ "-"
						+ ddpRuleDetail.getRdtPartyCode().getPtyPartyCode()
						+ "-" + genSourceList.get(i).toString() + "-"
						+ rateSetupList.get(i).toString();
				newDocTypeRdtIDMap.put(mappKey, ddpRuleDetail.getRdtId());
			}
		}
		String ruleDescription = "";
		DdpCompany ddpCompany = ddpCompanyService.findDdpCompany(ruleWrapper
				.getDdpRuleDetail().getRdtCompany().getComCompanyCode());
		String ruleName = ddpCompany.getComRegion()
				+ "_"
				+ ruleWrapper.getDdpRuleDetail().getRdtCompany()
						.getComCompanyCode() + "_"
				+ ruleWrapper.getDdpRuleDetail().getRdtPartyId();
		if(ruleWrapper.getDdpRule().getRulDescription().equals(""))
        	ruleDescription = ruleName;
        else 
        	ruleDescription = ruleWrapper.getDdpRule().getRulDescription();
		
		DdpRule ddpRule = ddpRuleService.findDdpRule(ruleWrapper
				.getDdpMultiAedRule().getMaedRuleId());
		ddpRule.setRulName(ruleName);
		ddpRule.setRulDescription(ruleDescription);
		// ddpRule.setRulStatus(Integer.parseInt(status));
		ddpRule.setRulModifiedBy(strUserName);
		ddpRule.setRulModifiedDate(currentDate);
		ddpRuleService.updateDdpRule(ddpRule);

		/************* Updating DDP_COMPRESSION_SETUP ********************************/
		int compressionSetupId = getDdpCompressionIDByMultiAedRule(ruleWrapper
				.getDdpMultiAedRule().getMaedRuleId());
		DdpCompressionSetup ddpCompressionSetup = new DdpCompressionSetup();
		ddpCompressionSetup.setCtsCompressionId(compressionSetupId);
		String attachmentLmt = httpServletRequest.getParameter("attachmentLmt");
		String attachCount = httpServletRequest.getParameter("attachCount");
		ddpCompressionSetup.setCtsEmailAttachmentLimit(Integer
				.parseInt(attachmentLmt));
		ddpCompressionSetup.setCtsNoOfFilesAttached(Integer
				.parseInt(attachCount));
		if (attachCount.equals("1")) {
			String fileCompression = httpServletRequest
					.getParameter("fileCompression");
			ddpCompressionSetup.setCtsCompressionLevel(fileCompression);
			if (fileCompression.equals("zip")) {
				String eachZipSize = httpServletRequest
						.getParameter("valZipSize");
				ddpCompressionSetup.setCtsCompressionSize(Integer
						.parseInt(eachZipSize));
			}
		}
		ddpCompressionSetupService.saveDdpCompressionSetup(ddpCompressionSetup);

		/************* DDP_COMPRESSION_SETUP Updated ********************************/
		DdpMultiAedRule ddpMultiAedRule = ddpMultiAedRuleService
				.findDdpMultiAedRule(ruleWrapper.getDdpMultiAedRule()
						.getMaedRuleId());
		/****************** UPDATE DDP_NOTIFICATION **************/
		DdpNotification ddpNotification = ddpMultiAedRule
				.getMaedNotificationId();
		if (ddpNotification != null) {
			ddpNotification.setNotFailureEmailAddress(ruleWrapper
					.getDdpNotification().getNotFailureEmailAddress());
			if (ddpNotification.getNotFailureEmailAddress().equals(""))
				ddpNotification.setNotFailureEmailAddress(env
						.getProperty("email.cc"));
			ddpNotification.setNotModifiedBy(strUserName);
			ddpNotification.setNotModifiedDate(currentDate);
			ddpNotificationService.updateDdpNotification(ddpNotification);
		} else {
			ddpNotification = ruleWrapper.getDdpNotification();
			if (ddpNotification.getNotFailureEmailAddress() == null
					|| ddpNotification.getNotFailureEmailAddress().equals("")) {
				ddpNotification.setNotFailureEmailAddress(env
						.getProperty("email.cc"));
			}
			ddpNotification.setNotSuccessEmailAddress(env
					.getProperty("email.cc"));
			ddpNotification.setNotCreatedBy(strUserName);
			ddpNotification.setNotCreatedDate(currentDate);
			ddpNotification.setNotModifiedBy(strUserName);
			ddpNotification.setNotModifiedDate(currentDate);
			ddpNotification.setNotStatus(0);
			ddpNotificationService.saveDdpNotification(ddpNotification);
		}
		// ddpMultiAedRule.setMaedStatus(Integer.parseInt(ruleStatus));
		ddpMultiAedRule.setMaedNotificationId(ddpNotification);
		ddpMultiAedRule.setMaedCommunicationId(ddpCommunicationSetupService
				.findDdpCommunicationSetup(comSetupId));
		ddpMultiAedRule.setMaedModifiedBy(strUserName);
		ddpMultiAedRule.setMaedModifiedDate(currentDate);
		ddpMultiAedRule.setMaedStatus(Integer.parseInt(status));
		if (null == ruleWrapper.getDdpRuleDetail().getRdtActivationDate()
				|| "".equals(ruleWrapper.getDdpRuleDetail()
						.getRdtActivationDate())) {
			ddpMultiAedRule.setMaedActivationDate(currentDate);
		} else {
			ddpMultiAedRule.setMaedActivationDate(ruleWrapper
					.getDdpRuleDetail().getRdtActivationDate());
		}
		ddpMultiAedRuleService.updateDdpMultiAedRule(ddpMultiAedRule);

		/************* update DDP_EMAIL_TRIGGER_SETUP ******************************/
		Integer etrId = findDdpTriggerSetupByRuleId(ruleWrapper.getDdpMultiAedRule().getMaedRuleId()).get(0).getEtrId();
		String strPrevTriggerName = ddpEmailTriggerSetupService.findDdpEmailTriggerSetup(etrId).getEtrTriggerName();
		DdpEmailTriggerSetup ddpEmailTriggerSetup = new DdpEmailTriggerSetup();
		ddpEmailTriggerSetup.setEtrId(etrId);
		ddpEmailTriggerSetup.setEtrRuleId(ddpRule);
		ddpEmailTriggerSetup.setEtrTriggerName(httpServletRequest.getParameter("selTgrCriteria"));
		ddpEmailTriggerSetup.setEtrDocTypes(docList.get(0).toString());
		ddpEmailTriggerSetup.setEtrRetries(Integer.parseInt(httpServletRequest.getParameter("noOfRetries")));
		if(httpServletRequest.getParameter("selTgrCriteria").trim().equals("consignmentID"))
		{
			ddpEmailTriggerSetup.setEtrInclude(httpServletRequest.getParameter("selinclude"));
		}
		// if(triggerSpec.equalsIgnoreCase(env.getProperty("emailtrigger.sDocs")))
		// {
		// ddpEmailTriggerSetup.setEtrTriggerName(com.agility.ddp.core.util.Constant.TRIGGER_NAME_SPECIFIC_DOCS);
		// String[] selectedDocsList =
		// httpServletRequest.getParameterValues("lstDocs");
		// String docsLst = "";
		// for(String docs:selectedDocsList)
		// {
		// docsLst = docsLst.concat(docs+",");
		// }
		// docsLst = docsLst.substring(0,docsLst.lastIndexOf(","));
		// ddpEmailTriggerSetup.setEtrDocTypes(docsLst);
		// }
		// else
		// if(triggerSpec.equalsIgnoreCase(env.getProperty("emailtrigger.aMin")))
		// {
		// ddpEmailTriggerSetup.setEtrTriggerName(com.agility.ddp.core.util.Constant.TRIGGER_NAME_ALL_OR_MINIMUM);
		// ddpEmailTriggerSetup.setEtrDocSelection(httpServletRequest.getParameter("selAMinDocs").toString());
		// }
		// else
		// if(triggerSpec.equalsIgnoreCase(env.getProperty("emailtrigger.sTime")))
		// {
		// /**set the cron Expression**/
		// String everyMinute = httpServletRequest.getParameter("minutespan");
		// String everyHour = httpServletRequest.getParameter("hourspan");
		// String dayofMonth = httpServletRequest.getParameter("dayOfMonth");
		// String dayofWeek = httpServletRequest.getParameter("dayOfWeek");
		// String weekflag= httpServletRequest.getParameter("weekflag");
		// String monthflag= httpServletRequest.getParameter("monthflag");
		// String hours = httpServletRequest.getParameter("hour");
		// String mins = httpServletRequest.getParameter("minute");
		// if(weekflag.equals("0") && monthflag.equals("0"))
		// {
		// dayofMonth="*";
		// dayofWeek="?";
		// }
		// if(weekflag.equals("0") && monthflag.equals("1"))
		// {
		// dayofWeek="?";
		// }
		// if(monthflag.equals("0") && weekflag.equals("1"))
		// {
		// dayofMonth="?";
		// }
		// if(! everyMinute.equals("0"))
		// mins=mins+"/"+everyMinute;
		// if(! everyHour.equals("0"))
		// hours=hours+"/"+everyHour;
		// String cronExpression =
		// "0 "+mins+" "+hours+" "+dayofMonth+" * "+dayofWeek;
		//
		// ddpEmailTriggerSetup.setEtrTriggerName(com.agility.ddp.core.util.Constant.TRIGGER_NAME_SPECIFIC_TIME);
		// ddpEmailTriggerSetup.setEtrCronExpression(cronExpression);
		// }
		// else
		// {
		// ddpEmailTriggerSetup.setEtrTriggerName(com.agility.ddp.core.util.Constant.TRIGGER_NAME_IMMEDIATE);
		// }
		ddpEmailTriggerSetupService
				.updateDdpEmailTriggerSetup(ddpEmailTriggerSetup);

		/************* DDP_EMAIL_TRIGGER_SETUP updated ******************************/

		/************** Updating Cron Expression ********************/
		DdpCreateMultiAedSchedulerTask ddpCreateMultiAedSchedulerTask = applicationContext
				.getBean("ddpCreateMultiAedSchedulerTask",
						DdpCreateMultiAedSchedulerTask.class);
		if (strPrevTriggerName
				.equalsIgnoreCase(com.agility.ddp.core.util.Constant.TRIGGER_NAME_SPECIFIC_TIME)) {
			if (triggerSpec.equalsIgnoreCase(env
					.getProperty("emailtrigger.sTime"))) {
				ddpCreateMultiAedSchedulerTask
						.updateSchedulerJob(ddpEmailTriggerSetup);
				logger.info("update Existing Cron Expression [{}]",
						ddpEmailTriggerSetup);
			} else {
				// TODO: must shutdown the running scheduler.
			}
		} else {
			if (triggerSpec.equalsIgnoreCase(env
					.getProperty("emailtrigger.sTime"))) {
				ddpCreateMultiAedSchedulerTask
						.createNewScheduler(ddpEmailTriggerSetup);
				logger.info("Creating new  Cron Expression [{}]",
						ddpEmailTriggerSetup);
			}
		}
		/****************** Updating Cron Expression Ends ***********/
		// update
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
		ruleUtil.updateCategorizedRdt(replaceRdts,ddpMultiAedRule.getMaedRuleId(),env.getProperty("rule.type.consolidate"));

		/************** *******************/
		//uiModel.asMap().clear();
		logger.info("DdpMultiAedRuleController.update Method Executed Successfully.");
		return "redirect:/ddpmultiaedrules/list/list/"
				+ ddpMultiAedRule.getMaedRuleId();
	}

	@RequestMapping(value = "/{maedRuleId}/form", produces = "text/html")
	public String updateForm(
			@PathVariable("maedRuleId") Integer maedRuleId,
			Model uiModel,
			@RequestParam(value = "customerId", required = false) String customerId) throws JsonGenerationException, JsonMappingException, IOException {
		String strUserName = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String strUserCompany = ruleUtil.getUserCompany(strUserName);
		RuleWrapper ruleWrapper = new RuleWrapper();
		if (customerId != null) {
			uiModel.addAttribute("duplicateCustID", customerId);
		}
		// Prepare list to get the Rule
		DdpRule ddpRule = ddpRuleService.findDdpRule(maedRuleId);
		ruleWrapper.setDdpRule(ddpRule);

		DdpMultiAedRule ddpMultiAedRule = ddpMultiAedRuleService
				.findDdpMultiAedRule(maedRuleId);
		ruleWrapper.setDdpMultiAedRule(ddpMultiAedRule);

		// find Ddp Rule Details
		TypedQuery<DdpRuleDetail> query = DdpRuleDetail
				.findDdpRuleDetailsByRdtRuleId(ddpRuleService
						.findDdpRule(maedRuleId));
		List<DdpRuleDetail> ddpRuleDetails = query.getResultList();
		ruleWrapper.setDdpRuleDetail(ddpRuleDetails.get(0));

		// Row Count
		List<String> docTypeLst = new ArrayList<String>();
		for (DdpRuleDetail ddpRuleDetail : ddpRuleDetails) {
			docTypeLst.add(ddpRuleDetail.getRdtDocType().getDtyDocTypeCode());
		}
		List<String> pCodeLst = new ArrayList<String>();
		for (DdpRuleDetail ddpRuleDetail : ddpRuleDetails) {
			pCodeLst.add(ddpRuleDetail.getRdtPartyCode().getPtyPartyCode());
		}
		Set<String> t = new LinkedHashSet<String>();
		for (int i = 0; i < docTypeLst.size(); i++) {
			t.add(docTypeLst.get(i) + "-" + pCodeLst.get(i));
		}
		Integer acrowcount = 0;
		if (docTypeLst.size() >= pCodeLst.size())
			acrowcount = t.size();
		Integer rowcount = ddpRuleDetails.size();
		ruleWrapper.setRowcount(rowcount);

		/*****************************************************************************************************************/
		// Prepare Communication Setup
		DdpCommunicationSetup ddpCommSetup = ddpRuleDetails.get(0).getRdtCommunicationId();
		if(ddpCommSetup.getCmsCommunicationProtocol().equalsIgnoreCase("SMTP"))
		{
			String comprotoco = ddpCommSetup.getCmsProtocolSettingsId();
			ruleWrapper.setDdpCommEmail(ddpCommEmailService.findDdpCommEmail(Integer.parseInt(comprotoco)));
		}
		if(ddpCommSetup.getCmsCommunicationProtocol().equalsIgnoreCase("MSMTP"))
		{
			List<DdpMultiEmails> multiEmails = ruleUtil.getMultiEmailsByCmsID(ddpCommSetup.getCmsCommunicationId());
			uiModel.addAttribute("multiEmails",multiEmails);
//			ObjectMapper mapper = new ObjectMapper();
//			String multiEmailsJson = mapper.writeValueAsString(multiEmails);
			Gson gson = new Gson();
            String multiEmailsjson = gson.toJson(multiEmails);
            uiModel.addAttribute("multiEmailsjson", multiEmailsjson);
			
		}
		/*****************************************************************************************************************/
		// Prepare Country branch list
		populateEditForm(uiModel,ddpMultiAedRuleService.findDdpMultiAedRule(maedRuleId),ddpRuleDetails.get(0).getRdtCompany().getComCompanyCode().toString(),"updateForm");

		// Prepare selected branch
		List<DdpBranch> branchs = new ArrayList<DdpBranch>();
		for (DdpRuleDetail ruleDetail : ddpRuleDetails) {
			branchs.add(ruleDetail.getRdtBranch());
		}
		Set<DdpBranch> list3 = new LinkedHashSet<DdpBranch>();
		list3.addAll(branchs);
		List<DdpBranch> ddpBranchs = new ArrayList<DdpBranch>();
		for (DdpBranch ddpBranch : list3) {
			ddpBranchs.add(ddpBranchService.findDdpBranch(ddpBranch
					.getBrnBranchCode()));
		}
		uiModel.addAttribute("selectedBranchList", ddpBranchs);
		// find DDP_TRIGGER_SETUP BY RULE_ID
		List<DdpEmailTriggerSetup> ddpEmailTriggerSetups = findDdpTriggerSetupByRuleId(maedRuleId);
		if (ddpEmailTriggerSetups.get(0).getEtrDocTypes() != null) {
			String[] selectedDocs = ddpEmailTriggerSetups.get(0).getEtrDocTypes().split(",");
			if(selectedDocs.length == 1 && selectedDocs[0].equals(""))
				selectedDocs[0] = "SALESINV"; // TO Avoid null in UI
			LinkedHashSet<String> selDocs = new LinkedHashSet<String>();
			selDocs.addAll(Arrays.asList(selectedDocs));
			List<DdpDoctype> ddpDoctypes = new ArrayList<DdpDoctype>();
			for (String doc : selDocs) {
				ddpDoctypes.add(ddpDoctypeService.findDdpDoctype(doc));
			}
			uiModel.addAttribute("selectedDocsList", ddpDoctypes);
			ruleWrapper.setLstDocs(ddpDoctypes);
		}
		if (ddpEmailTriggerSetups.get(0).getEtrCronExpression() != null) {
			// getting cronExpression
			String cronExpression = ddpEmailTriggerSetups.get(0)
					.getEtrCronExpression();
			String[] cronEx = cronExpression.split(" ");
			String dayofMonth = cronEx[3];
			String dayofWeek = cronEx[5];
			String hours = cronEx[2];
			String mins = cronEx[1];
			// check for every min, hour
			String minuteSpan = null;
			String hourSpan = null;
			if (cronEx[1].contains("/")) {
				String[] strMin = cronEx[1].split("/");
				mins = strMin[0];
				minuteSpan = strMin[1];
			}
			if (cronEx[2].contains("/")) {
				String[] strHour = cronEx[2].split("/");
				hours = strHour[0];
				hourSpan = strHour[1];
			}
			uiModel.addAttribute("mins", mins);
			uiModel.addAttribute("hours", hours);
			uiModel.addAttribute("dayofMonth", dayofMonth);
			uiModel.addAttribute("dayofWeek", dayofWeek);
			if (minuteSpan != null) {
				uiModel.addAttribute("minuteSpan", minuteSpan);
			}
			if (hourSpan != null) {
				uiModel.addAttribute("hourSpan", hourSpan);
			}
		}
		if (ddpEmailTriggerSetups.get(0).getEtrDocSelection() != null) {
			uiModel.addAttribute("selAMinDocs", ddpEmailTriggerSetups.get(0)
					.getEtrDocSelection());
		}
		uiModel.addAttribute("ddpEmailTriggerSetup",
				ddpEmailTriggerSetups.get(0));
		ruleWrapper.setLstBranch(ddpBranchs);
		/*****************************************************************************************************************/
		// Prepare Document type and Party code
		List<RuleDao> ruleDaos = getRuleDetailForRuleId(maedRuleId.toString(),
				acrowcount);
		if (ddpMultiAedRule.getMaedCompressionId() != null)
			uiModel.addAttribute("ddpCompressionSetup",
					ddpMultiAedRule.getMaedCompressionId());
		uiModel.addAttribute("ddpCommOptionsLkp",
				ddpCommOptionsLkpService.findAllDdpCommOptionsLkps());
		uiModel.addAttribute("ddpmaedrule", ruleDaos);
		uiModel.addAttribute("itemId", maedRuleId);
		ruleWrapper.setDdpNotification(ddpMultiAedRule.getMaedNotificationId());
		uiModel.addAttribute("ruleStatus", ddpMultiAedRule.getMaedStatus());
		uiModel.addAttribute("rulewrapper", ruleWrapper);
		return "ddpmultiaedrules/update";
	}

	@AuditLog(message = "Deleted")
	@Transactional
	@RequestMapping(value = "/{maedRuleId}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("maedRuleId") Integer maedRuleId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {

		logger.info("DdpMultiAedRuleController.delete Method Invoked.");
		// find Ddp Rule Details
		TypedQuery<DdpRuleDetail> query = DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(ddpRuleService.findDdpRule(maedRuleId));
		List<DdpRuleDetail> ddpRuleDetails = query.getResultList();

		for (DdpRuleDetail ddpRuleDetail : ddpRuleDetails) {
			// Delete Communication Option
			TypedQuery<DdpCommOptionsSetup> query1 = DdpCommOptionsSetup
					.findDdpCommOptionsSetupsByCopRdtId(ddpRuleDetail);
			for (DdpCommOptionsSetup strOptionId : query1.getResultList()) {
				ddpCommOptionsSetupService
						.deleteDdpCommOptionsSetup(strOptionId);
			}
			// Delete DdpGenSourceSetup
			TypedQuery<DdpGenSourceSetup> query2 = DdpGenSourceSetup
					.findDdpGenSourceSetupsByGssRdtId(ddpRuleDetail);
			for (DdpGenSourceSetup ddpGenSourceSetup : query2.getResultList()) {
				ddpGenSourceSetupService
						.deleteDdpGenSourceSetup(ddpGenSourceSetup);
			}
			// Delete Version Setup
			TypedQuery<DdpExportVersionSetup> typedDdpExpVrs = DdpExportVersionSetup.findDdpExportVersionSetupsByEvsRdtId(ddpRuleDetail);
		 	for(DdpExportVersionSetup ddpExportVersionSetup: typedDdpExpVrs.getResultList())
		 	{
		 		ddpExportVersionSetupService.deleteDdpExportVersionSetup(ddpExportVersionSetup);
		 	}
			// Delete Rate/Not Rate
			TypedQuery<DdpRateSetup> query3 = DdpRateSetup
					.findDdpRateSetupsByDdpRuleDetail(ddpRuleDetail);
			for (DdpRateSetup rateSetup : query3.getResultList()) {
				ddpRateSetupService.deleteDdpRateSetup(rateSetup);
			}
			// delete ruleDetail
			ddpRuleDetailService.deleteDdpRuleDetail(ddpRuleDetail);
		}
		DdpMultiAedRule ddpMultiAedRule = ddpMultiAedRuleService
				.findDdpMultiAedRule(maedRuleId);
		// Disable the Rule
		ddpMultiAedRule.setMaedStatus(1);
		ddpMultiAedRuleService.updateDdpMultiAedRule(ddpMultiAedRule);
		// Deleting Rule
		DdpRule ddpRule = ddpRuleService.findDdpRule(maedRuleId);
		// Disable the Rule
		ddpRule.setRulStatus(1);
		ddpRuleService.updateDdpRule(ddpRule);

		uiModel.asMap().clear();
		logger.info("DdpMultiAedRuleController.delete Method Executed Successfully.");
		return "redirect:/ddpmultiaedrules/list" + "/mediator";
	}

	void populateEditForm(Model uiModel, DdpMultiAedRule ddpMultiAedRule,
			String userCompany,String formtype) {
		uiModel.addAttribute("ddpMultiAedRule", ddpMultiAedRule);
		addDateTimeFormatPatterns(uiModel);
		List<DdpCompany> ddpCompanies = new ArrayList<DdpCompany>();
		String strUserName = SecurityContextHolder.getContext().getAuthentication().getName();
		ddpCompanies = ruleUtil.getAccessibleCompaniesLst(strUserName,env.getProperty("rule.maed.firstchars"));
		ddpCompanies.removeAll(ruleUtil.getListOfReadOnlyAccessibleCompanies(strUserName, env.getProperty("rule.maed.firstchars")));
		//sort companies
        Collections.sort(ddpCompanies,new Comparator<DdpCompany>() {
			@Override
			public int compare(DdpCompany company1, DdpCompany company2) {
				return company1.getComCompanyCode().compareTo(company2.getComCompanyCode());
			}	
		});
		uiModel.addAttribute("ddpcompanys", ddpCompanies);
		List<DdpBranch> lstBranch = new ArrayList<DdpBranch>();
		if(! ddpCompanies.isEmpty())
	    {
			if(! formtype.equalsIgnoreCase("updateForm"))
			{
//				if(! ddpCompanies.get(0).getComCompanyCode().equalsIgnoreCase(userCompany))
//	        	{
//	        		DdpCompany ddpCompany = ddpCompanyService.findDdpCompany(userCompany);
//	        		if(ddpCompanies.contains(ddpCompany))
//	        		{
//	        			ddpCompanies.remove(ddpCompany);
//	        			ddpCompanies.add(0,ddpCompany);
//	        		}
//	        	}
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
		uiModel.addAttribute("ddpRatedLkp", ruleUtil.getAllRateLkp());
		uiModel.addAttribute("ddpGenSourceLkp", ruleUtil.getAllGenSourceLkp());
		uiModel.addAttribute("ddpExpVersionLkp", ruleUtil.getExportVersionLkp());
		uiModel.addAttribute("ddpbranches", lstBranch);
		uiModel.addAttribute("ddppartys", ruleUtil.getAllPartys());
		uiModel.addAttribute("ddpdoctypes", doctypeLst);
		uiModel.addAttribute("multiplejobDocs", env.getProperty("document.multiplejobDocs"));
		String allCompanies = ruleUtil.getCompanyCodesAsString();
		uiModel.addAttribute("allCompanies", allCompanies);
	}

	public List<RuleDao> getRuleDetailForRuleIdShow(String maedRuleId) {
		List<RuleDao> ruleDaoLst = new ArrayList<RuleDao>();
		RuleDao ruleDao = null;
		TypedQuery<DdpRuleDetail> typRuleDetail = DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(ddpRuleService.findDdpRule(Integer.parseInt(maedRuleId)));
		List<DdpRuleDetail> lstRuleDetail = typRuleDetail.getResultList();

		List<String> brnLst = getBranchesByRuleId(Integer.parseInt(maedRuleId));
		String strBrnLst = "";
		for (String strBranch : brnLst) {
			strBrnLst += strBranch + ",";
		}
		strBrnLst = strBrnLst.substring(0, strBrnLst.lastIndexOf(","));
		// counting distinct DocTypes and PartyCodes
		int ddpRuleDetail = lstRuleDetail.size() / brnLst.size();
		System.out.println(ddpRuleDetail);
		for (int i = 0; i < ddpRuleDetail; i++) {
			ruleDao = new RuleDao();
			// Generated source
			TypedQuery<DdpGenSourceSetup> typedDdpGenSource = DdpGenSourceSetup
					.findDdpGenSourceSetupsByGssRdtId(lstRuleDetail.get(i));
			List<DdpGenSourceSetup> genSourceSetup = typedDdpGenSource
					.getResultList();
			if (!genSourceSetup.isEmpty())
				ruleDao.setGenSource(ruleUtil
						.findGenSourceDescriptionByOption(genSourceSetup.get(0)
								.getGssOption()));
			// Rated option
			TypedQuery<DdpRateSetup> typedDdpRateSetup = DdpRateSetup
					.findDdpRateSetupsByDdpRuleDetail(lstRuleDetail.get(i));
			List<DdpRateSetup> rateSetup = typedDdpRateSetup.getResultList();
			if (!rateSetup.isEmpty())
				ruleDao.setStrRate(ruleUtil
						.findRateDescriptionByOption(rateSetup.get(0)
								.getRtsOption()));
			//set Export Version Setup
		   	 TypedQuery<DdpExportVersionSetup> query2 = DdpExportVersionSetup.findDdpExportVersionSetupsByEvsRdtId(lstRuleDetail.get(i));
		 	 List<DdpExportVersionSetup> ddpExportVersionSetups = query2.getResultList();
		 	 ruleDao.setExpVersionSetup((ddpExportVersionSetups.size()>0)?ddpExportVersionSetups.get(0).getEvsOption():"");
		 	 
		 	 if(lstRuleDetail.get(i).getRdtCommunicationId().getCmsCommunicationProtocol().equalsIgnoreCase("SMTP"))
		 	 {
				// Comm Email
				DdpCommEmail commEmail = new DdpCommEmail();
				commEmail = ddpCommEmailService.findDdpCommEmail(Integer
						.parseInt(lstRuleDetail.get(i).getRdtCommunicationId()
								.getCmsProtocolSettingsId()));
				ruleDao.setCemEmailFrom((commEmail == null) ? "" : commEmail
						.getCemEmailFrom());
				ruleDao.setCemEmailTo((commEmail == null) ? "" : commEmail
						.getCemEmailTo());
				ruleDao.setCemEmailCc((commEmail == null) ? "" : commEmail
						.getCemEmailCc());
				ruleDao.setCemSubject((commEmail == null) ? "" : commEmail
						.getCemEmailSubject());
				ruleDao.setCemBody((commEmail == null) ? "" : commEmail
						.getCemEmailBody());
		 	 }
		 	 if(lstRuleDetail.get(i).getRdtCommunicationId().getCmsCommunicationProtocol().equalsIgnoreCase(env.getProperty("rule.muti.prototocol")))
		 	 {
		 		 List<DdpMultiEmails> multiEmails =  ruleUtil.getMultiEmailsByCmsID(lstRuleDetail.get(i).getRdtCommunicationId().getCmsCommunicationId());
		 		 ruleDao.setMultiEmails(multiEmails);
		 	 }
			ruleDao.setRdtId(lstRuleDetail.get(i).getRdtId());
			ruleDao.setRdtCompanyCode(lstRuleDetail.get(i).getRdtCompany()
					.getComCompanyCode());
			ruleDao.setRdtBranch(strBrnLst);
			ruleDao.setRdtPartyCode(lstRuleDetail.get(i).getRdtPartyCode()
					.getPtyPartyCode());
			ruleDao.setRdtPartyName(lstRuleDetail.get(i).getRdtPartyCode()
					.getPtyPartyName());
			ruleDao.setRdtDocType(lstRuleDetail.get(i).getRdtDocType()
					.getDtyDocTypeCode());
			ruleDao.setRdtPartyId(lstRuleDetail.get(i).getRdtPartyId());
			int ddpRuleId = lstRuleDetail.get(i).getRdtRuleId().getRulId();
			ruleDao.setRid(ddpRuleId);
			int manditoryFlag = lstRuleDetail.get(i).getRdtForcedType();
			ruleDao.setManditoryFlag(manditoryFlag + "");
			ruleDao.setRdtExclude((lstRuleDetail.get(i).getRdtInclude() == null) ? "" : lstRuleDetail.get(i).getRdtInclude() );
			ruleDaoLst.add(ruleDao);
		}
		return ruleDaoLst;
	}

	public List<RuleDao> getRuleDetailForRuleId(String strRulId, int rowCount) {
		List<RuleDao> ruleDaoLst = new ArrayList<RuleDao>();
		RuleDao ruleDao = null;
		TypedQuery<DdpRuleDetail> typRuleDetail = DdpRuleDetail
				.findDdpRuleDetailsByRdtRuleId(ddpRuleService
						.findDdpRule(Integer.parseInt(strRulId)));
		List<DdpRuleDetail> lstRuleDetail = typRuleDetail.getResultList();
		// for (int i=0;i< rowCount ;i++){
		for (DdpRuleDetail ddpRuleDetail : lstRuleDetail) {
			// DdpRuleDetail ddpRuleDetail = lstRuleDetail.get(i);
			ruleDao = new RuleDao();
			TypedQuery<DdpCommOptionsSetup> typDdpCommOption = DdpCommOptionsSetup
					.findDdpCommOptionsSetupsByCopRdtId(ddpRuleDetail);
			List<DdpCommOptionsSetup> commOptions = typDdpCommOption
					.getResultList();
			for (DdpCommOptionsSetup commOptionsSetup : commOptions) {
				if ("Printing"
						.equalsIgnoreCase(commOptionsSetup.getCopOption())) {
					ruleDao.setCopPrint(commOptionsSetup.getCopOption());
				} else if ("Emailing".equalsIgnoreCase(commOptionsSetup
						.getCopOption())) {
					ruleDao.setCopEmail(commOptionsSetup.getCopOption());
				}
			}
			TypedQuery<DdpGenSourceSetup> typedDdpGenSource = DdpGenSourceSetup
					.findDdpGenSourceSetupsByGssRdtId(ddpRuleDetail);
			List<DdpGenSourceSetup> genSourceSetup = typedDdpGenSource
					.getResultList();
			ruleDao.setGenSource((genSourceSetup.isEmpty() ? ""
					: genSourceSetup.get(0).getGssOption()));
			
		   	TypedQuery<DdpExportVersionSetup> typedDdpExpVrs = DdpExportVersionSetup.findDdpExportVersionSetupsByEvsRdtId(ddpRuleDetail);
		 	List<DdpExportVersionSetup> ddpExportVersionSetups = typedDdpExpVrs.getResultList();
		 	ruleDao.setExpVersionSetup((ddpExportVersionSetups.size()>0)?ddpExportVersionSetups.get(0).getEvsOption():"");
		 	 
			TypedQuery<DdpRateSetup> typedDdpRateSetup = DdpRateSetup.findDdpRateSetupsByDdpRuleDetail(ddpRuleDetail);
			List<DdpRateSetup> rateSetup = typedDdpRateSetup.getResultList();
			ruleDao.setStrRate((rateSetup.isEmpty() ? "" : rateSetup.get(0).getRtsOption()));
			ruleDao.setRdtId(ddpRuleDetail.getRdtId());
			ruleDao.setRdtCompanyCode(ddpRuleDetail.getRdtCompany().getComCompanyCode());
			ruleDao.setRdtBranch(ddpRuleDetail.getRdtBranch().getBrnBranchCode());
			ruleDao.setRdtPartyCode(ddpRuleDetail.getRdtPartyCode().getPtyPartyCode());
			ruleDao.setRdtDocType(ddpRuleDetail.getRdtDocType().getDtyDocTypeCode());
			ruleDao.setRdtPartyId(ddpRuleDetail.getRdtPartyId());
			ruleDao.setManditoryFlag(ddpRuleDetail.getRdtForcedType() + "");
			ruleDao.setDocumentSequence((ddpRuleDetail.getRdtDocSequence() == null) ? "": ddpRuleDetail.getRdtDocSequence().toString());
			ruleDao.setRdtExclude((ddpRuleDetail.getRdtInclude() == null) ? "" : ddpRuleDetail.getRdtInclude());
			int ddpRuleId = ddpRuleDetail.getRdtRuleId().getRulId();
			ruleDao.setRid(ddpRuleId);
			// Add to the List<RuleDao>
			ruleDaoLst.add(ruleDao);
		}
		return ruleDaoLst;
	}

	public List<DdpMultiAedRule> getRuleIdByCompany(String... ddpCompany) {
		String cmpLst = "";

		String col = "drd.RDT_COMPANY=";
		for (String company : ddpCompany) {
			cmpLst += col + "'" + company + "' OR ";
		}
		cmpLst = cmpLst.substring(0, cmpLst.lastIndexOf(" OR"));
		List<DdpMultiAedRule> ddpMultiaedrules = this.jdbcTemplate
				.query("SELECT DISTINCT dar.MAED_RULE_ID from dbo.DDP_MULTI_AED_RULE dar,dbo.DDP_RULE_DETAIL drd WHERE ("
						+ cmpLst + ") AND dar.MAED_RULE_ID=drd.RDT_RULE_ID",
						new Object[] {}, new RowMapper<DdpMultiAedRule>() {

							public DdpMultiAedRule mapRow(ResultSet rs,
									int rowNum) throws SQLException {

								DdpMultiAedRule ddpMultiaedrule = ddpMultiAedRuleService
										.findDdpMultiAedRule(rs
												.getInt("MAED_RULE_ID"));
								return ddpMultiaedrule;
							}
						});
		return ddpMultiaedrules;
	}

	/**
	 * 
	 * @param partyId
	 * @param companyCode
	 * @param partyCodes
	 * @return
	 */
	public List<Integer> getRuleIdsByPartyCode(String partyId,
			String companyCode, List<Object> partyCodes) {
		String col = "RDT_PARTY_CODE=";
		String partycodes = "";
		for (int in = 0; in < partyCodes.size(); in++) {
			partycodes += col + "'" + partyCodes.get(in) + "' or ";
		}
		partycodes = partycodes.substring(0, partycodes.lastIndexOf(" or"));
		List<Integer> rIDList = this.jdbcTemplate.query(
				"SELECT RDT_RULE_ID FROM DDP_RULE_DETAIL WHERE RDT_COMPANY= '"
						+ companyCode
						+ "' AND RDT_STATUS=0 AND RDT_PARTY_ID = '" + partyId
						+ "' AND (" + partycodes
						+ ") AND RDT_RULE_TYPE='MULTI_AED_RULE'",
				new Object[] {}, new RowMapper<Integer>() {
					@Override
					public Integer mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						int rId;
						rId = rs.getInt("RDT_RULE_ID");
						return rId;
					}
				});
		return rIDList;
	}

	public List<String> getBranchesByRuleId(int ruleId) {
		List<String> brachesLst = this.jdbcTemplate.query(
				Constant.BRANCHESBYRULEID1, new Object[] { ruleId },
				new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						String strBranch = rs.getString("RDT_BRANCH");
						return strBranch;
					}
				});
		return brachesLst;
	}

	public List<DdpEmailTriggerSetup> findDdpTriggerSetupByRuleId(
			Integer maedRuleId) {
		List<DdpEmailTriggerSetup> ddpEmailTriggerSetups = this.jdbcTemplate
				.query("SELECT * FROM DDP_EMAIL_TRIGGER_SETUP WHERE ETR_RULE_ID= ?",
						new Object[] { maedRuleId },
						new RowMapper<DdpEmailTriggerSetup>() {

							@Override
							public DdpEmailTriggerSetup mapRow(ResultSet rs,
									int rowNum) throws SQLException {
								DdpEmailTriggerSetup ddpEmailTriggerSetup = ddpEmailTriggerSetupService
										.findDdpEmailTriggerSetup(rs
												.getInt("ETR_ID"));
								return ddpEmailTriggerSetup;
							}

						});
		return ddpEmailTriggerSetups;
	}

	public List<SchedulerRuleWrapper> getAllActiveAedRules(String role,
			String regionName, String localCompany, String... companys) {
		String ruletype = "MULTI_AED_RULE";
		String cmpLst = "";
		List<SchedulerRuleWrapper> schedulerRuleWrapper = null;
		String query = "";

		if (role.equals("admin_role")) {
			query = Constant.SELECT_ALL_ACTIVE_RULE_DETAILS;
			schedulerRuleWrapper = this.jdbcTemplate.query(query,
					new Object[] { ruletype,ruletype }, new SchedulerRuleWrapper());
		} else if (role.equals("Region_role")) {
			query = Constant.SELECT_ACTIVE_RULES_REGEION;
			schedulerRuleWrapper = this.jdbcTemplate.query(query, new Object[] {
					ruletype, regionName,ruletype, regionName }, new SchedulerRuleWrapper());
		} else if (role.equals("multi")) {
			if (companys != null) {
				String col = "dc.COM_COMPANY_CODE=";
				for (String company : companys) {
					cmpLst += col + "'" + company + "' or ";
				}
				cmpLst = cmpLst.substring(0, cmpLst.lastIndexOf(" or"));
			}
			query = Constant.SELECT_ACTIVE_RULE_MULTI_COMPANY.replaceAll(
					"dynamiccondition", cmpLst);
			schedulerRuleWrapper = this.jdbcTemplate.query(query,
					new Object[] { ruletype,ruletype }, new SchedulerRuleWrapper());
		} else {
			query = Constant.SELECT_ACTIVE_RULE_LOCAL;
			schedulerRuleWrapper = this.jdbcTemplate.query(query, new Object[] {
					ruletype, localCompany,ruletype, localCompany }, new SchedulerRuleWrapper());
		}
		return schedulerRuleWrapper;
	}

	public int getDdpCompressionIDByMultiAedRule(int maedRuleId) {
		int compressionId = this.jdbcTemplate.queryForObject(
				Constant.SELECT_COMPRESSIONID_BY_MAED_ID,
				new Object[] { maedRuleId }, Integer.class);
		return compressionId;
	}
	

	// @RequestMapping(value = "/{maedRuleId}", method = RequestMethod.DELETE,
	// headers = "Accept=application/json")
	// public ResponseEntity<String> deleteFromJson(@PathVariable("maedRuleId")
	// Integer maedRuleId) {
	// DdpMultiAedRule ddpMultiAedRule =
	// ddpMultiAedRuleService.findDdpMultiAedRule(maedRuleId);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json");
	// if (ddpMultiAedRule == null) {
	// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	// }
	// ddpMultiAedRuleService.deleteDdpMultiAedRule(ddpMultiAedRule);
	// return new ResponseEntity<String>(headers, HttpStatus.OK);
	// }

}
