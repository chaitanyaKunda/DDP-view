package com.agility.ddp.view.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.agility.ddp.data.domain.DdpAedRule;
import com.agility.ddp.data.domain.DdpBranch;
import com.agility.ddp.data.domain.DdpCommEmail;
import com.agility.ddp.data.domain.DdpCommFtp;
import com.agility.ddp.data.domain.DdpCommOptionsSetup;
import com.agility.ddp.data.domain.DdpCommUnc;
import com.agility.ddp.data.domain.DdpCommunicationLkp;
import com.agility.ddp.data.domain.DdpCommunicationSetup;
import com.agility.ddp.data.domain.DdpCompany;
import com.agility.ddp.data.domain.DdpDocnameConv;
import com.agility.ddp.data.domain.DdpDoctype;
import com.agility.ddp.data.domain.DdpExportRule;
import com.agility.ddp.data.domain.DdpMultiAedRule;
import com.agility.ddp.data.domain.DdpMultiEmails;
import com.agility.ddp.data.domain.DdpNotification;
import com.agility.ddp.data.domain.DdpParty;
import com.agility.ddp.data.domain.DdpRule;
import com.agility.ddp.data.domain.DdpRuleDetail;
public class RuleWrapper{
	
	DdpAedRule ddpAedRule = new DdpAedRule() ;
	
	DdpRuleDetail ddpRuleDetail = new DdpRuleDetail();
	
	DdpRule ddpRule = new DdpRule();
	
	DdpCompany ddpCompany = new DdpCompany();
	
	DdpBranch ddpBranch = new DdpBranch();
	
	DdpDoctype ddpDoctype = new DdpDoctype();
	
	DdpParty ddpParty = new DdpParty();
	
	DdpCommEmail  ddpCommEmail = new DdpCommEmail();
	
	DdpMultiEmails ddpMultiEmails = new DdpMultiEmails();
	
	DdpCommunicationSetup ddpCommunicationSetup = new DdpCommunicationSetup();
	
	DdpCommunicationLkp ddpCommunicationLkp = new DdpCommunicationLkp();
	
	DdpCommOptionsSetup ddpCommOptionsSetup = new DdpCommOptionsSetup();
	
	DdpNotification ddpNotification = new DdpNotification();
	
	boolean prtActive;
	
	boolean emailActive;
	
	boolean active;
	
	Integer rowcount;
	
	String schReportEmailTo;
	
	String schReportEmailCc;
	
	String exportQueryfromTextarea;
	
	
	
	public DdpMultiEmails getDdpMultiEmails() {
		return ddpMultiEmails;
	}

	public void setDdpMultiEmails(DdpMultiEmails ddpMultiEmails) {
		this.ddpMultiEmails = ddpMultiEmails;
	}

	public String getExportQueryfromTextarea() {
		return exportQueryfromTextarea;
	}

	public void setExportQueryfromTextarea(String exportQueryfromTextarea) {
		this.exportQueryfromTextarea = exportQueryfromTextarea;
	}

	public String getSchReportEmailTo() {
		return schReportEmailTo;
	}

	public void setSchReportEmailTo(String schReportEmailTo) {
		this.schReportEmailTo = schReportEmailTo;
	}

	public String getSchReportEmailCc() {
		return schReportEmailCc;
	}

	public void setSchReportEmailCc(String schReportEmailCc) {
		this.schReportEmailCc = schReportEmailCc;
	}

	List<DdpBranch> lstBranch = new ArrayList<DdpBranch>();
	
	List<DdpDoctype> lstDocs = new ArrayList<DdpDoctype>();
	
	DdpMultiAedRule ddpMultiAedRule;
	
	
	
	/**
	 * @return the ddpMultiAedRule
	 */
	public DdpMultiAedRule getDdpMultiAedRule() {
		return ddpMultiAedRule;
	}

	/**
	 * @param ddpMultiAedRule the ddpMultiAedRule to set
	 */
	public void setDdpMultiAedRule(DdpMultiAedRule ddpMultiAedRule) {
		this.ddpMultiAedRule = ddpMultiAedRule;
	}

	public List<DdpDoctype> getLstDocs() {
		return lstDocs;
	}

	public void setLstDocs(List<DdpDoctype> lstDocs) {
		this.lstDocs = lstDocs;
	}

		//newly added
		DdpExportRule ddpExportRule = new DdpExportRule();
		
		List<String> ftpFolderLst;
		
		List<DdpCommFtp> ddpCommFtpLst;
		
		List<DdpCommUnc> ddpCommUncLst;
		
		DdpDocnameConv ddpDocnameConv;
		
		
		
		public DdpDocnameConv getDdpDocnameConv() {
			return ddpDocnameConv;
		}

		public void setDdpDocnameConv(DdpDocnameConv ddpDocnameConv) {
			this.ddpDocnameConv = ddpDocnameConv;
		}

		public List<DdpCommFtp> getDdpCommFtp() {
			return ddpCommFtpLst;
		}

		public void setDdpCommFtp(List<DdpCommFtp> ddpCommFtp) {
			this.ddpCommFtpLst = ddpCommFtp;
		}

		

		

		public List<DdpCommUnc> getDdpCommUnc() {
			return ddpCommUncLst;
		}

		public void setDdpCommUnc(List<DdpCommUnc> ddpCommUnc) {
			this.ddpCommUncLst = ddpCommUnc;
		}

		public List<String> getFtpFolder() {
			return ftpFolderLst;
		}

		public void setFtpFolder(List<String> ftpFolder) {
			this.ftpFolderLst = ftpFolder;
		}

		public DdpExportRule getDdpExportRule() {
			return ddpExportRule;
		}

		public void setDdpExportRule(DdpExportRule ddpExportRule) {
			this.ddpExportRule = ddpExportRule;
		}
		
	public Integer getRowcount() {
		return rowcount;
	}

	public void setRowcount(Integer rowcount) {
		this.rowcount = rowcount;
	}
	
	public List<DdpBranch> getLstBranch() {
		return lstBranch;
	}

	public void setLstBranch(List<DdpBranch> lstBranch) {
		this.lstBranch = lstBranch;
	}

	public DdpCommOptionsSetup getDdpCommOptionsSetup() {
		return ddpCommOptionsSetup;
	}

	public void setDdpCommOptionsSetup(DdpCommOptionsSetup ddpCommOptionsSetup) {
		this.ddpCommOptionsSetup = ddpCommOptionsSetup;
	}

	public DdpCommunicationLkp getDdpCommunicationLkp() {
		return ddpCommunicationLkp;
	}

	public void setDdpCommunicationLkp(DdpCommunicationLkp ddpCommunicationLkp) {
		this.ddpCommunicationLkp = ddpCommunicationLkp;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isPrtActive() {
		return prtActive;
	}

	public void setPrtActive(boolean prtActive) {
		this.prtActive = prtActive;
	}

	public boolean isEmailActive() {
		return emailActive;
	}

	public void setEmailActive(boolean emailActive) {
		this.emailActive = emailActive;
	}

	public DdpCommEmail getDdpCommEmail() {
		return ddpCommEmail;
	}

	public void setDdpCommEmail(DdpCommEmail ddpCommEmail) {
		this.ddpCommEmail = ddpCommEmail;
	}

	public DdpParty getDdpParty() {
		return ddpParty;
	}

	public void setDdpParty(DdpParty ddpParty) {
		this.ddpParty = ddpParty;
	}

	public DdpBranch getDdpBranch() {
		return ddpBranch;
	}

	public void setDdpBranch(DdpBranch ddpBranch) {
		this.ddpBranch = ddpBranch;
	}

	public DdpDoctype getDdpDoctype() {
		return ddpDoctype;
	}

	public void setDdpDoctype(DdpDoctype ddpDoctype) {
		this.ddpDoctype = ddpDoctype;
	}

	public DdpCompany getDdpCompany() {
		return ddpCompany;
	}

	public void setDdpCompany(DdpCompany ddpCompany) {
		this.ddpCompany = ddpCompany;
	}

	public DdpAedRule getDdpAedRule() {
		return ddpAedRule;
	}

	public void setDdpAedRule(DdpAedRule ddpAedRule) {
		this.ddpAedRule = ddpAedRule;
	}

	public DdpRuleDetail getDdpRuleDetail() {
		return ddpRuleDetail;
	}

	public void setDdpRuleDetail(DdpRuleDetail ddpRuleDetail) {
		this.ddpRuleDetail = ddpRuleDetail;
	}

	public DdpRule getDdpRule() {
		return ddpRule;
	}

	public void setDdpRule(DdpRule ddpRule) {
		this.ddpRule = ddpRule;
	}
	
	public DdpCommunicationSetup getDdpCommunicationSetup() {
		return ddpCommunicationSetup;
	}

	public void setDdpCommunicationSetup(DdpCommunicationSetup ddpCommunicationSetup) {
		this.ddpCommunicationSetup = ddpCommunicationSetup;
	}

	/**
	 * @return the ddpNotification
	 */
	public DdpNotification getDdpNotification() {
		return ddpNotification;
	}

	/**
	 * @param ddpNotification the ddpNotification to set
	 */
	public void setDdpNotification(DdpNotification ddpNotification) {
		this.ddpNotification = ddpNotification;
	}
}
