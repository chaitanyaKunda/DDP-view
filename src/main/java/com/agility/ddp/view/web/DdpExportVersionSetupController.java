package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpExportVersionSetup;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@RooWebJson(jsonObject = DdpExportVersionSetup.class)
@Controller
@RequestMapping("/ddpexportversionsetups")
@RooWebScaffold(path = "ddpexportversionsetups", formBackingObject = DdpExportVersionSetup.class)
public class DdpExportVersionSetupController {
}
