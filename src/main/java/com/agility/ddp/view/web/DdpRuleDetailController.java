package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpRuleDetail;

import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@Transactional
@RooWebJson(jsonObject = DdpRuleDetail.class)
@Controller
@RequestMapping("/ddpruledetails")
@RooWebScaffold(path = "ddpruledetails", formBackingObject = DdpRuleDetail.class)
public class DdpRuleDetailController {
}
