package com.agility.ddp.view.util;

import org.codehaus.jackson.annotate.JsonProperty;

public class RuleSearch {
	
	@JsonProperty("field")
	private String field;
	
	@JsonProperty("op")
	private String op;
	
	@JsonProperty("data")
	private String data;
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	
}
