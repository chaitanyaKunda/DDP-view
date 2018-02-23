// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpAedRule;
import com.agility.ddp.data.domain.DdpCommunicationSetup;
import com.agility.ddp.data.domain.DdpCompressionSetup;
import com.agility.ddp.data.domain.DdpRule;
import com.agility.ddp.data.domain.DdpScheduler;
import com.agility.ddp.view.web.DdpAedRuleController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpAedRuleController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{aedRuleId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpAedRuleController.showJson(@PathVariable("aedRuleId") Integer aedRuleId) {
        DdpAedRule ddpAedRule = ddpAedRuleService.findDdpAedRule(aedRuleId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpAedRule == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpAedRule.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpAedRuleController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpAedRule> result = ddpAedRuleService.findAllDdpAedRules();
        return new ResponseEntity<String>(DdpAedRule.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpAedRuleController.createFromJson(@RequestBody String json) {
        DdpAedRule ddpAedRule = DdpAedRule.fromJsonToDdpAedRule(json);
        ddpAedRuleService.saveDdpAedRule(ddpAedRule);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpAedRuleController.createFromJsonArray(@RequestBody String json) {
        for (DdpAedRule ddpAedRule: DdpAedRule.fromJsonArrayToDdpAedRules(json)) {
            ddpAedRuleService.saveDdpAedRule(ddpAedRule);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{aedRuleId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpAedRuleController.updateFromJson(@RequestBody String json, @PathVariable("aedRuleId") Integer aedRuleId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpAedRule ddpAedRule = DdpAedRule.fromJsonToDdpAedRule(json);
        if (ddpAedRuleService.updateDdpAedRule(ddpAedRule) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
        
    @RequestMapping(params = "find=ByAedCommunicationId", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpAedRuleController.jsonFindDdpAedRulesByAedCommunicationId(@RequestParam("aedCommunicationId") DdpCommunicationSetup aedCommunicationId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpAedRule.toJsonArray(DdpAedRule.findDdpAedRulesByAedCommunicationId(aedCommunicationId).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByAedCompressionId", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpAedRuleController.jsonFindDdpAedRulesByAedCompressionId(@RequestParam("aedCompressionId") DdpCompressionSetup aedCompressionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpAedRule.toJsonArray(DdpAedRule.findDdpAedRulesByAedCompressionId(aedCompressionId).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByAedSchedulerId", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpAedRuleController.jsonFindDdpAedRulesByAedSchedulerId(@RequestParam("aedSchedulerId") DdpScheduler aedSchedulerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpAedRule.toJsonArray(DdpAedRule.findDdpAedRulesByAedSchedulerId(aedSchedulerId).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByDdpRule", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpAedRuleController.jsonFindDdpAedRulesByDdpRule(@RequestParam("ddpRule") DdpRule ddpRule) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpAedRule.toJsonArray(DdpAedRule.findDdpAedRulesByDdpRule(ddpRule).getResultList()), headers, HttpStatus.OK);
    }
    
}
