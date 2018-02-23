package com.agility.ddp.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.agility.ddp.data.domain.DdpMultiEmails;

public class RuleDao {
	int rid;
	int rdtId;
	String rdtCompanyCode;
	String rdtBranch;
	String rdtPartyCode;
	String rdtDocType;
	int rdtStatus;
	String cemEmailFrom;
	String cemEmailTo;
	String cemEmailCc;
	String cemSubject;
	String cemBody;
	String copOption;
	String copEmail;
	String copPrint;
	String duplicateflag;
	String rdtPartyId;
	String genSource;
	String strRate;
	//newly added
	String commProtocol;
	String ftpLocation;
	String ftpUsername;
	String ftpPassword;
	Integer ftpPort;
	String expVersionSetup;
	String uncPath;
	String uncUsername;
	String uncPassword;
	String rdtPartyName;
	String manditoryFlag;
	String documentSequence;
	String relevantType;
	String rdtSlaFreq;
	String rdtSlaMin;
	String rdtSlaMax;
	String rdtExclude;
	List<DdpMultiEmails> multiEmails = new ArrayList<DdpMultiEmails>(); 
	
	

	public List<DdpMultiEmails> getMultiEmails() {
		return multiEmails;
	}

	public void setMultiEmails(List<DdpMultiEmails> multiEmails) {
		this.multiEmails = multiEmails;
	}

	public String getRdtExclude() {
		return rdtExclude;
	}

	public void setRdtExclude(String rdtExclude) {
		this.rdtExclude = rdtExclude;
	}

	public String getCommProtocol() {
		return commProtocol;
	}

	public void setCommProtocol(String commProtocol) {
		this.commProtocol = commProtocol;
	}

	public String getRdtSlaFreq() {
		return rdtSlaFreq;
	}

	public void setRdtSlaFreq(String rdtSlaFreq) {
		this.rdtSlaFreq = rdtSlaFreq;
	}

	public String getRdtSlaMin() {
		return rdtSlaMin;
	}

	public void setRdtSlaMin(String rdtSlaMin) {
		this.rdtSlaMin = rdtSlaMin;
	}

	public String getRdtSlaMax() {
		return rdtSlaMax;
	}

	public void setRdtSlaMax(String rdtSlaMax) {
		this.rdtSlaMax = rdtSlaMax;
	}

	public int getRdtStatus() {
		return rdtStatus;
	}

	public void setRdtStatus(int rdtStatus) {
		this.rdtStatus = rdtStatus;
	}

	/**
	 * @return the documentSequence
	 */
	public String getDocumentSequence() {
		return documentSequence;
	}

	/**
	 * @param documentSequence the documentSequence to set
	 */
	public void setDocumentSequence(String documentSequence) {
		this.documentSequence = documentSequence;
	}

	/**
	 * @return the relevantType
	 */
	public String getRelevantType() {
		return relevantType;
	}

	/**
	 * @param relevantType the relevantType to set
	 */
	public void setRelevantType(String relevantType) {
		this.relevantType = relevantType;
	}

	/**
	 * @return the rdtPartyName
	 */
	public String getRdtPartyName() {
		return rdtPartyName;
	}

	/**
	 * @param rdtPartyName the rdtPartyName to set
	 */
	public void setRdtPartyName(String rdtPartyName) {
		this.rdtPartyName = rdtPartyName;
	}

	/**
	 * @return the manditoryFlag
	 */
	public String getManditoryFlag() {
		return manditoryFlag;
	}

	/**
	 * @param manditoryFlag the manditoryFlag to set
	 */
	public void setManditoryFlag(String manditoryFlag) {
		this.manditoryFlag = manditoryFlag;
	}

	public String getExpVersionSetup() {
		return expVersionSetup;
	}

	public void setExpVersionSetup(String expVersionSetup) {
		this.expVersionSetup = expVersionSetup;
	}

	public void setFtpPort(Integer ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUsername() {
		return ftpUsername;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public int getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getUncPath() {
		return uncPath;
	}

	public void setUncPath(String uncPath) {
		this.uncPath = uncPath;
	}

	public String getUncUsername() {
		return uncUsername;
	}

	public void setUncUsername(String uncUsername) {
		this.uncUsername = uncUsername;
	}

	public String getUncPassword() {
		return uncPassword;
	}

	public void setUncPassword(String uncPassword) {
		this.uncPassword = uncPassword;
	}

	public RuleDao() {
	}
	
	public String getFtpLocation() {
		return ftpLocation;
	}

	public void setFtpLocation(String ftpLocation) {
		this.ftpLocation = ftpLocation;
	}

	public String getGenSource() {
		return genSource;
	}

	public void setGenSource(String genSource) {
		this.genSource = genSource;
	}

	public String getStrRate() {
		return strRate;
	}

	public void setStrRate(String strRate) {
		this.strRate = strRate;
	}

	public String getCopEmail() {
		return copEmail;
	}

	public void setCopEmail(String copEmail) {
		this.copEmail = copEmail;
	}

	public String getCopPrint() {
		return copPrint;
	}

	public void setCopPrint(String copPrint) {
		this.copPrint = copPrint;
	}

	public String getCemEmailCc() {
		return cemEmailCc;
	}

	public void setCemEmailCc(String cemEmailCc) {
		this.cemEmailCc = cemEmailCc;
	}

	public String getDuplicateflag() {
		return duplicateflag;
	}

	public void setDuplicateflag(String duplicateflag) {
		this.duplicateflag = duplicateflag;
	}

	public String getCemSubject() {
		return cemSubject;
	}
	public void setCemSubject(String cemSubject) {
		this.cemSubject = cemSubject;
	}
	public String getCopOption() {
		return copOption;
	}

	public String getCemBody() {
		return cemBody;
	}

	public void setCemBody(String cemBody) {
		this.cemBody = cemBody;
	}

	public void setCopOption(String copOption) {
		this.copOption = copOption;
	}
	public int getRdtId() {
		return rdtId;
	}

	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public void setRdtId(int rdtId) {
		this.rdtId = rdtId;
	}
	public String getRdtCompanyCode() {
		return rdtCompanyCode;
	}
	public void setRdtCompanyCode(String rdtCompanyCode) {
		this.rdtCompanyCode = rdtCompanyCode;
	}
	public String getRdtBranch() {
		return rdtBranch;
	}
	public void setRdtBranch(String rdtBranch) {
		this.rdtBranch = rdtBranch;
	}
	public String getRdtPartyCode() {
		return rdtPartyCode;
	}
	public void setRdtPartyCode(String rdtPartyCode) {
		this.rdtPartyCode = rdtPartyCode;
	}
	public String getRdtDocType() {
		return rdtDocType;
	}
	public void setRdtDocType(String rdtDocType) {
		this.rdtDocType = rdtDocType;
	}
	public String getCemEmailFrom() {
		return cemEmailFrom;
	}
	public void setCemEmailFrom(String cemEmailFrom) {
		this.cemEmailFrom = cemEmailFrom;
	}
	public String getCemEmailTo() {
		return cemEmailTo;
	}
	public void setCemEmailTo(String cemEmailTo) {
		this.cemEmailTo = cemEmailTo;
	}
	@Override
	public String toString() {
		return "RuleDao [rid=" + rid + ", rdtId=" + rdtId + ", rdtCompanyCode="
				+ rdtCompanyCode + ", rdtBranch=" + rdtBranch
				+ ", rdtPartyCode=" + rdtPartyCode + ", rdtDocType="
				+ rdtDocType + ", cemEmailFrom=" + cemEmailFrom
				+ ", cemEmailTo=" + cemEmailTo + ", cemSubject=" + cemSubject
				+ ", cemBody=" + cemBody + ", copOption=" + copOption + "]";
	}

	public String getRdtPartyId() {
		return rdtPartyId;
	}

	public void setRdtPartyId(String rdtPartyId) {
		this.rdtPartyId = rdtPartyId;
	}


}
