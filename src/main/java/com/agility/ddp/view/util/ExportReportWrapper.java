package com.agility.ddp.view.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.springframework.jdbc.core.RowMapper;

public class ExportReportWrapper implements RowMapper<ExportReportWrapper>{

	private String strExportMissingID;
	
	private String strRuleID;
	
	private String strCompany;
	
	private String strClientID;
	
	private String strFilename;
	
	private String strJobnumber;
	
	private String strConsignmentID;
	
	private Calendar calExportedDate;

	private Calendar calCreatedDate;
	
	private String status;
	
	public String getStrExportMissingID() {
		return strExportMissingID;
	}

	public void setStrExportMissingID(String strExportMissingID) {
		this.strExportMissingID = strExportMissingID;
	}

	public String getStrRuleID() {
		return strRuleID;
	}

	public void setStrRuleID(String strRuleID) {
		this.strRuleID = strRuleID;
	}

	public String getStrCompany() {
		return strCompany;
	}

	public void setStrCompany(String strCompany) {
		this.strCompany = strCompany;
	}

	public String getStrClientID() {
		return strClientID;
	}

	public void setStrClientID(String strClientID) {
		this.strClientID = strClientID;
	}

	public String getStrFilename() {
		return strFilename;
	}

	public void setStrFilename(String strFilename) {
		this.strFilename = strFilename;
	}

	public String getStrJobnumber() {
		return strJobnumber;
	}

	public void setStrJobnumber(String strJobnumber) {
		this.strJobnumber = strJobnumber;
	}

	public String getStrConsignmentID() {
		return strConsignmentID;
	}

	public void setStrConsignmentID(String strConsignmentID) {
		this.strConsignmentID = strConsignmentID;
	}

	public Calendar getCalExportedDate() {
		return calExportedDate;
	}

	public void setCalExportedDate(Calendar calExportedDate) {
		this.calExportedDate = calExportedDate;
	}

	public Calendar getCalCreatedDate() {
		return calCreatedDate;
	}

	public void setCalCreatedDate(Calendar calCreatedDate) {
		this.calCreatedDate = calCreatedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public ExportReportWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ExportReportWrapper exportReportWrapper = new ExportReportWrapper();
		exportReportWrapper.setStrExportMissingID(rs.getString("ESR_MIS_ID"));
		exportReportWrapper.setStrRuleID(rs.getString("RULE_ID"));
		exportReportWrapper.setStrCompany(rs.getString("RDT_COMPANY"));
		exportReportWrapper.setStrClientID(rs.getString("RDT_PARTY_ID"));
		exportReportWrapper.setStrFilename(rs.getString("ESR_FILE_NAME"));
		exportReportWrapper.setStrJobnumber(rs.getString("ESR_JOB_NUMBER"));
		exportReportWrapper.setStrConsignmentID(rs.getString("ESR_CONSIGNMENT_ID"));
		if(rs.getTimestamp("ESR_EXPORT_TIME") != null)
		{
			Calendar exportedDate = Calendar.getInstance();
			exportedDate.setTime(rs.getTimestamp("ESR_EXPORT_TIME"));
			exportReportWrapper.setCalExportedDate(exportedDate);
		}
		
		if(rs.getTimestamp("CAT_CREATED_DATE") != null)
		{
			Calendar createdDate = Calendar.getInstance();
			createdDate.setTime(rs.getTimestamp("CAT_CREATED_DATE"));
			exportReportWrapper.setCalCreatedDate(createdDate);
		}
		
		exportReportWrapper.setStatus(rs.getString("CAT_STATUS"));
		
		return exportReportWrapper;
	}
	
	
	
}
