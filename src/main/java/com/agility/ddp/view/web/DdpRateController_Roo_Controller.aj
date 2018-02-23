// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpRate;
import com.agility.ddp.data.domain.DdpRateService;
import com.agility.ddp.data.domain.DdpRuleDetailService;
import com.agility.ddp.view.web.DdpRateController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect DdpRateController_Roo_Controller {
    
    @Autowired
    DdpRateService DdpRateController.ddpRateService;
    
    @Autowired
    DdpRuleDetailService DdpRateController.ddpRuleDetailService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpRateController.create(@Valid DdpRate ddpRate, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpRate);
            return "ddprates/create";
        }
        uiModel.asMap().clear();
        ddpRateService.saveDdpRate(ddpRate);
        return "redirect:/ddprates/" + encodeUrlPathSegment(ddpRate.getRtsId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpRateController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpRate());
        return "ddprates/create";
    }
    
    @RequestMapping(value = "/{rtsId}", produces = "text/html")
    public String DdpRateController.show(@PathVariable("rtsId") Integer rtsId, Model uiModel) {
        uiModel.addAttribute("ddprate", ddpRateService.findDdpRate(rtsId));
        uiModel.addAttribute("itemId", rtsId);
        return "ddprates/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpRateController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddprates", ddpRateService.findDdpRateEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpRateService.countAllDdpRates() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddprates", ddpRateService.findAllDdpRates());
        }
        return "ddprates/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpRateController.update(@Valid DdpRate ddpRate, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpRate);
            return "ddprates/update";
        }
        uiModel.asMap().clear();
        ddpRateService.updateDdpRate(ddpRate);
        return "redirect:/ddprates/" + encodeUrlPathSegment(ddpRate.getRtsId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{rtsId}", params = "form", produces = "text/html")
    public String DdpRateController.updateForm(@PathVariable("rtsId") Integer rtsId, Model uiModel) {
        populateEditForm(uiModel, ddpRateService.findDdpRate(rtsId));
        return "ddprates/update";
    }
    
    @RequestMapping(value = "/{rtsId}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpRateController.delete(@PathVariable("rtsId") Integer rtsId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpRate ddpRate = ddpRateService.findDdpRate(rtsId);
        ddpRateService.deleteDdpRate(ddpRate);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddprates";
    }
    
    void DdpRateController.populateEditForm(Model uiModel, DdpRate ddpRate) {
        uiModel.addAttribute("ddpRate", ddpRate);
        uiModel.addAttribute("ddpruledetails", ddpRuleDetailService.findAllDdpRuleDetails());
    }
    
    String DdpRateController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
