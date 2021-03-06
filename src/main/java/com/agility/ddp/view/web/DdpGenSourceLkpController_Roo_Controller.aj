// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpGenSourceLkp;
import com.agility.ddp.data.domain.DdpGenSourceLkpPK;
import com.agility.ddp.data.domain.DdpGenSourceLkpService;
import com.agility.ddp.view.web.DdpGenSourceLkpController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect DdpGenSourceLkpController_Roo_Controller {
    
    private ConversionService DdpGenSourceLkpController.conversionService;
    
    @Autowired
    DdpGenSourceLkpService DdpGenSourceLkpController.ddpGenSourceLkpService;
    
    @Autowired
    public DdpGenSourceLkpController.new(ConversionService conversionService) {
        super();
        this.conversionService = conversionService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpGenSourceLkpController.create(@Valid DdpGenSourceLkp ddpGenSourceLkp, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpGenSourceLkp);
            return "ddpgensourcelkps/create";
        }
        uiModel.asMap().clear();
        ddpGenSourceLkpService.saveDdpGenSourceLkp(ddpGenSourceLkp);
        return "redirect:/ddpgensourcelkps/" + encodeUrlPathSegment(conversionService.convert(ddpGenSourceLkp.getId(), String.class), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpGenSourceLkpController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpGenSourceLkp());
        return "ddpgensourcelkps/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String DdpGenSourceLkpController.show(@PathVariable("id") DdpGenSourceLkpPK id, Model uiModel) {
        uiModel.addAttribute("ddpgensourcelkp", ddpGenSourceLkpService.findDdpGenSourceLkp(id));
        uiModel.addAttribute("itemId", conversionService.convert(id, String.class));
        return "ddpgensourcelkps/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpGenSourceLkpController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpgensourcelkps", ddpGenSourceLkpService.findDdpGenSourceLkpEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpGenSourceLkpService.countAllDdpGenSourceLkps() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpgensourcelkps", ddpGenSourceLkpService.findAllDdpGenSourceLkps());
        }
        return "ddpgensourcelkps/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpGenSourceLkpController.update(@Valid DdpGenSourceLkp ddpGenSourceLkp, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpGenSourceLkp);
            return "ddpgensourcelkps/update";
        }
        uiModel.asMap().clear();
        ddpGenSourceLkpService.updateDdpGenSourceLkp(ddpGenSourceLkp);
        return "redirect:/ddpgensourcelkps/" + encodeUrlPathSegment(conversionService.convert(ddpGenSourceLkp.getId(), String.class), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String DdpGenSourceLkpController.updateForm(@PathVariable("id") DdpGenSourceLkpPK id, Model uiModel) {
        populateEditForm(uiModel, ddpGenSourceLkpService.findDdpGenSourceLkp(id));
        return "ddpgensourcelkps/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpGenSourceLkpController.delete(@PathVariable("id") DdpGenSourceLkpPK id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpGenSourceLkp ddpGenSourceLkp = ddpGenSourceLkpService.findDdpGenSourceLkp(id);
        ddpGenSourceLkpService.deleteDdpGenSourceLkp(ddpGenSourceLkp);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpgensourcelkps";
    }
    
    void DdpGenSourceLkpController.populateEditForm(Model uiModel, DdpGenSourceLkp ddpGenSourceLkp) {
        uiModel.addAttribute("ddpGenSourceLkp", ddpGenSourceLkp);
    }
    
    String DdpGenSourceLkpController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
