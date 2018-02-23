package com.agility.ddp.view.web;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.agility.ddp.core.logger.AuditLog;
import com.agility.ddp.data.domain.DdpBranch;
import com.agility.ddp.data.domain.DdpCompany;
import com.agility.ddp.view.util.Constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
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


@RooWebJson(jsonObject = DdpBranch.class)
@Controller
@RequestMapping("/ddpbranches/list")
@RooWebScaffold(path = "ddpbranches", formBackingObject = DdpBranch.class)
public class DdpBranchController {

	
	 private static final Logger logger = LoggerFactory.getLogger(DdpBranchController.class);
	 
	 
	 @RequestMapping(value="mediator",method=RequestMethod.GET)
	 public String mediatorController()
	 {
		 return "ddpbranches/display";
	 }
	 
	 @RequestMapping(value="listBranches",headers="Accept=Application/json",method=RequestMethod.GET)
	 @ResponseBody
	 public Map listBranches(@RequestParam(value="page",required=false) Integer page)
	 {
		 Map<Object,Object> map = new HashMap<Object,Object>();
		 ArrayList<Object> records = new ArrayList<Object>();
		 List<DdpBranch> result = null;
		 HttpHeaders headers = new HttpHeaders();
	     headers.add("Content-Type", "application/json; charset=utf-8");
	     result=ddpBranchService.findAllDdpBranches();
	     HashMap<Object,Object>[] rowData = new HashMap[result.size()];
	    	for(int i =0;i<result.size();i++)
	    	{
//	    		if(result.get(i).getBrnStatus() == 0)
//	    		{
		    		rowData[i] = new HashMap<Object, Object>();
		    		rowData[i].put("branchName", result.get(i).getBrnBranchName());
		    		rowData[i].put("branchCode", result.get(i).getBrnBranchCode());
		    		rowData[i].put("companyCode", result.get(i).getBrnCompnayCode());
		    		records.add(rowData[i]);
//	    		}
	    	}
	    	map.put("rows",records);
	    	map.put("page",page);
	    	return map;
	 }
    /**
     *
     * @param ddpBranch
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
	@Transactional
	@AuditLog(message="New Branch Created. ")
    @RequestMapping(params = {"create"},method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid DdpBranch ddpBranch, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpBranchController.create() Method Invoked."); 
        uiModel.asMap().clear();
        ddpBranch.setBrnStatus(Constant.ACTIVE);
        //Save the Branch
        ddpBranchService.saveDdpBranch(ddpBranch);
        logger.info("DdpBranchController.create() Executed Successfully."); 
        return "redirect:/ddpbranches/list/list/" + ddpBranch.getBrnBranchCode();
    }

    /**
     *
     * @param ddpBranch
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
	@Transactional
	@AuditLog(message="Branch Details Updated. ")
    @RequestMapping(params = {"update"},method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid DdpBranch ddpBranch, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpBranchController.update() Method Invoked."); 
        String status = httpServletRequest.getParameter("status");
        ddpBranch.setBrnStatus(Integer.parseInt(status));
        uiModel.asMap().clear();
        //Update the Branch
        ddpBranchService.updateDdpBranch(ddpBranch);
        logger.info("DdpBranchController.update() Executed Successfully."); 
        return "redirect:/ddpbranches/list/list/" + encodeUrlPathSegment(ddpBranch.getBrnBranchCode().toString(), httpServletRequest);
    }

	@RequestMapping(params="list",produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.info("DdpBranchController.list() Method Invoked."); 
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpbranches", ddpBranchService.findDdpBranchEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpBranchService.countAllDdpBranches() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpbranches", ddpBranchService.findAllDdpBranches());
        }
        logger.info("DdpBranchController.list() Executed Successfully."); 
        return "ddpbranches/list";
    }

	@RequestMapping(value = "/list/{brnBranchCode}", produces = "text/html")
    public String show(@PathVariable("brnBranchCode") String brnBranchCode, Model uiModel) {
        uiModel.addAttribute("ddpbranch", ddpBranchService.findDdpBranch(brnBranchCode));
        uiModel.addAttribute("itemId", brnBranchCode);
        logger.info("DdpBranchController.show() Method Invoked and Executed."); 
        return "ddpbranches/show";
    }
	@Transactional
	@AuditLog(message="Branch Deleted. ")
	@RequestMapping(value = "/{brnBranchCode}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("brnBranchCode") String brnBranchCode, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.info("DdpBranchController.delete() Method Invoked."); 
        DdpBranch ddpBranch = ddpBranchService.findDdpBranch(brnBranchCode);
        ddpBranchService.deleteDdpBranch(ddpBranch);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        logger.info("DdpBranchController.delete() Executed Successfully.");
//        return "redirect:/ddpbranches/list?list&page=1&size=10";
        return "redirect:/ddpbranches/list/mediator";
    }
	/*
	@RequestMapping(value = "/{brnBranchCode}", method = RequestMethod.DELETE,produces = "text/html")
    public String deleteFrom(@PathVariable("brnBranchCode") String brnBranchCode) {
		 DdpBranch ddpBranch = ddpBranchService.findDdpBranch(brnBranchCode);
	        ddpBranchService.deleteDdpBranch(ddpBranch);
	        
	        return "redirect:/ddpbranches/list?list&page=1&size=10";
    }
	
	@RequestMapping(value = "/{brnBranchCode}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("brnBranchCode") String brnBranchCode) {
        DdpBranch ddpBranch = ddpBranchService.findDdpBranch(brnBranchCode);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpBranch == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpBranchService.deleteDdpBranch(ddpBranch);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }*/

	@RequestMapping(value = "/form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpBranch());
        return "ddpbranches/create";
    }

	@RequestMapping(value = "/{brnBranchCode}/form", produces = "text/html")
    public String updateForm(@PathVariable("brnBranchCode") String brnBranchCode, Model uiModel) {
        populateEditForm(uiModel, ddpBranchService.findDdpBranch(brnBranchCode));
        return "ddpbranches/update";
    }
}
