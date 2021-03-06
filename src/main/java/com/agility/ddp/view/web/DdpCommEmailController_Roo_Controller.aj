// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpCommEmail;
import com.agility.ddp.data.domain.DdpCommEmailService;
import com.agility.ddp.view.web.DdpCommEmailController;
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

privileged aspect DdpCommEmailController_Roo_Controller {
    
    @Autowired
    DdpCommEmailService DdpCommEmailController.ddpCommEmailService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpCommEmailController.create(@Valid DdpCommEmail ddpCommEmail, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpCommEmail);
            return "ddpcommemails/create";
        }
        uiModel.asMap().clear();
        ddpCommEmailService.saveDdpCommEmail(ddpCommEmail);
        return "redirect:/ddpcommemails/" + encodeUrlPathSegment(ddpCommEmail.getCemEmailId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpCommEmailController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpCommEmail());
        return "ddpcommemails/create";
    }
    
    @RequestMapping(value = "/{cemEmailId}", produces = "text/html")
    public String DdpCommEmailController.show(@PathVariable("cemEmailId") Integer cemEmailId, Model uiModel) {
        uiModel.addAttribute("ddpcommemail", ddpCommEmailService.findDdpCommEmail(cemEmailId));
        uiModel.addAttribute("itemId", cemEmailId);
        return "ddpcommemails/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpCommEmailController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpcommemails", ddpCommEmailService.findDdpCommEmailEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpCommEmailService.countAllDdpCommEmails() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpcommemails", ddpCommEmailService.findAllDdpCommEmails());
        }
        return "ddpcommemails/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpCommEmailController.update(@Valid DdpCommEmail ddpCommEmail, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpCommEmail);
            return "ddpcommemails/update";
        }
        uiModel.asMap().clear();
        ddpCommEmailService.updateDdpCommEmail(ddpCommEmail);
        return "redirect:/ddpcommemails/" + encodeUrlPathSegment(ddpCommEmail.getCemEmailId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{cemEmailId}", params = "form", produces = "text/html")
    public String DdpCommEmailController.updateForm(@PathVariable("cemEmailId") Integer cemEmailId, Model uiModel) {
        populateEditForm(uiModel, ddpCommEmailService.findDdpCommEmail(cemEmailId));
        return "ddpcommemails/update";
    }
    
    @RequestMapping(value = "/{cemEmailId}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpCommEmailController.delete(@PathVariable("cemEmailId") Integer cemEmailId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpCommEmail ddpCommEmail = ddpCommEmailService.findDdpCommEmail(cemEmailId);
        ddpCommEmailService.deleteDdpCommEmail(ddpCommEmail);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpcommemails";
    }
    
    void DdpCommEmailController.populateEditForm(Model uiModel, DdpCommEmail ddpCommEmail) {
        uiModel.addAttribute("ddpCommEmail", ddpCommEmail);
    }
    
    String DdpCommEmailController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
