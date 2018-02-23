// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpDmsDocsHolder;
import com.agility.ddp.data.domain.DdpDmsDocsHolderService;
import com.agility.ddp.view.web.DdpDmsDocsHolderController;
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

privileged aspect DdpDmsDocsHolderController_Roo_Controller {
    
    @Autowired
    DdpDmsDocsHolderService DdpDmsDocsHolderController.ddpDmsDocsHolderService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpDmsDocsHolderController.create(@Valid DdpDmsDocsHolder ddpDmsDocsHolder, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpDmsDocsHolder);
            return "ddpdmsdocsholders/create";
        }
        uiModel.asMap().clear();
        ddpDmsDocsHolderService.saveDdpDmsDocsHolder(ddpDmsDocsHolder);
        return "redirect:/ddpdmsdocsholders/" + encodeUrlPathSegment(ddpDmsDocsHolder.getThlSynId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpDmsDocsHolderController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpDmsDocsHolder());
        return "ddpdmsdocsholders/create";
    }
    
    @RequestMapping(value = "/{thlSynId}", produces = "text/html")
    public String DdpDmsDocsHolderController.show(@PathVariable("thlSynId") Integer thlSynId, Model uiModel) {
        uiModel.addAttribute("ddpdmsdocsholder", ddpDmsDocsHolderService.findDdpDmsDocsHolder(thlSynId));
        uiModel.addAttribute("itemId", thlSynId);
        return "ddpdmsdocsholders/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpDmsDocsHolderController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpdmsdocsholders", ddpDmsDocsHolderService.findDdpDmsDocsHolderEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpDmsDocsHolderService.countAllDdpDmsDocsHolders() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpdmsdocsholders", ddpDmsDocsHolderService.findAllDdpDmsDocsHolders());
        }
        return "ddpdmsdocsholders/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpDmsDocsHolderController.update(@Valid DdpDmsDocsHolder ddpDmsDocsHolder, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpDmsDocsHolder);
            return "ddpdmsdocsholders/update";
        }
        uiModel.asMap().clear();
        ddpDmsDocsHolderService.updateDdpDmsDocsHolder(ddpDmsDocsHolder);
        return "redirect:/ddpdmsdocsholders/" + encodeUrlPathSegment(ddpDmsDocsHolder.getThlSynId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{thlSynId}", params = "form", produces = "text/html")
    public String DdpDmsDocsHolderController.updateForm(@PathVariable("thlSynId") Integer thlSynId, Model uiModel) {
        populateEditForm(uiModel, ddpDmsDocsHolderService.findDdpDmsDocsHolder(thlSynId));
        return "ddpdmsdocsholders/update";
    }
    
    @RequestMapping(value = "/{thlSynId}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpDmsDocsHolderController.delete(@PathVariable("thlSynId") Integer thlSynId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpDmsDocsHolder ddpDmsDocsHolder = ddpDmsDocsHolderService.findDdpDmsDocsHolder(thlSynId);
        ddpDmsDocsHolderService.deleteDdpDmsDocsHolder(ddpDmsDocsHolder);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpdmsdocsholders";
    }
    
    void DdpDmsDocsHolderController.populateEditForm(Model uiModel, DdpDmsDocsHolder ddpDmsDocsHolder) {
        uiModel.addAttribute("ddpDmsDocsHolder", ddpDmsDocsHolder);
    }
    
    String DdpDmsDocsHolderController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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