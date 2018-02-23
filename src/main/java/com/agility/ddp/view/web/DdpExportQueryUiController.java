package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpExportQueryUi;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@RooWebJson(jsonObject = DdpExportQueryUi.class)
@Controller
@RequestMapping("/ddpexportqueryuis")
@RooWebScaffold(path = "ddpexportqueryuis", formBackingObject = DdpExportQueryUi.class)
public class DdpExportQueryUiController {
}
