// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpCommFtp;
import com.agility.ddp.data.domain.DdpCommFtpService;
import com.agility.ddp.view.web.DdpCommFtpController;
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

privileged aspect DdpCommFtpController_Roo_Controller {
    
    @Autowired
    DdpCommFtpService DdpCommFtpController.ddpCommFtpService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpCommFtpController.create(@Valid DdpCommFtp ddpCommFtp, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpCommFtp);
            return "ddpcommftps/create";
        }
        uiModel.asMap().clear();
        ddpCommFtpService.saveDdpCommFtp(ddpCommFtp);
        return "redirect:/ddpcommftps/" + encodeUrlPathSegment(ddpCommFtp.getCftFtpId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpCommFtpController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpCommFtp());
        return "ddpcommftps/create";
    }
    
    @RequestMapping(value = "/{cftFtpId}", produces = "text/html")
    public String DdpCommFtpController.show(@PathVariable("cftFtpId") Long cftFtpId, Model uiModel) {
        uiModel.addAttribute("ddpcommftp", ddpCommFtpService.findDdpCommFtp(cftFtpId));
        uiModel.addAttribute("itemId", cftFtpId);
        return "ddpcommftps/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpCommFtpController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpcommftps", ddpCommFtpService.findDdpCommFtpEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpCommFtpService.countAllDdpCommFtps() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpcommftps", ddpCommFtpService.findAllDdpCommFtps());
        }
        return "ddpcommftps/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpCommFtpController.update(@Valid DdpCommFtp ddpCommFtp, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpCommFtp);
            return "ddpcommftps/update";
        }
        uiModel.asMap().clear();
        ddpCommFtpService.updateDdpCommFtp(ddpCommFtp);
        return "redirect:/ddpcommftps/" + encodeUrlPathSegment(ddpCommFtp.getCftFtpId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{cftFtpId}", params = "form", produces = "text/html")
    public String DdpCommFtpController.updateForm(@PathVariable("cftFtpId") Long cftFtpId, Model uiModel) {
        populateEditForm(uiModel, ddpCommFtpService.findDdpCommFtp(cftFtpId));
        return "ddpcommftps/update";
    }
    
    @RequestMapping(value = "/{cftFtpId}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpCommFtpController.delete(@PathVariable("cftFtpId") Long cftFtpId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpCommFtp ddpCommFtp = ddpCommFtpService.findDdpCommFtp(cftFtpId);
        ddpCommFtpService.deleteDdpCommFtp(ddpCommFtp);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpcommftps";
    }
    
    void DdpCommFtpController.populateEditForm(Model uiModel, DdpCommFtp ddpCommFtp) {
        uiModel.addAttribute("ddpCommFtp", ddpCommFtp);
    }
    
    String DdpCommFtpController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
