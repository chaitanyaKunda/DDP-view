package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpCommOptionsLkp;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@Transactional
@RequestMapping("/ddpcommoptionslkps")
@Controller
@RooWebScaffold(path = "ddpcommoptionslkps", formBackingObject = DdpCommOptionsLkp.class)
@RooWebJson(jsonObject = DdpCommOptionsLkp.class)
public class DdpCommOptionsLkpController {
}
