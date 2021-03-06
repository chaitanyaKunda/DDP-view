// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpCommOptionsLkp;
import com.agility.ddp.data.domain.DdpCommOptionsLkpPK;
import com.agility.ddp.view.web.DdpCommOptionsLkpController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpCommOptionsLkpController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpCommOptionsLkpController.showJson(@PathVariable("id") DdpCommOptionsLkpPK id) {
        DdpCommOptionsLkp ddpCommOptionsLkp = ddpCommOptionsLkpService.findDdpCommOptionsLkp(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpCommOptionsLkp == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpCommOptionsLkp.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpCommOptionsLkpController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpCommOptionsLkp> result = ddpCommOptionsLkpService.findAllDdpCommOptionsLkps();
        return new ResponseEntity<String>(DdpCommOptionsLkp.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCommOptionsLkpController.createFromJson(@RequestBody String json) {
        DdpCommOptionsLkp ddpCommOptionsLkp = DdpCommOptionsLkp.fromJsonToDdpCommOptionsLkp(json);
        ddpCommOptionsLkpService.saveDdpCommOptionsLkp(ddpCommOptionsLkp);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCommOptionsLkpController.createFromJsonArray(@RequestBody String json) {
        for (DdpCommOptionsLkp ddpCommOptionsLkp: DdpCommOptionsLkp.fromJsonArrayToDdpCommOptionsLkps(json)) {
            ddpCommOptionsLkpService.saveDdpCommOptionsLkp(ddpCommOptionsLkp);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCommOptionsLkpController.updateFromJson(@RequestBody String json, @PathVariable("id") DdpCommOptionsLkpPK id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpCommOptionsLkp ddpCommOptionsLkp = DdpCommOptionsLkp.fromJsonToDdpCommOptionsLkp(json);
        if (ddpCommOptionsLkpService.updateDdpCommOptionsLkp(ddpCommOptionsLkp) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCommOptionsLkpController.deleteFromJson(@PathVariable("id") DdpCommOptionsLkpPK id) {
        DdpCommOptionsLkp ddpCommOptionsLkp = ddpCommOptionsLkpService.findDdpCommOptionsLkp(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpCommOptionsLkp == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpCommOptionsLkpService.deleteDdpCommOptionsLkp(ddpCommOptionsLkp);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}
