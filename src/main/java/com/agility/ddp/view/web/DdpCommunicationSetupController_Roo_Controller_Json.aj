// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpCommunicationSetup;
import com.agility.ddp.view.web.DdpCommunicationSetupController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpCommunicationSetupController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{cmsCommunicationId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpCommunicationSetupController.showJson(@PathVariable("cmsCommunicationId") Integer cmsCommunicationId) {
        DdpCommunicationSetup ddpCommunicationSetup = ddpCommunicationSetupService.findDdpCommunicationSetup(cmsCommunicationId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpCommunicationSetup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpCommunicationSetup.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpCommunicationSetupController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpCommunicationSetup> result = ddpCommunicationSetupService.findAllDdpCommunicationSetups();
        return new ResponseEntity<String>(DdpCommunicationSetup.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCommunicationSetupController.createFromJson(@RequestBody String json) {
        DdpCommunicationSetup ddpCommunicationSetup = DdpCommunicationSetup.fromJsonToDdpCommunicationSetup(json);
        ddpCommunicationSetupService.saveDdpCommunicationSetup(ddpCommunicationSetup);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCommunicationSetupController.createFromJsonArray(@RequestBody String json) {
        for (DdpCommunicationSetup ddpCommunicationSetup: DdpCommunicationSetup.fromJsonArrayToDdpCommunicationSetups(json)) {
            ddpCommunicationSetupService.saveDdpCommunicationSetup(ddpCommunicationSetup);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{cmsCommunicationId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCommunicationSetupController.updateFromJson(@RequestBody String json, @PathVariable("cmsCommunicationId") Integer cmsCommunicationId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpCommunicationSetup ddpCommunicationSetup = DdpCommunicationSetup.fromJsonToDdpCommunicationSetup(json);
        if (ddpCommunicationSetupService.updateDdpCommunicationSetup(ddpCommunicationSetup) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{cmsCommunicationId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCommunicationSetupController.deleteFromJson(@PathVariable("cmsCommunicationId") Integer cmsCommunicationId) {
        DdpCommunicationSetup ddpCommunicationSetup = ddpCommunicationSetupService.findDdpCommunicationSetup(cmsCommunicationId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpCommunicationSetup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpCommunicationSetupService.deleteDdpCommunicationSetup(ddpCommunicationSetup);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}
