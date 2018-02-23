// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpAedRule;
import com.agility.ddp.data.domain.DdpExportRule;
import com.agility.ddp.data.domain.DdpNotification;
import com.agility.ddp.data.domain.DdpRuleDetail;
import com.agility.ddp.view.web.DdpNotificationController;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpNotificationController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{notId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpNotificationController.showJson(@PathVariable("notId") Integer notId) {
        DdpNotification ddpNotification = ddpNotificationService.findDdpNotification(notId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpNotification == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpNotification.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpNotificationController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpNotification> result = ddpNotificationService.findAllDdpNotifications();
        return new ResponseEntity<String>(DdpNotification.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpNotificationController.createFromJson(@RequestBody String json) {
        DdpNotification ddpNotification = DdpNotification.fromJsonToDdpNotification(json);
        ddpNotificationService.saveDdpNotification(ddpNotification);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpNotificationController.createFromJsonArray(@RequestBody String json) {
        for (DdpNotification ddpNotification: DdpNotification.fromJsonArrayToDdpNotifications(json)) {
            ddpNotificationService.saveDdpNotification(ddpNotification);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{notId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpNotificationController.updateFromJson(@RequestBody String json, @PathVariable("notId") Integer notId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpNotification ddpNotification = DdpNotification.fromJsonToDdpNotification(json);
        if (ddpNotificationService.updateDdpNotification(ddpNotification) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{notId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpNotificationController.deleteFromJson(@PathVariable("notId") Integer notId) {
        DdpNotification ddpNotification = ddpNotificationService.findDdpNotification(notId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpNotification == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpNotificationService.deleteDdpNotification(ddpNotification);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByDdpAedRules", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpNotificationController.jsonFindDdpNotificationsByDdpAedRules(@RequestParam("ddpAedRules") Set<DdpAedRule> ddpAedRules) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpNotification.toJsonArray(DdpNotification.findDdpNotificationsByDdpAedRules(ddpAedRules).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByDdpExportRules", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpNotificationController.jsonFindDdpNotificationsByDdpExportRules(@RequestParam("ddpExportRules") Set<DdpExportRule> ddpExportRules) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpNotification.toJsonArray(DdpNotification.findDdpNotificationsByDdpExportRules(ddpExportRules).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByDdpRuleDetails", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpNotificationController.jsonFindDdpNotificationsByDdpRuleDetails(@RequestParam("ddpRuleDetails") Set<DdpRuleDetail> ddpRuleDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpNotification.toJsonArray(DdpNotification.findDdpNotificationsByDdpRuleDetails(ddpRuleDetails).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByNotCreatedDateBetween", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpNotificationController.jsonFindDdpNotificationsByNotCreatedDateBetween(@RequestParam("minNotCreatedDate") @DateTimeFormat(style = "MM") Calendar minNotCreatedDate, @RequestParam("maxNotCreatedDate") @DateTimeFormat(style = "MM") Calendar maxNotCreatedDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpNotification.toJsonArray(DdpNotification.findDdpNotificationsByNotCreatedDateBetween(minNotCreatedDate, maxNotCreatedDate).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByNotCreatedDateEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpNotificationController.jsonFindDdpNotificationsByNotCreatedDateEquals(@RequestParam("notCreatedDate") @DateTimeFormat(style = "MM") Calendar notCreatedDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpNotification.toJsonArray(DdpNotification.findDdpNotificationsByNotCreatedDateEquals(notCreatedDate).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByNotFailureEmailAddressEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpNotificationController.jsonFindDdpNotificationsByNotFailureEmailAddressEquals(@RequestParam("notFailureEmailAddress") String notFailureEmailAddress) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpNotification.toJsonArray(DdpNotification.findDdpNotificationsByNotFailureEmailAddressEquals(notFailureEmailAddress).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByNotModifiedDateBetween", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpNotificationController.jsonFindDdpNotificationsByNotModifiedDateBetween(@RequestParam("minNotModifiedDate") @DateTimeFormat(style = "MM") Calendar minNotModifiedDate, @RequestParam("maxNotModifiedDate") @DateTimeFormat(style = "MM") Calendar maxNotModifiedDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpNotification.toJsonArray(DdpNotification.findDdpNotificationsByNotModifiedDateBetween(minNotModifiedDate, maxNotModifiedDate).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByNotModifiedDateEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpNotificationController.jsonFindDdpNotificationsByNotModifiedDateEquals(@RequestParam("notModifiedDate") @DateTimeFormat(style = "MM") Calendar notModifiedDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpNotification.toJsonArray(DdpNotification.findDdpNotificationsByNotModifiedDateEquals(notModifiedDate).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByNotStatusEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpNotificationController.jsonFindDdpNotificationsByNotStatusEquals(@RequestParam("notStatus") Integer notStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpNotification.toJsonArray(DdpNotification.findDdpNotificationsByNotStatusEquals(notStatus).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByNotSuccessEmailAddressEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpNotificationController.jsonFindDdpNotificationsByNotSuccessEmailAddressEquals(@RequestParam("notSuccessEmailAddress") String notSuccessEmailAddress) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpNotification.toJsonArray(DdpNotification.findDdpNotificationsByNotSuccessEmailAddressEquals(notSuccessEmailAddress).getResultList()), headers, HttpStatus.OK);
    }
    
}
