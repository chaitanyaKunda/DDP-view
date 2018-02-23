// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpUser;
import com.agility.ddp.view.web.DdpUserController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpUserController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{usrId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpUserController.showJson(@PathVariable("usrId") Integer usrId) {
        DdpUser ddpUser = ddpUserService.findDdpUser(usrId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpUser == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpUser.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpUserController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpUser> result = ddpUserService.findAllDdpUsers();
        return new ResponseEntity<String>(DdpUser.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpUserController.createFromJson(@RequestBody String json) {
        DdpUser ddpUser = DdpUser.fromJsonToDdpUser(json);
        ddpUserService.saveDdpUser(ddpUser);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpUserController.createFromJsonArray(@RequestBody String json) {
        for (DdpUser ddpUser: DdpUser.fromJsonArrayToDdpUsers(json)) {
            ddpUserService.saveDdpUser(ddpUser);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{usrId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpUserController.updateFromJson(@RequestBody String json, @PathVariable("usrId") Integer usrId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpUser ddpUser = DdpUser.fromJsonToDdpUser(json);
        if (ddpUserService.updateDdpUser(ddpUser) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
        
}