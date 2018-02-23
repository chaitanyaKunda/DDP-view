// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpRateLkp;
import com.agility.ddp.data.domain.DdpRateLkpPK;
import com.agility.ddp.data.domain.DdpRateLkpService;
import com.agility.ddp.view.web.DdpRateLkpController;
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

privileged aspect DdpRateLkpController_Roo_Controller {
    
    private ConversionService DdpRateLkpController.conversionService;
    
    @Autowired
    DdpRateLkpService DdpRateLkpController.ddpRateLkpService;
    
    @Autowired
    public DdpRateLkpController.new(ConversionService conversionService) {
        super();
        this.conversionService = conversionService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpRateLkpController.create(@Valid DdpRateLkp ddpRateLkp, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpRateLkp);
            return "ddpratelkps/create";
        }
        uiModel.asMap().clear();
        ddpRateLkpService.saveDdpRateLkp(ddpRateLkp);
        return "redirect:/ddpratelkps/" + encodeUrlPathSegment(conversionService.convert(ddpRateLkp.getId(), String.class), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpRateLkpController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpRateLkp());
        return "ddpratelkps/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String DdpRateLkpController.show(@PathVariable("id") DdpRateLkpPK id, Model uiModel) {
        uiModel.addAttribute("ddpratelkp", ddpRateLkpService.findDdpRateLkp(id));
        uiModel.addAttribute("itemId", conversionService.convert(id, String.class));
        return "ddpratelkps/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpRateLkpController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpratelkps", ddpRateLkpService.findDdpRateLkpEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpRateLkpService.countAllDdpRateLkps() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpratelkps", ddpRateLkpService.findAllDdpRateLkps());
        }
        return "ddpratelkps/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpRateLkpController.update(@Valid DdpRateLkp ddpRateLkp, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpRateLkp);
            return "ddpratelkps/update";
        }
        uiModel.asMap().clear();
        ddpRateLkpService.updateDdpRateLkp(ddpRateLkp);
        return "redirect:/ddpratelkps/" + encodeUrlPathSegment(conversionService.convert(ddpRateLkp.getId(), String.class), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String DdpRateLkpController.updateForm(@PathVariable("id") DdpRateLkpPK id, Model uiModel) {
        populateEditForm(uiModel, ddpRateLkpService.findDdpRateLkp(id));
        return "ddpratelkps/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpRateLkpController.delete(@PathVariable("id") DdpRateLkpPK id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpRateLkp ddpRateLkp = ddpRateLkpService.findDdpRateLkp(id);
        ddpRateLkpService.deleteDdpRateLkp(ddpRateLkp);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpratelkps";
    }
    
    void DdpRateLkpController.populateEditForm(Model uiModel, DdpRateLkp ddpRateLkp) {
        uiModel.addAttribute("ddpRateLkp", ddpRateLkp);
    }
    
    String DdpRateLkpController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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