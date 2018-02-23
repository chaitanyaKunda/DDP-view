package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpExportMissingDocs;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@RooWebJson(jsonObject = DdpExportMissingDocs.class)
@Controller
@RequestMapping("/ddpexportmissingdocses")
@RooWebScaffold(path = "ddpexportmissingdocses", formBackingObject = DdpExportMissingDocs.class)
public class DdpExportMissingDocsController {
}
