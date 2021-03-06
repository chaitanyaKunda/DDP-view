// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpDmsDocsHolder;
import com.agility.ddp.view.web.DdpDmsDocsHolderController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpDmsDocsHolderController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{thlSynId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpDmsDocsHolderController.showJson(@PathVariable("thlSynId") Integer thlSynId) {
        DdpDmsDocsHolder ddpDmsDocsHolder = ddpDmsDocsHolderService.findDdpDmsDocsHolder(thlSynId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpDmsDocsHolder == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpDmsDocsHolder.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpDmsDocsHolderController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpDmsDocsHolder> result = ddpDmsDocsHolderService.findAllDdpDmsDocsHolders();
        return new ResponseEntity<String>(DdpDmsDocsHolder.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpDmsDocsHolderController.createFromJson(@RequestBody String json) {
        DdpDmsDocsHolder ddpDmsDocsHolder = DdpDmsDocsHolder.fromJsonToDdpDmsDocsHolder(json);
        ddpDmsDocsHolderService.saveDdpDmsDocsHolder(ddpDmsDocsHolder);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpDmsDocsHolderController.createFromJsonArray(@RequestBody String json) {
        for (DdpDmsDocsHolder ddpDmsDocsHolder: DdpDmsDocsHolder.fromJsonArrayToDdpDmsDocsHolders(json)) {
            ddpDmsDocsHolderService.saveDdpDmsDocsHolder(ddpDmsDocsHolder);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{thlSynId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpDmsDocsHolderController.updateFromJson(@RequestBody String json, @PathVariable("thlSynId") Integer thlSynId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpDmsDocsHolder ddpDmsDocsHolder = DdpDmsDocsHolder.fromJsonToDdpDmsDocsHolder(json);
        if (ddpDmsDocsHolderService.updateDdpDmsDocsHolder(ddpDmsDocsHolder) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{thlSynId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpDmsDocsHolderController.deleteFromJson(@PathVariable("thlSynId") Integer thlSynId) {
        DdpDmsDocsHolder ddpDmsDocsHolder = ddpDmsDocsHolderService.findDdpDmsDocsHolder(thlSynId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpDmsDocsHolder == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpDmsDocsHolderService.deleteDdpDmsDocsHolder(ddpDmsDocsHolder);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}
