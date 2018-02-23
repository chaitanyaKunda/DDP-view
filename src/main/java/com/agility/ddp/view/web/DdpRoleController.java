package com.agility.ddp.view.web;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import com.agility.ddp.data.domain.DdpRole;
import com.agility.ddp.data.domain.DdpRoleService;
import com.agility.ddp.data.domain.DdpRoleSetupService;
import com.agility.ddp.view.util.Constant;


@RooWebJson(jsonObject = DdpRole.class)
@Controller
@RequestMapping("/ddproles/list")
@RooWebScaffold(path = "ddproles", formBackingObject = DdpRole.class)
public class DdpRoleController {
	
	private static final Logger logger = LoggerFactory.getLogger(DdpRoleController.class);
	
	 @RequestMapping(value="mediator")
	 public String roleMediator()
	 {
		 return "ddproles/display";
	 }
	 @RequestMapping(value="listRoles",headers="Accept=Application/json",method=RequestMethod.GET)
	 @ResponseBody
	 public Map<Object,Object> listRoles(@RequestParam(value="page",required=false) Integer page)
	 {
		 Map<Object,Object> map = new HashMap<Object,Object>();
		 ArrayList<Object> records = new ArrayList<Object>();
		 List<DdpRole> result = null;
		 HttpHeaders headers = new HttpHeaders();
	     headers.add("Content-Type", "application/json; charset=utf-8");
	     result=ddpRoleService.findAllDdpRoles();
	     HashMap<Object,Object>[] rowData = new HashMap[result.size()];
    	for(int i =0;i<result.size();i++)
    	{
    		rowData[i] = new HashMap<Object, Object>();
    		rowData[i].put("rolId", result.get(i).getRolId());
    		rowData[i].put("rolName", result.get(i).getRolName());
    		rowData[i].put("rolDisplayName", result.get(i).getRolDisplayName());
    		rowData[i].put("rolDescription", result.get(i).getRolDescription());
    		rowData[i].put("rolStatus", result.get(i).getRolStatus());
    		rowData[i].put("rolCreatedBy", result.get(i).getRolCreatedBy());
    		records.add(rowData[i]);
    	}
    	map.put("rows",records);
    	map.put("page",page);
    	return map;
	 }
    /**
     *
     * @param userWrapper
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
	@Transactional
	@AuditLog(message="New Role Created. ")
    @RequestMapping(params = {"create"},method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid UserWrapper userWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpRoleController.create() Method Invoked.");
    	//Get Login User Name
    	String strUserName	= ""; 
        uiModel.asMap().clear();
        strUserName	= SecurityContextHolder.getContext().getAuthentication().getName(); 
        //Prepare the DdpRole
        userWrapper.getDdpRole().setRolCreatedBy(strUserName);
        userWrapper.getDdpRole().setRolCreatedDate(Constant.CURRENTDATE);
        userWrapper.getDdpRole().setRolModifiedBy(strUserName);
        userWrapper.getDdpRole().setRolModifiedDate(Constant.CURRENTDATE);
        //Set the Active by as default for
        userWrapper.getDdpRole().setRolStatus(Constant.ACTIVE);
        ddpRoleService.saveDdpRole(userWrapper.getDdpRole());
        logger.info("DdpRoleController.create() Executed Successfully.");
        return "redirect:/ddproles/list/list/" + encodeUrlPathSegment(userWrapper.getDdpRole().getRolId().toString(), httpServletRequest);
    }

    /**
     *
     * @param uiModel
     * @param userWrapper
     */
    private void populateEditForm(Model uiModel, UserWrapper userWrapper) {
        uiModel.addAttribute("userWrapper", userWrapper);
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpRoleController.populateEditForm() Method Invoked and Executed.");
        uiModel.addAttribute("ddprolesetups", ddpRoleSetupService.findAllDdpRoleSetups());
    }

    /**
     *
     * @param userWrapper
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
    @Transactional
    @AuditLog(message="Role Details Updated. ")
    @RequestMapping(params = {"update"},method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid UserWrapper userWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpRoleController.update() Method Invoked.");
    	//Get Login User Name
    	String strUserName	= ""; 
        uiModel.asMap().clear();
        strUserName	= SecurityContextHolder.getContext().getAuthentication().getName(); 
        DdpRole ddpRole = ddpRoleService.findDdpRole(userWrapper.getDdpRole().getRolId());
        ddpRole.setRolDescription(userWrapper.getDdpRole().getRolDescription());
        ddpRole.setRolDisplayName(userWrapper.getDdpRole().getRolDisplayName());
        ddpRole.setRolModifiedBy(strUserName);
        ddpRole.setRolModifiedDate(Constant.CURRENTDATE);
        //Set the Active by as default for
        ddpRole.setRolStatus(Constant.ACTIVE);
        ddpRoleService.updateDdpRole(ddpRole);
        logger.info("DdpRoleController.update() Executed Successfully.");
        return "redirect:/ddproles/list/list/" + encodeUrlPathSegment(userWrapper.getDdpRole().getRolId().toString(), httpServletRequest);
    }

    /**
     *
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "/form", produces = "text/html")
    public String createForm(Model uiModel) {
        UserWrapper userWrapper = new UserWrapper();
        populateEditForm(uiModel, userWrapper);
        logger.info("DdpRoleController.createForm() Method Invoked and Executed.");
        return "ddproles/create";
    }

    /**
     *
     * @param rolId
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "/{rolId}/form", produces = "text/html")
    public String updateForm(@PathVariable("rolId") Integer rolId, Model uiModel) {
    	 logger.info("DdpRoleController.updateForm() Method Invoked.");
        UserWrapper userWrapper = new UserWrapper();
        DdpRole ddpRole = ddpRoleService.findDdpRole(rolId);
        userWrapper.setDdpRole(ddpRole);
        populateEditForm(uiModel, userWrapper);
        logger.info("DdpRoleController.updateForm() Executed Successfully.");
        return "ddproles/update";
    }

	@RequestMapping(params="list",produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.info("DdpRoleController.List() Method Invoked.");
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddproles", ddpRoleService.findDdpRoleEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpRoleService.countAllDdpRoles() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddproles", ddpRoleService.findAllDdpRoles());
        }
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpRoleController.List() Executed Successfully.");
        return "ddproles/list";
    }

	@Autowired
    DdpRoleService ddpRoleService;

	@Autowired
    DdpRoleSetupService ddpRoleSetupService;


	@RequestMapping(value = "/list/{rolId}", produces = "text/html")
    public String show(@PathVariable("rolId") Integer rolId, Model uiModel) {
		logger.info("DdpRoleController.show() Method Invoked.");
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddprole", ddpRoleService.findDdpRole(rolId));
        uiModel.addAttribute("itemId", rolId);
        logger.info("DdpRoleController.show() Executed Successfully.");
        return "ddproles/show";
    }
	@Transactional
	@AuditLog(message="Role Deleted. ")
	@RequestMapping(value = "/{rolId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("rolId") Integer rolId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.info("DdpRoleController.delete() Method Invoked.");
        DdpRole ddpRole = ddpRoleService.findDdpRole(rolId);
        ddpRoleService.deleteDdpRole(ddpRole);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        logger.info("DdpRoleController.delete() Executed Successfully.");
        return "redirect:/ddproles/list/mediator";
    }

	/*@RequestMapping(value = "/{rolId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("rolId") Integer rolId) {
        DdpRole ddpRole = ddpRoleService.findDdpRole(rolId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpRole == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpRoleService.deleteDdpRole(ddpRole);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }*/
}
