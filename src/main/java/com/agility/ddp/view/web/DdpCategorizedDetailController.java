package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpCategorizedDetail;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@Transactional
@RooWebJson(jsonObject = DdpCategorizedDetail.class)
@Controller
@RequestMapping("/ddpcategorizeddetails")
@RooWebScaffold(path = "ddpcategorizeddetails", formBackingObject = DdpCategorizedDetail.class)
public class DdpCategorizedDetailController {
}
