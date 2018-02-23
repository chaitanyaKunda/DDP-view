package com.agility.ddp.view.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.agility.ddp.core.monitor.MonitoringObserver;
import com.agility.ddp.core.quartz.RuleJob;
import com.agility.ddp.core.util.SecurityUtils;
import com.agility.ddp.data.domain.DdpCategorizedDocs;
import com.agility.ddp.data.domain.DdpCategorizedDocsService;
import com.agility.ddp.view.util.CategorizedDocsWrapper;

@PropertySource("classpath:common.properties")
@Controller
public class ProcessController {

	@Autowired
	DdpCategorizedDocsController categorizedDocsController;
	
	@Autowired
	DdpCategorizedDocsService ddpCategorizedDocsService;
	
	@Autowired
	RuleJob ruleJob;
	
	@Autowired
	public MonitoringObserver monitoringObserver;
	
    @RequestMapping(value="process/{encryptedCatID}/check")
    public String dupEmailAlert(@PathVariable String encryptedCatID,Model uiModel)
    {
    	String orgCatID = SecurityUtils.agilityDecryptionOnlyNumbers(encryptedCatID);
    	List<CategorizedDocsWrapper> categorizedDocsWrappers = categorizedDocsController.findCategorizedDocsWrappersByParam(Integer.parseInt(orgCatID));
    	if(! categorizedDocsWrappers.isEmpty())
    	{
    		uiModel.addAttribute("categorizedDocsWrapper",categorizedDocsWrappers.get(0));
    		uiModel.addAttribute("encryptedCatID", encryptedCatID);
    	}
    	return "processCat";
    }
    @RequestMapping(value = "process/{encryptedCatID}/confirm")
    public String reProcess(@PathVariable String encryptedCatID) {
    	String orgCatID = SecurityUtils.agilityDecryptionOnlyNumbers(encryptedCatID);
        DdpCategorizedDocs categorizedDocs = ddpCategorizedDocsService.findDdpCategorizedDocs(Integer.parseInt(orgCatID));
        if(categorizedDocs.getCatStatus() != 1){
        	ruleJob.callWorkFlow(new Object[] { categorizedDocs });
        	int status = categorizedDocs.getCatStatus();
        }
        return "redirect:/";
    }
   	@RequestMapping(value="jobDetails") 
   	public String jobDetails(Model uiModel)
   	{
//   		monitoringObserver.doGlobalReferesh();
//			try {
//			Thread.sleep(5*1000L);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//			
   		Map<Object,Object> map = new HashMap<Object,Object>();
//   		map.put("ddpJobsStatus", monitoringObserver.getOverAllStatus());
//   		Map<String,Object> aedRuleJobs = new HashMap<String,Object>();
   		//String aedJobsJson = monitoringObserver.getJSonStringObject(1);
   	//	String consolidatedJobsJson =;
   	//	String exportJobsJson = ;
//   		monitoringObserver.doRefreshBasedService(1);
//			try {
//			Thread.sleep(5*1000L);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//   		aedRuleJobs.put("aedRulesStatus", monitoringObserver.getStatus(1));
//   		aedRuleJobs.put("aedJobsJson", monitoringObserver.getJSonStringObject(1));
//   		map.put("aedRuleJobs", aedRuleJobs);
//   		
//   		monitoringObserver.doRefreshBasedService(2);
//			try {
//			Thread.sleep(3*1000L);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
			
//   		Map<String,Object> consolidatedAEDRuleJobs = new HashMap<String,Object>();
//   		consolidatedAEDRuleJobs.put("consolidateAedRulesStatus", monitoringObserver.getStatus(2));
//   		consolidatedAEDRuleJobs.put("consolidatedJobsJson",  monitoringObserver.getJSonStringObject(2));
//   		map.put("consolidatedAEDRuleJobs", consolidatedAEDRuleJobs);
//   		
//   		monitoringObserver.doRefreshBasedService(3);
//			try {
//			Thread.sleep(3*1000L);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//			
//   		Map<String,Object> exportRuleJobs = new HashMap<String,Object>();
//   		exportRuleJobs.put("exportRulesStatus", monitoringObserver.getStatus(3));
//   		exportRuleJobs.put("exportJobsJson", monitoringObserver.getJSonStringObject(3));
//   		map.put("exportRuleJobs", exportRuleJobs);
   		uiModel.addAttribute("jobDetails", map);
   		return  "monitorJobs";
   	}
   	
   	@RequestMapping(value="process/jobDetailsRefresh/{jobtype}",method=RequestMethod.GET,headers="Accept=Application/json")
   	@ResponseBody
   	public Map<Object,Object> jobDetailsRefresh(@PathVariable("jobtype") Integer jobType, Model uiModel)
   	{
   		Map<Object,Object> map = new HashMap<Object,Object>();
   		if(jobType.intValue() == 111)
   		{
   			monitoringObserver.doGlobalReferesh();
   			try {
				Thread.sleep(5*1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
   			map.put("globalJob", monitoringObserver.getOverAllStatus());
   		}
   		if(jobType.intValue() == 1 || jobType.intValue() == 0)
   		{
   			monitoringObserver.doRefreshBasedService(1);
   			try {
				Thread.sleep(5*1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
   			
	   		Map<String,Object> aedRuleJobs = new HashMap<String,Object>();
	   		aedRuleJobs.put("aedRulesStatus", monitoringObserver.getStatus(1));
	   		aedRuleJobs.put("aedJobsJson", monitoringObserver.getJSonStringObject(1));
	   		map.put("aedRuleJobs", aedRuleJobs);
   		}
   		if(jobType.intValue() == 2 || jobType.intValue() == 0)
   		{
   			monitoringObserver.doRefreshBasedService(2);
   			try {
				Thread.sleep(2*1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
   			
   			
	   		Map<String,Object> consolidatedAEDRuleJobs = new HashMap<String,Object>();
	   		consolidatedAEDRuleJobs.put("consolidateAedRulesStatus", monitoringObserver.getStatus(2));
	   		consolidatedAEDRuleJobs.put("consolidatedJobsJson",  monitoringObserver.getJSonStringObject(2));
	   		map.put("consolidatedAEDRuleJobs", consolidatedAEDRuleJobs);
   		}
   		if(jobType.intValue() == 3 || jobType.intValue() == 0)
   		{
   			
   			monitoringObserver.doRefreshBasedService(3);
   			try {
				Thread.sleep(2*1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
   			
	   		Map<String,Object> exportRuleJobs = new HashMap<String,Object>();
	   		exportRuleJobs.put("exportRulesStatus", monitoringObserver.getStatus(3));
	   		exportRuleJobs.put("exportJobsJson", monitoringObserver.getJSonStringObject(3));
	   		map.put("exportRuleJobs", exportRuleJobs);
   		}
   		if(jobType.intValue() == 0)
   		{
   			map.put("ddpJobsStatus", monitoringObserver.getOverAllStatus());
   		}
   		uiModel.addAttribute("jobDetails", map);
   		return  map;
   	}
   	

   	

}
