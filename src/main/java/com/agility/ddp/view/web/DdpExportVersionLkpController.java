package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpExportVersionLkp;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@RooWebJson(jsonObject = DdpExportVersionLkp.class)
@Controller
@RequestMapping("/ddpexportversionlkps")
@RooWebScaffold(path = "ddpexportversionlkps", formBackingObject = DdpExportVersionLkp.class)
public class DdpExportVersionLkpController {
}
