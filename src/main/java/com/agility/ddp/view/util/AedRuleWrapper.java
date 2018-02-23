package com.agility.ddp.view.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class AedRuleWrapper implements RowMapper<AedRuleWrapper>{

	private Integer aedRuleId;
	
	private String ddpRule;
	
	private String ddpCompany;
	
	private String ddpBranch;
	
	private String customerId;
	
	private String emailTo;
	
	private String emailCc;
	
	
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

	/**
	 * @return the aedRuleId
	 */
	public Integer getAedRuleId() {
		return aedRuleId;
	}

	/**
	 * @param aedRuleId the aedRuleId to set
	 */
	public void setAedRuleId(Integer aedRuleId) {
		this.aedRuleId = aedRuleId;
	}

	/**
	 * @return the ddpRule
	 */
	public String getDdpRule() {
		return ddpRule;
	}

	/**
	 * @param ddpRule the ddpRule to set
	 */
	public void setDdpRule(String ddpRule) {
		this.ddpRule = ddpRule;
	}

	/**
	 * @return the ddpCompany
	 */
	public String getDdpCompany() {
		return ddpCompany;
	}

	/**
	 * @param ddpCompany the ddpCompany to set
	 */
	public void setDdpCompany(String ddpCompany) {
		this.ddpCompany = ddpCompany;
	}

	/**
	 * @return the ddpBranch
	 */
	public String getDdpBranch() {
		return ddpBranch;
	}

	/**
	 * @param ddpBranch the ddpBranch to set
	 */
	public void setDdpBranch(String ddpBranch) {
		this.ddpBranch = ddpBranch;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Override
	public AedRuleWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
		AedRuleWrapper ruleWrapper = new AedRuleWrapper();
		ruleWrapper.setAedRuleId(Integer.parseInt(rs.getString("RDT_RULE_ID")));
		ruleWrapper.setDdpRule(rs.getString("RUL_DESCRIPTION"));
		ruleWrapper.setEmailTo(rs.getString("CEM_EMAIL_TO"));
		ruleWrapper.setEmailCc(rs.getString("CEM_EMAIL_CC"));
		ruleWrapper.setDdpCompany(rs.getString("RDT_COMPANY"));
		ruleWrapper.setDdpBranch(rs.getString("RDT_BRANCH"));
		ruleWrapper.setCustomerId(rs.getString("RDT_PARTY_ID"));
		return ruleWrapper;
	}
	
	
}
