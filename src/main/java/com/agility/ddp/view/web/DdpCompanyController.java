package com.agility.ddp.view.web;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.agility.ddp.core.logger.AuditLog;
import com.agility.ddp.data.domain.DdpCompany;
import com.agility.ddp.view.util.Constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;



@RooWebJson(jsonObject = DdpCompany.class)
@Controller
@RequestMapping("/ddpcompanys/list")
@RooWebScaffold(path = "ddpcompanys", formBackingObject = DdpCompany.class)
public class DdpCompanyController {

	
	 private static final Logger logger = LoggerFactory.getLogger(DdpCompanyController.class);
	
	 @RequestMapping(value="mediator")
	 public String companyMediator()
	 {
		 return "ddpcompanys/display";
	 }
	 @RequestMapping(value="listCompanys",headers="Accept=Application/json",method=RequestMethod.GET)
	 @ResponseBody
	 public Map listCompanys(@RequestParam(value="page",required=false) Integer page)
	 {
		 Map<Object,Object> map = new HashMap<Object,Object>();
		 ArrayList<Object> records = new ArrayList<Object>();
		 List<DdpCompany> result = null;
		 HttpHeaders headers = new HttpHeaders();
	     headers.add("Content-Type", "application/json; charset=utf-8");
	     result=ddpCompanyService.findAllDdpCompanys();
	     HashMap<Object,Object>[] rowData = new HashMap[result.size()];
	    	for(int i =0;i<result.size();i++)
	    	{
//	    		if(result.get(i).getComStatus() == 0)
//	    		{
		    		rowData[i] = new HashMap<Object, Object>();
		    		rowData[i].put("companyName", result.get(i).getComCompanyName());
		    		rowData[i].put("companyCode", result.get(i).getComCompanyCode());
		    		rowData[i].put("countryCode", result.get(i).getComCountryCode());
		    		rowData[i].put("countryName", result.get(i).getComCountryName());
		    		rowData[i].put("region", result.get(i).getComRegion());
		    		records.add(rowData[i]);
//	    		}
	    	}
	    	map.put("rows",records);
	    	map.put("page",page);
	    	return map;
	 }
	 
    /**
     *
     * @param ddpCompany
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
	@Transactional
	@AuditLog(message="New Company Created. ")
    @RequestMapping(params = {"create"},method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid DdpCompany ddpCompany, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpCompanyController.create() Method Invoked."); 
        ddpCompany.setComStatus(Constant.ACTIVE);
        uiModel.asMap().clear();
        //Save the Company
        ddpCompanyService.saveDdpCompany(ddpCompany);
        logger.info("DdpCompanyController.create() Executed Successfully."); 
        return "redirect:/ddpcompanys/list/list/" +ddpCompany.getComCompanyCode();
    }

    /**
     *
     * @param ddpCompany
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
	@Transactional
	@AuditLog(message="Company Details Updated. ")
    @RequestMapping(params = "update",method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid DdpCompany ddpCompany, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	 logger.info("DdpCompanyController.update() Method Invoked."); 
        //Get the status
        String status = httpServletRequest.getParameter("status");
        ddpCompany.setComStatus(Integer.parseInt(status));
        uiModel.asMap().clear();
        //Update the Company
        ddpCompanyService.updateDdpCompany(ddpCompany);
        logger.info("DdpCompanyController.update() Executed Successfully.");
        return "redirect:/ddpcompanys/list/list/"+ddpCompany.getComCompanyCode();
    }

	@RequestMapping(params="list",produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.info("DdpCompanyController.list() Method Invoked.");
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpcompanys", ddpCompanyService.findDdpCompanyEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpCompanyService.countAllDdpCompanys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpcompanys", ddpCompanyService.findAllDdpCompanys());
        }
        logger.info("DdpCompanyController.list() Executed Successfully.");
        return "ddpcompanys/list";
    }

	@RequestMapping(value = "/list/{comCompanyCode}", produces = "text/html")
    public String show(@PathVariable("comCompanyCode") String comCompanyCode, Model uiModel) {
        uiModel.addAttribute("ddpcompany", ddpCompanyService.findDdpCompany(comCompanyCode));
        uiModel.addAttribute("itemId", comCompanyCode);
        logger.info("DdpCompanyController.show() Method Invoked and Executed.");
        return "ddpcompanys/show";
    }

	@RequestMapping(value = "/{comCompanyCode}/form", produces = "text/html")
    public String updateForm(@PathVariable("comCompanyCode") String comCompanyCode, Model uiModel) {
        populateEditForm(uiModel, ddpCompanyService.findDdpCompany(comCompanyCode));
        logger.info("DdpCompanyController.updateForm() Method Invoked and Executed.");
        return "ddpcompanys/update";
    }
	@Transactional
	@AuditLog(message="Company  Deleted. ")
	@RequestMapping(value = "/{comCompanyCode}", method = RequestMethod.DELETE, produces = "text/html")
    public String deletecompany(@PathVariable("comCompanyCode") String comCompanyCode, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.info("DdpCompanyController.deletecompany() Method Invoked.");
        DdpCompany ddpCompany = ddpCompanyService.findDdpCompany(comCompanyCode);
        ddpCompanyService.deleteDdpCompany(ddpCompany);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        logger.info("DdpCompanyController.deletecompany() Executed Successfully.");
//        return "redirect:/ddpcompanys//list?list&page=1&size=10";
        return "redirect:/ddpcompanys/list/mediator";
    }

	/*@RequestMapping(value = "/{comCompanyCode}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("comCompanyCode") String comCompanyCode) {
        DdpCompany ddpCompany = ddpCompanyService.findDdpCompany(comCompanyCode);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpCompany == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpCompanyService.deleteDdpCompany(ddpCompany);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }*/

	@RequestMapping(value = "/form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpCompany());
        return "ddpcompanys/create";
    }
}
