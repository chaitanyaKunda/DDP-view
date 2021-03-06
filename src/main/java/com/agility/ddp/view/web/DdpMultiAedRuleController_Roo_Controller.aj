// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpCommunicationSetupService;
import com.agility.ddp.data.domain.DdpCompressionSetupService;
import com.agility.ddp.data.domain.DdpDocnameConvService;
import com.agility.ddp.data.domain.DdpMergeSetupService;
import com.agility.ddp.data.domain.DdpMultiAedRule;
import com.agility.ddp.data.domain.DdpMultiAedRuleService;
import com.agility.ddp.data.domain.DdpNotificationService;
import com.agility.ddp.data.domain.DdpRuleService;
import com.agility.ddp.data.domain.DdpSchedulerService;
import com.agility.ddp.view.web.DdpMultiAedRuleController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect DdpMultiAedRuleController_Roo_Controller {
    
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    void DdpMultiAedRuleController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("ddpMultiAedRule_maedcreateddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpMultiAedRule_maedmodifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpMultiAedRule_maedactivationdate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }
    
        
    String DdpMultiAedRuleController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
