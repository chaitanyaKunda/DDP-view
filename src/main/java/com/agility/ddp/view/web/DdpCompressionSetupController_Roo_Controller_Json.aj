// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpCompressionSetup;
import com.agility.ddp.view.web.DdpCompressionSetupController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpCompressionSetupController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{ctsCompressionId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpCompressionSetupController.showJson(@PathVariable("ctsCompressionId") Integer ctsCompressionId) {
        DdpCompressionSetup ddpCompressionSetup = ddpCompressionSetupService.findDdpCompressionSetup(ctsCompressionId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpCompressionSetup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpCompressionSetup.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpCompressionSetupController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpCompressionSetup> result = ddpCompressionSetupService.findAllDdpCompressionSetups();
        return new ResponseEntity<String>(DdpCompressionSetup.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCompressionSetupController.createFromJson(@RequestBody String json) {
        DdpCompressionSetup ddpCompressionSetup = DdpCompressionSetup.fromJsonToDdpCompressionSetup(json);
        ddpCompressionSetupService.saveDdpCompressionSetup(ddpCompressionSetup);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCompressionSetupController.createFromJsonArray(@RequestBody String json) {
        for (DdpCompressionSetup ddpCompressionSetup: DdpCompressionSetup.fromJsonArrayToDdpCompressionSetups(json)) {
            ddpCompressionSetupService.saveDdpCompressionSetup(ddpCompressionSetup);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{ctsCompressionId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCompressionSetupController.updateFromJson(@RequestBody String json, @PathVariable("ctsCompressionId") Integer ctsCompressionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpCompressionSetup ddpCompressionSetup = DdpCompressionSetup.fromJsonToDdpCompressionSetup(json);
        if (ddpCompressionSetupService.updateDdpCompressionSetup(ddpCompressionSetup) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{ctsCompressionId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCompressionSetupController.deleteFromJson(@PathVariable("ctsCompressionId") Integer ctsCompressionId) {
        DdpCompressionSetup ddpCompressionSetup = ddpCompressionSetupService.findDdpCompressionSetup(ctsCompressionId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpCompressionSetup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpCompressionSetupService.deleteDdpCompressionSetup(ddpCompressionSetup);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}
