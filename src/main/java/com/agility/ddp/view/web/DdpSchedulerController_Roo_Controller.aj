// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpAedRuleService;
import com.agility.ddp.data.domain.DdpExportRuleService;
import com.agility.ddp.data.domain.DdpMultiAedRuleService;
import com.agility.ddp.data.domain.DdpRuleDetailService;
import com.agility.ddp.data.domain.DdpScheduler;
import com.agility.ddp.data.domain.DdpSchedulerService;
import com.agility.ddp.view.web.DdpSchedulerController;
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

privileged aspect DdpSchedulerController_Roo_Controller {
    
    @Autowired
    DdpSchedulerService DdpSchedulerController.ddpSchedulerService;
    
    @Autowired
    DdpAedRuleService DdpSchedulerController.ddpAedRuleService;
    
    @Autowired
    DdpExportRuleService DdpSchedulerController.ddpExportRuleService;
    
    @Autowired
    DdpMultiAedRuleService DdpSchedulerController.ddpMultiAedRuleService;
    
    @Autowired
    DdpRuleDetailService DdpSchedulerController.ddpRuleDetailService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpSchedulerController.create(@Valid DdpScheduler ddpScheduler, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpScheduler);
            return "ddpschedulers/create";
        }
        uiModel.asMap().clear();
        ddpSchedulerService.saveDdpScheduler(ddpScheduler);
        return "redirect:/ddpschedulers/" + encodeUrlPathSegment(ddpScheduler.getSchId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpSchedulerController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpScheduler());
        return "ddpschedulers/create";
    }
    
    @RequestMapping(value = "/{schId}", produces = "text/html")
    public String DdpSchedulerController.show(@PathVariable("schId") Integer schId, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpscheduler", ddpSchedulerService.findDdpScheduler(schId));
        uiModel.addAttribute("itemId", schId);
        return "ddpschedulers/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpSchedulerController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpschedulers", ddpSchedulerService.findDdpSchedulerEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpSchedulerService.countAllDdpSchedulers() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpschedulers", ddpSchedulerService.findAllDdpSchedulers());
        }
        addDateTimeFormatPatterns(uiModel);
        return "ddpschedulers/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpSchedulerController.update(@Valid DdpScheduler ddpScheduler, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpScheduler);
            return "ddpschedulers/update";
        }
        uiModel.asMap().clear();
        ddpSchedulerService.updateDdpScheduler(ddpScheduler);
        return "redirect:/ddpschedulers/" + encodeUrlPathSegment(ddpScheduler.getSchId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{schId}", params = "form", produces = "text/html")
    public String DdpSchedulerController.updateForm(@PathVariable("schId") Integer schId, Model uiModel) {
        populateEditForm(uiModel, ddpSchedulerService.findDdpScheduler(schId));
        return "ddpschedulers/update";
    }
    
    @RequestMapping(value = "/{schId}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpSchedulerController.delete(@PathVariable("schId") Integer schId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpScheduler ddpScheduler = ddpSchedulerService.findDdpScheduler(schId);
        ddpSchedulerService.deleteDdpScheduler(ddpScheduler);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpschedulers";
    }
    
    void DdpSchedulerController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("ddpScheduler_schstartdate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpScheduler_schenddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpScheduler_schcreateddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpScheduler_schmodifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpScheduler_schlastrun_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpScheduler_schnextrun_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpScheduler_schlastsuccessrun_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpScheduler_schlastfailedrun_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }
    
    void DdpSchedulerController.populateEditForm(Model uiModel, DdpScheduler ddpScheduler) {
        uiModel.addAttribute("ddpScheduler", ddpScheduler);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpaedrules", ddpAedRuleService.findAllDdpAedRules());
        uiModel.addAttribute("ddpexportrules", ddpExportRuleService.findAllDdpExportRules());
        uiModel.addAttribute("ddpmultiaedrules", ddpMultiAedRuleService.findAllDdpMultiAedRules());
        uiModel.addAttribute("ddpruledetails", ddpRuleDetailService.findAllDdpRuleDetails());
    }
    
    String DdpSchedulerController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
