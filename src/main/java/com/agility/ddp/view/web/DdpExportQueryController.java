package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpExportQuery;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = DdpExportQuery.class)
@Controller
@RequestMapping("/ddpexportquerys")
public class DdpExportQueryController {
}
