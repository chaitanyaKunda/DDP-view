// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpCompany;
import com.agility.ddp.view.web.DdpCompanyController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpCompanyController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{comCompanyCode}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpCompanyController.showJson(@PathVariable("comCompanyCode") String comCompanyCode) {
        DdpCompany ddpCompany = ddpCompanyService.findDdpCompany(comCompanyCode);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpCompany == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpCompany.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpCompanyController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpCompany> result = ddpCompanyService.findAllDdpCompanys();
        return new ResponseEntity<String>(DdpCompany.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCompanyController.createFromJson(@RequestBody String json) {
        DdpCompany ddpCompany = DdpCompany.fromJsonToDdpCompany(json);
        ddpCompanyService.saveDdpCompany(ddpCompany);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCompanyController.createFromJsonArray(@RequestBody String json) {
        for (DdpCompany ddpCompany: DdpCompany.fromJsonArrayToDdpCompanys(json)) {
            ddpCompanyService.saveDdpCompany(ddpCompany);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{comCompanyCode}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpCompanyController.updateFromJson(@RequestBody String json, @PathVariable("comCompanyCode") String comCompanyCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpCompany ddpCompany = DdpCompany.fromJsonToDdpCompany(json);
        if (ddpCompanyService.updateDdpCompany(ddpCompany) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
        
}
