// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpParty;
import com.agility.ddp.data.domain.DdpPartyService;
import com.agility.ddp.data.domain.DdpRuleDetailService;
import com.agility.ddp.view.web.DdpPartyController;
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

privileged aspect DdpPartyController_Roo_Controller {
    
    @Autowired
    DdpPartyService DdpPartyController.ddpPartyService;
    
    @Autowired
    DdpRuleDetailService DdpPartyController.ddpRuleDetailService;
    
        
        
        
        
        
    void DdpPartyController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("ddpParty_ptycreateddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpParty_ptymodifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }
    
    void DdpPartyController.populateEditForm(Model uiModel, DdpParty ddpParty) {
        uiModel.addAttribute("ddpParty", ddpParty);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpruledetails", ddpRuleDetailService.findAllDdpRuleDetails());
    }
    
    String DdpPartyController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
