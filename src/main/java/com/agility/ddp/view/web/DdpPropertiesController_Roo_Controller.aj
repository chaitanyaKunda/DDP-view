// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpProperties;
import com.agility.ddp.data.domain.DdpPropertiesPK;
import com.agility.ddp.data.domain.DdpPropertiesService;
import com.agility.ddp.view.web.DdpPropertiesController;
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

privileged aspect DdpPropertiesController_Roo_Controller {
    
    private ConversionService DdpPropertiesController.conversionService;
    
    @Autowired
    DdpPropertiesService DdpPropertiesController.ddpPropertiesService;
    
    @Autowired
    public DdpPropertiesController.new(ConversionService conversionService) {
        super();
        this.conversionService = conversionService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DdpPropertiesController.create(@Valid DdpProperties ddpProperties, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpProperties);
            return "ddppropertieses/create";
        }
        uiModel.asMap().clear();
        ddpPropertiesService.saveDdpProperties(ddpProperties);
        return "redirect:/ddppropertieses/" + encodeUrlPathSegment(conversionService.convert(ddpProperties.getId(), String.class), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DdpPropertiesController.createForm(Model uiModel) {
        populateEditForm(uiModel, new DdpProperties());
        return "ddppropertieses/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String DdpPropertiesController.show(@PathVariable("id") DdpPropertiesPK id, Model uiModel) {
        uiModel.addAttribute("ddpproperties", ddpPropertiesService.findDdpProperties(id));
        uiModel.addAttribute("itemId", conversionService.convert(id, String.class));
        return "ddppropertieses/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DdpPropertiesController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddppropertieses", ddpPropertiesService.findDdpPropertiesEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpPropertiesService.countAllDdpPropertieses() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddppropertieses", ddpPropertiesService.findAllDdpPropertieses());
        }
        return "ddppropertieses/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DdpPropertiesController.update(@Valid DdpProperties ddpProperties, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpProperties);
            return "ddppropertieses/update";
        }
        uiModel.asMap().clear();
        ddpPropertiesService.updateDdpProperties(ddpProperties);
        return "redirect:/ddppropertieses/" + encodeUrlPathSegment(conversionService.convert(ddpProperties.getId(), String.class), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String DdpPropertiesController.updateForm(@PathVariable("id") DdpPropertiesPK id, Model uiModel) {
        populateEditForm(uiModel, ddpPropertiesService.findDdpProperties(id));
        return "ddppropertieses/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String DdpPropertiesController.delete(@PathVariable("id") DdpPropertiesPK id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpProperties ddpProperties = ddpPropertiesService.findDdpProperties(id);
        ddpPropertiesService.deleteDdpProperties(ddpProperties);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddppropertieses";
    }
    
    void DdpPropertiesController.populateEditForm(Model uiModel, DdpProperties ddpProperties) {
        uiModel.addAttribute("ddpProperties", ddpProperties);
    }
    
    String DdpPropertiesController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
