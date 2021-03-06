// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpEmailAttachment;
import com.agility.ddp.view.web.DdpEmailAttachmentController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpEmailAttachmentController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{eatId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpEmailAttachmentController.showJson(@PathVariable("eatId") Integer eatId) {
        DdpEmailAttachment ddpEmailAttachment = ddpEmailAttachmentService.findDdpEmailAttachment(eatId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpEmailAttachment == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpEmailAttachment.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpEmailAttachmentController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpEmailAttachment> result = ddpEmailAttachmentService.findAllDdpEmailAttachments();
        return new ResponseEntity<String>(DdpEmailAttachment.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpEmailAttachmentController.createFromJson(@RequestBody String json) {
        DdpEmailAttachment ddpEmailAttachment = DdpEmailAttachment.fromJsonToDdpEmailAttachment(json);
        ddpEmailAttachmentService.saveDdpEmailAttachment(ddpEmailAttachment);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpEmailAttachmentController.createFromJsonArray(@RequestBody String json) {
        for (DdpEmailAttachment ddpEmailAttachment: DdpEmailAttachment.fromJsonArrayToDdpEmailAttachments(json)) {
            ddpEmailAttachmentService.saveDdpEmailAttachment(ddpEmailAttachment);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{eatId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpEmailAttachmentController.updateFromJson(@RequestBody String json, @PathVariable("eatId") Integer eatId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpEmailAttachment ddpEmailAttachment = DdpEmailAttachment.fromJsonToDdpEmailAttachment(json);
        if (ddpEmailAttachmentService.updateDdpEmailAttachment(ddpEmailAttachment) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{eatId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpEmailAttachmentController.deleteFromJson(@PathVariable("eatId") Integer eatId) {
        DdpEmailAttachment ddpEmailAttachment = ddpEmailAttachmentService.findDdpEmailAttachment(eatId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpEmailAttachment == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpEmailAttachmentService.deleteDdpEmailAttachment(ddpEmailAttachment);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}
