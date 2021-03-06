// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpExportRuleService;
import com.agility.ddp.data.domain.DdpSourceSystem;
import com.agility.ddp.data.domain.DdpSourceSystemDetailService;
import com.agility.ddp.data.domain.DdpSourceSystemService;
import com.agility.ddp.view.web.DdpSourceSystemController;
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

privileged aspect DdpSourceSystemController_Roo_Controller {
    
    @Autowired
    DdpSourceSystemService DdpSourceSystemController.ddpSourceSystemService;
    
    @Autowired
    DdpExportRuleService DdpSourceSystemController.ddpExportRuleService;
    
    @Autowired
    DdpSourceSystemDetailService DdpSourceSystemController.ddpSourceSystemDetailService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpSourceSystemController.create(@Valid DdpSourceSystem ddpSourceSystem, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpSourceSystem);
            return "ddpsourcesystems/create";
        }
        uiModel.asMap().clear();
        ddpSourceSystemService.saveDdpSourceSystem(ddpSourceSystem);
        return "redirect:/ddpsourcesystems/" + encodeUrlPathSegment(ddpSourceSystem.getSouApplicationCode().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpSourceSystemController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpSourceSystem());
        return "ddpsourcesystems/create";
    }
    
    @RequestMapping(value = "/{souApplicationCode}", produces = "text/html")
    public String DdpSourceSystemController.show(@PathVariable("souApplicationCode") String souApplicationCode, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpsourcesystem", ddpSourceSystemService.findDdpSourceSystem(souApplicationCode));
        uiModel.addAttribute("itemId", souApplicationCode);
        return "ddpsourcesystems/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpSourceSystemController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpsourcesystems", ddpSourceSystemService.findDdpSourceSystemEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpSourceSystemService.countAllDdpSourceSystems() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpsourcesystems", ddpSourceSystemService.findAllDdpSourceSystems());
        }
        addDateTimeFormatPatterns(uiModel);
        return "ddpsourcesystems/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpSourceSystemController.update(@Valid DdpSourceSystem ddpSourceSystem, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpSourceSystem);
            return "ddpsourcesystems/update";
        }
        uiModel.asMap().clear();
        ddpSourceSystemService.updateDdpSourceSystem(ddpSourceSystem);
        return "redirect:/ddpsourcesystems/" + encodeUrlPathSegment(ddpSourceSystem.getSouApplicationCode().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{souApplicationCode}", params = "form", produces = "text/html")
    public String DdpSourceSystemController.updateForm(@PathVariable("souApplicationCode") String souApplicationCode, Model uiModel) {
        populateEditForm(uiModel, ddpSourceSystemService.findDdpSourceSystem(souApplicationCode));
        return "ddpsourcesystems/update";
    }
    
    @RequestMapping(value = "/{souApplicationCode}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpSourceSystemController.delete(@PathVariable("souApplicationCode") String souApplicationCode, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpSourceSystem ddpSourceSystem = ddpSourceSystemService.findDdpSourceSystem(souApplicationCode);
        ddpSourceSystemService.deleteDdpSourceSystem(ddpSourceSystem);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpsourcesystems";
    }
    
    void DdpSourceSystemController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("ddpSourceSystem_soucreateddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpSourceSystem_soumodifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }
    
    void DdpSourceSystemController.populateEditForm(Model uiModel, DdpSourceSystem ddpSourceSystem) {
        uiModel.addAttribute("ddpSourceSystem", ddpSourceSystem);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpexportrules", ddpExportRuleService.findAllDdpExportRules());
        uiModel.addAttribute("ddpsourcesystemdetails", ddpSourceSystemDetailService.findAllDdpSourceSystemDetails());
    }
    
    String DdpSourceSystemController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
