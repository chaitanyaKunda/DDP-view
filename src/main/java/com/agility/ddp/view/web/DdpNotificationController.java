package com.agility.ddp.view.web;
import com.agility.ddp.data.domain.DdpNotification;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@Transactional
@RooWebJson(jsonObject = DdpNotification.class)
@Controller
@RequestMapping("/ddpnotifications")
@RooWebScaffold(path = "ddpnotifications", formBackingObject = DdpNotification.class)
public class DdpNotificationController {
}
