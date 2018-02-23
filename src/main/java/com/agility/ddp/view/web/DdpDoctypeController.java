package com.agility.ddp.view.web;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
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

import com.agility.ddp.core.logger.AuditLog;
import com.agility.ddp.data.domain.DdpDoctype;
import com.agility.ddp.view.util.Constant;


@RooWebJson(jsonObject = DdpDoctype.class)
@Controller
@RequestMapping("/ddpdoctypes/list")
@RooWebScaffold(path = "ddpdoctypes", formBackingObject = DdpDoctype.class)
public class DdpDoctypeController {
	
	 private static final Logger logger = LoggerFactory.getLogger(DdpDoctypeController.class);

	 
	 @RequestMapping(value="mediator",method=RequestMethod.GET)
	 public String mediatorController()
	 {
		 return "ddpdoctypes/display";
	 }
	 @RequestMapping(value="listDocTypes",headers="Accept=Application/json",method=RequestMethod.GET)
	 @ResponseBody
	 public Map listDocTypes(@RequestParam(value="page",required=false) Integer page)
	 {
		 Map<Object,Object> map = new HashMap<Object,Object>();
		 ArrayList<Object> records = new ArrayList<Object>();
		 List<DdpDoctype> result = null;
		 HttpHeaders headers = new HttpHeaders();
	     headers.add("Content-Type", "application/json; charset=utf-8");
	     result=ddpDoctypeService.findAllDdpDoctypes();
	     HashMap<Object,Object>[] rowData = new HashMap[result.size()];
	     for(int i =0;i<result.size();i++)
	    	{
//	    		if(result.get(i).getDtyStatus()==0)
//	    		{
	    			rowData[i] = new HashMap<Object, Object>();
	    			rowData[i].put("docTypeName", result.get(i).getDtyDocTypeName());
	    			rowData[i].put("docTypeCode", result.get(i).getDtyDocTypeCode());
	    			rowData[i].put("docTypeSource", result.get(i).getDtySource());
	    			rowData[i].put("docTypeCompanyCode", result.get(i).getDtyCompanyCode());
	    			rowData[i].put("docTypeCreatedBy", result.get(i).getDtyCreatedBy());
	    			rowData[i].put("docTypeCreatedDate", result.get(i).getDtyCreatedDate());
	    			records.add(rowData[i]);
//	    		}
	    	}
	    	map.put("rows",records);
	    	map.put("page",page);
	    	return map;
	 }
	 
	//Get Login User Name
		public String strUserName	= ""; 
	 
    /**
     *
     * @param ddpDoctype
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
	@Transactional
	@AuditLog(message="New Document Type Created. ")
    @RequestMapping(params = {"create"},method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid DdpDoctype ddpDoctype, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpDoctypeController.create() method Invoked.");
        uiModel.asMap().clear();
        strUserName	= SecurityContextHolder.getContext().getAuthentication().getName(); 
        //Prepare doctype
        ddpDoctype.setDtyCreatedBy(strUserName);
        ddpDoctype.setDtyCreatedDate(Constant.CURRENTDATE);
        ddpDoctype.setDtyModifiedBy(strUserName);
        ddpDoctype.setDtyModifiedDate(Constant.CURRENTDATE);
        //Set Status as Active
        ddpDoctype.setDtyStatus(Constant.ACTIVE);
        //Save the Document type
        ddpDoctypeService.saveDdpDoctype(ddpDoctype);
        logger.info("DdpDoctypeController.create() Executed Successfully.");
        return "redirect:/ddpdoctypes/list/list/" + encodeUrlPathSegment(ddpDoctype.getDtyDocTypeCode().toString(), httpServletRequest);
    }

    /**
     *
     * @param ddpDoctype
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
	@Transactional
	@AuditLog(message="DocType Details Updated. ")
    @RequestMapping(params = {"update"},method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid DdpDoctype ddpDoctype, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpDoctypeController.update() method Invoked.");
        uiModel.asMap().clear();
        strUserName	= SecurityContextHolder.getContext().getAuthentication().getName(); 
        //Get selected Groups and selected Roles
        String status = httpServletRequest.getParameter("status");
        DdpDoctype doctype = ddpDoctypeService.findDdpDoctype(ddpDoctype.getDtyDocTypeCode());
        doctype.setDtyStatus(Integer.parseInt(status));
        doctype.setDtyModifiedBy(strUserName);
        doctype.setDtyModifiedDate(Constant.CURRENTDATE);
        doctype.setDtyDocTypeName(ddpDoctype.getDtyDocTypeName());
        doctype.setDtyCompanyCode(ddpDoctype.getDtyCompanyCode());
        doctype.setDtySource(ddpDoctype.getDtySource());
        //Update DocType
        ddpDoctypeService.updateDdpDoctype(doctype);
        logger.info("DdpDoctypeController.update() Executed Successfully.");
        return "redirect:/ddpdoctypes/list/list/" + encodeUrlPathSegment(doctype.getDtyDocTypeCode().toString(), httpServletRequest);
    }

	@RequestMapping(params="list",produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.info("DdpDoctypeController.list() method Invoked.");
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpdoctypes", ddpDoctypeService.findDdpDoctypeEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpDoctypeService.countAllDdpDoctypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpdoctypes", ddpDoctypeService.findAllDdpDoctypes());
        }
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpDoctypeController.list() Executed Successfully.");
        return "ddpdoctypes/list";
    }
	@Transactional
	@AuditLog(message="DocType Deleted. ")
	@RequestMapping(value = "/{dtyDocTypeCode}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("dtyDocTypeCode") String dtyDocTypeCode, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.info("DdpDoctypeController.delete() method Invoked.");
        DdpDoctype ddpDoctype = ddpDoctypeService.findDdpDoctype(dtyDocTypeCode);
        ddpDoctypeService.deleteDdpDoctype(ddpDoctype);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        logger.info("DdpDoctypeController.delete() Executed Successfully.");
//        return "redirect:/ddpdoctypes/list?list&page=1&size=10";
        return "redirect:/ddpdoctypes/list/mediator";
    }

	@RequestMapping(value = "/list/{dtyDocTypeCode}", produces = "text/html")
    public String show(@PathVariable("dtyDocTypeCode") String dtyDocTypeCode, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpdoctype", ddpDoctypeService.findDdpDoctype(dtyDocTypeCode));
        uiModel.addAttribute("itemId", dtyDocTypeCode);
        logger.info("DdpDoctypeController.show() method Invoked and Executed.");
        return "ddpdoctypes/show";
    }

	/*@RequestMapping(value = "/{dtyDocTypeCode}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("dtyDocTypeCode") String dtyDocTypeCode) {
        DdpDoctype ddpDoctype = ddpDoctypeService.findDdpDoctype(dtyDocTypeCode);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpDoctype == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpDoctypeService.deleteDdpDoctype(ddpDoctype);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }*/

	@RequestMapping(value = "/form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpDoctype());
        return "ddpdoctypes/create";
    }

	@RequestMapping(value = "/{dtyDocTypeCode}/form", produces = "text/html")
    public String updateForm(@PathVariable("dtyDocTypeCode") String dtyDocTypeCode, Model uiModel) {
        populateEditForm(uiModel, ddpDoctypeService.findDdpDoctype(dtyDocTypeCode));
        return "ddpdoctypes/update";
    }
}
