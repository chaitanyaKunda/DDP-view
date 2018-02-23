package com.agility.ddp.view.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class SchedulerRuleWrapper implements RowMapper<SchedulerRuleWrapper>{

	private Integer maedRuleId;
	
	private String maedRuleName;
	
	private String ddpCompany;
	
	private String ddpBranch;
	
	private String customerId;

	private String emailTo;
	
	private String emailCc;
	
	

	public Integer getMaedRuleId() {
		return maedRuleId;
	}



	public void setMaedRuleId(Integer maedRuleId) {
		this.maedRuleId = maedRuleId;
	}



	public String getMaedRuleName() {
		return maedRuleName;
	}



	public void setMaedRuleName(String maedRuleName) {
		this.maedRuleName = maedRuleName;
	}



	public String getDdpCompany() {
		return ddpCompany;
	}



	public void setDdpCompany(String ddpCompany) {
		this.ddpCompany = ddpCompany;
	}



	public String getDdpBranch() {
		return ddpBranch;
	}



	public void setDdpBranch(String ddpBranch) {
		this.ddpBranch = ddpBranch;
	}



	public String getCustomerId() {
		return customerId;
	}



	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}



	public String getEmailTo() {
		return emailTo;
	}



	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}



	public String getEmailCc() {
		return emailCc;
	}



	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}



	@Override
	public SchedulerRuleWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
		SchedulerRuleWrapper ruleWrapper = new SchedulerRuleWrapper();
		ruleWrapper.setMaedRuleId(Integer.parseInt(rs.getString("RDT_RULE_ID")));
		ruleWrapper.setMaedRuleName(rs.getString("RUL_DESCRIPTION"));
		ruleWrapper.setEmailTo(rs.getString("CEM_EMAIL_TO"));
		ruleWrapper.setEmailCc(rs.getString("CEM_EMAIL_CC"));
		ruleWrapper.setDdpCompany(rs.getString("RDT_COMPANY"));
		ruleWrapper.setDdpBranch(rs.getString("RDT_BRANCH"));
		ruleWrapper.setCustomerId(rs.getString("RDT_PARTY_ID"));
		return ruleWrapper;
	}
	
	
}
