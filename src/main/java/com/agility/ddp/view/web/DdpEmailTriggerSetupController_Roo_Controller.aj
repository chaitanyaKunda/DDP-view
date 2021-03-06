// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpEmailTriggerSetup;
import com.agility.ddp.data.domain.DdpEmailTriggerSetupService;
import com.agility.ddp.data.domain.DdpRuleService;
import com.agility.ddp.view.web.DdpEmailTriggerSetupController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect DdpEmailTriggerSetupController_Roo_Controller {
    
    @Autowired
    DdpEmailTriggerSetupService DdpEmailTriggerSetupController.ddpEmailTriggerSetupService;
    
    @Autowired
    DdpRuleService DdpEmailTriggerSetupController.ddpRuleService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpEmailTriggerSetupController.create(@Valid DdpEmailTriggerSetup ddpEmailTriggerSetup, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpEmailTriggerSetup);
            return "ddpemailtriggersetups/create";
        }
        uiModel.asMap().clear();
        ddpEmailTriggerSetupService.saveDdpEmailTriggerSetup(ddpEmailTriggerSetup);
        return "redirect:/ddpemailtriggersetups/" + encodeUrlPathSegment(ddpEmailTriggerSetup.getEtrId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpEmailTriggerSetupController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpEmailTriggerSetup());
        return "ddpemailtriggersetups/create";
    }
    
    @RequestMapping(value = "/{etrId}", produces = "text/html")
    public String DdpEmailTriggerSetupController.show(@PathVariable("etrId") Integer etrId, Model uiModel) {
        uiModel.addAttribute("ddpemailtriggersetup", ddpEmailTriggerSetupService.findDdpEmailTriggerSetup(etrId));
        uiModel.addAttribute("itemId", etrId);
        return "ddpemailtriggersetups/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpEmailTriggerSetupController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpemailtriggersetups", ddpEmailTriggerSetupService.findDdpEmailTriggerSetupEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpEmailTriggerSetupService.countAllDdpEmailTriggerSetups() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpemailtriggersetups", ddpEmailTriggerSetupService.findAllDdpEmailTriggerSetups());
        }
        return "ddpemailtriggersetups/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpEmailTriggerSetupController.update(@Valid DdpEmailTriggerSetup ddpEmailTriggerSetup, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpEmailTriggerSetup);
            return "ddpemailtriggersetups/update";
        }
        uiModel.asMap().clear();
        ddpEmailTriggerSetupService.updateDdpEmailTriggerSetup(ddpEmailTriggerSetup);
        return "redirect:/ddpemailtriggersetups/" + encodeUrlPathSegment(ddpEmailTriggerSetup.getEtrId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{etrId}", params = "form", produces = "text/html")
    public String DdpEmailTriggerSetupController.updateForm(@PathVariable("etrId") Integer etrId, Model uiModel) {
        populateEditForm(uiModel, ddpEmailTriggerSetupService.findDdpEmailTriggerSetup(etrId));
        return "ddpemailtriggersetups/update";
    }
    
    @RequestMapping(value = "/{etrId}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpEmailTriggerSetupController.delete(@PathVariable("etrId") Integer etrId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpEmailTriggerSetup ddpEmailTriggerSetup = ddpEmailTriggerSetupService.findDdpEmailTriggerSetup(etrId);
        ddpEmailTriggerSetupService.deleteDdpEmailTriggerSetup(ddpEmailTriggerSetup);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpemailtriggersetups";
    }
    
    void DdpEmailTriggerSetupController.populateEditForm(Model uiModel, DdpEmailTriggerSetup ddpEmailTriggerSetup) {
        uiModel.addAttribute("ddpEmailTriggerSetup", ddpEmailTriggerSetup);
        uiModel.addAttribute("ddprules", ddpRuleService.findAllDdpRules());
    }
    
    String DdpEmailTriggerSetupController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
