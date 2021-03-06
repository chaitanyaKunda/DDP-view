// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.agility.ddp.view.web;

import com.agility.ddp.data.domain.DdpBranch;
import com.agility.ddp.data.domain.DdpCommOptionsSetup;
import com.agility.ddp.data.domain.DdpCommunicationSetup;
import com.agility.ddp.data.domain.DdpCompany;
import com.agility.ddp.data.domain.DdpDoctype;
import com.agility.ddp.data.domain.DdpGenSystem;
import com.agility.ddp.data.domain.DdpNotification;
import com.agility.ddp.data.domain.DdpParty;
import com.agility.ddp.data.domain.DdpRule;
import com.agility.ddp.data.domain.DdpRuleDetail;
import com.agility.ddp.view.web.DdpRuleDetailController;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect DdpRuleDetailController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{rdtId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRuleDetailController.showJson(@PathVariable("rdtId") Integer rdtId) {
        DdpRuleDetail ddpRuleDetail = ddpRuleDetailService.findDdpRuleDetail(rdtId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (ddpRuleDetail == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ddpRuleDetail.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRuleDetailController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DdpRuleDetail> result = ddpRuleDetailService.findAllDdpRuleDetails();
        return new ResponseEntity<String>(DdpRuleDetail.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpRuleDetailController.createFromJson(@RequestBody String json) {
        DdpRuleDetail ddpRuleDetail = DdpRuleDetail.fromJsonToDdpRuleDetail(json);
        ddpRuleDetailService.saveDdpRuleDetail(ddpRuleDetail);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> DdpRuleDetailController.createFromJsonArray(@RequestBody String json) {
        for (DdpRuleDetail ddpRuleDetail: DdpRuleDetail.fromJsonArrayToDdpRuleDetails(json)) {
            ddpRuleDetailService.saveDdpRuleDetail(ddpRuleDetail);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{rdtId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> DdpRuleDetailController.updateFromJson(@RequestBody String json, @PathVariable("rdtId") Integer rdtId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DdpRuleDetail ddpRuleDetail = DdpRuleDetail.fromJsonToDdpRuleDetail(json);
        if (ddpRuleDetailService.updateDdpRuleDetail(ddpRuleDetail) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{rdtId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> DdpRuleDetailController.deleteFromJson(@PathVariable("rdtId") Integer rdtId) {
        DdpRuleDetail ddpRuleDetail = ddpRuleDetailService.findDdpRuleDetail(rdtId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpRuleDetail == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpRuleDetailService.deleteDdpRuleDetail(ddpRuleDetail);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByDdpCommOptionsSetups", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRuleDetailController.jsonFindDdpRuleDetailsByDdpCommOptionsSetups(@RequestParam("ddpCommOptionsSetups") Set<DdpCommOptionsSetup> ddpCommOptionsSetups) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpRuleDetail.toJsonArray(DdpRuleDetail.findDdpRuleDetailsByDdpCommOptionsSetups(ddpCommOptionsSetups).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByRdtBranch", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRuleDetailController.jsonFindDdpRuleDetailsByRdtBranch(@RequestParam("rdtBranch") DdpBranch rdtBranch) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpRuleDetail.toJsonArray(DdpRuleDetail.findDdpRuleDetailsByRdtBranch(rdtBranch).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByRdtCommunicationId", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRuleDetailController.jsonFindDdpRuleDetailsByRdtCommunicationId(@RequestParam("rdtCommunicationId") DdpCommunicationSetup rdtCommunicationId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpRuleDetail.toJsonArray(DdpRuleDetail.findDdpRuleDetailsByRdtCommunicationId(rdtCommunicationId).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByRdtCompany", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRuleDetailController.jsonFindDdpRuleDetailsByRdtCompany(@RequestParam("rdtCompany") DdpCompany rdtCompany) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpRuleDetail.toJsonArray(DdpRuleDetail.findDdpRuleDetailsByRdtCompany(rdtCompany).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByRdtDocType", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRuleDetailController.jsonFindDdpRuleDetailsByRdtDocType(@RequestParam("rdtDocType") DdpDoctype rdtDocType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpRuleDetail.toJsonArray(DdpRuleDetail.findDdpRuleDetailsByRdtDocType(rdtDocType).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByRdtGenSystem", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRuleDetailController.jsonFindDdpRuleDetailsByRdtGenSystem(@RequestParam("rdtGenSystem") DdpGenSystem rdtGenSystem) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpRuleDetail.toJsonArray(DdpRuleDetail.findDdpRuleDetailsByRdtGenSystem(rdtGenSystem).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByRdtNotificationId", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRuleDetailController.jsonFindDdpRuleDetailsByRdtNotificationId(@RequestParam("rdtNotificationId") DdpNotification rdtNotificationId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpRuleDetail.toJsonArray(DdpRuleDetail.findDdpRuleDetailsByRdtNotificationId(rdtNotificationId).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByRdtPartyCode", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRuleDetailController.jsonFindDdpRuleDetailsByRdtPartyCode(@RequestParam("rdtPartyCode") DdpParty rdtPartyCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpRuleDetail.toJsonArray(DdpRuleDetail.findDdpRuleDetailsByRdtPartyCode(rdtPartyCode).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByRdtRuleId", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRuleDetailController.jsonFindDdpRuleDetailsByRdtRuleId(@RequestParam("rdtRuleId") DdpRule rdtRuleId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpRuleDetail.toJsonArray(DdpRuleDetail.findDdpRuleDetailsByRdtRuleId(rdtRuleId).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByRdtRuleTypeEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRuleDetailController.jsonFindDdpRuleDetailsByRdtRuleTypeEquals(@RequestParam("rdtRuleType") String rdtRuleType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpRuleDetail.toJsonArray(DdpRuleDetail.findDdpRuleDetailsByRdtRuleTypeEquals(rdtRuleType).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByRdtRuleTypeLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRuleDetailController.jsonFindDdpRuleDetailsByRdtRuleTypeLike(@RequestParam("rdtRuleType") String rdtRuleType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpRuleDetail.toJsonArray(DdpRuleDetail.findDdpRuleDetailsByRdtRuleTypeLike(rdtRuleType).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByRdtRuleTypeNotEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> DdpRuleDetailController.jsonFindDdpRuleDetailsByRdtRuleTypeNotEquals(@RequestParam("rdtRuleType") String rdtRuleType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DdpRuleDetail.toJsonArray(DdpRuleDetail.findDdpRuleDetailsByRdtRuleTypeNotEquals(rdtRuleType).getResultList()), headers, HttpStatus.OK);
    }
    
}
