package com.agility.ddp.view.web;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agility.ddp.data.domain.DdpAuditTxn;
import com.agility.ddp.data.domain.DdpAuditTxnService;
import com.agility.ddp.view.util.Constant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@RooWebJson(jsonObject = DdpAuditTxn.class)
@Controller
@RequestMapping("/ddpaudittxns")
@RooWebScaffold(path = "ddpaudittxns", formBackingObject = DdpAuditTxn.class)
public class DdpAuditTxnController {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@RequestMapping(value={"auditMediator"})
	public String auditMediator()
	{
		return "ddpaedrules/auditLogs";
	}
	
	@RequestMapping(value="auditLogs",headers="accept=Application/json",method=RequestMethod.GET)
	@ResponseBody
	public Map listAuditLogs(@RequestParam(value="page",required=false) Integer page)
	{
		Map<Object, Object> map = new HashMap<Object,Object>();
		ArrayList<Object> records = new ArrayList<Object>();
		List<DdpAuditTxn> result = null;
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
//        result = ddpAuditTxnService.findAllDdpAuditTxns();
        result = getAuditRecords();
		HashMap<Object,Object>[] rowData = new HashMap[result.size()];
		for(int i =0;i<result.size();i++)
        {
        	rowData[i] = new HashMap<Object,Object>();
        	rowData[i].put("auditId", result.get(i).getAtxId());
        	rowData[i].put("evtName", result.get(i).getAtxEventName());
        	rowData[i].put("evtSource", result.get(i).getAtxEventSource());
        	rowData[i].put("evtObjectName", result.get(i).getAtxObjectName());
        	rowData[i].put("appName", result.get(i).getAtxApplicationName());
        	rowData[i].put("detail", result.get(i).getAtxDetail());
        	rowData[i].put("createdBy", result.get(i).getAtxCreatedBy());
        	rowData[i].put("createdDate", result.get(i).getAtxCreatedDate());
        	records.add(rowData[i]);
        }
        map.put("rows", records);
        map.put("page", page);
		return map;
	}
	
	public List<DdpAuditTxn> getAuditRecords()
	{
		List<DdpAuditTxn> auditTxns = this.jdbcTemplate.query(Constant.AUDITTXNDESC, new RowMapper<DdpAuditTxn>() {

			@Override
			public DdpAuditTxn mapRow(ResultSet rs, int rowNum)	throws SQLException {
				 DdpAuditTxn auditTxn = new DdpAuditTxn();
				 auditTxn.setAtxId(Integer.parseInt(rs.getString("ATX_ID")));
				 auditTxn.setAtxTxnId(rs.getString("ATX_TXN_ID"));
				 auditTxn.setAtxEventName(rs.getString("ATX_EVENT_NAME"));
				 auditTxn.setAtxEventSource(rs.getString("ATX_EVENT_SOURCE"));
				 auditTxn.setAtxObjectName(rs.getString("ATX_OBJECT_NAME"));
				 auditTxn.setAtxApplicationName(rs.getString("ATX_APPLICATION_NAME"));
				 auditTxn.setAtxDetail(rs.getString("ATX_DETAIL"));
				 auditTxn.setAtxCreatedBy(rs.getString("ATX_CREATED_BY"));
				 if(rs.getTimestamp("ATX_CREATED_DATE") == null){ auditTxn.setAtxCreatedDate(null);}
				 else
				 {
					Calendar createdDate = Calendar.getInstance();
					createdDate.setTime(rs.getTimestamp("ATX_CREATED_DATE"));
					auditTxn.setAtxCreatedDate(createdDate);
				 }
				return auditTxn;
			}
		});
		return auditTxns;
	}
}
