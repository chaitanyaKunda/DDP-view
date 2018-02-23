// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpRateLkp;
import com.agility.ddp.data.domain.DdpRateLkpPK;
import com.agility.ddp.view.web.DdpRateLkpController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpRateLkpController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRateLkpController.showJson(@PathVariable("id") DdpRateLkpPK id) {
        DdpRateLkp ddpRateLkp = ddpRateLkpService.findDdpRateLkp(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpRateLkp == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpRateLkp.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRateLkpController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpRateLkp> result = ddpRateLkpService.findAllDdpRateLkps();
        return new ResponseEntity<String>(DdpRateLkp.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpRateLkpController.createFromJson(@RequestBody String json) {
        DdpRateLkp ddpRateLkp = DdpRateLkp.fromJsonToDdpRateLkp(json);
        ddpRateLkpService.saveDdpRateLkp(ddpRateLkp);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpRateLkpController.createFromJsonArray(@RequestBody String json) {
        for (DdpRateLkp ddpRateLkp: DdpRateLkp.fromJsonArrayToDdpRateLkps(json)) {
            ddpRateLkpService.saveDdpRateLkp(ddpRateLkp);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpRateLkpController.updateFromJson(@RequestBody String json, @PathVariable("id") DdpRateLkpPK id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpRateLkp ddpRateLkp = DdpRateLkp.fromJsonToDdpRateLkp(json);
        if (ddpRateLkpService.updateDdpRateLkp(ddpRateLkp) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpRateLkpController.deleteFromJson(@PathVariable("id") DdpRateLkpPK id) {
        DdpRateLkp ddpRateLkp = ddpRateLkpService.findDdpRateLkp(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpRateLkp == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpRateLkpService.deleteDdpRateLkp(ddpRateLkp);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}
