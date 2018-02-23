package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpRateSetup;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@RooWebJson(jsonObject = DdpRateSetup.class)
@Controller
@RequestMapping("/ddpratesetups")
@RooWebScaffold(path = "ddpratesetups", formBackingObject = DdpRateSetup.class)
public class DdpRateSetupController {
}
