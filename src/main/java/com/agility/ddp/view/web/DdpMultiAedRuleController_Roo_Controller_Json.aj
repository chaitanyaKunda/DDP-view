// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpMultiAedRule;
import com.agility.ddp.view.web.DdpMultiAedRuleController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpMultiAedRuleController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{maedRuleId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpMultiAedRuleController.showJson(@PathVariable("maedRuleId") Integer maedRuleId) {
        DdpMultiAedRule ddpMultiAedRule = ddpMultiAedRuleService.findDdpMultiAedRule(maedRuleId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpMultiAedRule == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpMultiAedRule.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpMultiAedRuleController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpMultiAedRule> result = ddpMultiAedRuleService.findAllDdpMultiAedRules();
        return new ResponseEntity<String>(DdpMultiAedRule.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpMultiAedRuleController.createFromJson(@RequestBody String json) {
        DdpMultiAedRule ddpMultiAedRule = DdpMultiAedRule.fromJsonToDdpMultiAedRule(json);
        ddpMultiAedRuleService.saveDdpMultiAedRule(ddpMultiAedRule);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpMultiAedRuleController.createFromJsonArray(@RequestBody String json) {
        for (DdpMultiAedRule ddpMultiAedRule: DdpMultiAedRule.fromJsonArrayToDdpMultiAedRules(json)) {
            ddpMultiAedRuleService.saveDdpMultiAedRule(ddpMultiAedRule);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{maedRuleId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpMultiAedRuleController.updateFromJson(@RequestBody String json, @PathVariable("maedRuleId") Integer maedRuleId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpMultiAedRule ddpMultiAedRule = DdpMultiAedRule.fromJsonToDdpMultiAedRule(json);
        if (ddpMultiAedRuleService.updateDdpMultiAedRule(ddpMultiAedRule) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
        
}
