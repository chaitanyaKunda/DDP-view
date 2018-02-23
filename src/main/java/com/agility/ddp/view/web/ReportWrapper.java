package com.agility.ddp.view.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.agility.ddp.data.domain.DdpCategorizedDocs;
import com.agility.ddp.data.domain.DdpDmsDocsDetail;
/**
 * 
 * @author CKunda
 *
 */
public class ReportWrapper {

	private List<String> regionList = null;
	
	private List<String> companyList = null;
	
	private List<String> branchList = null;
	
	private List<String> doctypeList = null;
 	
	private List<String> statusList = null;
	
	private List<String> clientIDList = null;
	
	private String toAddress;
	
	private String ccAddress;
	
	private String strClientID;
	
	private String strCatID;
	
	private String strSynID;
	
	private String strCatStatus;
	
	private String strRuleID;
	
	private Calendar strCatCreatedDate;
	
	private String strDddCompanySource;
	
	private String strDddBranchSource;
	
	private String strDddControlDocumentType;
	
	private String strDddConsignmentID;
	
	private String strDddJobNumber;
	
	private String strrDddDocRef;
		
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Calendar startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Calendar endDate;
	

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	private String cronExpression;
	
 	
	public List<String> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<String> regionList) {
		this.regionList = regionList;
	}

	public List<String> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<String> companyList) {
		this.companyList = companyList;
	}

	public List<String> getBranchList() {
		return branchList;
	}

	public void setBranchList(List<String> branchList) {
		this.branchList = branchList;
	}

	public List<String> getDoctypeList() {
		return doctypeList;
	}

	public void setDoctypeList(List<String> doctypeList) {
		this.doctypeList = doctypeList;
	}

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
	}

	
	
	public String getStrClientID() {
		return strClientID;
	}

	public void setStrClientID(String strClientID) {
		this.strClientID = strClientID;
	}

	public String getStrCatID() {
		return strCatID;
	}

	public void setStrCatID(String strCatID) {
		this.strCatID = strCatID;
	}

	public String getStrSynID() {
		return strSynID;
	}

	public void setStrSynID(String strSynID) {
		this.strSynID = strSynID;
	}

	public String getStrCatStatus() {
		return strCatStatus;
	}

	public void setStrCatStatus(String strCatStatus) {
		this.strCatStatus = strCatStatus;
	}

	public String getStrRuleID() {
		return strRuleID;
	}

	public void setStrRuleID(String strRuleID) {
		this.strRuleID = strRuleID;
	}

	public Calendar getStrCatCreatedDate() {
		return strCatCreatedDate;
	}

	public void setStrCatCreatedDate(Calendar strCatCreatedDate) {
		this.strCatCreatedDate = strCatCreatedDate;
	}

	public String getStrDddCompanySource() {
		return strDddCompanySource;
	}

	public void setStrDddCompanySource(String strDddCompanySource) {
		this.strDddCompanySource = strDddCompanySource;
	}

	public String getStrDddBranchSource() {
		return strDddBranchSource;
	}

	public void setStrDddBranchSource(String strDddBranchSource) {
		this.strDddBranchSource = strDddBranchSource;
	}

	public String getStrDddControlDocumentType() {
		return strDddControlDocumentType;
	}

	public void setStrDddControlDocumentType(String strDddControlDocumentType) {
		this.strDddControlDocumentType = strDddControlDocumentType;
	}

	public String getStrDddConsignmentID() {
		return strDddConsignmentID;
	}

	public void setStrDddConsignmentID(String strDddConsignmentID) {
		this.strDddConsignmentID = strDddConsignmentID;
	}

	public String getStrDddJobNumber() {
		return strDddJobNumber;
	}

	public void setStrDddJobNumber(String strDddJobNumber) {
		this.strDddJobNumber = strDddJobNumber;
	}

	public String getStrrDddDocRef() {
		return strrDddDocRef;
	}

	public void setStrrDddDocRef(String strrDddDocRef) {
		this.strrDddDocRef = strrDddDocRef;
	}

	public List<String> getClientIDList() {
		return clientIDList;
	}

	public void setClientIDList(List<String> clientIDList) {
		this.clientIDList = clientIDList;
	}
	
 	
}
