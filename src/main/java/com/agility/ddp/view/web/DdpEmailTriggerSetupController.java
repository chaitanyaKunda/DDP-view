package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpEmailTriggerSetup;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@RooWebJson(jsonObject = DdpEmailTriggerSetup.class)
@Controller
@RequestMapping("/ddpemailtriggersetups")
@RooWebScaffold(path = "ddpemailtriggersetups", formBackingObject = DdpEmailTriggerSetup.class)
public class DdpEmailTriggerSetupController {
}
