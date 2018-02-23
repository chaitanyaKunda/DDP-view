package com.agility.ddp.view.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

public class CategorizedDocsWrapper implements RowMapper<CategorizedDocsWrapper>{

		private String strCatId;
		
		private String strCatDtxId;
		
		private String strCatRuleId;
		
		private String strRdtCompany;
		
		private String strRdtBranch;
		
		private String strRdtDocType;
		
		private String strRdtPartyCode;
		
		private String strRdtGenSource;
		
		private String strRdtScheduleId;
		
		private String strRdtNotificationId;
		
		private String strRdtCommunicationId;
		
		private String strRObjctId;
		
		private String strConsignmentId;
		
		private String strDocumentReferce;
		
		private String strClientId;
		
		private String strDocumentType;
		
		private String strShipper;
		
		private String strConsignee;
		
		private String strNotifyParty;
		
		private String strDebitsForward;
		
		private String strDebitsBack;
		
		private String strIntialAgent;
		
		private String strIntermediateAgent;
		
		private String strFinalAgent;
		
		private String strCustomEntryNo;
		
		private String strRated;
		
		private String strCatStatus;
		
		private Calendar calCatCreatedDate;
		
		private String strSynID;
		
		private String strCatRuletype;
		
		private String strCatRdtID;
		
		private String strJobnumber;
		
		private String strRdtPartyID;
		
		private String strRelevantType;
		
		public String getStrRelevantType() {
			return strRelevantType;
		}

		public void setStrRelevantType(String strRelevantType) {
			this.strRelevantType = strRelevantType;
		}

		public String getStrSynID() {
			return strSynID;
		}

		public void setStrSynID(String strSynID) {
			this.strSynID = strSynID;
		}

		public String getStrCatRuletype() {
			return strCatRuletype;
		}

		public void setStrCatRuletype(String strCatRuletype) {
			this.strCatRuletype = strCatRuletype;
		}

		public String getStrCatRdtID() {
			return strCatRdtID;
		}

		public void setStrCatRdtID(String strCatRdtID) {
			this.strCatRdtID = strCatRdtID;
		}

		public String getStrJobnumber() {
			return strJobnumber;
		}

		public void setStrJobnumber(String strJobnumber) {
			this.strJobnumber = strJobnumber;
		}

		public String getStrRdtPartyID() {
			return strRdtPartyID;
		}

		public void setStrRdtPartyID(String strRdtPartyID) {
			this.strRdtPartyID = strRdtPartyID;
		}

		public String getStrCatId() {
			return strCatId;
		}

		public void setStrCatId(String strCatId) {
			this.strCatId = strCatId;
		}

		public String getStrCatDtxId() {
			return strCatDtxId;
		}

		public void setStrCatDtxId(String strCatDtxId) {
			this.strCatDtxId = strCatDtxId;
		}

		public String getStrCatRuleId() {
			return strCatRuleId;
		}

		public void setStrCatRuleId(String strCatRuleId) {
			this.strCatRuleId = strCatRuleId;
		}

		public String getStrRdtCompany() {
			return strRdtCompany;
		}

		public void setStrRdtCompany(String strRdtCompany) {
			this.strRdtCompany = strRdtCompany;
		}

		public String getStrRdtBranch() {
			return strRdtBranch;
		}

		public void setStrRdtBranch(String strRdtBranch) {
			this.strRdtBranch = strRdtBranch;
		}

		public String getStrRdtDocType() {
			return strRdtDocType;
		}

		public void setStrRdtDocType(String strRdtDocType) {
			this.strRdtDocType = strRdtDocType;
		}

		public String getStrRdtPartyCode() {
			return strRdtPartyCode;
		}

		public void setStrRdtPartyCode(String strRdtPartyCode) {
			this.strRdtPartyCode = strRdtPartyCode;
		}

		public String getStrRdtGenSource() {
			return strRdtGenSource;
		}

		public void setStrRdtGenSource(String strRdtGenSource) {
			this.strRdtGenSource = strRdtGenSource;
		}

		public String getStrRdtScheduleId() {
			return strRdtScheduleId;
		}

		public void setStrRdtScheduleId(String strRdtScheduleId) {
			this.strRdtScheduleId = strRdtScheduleId;
		}

		public String getStrRdtNotificationId() {
			return strRdtNotificationId;
		}

		public void setStrRdtNotificationId(String strRdtNotificationId) {
			this.strRdtNotificationId = strRdtNotificationId;
		}

		public String getStrRdtCommunicationId() {
			return strRdtCommunicationId;
		}

		public void setStrRdtCommunicationId(String strRdtCommunicationId) {
			this.strRdtCommunicationId = strRdtCommunicationId;
		}

		public String getStrRObjctId() {
			return strRObjctId;
		}

		public void setStrRObjctId(String strRObjctId) {
			this.strRObjctId = strRObjctId;
		}

		public String getStrConsignmentId() {
			return strConsignmentId;
		}

		public void setStrConsignmentId(String strConsignmentId) {
			this.strConsignmentId = strConsignmentId;
		}

		public String getStrDocumentReferce() {
			return strDocumentReferce;
		}

		public void setStrDocumentReferce(String strDocumentReferce) {
			this.strDocumentReferce = strDocumentReferce;
		}

		public String getStrClientId() {
			return strClientId;
		}

		public void setStrClientId(String strClientId) {
			this.strClientId = strClientId;
		}

		public String getStrDocumentType() {
			return strDocumentType;
		}

		public void setStrDocumentType(String strDocumentType) {
			this.strDocumentType = strDocumentType;
		}

		public String getStrShipper() {
			return strShipper;
		}

		public void setStrShipper(String strShipper) {
			this.strShipper = strShipper;
		}

		public String getStrConsignee() {
			return strConsignee;
		}

		public void setStrConsignee(String strConsignee) {
			this.strConsignee = strConsignee;
		}

		public String getStrNotifyParty() {
			return strNotifyParty;
		}

		public void setStrNotifyParty(String strNotifyParty) {
			this.strNotifyParty = strNotifyParty;
		}

		public String getStrDebitsForward() {
			return strDebitsForward;
		}

		public void setStrDebitsForward(String strDebitsForward) {
			this.strDebitsForward = strDebitsForward;
		}

		public String getStrDebitsBack() {
			return strDebitsBack;
		}

		public void setStrDebitsBack(String strDebitsBack) {
			this.strDebitsBack = strDebitsBack;
		}

		public String getStrIntialAgent() {
			return strIntialAgent;
		}

		public void setStrIntialAgent(String strIntialAgent) {
			this.strIntialAgent = strIntialAgent;
		}

		public String getStrIntermediateAgent() {
			return strIntermediateAgent;
		}

		public void setStrIntermediateAgent(String strIntermediateAgent) {
			this.strIntermediateAgent = strIntermediateAgent;
		}

		public String getStrFinalAgent() {
			return strFinalAgent;
		}

		public void setStrFinalAgent(String strFinalAgent) {
			this.strFinalAgent = strFinalAgent;
		}

		public String getStrCustomEntryNo() {
			return strCustomEntryNo;
		}

		public void setStrCustomEntryNo(String strCustomEntryNo) {
			this.strCustomEntryNo = strCustomEntryNo;
		}

		public String getStrRated() {
			return strRated;
		}

		public void setStrRated(String strRated) {
			this.strRated = strRated;
		}

		public String getStrCatStatus() {
			return strCatStatus;
		}

		public void setStrCatStatus(String strCatStatus) {
			this.strCatStatus = strCatStatus;
		}


		public Calendar getCalCatCreatedDate() {
			return calCatCreatedDate;
		}

		public void setCalCatCreatedDate(Calendar calCatCreatedDate) {
			this.calCatCreatedDate = calCatCreatedDate;
		}

		@Override
		public CategorizedDocsWrapper mapRow(ResultSet rs, int rowNum)	throws SQLException
		{
			CategorizedDocsWrapper docsWrapper = new CategorizedDocsWrapper();
			docsWrapper.setStrCatId((rs.getString("CAT_ID") == null) ? "" : rs.getString("CAT_ID"));
			docsWrapper.setStrSynID((rs.getString("CAT_SYN_ID") == null) ? "" : rs.getString("CAT_SYN_ID"));
			docsWrapper.setStrCatRuletype((rs.getString("CAT_RULE_TYPE") == null) ? "" : rs.getString("CAT_RULE_TYPE"));
			docsWrapper.setStrCatRdtID((rs.getString("CAT_RDT_ID") == null) ? "" : rs.getString("CAT_RDT_ID") );
			docsWrapper.setStrRdtCompany((rs.getString("DDD_COMPANY_SOURCE") == null ) ? "" : rs.getString("DDD_COMPANY_SOURCE"));
			docsWrapper.setStrRdtDocType((rs.getString("DDD_CONTROL_DOC_TYPE") == null) ? "" : rs.getString("DDD_CONTROL_DOC_TYPE"));
			docsWrapper.setStrConsignmentId((rs.getString("DDD_CONSIGNMENT_ID") == null) ? "" : rs.getString("DDD_CONSIGNMENT_ID"));
			docsWrapper.setStrJobnumber((rs.getString("DDD_JOB_NUMBER") == null) ? "" : rs.getString("DDD_JOB_NUMBER"));
			docsWrapper.setStrDocumentReferce((rs.getString("DDD_DOC_REF") == null) ? "" : rs.getString("DDD_DOC_REF"));
			docsWrapper.setStrCatRuleId((rs.getString("CAT_RUL_ID") == null) ? "" : rs.getString("CAT_RUL_ID"));
			if(rs.getTimestamp("CAT_CREATED_DATE") != null)
			{
				Calendar createdDate = Calendar.getInstance();
				createdDate.setTime(rs.getTimestamp("CAT_CREATED_DATE"));
				docsWrapper.setCalCatCreatedDate(createdDate);
			}
			docsWrapper.setStrCatStatus((rs.getString("CAT_STATUS") == null) ? "" : rs.getString("CAT_STATUS"));
			docsWrapper.setStrRdtPartyCode((rs.getString("RDT_PARTY_CODE")== null) ? "" : rs.getString("RDT_PARTY_CODE"));
			docsWrapper.setStrRdtPartyID((rs.getString("RDT_PARTY_ID")==null) ? "" : rs.getString("RDT_PARTY_ID"));
			docsWrapper.setStrRdtGenSource((rs.getString("DDD_GENERATING_SYSTEM")==null) ? "" : rs.getString("DDD_GENERATING_SYSTEM"));
			docsWrapper.setStrRated((rs.getString("DDD_IS_RATED")==null)? "" : rs.getString("DDD_IS_RATED"));
			docsWrapper.setStrRelevantType((rs.getString("RELEVANT_TYPE")==null)? "0" : rs.getString("RELEVANT_TYPE"));
			return docsWrapper;
		}
		
		
}
