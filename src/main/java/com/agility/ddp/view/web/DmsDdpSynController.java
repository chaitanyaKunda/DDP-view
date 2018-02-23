package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DmsDdpSyn;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@Transactional
@RequestMapping("/dmsddpsyns")
@Controller
@RooWebScaffold(path = "dmsddpsyns", formBackingObject = DmsDdpSyn.class)
@RooWebJson(jsonObject = DmsDdpSyn.class)
public class DmsDdpSynController {
}
