package com.agility.ddp.view.util;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchFilter {
	
	
	
	private String groupOp;
	
	private List<RuleSearch> rules;
 	
	public String getGroupOp() {
		return groupOp;
	}
	public void setGroupOp(String groupOp) {
		this.groupOp = groupOp;
	}
	public List<RuleSearch> getRules() {
		return rules;
	}
	public void setRules(List<RuleSearch> rules) {
		this.rules = rules;
	}
	
	
	
		@Override
		public String toString() {
			return "sf"+groupOp;
		}
	
}
