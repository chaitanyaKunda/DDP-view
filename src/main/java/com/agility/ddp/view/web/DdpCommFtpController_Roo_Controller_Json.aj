// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpCommFtp;
import com.agility.ddp.view.web.DdpCommFtpController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpCommFtpController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{cftFtpId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpCommFtpController.showJson(@PathVariable("cftFtpId") Long cftFtpId) {
        DdpCommFtp ddpCommFtp = ddpCommFtpService.findDdpCommFtp(cftFtpId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpCommFtp == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpCommFtp.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpCommFtpController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpCommFtp> result = ddpCommFtpService.findAllDdpCommFtps();
        return new ResponseEntity<String>(DdpCommFtp.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCommFtpController.createFromJson(@RequestBody String json) {
        DdpCommFtp ddpCommFtp = DdpCommFtp.fromJsonToDdpCommFtp(json);
        ddpCommFtpService.saveDdpCommFtp(ddpCommFtp);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCommFtpController.createFromJsonArray(@RequestBody String json) {
        for (DdpCommFtp ddpCommFtp: DdpCommFtp.fromJsonArrayToDdpCommFtps(json)) {
            ddpCommFtpService.saveDdpCommFtp(ddpCommFtp);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{cftFtpId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCommFtpController.updateFromJson(@RequestBody String json, @PathVariable("cftFtpId") Long cftFtpId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpCommFtp ddpCommFtp = DdpCommFtp.fromJsonToDdpCommFtp(json);
        if (ddpCommFtpService.updateDdpCommFtp(ddpCommFtp) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{cftFtpId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCommFtpController.deleteFromJson(@PathVariable("cftFtpId") Long cftFtpId) {
        DdpCommFtp ddpCommFtp = ddpCommFtpService.findDdpCommFtp(cftFtpId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpCommFtp == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpCommFtpService.deleteDdpCommFtp(ddpCommFtp);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}