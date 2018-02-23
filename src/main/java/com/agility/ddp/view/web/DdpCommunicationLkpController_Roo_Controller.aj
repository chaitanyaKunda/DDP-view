// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpCommunicationLkp;
import com.agility.ddp.data.domain.DdpCommunicationLkpService;
import com.agility.ddp.view.web.DdpCommunicationLkpController;
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

privileged aspect DdpCommunicationLkpController_Roo_Controller {
    
    @Autowired
    DdpCommunicationLkpService DdpCommunicationLkpController.ddpCommunicationLkpService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpCommunicationLkpController.create(@Valid DdpCommunicationLkp ddpCommunicationLkp, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpCommunicationLkp);
            return "ddpcommunicationlkps/create";
        }
        uiModel.asMap().clear();
        ddpCommunicationLkpService.saveDdpCommunicationLkp(ddpCommunicationLkp);
        return "redirect:/ddpcommunicationlkps/" + encodeUrlPathSegment(ddpCommunicationLkp.getColCommunicationLkpId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpCommunicationLkpController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpCommunicationLkp());
        return "ddpcommunicationlkps/create";
    }
    
    @RequestMapping(value = "/{colCommunicationLkpId}", produces = "text/html")
    public String DdpCommunicationLkpController.show(@PathVariable("colCommunicationLkpId") Integer colCommunicationLkpId, Model uiModel) {
        uiModel.addAttribute("ddpcommunicationlkp", ddpCommunicationLkpService.findDdpCommunicationLkp(colCommunicationLkpId));
        uiModel.addAttribute("itemId", colCommunicationLkpId);
        return "ddpcommunicationlkps/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpCommunicationLkpController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpcommunicationlkps", ddpCommunicationLkpService.findDdpCommunicationLkpEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpCommunicationLkpService.countAllDdpCommunicationLkps() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpcommunicationlkps", ddpCommunicationLkpService.findAllDdpCommunicationLkps());
        }
        return "ddpcommunicationlkps/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpCommunicationLkpController.update(@Valid DdpCommunicationLkp ddpCommunicationLkp, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpCommunicationLkp);
            return "ddpcommunicationlkps/update";
        }
        uiModel.asMap().clear();
        ddpCommunicationLkpService.updateDdpCommunicationLkp(ddpCommunicationLkp);
        return "redirect:/ddpcommunicationlkps/" + encodeUrlPathSegment(ddpCommunicationLkp.getColCommunicationLkpId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{colCommunicationLkpId}", params = "form", produces = "text/html")
    public String DdpCommunicationLkpController.updateForm(@PathVariable("colCommunicationLkpId") Integer colCommunicationLkpId, Model uiModel) {
        populateEditForm(uiModel, ddpCommunicationLkpService.findDdpCommunicationLkp(colCommunicationLkpId));
        return "ddpcommunicationlkps/update";
    }
    
    @RequestMapping(value = "/{colCommunicationLkpId}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpCommunicationLkpController.delete(@PathVariable("colCommunicationLkpId") Integer colCommunicationLkpId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpCommunicationLkp ddpCommunicationLkp = ddpCommunicationLkpService.findDdpCommunicationLkp(colCommunicationLkpId);
        ddpCommunicationLkpService.deleteDdpCommunicationLkp(ddpCommunicationLkp);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpcommunicationlkps";
    }
    
    void DdpCommunicationLkpController.populateEditForm(Model uiModel, DdpCommunicationLkp ddpCommunicationLkp) {
        uiModel.addAttribute("ddpCommunicationLkp", ddpCommunicationLkp);
    }
    
    String DdpCommunicationLkpController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
