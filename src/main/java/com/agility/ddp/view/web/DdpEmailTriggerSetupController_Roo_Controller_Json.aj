// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpEmailTriggerSetup;
import com.agility.ddp.view.web.DdpEmailTriggerSetupController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpEmailTriggerSetupController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{etrId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpEmailTriggerSetupController.showJson(@PathVariable("etrId") Integer etrId) {
        DdpEmailTriggerSetup ddpEmailTriggerSetup = ddpEmailTriggerSetupService.findDdpEmailTriggerSetup(etrId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpEmailTriggerSetup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpEmailTriggerSetup.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpEmailTriggerSetupController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpEmailTriggerSetup> result = ddpEmailTriggerSetupService.findAllDdpEmailTriggerSetups();
        return new ResponseEntity<String>(DdpEmailTriggerSetup.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpEmailTriggerSetupController.createFromJson(@RequestBody String json) {
        DdpEmailTriggerSetup ddpEmailTriggerSetup = DdpEmailTriggerSetup.fromJsonToDdpEmailTriggerSetup(json);
        ddpEmailTriggerSetupService.saveDdpEmailTriggerSetup(ddpEmailTriggerSetup);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpEmailTriggerSetupController.createFromJsonArray(@RequestBody String json) {
        for (DdpEmailTriggerSetup ddpEmailTriggerSetup: DdpEmailTriggerSetup.fromJsonArrayToDdpEmailTriggerSetups(json)) {
            ddpEmailTriggerSetupService.saveDdpEmailTriggerSetup(ddpEmailTriggerSetup);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{etrId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpEmailTriggerSetupController.updateFromJson(@RequestBody String json, @PathVariable("etrId") Integer etrId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpEmailTriggerSetup ddpEmailTriggerSetup = DdpEmailTriggerSetup.fromJsonToDdpEmailTriggerSetup(json);
        if (ddpEmailTriggerSetupService.updateDdpEmailTriggerSetup(ddpEmailTriggerSetup) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{etrId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpEmailTriggerSetupController.deleteFromJson(@PathVariable("etrId") Integer etrId) {
        DdpEmailTriggerSetup ddpEmailTriggerSetup = ddpEmailTriggerSetupService.findDdpEmailTriggerSetup(etrId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpEmailTriggerSetup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpEmailTriggerSetupService.deleteDdpEmailTriggerSetup(ddpEmailTriggerSetup);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}