package com.agility.ddp.view.util;

import java.util.ArrayList;
import java.util.List;

import com.agility.ddp.view.web.ReportWrapper;

public class ReportUtil {

	private ReportWrapper reportWrapper;
	
	private RulesetupReportWrapper rulesetupReportWrapper;
	
	private List<String> transCompanyList = null;
	
	private List<String> transDoctypeList = null;
	
	private List<String> transClientIDList = null;
	
	private List<String> setupCompanyList = null;
	
	private List<String> setupDoctypeList = null;
	
	private List<String> setupClientIDList = null;
	
	public ReportWrapper getReportWrapper() {
		return reportWrapper;
	}

	public void setReportWrapper(ReportWrapper reportWrapper) {
		this.reportWrapper = reportWrapper;
	}

	public RulesetupReportWrapper getRulesetupReportWrapper() {
		return rulesetupReportWrapper;
	}

	public void setRulesetupReportWrapper(RulesetupReportWrapper rulesetupReportWrapper) {
		this.rulesetupReportWrapper = rulesetupReportWrapper;
	}

	public List<String> getTransCompanyList() {
		return transCompanyList;
	}

	public void setTransCompanyList(List<String> transCompanyList) {
		this.transCompanyList = transCompanyList;
	}

	public List<String> getTransDoctypeList() {
		return transDoctypeList;
	}

	public void setTransDoctypeList(List<String> transDoctypeList) {
		this.transDoctypeList = transDoctypeList;
	}

	public List<String> getTransClientIDList() {
		return transClientIDList;
	}

	public void setTransClientIDList(List<String> transClientIDList) {
		this.transClientIDList = transClientIDList;
	}

	public List<String> getSetupCompanyList() {
		return setupCompanyList;
	}

	public void setSetupCompanyList(List<String> setupCompanyList) {
		this.setupCompanyList = setupCompanyList;
	}

	public List<String> getSetupDoctypeList() {
		return setupDoctypeList;
	}

	public void setSetupDoctypeList(List<String> setupDoctypeList) {
		this.setupDoctypeList = setupDoctypeList;
	}

	public List<String> getSetupClientIDList() {
		return setupClientIDList;
	}

	public void setSetupClientIDList(List<String> setupClientIDList) {
		this.setupClientIDList = setupClientIDList;
	}
	
	
}
