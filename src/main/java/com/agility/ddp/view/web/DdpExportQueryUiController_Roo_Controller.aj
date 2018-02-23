// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpExportQueryUi;
import com.agility.ddp.data.domain.DdpExportQueryUiService;
import com.agility.ddp.view.web.DdpExportQueryUiController;
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

privileged aspect DdpExportQueryUiController_Roo_Controller {
    
    @Autowired
    DdpExportQueryUiService DdpExportQueryUiController.ddpExportQueryUiService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpExportQueryUiController.create(@Valid DdpExportQueryUi ddpExportQueryUi, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpExportQueryUi);
            return "ddpexportqueryuis/create";
        }
        uiModel.asMap().clear();
        ddpExportQueryUiService.saveDdpExportQueryUi(ddpExportQueryUi);
        return "redirect:/ddpexportqueryuis/" + encodeUrlPathSegment(ddpExportQueryUi.getEqiId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpExportQueryUiController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpExportQueryUi());
        return "ddpexportqueryuis/create";
    }
    
    @RequestMapping(value = "/{eqiId}", produces = "text/html")
    public String DdpExportQueryUiController.show(@PathVariable("eqiId") Integer eqiId, Model uiModel) {
        uiModel.addAttribute("ddpexportqueryui", ddpExportQueryUiService.findDdpExportQueryUi(eqiId));
        uiModel.addAttribute("itemId", eqiId);
        return "ddpexportqueryuis/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpExportQueryUiController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpexportqueryuis", ddpExportQueryUiService.findDdpExportQueryUiEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpExportQueryUiService.countAllDdpExportQueryUis() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpexportqueryuis", ddpExportQueryUiService.findAllDdpExportQueryUis());
        }
        return "ddpexportqueryuis/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpExportQueryUiController.update(@Valid DdpExportQueryUi ddpExportQueryUi, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpExportQueryUi);
            return "ddpexportqueryuis/update";
        }
        uiModel.asMap().clear();
        ddpExportQueryUiService.updateDdpExportQueryUi(ddpExportQueryUi);
        return "redirect:/ddpexportqueryuis/" + encodeUrlPathSegment(ddpExportQueryUi.getEqiId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{eqiId}", params = "form", produces = "text/html")
    public String DdpExportQueryUiController.updateForm(@PathVariable("eqiId") Integer eqiId, Model uiModel) {
        populateEditForm(uiModel, ddpExportQueryUiService.findDdpExportQueryUi(eqiId));
        return "ddpexportqueryuis/update";
    }
    
    @RequestMapping(value = "/{eqiId}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpExportQueryUiController.delete(@PathVariable("eqiId") Integer eqiId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpExportQueryUi ddpExportQueryUi = ddpExportQueryUiService.findDdpExportQueryUi(eqiId);
        ddpExportQueryUiService.deleteDdpExportQueryUi(ddpExportQueryUi);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpexportqueryuis";
    }
    
    void DdpExportQueryUiController.populateEditForm(Model uiModel, DdpExportQueryUi ddpExportQueryUi) {
        uiModel.addAttribute("ddpExportQueryUi", ddpExportQueryUi);
    }
    
    String DdpExportQueryUiController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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