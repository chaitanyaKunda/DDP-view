// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpIntegSystem;
import com.agility.ddp.view.web.DdpIntegSystemController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpIntegSystemController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{insApplicationCode}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpIntegSystemController.showJson(@PathVariable("insApplicationCode") String insApplicationCode) {
        DdpIntegSystem ddpIntegSystem = ddpIntegSystemService.findDdpIntegSystem(insApplicationCode);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpIntegSystem == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpIntegSystem.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpIntegSystemController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpIntegSystem> result = ddpIntegSystemService.findAllDdpIntegSystems();
        return new ResponseEntity<String>(DdpIntegSystem.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpIntegSystemController.createFromJson(@RequestBody String json) {
        DdpIntegSystem ddpIntegSystem = DdpIntegSystem.fromJsonToDdpIntegSystem(json);
        ddpIntegSystemService.saveDdpIntegSystem(ddpIntegSystem);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpIntegSystemController.createFromJsonArray(@RequestBody String json) {
        for (DdpIntegSystem ddpIntegSystem: DdpIntegSystem.fromJsonArrayToDdpIntegSystems(json)) {
            ddpIntegSystemService.saveDdpIntegSystem(ddpIntegSystem);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{insApplicationCode}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpIntegSystemController.updateFromJson(@RequestBody String json, @PathVariable("insApplicationCode") String insApplicationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpIntegSystem ddpIntegSystem = DdpIntegSystem.fromJsonToDdpIntegSystem(json);
        if (ddpIntegSystemService.updateDdpIntegSystem(ddpIntegSystem) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{insApplicationCode}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpIntegSystemController.deleteFromJson(@PathVariable("insApplicationCode") String insApplicationCode) {
        DdpIntegSystem ddpIntegSystem = ddpIntegSystemService.findDdpIntegSystem(insApplicationCode);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpIntegSystem == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpIntegSystemService.deleteDdpIntegSystem(ddpIntegSystem);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}