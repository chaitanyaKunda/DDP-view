package com.agility.ddp.view.web;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.agility.ddp.core.logger.AuditLog;
import com.agility.ddp.data.domain.DdpDoctype;
import com.agility.ddp.data.domain.DdpParty;
import com.agility.ddp.view.util.Constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
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


@RooWebJson(jsonObject = DdpParty.class)
@Controller
@RequestMapping("/ddppartys/list")
@RooWebScaffold(path = "ddppartys", formBackingObject = DdpParty.class)
public class DdpPartyController {

	
	 private static final Logger logger = LoggerFactory.getLogger(DdpPartyController.class);
	
	//Get Login User Name
	public String strUserName	= "";
	
	
	
	@RequestMapping(value="mediator",method=RequestMethod.GET)
	 public String mediatorController()
	 {
		 return "ddppartys/display";
	 }
	@RequestMapping(value="listParty",headers="Accept=Application/json",method=RequestMethod.GET)
	 @ResponseBody
	 public Map listDocTypes(@RequestParam(value="page",required=false) Integer page)
	 {
		 Map<Object,Object> map = new HashMap<Object,Object>();
		 ArrayList<Object> records = new ArrayList<Object>();
		 List<DdpParty> result = null;
		 HttpHeaders headers = new HttpHeaders();
	     headers.add("Content-Type", "application/json; charset=utf-8");
	     result = ddpPartyService.findAllDdpPartys();
	     HashMap<Object,Object>[] rowData = new HashMap[result.size()];
	     for(int i =0;i<result.size();i++)
	    	{
//	    	 	if(result.get(i).getPtyStatus() == 0)
//	    	 	{
	    	 		rowData[i] = new HashMap<Object, Object>();
	    	 		rowData[i].put("partyName", result.get(i).getPtyPartyName());
	    	 		rowData[i].put("partyCode", result.get(i).getPtyPartyCode());
	    	 		rowData[i].put("companyCode", result.get(i).getPtyCompanyCode());
	    	 		rowData[i].put("applicationCode", result.get(i).getPtyApplicationCode());
	    	 		rowData[i].put("createdBy", result.get(i).getPtyCreatedBy());
	    	 		rowData[i].put("createdDate", result.get(i).getPtyCreatedDate());
	    	 		rowData[i].put("modifiedBy", result.get(i).getPtyModifiedBy());
	    	 		rowData[i].put("modifiedDate", result.get(i).getPtyModifiedDate());
	    	 		records.add(rowData[i]);
//	    	 	}
	    	}
	     map.put("rows",records);
	     map.put("page",page);
	     return map;
	 }
	
    /**
     *
     * @param ddpParty
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
	@Transactional
	@AuditLog(message="New Party Created. ")
    @RequestMapping(params = {"create"},method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid DdpParty ddpParty, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpPartyController.create() Method Invoked."); 
        uiModel.asMap().clear();
        strUserName	= SecurityContextHolder.getContext().getAuthentication().getName(); 
        //Prepare party code
        ddpParty.setPtyCreatedBy(strUserName);
        ddpParty.setPtyCreatedDate(Constant.CURRENTDATE);
        ddpParty.setPtyModifiedBy(strUserName);
        ddpParty.setPtyModifiedDate(Constant.CURRENTDATE);
        ddpParty.setPtyStatus(Constant.ACTIVE);
        //Save Party
        ddpPartyService.saveDdpParty(ddpParty);
        logger.info("DdpPartyController.create() Executed Successfully."); 
        return "redirect:/ddppartys/list/list/" + encodeUrlPathSegment(ddpParty.getPtyPartyCode().toString(), httpServletRequest);
    }
	@Transactional
	@AuditLog(message="Party Details Updated. ")
    @RequestMapping(params = {"update"},method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid DdpParty ddpParty, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpPartyController.update() Method Invoked."); 
        uiModel.asMap().clear();
        strUserName	= SecurityContextHolder.getContext().getAuthentication().getName(); 
        //Get selected Groups and selected Roles
        String status = httpServletRequest.getParameter("status");
        DdpParty party = ddpPartyService.findDdpParty(ddpParty.getPtyPartyCode());
        party.setPtyModifiedBy(strUserName);
        party.setPtyModifiedDate(Constant.CURRENTDATE);
        party.setPtyApplicationCode(ddpParty.getPtyApplicationCode());
        party.setPtyCompanyCode(ddpParty.getPtyCompanyCode());
        party.setPtyStatus(Integer.parseInt(status));
        //Update Party
        ddpPartyService.updateDdpParty(party);
        logger.info("DdpPartyController.update() Executed Successfully."); 
        return "redirect:/ddppartys/list/list/" + encodeUrlPathSegment(party.getPtyPartyCode().toString(), httpServletRequest);
    }

	@RequestMapping(params="list",produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.info("DdpPartyController.list() Method Invoked."); 
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddppartys", ddpPartyService.findDdpPartyEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpPartyService.countAllDdpPartys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddppartys", ddpPartyService.findAllDdpPartys());
        }
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpPartyController.list() Executed Successfully."); 
        return "ddppartys/list";
    }

	@RequestMapping(value = "/list/{ptyPartyCode}", produces = "text/html")
    public String show(@PathVariable("ptyPartyCode") String ptyPartyCode, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpparty", ddpPartyService.findDdpParty(ptyPartyCode));
        uiModel.addAttribute("itemId", ptyPartyCode);
        logger.info("DdpPartyController.list() Method Invoked and Executed."); 
        return "ddppartys/show";
    }
	@Transactional
	@AuditLog(message="Party Deleted. ")
	@RequestMapping(value = "/{ptyPartyCode}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("ptyPartyCode") String ptyPartyCode, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.info("DdpPartyController.delete() Method Invoked."); 
        DdpParty ddpParty = ddpPartyService.findDdpParty(ptyPartyCode);
        ddpPartyService.deleteDdpParty(ddpParty);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        logger.info("DdpPartyController.delete() Executed Successfully."); 
//        return "redirect:/ddppartys/list?list&page=1&size=10";
        return "redirect:/ddppartys/list/mediator";
    }

	/*@RequestMapping(value = "/{ptyPartyCode}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("ptyPartyCode") String ptyPartyCode) {
        DdpParty ddpParty = ddpPartyService.findDdpParty(ptyPartyCode);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpParty == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpPartyService.deleteDdpParty(ddpParty);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }*/

	@RequestMapping(value = "/form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpParty());
        return "ddppartys/create";
    }

	@RequestMapping(value = "/{ptyPartyCode}/form", produces = "text/html")
    public String updateForm(@PathVariable("ptyPartyCode") String ptyPartyCode, Model uiModel) {
        populateEditForm(uiModel, ddpPartyService.findDdpParty(ptyPartyCode));
        return "ddppartys/update";
    }
}
