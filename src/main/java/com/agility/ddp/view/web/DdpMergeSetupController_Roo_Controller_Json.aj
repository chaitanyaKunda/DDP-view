// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpMergeSetup;
import com.agility.ddp.view.web.DdpMergeSetupController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpMergeSetupController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{mtsMergeId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpMergeSetupController.showJson(@PathVariable("mtsMergeId") Integer mtsMergeId) {
        DdpMergeSetup ddpMergeSetup = ddpMergeSetupService.findDdpMergeSetup(mtsMergeId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpMergeSetup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpMergeSetup.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpMergeSetupController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpMergeSetup> result = ddpMergeSetupService.findAllDdpMergeSetups();
        return new ResponseEntity<String>(DdpMergeSetup.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpMergeSetupController.createFromJson(@RequestBody String json) {
        DdpMergeSetup ddpMergeSetup = DdpMergeSetup.fromJsonToDdpMergeSetup(json);
        ddpMergeSetupService.saveDdpMergeSetup(ddpMergeSetup);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpMergeSetupController.createFromJsonArray(@RequestBody String json) {
        for (DdpMergeSetup ddpMergeSetup: DdpMergeSetup.fromJsonArrayToDdpMergeSetups(json)) {
            ddpMergeSetupService.saveDdpMergeSetup(ddpMergeSetup);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{mtsMergeId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpMergeSetupController.updateFromJson(@RequestBody String json, @PathVariable("mtsMergeId") Integer mtsMergeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpMergeSetup ddpMergeSetup = DdpMergeSetup.fromJsonToDdpMergeSetup(json);
        if (ddpMergeSetupService.updateDdpMergeSetup(ddpMergeSetup) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{mtsMergeId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpMergeSetupController.deleteFromJson(@PathVariable("mtsMergeId") Integer mtsMergeId) {
        DdpMergeSetup ddpMergeSetup = ddpMergeSetupService.findDdpMergeSetup(mtsMergeId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpMergeSetup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpMergeSetupService.deleteDdpMergeSetup(ddpMergeSetup);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}
