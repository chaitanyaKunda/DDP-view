// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpGroupService;
import com.agility.ddp.data.domain.DdpRoleService;
import com.agility.ddp.data.domain.DdpRoleSetup;
import com.agility.ddp.data.domain.DdpRoleSetupService;
import com.agility.ddp.data.domain.DdpUserService;
import com.agility.ddp.view.web.DdpRoleSetupController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect DdpRoleSetupController_Roo_Controller {
    
    @Autowired
    DdpRoleSetupService DdpRoleSetupController.ddpRoleSetupService;
    
    @Autowired
    DdpGroupService DdpRoleSetupController.ddpGroupService;
    
    @Autowired
    DdpRoleService DdpRoleSetupController.ddpRoleService;
    
    @Autowired
    DdpUserService DdpRoleSetupController.ddpUserService;
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpRoleSetupController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpRoleSetup());
        return "ddprolesetups/create";
    }
    
    void DdpRoleSetupController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("ddpRoleSetup_rlscreateddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpRoleSetup_rlsmodifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }
    
    void DdpRoleSetupController.populateEditForm(Model uiModel, DdpRoleSetup ddpRoleSetup) {
        uiModel.addAttribute("ddpRoleSetup", ddpRoleSetup);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpgroups", ddpGroupService.findAllDdpGroups());
        uiModel.addAttribute("ddproles", ddpRoleService.findAllDdpRoles());
        uiModel.addAttribute("ddpusers", ddpUserService.findAllDdpUsers());
    }
    
    String DdpRoleSetupController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
