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
import java.util.Date;
import java.util.Enumeration;
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
import javax.validation.Valid;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
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

import com.agility.ddp.core.components.ApplicationProperties;
import com.agility.ddp.core.logger.AuditLog;
import com.agility.ddp.dao.RuleDao;
import com.agility.ddp.data.domain.DdpAedRule;
import com.agility.ddp.data.domain.DdpAedRuleService;
import com.agility.ddp.data.domain.DdpBranch;
import com.agility.ddp.data.domain.DdpBranchService;
import com.agility.ddp.data.domain.DdpCommEmail;
import com.agility.ddp.data.domain.DdpCommEmailService;
import com.agility.ddp.data.domain.DdpCommOptionsLkpService;
import com.agility.ddp.data.domain.DdpCommOptionsSetup;
import com.agility.ddp.data.domain.DdpCommOptionsSetupService;
import com.agility.ddp.data.domain.DdpCommunicationLkpService;
import com.agility.ddp.data.domain.DdpCommunicationSetup;
import com.agility.ddp.data.domain.DdpCommunicationSetupService;
import com.agility.ddp.data.domain.DdpCompany;
import com.agility.ddp.data.domain.DdpCompanyService;
import com.agility.ddp.data.domain.DdpCompressionSetupService;
import com.agility.ddp.data.domain.DdpDocnameConvService;
import com.agility.ddp.data.domain.DdpDoctype;
import com.agility.ddp.data.domain.DdpDoctypeService;
import com.agility.ddp.data.domain.DdpGenSourceLkpService;
import com.agility.ddp.data.domain.DdpGenSourceSetup;
import com.agility.ddp.data.domain.DdpGenSourceSetupService;
import com.agility.ddp.data.domain.DdpMergeSetupService;
import com.agility.ddp.data.domain.DdpNotification;
import com.agility.ddp.data.domain.DdpNotificationService;
import com.agility.ddp.data.domain.DdpParty;
import com.agility.ddp.data.domain.DdpPartyService;
import com.agility.ddp.data.domain.DdpRateLkpService;
import com.agility.ddp.data.domain.DdpRateSetup;
import com.agility.ddp.data.domain.DdpRateSetupService;
import com.agility.ddp.data.domain.DdpRule;
import com.agility.ddp.data.domain.DdpRuleDetail;
import com.agility.ddp.data.domain.DdpRuleDetailService;
import com.agility.ddp.data.domain.DdpRuleService;
import com.agility.ddp.data.domain.DdpUser;
import com.agility.ddp.data.domain.DdpUserService;
import com.agility.ddp.view.util.AedRuleWrapper;
import com.agility.ddp.view.util.ApplicationUIProperties;
import com.agility.ddp.view.util.Constant;
import com.agility.ddp.view.util.RuleUtil;
import com.google.gson.Gson;

/**
 * @author Rnagarathinam
 *
 */
@RooWebJson(jsonObject = DdpAedRule.class)
@Controller
@RequestMapping("/ddpaedrules/list")
@RooWebScaffold(path = "ddpaedrules", formBackingObject = DdpAedRule.class)
public class DdpAedRuleController {

	private static final Logger logger = LoggerFactory.getLogger(DdpAedRuleController.class);
	
	@Autowired
	private ApplicationUIProperties env;
	
    @Autowired
    DdpBranchService ddpBranchService;

    @Autowired
    DdpCompanyService ddpCompanyService;

    @Autowired
    DdpDoctypeService ddpDoctypeService;

    @Autowired
    DdpPartyService ddpPartyService;

    @Autowired
    DdpCommEmailService ddpCommEmailService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    DdpCommunicationSetupService ddpCommunicationSetupService;

    @Autowired
    DdpCommunicationLkpService ddpCommunicationLkpService;

    @Autowired
    DdpRuleService ddpRuleService;

    @Autowired
    DdpRuleDetailService ddpRuleDetailService;

    @Autowired
    DdpCommOptionsSetupService ddpCommOptionsSetupService;

    @Autowired
    DdpAedRuleService ddpAedRuleService;

    @Autowired
    DdpCompressionSetupService ddpCompressionSetupService;

    @Autowired
    DdpDocnameConvService ddpDocnameConvService;

    @Autowired
    DdpMergeSetupService ddpMergeSetupService;

    @Autowired
    DdpNotificationService ddpNotificationService;

    @Autowired
    DdpUserService ddpUserService;

    @Autowired
    DdpCommOptionsLkpService commOptionsLkpService;
    
    @Autowired
    DdpGenSourceLkpService ddpGenSourceLkpService;
    
    @Autowired
    DdpGenSourceSetupService ddpGenSourceSetupService;
    
    @Autowired
    DdpRateLkpService ddpRateLkpService;
    
    @Autowired
    DdpRateSetupService ddpRateSetupService;
    
    @Autowired
    RuleUtil ruleUtil;
    
    @Autowired
    private ApplicationProperties applicationProperties;
    	
	public Calendar currentDate ; 
	

//	public  boolean dflag=false;
	
//	@RequestMapping(value="/list/mediator", produces = "text/html")
//	   public String mediatorUrl(Model uiModel) {
//	       return "redirect:/ddpaedrules/mediator";
//	   }
	
    @RequestMapping(value = "/mediator", method = RequestMethod.GET)
    public String mediatorController(Model uiModel) {
    	
    	logger.info("DdpAedRuleController.mediatorController() Method Invoked.");
    	List<DdpCompany> userCompanies = new ArrayList<DdpCompany>();
    	String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
    	Integer activeFlag = 1;
    	DdpUser ddpUser = null ;
    	List<DdpUser> ddpUserLst = checkUserExististance(strUserName);
		if(! ddpUserLst.isEmpty())
		{
			ddpUser = ddpUserLst.get(0);
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
    		userCompanies = ruleUtil.getAccessibleCompaniesLst(strUserName, env.getProperty("rule.aed.firstchars"));
    		List<String> listCompanies = new ArrayList<String>();
		    for(DdpCompany company: userCompanies ){
		    	 listCompanies.add(company.getComCompanyCode());
		    }
		    userCompanies.removeAll(ruleUtil.getListOfReadOnlyAccessibleCompanies(strUserName, env.getProperty("rule.aed.firstchars")));
			//Get the branches for the company
			List<DdpBranch>  branchs =  ruleUtil.findBranchbyCompany(ddpUser.getUsrCompanyCode());
			List<String> listBranches = new ArrayList<String>();
//			for(DdpBranch strbrn :branchs){
//				listBranches.add(strbrn.getBrnBranchName());	
//			}
			
			for(DdpBranch strbrn :branchs){
				listBranches.add(strbrn.getBrnBranchCode());	
			}
			//userCompanies.add(userCompany);
			uiModel.addAttribute("accessToCreate",userCompanies.size());
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
    	logger.info("DdpAedRuleController.mediatorController() Ecxecuted Successfully.");
        return "ddpaedrules/display";
    }
   
    /**
     *
     * @param uiModel
     * @return
     */
    @RequestMapping(value = {"/form","/list/form"}, produces = "text/html")
    public String createForm(Model uiModel,@RequestParam(value="customerId",required=false) String customerId) {
    	logger.info("DdpAedRuleController.mediatorController() Method Invoked.");
        RuleWrapper ruleWrapper = new RuleWrapper();
        String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
        // Get the logged in user company
        String userCompany = ruleUtil.getUserCompany(strUserName);
        if(customerId != null)
        {
        	uiModel.addAttribute("ruleParty","one");
        	uiModel.addAttribute("duplicateCustID",customerId);
        }
        uiModel.addAttribute("rulewrapper", ruleWrapper);
        populateEditForm(uiModel, new DdpAedRule(), userCompany,"createForm");
    	logger.info("DdpAedRuleController.mediatorController() Ecxecuted Successfully.");
        return "ddpaedrules/create";
    }

    /**
     *
     * @param ruleWrapper
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
    @AuditLog(message="Aed Rule Created. ")
   	@Transactional
    @RequestMapping(value = {"","/list"}, params="create",method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid RuleWrapper ruleWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	
    	logger.info("DdpAedRuleController.create() Method Invoked.");
    	String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
        // Get the row for number of document types are selected/parties were
        // selected
        String rowCount = httpServletRequest.getParameter("rowscount");
        // Get the doctype,party code and printing and emailing enable option
        LinkedList<Object> docList = new LinkedList<Object>();
        LinkedList<Object> partyList = new LinkedList<Object>();
        LinkedList<Object> optionList = new LinkedList<Object>();
        LinkedList<Object> genSourceList = new LinkedList<Object>();
        LinkedList<Object> rateList = new LinkedList<Object>();
        LinkedList<Object> slaFreqList = new LinkedList<Object>();
        LinkedList<Object> slaMinList = new LinkedList<Object>();
        LinkedList<Object> slaMaxList = new LinkedList<Object>();
        
        int rCount = Integer.parseInt(rowCount);
        String optionfalg = null ;
        String docTypefalg = null;
        String partyfalg = null;
        String genSourcefalg = null;
        String ratefalg = null;
        String slaFreqfalg = null;
        String slaMinfalg = null;
        String slaMaxfalg = null;
    	for (int i = 1; i <= rCount ; i++) {
            int count = i;
            optionfalg = httpServletRequest.getParameter("selectCommOption" + count);
            docTypefalg = httpServletRequest.getParameter("selectDoctype" + count);
            partyfalg = httpServletRequest.getParameter("selectpartycode" + count);
            genSourcefalg = httpServletRequest.getParameter("selectDdpGenSource"+ count);
            ratefalg = httpServletRequest.getParameter("selectDdpRate"+ count);
            slaFreqfalg = httpServletRequest.getParameter("selectSLAfreq"+ count);
            slaMinfalg = httpServletRequest.getParameter("SLAMin"+ count);
            slaMaxfalg = httpServletRequest.getParameter("SLAMax"+ count);
            if (docTypefalg != null) {
                docList.add(docTypefalg);
                partyList.add(partyfalg);
                optionList.add(optionfalg);
                rCount = rCount + 1;
                genSourceList.add(genSourcefalg);
                rateList.add(ratefalg);
                slaFreqList.add(slaFreqfalg);
                slaMinList.add(slaMinfalg);
                slaMaxList.add(slaMaxfalg);
            }
        }
      //get partyId
    	String partyId = ruleWrapper.getDdpRuleDetail().getRdtPartyId();
    	String companycode = ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode();
    	
    	// Get the selected branch
        String[] selectedBranchList = httpServletRequest.getParameterValues("lstBranch");
        // Flag to check the ALL Branches selected
        List<String> brnList = Arrays.asList(selectedBranchList);
        if (brnList.contains("ALL")) {
            brnList = new ArrayList<String>();
            brnList.add("ALL");
        }
      //check this party id is available or not.
    	List<Integer> rids = getRuleIdsByPartyCode(partyId,companycode,docList,partyList,brnList,null);
    	if(rids.size() > 0)
    	{
    		return "redirect:/ddpaedrules/list/form?customerId="+partyId;
    	}
         
        List<String> requestParameterNames = Collections.list((Enumeration<String>) httpServletRequest.getParameterNames());
        for (String parameterName : requestParameterNames) {
            String attributeName = parameterName;
            String attributeValue = httpServletRequest.getParameter(parameterName);
//            System.out.println(attributeName + ":" + attributeValue);
        }
        
       
        /**
         * ##### Save DDp Communication Email ######
         *
         * 1) Save DDPCommunication Email 2) Get the saved communication email
         * id
         *
         */
        // Save DDp Communication Email
        //set unc path to ddpcommEmail
        String evn = env.getProperty("app.evn").trim();
        String uncPath = env.getProperty("rule.unc.path."+evn);
        ruleWrapper.getDdpCommEmail().setCemUncPath(uncPath);
        ddpCommEmailService.saveDdpCommEmail(ruleWrapper.getDdpCommEmail());
        // Get the auto increments id commEmailId
        Integer commEmailId = ruleWrapper.getDdpCommEmail().getCemEmailId();
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
        //set date
    	currentDate = Calendar.getInstance();
        ruleWrapper.getDdpCommunicationSetup().setCmsCommunicationProtocol(env.getProperty("rule.comm.prototocol"));
        ruleWrapper.getDdpCommunicationSetup().setCmsProtocolSettingsId(commEmailId.toString());
        ruleWrapper.getDdpCommunicationSetup().setCmsStatus(0);
        ruleWrapper.getDdpCommunicationSetup().setCmsCreatedBy(strUserName);
//        ruleWrapper.getDdpCommunicationSetup().setCmsCreatedDate(Constant.CURRENTDATE);
        ruleWrapper.getDdpCommunicationSetup().setCmsCreatedDate(currentDate);
        ruleWrapper.getDdpCommunicationSetup().setCmsModifiedBy(strUserName);
//        ruleWrapper.getDdpCommunicationSetup().setCmsModifiedDate(Constant.CURRENTDATE);
        ruleWrapper.getDdpCommunicationSetup().setCmsModifiedDate(currentDate);
        // Save DDPCommunicationSetup
        ddpCommunicationSetupService.saveDdpCommunicationSetup(ruleWrapper.getDdpCommunicationSetup());
        // Get the cms_communication_id
        Integer comSetupId = ruleWrapper.getDdpCommunicationSetup().getCmsCommunicationId();
        /**********************************************************************************************************/
        		//save DdpNotification
      		DdpNotification ddpNotification = ruleWrapper.getDdpNotification();
      		ddpNotification.setNotFailureEmailAddress(env.getProperty("email.from"));
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
        String ruleName = ddpCompany.getComRegion() + "_" + ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode() + "_" + ruleWrapper.getDdpRuleDetail().getRdtPartyId();
        if(ruleWrapper.getDdpRule().getRulDescription().equals(""))
        	ruleDescription = ruleWrapper.getDdpRuleDetail().getRdtPartyId();
        else
        	ruleDescription = ruleWrapper.getDdpRule().getRulDescription();
        
        ruleDescription = ddpCompany.getComRegion() + "_" + ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode() + "_" + ruleDescription;
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
        // Set Value for AED Rule Id
        ruleWrapper.getDdpAedRule().setAedCommunicationId(ddpCommunicationSetupService.findDdpCommunicationSetup(comSetupId));
        // Default value is 0/1 , 0-yes(is required), 1-No(is not required)
        // 0 - Yes, 1-No
        ruleWrapper.getDdpAedRule().setAedIsPartyCheckRequired(0);
        ruleWrapper.getDdpAedRule().setAedStatus(0);
        //Set the Activation date , activate the rule on date,
        //if no date is mentioned by the user, activate the rule at the time of creation
        if (null == ruleWrapper.getDdpRuleDetail().getRdtActivationDate() || "".equals(ruleWrapper.getDdpRuleDetail().getRdtActivationDate())) {
            ruleWrapper.getDdpAedRule().setAedActivationDate(currentDate);
        } else {
            ruleWrapper.getDdpAedRule().setAedActivationDate(ruleWrapper.getDdpRuleDetail().getRdtActivationDate());
        }
        ruleWrapper.getDdpAedRule().setAedCreatedBy(strUserName);
        ruleWrapper.getDdpAedRule().setAedCreatedDate(currentDate);
        ruleWrapper.getDdpAedRule().setAedModifiedBy(strUserName);
        ruleWrapper.getDdpAedRule().setAedModifiedDate(currentDate);
        ruleWrapper.getDdpAedRule().setAedNotificationId(ddpNotification);
        // Insert Rule
        ruleWrapper.getDdpAedRule().setAedRuleId(ruleId);
        // Save AED Rule
        ddpAedRuleService.saveDdpAedRule(ruleWrapper.getDdpAedRule());
        /*******************************************************************************/
        /**
         * ##### Save Rule Details table ######
         *
         * 1) Loop all the seleceted branches 2) Inner Loop will be selected
         * document types, party code for that branches.
         *
         */
        /****************************************************************************/
        String department = ((ruleWrapper.getDdpRuleDetail().getRdtDepartment() != null && ruleWrapper.getDdpRuleDetail().getRdtDepartment().trim().length() > 0)? ruleWrapper.getDdpRuleDetail().getRdtDepartment().trim() : null );
        if (department != null && department.endsWith(","))
        	department = department.substring(0, department.length()-1);
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
                DdpDoctype ddpDoctypes = ddpDoctypeService.findDdpDoctype(docList.get(i).toString());
                ddpRuleDetail.setRdtDocType(ddpDoctypes);
                // Get PartyBean
                DdpParty ddpParties = ddpPartyService.findDdpParty(partyList.get(i).toString());
                ddpRuleDetail.setRdtPartyCode(ddpParties);
                // Generate system null
                // Shedular id null
                // Save the communication lookup id in RuleDetail table
                DdpCommunicationSetup ddpCommunicationSetup = ddpCommunicationSetupService.findDdpCommunicationSetup(comSetupId);
                ddpRuleDetail.setRdtCommunicationId(ddpCommunicationSetup);
                // Set status as active for creating rule
                ddpRuleDetail.setRdtStatus(0);
                // Set Party code
                ddpRuleDetail.setRdtPartyId(ruleWrapper.getDdpRuleDetail().getRdtPartyId().trim());
                // set created by , modified by and created date and modified
                // date
                ddpRuleDetail.setRdtCreatedBy(strUserName);
                ddpRuleDetail.setRdtCreatedDate(currentDate);
                ddpRuleDetail.setRdtModifiedBy(strUserName);
                ddpRuleDetail.setRdtModifiedDate(currentDate);
                //set updated Activation date
                if (null == ruleWrapper.getDdpRuleDetail().getRdtActivationDate() || "".equals(ruleWrapper.getDdpRuleDetail().getRdtActivationDate())) {
                    ddpRuleDetail.setRdtActivationDate(currentDate);
                } else {
                    ddpRuleDetail.setRdtActivationDate(ruleWrapper.getDdpRuleDetail().getRdtActivationDate());
                }
                // Set Rule Type
                ddpRuleDetail.setRdtRuleType(env.getProperty("rule.type.aed"));
                ddpRuleDetail.setRdtDepartment(department);
                ddpRuleDetail.setRdtSlaFreq(slaFreqList.get(i).toString());
                ddpRuleDetail.setRdtSlaMin(slaMinList.get(i).toString());
                ddpRuleDetail.setRdtSlaMax(slaMaxList.get(i).toString());
                ddpRuleDetail.setRdtNotificationId(ddpNotification);
                // Save rule deails
                ddpRuleDetailService.saveDdpRuleDetail(ddpRuleDetail);
                // Get save rule deatil id 
                Integer ruleDetailId = ddpRuleDetail.getRdtId();
                if (optionList.get(i).toString().equalsIgnoreCase("Not Applicable")) {
                    DdpCommOptionsSetup ddpCommOptionsSetup = new DdpCommOptionsSetup();
                    ddpCommOptionsSetup.setCopRdtId(ddpRuleDetail);
                    ddpCommOptionsSetup.setCopOption("Not Applicable");
                    ddpCommOptionsSetup.setCopCreatedBy(strUserName);
                    ddpCommOptionsSetup.setCopCreatedDate(currentDate);
                    ddpCommOptionsSetup.setCopModifiedBy(strUserName);
                    ddpCommOptionsSetup.setCopModifiedDate(currentDate);
                    ddpCommOptionsSetupService.saveDdpCommOptionsSetup(ddpCommOptionsSetup);
                }
                else
                {
                	 if (optionList.get(i).toString().equalsIgnoreCase("Yes")) {
                         DdpCommOptionsSetup ddpCommOptionsSetup = new DdpCommOptionsSetup();
                         ddpCommOptionsSetup.setCopRdtId(ddpRuleDetailService.findDdpRuleDetail(ruleDetailId));
                         ddpCommOptionsSetup.setCopOption("Printing");
                         ddpCommOptionsSetup.setCopCreatedBy(strUserName);
                         ddpCommOptionsSetup.setCopCreatedDate(currentDate);
                         ddpCommOptionsSetup.setCopModifiedBy(strUserName);
                         ddpCommOptionsSetup.setCopModifiedDate(currentDate);
                         ddpCommOptionsSetupService.saveDdpCommOptionsSetup(ddpCommOptionsSetup);
                     }
                     //if (optionList.get(i).toString().equalsIgnoreCase("Emailing")) {
                         DdpCommOptionsSetup ddpCommOptionsSetup = new DdpCommOptionsSetup();
                         ddpCommOptionsSetup.setCopRdtId(ddpRuleDetailService.findDdpRuleDetail(ruleDetailId));
                         ddpCommOptionsSetup.setCopOption("Emailing");
                         ddpCommOptionsSetup.setCopCreatedBy(strUserName);
                         ddpCommOptionsSetup.setCopCreatedDate(currentDate);
                         ddpCommOptionsSetup.setCopModifiedBy(strUserName);
                         ddpCommOptionsSetup.setCopModifiedDate(currentDate);
                         ddpCommOptionsSetupService.saveDdpCommOptionsSetup(ddpCommOptionsSetup);
                     //}
                }
//                if (optionList.get(i).toString().equalsIgnoreCase("Both")) {
//                    // Save Printing
//                    DdpCommOptionsSetup ddpCommOptionsSetup = new DdpCommOptionsSetup();
//                    ddpCommOptionsSetup.setCopRdtId(ddpRuleDetailService.findDdpRuleDetail(ruleDetailId));
//                    ddpCommOptionsSetup.setCopOption("Printing");
//                    ddpCommOptionsSetup.setCopCreatedBy(strUserName);
//                    ddpCommOptionsSetup.setCopCreatedDate(currentDate);
//                    ddpCommOptionsSetup.setCopModifiedBy(strUserName);
//                    ddpCommOptionsSetup.setCopModifiedDate(currentDate);
//                    ddpCommOptionsSetupService.saveDdpCommOptionsSetup(ddpCommOptionsSetup);
//                    // Save Emailing
//                    ddpCommOptionsSetup = new DdpCommOptionsSetup();
//                    ddpCommOptionsSetup.setCopRdtId(ddpRuleDetailService.findDdpRuleDetail(ruleDetailId));
//                    ddpCommOptionsSetup.setCopOption("Emailing");
//                    ddpCommOptionsSetup.setCopCreatedBy(strUserName);
//                    ddpCommOptionsSetup.setCopCreatedDate(currentDate);
//                    ddpCommOptionsSetup.setCopModifiedBy(strUserName);
//                    ddpCommOptionsSetup.setCopModifiedDate(currentDate);
//                    ddpCommOptionsSetupService.saveDdpCommOptionsSetup(ddpCommOptionsSetup);
//                }
                //save Generate System
                DdpGenSourceSetup ddpGenSourceSetup = new DdpGenSourceSetup();
                ddpGenSourceSetup.setGssRdtId(ddpRuleDetailService.findDdpRuleDetail(ruleDetailId));
                ddpGenSourceSetup.setGssOption(genSourceList.get(i).toString());
                ddpGenSourceSetup.setGssCreatedBy(strUserName);
                ddpGenSourceSetup.setGssCreatedDate(currentDate);
                ddpGenSourceSetup.setGssModifiedBy(strUserName);
                ddpGenSourceSetup.setGssModifiedDate(currentDate);
                ddpGenSourceSetupService.saveDdpGenSourceSetup(ddpGenSourceSetup);
                
                //Save Rated/Not Rated document
                DdpRateSetup ddpRateSetup = new DdpRateSetup();
                ddpRateSetup.setRtsRdtId(ddpRuleDetailService.findDdpRuleDetail(ruleDetailId));
                ddpRateSetup.setRtsOption(rateList.get(i).toString());
                ddpRateSetup.setRtsCreatedBy(strUserName);
                ddpRateSetup.setRtsCreatedDate(currentDate);
                ddpRateSetup.setRtsModifiedBy(strUserName);
                ddpRateSetup.setRtsModifiedDate(currentDate);
                ddpRateSetupService.saveDdpRateSetup(ddpRateSetup);
            }
        }
        uiModel.asMap().clear();
        // ddpAedRuleService.saveDdpAedRule(ruleWrapper.getDdpAedRule());
        logger.info("DdpAedRuleController.create() Executed Successfully.");
        return "redirect:/ddpaedrules/list/"+ruleId+"/"+ddpCompany.getComCompanyCode()+"?createxml";
    }
    

    /**
     *
     * @param aedRuleId
     * @param uiModel
     * @return
     */
    @RequestMapping(value = {"/{aedRuleId}/form","/list/{aedRuleId}/form"}, produces = "text/html")
    public String updateForm(@PathVariable("aedRuleId") Integer aedRuleId, Model uiModel,@RequestParam(value="customerId",required=false) String customerId) {
    	 logger.info("DdpAedRuleController.updateForm() Method Invoked.");
        RuleWrapper ruleWrapper = new RuleWrapper();
        if(customerId != null)
        {
        	uiModel.addAttribute("duplicateCustID",customerId);
//        	dflag = false;
        }
        // Prepare list to get the Rule
        DdpRule ddpRule = ddpRuleService.findDdpRule(aedRuleId);
        ruleWrapper.setDdpRule(ddpRule);
        /*****************************************************************************************************************/
        // Prepare Aed List
        DdpAedRule ddpAedRule = ddpAedRuleService.findDdpAedRule(aedRuleId);
        ruleWrapper.setDdpAedRule(ddpAedRule);
        /*****************************************************************************************************************/
        /***************************************************/
        //prepare notification 
        ruleWrapper.setDdpNotification(ddpAedRule.getAedNotificationId());
        /***************************************************/
        // Prepare RuleDetail
        TypedQuery<DdpRuleDetail> query = DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(ddpRule);
        List<DdpRuleDetail> ddpRuleDetails = query.getResultList();
        
        List<DdpBranch> ddpBranchs1 = new ArrayList<DdpBranch>();
        ddpBranchs1.add(ddpBranchService.findDdpBranch(ddpRuleDetails.get(0).getRdtBranch().getBrnBranchCode()));
        ruleWrapper.setDdpRuleDetail(ddpRuleDetails.get(0));
        /*****************************************************************************************************************/
        // Row Count
        List<String> docTypeLst = new ArrayList<String>();
        for(DdpRuleDetail ddpRuleDetail: ddpRuleDetails)
        {
        	docTypeLst.add(ddpRuleDetail.getRdtDocType().getDtyDocTypeCode());
        }
        List<String> pCodeLst = new ArrayList<String>();
        for(DdpRuleDetail ddpRuleDetail: ddpRuleDetails)
        {
        	pCodeLst.add(ddpRuleDetail.getRdtPartyCode().getPtyPartyCode());
        }
        Set<String> t = new LinkedHashSet<String>();
        for(int i=0;i<docTypeLst.size();i++)
        {
        	t.add(docTypeLst.get(i)+"-"+pCodeLst.get(i));
        }
        Integer acrowcount = 0;
        if(docTypeLst.size() >= pCodeLst.size())
        	acrowcount = t.size();
    	
        
        Integer rowcount = ddpRuleDetails.size();
        
//        Integer rowcount = acrowcount;
        ruleWrapper.setRowcount(rowcount);
        /*****************************************************************************************************************/
        // Prepare Communication Setup
        DdpCommunicationSetup ddpCommSetup = ddpRuleDetails.get(0).getRdtCommunicationId();
        String comprotoco = ddpCommSetup.getCmsProtocolSettingsId();
        ruleWrapper.setDdpCommEmail(ddpCommEmailService.findDdpCommEmail(Integer.parseInt(comprotoco)));
        /*****************************************************************************************************************/
        // Prepare Country branch list
        populateEditForm(uiModel, ddpAedRuleService.findDdpAedRule(aedRuleId), ddpRuleDetails.get(0).getRdtCompany().getComCompanyCode().toString(),"updateForm");
        /*****************************************************************************************************************/
        // Prepare selectd branch
        List<DdpBranch> branchs = new ArrayList<DdpBranch>();
        for (DdpRuleDetail ruleDetail : ddpRuleDetails) {
            branchs.add(ruleDetail.getRdtBranch());
        }
        Set<DdpBranch> list3 = new LinkedHashSet<DdpBranch>();
        list3.addAll(branchs);
        List<DdpBranch> ddpBranchs = new ArrayList<DdpBranch>();
        for (DdpBranch ddpBranch : list3) {
            ddpBranchs.add(ddpBranchService.findDdpBranch(ddpBranch.getBrnBranchCode()));
        }
        uiModel.addAttribute("selectedBranchList", ddpBranchs);
        ruleWrapper.setLstBranch(ddpBranchs);
        /*****************************************************************************************************************/
        // Prepare Doctype and Party code
        List<RuleDao> ruleDaos = getRuleDetailForRuleId(aedRuleId.toString(),acrowcount);
        // Set<RuleDao> test = Set<RuleDao>(ruleDaos);
        // ruleDaos.clear();
        // ruleDaos = ArrayLsit<RuleDao>(test);
        uiModel.addAttribute("ddpaedrule", ruleDaos);
        uiModel.addAttribute("itemId", aedRuleId);
        uiModel.addAttribute("rulewrapper", ruleWrapper);
        logger.info("DdpAedRuleController.updateForm() Executed Successfully.");
        return "ddpaedrules/update";
    }

    /**
     *
     * @param ruleWrapper
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
    @Transactional
    @AuditLog(message="Aed Rule Updated. ")
    @RequestMapping(value={"","/list"}, params="update",method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid RuleWrapper ruleWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpAedRuleController.update() Method Invoked.");
    	String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ruleWrapper.getDdpAedRule(), null,"update");
            return "ddpaedrules/update";
        }
        /********* For Testing ***********************/
        // List<String> requestParameterNames =
        // Collections.list((Enumeration<String>)
        // httpServletRequest.getParameterNames());
        // for (String parameterName : requestParameterNames ) {
        // String attributeName = parameterName;
        // String attributeValue =
        // httpServletRequest.getParameter(parameterName);
        // System.out.println(attributeName + ":" + attributeValue);
        // }
        /********* Testing block ends ****************/
        // Get the row count
        String rowCount = httpServletRequest.getParameter("rowscount");
        // Get the doctype,party code and printing and emailing enable option
        LinkedList<Object> docList = new LinkedList<Object>();
        LinkedList<Object> partyList = new LinkedList<Object>();
        LinkedList<Object> optionList = new LinkedList<Object>();
        LinkedList<Object> genSourceList = new LinkedList<Object>();
        LinkedList<Object> rateList = new LinkedList<Object>();
        LinkedList<Object> slaFreqList = new LinkedList<Object>();
        LinkedList<Object> slaMinList = new LinkedList<Object>();
        LinkedList<Object> slaMaxList = new LinkedList<Object>();
        int rCount = Integer.parseInt(rowCount);
        for (int i = 1; i <= rCount ; i++) {
            int count = i;
            String optionfalg = httpServletRequest.getParameter("selectCommOption" + count);
            String docTypefalg = httpServletRequest.getParameter("selectDoctype" + count);
            String partyfalg = httpServletRequest.getParameter("selectpartycode" + count);
            String genSourcefalg = httpServletRequest.getParameter("selectDdpGenSource" + count);
            String ratefalg = httpServletRequest.getParameter("selectDdpRate" + count);
            String slaFreqfalg = httpServletRequest.getParameter("selectSLAfreq"+ count);
            String slaMinfalg = httpServletRequest.getParameter("SLAMin"+ count);
            String slaMaxfalg = httpServletRequest.getParameter("SLAMax"+ count);
            if (docTypefalg != null) {
                docList.add(docTypefalg);
                partyList.add(partyfalg);
                optionList.add(optionfalg);
                genSourceList.add(genSourcefalg);
                rateList.add(ratefalg);
                slaFreqList.add(slaFreqfalg);
                slaMinList.add(slaMinfalg);
                slaMaxList.add(slaMaxfalg);
                rCount = rCount + 1;
            }
        }
        // Get the selected branch
        String[] selectedBranchList = httpServletRequest.getParameterValues("lstBranch");
        List<String> brnList = Arrays.asList(selectedBranchList);
        if(brnList.contains("ALL"))
        {
        	brnList = new ArrayList<String>();
        	brnList.add("ALL");
        }
        // Get Status
        String status = httpServletRequest.getParameter("status");
        // Prepare RuleDetail
        TypedQuery<DdpRuleDetail> rdtquery = DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(ddpRuleService.findDdpRule(ruleWrapper.getDdpAedRule().getAedRuleId()));
        List<DdpRuleDetail> ddpRuleDetails = rdtquery.getResultList();
        
        /*
        verify whether partyId value is modified.
        if modified check whether new partyId value is already exist. 
        */
        String newPartyId = ruleWrapper.getDdpRuleDetail().getRdtPartyId().toString().trim();
        String oldPartyId = ddpRuleDetails.get(0).getRdtPartyId().trim();
       	String companycode = ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode();
       	//check this party id is available or not.
       	List<Integer> rids = getRuleIdsByPartyCode(newPartyId,companycode,docList,partyList,brnList,ruleWrapper.getDdpAedRule().getAedRuleId());
       	if(rids.size() > 0)
       	{
       		return "redirect:/ddpaedrules/list/"+ruleWrapper.getDdpAedRule().getAedRuleId().toString()+"/form?customerId="+newPartyId;
       	}
        /******
         * Get the created date and created by user name before deleting the
         * records in DDPRuleDetail table
         ***********/
        Map<String, Integer> oldDocTypeRdtIDMap = new HashMap<String, Integer>();
		Map<String, Integer> newDocTypeRdtIDMap = new HashMap<String, Integer>();
        String crtedUserName = ddpRuleDetails.get(0).getRdtCreatedBy();
        Calendar crtedDateAndTime = ddpRuleDetails.get(0).getRdtCreatedDate();
        /*****************************************************************************************************************/
        /***************** Prepare CommunicationSetUp Email ****************************************************************/
        DdpCommunicationSetup ddpCommSetup = ddpRuleDetails.get(0).getRdtCommunicationId();
        String comprotoco = ddpCommSetup.getCmsProtocolSettingsId();
        /*****************************************************************************************************************/
        /***************** Get the Communication Email and Update Communication Email Services *****************************/
        DdpCommEmail ddpcommEmail = ddpCommEmailService.findDdpCommEmail(Integer.parseInt(comprotoco));
        ddpcommEmail.setCemEmailFrom(ruleWrapper.getDdpCommEmail().getCemEmailFrom());
        ddpcommEmail.setCemEmailTo(ruleWrapper.getDdpCommEmail().getCemEmailTo());
        ddpcommEmail.setCemEmailCc(ruleWrapper.getDdpCommEmail().getCemEmailCc());
        ddpcommEmail.setCemEmailSubject(ruleWrapper.getDdpCommEmail().getCemEmailSubject());
        ddpcommEmail.setCemEmailBody(ruleWrapper.getDdpCommEmail().getCemEmailBody());
        ddpCommEmailService.updateDdpCommEmail(ddpcommEmail);
        /*****************************************************************************************************************/
        /**************** Update Communication Setup Services **************************************************************/
      //set date
    	currentDate = Calendar.getInstance();
        ddpCommSetup.setCmsModifiedBy(strUserName);
        ddpCommSetup.setCmsModifiedDate(currentDate);
        ddpCommunicationSetupService.updateDdpCommunicationSetup(ddpCommSetup);
        // Get the cms_communication_id
        Integer comSetupId = ddpCommSetup.getCmsCommunicationId();
        /*****************************************************************************************************************/
        /********************** Updating DdpNotification *****************************************************************/
        DdpNotification ddpNotification = null;
        ddpNotification = ddpAedRuleService.findDdpAedRule(ruleWrapper.getDdpAedRule().getAedRuleId()).getAedNotificationId();
        if(ddpNotification == null)
        {
        	ddpNotification = new DdpNotification(); 
        	ddpNotification.setNotCreatedBy(strUserName);
        	ddpNotification.setNotCreatedDate(currentDate);
        	ddpNotification.setNotStatus(0);
            ddpNotification.setNotSuccessEmailAddress(ruleWrapper.getDdpNotification().getNotSuccessEmailAddress());
            ddpNotification.setNotFailureEmailAddress(env.getProperty("email.from"));
            ddpNotification.setNotModifiedBy(strUserName);
            ddpNotification.setNotModifiedDate(currentDate);
            ddpNotificationService.saveDdpNotification(ddpNotification);
        }
        else{
        	ddpNotification.setNotStatus(0);
            ddpNotification.setNotSuccessEmailAddress(ruleWrapper.getDdpNotification().getNotSuccessEmailAddress());
            ddpNotification.setNotFailureEmailAddress(env.getProperty("email.from"));
            ddpNotification.setNotModifiedBy(strUserName);
            ddpNotification.setNotModifiedDate(currentDate);
           	ddpNotificationService.updateDdpNotification(ddpNotification);
        }
        
        /*************************************************************/
        for (DdpRuleDetail ddpRuleDetail : ddpRuleDetails) {
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
        	// Delete Communication Option for ddp detail id
            TypedQuery<DdpCommOptionsSetup> lst1 = DdpCommOptionsSetup.findDdpCommOptionsSetupsByCopRdtId(ddpRuleDetail);
            for (DdpCommOptionsSetup strOptionId : lst1.getResultList()) {
                ddpCommOptionsSetupService.deleteDdpCommOptionsSetup(strOptionId);
            }
        
            //Delete Generate Source
            TypedQuery<DdpGenSourceSetup> genSourcelst1 = DdpGenSourceSetup.findDdpGenSourceSetupsByGssRdtId(ddpRuleDetail);
            for (DdpGenSourceSetup strOptionId : genSourcelst1.getResultList()) {
            	mappKey = mappKey.concat("-" + strOptionId.getGssOption());
                ddpGenSourceSetupService.deleteDdpGenSourceSetup(strOptionId);
            }
        
            //Delete Rate/Not Rate
            TypedQuery<DdpRateSetup> ratelst = DdpRateSetup.findDdpRateSetupsByDdpRuleDetail(ddpRuleDetail);
            for (DdpRateSetup strOptionId : ratelst.getResultList()) {
            	mappKey = mappKey.concat("-" + strOptionId.getRtsOption());
                ddpRateSetupService.deleteDdpRateSetup(strOptionId);
            }
            oldDocTypeRdtIDMap.put(mappKey, ddpRuleDetail.getRdtId());
            ddpRuleDetailService.deleteDdpRuleDetail(ddpRuleDetail);
        }
        
      
        String department = ((ruleWrapper.getDdpRuleDetail().getRdtDepartment() != null && ruleWrapper.getDdpRuleDetail().getRdtDepartment().trim().length() > 0)? ruleWrapper.getDdpRuleDetail().getRdtDepartment().trim() : null );
        if (department != null && department.endsWith(","))
        	department = department.substring(0, department.length()-1);
        
        /************************** Loop the branch and add the rule details *********************************************/
        for (String strBranch : brnList) {
            // get the document type and party code
            for (int i = 0; i < docList.size(); i++) {
                DdpRuleDetail ddpRuleDetail = new DdpRuleDetail();
                DdpRule ddpRules = ddpRuleService.findDdpRule(ruleWrapper.getDdpAedRule().getAedRuleId());
                ddpRuleDetail.setRdtRuleId(ddpRules);
                // Set Company code
                DdpCompany ddpCompany = ddpCompanyService.findDdpCompany(ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode());
                ddpRuleDetail.setRdtCompany(ddpCompany);
                // Get Branch Detail based on selected branch code and country
                // code.
                DdpBranch ddpBranch = ddpBranchService.findDdpBranch(strBranch);
                ddpRuleDetail.setRdtBranch(ddpBranch);
                // Get DocTypeDoc
                DdpDoctype ddpDoctypes = ddpDoctypeService.findDdpDoctype(docList.get(i).toString());
                ddpRuleDetail.setRdtDocType(ddpDoctypes);
                // Get PartyBean
                DdpParty ddpParties = ddpPartyService.findDdpParty(partyList.get(i).toString());
                ddpRuleDetail.setRdtPartyCode(ddpParties);
                // Generate system null
                // Shedular id null
                // Save the communication lookup id in RuleDetail table
                DdpCommunicationSetup ddpCommunicationSetup = ddpCommunicationSetupService.findDdpCommunicationSetup(comSetupId);
                ddpRuleDetail.setRdtCommunicationId(ddpCommunicationSetup);
                // Set status as active for creating rule
                ddpRuleDetail.setRdtStatus(Integer.parseInt(status));
                // Set Party code
                ddpRuleDetail.setRdtPartyId(ruleWrapper.getDdpRuleDetail().getRdtPartyId().trim());
                //set updated Activation date
                if (null == ruleWrapper.getDdpRuleDetail().getRdtActivationDate() || "".equals(ruleWrapper.getDdpRuleDetail().getRdtActivationDate())) {
                    ddpRuleDetail.setRdtActivationDate(currentDate);
                } else {
                    ddpRuleDetail.setRdtActivationDate(ruleWrapper.getDdpRuleDetail().getRdtActivationDate());
                }
                // set created by , modified by and created date and modified
                // date
                ddpRuleDetail.setRdtCreatedBy(crtedUserName);
                ddpRuleDetail.setRdtCreatedDate(crtedDateAndTime);
                ddpRuleDetail.setRdtModifiedBy(strUserName);
                ddpRuleDetail.setRdtModifiedDate(currentDate);
                // Set Rule Type
                ddpRuleDetail.setRdtRuleType(env.getProperty("rule.type.aed"));
                ddpRuleDetail.setRdtDepartment(department);
                ddpRuleDetail.setRdtSlaFreq(slaFreqList.get(i).toString());
                ddpRuleDetail.setRdtSlaMin(slaMinList.get(i).toString());
                ddpRuleDetail.setRdtSlaMax(slaMaxList.get(i).toString());
                ddpRuleDetail.setRdtNotificationId(ddpNotification);
                // Save rule deails
                ddpRuleDetailService.saveDdpRuleDetail(ddpRuleDetail);
                // Get save rule deatil id
                DdpCommOptionsSetup ddpCommOptionsSetup = new DdpCommOptionsSetup();
                Integer ruleDetailId1 = ddpRuleDetail.getRdtId();
                if (optionList.get(i).toString().equalsIgnoreCase("Not Applicable")) {
                    ddpCommOptionsSetup = new DdpCommOptionsSetup();
                   ddpCommOptionsSetup.setCopRdtId(ddpRuleDetail);
                   ddpCommOptionsSetup.setCopOption("Not Applicable");
                   ddpCommOptionsSetup.setCopCreatedBy(strUserName);
                   ddpCommOptionsSetup.setCopCreatedDate(currentDate);
                   ddpCommOptionsSetup.setCopModifiedBy(strUserName);
                   ddpCommOptionsSetup.setCopModifiedDate(currentDate);
                   ddpCommOptionsSetupService.saveDdpCommOptionsSetup(ddpCommOptionsSetup);
               }
	           else
	           {
	        	   if (optionList.get(i).toString().equalsIgnoreCase("Yes")) {
	                    ddpCommOptionsSetup = new DdpCommOptionsSetup();
	                    ddpCommOptionsSetup.setCopRdtId(ddpRuleDetailService.findDdpRuleDetail(ruleDetailId1));
	                    ddpCommOptionsSetup.setCopOption("Printing");
	                    ddpCommOptionsSetup.setCopCreatedBy(strUserName);
	                    ddpCommOptionsSetup.setCopCreatedDate(currentDate);
	                    ddpCommOptionsSetup.setCopModifiedBy(strUserName);
	                    ddpCommOptionsSetup.setCopModifiedDate(currentDate);
	                    ddpCommOptionsSetupService.saveDdpCommOptionsSetup(ddpCommOptionsSetup);
	                }
//	                if (optionList.get(i).toString().equalsIgnoreCase("Emailing")) {
	                    ddpCommOptionsSetup = new DdpCommOptionsSetup();
	                    ddpCommOptionsSetup.setCopRdtId(ddpRuleDetailService.findDdpRuleDetail(ruleDetailId1));
	                    ddpCommOptionsSetup.setCopOption("Emailing");
	                    ddpCommOptionsSetup.setCopCreatedBy(strUserName);
	                    ddpCommOptionsSetup.setCopCreatedDate(currentDate);
	                    ddpCommOptionsSetup.setCopModifiedBy(strUserName);
	                    ddpCommOptionsSetup.setCopModifiedDate(currentDate);
	                    ddpCommOptionsSetupService.saveDdpCommOptionsSetup(ddpCommOptionsSetup);
//	                }
	            }
                
//                if (optionList.get(i).toString().equalsIgnoreCase("Both")) {
//                    ddpCommOptionsSetup = new DdpCommOptionsSetup();
//                    ddpCommOptionsSetup.setCopRdtId(ddpRuleDetailService.findDdpRuleDetail(ruleDetailId1));
//                    ddpCommOptionsSetup.setCopOption("Printing");
//                    ddpCommOptionsSetup.setCopCreatedBy(strUserName);
//                    ddpCommOptionsSetup.setCopCreatedDate(currentDate);
//                    ddpCommOptionsSetup.setCopModifiedBy(strUserName);
//                    ddpCommOptionsSetup.setCopModifiedDate(currentDate);
//                    ddpCommOptionsSetupService.saveDdpCommOptionsSetup(ddpCommOptionsSetup);
//                    ddpCommOptionsSetup = new DdpCommOptionsSetup();
//                    ddpCommOptionsSetup.setCopRdtId(ddpRuleDetailService.findDdpRuleDetail(ruleDetailId1));
//                    ddpCommOptionsSetup.setCopOption("Emailing");
//                    ddpCommOptionsSetup.setCopCreatedBy(strUserName);
//                    ddpCommOptionsSetup.setCopCreatedDate(currentDate);
//                    ddpCommOptionsSetup.setCopModifiedBy(strUserName);
//                    ddpCommOptionsSetup.setCopModifiedDate(currentDate);
//                    ddpCommOptionsSetupService.saveDdpCommOptionsSetup(ddpCommOptionsSetup);
//                }
                //save Generate System
                DdpGenSourceSetup ddpGenSourceSetup = new DdpGenSourceSetup();
                ddpGenSourceSetup.setGssRdtId(ddpRuleDetailService.findDdpRuleDetail(ruleDetailId1));
                ddpGenSourceSetup.setGssOption(genSourceList.get(i).toString());
                ddpGenSourceSetup.setGssCreatedBy(strUserName);
                ddpGenSourceSetup.setGssCreatedDate(currentDate);
                ddpGenSourceSetup.setGssModifiedBy(strUserName);
                ddpGenSourceSetup.setGssModifiedDate(currentDate);
                ddpGenSourceSetupService.saveDdpGenSourceSetup(ddpGenSourceSetup);
                
                //Save Rated/Not Rated document
                DdpRateSetup ddpRateSetup = new DdpRateSetup();
                ddpRateSetup.setRtsRdtId(ddpRuleDetailService.findDdpRuleDetail(ruleDetailId1));
                ddpRateSetup.setRtsOption(rateList.get(i).toString());
                ddpRateSetup.setRtsCreatedBy(strUserName);
                ddpRateSetup.setRtsCreatedDate(currentDate);
                ddpRateSetup.setRtsModifiedBy(strUserName);
                ddpRateSetup.setRtsModifiedDate(currentDate);
                ddpRateSetupService.saveDdpRateSetup(ddpRateSetup);
                
                String mappKey = ddpRuleDetail.getRdtPartyId() + "-"
						+ ddpRuleDetail.getRdtCompany().getComCompanyCode()
						+ "-" + ddpRuleDetail.getRdtBranch().getBrnBranchCode()
						+ "-"
						+ ddpRuleDetail.getRdtDocType().getDtyDocTypeCode()
						+ "-"
						+ ddpRuleDetail.getRdtPartyCode().getPtyPartyCode()
						+ "-" + genSourceList.get(i).toString() + "-"
						+ rateList.get(i).toString();
				newDocTypeRdtIDMap.put(mappKey, ddpRuleDetail.getRdtId());
            }
        }
        /********************* Prepare Rule Update and Update Rule *********************************************************/
        String ruleDescription = "";
        DdpCompany ddpCompany = ddpCompanyService.findDdpCompany(ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode());
        String ruleName = ddpCompany.getComRegion() + "_" + ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode() + "_" + ruleWrapper.getDdpRuleDetail().getRdtPartyId();
        if(ruleWrapper.getDdpRule().getRulDescription().equals(""))
        	ruleDescription = ruleName;
        else 
        	ruleDescription = ruleWrapper.getDdpRule().getRulDescription();
        
        DdpRule ddpRule = ddpRuleService.findDdpRule(ruleWrapper.getDdpAedRule().getAedRuleId());
        ddpRule.setRulName(ruleName);
        ddpRule.setRulDescription(ruleDescription);
//        ddpRule.setRulStatus(Integer.parseInt(status));
        ddpRule.setRulModifiedBy(strUserName);
        ddpRule.setRulModifiedDate(currentDate);
        ddpRuleService.updateDdpRule(ddpRule);
        // Get Updatag Rule Id
        Integer ddpRuleId = ddpRule.getRulId();
        /*****************************************************************************************************************/
        /************************************ Prepare AED Rule Update list *************************************************/
        DdpAedRule ddpAedRule = ddpAedRuleService.findDdpAedRule(ruleWrapper.getDdpAedRule().getAedRuleId());
        ddpAedRule.setAedCommunicationId(ddpCommunicationSetupService.findDdpCommunicationSetup(comSetupId));
        ddpAedRule.setAedModifiedBy(strUserName);
        ddpAedRule.setAedStatus(Integer.parseInt(status));
        ddpAedRule.setAedModifiedDate(currentDate);
        ddpAedRule.setAedNotificationId(ddpNotification);
        //Set the Activation date , activate the rule on date,
        //if no date is mentioned by the user, activate the rule at the time of creation
        if (null == ruleWrapper.getDdpRuleDetail().getRdtActivationDate() || "".equals(ruleWrapper.getDdpRuleDetail().getRdtActivationDate())) {
            ddpAedRule.setAedActivationDate(currentDate);
        } else {
            ddpAedRule.setAedActivationDate(ruleWrapper.getDdpRuleDetail().getRdtActivationDate());
        }
        // Update AED Rule
        ddpAedRuleService.updateDdpAedRule(ddpAedRule);
        // Get Updated AED Rule Id
        Integer ddpAedRuleID = ddpAedRule.getAedRuleId();
        /*****************************************************************************************************************/
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
 		ruleUtil.updateCategorizedRdt(replaceRdts, ddpAedRuleID, env.getProperty("rule.type.aed"));
        uiModel.asMap().clear();
        // ddpAedRuleService.updateDdpAedRule(ruleWrapper.getDdpAedRule());
        
        /*******************************************************************************/
        //Create XML file and move to servers.
        logger.info("DdpAedRuleController.update() Executed Successfully.");
        return "redirect:/ddpaedrules/list/"+ddpAedRuleID+"/"+ruleWrapper.getDdpRuleDetail().getRdtCompany().getComCompanyCode()+"?createxml";
    }

    /**
     *
     * @param page
     * @param size
     * @param uiModel
     * @return
     */
    @RequestMapping(value={"","/list"},produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    	  logger.info("DdpAedRuleController.list() Method Invoked.");
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpaedrules", ddpAedRuleService.findDdpAedRuleEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpAedRuleService.countAllDdpAedRules() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpaedrules", ddpAedRuleService.findAllDdpAedRules());
        }
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpAedRuleController.list() Executed Successfully.");
        return "ddpaedrules/list";
    }

    /**
     *
     * @param aedRuleId
     * @param page
     * @param size
     * @param uiModel
     * @return
     */
    @AuditLog(message="Aed Rule Deleted. ")
    @Transactional
    @RequestMapping(value = {"/{aedRuleId}","/list/{aedRuleId}","/list/list/{RuleId}"}, method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("aedRuleId") Integer aedRuleId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    	 logger.info("DdpAedRuleController.delete() Method Invoked.");
        // Prepare RuleDetail
        
        String strCompany = "";
        
       TypedQuery<DdpRuleDetail> bsquery = DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(ddpRuleService.findDdpRule(aedRuleId));
       List<DdpRuleDetail> ddpRuleDetails = bsquery.getResultList();
        
        
        for (DdpRuleDetail ruleDetail : ddpRuleDetails) {
            	strCompany = ruleDetail.getRdtCompany().getComCompanyCode();
                // Deleting option setup table
                Integer rdtId = ruleDetail.getRdtId();
                // Delete Communication Option for ddp detail id
                TypedQuery<DdpCommOptionsSetup> lst1 = DdpCommOptionsSetup.findDdpCommOptionsSetupsByCopRdtId(ddpRuleDetailService.findDdpRuleDetail(rdtId));
                List<DdpCommOptionsSetup> copIds = lst1.getResultList();
                for (DdpCommOptionsSetup strOptionId : copIds) {
                	 logger.info("DdpAedRuleController.delete() : Deleting DdpCommOptionsSetup of CopId:"+strOptionId);
                    ddpCommOptionsSetupService.deleteDdpCommOptionsSetup(strOptionId);
                }
        }
        // Deleting Detail
        for (DdpRuleDetail ddpRuleDetail : ddpRuleDetails) {
        	 logger.info("DdpAedRuleController.delete() : Deleting DdpRuleDetail of RdtId:"+ddpRuleDetail.getRdtId());
        	 //deleting GEN_SOURCE_SETUP
           	TypedQuery<DdpGenSourceSetup> query = DdpGenSourceSetup.findDdpGenSourceSetupsByGssRdtId(ddpRuleDetail);
           	List<DdpGenSourceSetup> ddpGenSourceSetups = query.getResultList();
           	for(DdpGenSourceSetup ddpGenSourceSetup:ddpGenSourceSetups)
           	{
           		ddpGenSourceSetupService.deleteDdpGenSourceSetup(ddpGenSourceSetup);
           	}
           	//deleting RATE_SETUP
           	TypedQuery<DdpRateSetup> query1 =  DdpRateSetup.findDdpRateSetupsByDdpRuleDetail(ddpRuleDetail);
           	List<DdpRateSetup> ddpRateSetups = query1.getResultList();
           	for(DdpRateSetup ddpRateSetup: ddpRateSetups)
           	{
           		ddpRateSetupService.deleteDdpRateSetup(ddpRateSetup);
           	}
            // Deleting Rule detail to the aedrule
            ddpRuleDetailService.deleteDdpRuleDetail(ddpRuleDetail);
        }
        // CommunicationSetup
//        DdpCommunicationSetup ddpCommSetup = ddpRuleDetails.get(0).getRdtCommunicationId();
//        String comprotoco = ddpCommSetup.getCmsProtocolSettingsId();
        // Delete CommunicationEmail
//        ddpCommEmailService.deleteDdpCommEmail(ddpCommEmailService.findDdpCommEmail(Integer.parseInt(comprotoco)));
        // Delete CommunicationSetup
//        ddpCommunicationSetupService.deleteDdpCommunicationSetup(ddpCommSetup);
        // Delete AED Rule before deleting the Communication setup
        DdpAedRule ddpAedRule = ddpAedRuleService.findDdpAedRule(aedRuleId);
        //Disable the Rule
        ddpAedRule.setAedStatus(1);
        logger.info("DdpAedRuleController.delete() - updating status of DdpAedRule whose AedRuleId:"+ddpAedRule.getAedRuleId());
        ddpAedRuleService.updateDdpAedRule(ddpAedRule);
        // Deleting Rule
        DdpRule ddpRule = ddpRuleService.findDdpRule(aedRuleId);
        //Disable the Rule
        ddpRule.setRulStatus(1);
        ddpRuleService.updateDdpRule(ddpRule);
        uiModel.asMap().clear();
       
        logger.info("DdpAedRuleController.delete() Executed Successfully.");
        return "redirect:/ddpaedrules/list/"+strCompany+"?createxml";
        
        
    }

    
    /**
     *
     * @param RuleId
     * @param uiModel
     * @return
     */
    @RequestMapping(value = {"/{RuleId}","/list/{RuleId}"}, produces = "text/html")
    public String show(@PathVariable("RuleId") Integer RuleId, Model uiModel) {
    	logger.info("DdpAedRuleController.show() Method Inlvoked.");
    	String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
        addDateTimeFormatPatterns(uiModel);
        List<DdpCompany> userCompanies = new ArrayList<DdpCompany>();
        List<RuleDao> ruleDaos = getRuleDetailForRuleIdShow(RuleId.toString());
        DdpRule ddpRule = ddpRuleService.findDdpRule(RuleId);
//        if(ddpRule.getRulStatus()!=1){
        	uiModel.addAttribute("ddpaedrule", ruleDaos);
            uiModel.addAttribute("itemId", RuleId);
            uiModel.addAttribute("itemName", ddpRule.getRulDescription());
            DdpAedRule aedRule = ddpAedRuleService.findDdpAedRule(RuleId);
            uiModel.addAttribute("isActive", aedRule.getAedStatus());
            DdpNotification ddpNotification = ddpAedRuleService.findDdpAedRule(RuleId).getAedNotificationId();
            uiModel.addAttribute("SLANotify", ddpNotification == null ? "" : ddpNotification.getNotSuccessEmailAddress());
            userCompanies = ruleUtil.getAccessibleCompaniesLst(strUserName, env.getProperty("rule.aed.firstchars"));
            List<DdpCompany> readonlycompanies = ruleUtil.getListOfReadOnlyAccessibleCompanies(strUserName, env.getProperty("rule.aed.firstchars"));
            int restrictQuickLinks = 0; 
            if(! ruleDaos.isEmpty())
            {
            	if(readonlycompanies.contains(ddpCompanyService.findDdpCompany(ruleDaos.get(0).getRdtCompanyCode())))
            		restrictQuickLinks = 1;
            }
            userCompanies.removeAll(readonlycompanies);
            uiModel.addAttribute("restrictQuickLinks",restrictQuickLinks);
            uiModel.addAttribute("accessToCreate",userCompanies.size());
//        }
        logger.info("DdpAedRuleController.show() Executed Successfully.");
        return "ddpaedrules/show";
    }

    /**
     *
     * @param uiModel
     * @param ddpAedRule
     */
    void populateEditForm(Model uiModel, DdpAedRule ddpAedRule, String userCompany,String formtype) {
    	
    	logger.info("DdpAedRuleController.populateEditForm(Model uiModel, DdpAedRule ddpAedRule, String userCompany) Method Inlvoked.");
    	uiModel.addAttribute("ddpAedRule", ddpAedRule);
        addDateTimeFormatPatterns(uiModel);
        List<DdpCompany> ddpCompanies = new ArrayList<DdpCompany>();
        String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
        ddpCompanies = ruleUtil.getAccessibleCompaniesLst(strUserName, env.getProperty("rule.aed.firstchars"));
        ddpCompanies.removeAll(ruleUtil.getListOfReadOnlyAccessibleCompanies(strUserName, env.getProperty("rule.aed.firstchars")));
        //sort companies
        Collections.sort(ddpCompanies,new Comparator<DdpCompany>() {
			@Override
			public int compare(DdpCompany company1, DdpCompany company2) {
				return company1.getComCompanyCode().compareTo(company2.getComCompanyCode());
			}	
		});
//        Map<String,DdpCompany> companyMap = new LinkedHashMap<String,DdpCompany>();
//        for(DdpCompany company:ddpCompanies)
//        	companyMap.put(company.getComCountryCode(), company);
//        ddpCompanies.clear();
//        ddpCompanies.addAll(companyMap.values());
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
        uiModel.addAttribute("ddpCommOptionsLkp", ruleUtil.getAllCommOptionsLkp());
        uiModel.addAttribute("ddpGenSourceLkp", ruleUtil.getAllGenSourceLkp());
        uiModel.addAttribute("ddpRatedLkp", ruleUtil.getAllRateLkp());
        uiModel.addAttribute("ddpbranches", lstBranch);
        uiModel.addAttribute("ddppartys", ruleUtil.getAllPartys());
        uiModel.addAttribute("ddpdoctypes", doctypeLst);
        logger.info("DdpAedRuleController.populateEditForm(Model uiModel, DdpAedRule ddpAedRule, String userCompany) Executed Successfully.");
    }

    /**
     *
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/branchbycountry", method = RequestMethod.POST, produces = "text/html")
    public String branchbyCountry(Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpAedRuleController.branchbyCountry(Model uiModel, HttpServletRequest httpServletRequest) Method Inlvoked.");
        // Get the company ID
        String companyID1 = httpServletRequest.getParameter("companyID");
        uiModel.addAttribute("rulewrapper", new RuleWrapper());
        // send company code in parameter to populate Branch for the company code
        populateEditForm(uiModel, new DdpAedRule(), companyID1.toString(),"branchbyCountry");
        logger.info("DdpAedRuleController.branchbyCountry(Model uiModel, HttpServletRequest httpServletRequest) Executed Successfully.");
        return "ddpaedrules/create";
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
	   	 if (res.trim().length() > 0)
	   		 res = res.substring(0, res.lastIndexOf(","));
		 return res;
	}
	
    /**
     * 
     * @param companyCode
     * @return
     */
    @RequestMapping(value = "/getBranches/{companyCode}", method = RequestMethod.GET, produces = "text/html")
    public @ResponseBody String getBranchByCompany(@PathVariable String companyCode) {
        // Get the company ID
    	 List<DdpBranch> branchs = ruleUtil.findBranchbyCompany(companyCode);
    	 String res = "";
    	 for(DdpBranch branch:branchs){
    		 res+=branch.getBrnBranchCode()+",";
    	 }
    	 if (res.trim().length() > 0)
    		 res = res.substring(0, res.lastIndexOf(","));
        return res;
    }

    //controller code for jqgrid
    @RequestMapping(value = { "listAedRules" }, headers = "Accept=application/json", method = RequestMethod.GET)
    @ResponseBody
    public String listJsonforGrid(@RequestParam(value = "_search", required = false) String search,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "filters", required = false) String filters,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "sortdir", required = false) String sortdir) throws JsonParseException, JsonMappingException, IOException {
    	logger.info("DdpAedRuleController.listJsonforGrid() Method Inlvoked.");
        Map<String,Object> map = new HashMap<String,Object>();
        ArrayList<Object> records = new ArrayList<Object>(50);
        List<DdpAedRule> result = null;
        List<AedRuleWrapper> aedRuleWrapperList = new ArrayList<AedRuleWrapper>();
        HashMap<Object,Object>[] rowdata;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        String[] compnies ;
        String userGroup = "";
        String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    	List<String> authoritiesLst = new ArrayList<String>();
    	Set<String> multiCountries = new HashSet<String>();
	  for(GrantedAuthority authority:authorities)
	  {
		  authoritiesLst.add(authority.getAuthority());
		  if(authority.getAuthority().toLowerCase().startsWith(env.getProperty("rule.aed.firstchars").toLowerCase()+"_") ||
				  authority.getAuthority().toLowerCase().startsWith(env.getProperty("role_sub_all").toLowerCase()+"_") )
		  {
			  String strCompany = authority.getAuthority().split("_")[1].toUpperCase().trim();
			  DdpCompany company = ddpCompanyService.findDdpCompany(strCompany);
			  if(company != null && company.getComStatus()==0 )
				  multiCountries.add(strCompany.toUpperCase()); 
		  }
		  if(authority.getAuthority().toLowerCase().startsWith(env.getProperty("Read_only_start").toLowerCase()))
		  {
			  if(authority.getAuthority().split("_")[2].equalsIgnoreCase(env.getProperty("read_only_region_sub")))
			  {
				  if(authority.getAuthority().split("_")[4].equalsIgnoreCase(env.getProperty("rule.aed.firstchars")) || 
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
				  if(authority.getAuthority().split("_")[3].equalsIgnoreCase(env.getProperty("rule.aed.firstchars")) || 
						  authority.getAuthority().split("_")[3].equalsIgnoreCase(env.getProperty("role_sub_all")) )
				  {
					  multiCountries.add(authority.getAuthority().toUpperCase().split("_")[2].trim()); 
				  }
			  }
		  }
	  }
	  if(authoritiesLst.contains(env.getProperty("Admin_Role")))
	  {
		  aedRuleWrapperList = getAllActiveAedRules("admin_role",null,null,null); 
	  }
	  else
	  {
		  //add Read only rules to list
		  
		  if(authoritiesLst.contains(env.getProperty("Region_Role")))
		  { 
			  String userRegion = ruleUtil.getUserRegion(strUserName);
			  aedRuleWrapperList = getAllActiveAedRules("Region_role",userRegion,null,null); 
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
			  aedRuleWrapperList.addAll(getAllActiveAedRules("multi",null,null,compnies));
		  }
		  else
		  {
			  String userCompany = ruleUtil.getUserCompany(strUserName);
			  compnies = new String[1];
			  compnies[0] = userCompany;
			  aedRuleWrapperList.addAll(getAllActiveAedRules("local",null,userCompany,null)); 
		  }
		  if(! aedRuleWrapperList.isEmpty())
		  {
			  Map<Integer,AedRuleWrapper> aedRuleWrapperMap = new LinkedHashMap<Integer,AedRuleWrapper>();
		         for(AedRuleWrapper aedRuleWrapper:aedRuleWrapperList)
		        	 aedRuleWrapperMap.put(aedRuleWrapper.getAedRuleId(), aedRuleWrapper);
		         aedRuleWrapperList.clear();
		         aedRuleWrapperList.addAll(aedRuleWrapperMap.values());
		  }
	  }
    	   
        rowdata = new HashMap[aedRuleWrapperList.size()];
        map.put("rows", aedRuleWrapperList);
        map.put("page",page);
        
        Gson gson = new Gson();
        String json = gson.toJson(map); 
        logger.info("DdpAedRuleController.listJsonforGrid() Executed Successfully.");
        return json;	
    }

    @RequestMapping(value = "/list",params = "mediator", method = RequestMethod.GET)
    public String creatlistJsonforGrid(){
    	return "redirect:/ddpaedrules/mediator";
    }
    
    //copy rules by grid
    @AuditLog(message="New Aed Rule Created with Existing Rule Details.")
    @Transactional
    @RequestMapping(value = "/copydata/{Id}",method = RequestMethod.GET, headers = "Accept=application/json")
    public String updateFromJsoninGrid(@PathVariable("Id") Integer Id) throws JsonParseException, JsonMappingException, IOException {
    	 logger.info("DdpAedRuleController.updateFromJsoninGrid() Method Invoked.");
    	 String strUserName	= SecurityContextHolder.getContext().getAuthentication().getName();
        //	System.out.println(json);
//        ObjectMapper mapper = new ObjectMapper();
        //AedJsontoInt aji = mapper.readValue(json, AedJsontoInt.class);
        //set date
    	currentDate = Calendar.getInstance();
        int aedRuleId = Id;
        //Add Rule
        DdpRule ddpRule = ddpRuleService.findDdpRule(aedRuleId);
        DdpRule newDRule = new DdpRule();
        String strRuleName = ddpRule.getRulName();
        strRuleName = strRuleName + "_copy";
        newDRule.setRulName(strRuleName);
        newDRule.setRulDescription(ddpRule.getRulDescription());
        newDRule.setRulStatus(ddpRule.getRulStatus());
        newDRule.setRulCreatedBy(strUserName);
        newDRule.setRulCreatedDate(currentDate);
        newDRule.setRulModifiedBy(strUserName);
        newDRule.setRulModifiedDate(currentDate);
        ddpRuleService.saveDdpRule(newDRule);
        //Get Rule Id
        Integer ruleId = newDRule.getRulId();
        
        TypedQuery<DdpRuleDetail> typedListOfDdpRuleDetail = DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(ddpRule);
        List<DdpRuleDetail> ruleDetail_ids = typedListOfDdpRuleDetail.getResultList();
        
        DdpRuleDetail ddpRuleDetail = new DdpRuleDetail();
        if (!ruleDetail_ids.isEmpty()) {
            ddpRuleDetail = ddpRuleDetailService.findDdpRuleDetail(ruleDetail_ids.get(0).getRdtId());
        }
        //Get the Communication id
        DdpCommunicationSetup comSetup = ddpRuleDetail.getRdtCommunicationId();
        //Get Communocation protocol id
        String comProtocolId = comSetup.getCmsProtocolSettingsId();
        //Get Comm Email Bean
        DdpCommEmail commEmail = ddpCommEmailService.findDdpCommEmail(Integer.parseInt(comProtocolId));
        DdpCommEmail newcommEmail = new DdpCommEmail();
        newcommEmail.setCemEmailFrom(commEmail.getCemEmailFrom());
        newcommEmail.setCemEmailTo(commEmail.getCemEmailTo());
        newcommEmail.setCemEmailCc(commEmail.getCemEmailCc());
        newcommEmail.setCemEmailSubject(commEmail.getCemEmailSubject());
        newcommEmail.setCemEmailBody(commEmail.getCemEmailBody());
        //Add the comm Email for new Rule
        ddpCommEmailService.saveDdpCommEmail(newcommEmail);
        //Get the new comm email id
        Integer commEmailId = newcommEmail.getCemEmailId();
        //Set new commEmail id in communication setup
        DdpCommunicationSetup newcomSetup = new DdpCommunicationSetup();
        newcomSetup.setCmsProtocolSettingsId(commEmailId.toString());
        newcomSetup.setCmsCommunicationProtocol(env.getProperty("rule.comm.prototocol"));
        newcomSetup.setCmsCreatedBy(strUserName);
        newcomSetup.setCmsCreatedDate(currentDate);
        newcomSetup.setCmsModifiedBy(strUserName);
        newcomSetup.setCmsModifiedDate(currentDate);
        newcomSetup.setCmsStatus(0);
        //Save the communication setup and get the id
        ddpCommunicationSetupService.saveDdpCommunicationSetup(newcomSetup);
        //Get the id communication setup id
        Integer commSetupId = newcomSetup.getCmsCommunicationId();
        //Add Aed Rule detail
        DdpAedRule aedRule = ddpAedRuleService.findDdpAedRule(aedRuleId);
        DdpAedRule newaedRule = new DdpAedRule();
        newaedRule.setAedIsPartyCheckRequired(aedRule.getAedIsPartyCheckRequired());
        newaedRule.setAedStatus(aedRule.getAedStatus());
        newaedRule.setAedCommunicationId(ddpCommunicationSetupService.findDdpCommunicationSetup(commSetupId));
        newaedRule.setAedCreatedBy(strUserName);
        newaedRule.setAedModifiedBy(strUserName);
        newaedRule.setAedCreatedDate(currentDate);
        newaedRule.setAedModifiedDate(currentDate);
        newaedRule.setAedActivationDate(currentDate);
        //Set the Rule id
        newaedRule.setAedRuleId(ruleId);
        //save aed rule
        ddpAedRuleService.saveDdpAedRule(newaedRule);
       
        //Add the Rule detail id
        for (DdpRuleDetail ruleDetail_id : ruleDetail_ids) {
            DdpRuleDetail ruleDetail = ddpRuleDetailService.findDdpRuleDetail(ruleDetail_id.getRdtId());
            //Set the new rule
            DdpRuleDetail newruleDetail = new DdpRuleDetail();
            newruleDetail.setRdtRuleId(ddpRuleService.findDdpRule(ruleId));
            //Set Communication setup id
            newruleDetail.setRdtCommunicationId(ddpCommunicationSetupService.findDdpCommunicationSetup(commSetupId));
            newruleDetail.setRdtBranch(ruleDetail.getRdtBranch());
//            newruleDetail.setRdtCommunicationId(ruleDetail.getRdtCommunicationId());
            newruleDetail.setRdtCompany(ruleDetail.getRdtCompany());
            newruleDetail.setRdtCreatedBy(strUserName);
            newruleDetail.setRdtCreatedDate(currentDate);
            newruleDetail.setRdtModifiedBy(strUserName);
            newruleDetail.setRdtModifiedDate(currentDate);
            newruleDetail.setRdtDocType(ruleDetail.getRdtDocType());
            newruleDetail.setRdtGenSystem(ruleDetail.getRdtGenSystem());
            newruleDetail.setRdtNotificationId(ruleDetail.getRdtNotificationId());
            newruleDetail.setRdtPartyId(ruleDetail.getRdtPartyId());
            newruleDetail.setRdtPartyCode(ruleDetail.getRdtPartyCode());
            newruleDetail.setRdtRuleType(ruleDetail.getRdtRuleType());
            newruleDetail.setRdtSchedulerId(ruleDetail.getRdtSchedulerId());
            newruleDetail.setRdtStatus(ruleDetail.getRdtStatus());
            newruleDetail.setRdtActivationDate(currentDate);
            //Save Rule Details
            ddpRuleDetailService.saveDdpRuleDetail(newruleDetail);
           
            //Get Communication option for the rule id
            TypedQuery<DdpCommOptionsSetup> ddpTypedCommOption = DdpCommOptionsSetup.findDdpCommOptionsSetupsByCopRdtId(ruleDetail_id);
            List<DdpCommOptionsSetup> comOptionSetupLst = ddpTypedCommOption.getResultList();
            for (DdpCommOptionsSetup comOptionSetup : comOptionSetupLst) {
                DdpCommOptionsSetup newcomOptionSetup = new DdpCommOptionsSetup();
                newcomOptionSetup.setCopRdtId(newruleDetail);
                newcomOptionSetup.setCopOption(comOptionSetup.getCopOption());
                newcomOptionSetup.setCopCreatedBy(strUserName);
                newcomOptionSetup.setCopCreatedDate(currentDate);
                newcomOptionSetup.setCopModifiedBy(strUserName);
                newcomOptionSetup.setCopModifiedDate(currentDate);
                newcomOptionSetup.setCopRdtId(ddpRuleDetailService.findDdpRuleDetail(newruleDetail.getRdtId()));
                //Save Communication Option Setup
                ddpCommOptionsSetupService.saveDdpCommOptionsSetup(newcomOptionSetup);
            }
            //Get Generate Source 
            TypedQuery<DdpGenSourceSetup> ddpTypedGenSource = DdpGenSourceSetup.findDdpGenSourceSetupsByGssRdtId(ruleDetail_id);
            List<DdpGenSourceSetup>	ddpGenSourceSetups = ddpTypedGenSource.getResultList();
            for(DdpGenSourceSetup ddpGenSourceSetup : ddpGenSourceSetups){
            	DdpGenSourceSetup newGenSourceSetupLst = new DdpGenSourceSetup();
                newGenSourceSetupLst.setGssRdtId(newruleDetail);
                newGenSourceSetupLst.setGssOption(ddpGenSourceSetup.getGssOption());
                newGenSourceSetupLst.setGssCreatedBy(strUserName);
                newGenSourceSetupLst.setGssCreatedDate(currentDate);
                newGenSourceSetupLst.setGssModifiedBy(strUserName);
                newGenSourceSetupLst.setGssModifiedDate(currentDate);
                //save ddpGeneratingSource
                ddpGenSourceSetupService.saveDdpGenSourceSetup(newGenSourceSetupLst);
            }  
            //Get Rate 
            TypedQuery<DdpRateSetup> ddpTypedRateSetup = DdpRateSetup.findDdpRateSetupsByDdpRuleDetail(ruleDetail_id);
            List<DdpRateSetup>	ddpRateSetups = ddpTypedRateSetup.getResultList();
            for(DdpRateSetup ddpRateSetup : ddpRateSetups){
            	DdpRateSetup newDdpRateSetuplst = new DdpRateSetup();
                newDdpRateSetuplst.setRtsRdtId(newruleDetail);
                newDdpRateSetuplst.setRtsOption(ddpRateSetup.getRtsOption());
                newDdpRateSetuplst.setRtsCreatedBy(strUserName);
                newDdpRateSetuplst.setRtsCreatedDate(currentDate);
                newDdpRateSetuplst.setRtsModifiedBy(strUserName);
                newDdpRateSetuplst.setRtsModifiedDate(currentDate);
                //Save Rated/Not Rated document
                ddpRateSetupService.saveDdpRateSetup(newDdpRateSetuplst);
            }
        }
       
        /*******************************************************************************/
        return "redirect:/ddpaedrules/list/"+newaedRule.getAedRuleId()+"/form";
        //Create XML file and move to servers.
        //return "redirect:/ddpaedrules/"+ruleId+"/"+ruleDetail_ids.get(0).getRdtCompany().getComCompanyCode()+"?createxml";
    }

    /**
     * 
     * @param aedRuleId
     * @param companyCode
     * @return
     */
    @RequestMapping(value = "/{aedRuleId}/{companyCode}", params = "createxml", produces = "text/html")
    public String createXML(@PathVariable("aedRuleId") Integer aedRuleId,@PathVariable("companyCode") String companyCode){
    	
    	 logger.info("DdpAedRuleController.createXML() Method Invoked.");
         String crUser = SecurityContextHolder.getContext().getAuthentication().getName();
         DdpUser user = ruleUtil.getDdpUser(crUser);
         DdpCompany ddpCompany = ddpCompanyService.findDdpCompany(companyCode);
    	 /*******************************************************************************/
        //Create XML file and move to servers by Simple Trigger.
         
    	try
    	{
    		Scheduler scheduler = applicationProperties.getQuartzScheduler("AED_XML_"+companyCode+"_"+(new Date()));
    		MethodInvokingJobDetailFactoryBean jobDetailFactory = new MethodInvokingJobDetailFactoryBean();
    		jobDetailFactory.setTargetObject(ruleUtil);
    		jobDetailFactory.setTargetMethod("createXml");
    		jobDetailFactory.setArguments(new Object[]{user,ddpCompany,scheduler});
    		jobDetailFactory.setName("CreateXML:"+aedRuleId+"_"+(new Date()));
    		jobDetailFactory.afterPropertiesSet();
    		JobDetail jobDetail = (JobDetail)jobDetailFactory.getObject();

	        
	         
	         SimpleTriggerImpl trigger = new SimpleTriggerImpl();
		     trigger.setName("CreateXML:"+aedRuleId+"_"+(new Date()));
		     trigger.setGroup("CreateXML-Group:"+aedRuleId+"_"+(new Date()));
		     trigger.setStartTime(new Date());
		     trigger.setEndTime(null);
		     trigger.setRepeatCount(0);
		     trigger.setRepeatInterval(10000L);
	        
	         
	         scheduler.scheduleJob(jobDetail, trigger);
	         scheduler.start();
	     	 
	     	
//    		MethodInvokingJobDetailFactoryBean jobDetailFactory = new MethodInvokingJobDetailFactoryBean();
//    		jobDetailFactory.setTargetObject(ruleUtil);
//    		jobDetailFactory.setTargetMethod("createXml");
//    		jobDetailFactory.setArguments(new Object[]{user,ddpCompany});
//    		jobDetailFactory.setName("CreateXml");
//    		jobDetailFactory.afterPropertiesSet();
//    		JobDetail jobDetail = (JobDetail)jobDetailFactory.getObject();
//    		
//    		
//    		SimpleTriggerFactoryBean triggerBean = new SimpleTriggerFactoryBean();
//    		
//    		triggerBean.setJobDetail(jobDetail);
////    		triggerBean.getObject().setJobName(jobDetail.getName());
//    		triggerBean.setRepeatInterval(10*1000);
//    		triggerBean.setRepeatCount(0);
//    		triggerBean.setStartDelay(0);
//    		triggerBean.setStartTime(new Date());
//    		triggerBean.setBeanName("CreateXML:"+aedRuleId);
//    		triggerBean.setName("CreateXML:"+aedRuleId);
//    		
//    		SimpleTriggerImpl simpleTriggerImpl = new SimpleTriggerImpl();
//    		simpleTriggerImpl.setRepeatInterval(10*1000);
//    		simpleTriggerImpl.setRepeatCount(0);
//    		//simpleTriggerImpl.setStartDelay(0);
//    		simpleTriggerImpl.setStartTime(new Date());
//    		simpleTriggerImpl.setJobName("CreateXML");
//    		simpleTriggerImpl.setEndTime(null);
//    		simpleTriggerImpl.setGroup("CreateXML-Group:"+aedRuleId);
//    		simpleTriggerImpl.setName("CreateXML:"+aedRuleId);
//    		
    		    		
//    		SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
//    		factoryBean.setJobDetails(jobDetail);
//    		factoryBean.setTriggers( simpleTriggerImpl);
//    		factoryBean.afterPropertiesSet();
//    		
//    		factoryBean.start();
    		
    	}catch(Exception schex){
    		logger.error("DdpAedRuleController.createXML() : Exception in Thread",schex);
    	}
       
        /*******************************************************************************/
        //return "redirect:/ddpaedrules/" + encodeUrlPathSegment(ruleWrapper.getDdpAedRule().getAedRuleId().toString(), httpServletRequest);
        logger.info("DdpAedRuleController.createXML() Executed Successfully.");
        
        return "redirect:/ddpaedrules/list/list/"+aedRuleId;
    }
    
    
    //delete rules by grid
    @Transactional
    @RequestMapping(value = "grid/{aedRuleId}", method = RequestMethod.DELETE,produces = "text/html")
    public String deleteFromJsongrid(@PathVariable("aedRuleId") String ruleId) throws JsonParseException, JsonMappingException, IOException {
    	Integer aedRuleId = new ObjectMapper().readValue(ruleId,Integer.class);
    	 logger.info("DdpAedRuleController.deleteFromJsongrid() Method Invoked.");
         // Prepare RuleDetail
         List<DdpRuleDetail> ddpRuleDetails = new ArrayList<DdpRuleDetail>();
         List<DdpRuleDetail> ruleDetails = ddpRuleDetailService.findAllDdpRuleDetails();
         for (DdpRuleDetail ruleDetail : ruleDetails) {
             if (ruleDetail.getRdtRuleId().getRulId().equals(aedRuleId)) {
                 ddpRuleDetails.add(ruleDetail);
                 // Deleting option setup table
                 Integer rdtId = ruleDetail.getRdtId();
                 // Delete Communication Option for ddp detail id
                 TypedQuery<DdpCommOptionsSetup> lst1 = DdpCommOptionsSetup.findDdpCommOptionsSetupsByCopRdtId(ddpRuleDetailService.findDdpRuleDetail(rdtId));
                 List<DdpCommOptionsSetup> copIds = lst1.getResultList();
                 for (DdpCommOptionsSetup strOptionId : copIds) {
                 	 logger.info("DdpAedRuleController.deleteFromJsongrid() : Deleting DdpCommOptionsSetup of CopId:"+strOptionId.getCopId());
                     ddpCommOptionsSetupService.deleteDdpCommOptionsSetup(strOptionId);
                 }
             }
         }
         String company = ddpRuleDetails.get(0).getRdtCompany().getComCompanyName();
         // Deleting Detail
         for (DdpRuleDetail ddpRuleDetail : ddpRuleDetails) {
         	 logger.info("DdpAedRuleController.deleteFromJsongrid() : Deleting DdpRuleDetail of RdtId:"+ddpRuleDetail.getRdtId());
         	 //deleting GEN_SOURCE_SETUP
          	TypedQuery<DdpGenSourceSetup> query = DdpGenSourceSetup.findDdpGenSourceSetupsByGssRdtId(ddpRuleDetail);
          	List<DdpGenSourceSetup> ddpGenSourceSetups = query.getResultList();
          	for(DdpGenSourceSetup ddpGenSourceSetup:ddpGenSourceSetups)
          	{
          		ddpGenSourceSetupService.deleteDdpGenSourceSetup(ddpGenSourceSetup);
          	}
          	//deleting RATE_SETUP
          	TypedQuery<DdpRateSetup> query1 =  DdpRateSetup.findDdpRateSetupsByDdpRuleDetail(ddpRuleDetail);
          	List<DdpRateSetup> ddpRateSetups = query1.getResultList();
          	for(DdpRateSetup ddpRateSetup: ddpRateSetups)
          	{
          		ddpRateSetupService.deleteDdpRateSetup(ddpRateSetup);
          	}
             // Deleting Rule detail to the aedrule
             ddpRuleDetailService.deleteDdpRuleDetail(ddpRuleDetail);
         }
         // CommunicationSetup
//         DdpCommunicationSetup ddpCommSetup = ddpRuleDetails.get(0).getRdtCommunicationId();
//         String comprotoco = ddpCommSetup.getCmsProtocolSettingsId();
         // Delete CommunicationEmail
//         ddpCommEmailService.deleteDdpCommEmail(ddpCommEmailService.findDdpCommEmail(Integer.parseInt(comprotoco)));
         // Delete CommunicationSetup
//         ddpCommunicationSetupService.deleteDdpCommunicationSetup(ddpCommSetup);
         // Delete AED Rule before deleting the Communication setup
         DdpAedRule ddpAedRule = ddpAedRuleService.findDdpAedRule(aedRuleId);
         //Disable the Rule
         ddpAedRule.setAedStatus(1);
         logger.info("DdpAedRuleController.deleteFromJsongrid() - updating status of DdpAedRule whose AedRuleId:"+ddpAedRule.getAedRuleId());
         ddpAedRuleService.updateDdpAedRule(ddpAedRule);
         // Deleting Rule
         DdpRule ddpRule = ddpRuleService.findDdpRule(aedRuleId);
         //Disable the Rule
         ddpRule.setRulStatus(1);
         ddpRuleService.updateDdpRule(ddpRule);
         logger.info("DdpAedRuleController.deleteFromJsongrid() Executed Successfully.");
         return "redirect:/ddpaedrules/list/"+company+"?createxml";
    }
    
    @RequestMapping(value = {"/{companyCode}","/list/{companyCode}"}, params = "createxml", produces = "text/html")
    public String createXMAfterDelete(@PathVariable("companyCode") String companyCode){
    	
    	 logger.info("DdpAedRuleController.createXMAfterDelete() Method Invoked.");
    	 /*******************************************************************************/
         String crUser = SecurityContextHolder.getContext().getAuthentication().getName();
         DdpUser user = ruleUtil.getDdpUser(crUser);
         DdpCompany ddpCompany = ddpCompanyService.findDdpCompany(companyCode);
    	 /*******************************************************************************/
        //Create XML file and move to servers by Simple Trigger.
    	try
    	{
    		Scheduler scheduler = applicationProperties.getQuartzScheduler("AED_XML_"+companyCode+"_"+(new Date()));
    		MethodInvokingJobDetailFactoryBean jobDetailFactory = new MethodInvokingJobDetailFactoryBean();
    		jobDetailFactory.setTargetObject(ruleUtil);
    		jobDetailFactory.setTargetMethod("createXml");
    		jobDetailFactory.setArguments(new Object[]{user,ddpCompany,scheduler});
    		jobDetailFactory.setName("CreateXmlAfterDelete"+companyCode+"_"+(new Date()));
    		jobDetailFactory.afterPropertiesSet();
    		JobDetail jobDetail= (JobDetail)jobDetailFactory.getObject();
	         
	         SimpleTriggerImpl trigger = new SimpleTriggerImpl();
		     trigger.setName("DeleteXML:"+companyCode+"_"+(new Date()));
		     trigger.setGroup("DeleteXML-Group:"+companyCode+"_"+(new Date()));
		     trigger.setStartTime(new Date());
		     trigger.setEndTime(null);
		     trigger.setRepeatCount(0);
		     trigger.setRepeatInterval(10000L);
	        
	         scheduler.scheduleJob(jobDetail, trigger);
	     	 scheduler.start();
	     	 
    		
    		
//    		SimpleTriggerFactoryBean triggerBean = new SimpleTriggerFactoryBean();
//    		triggerBean.setJobDetail(jobDetail);
////    		triggerBean.getObject().setJobName(jobDetail.getName());
//    		triggerBean.setRepeatInterval(10*1000);
//    		triggerBean.setRepeatCount(0);
//    		triggerBean.setStartDelay(0);
//    		triggerBean.setStartTime(new Date());
//    		triggerBean.setBeanName("CreateXmlAfterDelete");
//    		triggerBean.setName("CreateXmlAfterDelete");
//    		
//    		SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
//    		factoryBean.setJobDetails(new JobDetail[]{ (JobDetail)jobDetailFactory.getObject()});
//    		factoryBean.setTriggers(new Trigger[]{ triggerBean.getObject() });
//    		factoryBean.afterPropertiesSet();
//    		
//    		factoryBean.start();
    		
    	}catch(Exception schex){
    		logger.error("DdpAedRuleController.createXMLAfterDelete() : Exception in Thread",schex);
    	}
        /*******************************************************************************/
        //return "redirect:/ddpaedrules/" + encodeUrlPathSegment(ruleWrapper.getDdpAedRule().getAedRuleId().toString(), httpServletRequest);
        logger.info("DdpAedRuleController.createXMAfterDelete() Executed Successfully.");
        
        return "redirect:/ddpaedrules/list"+"/mediator";
    }

    /**
     * 
     * @param ddpCompany
     * @return
     */
    public List<DdpAedRule> getRuleIdByCompany(String... ddpCompany) {
    	String cmpLst = "";
    	
    	String col= "drd.RDT_COMPANY=";
    	for(String company:ddpCompany){
    		cmpLst  +=col+"'"+company+"' or ";
    	}
    	cmpLst = cmpLst.substring(0, cmpLst.lastIndexOf(" or"));
        List<DdpAedRule> ddpaedrules = this.jdbcTemplate.query("select distinct dar.AED_RULE_ID from dbo.DDP_AED_RULE dar,dbo.DDP_RULE_DETAIL drd where ("+cmpLst+") and dar.AED_RULE_ID=drd.RDT_RULE_ID", new Object[] {  }, new RowMapper<DdpAedRule>() {

            public DdpAedRule mapRow(ResultSet rs, int rowNum) throws SQLException {
            	
            	DdpAedRule ddpaedrule = ddpAedRuleService.findDdpAedRule(rs.getInt("aed_rule_id"));
                return ddpaedrule;
            }
        });
        return ddpaedrules;
    }


    /**
     * This query is written to return only CommOption Id
     * @param ruleDetailId
     * @return
     */
    public List<String> getCommunicationOptionId(String ruleDetailId) {
        List<String> docTypePartyCode = this.jdbcTemplate.query(Constant.SELECT_OPTION, new Object[] { ruleDetailId }, new RowMapper<String>() {

            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String strOption = rs.getString("COP_ID");
                return strOption;
            }
        });
        return docTypePartyCode;
    }

//    /**
//     *
//     * @param company
//     * @return
//     */
//    public List<DdpBranch> findBranchbyComapny(String company) {
//        List<DdpBranch> ddpBranchName = this.jdbcTemplate.query(Constant.SELECT_BRANCH_BY_COMPANY, new Object[] { company }, new RowMapper<DdpBranch>() {
//
//            public DdpBranch mapRow(ResultSet rs, int rowNum) throws SQLException {
//                DdpBranch ddpBranch = new DdpBranch();
//                ddpBranch.setBrnBranchCode(rs.getString("brn_branch_code"));
//                ddpBranch.setBrnBranchName(rs.getString("brn_branch_name"));
//                ddpBranch.setBrnCompnayCode(rs.getString("BRN_COMPNAY_CODE"));
//                ddpBranch.setBrnStatus(rs.getInt("brn_status"));
//                return ddpBranch;
//            }
//        });
//        return ddpBranchName;
//    }

//    public DdpUser getDdpUser(String crUser)
//    {
//    	String usrId = this.jdbcTemplate.queryForObject(Constant.GETUSERBYLOGINID,String.class,crUser);
//    	 DdpUser user = ddpUserService.findDdpUser(Integer.parseInt(usrId));
//    	 return user;
//    }
    /**
     *
     * @param userLogin_Id
     * @return User Company
     */
//    public String getUserCompany(String strUserName) {
//        return jdbcTemplate.queryForObject(Constant.USERCOMPANY, String.class, strUserName);
//    }
    
    /**
     * @param userLoginId
     * @return User Region
     */
//    public String getUserRegion(String strUserName) {
//        return jdbcTemplate.queryForObject(Constant.USERREGION, String.class, strUserName);
//    }
    
    /**
     * @param userLoginId
     * @return usergroup 
     */
    public List<String> getUserGroup(String strUserName) {
    	List<String> userGroups =  jdbcTemplate.query(Constant.USERGROUP, new Object[] { strUserName }, new RowMapper<String>() {
    		 @Override
             public String mapRow(ResultSet rs, int rowNum) throws SQLException {
    			 String struserGroup = rs.getString("GRP_NAME");
                 return struserGroup;
             }
         });
         return userGroups;
    }
    
    /**
     * 
     * @param region
     * @return
     */
//    public List<DdpCompany> getCompaniesByRegionJdbcTemplate(String region) {
//        List<DdpCompany> ddpCompanies = this.jdbcTemplate.query(Constant.COMPANIESBYREGION, new Object[] { region }, new RowMapper<DdpCompany>() {
//
//            @Override
//            public DdpCompany mapRow(ResultSet rs, int rowNum) throws SQLException {
//            	DdpCompany ddpCompany = new DdpCompany();
//            	ddpCompany.setComCompanyCode(rs.getString("COM_COMPANY_CODE"));
//               	ddpCompany.setComCompanyName(rs.getString("COM_COMPANY_NAME"));
//            	ddpCompany.setComCountryCode(rs.getString("COM_COUNTRY_CODE"));
//            	ddpCompany.setComCountryName(rs.getString("COM_COUNTRY_NAME"));
//            	ddpCompany.setComRegion(rs.getString("COM_REGION"));
//            	return ddpCompany;
//            }
//        });
//        return ddpCompanies;
//    }
    /**
     *
     * @param ruleId
     * @return
     */
    public List<RuleDao> getRuleDetailByRuleIdJdbcTemplate(String ruleId) {
        List<RuleDao> lstRuleDao = this.jdbcTemplate.query(Constant.SELECT_RULEDETAILFORRULEID, new Object[] { ruleId }, new RowMapper<RuleDao>() {

            @Override
            public RuleDao mapRow(ResultSet rs, int rowNum) throws SQLException {
                RuleDao ruleDao = new RuleDao();
                ruleDao.setRdtId(rs.getInt("RDT_ID"));
                ruleDao.setRdtCompanyCode(rs.getString("RDT_COMPANY"));
                ruleDao.setRdtBranch(rs.getString("RDT_BRANCH"));
                ruleDao.setRdtPartyCode(rs.getString("RDT_PARTY_CODE"));
                ruleDao.setRdtDocType(rs.getString("RDT_DOC_TYPE"));
                ruleDao.setCemEmailFrom(rs.getString("CEM_EMAIL_FROM"));
                ruleDao.setCemEmailTo(rs.getString("CEM_EMAIL_TO"));
                // ruleDao.setCemSubject(rs.getString("CEM_EMAIL_SUBJECT"));
                ruleDao.setCemBody(rs.getString("CEM_EMAIL_BODY"));
                ruleDao.setCemEmailTo(rs.getString("CEM_EMAIL_CC"));
                // ruleDao.setCopOption(rs.getString("COP_OPTION"));
                return ruleDao;
            }
        });
        return lstRuleDao;
    }
    /**
     * 
     * @param partyId
     * @param companyCode
     * @param partyCodes
     * @return
     */
    public List<Integer> getRuleIdsByPartyCode(String partyId,String companyCode,LinkedList<Object> docTypes,LinkedList<Object> partyCodes,List<String> brnList,Integer existingRuleID)
    {
    	String conditionParty = "";
    	for(int in=0;in<partyCodes.size();in++)
    	{
    		conditionParty +="(RDT_PARTY_CODE='"+partyCodes.get(in)+"' AND RDT_DOC_TYPE='"+docTypes.get(in)+"') or ";
    	}
    	conditionParty = conditionParty.substring(0, conditionParty.lastIndexOf(" or"));
    	
    	String conditionBranch = "";
    	for(int indexI=0;indexI<brnList.size();indexI++)
    	{
    		conditionBranch +="RDT_BRANCH='"+brnList.get(indexI)+"' or ";
    	}
    	conditionBranch = conditionBranch.substring(0, conditionBranch.lastIndexOf(" or"));
    	String query = "SELECT RDT_RULE_ID FROM DDP_RULE_DETAIL WHERE RDT_COMPANY= '"+companyCode+"' AND RDT_STATUS=0 AND RDT_PARTY_ID = '"+partyId+"' AND ("+conditionParty+") AND ("+conditionBranch+") AND RDT_RULE_TYPE='AED_RULE'";
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
     * @param strCompanyCode
     * @return
     */
    public List<RuleDao> getRuleDetailForCompanyCode(String strCompanyCode){
    	logger.info("DdpAedRuleController.getRuleDetailForCompanyCode(String strCompanyCode) Method Invoked.");
    	List<RuleDao> ruleDaoLst = new ArrayList<RuleDao>();
        TypedQuery<DdpRuleDetail> typRuleDetails = DdpRuleDetail.findDdpRuleDetailsByRdtCompany(ddpCompanyService.findDdpCompany(strCompanyCode));
        List<DdpRuleDetail> lstRuleDetail = ruleUtil.getRuleDetailsByCompany(strCompanyCode, "AED_RULE");
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
//                       else if("Emailing".equalsIgnoreCase(commOptionsSetup.getCopOption())){
//                             emailing = "Y";
//                       }
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
     * @param companyId
     * @return
     */
    public List<RuleDao> getAEDByRuleIdJdbcTemplateXMl(String companyId) {
    	String t = Constant.SELECTEDRULEDETAILBYIDVIEW;
    	System.out.println(t);
    	
        List<RuleDao> lstRuleDao = this.jdbcTemplate.query(Constant.SELECTEDRULEDETAILBYIDVIEW, new Object[] { companyId, companyId, companyId, companyId }, new RowMapper<RuleDao>() {
        	
            @Override
            public RuleDao mapRow(ResultSet rs, int rowNum) throws SQLException {
            	String qry = Constant.SELECTEDRULEDETAILBYIDVIEW;
            	
            	System.out.println(qry);
            	
                RuleDao ruleDao = new RuleDao();
                ruleDao.setRdtId(rs.getInt("RDT_ID"));
                ruleDao.setRdtCompanyCode(rs.getString("RDT_COMPANY"));
                ruleDao.setRdtBranch(rs.getString("RDT_BRANCH"));
                ruleDao.setRdtPartyCode(rs.getString("RDT_PARTY_CODE"));
                ruleDao.setRdtDocType(rs.getString("RDT_DOC_TYPE"));
                ruleDao.setCemEmailFrom(rs.getString("CEM_EMAIL_FROM"));
                ruleDao.setCemEmailTo(rs.getString("CEM_EMAIL_TO"));
                ruleDao.setCemEmailCc(rs.getString("CEM_EMAIL_CC"));
                // ruleDao.setCemSubject(rs.getString("CEM_EMAIL_SUBJECT"));
                ruleDao.setCemBody(rs.getString("CEM_EMAIL_BODY"));
                ruleDao.setCopEmail(rs.getString("EMAIL"));
                ruleDao.setCopPrint(rs.getString("PRINTING"));
                ruleDao.setRdtPartyId(rs.getString("RDT_PARTY_ID"));
                ruleDao.setRid(rs.getInt("RUL_ID"));
                return ruleDao;
            }
        });
        return lstRuleDao;
    }
    /**
     * 
     * @param ruleId
     * @return
     */
    public List<String> getBranchesByRuleId(int ruleId){
   	 List<String> brachesLst = this.jdbcTemplate.query(Constant.BRANCHESBYRULEID1,new Object[]{ruleId}, new RowMapper<String>(){
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
     * @param ruleId
     * @return
     */
    private List<RuleDao> getAEDByRuleIdJdbcTemplateShow(String ruleId) {
    	  List<RuleDao> lstRuleDao = this.jdbcTemplate.query(Constant.SELECTEDRULEDETAILBYID1, new Object[] { ruleId, ruleId, ruleId, ruleId }, new RowMapper<RuleDao>() {

              @Override
              public RuleDao mapRow(ResultSet rs, int rowNum) throws SQLException {
                  RuleDao ruleDao = new RuleDao();
                  ruleDao.setRdtId(rs.getInt("RDT_ID"));
                  ruleDao.setRdtCompanyCode(rs.getString("RDT_COMPANY"));
                  ruleDao.setRdtBranch(rs.getString("RDT_BRANCH"));
                  ruleDao.setRdtPartyCode(rs.getString("RDT_PARTY_CODE"));
                  ruleDao.setRdtDocType(rs.getString("RDT_DOC_TYPE"));
                  ruleDao.setCemEmailFrom(rs.getString("CEM_EMAIL_FROM"));
                  ruleDao.setCemEmailTo(rs.getString("CEM_EMAIL_TO"));
                  ruleDao.setCemEmailCc(rs.getString("CEM_EMAIL_CC"));
                  // ruleDao.setCemSubject(rs.getString("CEM_EMAIL_SUBJECT"));
                  ruleDao.setCemBody(rs.getString("CEM_EMAIL_BODY"));
                  ruleDao.setCopEmail(rs.getString("EMAIL"));
                  ruleDao.setCopPrint(rs.getString("PRINTING"));
                  ruleDao.setRid(rs.getInt("RUL_ID"));
                  return ruleDao;
              }
          });
          return lstRuleDao;
    }
    
    /**
     * 
     * @param ruleId
     * @return
     */
     public List<RuleDao> getRuleDetailForRuleIdShow(String ruleId){
        List<RuleDao> ruleDaoLst = new ArrayList<RuleDao>();
        HashMap<String,RuleDao> ruleDaoMap = new HashMap<String,RuleDao>();
        RuleDao ruleDao = null;
        TypedQuery<DdpRuleDetail> typRuleDetail =  DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(ddpRuleService.findDdpRule(Integer.parseInt(ruleId)));
        List<DdpRuleDetail> lstRuleDetail = typRuleDetail.getResultList();
        
        List<String> brnLst = getBranchesByRuleId(Integer.parseInt(ruleId));
        String strBrnLst = "";
        for(String strBranch:brnLst){
              strBrnLst+=strBranch+",";
        }
        strBrnLst = strBrnLst.substring(0,strBrnLst.lastIndexOf(","));
        //counting distinct DocTypes and PartyCodes

        int ddpRuleDetail = lstRuleDetail.size()/brnLst.size();

        for(int i=0; i<ddpRuleDetail;i++){
 	         ruleDao = new RuleDao();
//           String emailing = "Emailing"; 
          	 TypedQuery<DdpCommOptionsSetup> typDdpCommOption =  DdpCommOptionsSetup.findDdpCommOptionsSetupsByCopRdtId(lstRuleDetail.get(i));
    	         List<DdpCommOptionsSetup> commOptions = typDdpCommOption.getResultList();
    	         for(DdpCommOptionsSetup commOptionsSetup : commOptions){
                     if("Printing".equalsIgnoreCase(commOptionsSetup.getCopOption())){
                     	ruleDao.setCopPrint(commOptionsSetup.getCopOption());
                     }
//                     else if("Not Applicable".equalsIgnoreCase(commOptionsSetup.getCopOption())){
//                     	ruleDao.setCopPrint(commOptionsSetup.getCopOption());
//                     }
    	         }
//	         ruleDao.setCopEmail(emailing);
 	         //Generated source
 	         TypedQuery<DdpGenSourceSetup> typedDdpGenSource = DdpGenSourceSetup.findDdpGenSourceSetupsByGssRdtId(lstRuleDetail.get(i));
 	         List<DdpGenSourceSetup> genSourceSetup = typedDdpGenSource.getResultList();
 	         ruleDao.setGenSource(ruleUtil.findGenSourceDescriptionByOption(genSourceSetup.get(0).getGssOption()));
 	         
 	         //Rated option
 	         TypedQuery<DdpRateSetup> typedDdpRateSetup = DdpRateSetup.findDdpRateSetupsByDdpRuleDetail(lstRuleDetail.get(i));
 	         List<DdpRateSetup> rateSetup = typedDdpRateSetup.getResultList();
 	         ruleDao.setStrRate(ruleUtil.findRateDescriptionByOption(rateSetup.get(0).getRtsOption()));
 	         //Comm Email 
 	         DdpCommEmail commEmail = new DdpCommEmail();
              commEmail = ddpCommEmailService.findDdpCommEmail(Integer.parseInt(lstRuleDetail.get(i).getRdtCommunicationId().getCmsProtocolSettingsId()));
              ruleDao.setCemEmailFrom((commEmail == null) ? "" : commEmail.getCemEmailFrom());
              ruleDao.setCemEmailTo((commEmail == null) ? "" :commEmail.getCemEmailTo());
              ruleDao.setCemEmailCc((commEmail == null) ? "" :commEmail.getCemEmailCc());
              ruleDao.setCemSubject((commEmail == null) ? "" :commEmail.getCemEmailSubject());
              ruleDao.setCemBody((commEmail == null) ? "" :commEmail.getCemEmailBody());
 	         
              ruleDao.setRdtId(lstRuleDetail.get(i).getRdtId());
              ruleDao.setRdtCompanyCode(lstRuleDetail.get(i).getRdtCompany().getComCompanyCode());
              ruleDao.setRdtBranch(strBrnLst);
              ruleDao.setRdtPartyCode(lstRuleDetail.get(i).getRdtPartyCode().getPtyPartyCode());
              ruleDao.setRdtDocType(lstRuleDetail.get(i).getRdtDocType().getDtyDocTypeCode());
              ruleDao.setRdtPartyId(lstRuleDetail.get(i).getRdtPartyId());
              ruleDao.setRdtPartyName(lstRuleDetail.get(i).getRdtPartyCode().getPtyPartyName());
              ruleDao.setRdtSlaFreq((lstRuleDetail.get(i).getRdtSlaFreq() == null) ? "" : lstRuleDetail.get(i).getRdtSlaFreq());
              ruleDao.setRdtSlaMin((lstRuleDetail.get(i).getRdtSlaMin() == null) ? "0" : lstRuleDetail.get(i).getRdtSlaMin());
              ruleDao.setRdtSlaMax((lstRuleDetail.get(i).getRdtSlaMax() == null) ? "0" : lstRuleDetail.get(i).getRdtSlaMax());
              int ddpRuleId  = lstRuleDetail.get(i).getRdtRuleId().getRulId();
              ruleDao.setRid(ddpRuleId);
             //Add to the List<RuleDao>
//              ruleDaoMap.put(lstRuleDetail.get(i).getRdtDocType().getDtyDocTypeCode(), ruleDao) ;
              ruleDaoLst.add(ruleDao);
        }
//        if(!ruleDaoMap.isEmpty()||null!=ruleDaoMap){
// 	       for(Map.Entry<String, RuleDao> ruleEntry : ruleDaoMap.entrySet()){
// 	    	   ruleDao = new RuleDao();
// 	    	   ruleDao= ruleEntry.getValue();
// 	    	   ruleDaoLst.add(ruleDao);
// 	       }
//        }
        return ruleDaoLst;
    }
	//Added to replace the query for update screen
    /**
     * 
     * @param strRuleId
     * @return
     */
    public List<RuleDao> getRuleDetailForRuleId(String strRulId,int rowCount){
        List<RuleDao> ruleDaoLst = new ArrayList<RuleDao>();
        RuleDao ruleDao = null;
        TypedQuery<DdpRuleDetail> typRuleDetail =  DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(ddpRuleService.findDdpRule(Integer.parseInt(strRulId)));
        List<DdpRuleDetail> lstRuleDetail = typRuleDetail.getResultList();
        for(DdpRuleDetail ddpRuleDetail : lstRuleDetail){
	         ruleDao = new RuleDao();
	         TypedQuery<DdpCommOptionsSetup> typDdpCommOption =  DdpCommOptionsSetup.findDdpCommOptionsSetupsByCopRdtId(ddpRuleDetail);
	         List<DdpCommOptionsSetup> commOptions = typDdpCommOption.getResultList();
	         for(DdpCommOptionsSetup commOptionsSetup : commOptions){
                  if("Printing".equalsIgnoreCase(commOptionsSetup.getCopOption())){
                	  ruleDao.setCopPrint(commOptionsSetup.getCopOption());
                  }else if("Emailing".equalsIgnoreCase(commOptionsSetup.getCopOption())){
                	  ruleDao.setCopEmail(commOptionsSetup.getCopOption());
                  }
	         }
	         TypedQuery<DdpGenSourceSetup> typedDdpGenSource = DdpGenSourceSetup.findDdpGenSourceSetupsByGssRdtId(ddpRuleDetail);
	         List<DdpGenSourceSetup> genSourceSetup = typedDdpGenSource.getResultList();
	         ruleDao.setGenSource(genSourceSetup.get(0).getGssOption());
	         TypedQuery<DdpRateSetup> typedDdpRateSetup = DdpRateSetup.findDdpRateSetupsByDdpRuleDetail(ddpRuleDetail);
	         List<DdpRateSetup> rateSetup = typedDdpRateSetup.getResultList();
	         ruleDao.setStrRate(rateSetup.get(0).getRtsOption());
             ruleDao.setRdtId(ddpRuleDetail.getRdtId());
             ruleDao.setRdtCompanyCode(ddpRuleDetail.getRdtCompany().getComCompanyCode());
             ruleDao.setRdtBranch(ddpRuleDetail.getRdtBranch().getBrnBranchCode());
             ruleDao.setRdtPartyCode(ddpRuleDetail.getRdtPartyCode().getPtyPartyCode());
             ruleDao.setRdtDocType(ddpRuleDetail.getRdtDocType().getDtyDocTypeCode());
             ruleDao.setRdtPartyId(ddpRuleDetail.getRdtPartyId());
             ruleDao.setRdtSlaFreq((ddpRuleDetail.getRdtSlaFreq() == null) ? "" : ddpRuleDetail.getRdtSlaFreq());
             ruleDao.setRdtSlaMin((ddpRuleDetail.getRdtSlaMin() == null) ? "0" : ddpRuleDetail.getRdtSlaMin());
             ruleDao.setRdtSlaMax((ddpRuleDetail.getRdtSlaMax() == null) ? "0" : ddpRuleDetail.getRdtSlaMax());
             int ddpRuleId  = ddpRuleDetail.getRdtRuleId().getRulId();
             ruleDao.setRid(ddpRuleId);
             //Add to the List<RuleDao>
             ruleDaoLst.add(ruleDao);
        }
        return ruleDaoLst;
     }
    
    /**
    *
    * @param ruleId
    * @return
    */
   public List<RuleDao> getAEDByRuleIdJdbcTemplate(String ruleId) {
       List<RuleDao> lstRuleDao = this.jdbcTemplate.query(Constant.SELECTEDRULEDETAILBYID, new Object[] { ruleId, ruleId }, new RowMapper<RuleDao>() {

           @Override
           public RuleDao mapRow(ResultSet rs, int rowNum) throws SQLException {
               RuleDao ruleDao = new RuleDao();
               ruleDao.setRdtId(rs.getInt("RDT_ID"));
               ruleDao.setRdtCompanyCode(rs.getString("RDT_COMPANY"));
               ruleDao.setRdtBranch(rs.getString("RDT_BRANCH"));
               ruleDao.setRdtPartyCode(rs.getString("RDT_PARTY_CODE"));
               ruleDao.setRdtDocType(rs.getString("RDT_DOC_TYPE"));
               ruleDao.setCemEmailFrom(rs.getString("CEM_EMAIL_FROM"));
               ruleDao.setCemEmailTo(rs.getString("CEM_EMAIL_TO"));
               ruleDao.setCemEmailCc(rs.getString("CEM_EMAIL_CC"));
               // ruleDao.setCemSubject(rs.getString("CEM_EMAIL_SUBJECT"));
               ruleDao.setCemBody(rs.getString("CEM_EMAIL_BODY"));
               ruleDao.setCopEmail(rs.getString("EMAIL"));
               ruleDao.setCopPrint(rs.getString("PRINTING"));
               return ruleDao;
           }
       });
       return lstRuleDao;
   }
   
   /**
    * 
    * @param company
    * @return
    */
   public List<DdpRuleDetail> getRuleDetailByCompanyCode(String company) {
       List<DdpRuleDetail> ddpRuleDetail = this.jdbcTemplate.queryForList("select * from ddp_rule_detail where RDT_COMPANY = ?", new Object[] { company }, DdpRuleDetail.class);
       return ddpRuleDetail;
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
    * @return list of All DocTypes whose status is active
    */
   public List<DdpDoctype> getAllDocTypes()
   {
	   List<DdpDoctype> ddpDoctypes = this.jdbcTemplate.query(Constant.SELECT_DOCTYPE_BY_STATUS, new RowMapper<DdpDoctype>(){
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
   
   public List<AedRuleWrapper> getAllActiveAedRules(String role,String regionName,String localCompany,String... companys)
   {
	   String ruletype="AED_RULE";
	   String cmpLst = "";
	   List<AedRuleWrapper> aedRuleWrappers = null;
	   String query = "";
	   
	   if(role.equals("admin_role"))
	   {
		   query = Constant.SELECT_ALL_ACTIVE_RULE_DETAILS;
		   aedRuleWrappers = this.jdbcTemplate.query(query,new Object[]{ ruletype,ruletype },new AedRuleWrapper());
	   }
	   else if(role.equals("Region_role"))
	   {
		   query=Constant.SELECT_ACTIVE_RULES_REGEION;
		   aedRuleWrappers = this.jdbcTemplate.query(query, new Object[]{ ruletype,regionName,ruletype,regionName },new AedRuleWrapper());
	   }
	   else if(role.equals("multi"))
	   {
		   if(companys != null)
		   {
			   String col= "dc.COM_COMPANY_CODE=";
		    	for(String company:companys){
		    		cmpLst  +=col+"'"+company+"' or ";
		    	}
		    	cmpLst = cmpLst.substring(0, cmpLst.lastIndexOf(" or"));
		   }
		   query=Constant.SELECT_ACTIVE_RULE_MULTI_COMPANY.replaceAll("dynamiccondition", cmpLst);
		   aedRuleWrappers = this.jdbcTemplate.query(query, new Object[]{ ruletype,ruletype },new AedRuleWrapper());
	   }
	   else
	   {
		   query=Constant.SELECT_ACTIVE_RULE_LOCAL;
		   aedRuleWrappers = this.jdbcTemplate.query(query, new Object[]{ ruletype,localCompany,ruletype,localCompany },new AedRuleWrapper());
	   }
	   return aedRuleWrappers;
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
   
//   @RequestMapping(value = "/findBranches/{companyCode}", method = RequestMethod.GET, produces = "text/html")
//   public @ResponseBody List<DdpBranch> findBranchByCompany(@PathVariable String companyCode) {
//       // Get the company ID
//   	 List<DdpBranch> branchs = findBranchbyComapny(companyCode);
////   	 String res = "";
////   	 for(DdpBranch branch:branchs){
////   		 res+=branch.getBrnBranchCode()+",";
////   	 }
////   	 res = res.substring(0, res.lastIndexOf(","));
//       return branchs;
//   }
//
   
   @RequestMapping(value={"/isRuleInProcessing","/list/isRuleInProcessing"},method = RequestMethod.GET)
	public @ResponseBody String isRuleInProcessing(@RequestParam("ruleID") Integer ruleID) {
	  
	   String recordsAVailable = "false";
	   List<String> chlAndcatIDs = new ArrayList<String>();
	   String query = "SELECT CHL_ID FROM DDP_CATEGORIZATION_HOLDER WHERE CHL_RUL_ID = ? AND CHL_DTX_ID NOT IN (SELECT DDD_DTX_ID FROM DDP_DMS_DOCS_DETAIL)";
	   List<String> chlIDs = this.jdbcTemplate.query(query, new Object[]{ruleID},new RowMapper<String>(){
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("CHL_ID");
			}
	   });
	   chlAndcatIDs.addAll(chlIDs);
	   List<String> catIDs = this.jdbcTemplate.query(Constant.SELECT_NOT_PROCESSED_CAT_IDS, new Object[]{ruleID},new RowMapper<String>(){
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("CAT_ID");
			}
	   });
	   chlAndcatIDs.addAll(catIDs);
	   if(! chlAndcatIDs.isEmpty())
		   recordsAVailable = "true" ; 
	   return recordsAVailable;
   }

//	@RequestMapping(value = "/{aedRuleId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<String> deleteFromJson(@PathVariable("aedRuleId") Integer aedRuleId) {
//        DdpAedRule ddpAedRule = ddpAedRuleService.findDdpAedRule(aedRuleId);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (ddpAedRule == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        ddpAedRuleService.deleteDdpAedRule(ddpAedRule);
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
}
