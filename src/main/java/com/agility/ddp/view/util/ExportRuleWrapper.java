package com.agility.ddp.view.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ExportRuleWrapper implements RowMapper<ExportRuleWrapper> {

	private String expRuleId;
	
	private String ddpRule;
	
	private String ddpCompany;
	
	private String ddpBranch;
	
	private String ddpCountry;
	
	private String customerId;
	
	private String ruleStatus;
	

	public String getExpRuleId() {
		return expRuleId;
	}

	public void setExpRuleId(String expRuleId) {
		this.expRuleId = expRuleId;
	}

	public String getDdpRule() {
		return ddpRule;
	}

	public void setDdpRule(String ddpRule) {
		this.ddpRule = ddpRule;
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

	public String getDdpCountry() {
		return ddpCountry;
	}

	public void setDdpCountry(String ddpCountry) {
		this.ddpCountry = ddpCountry;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	
	public String getRuleStatus() {
		return ruleStatus;
	}

	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	@Override
	public ExportRuleWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ExportRuleWrapper ruleWrapper = new ExportRuleWrapper();
		ruleWrapper.setExpRuleId((rs.getString("RDT_RULE_ID")==null) ? "" : rs.getString("RDT_RULE_ID"));
		ruleWrapper.setDdpRule((rs.getString("RUL_DESCRIPTION")==null) ? "" : rs.getString("RUL_DESCRIPTION"));
		ruleWrapper.setDdpCountry((rs.getString("COM_COUNTRY_CODE")==null) ? "" : rs.getString("COM_COUNTRY_CODE"));
		ruleWrapper.setDdpCompany((rs.getString("RDT_COMPANY")==null) ? "" : rs.getString("RDT_COMPANY") );
		ruleWrapper.setDdpBranch((rs.getString("RDT_BRANCH")==null) ? "" : rs.getString("RDT_BRANCH"));
		ruleWrapper.setCustomerId((rs.getString("RDT_PARTY_ID")==null)? "" : rs.getString("RDT_PARTY_ID"));
		ruleWrapper.setRuleStatus((rs.getString("RDT_STATUS") == null)?"" : rs.getString("RDT_STATUS"));
		return ruleWrapper;
	}
	
	

}
