package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpMultiAedSuccessReport;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/ddpmultiaedsuccessreports")
@Controller
@RooWebScaffold(path = "ddpmultiaedsuccessreports", formBackingObject = DdpMultiAedSuccessReport.class)
public class DdpMultiAedSuccessReportController {
}
