// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpBranch;
import com.agility.ddp.data.domain.DdpBranchService;
import com.agility.ddp.data.domain.DdpRuleDetailService;
import com.agility.ddp.view.web.DdpBranchController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect DdpBranchController_Roo_Controller {
    
    @Autowired
    DdpBranchService DdpBranchController.ddpBranchService;
    
    @Autowired
    DdpRuleDetailService DdpBranchController.ddpRuleDetailService;
    
        
        
        
        
        
    void DdpBranchController.populateEditForm(Model uiModel, DdpBranch ddpBranch) {
        uiModel.addAttribute("ddpBranch", ddpBranch);
        uiModel.addAttribute("ddpruledetails", ddpRuleDetailService.findAllDdpRuleDetails());
    }
    
    String DdpBranchController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
