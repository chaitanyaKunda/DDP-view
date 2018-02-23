package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpExportSuccessReport;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@RooWebJson(jsonObject = DdpExportSuccessReport.class)
@Controller
@RequestMapping("/ddpexportsuccessreports")
@RooWebScaffold(path = "ddpexportsuccessreports", formBackingObject = DdpExportSuccessReport.class)
public class DdpExportSuccessReportController { 
}
