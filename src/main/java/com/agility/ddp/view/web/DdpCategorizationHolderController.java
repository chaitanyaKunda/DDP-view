package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpCategorizationHolder;

import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;


@Transactional
@RooWebJson(jsonObject = DdpCategorizationHolder.class)
@Controller
@RequestMapping("/ddpcategorizationholders")
@RooWebScaffold(path = "ddpcategorizationholders", formBackingObject = DdpCategorizationHolder.class)
public class DdpCategorizationHolderController {
}
