package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpCommOptionsSetup;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@Transactional
@RequestMapping("/ddpcommoptionssetups")
@Controller
@RooWebScaffold(path = "ddpcommoptionssetups", formBackingObject = DdpCommOptionsSetup.class)
@RooWebJson(jsonObject = DdpCommOptionsSetup.class)
public class DdpCommOptionsSetupController {
}
