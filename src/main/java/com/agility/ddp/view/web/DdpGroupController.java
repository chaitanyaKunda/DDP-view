package com.agility.ddp.view.web;
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

import com.agility.ddp.core.logger.AuditLog;
import com.agility.ddp.data.domain.DdpGroup;
import com.agility.ddp.view.util.Constant;


@RooWebJson(jsonObject = DdpGroup.class)
@Controller
@RequestMapping("/ddpgroups/list")
@RooWebScaffold(path = "ddpgroups", formBackingObject = DdpGroup.class)
public class DdpGroupController {

	
	private static final Logger logger = LoggerFactory.getLogger(DdpGroupController.class);
	
	//Get Login User Name
	public String strUserName	= ""; 
		
    /**
     *
     * @param userWrapper
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
	@Transactional
	@AuditLog(message="New Group Created. ")
    @RequestMapping(params = {"create"},method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid UserWrapper userWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpGroupController.create() Method Invoked.");
        uiModel.asMap().clear();
        //Get Login User Name
        strUserName	= SecurityContextHolder.getContext().getAuthentication().getName(); 
        userWrapper.getDdpGroup().setGrpCreatedBy(strUserName);
        userWrapper.getDdpGroup().setGrpCreatedDate(Constant.CURRENTDATE);
        userWrapper.getDdpGroup().setGrpModifiedBy(strUserName);
        userWrapper.getDdpGroup().setGrpModifiedDate(Constant.CURRENTDATE);
        //Set Status as Active
        userWrapper.getDdpGroup().setGrpStatus(Constant.ACTIVE);
        //Save the DDPGroup
        ddpGroupService.saveDdpGroup(userWrapper.getDdpGroup());
        logger.info("DdpGroupController.create() Executed Successfully.");
        return "redirect:/ddpgroups/list/list/" + encodeUrlPathSegment(userWrapper.getDdpGroup().getGrpId().toString(), httpServletRequest);
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
	@AuditLog(message="Group Details Updated. ")
    @RequestMapping(params = {"update"},method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid UserWrapper userWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	 logger.info("DdpGroupController.update() Method Invoked.");
        uiModel.asMap().clear();
        strUserName	= SecurityContextHolder.getContext().getAuthentication().getName(); 
        //Prepare the ddpgourp bean
        DdpGroup ddpGroup = ddpGroupService.findDdpGroup(userWrapper.getDdpGroup().getGrpId());
        ddpGroup.setGrpDescription(userWrapper.getDdpGroup().getGrpDescription());
        ddpGroup.setGrpDisplayName(userWrapper.getDdpGroup().getGrpDisplayName());
        ddpGroup.setGrpModifiedBy(strUserName);
        ddpGroup.setGrpModifiedDate(Constant.CURRENTDATE);
        //Set Status as Active
        userWrapper.getDdpGroup().setGrpStatus(Constant.ACTIVE);
        //Update the DDPGroup
        ddpGroupService.updateDdpGroup(ddpGroup);
        logger.info("DdpGroupController.update() Executed Successfully.");
        return "redirect:/ddpgroups/list/list/" + encodeUrlPathSegment(userWrapper.ddpGroup.getGrpId().toString(), httpServletRequest);
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
        logger.info("DdpGroupController.updateForm(Model uiModel) Method Invoked and Executed.");
        return "ddpgroups/create";
    }

    /**
     *
     * @param uiModel
     * @param userWrapper
     */
    void populateEditForm(Model uiModel, UserWrapper userWrapper) {
    	logger.info("DdpGroupController.populateEditForm() Method Invoked.");
        uiModel.addAttribute("userWrapper", userWrapper);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpgroupsetups", ddpGroupSetupService.findAllDdpGroupSetups());
        uiModel.addAttribute("ddprolesetups", ddpRoleSetupService.findAllDdpRoleSetups());
        logger.info("DdpGroupController.populateEditForm() Executed Successfully.");
    }

    /**
     *
     * @param grpId
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "/{grpId}/form", produces = "text/html")
    public String updateForm(@PathVariable("grpId") Integer grpId, Model uiModel) {
    	logger.info("DdpGroupController.updateForm() Method Invoked.");
        UserWrapper userWrapper = new UserWrapper();
        DdpGroup ddpGroup = ddpGroupService.findDdpGroup(grpId);
        userWrapper.setDdpGroup(ddpGroup);
        populateEditForm(uiModel, userWrapper);
        logger.info("DdpGroupController.updateForm() Executed Successfully.");
        return "ddpgroups/update";
    }

	@RequestMapping(params="list",produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.info("DdpGroupController.list() Method Invoked.");
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpgroups", ddpGroupService.findDdpGroupEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpGroupService.countAllDdpGroups() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpgroups", ddpGroupService.findAllDdpGroups());
        }
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpGroupController.list() Executed Successfully.");
        return "ddpgroups/list";
    }
	@Transactional
	@AuditLog(message="Group Deleted. ")
	@RequestMapping(value = "/{grpId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("grpId") Integer grpId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		logger.info("DdpGroupController.list() Method Invoked.");
        DdpGroup ddpGroup = ddpGroupService.findDdpGroup(grpId);
        ddpGroupService.deleteDdpGroup(ddpGroup);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        logger.info("DdpGroupController.list() Executed Successfully.");
        return "redirect:/ddpgroups/list?list&page=1&size=10";
    }

	@RequestMapping(value = "/list/{grpId}", produces = "text/html")
    public String show(@PathVariable("grpId") Integer grpId, Model uiModel) {
		logger.info("DdpGroupController.show() Method Invoked.");
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpgroup", ddpGroupService.findDdpGroup(grpId));
        uiModel.addAttribute("itemId", grpId);
        logger.info("DdpGroupController.show() Executed Successfully.");
        return "ddpgroups/show";
    }

	/*@RequestMapping(value = "/{grpId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("grpId") Integer grpId) {
        DdpGroup ddpGroup = ddpGroupService.findDdpGroup(grpId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpGroup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpGroupService.deleteDdpGroup(ddpGroup);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }*/
}
