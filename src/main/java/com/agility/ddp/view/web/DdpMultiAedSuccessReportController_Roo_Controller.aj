// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpMultiAedSuccessReport;
import com.agility.ddp.data.domain.DdpMultiAedSuccessReportService;
import com.agility.ddp.view.web.DdpMultiAedSuccessReportController;
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

privileged aspect DdpMultiAedSuccessReportController_Roo_Controller {
    
    @Autowired
    DdpMultiAedSuccessReportService DdpMultiAedSuccessReportController.ddpMultiAedSuccessReportService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpMultiAedSuccessReportController.create(@Valid DdpMultiAedSuccessReport ddpMultiAedSuccessReport, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpMultiAedSuccessReport);
            return "ddpmultiaedsuccessreports/create";
        }
        uiModel.asMap().clear();
        ddpMultiAedSuccessReportService.saveDdpMultiAedSuccessReport(ddpMultiAedSuccessReport);
        return "redirect:/ddpmultiaedsuccessreports/" + encodeUrlPathSegment(ddpMultiAedSuccessReport.getMaedrId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpMultiAedSuccessReportController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpMultiAedSuccessReport());
        return "ddpmultiaedsuccessreports/create";
    }
    
    @RequestMapping(value = "/{maedrId}", produces = "text/html")
    public String DdpMultiAedSuccessReportController.show(@PathVariable("maedrId") Integer maedrId, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpmultiaedsuccessreport", ddpMultiAedSuccessReportService.findDdpMultiAedSuccessReport(maedrId));
        uiModel.addAttribute("itemId", maedrId);
        return "ddpmultiaedsuccessreports/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpMultiAedSuccessReportController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpmultiaedsuccessreports", ddpMultiAedSuccessReportService.findDdpMultiAedSuccessReportEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpMultiAedSuccessReportService.countAllDdpMultiAedSuccessReports() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpmultiaedsuccessreports", ddpMultiAedSuccessReportService.findAllDdpMultiAedSuccessReports());
        }
        addDateTimeFormatPatterns(uiModel);
        return "ddpmultiaedsuccessreports/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpMultiAedSuccessReportController.update(@Valid DdpMultiAedSuccessReport ddpMultiAedSuccessReport, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpMultiAedSuccessReport);
            return "ddpmultiaedsuccessreports/update";
        }
        uiModel.asMap().clear();
        ddpMultiAedSuccessReportService.updateDdpMultiAedSuccessReport(ddpMultiAedSuccessReport);
        return "redirect:/ddpmultiaedsuccessreports/" + encodeUrlPathSegment(ddpMultiAedSuccessReport.getMaedrId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{maedrId}", params = "form", produces = "text/html")
    public String DdpMultiAedSuccessReportController.updateForm(@PathVariable("maedrId") Integer maedrId, Model uiModel) {
        populateEditForm(uiModel, ddpMultiAedSuccessReportService.findDdpMultiAedSuccessReport(maedrId));
        return "ddpmultiaedsuccessreports/update";
    }
    
    @RequestMapping(value = "/{maedrId}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpMultiAedSuccessReportController.delete(@PathVariable("maedrId") Integer maedrId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpMultiAedSuccessReport ddpMultiAedSuccessReport = ddpMultiAedSuccessReportService.findDdpMultiAedSuccessReport(maedrId);
        ddpMultiAedSuccessReportService.deleteDdpMultiAedSuccessReport(ddpMultiAedSuccessReport);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpmultiaedsuccessreports";
    }
    
    void DdpMultiAedSuccessReportController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("ddpMultiAedSuccessReport_maedrdmscreated_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpMultiAedSuccessReport_maedrconsolidatedate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }
    
    void DdpMultiAedSuccessReportController.populateEditForm(Model uiModel, DdpMultiAedSuccessReport ddpMultiAedSuccessReport) {
        uiModel.addAttribute("ddpMultiAedSuccessReport", ddpMultiAedSuccessReport);
        addDateTimeFormatPatterns(uiModel);
    }
    
    String DdpMultiAedSuccessReportController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
