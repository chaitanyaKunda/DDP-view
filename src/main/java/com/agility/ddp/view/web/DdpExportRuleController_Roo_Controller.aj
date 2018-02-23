// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpCommunicationSetupService;
import com.agility.ddp.data.domain.DdpCompressionSetupService;
import com.agility.ddp.data.domain.DdpDocnameConvService;
import com.agility.ddp.data.domain.DdpExportRule;
import com.agility.ddp.data.domain.DdpExportRuleService;
import com.agility.ddp.data.domain.DdpMergeSetupService;
import com.agility.ddp.data.domain.DdpNotificationService;
import com.agility.ddp.data.domain.DdpRuleService;
import com.agility.ddp.data.domain.DdpSchedulerService;
import com.agility.ddp.data.domain.DdpSourceSystemService;
import com.agility.ddp.view.web.DdpExportRuleController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect DdpExportRuleController_Roo_Controller {
    
    @Autowired
    DdpExportRuleService DdpExportRuleController.ddpExportRuleService;
    
    @Autowired
    DdpCommunicationSetupService DdpExportRuleController.ddpCommunicationSetupService;
    
    @Autowired
    DdpCompressionSetupService DdpExportRuleController.ddpCompressionSetupService;
    
    @Autowired
    DdpDocnameConvService DdpExportRuleController.ddpDocnameConvService;
    
    @Autowired
    DdpMergeSetupService DdpExportRuleController.ddpMergeSetupService;
    
    @Autowired
    DdpNotificationService DdpExportRuleController.ddpNotificationService;
    
    @Autowired
    DdpRuleService DdpExportRuleController.ddpRuleService;
    
    @Autowired
    DdpSchedulerService DdpExportRuleController.ddpSchedulerService;
    
    @Autowired
    DdpSourceSystemService DdpExportRuleController.ddpSourceSystemService;
    
    @RequestMapping(produces = "text/html")
    public String DdpExportRuleController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpexportrules", ddpExportRuleService.findDdpExportRuleEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpExportRuleService.countAllDdpExportRules() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpexportrules", ddpExportRuleService.findAllDdpExportRules());
        }
        addDateTimeFormatPatterns(uiModel);
        return "ddpexportrules/list";
    }
    
    void DdpExportRuleController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("ddpExportRule_expcreateddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpExportRule_expmodifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpExportRule_expactivationdate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }
    
    void DdpExportRuleController.populateEditForm(Model uiModel, DdpExportRule ddpExportRule) {
        uiModel.addAttribute("ddpExportRule", ddpExportRule);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpcommunicationsetups", ddpCommunicationSetupService.findAllDdpCommunicationSetups());
        uiModel.addAttribute("ddpcompressionsetups", ddpCompressionSetupService.findAllDdpCompressionSetups());
        uiModel.addAttribute("ddpdocnameconvs", ddpDocnameConvService.findAllDdpDocnameConvs());
        uiModel.addAttribute("ddpmergesetups", ddpMergeSetupService.findAllDdpMergeSetups());
        uiModel.addAttribute("ddpnotifications", ddpNotificationService.findAllDdpNotifications());
        uiModel.addAttribute("ddprules", ddpRuleService.findAllDdpRules());
        uiModel.addAttribute("ddpschedulers", ddpSchedulerService.findAllDdpSchedulers());
        uiModel.addAttribute("ddpsourcesystems", ddpSourceSystemService.findAllDdpSourceSystems());
    }
    
    String DdpExportRuleController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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