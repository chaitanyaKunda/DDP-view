// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpJobRefHolder;
import com.agility.ddp.data.domain.DdpJobRefHolderPK;
import com.agility.ddp.view.web.DdpJobRefHolderController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpJobRefHolderController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpJobRefHolderController.showJson(@PathVariable("id") DdpJobRefHolderPK id) {
        DdpJobRefHolder ddpJobRefHolder = ddpJobRefHolderService.findDdpJobRefHolder(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpJobRefHolder == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpJobRefHolder.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpJobRefHolderController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpJobRefHolder> result = ddpJobRefHolderService.findAllDdpJobRefHolders();
        return new ResponseEntity<String>(DdpJobRefHolder.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpJobRefHolderController.createFromJson(@RequestBody String json) {
        DdpJobRefHolder ddpJobRefHolder = DdpJobRefHolder.fromJsonToDdpJobRefHolder(json);
        ddpJobRefHolderService.saveDdpJobRefHolder(ddpJobRefHolder);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpJobRefHolderController.createFromJsonArray(@RequestBody String json) {
        for (DdpJobRefHolder ddpJobRefHolder: DdpJobRefHolder.fromJsonArrayToDdpJobRefHolders(json)) {
            ddpJobRefHolderService.saveDdpJobRefHolder(ddpJobRefHolder);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpJobRefHolderController.updateFromJson(@RequestBody String json, @PathVariable("id") DdpJobRefHolderPK id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpJobRefHolder ddpJobRefHolder = DdpJobRefHolder.fromJsonToDdpJobRefHolder(json);
        if (ddpJobRefHolderService.updateDdpJobRefHolder(ddpJobRefHolder) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpJobRefHolderController.deleteFromJson(@PathVariable("id") DdpJobRefHolderPK id) {
        DdpJobRefHolder ddpJobRefHolder = ddpJobRefHolderService.findDdpJobRefHolder(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpJobRefHolder == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpJobRefHolderService.deleteDdpJobRefHolder(ddpJobRefHolder);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}
