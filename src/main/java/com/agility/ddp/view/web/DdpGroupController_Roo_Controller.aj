// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpGroup;
import com.agility.ddp.data.domain.DdpGroupService;
import com.agility.ddp.data.domain.DdpGroupSetupService;
import com.agility.ddp.data.domain.DdpRoleSetupService;
import com.agility.ddp.view.web.DdpGroupController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect DdpGroupController_Roo_Controller {
    
    @Autowired
    DdpGroupService DdpGroupController.ddpGroupService;
    
    @Autowired
    DdpGroupSetupService DdpGroupController.ddpGroupSetupService;
    
    @Autowired
    DdpRoleSetupService DdpGroupController.ddpRoleSetupService;
    
        
        
        
    void DdpGroupController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("ddpGroup_grpcreateddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpGroup_grpmodifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }
    
    void DdpGroupController.populateEditForm(Model uiModel, DdpGroup ddpGroup) {
        uiModel.addAttribute("ddpGroup", ddpGroup);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpgroupsetups", ddpGroupSetupService.findAllDdpGroupSetups());
        uiModel.addAttribute("ddprolesetups", ddpRoleSetupService.findAllDdpRoleSetups());
    }
    
    String DdpGroupController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
