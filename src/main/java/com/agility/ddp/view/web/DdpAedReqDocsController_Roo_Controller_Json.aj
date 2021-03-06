// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpAedReqDocs;
import com.agility.ddp.view.web.DdpAedReqDocsController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpAedReqDocsController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{ardMergeId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpAedReqDocsController.showJson(@PathVariable("ardMergeId") Integer ardMergeId) {
        DdpAedReqDocs ddpAedReqDocs = ddpAedReqDocsService.findDdpAedReqDocs(ardMergeId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpAedReqDocs == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpAedReqDocs.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpAedReqDocsController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpAedReqDocs> result = ddpAedReqDocsService.findAllDdpAedReqDocses();
        return new ResponseEntity<String>(DdpAedReqDocs.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpAedReqDocsController.createFromJson(@RequestBody String json) {
        DdpAedReqDocs ddpAedReqDocs = DdpAedReqDocs.fromJsonToDdpAedReqDocs(json);
        ddpAedReqDocsService.saveDdpAedReqDocs(ddpAedReqDocs);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpAedReqDocsController.createFromJsonArray(@RequestBody String json) {
        for (DdpAedReqDocs ddpAedReqDocs: DdpAedReqDocs.fromJsonArrayToDdpAedReqDocses(json)) {
            ddpAedReqDocsService.saveDdpAedReqDocs(ddpAedReqDocs);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{ardMergeId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpAedReqDocsController.updateFromJson(@RequestBody String json, @PathVariable("ardMergeId") Integer ardMergeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpAedReqDocs ddpAedReqDocs = DdpAedReqDocs.fromJsonToDdpAedReqDocs(json);
        if (ddpAedReqDocsService.updateDdpAedReqDocs(ddpAedReqDocs) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{ardMergeId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpAedReqDocsController.deleteFromJson(@PathVariable("ardMergeId") Integer ardMergeId) {
        DdpAedReqDocs ddpAedReqDocs = ddpAedReqDocsService.findDdpAedReqDocs(ardMergeId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpAedReqDocs == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpAedReqDocsService.deleteDdpAedReqDocs(ddpAedReqDocs);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}
