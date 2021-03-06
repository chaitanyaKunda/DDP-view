// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpBranchService;
import com.agility.ddp.data.domain.DdpCommOptionsSetupService;
import com.agility.ddp.data.domain.DdpCommunicationSetupService;
import com.agility.ddp.data.domain.DdpCompanyService;
import com.agility.ddp.data.domain.DdpDoctypeService;
import com.agility.ddp.data.domain.DdpExportVersionSetupService;
import com.agility.ddp.data.domain.DdpGenSourceSetupService;
import com.agility.ddp.data.domain.DdpGenSystemService;
import com.agility.ddp.data.domain.DdpNotificationService;
import com.agility.ddp.data.domain.DdpPartyService;
import com.agility.ddp.data.domain.DdpRateService;
import com.agility.ddp.data.domain.DdpRateSetupService;
import com.agility.ddp.data.domain.DdpRuleDetail;
import com.agility.ddp.data.domain.DdpRuleDetailService;
import com.agility.ddp.data.domain.DdpRuleService;
import com.agility.ddp.data.domain.DdpSchedulerService;
import com.agility.ddp.view.web.DdpRuleDetailController;
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

privileged aspect DdpRuleDetailController_Roo_Controller {
    
    @Autowired
    DdpRuleDetailService DdpRuleDetailController.ddpRuleDetailService;
    
    @Autowired
    DdpBranchService DdpRuleDetailController.ddpBranchService;
    
    @Autowired
    DdpCommOptionsSetupService DdpRuleDetailController.ddpCommOptionsSetupService;
    
    @Autowired
    DdpCommunicationSetupService DdpRuleDetailController.ddpCommunicationSetupService;
    
    @Autowired
    DdpCompanyService DdpRuleDetailController.ddpCompanyService;
    
    @Autowired
    DdpDoctypeService DdpRuleDetailController.ddpDoctypeService;
    
    @Autowired
    DdpExportVersionSetupService DdpRuleDetailController.ddpExportVersionSetupService;
    
    @Autowired
    DdpGenSourceSetupService DdpRuleDetailController.ddpGenSourceSetupService;
    
    @Autowired
    DdpGenSystemService DdpRuleDetailController.ddpGenSystemService;
    
    @Autowired
    DdpNotificationService DdpRuleDetailController.ddpNotificationService;
    
    @Autowired
    DdpPartyService DdpRuleDetailController.ddpPartyService;
    
    @Autowired
    DdpRateService DdpRuleDetailController.ddpRateService;
    
    @Autowired
    DdpRateSetupService DdpRuleDetailController.ddpRateSetupService;
    
    @Autowired
    DdpRuleService DdpRuleDetailController.ddpRuleService;
    
    @Autowired
    DdpSchedulerService DdpRuleDetailController.ddpSchedulerService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpRuleDetailController.create(@Valid DdpRuleDetail ddpRuleDetail, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpRuleDetail);
            return "ddpruledetails/create";
        }
        uiModel.asMap().clear();
        ddpRuleDetailService.saveDdpRuleDetail(ddpRuleDetail);
        return "redirect:/ddpruledetails/" + encodeUrlPathSegment(ddpRuleDetail.getRdtId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpRuleDetailController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpRuleDetail());
        return "ddpruledetails/create";
    }
    
    @RequestMapping(value = "/{rdtId}", produces = "text/html")
    public String DdpRuleDetailController.show(@PathVariable("rdtId") Integer rdtId, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpruledetail", ddpRuleDetailService.findDdpRuleDetail(rdtId));
        uiModel.addAttribute("itemId", rdtId);
        return "ddpruledetails/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpRuleDetailController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpruledetails", ddpRuleDetailService.findDdpRuleDetailEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpRuleDetailService.countAllDdpRuleDetails() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpruledetails", ddpRuleDetailService.findAllDdpRuleDetails());
        }
        addDateTimeFormatPatterns(uiModel);
        return "ddpruledetails/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpRuleDetailController.update(@Valid DdpRuleDetail ddpRuleDetail, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpRuleDetail);
            return "ddpruledetails/update";
        }
        uiModel.asMap().clear();
        ddpRuleDetailService.updateDdpRuleDetail(ddpRuleDetail);
        return "redirect:/ddpruledetails/" + encodeUrlPathSegment(ddpRuleDetail.getRdtId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{rdtId}", params = "form", produces = "text/html")
    public String DdpRuleDetailController.updateForm(@PathVariable("rdtId") Integer rdtId, Model uiModel) {
        populateEditForm(uiModel, ddpRuleDetailService.findDdpRuleDetail(rdtId));
        return "ddpruledetails/update";
    }
    
    @RequestMapping(value = "/{rdtId}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpRuleDetailController.delete(@PathVariable("rdtId") Integer rdtId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpRuleDetail ddpRuleDetail = ddpRuleDetailService.findDdpRuleDetail(rdtId);
        ddpRuleDetailService.deleteDdpRuleDetail(ddpRuleDetail);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpruledetails";
    }
    
    void DdpRuleDetailController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("ddpRuleDetail_rdtcreateddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpRuleDetail_rdtmodifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpRuleDetail_rdtactivationdate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }
    
    void DdpRuleDetailController.populateEditForm(Model uiModel, DdpRuleDetail ddpRuleDetail) {
        uiModel.addAttribute("ddpRuleDetail", ddpRuleDetail);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpbranches", ddpBranchService.findAllDdpBranches());
        uiModel.addAttribute("ddpcommoptionssetups", ddpCommOptionsSetupService.findAllDdpCommOptionsSetups());
        uiModel.addAttribute("ddpcommunicationsetups", ddpCommunicationSetupService.findAllDdpCommunicationSetups());
        uiModel.addAttribute("ddpcompanys", ddpCompanyService.findAllDdpCompanys());
        uiModel.addAttribute("ddpdoctypes", ddpDoctypeService.findAllDdpDoctypes());
        uiModel.addAttribute("ddpexportversionsetups", ddpExportVersionSetupService.findAllDdpExportVersionSetups());
        uiModel.addAttribute("ddpgensourcesetups", ddpGenSourceSetupService.findAllDdpGenSourceSetups());
        uiModel.addAttribute("ddpgensystems", ddpGenSystemService.findAllDdpGenSystems());
        uiModel.addAttribute("ddpnotifications", ddpNotificationService.findAllDdpNotifications());
        uiModel.addAttribute("ddppartys", ddpPartyService.findAllDdpPartys());
        uiModel.addAttribute("ddprates", ddpRateService.findAllDdpRates());
        uiModel.addAttribute("ddpratesetups", ddpRateSetupService.findAllDdpRateSetups());
        uiModel.addAttribute("ddprules", ddpRuleService.findAllDdpRules());
        uiModel.addAttribute("ddpschedulers", ddpSchedulerService.findAllDdpSchedulers());
    }
    
    String DdpRuleDetailController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
