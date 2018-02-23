/**
 * 
 */
package com.agility.ddp.view.web;

import java.util.Calendar;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author CKunda
 *
 */
public class OnDemandExportWrapper {

	    @Temporal(TemporalType.TIMESTAMP)
	    @DateTimeFormat(style = "MM")
	    private Calendar fromDate;
	 
	    @Temporal(TemporalType.TIMESTAMP)
	    @DateTimeFormat(style = "MM")
	    private Calendar toDate;
		
		private String jobNumber;
		
		private String consignmentID;
		
		private String docRef;
		
		
		@Temporal(TemporalType.TIMESTAMP)
		@DateTimeFormat(style = "MM")
		private Calendar jobFromDate;
		 
		@Temporal(TemporalType.TIMESTAMP)
		@DateTimeFormat(style = "MM")
		private Calendar jobToDate;
		
		@Temporal(TemporalType.TIMESTAMP)
		@DateTimeFormat(style = "MM")
		private Calendar consFromDate;
		 
		@Temporal(TemporalType.TIMESTAMP)
		@DateTimeFormat(style = "MM")
		private Calendar consToDate;
		
		@Temporal(TemporalType.TIMESTAMP)
		@DateTimeFormat(style = "MM")
		private Calendar docRefFromDate;
		 
		@Temporal(TemporalType.TIMESTAMP)
		@DateTimeFormat(style = "MM")
		private Calendar docRefToDate;
		
		@Temporal(TemporalType.TIMESTAMP)
		@DateTimeFormat(style = "MM")
		private Calendar reportsFromDate;
		 
		@Temporal(TemporalType.TIMESTAMP)
		@DateTimeFormat(style = "MM")
		private Calendar reportsToDate;

		/**
		 * @return the fromDate
		 */
		public Calendar getFromDate() {
			return fromDate;
		}

		/**
		 * @param fromDate the fromDate to set
		 */
		public void setFromDate(Calendar fromDate) {
			this.fromDate = fromDate;
		}

		/**
		 * @return the toDate
		 */
		public Calendar getToDate() {
			return toDate;
		}

		/**
		 * @param toDate the toDate to set
		 */
		public void setToDate(Calendar toDate) {
			this.toDate = toDate;
		}

		/**
		 * @return the jobNumber
		 */
		public String getJobNumber() {
			return jobNumber;
		}

		/**
		 * @param jobNumber the jobNumber to set
		 */
		public void setJobNumber(String jobNumber) {
			this.jobNumber = jobNumber;
		}

		/**
		 * @return the consignmentID
		 */
		public String getConsignmentID() {
			return consignmentID;
		}

		/**
		 * @param consignmentID the consignmentID to set
		 */
		public void setConsignmentID(String consignmentID) {
			this.consignmentID = consignmentID;
		}

		/**
		 * @return the docRef
		 */
		public String getDocRef() {
			return docRef;
		}

		/**
		 * @param docRef the docRef to set
		 */
		public void setDocRef(String docRef) {
			this.docRef = docRef;
		}

		/**
		 * @return the jobFromDate
		 */
		public Calendar getJobFromDate() {
			return jobFromDate;
		}

		/**
		 * @param jobFromDate the jobFromDate to set
		 */
		public void setJobFromDate(Calendar jobFromDate) {
			this.jobFromDate = jobFromDate;
		}

		/**
		 * @return the jobToDate
		 */
		public Calendar getJobToDate() {
			return jobToDate;
		}

		/**
		 * @param jobToDate the jobToDate to set
		 */
		public void setJobToDate(Calendar jobToDate) {
			this.jobToDate = jobToDate;
		}

		/**
		 * @return the consFromDate
		 */
		public Calendar getConsFromDate() {
			return consFromDate;
		}

		/**
		 * @param consFromDate the consFromDate to set
		 */
		public void setConsFromDate(Calendar consFromDate) {
			this.consFromDate = consFromDate;
		}

		/**
		 * @return the consToDate
		 */
		public Calendar getConsToDate() {
			return consToDate;
		}

		/**
		 * @param consToDate the consToDate to set
		 */
		public void setConsToDate(Calendar consToDate) {
			this.consToDate = consToDate;
		}

		/**
		 * @return the docRefFromDate
		 */
		public Calendar getDocRefFromDate() {
			return docRefFromDate;
		}

		/**
		 * @param docRefFromDate the docRefFromDate to set
		 */
		public void setDocRefFromDate(Calendar docRefFromDate) {
			this.docRefFromDate = docRefFromDate;
		}

		/**
		 * @return the docRefToDate
		 */
		public Calendar getDocRefToDate() {
			return docRefToDate;
		}

		/**
		 * @param docRefToDate the docRefToDate to set
		 */
		public void setDocRefToDate(Calendar docRefToDate) {
			this.docRefToDate = docRefToDate;
		}

		/**
		 * @return the reportsFromDate
		 */
		public Calendar getReportsFromDate() {
			return reportsFromDate;
		}

		/**
		 * @param reportsFromDate the reportsFromDate to set
		 */
		public void setReportsFromDate(Calendar reportsFromDate) {
			this.reportsFromDate = reportsFromDate;
		}

		/**
		 * @return the reportsToDate
		 */
		public Calendar getReportsToDate() {
			return reportsToDate;
		}

		/**
		 * @param reportsToDate the reportsToDate to set
		 */
		public void setReportsToDate(Calendar reportsToDate) {
			this.reportsToDate = reportsToDate;
		}
		
		
	 
}
