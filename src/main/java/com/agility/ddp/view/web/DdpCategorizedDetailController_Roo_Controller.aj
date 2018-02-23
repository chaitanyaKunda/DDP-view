// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpCategorizedDetail;
import com.agility.ddp.data.domain.DdpCategorizedDetailService;
import com.agility.ddp.data.domain.DdpDmsDocsTxnService;
import com.agility.ddp.view.web.DdpCategorizedDetailController;
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

privileged aspect DdpCategorizedDetailController_Roo_Controller {
    
    @Autowired
    DdpCategorizedDetailService DdpCategorizedDetailController.ddpCategorizedDetailService;
    
    @Autowired
    DdpDmsDocsTxnService DdpCategorizedDetailController.ddpDmsDocsTxnService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpCategorizedDetailController.create(@Valid DdpCategorizedDetail ddpCategorizedDetail, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpCategorizedDetail);
            return "ddpcategorizeddetails/create";
        }
        uiModel.asMap().clear();
        ddpCategorizedDetailService.saveDdpCategorizedDetail(ddpCategorizedDetail);
        return "redirect:/ddpcategorizeddetails/" + encodeUrlPathSegment(ddpCategorizedDetail.getCadId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpCategorizedDetailController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpCategorizedDetail());
        return "ddpcategorizeddetails/create";
    }
    
    @RequestMapping(value = "/{cadId}", produces = "text/html")
    public String DdpCategorizedDetailController.show(@PathVariable("cadId") Integer cadId, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpcategorizeddetail", ddpCategorizedDetailService.findDdpCategorizedDetail(cadId));
        uiModel.addAttribute("itemId", cadId);
        return "ddpcategorizeddetails/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpCategorizedDetailController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpcategorizeddetails", ddpCategorizedDetailService.findDdpCategorizedDetailEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpCategorizedDetailService.countAllDdpCategorizedDetails() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpcategorizeddetails", ddpCategorizedDetailService.findAllDdpCategorizedDetails());
        }
        addDateTimeFormatPatterns(uiModel);
        return "ddpcategorizeddetails/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpCategorizedDetailController.update(@Valid DdpCategorizedDetail ddpCategorizedDetail, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpCategorizedDetail);
            return "ddpcategorizeddetails/update";
        }
        uiModel.asMap().clear();
        ddpCategorizedDetailService.updateDdpCategorizedDetail(ddpCategorizedDetail);
        return "redirect:/ddpcategorizeddetails/" + encodeUrlPathSegment(ddpCategorizedDetail.getCadId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{cadId}", params = "form", produces = "text/html")
    public String DdpCategorizedDetailController.updateForm(@PathVariable("cadId") Integer cadId, Model uiModel) {
        populateEditForm(uiModel, ddpCategorizedDetailService.findDdpCategorizedDetail(cadId));
        return "ddpcategorizeddetails/update";
    }
    
    @RequestMapping(value = "/{cadId}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpCategorizedDetailController.delete(@PathVariable("cadId") Integer cadId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpCategorizedDetail ddpCategorizedDetail = ddpCategorizedDetailService.findDdpCategorizedDetail(cadId);
        ddpCategorizedDetailService.deleteDdpCategorizedDetail(ddpCategorizedDetail);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpcategorizeddetails";
    }
    
    void DdpCategorizedDetailController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("ddpCategorizedDetail_cadcreateddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpCategorizedDetail_cadmodifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }
    
    void DdpCategorizedDetailController.populateEditForm(Model uiModel, DdpCategorizedDetail ddpCategorizedDetail) {
        uiModel.addAttribute("ddpCategorizedDetail", ddpCategorizedDetail);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpdmsdocstxns", ddpDmsDocsTxnService.findAllDdpDmsDocsTxns());
    }
    
    String DdpCategorizedDetailController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
