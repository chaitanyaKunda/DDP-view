// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpRate;
import com.agility.ddp.view.web.DdpRateController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpRateController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{rtsId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRateController.showJson(@PathVariable("rtsId") Integer rtsId) {
        DdpRate ddpRate = ddpRateService.findDdpRate(rtsId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpRate == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpRate.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRateController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpRate> result = ddpRateService.findAllDdpRates();
        return new ResponseEntity<String>(DdpRate.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpRateController.createFromJson(@RequestBody String json) {
        DdpRate ddpRate = DdpRate.fromJsonToDdpRate(json);
        ddpRateService.saveDdpRate(ddpRate);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpRateController.createFromJsonArray(@RequestBody String json) {
        for (DdpRate ddpRate: DdpRate.fromJsonArrayToDdpRates(json)) {
            ddpRateService.saveDdpRate(ddpRate);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{rtsId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpRateController.updateFromJson(@RequestBody String json, @PathVariable("rtsId") Integer rtsId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpRate ddpRate = DdpRate.fromJsonToDdpRate(json);
        if (ddpRateService.updateDdpRate(ddpRate) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{rtsId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpRateController.deleteFromJson(@PathVariable("rtsId") Integer rtsId) {
        DdpRate ddpRate = ddpRateService.findDdpRate(rtsId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpRate == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpRateService.deleteDdpRate(ddpRate);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}
