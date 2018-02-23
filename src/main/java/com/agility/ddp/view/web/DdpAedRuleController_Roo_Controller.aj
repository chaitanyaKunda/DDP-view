// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpAedRule;
import com.agility.ddp.data.domain.DdpSchedulerService;
import com.agility.ddp.view.web.DdpAedRuleController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect DdpAedRuleController_Roo_Controller {
    
    @Autowired
    DdpSchedulerService DdpAedRuleController.ddpSchedulerService;
    
    void DdpAedRuleController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("ddpAedRule_aedcreateddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpAedRule_aedmodifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpAedRule_aedactivationdate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }
    
    void DdpAedRuleController.populateEditForm(Model uiModel, DdpAedRule ddpAedRule) {
        uiModel.addAttribute("ddpAedRule", ddpAedRule);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpcommunicationsetups", ddpCommunicationSetupService.findAllDdpCommunicationSetups());
        uiModel.addAttribute("ddpcompressionsetups", ddpCompressionSetupService.findAllDdpCompressionSetups());
        uiModel.addAttribute("ddpdocnameconvs", ddpDocnameConvService.findAllDdpDocnameConvs());
        uiModel.addAttribute("ddpmergesetups", ddpMergeSetupService.findAllDdpMergeSetups());
        uiModel.addAttribute("ddpnotifications", ddpNotificationService.findAllDdpNotifications());
        uiModel.addAttribute("ddprules", ddpRuleService.findAllDdpRules());
        uiModel.addAttribute("ddpschedulers", ddpSchedulerService.findAllDdpSchedulers());
    }
    
    String DdpAedRuleController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
