package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpDmsDocProp;

import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@Transactional
@RooWebJson(jsonObject = DdpDmsDocProp.class)
@Controller
@RequestMapping("/ddpdmsdocprops")
@RooWebScaffold(path = "ddpdmsdocprops", formBackingObject = DdpDmsDocProp.class)
public class DdpDmsDocPropController {
}
