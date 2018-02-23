package com.agility.ddp.view.web;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.agility.ddp.core.quartz.DdpMultiAedRuleJob;
import com.agility.ddp.core.quartz.RuleJob;
import com.agility.ddp.core.task.DdpCategorizationTask;
import com.agility.ddp.core.task.DdpInitiateProcessTask;
import com.agility.ddp.core.task.DdpUpdateMetadataTask;
import com.agility.ddp.data.domain.DdpCategorizedDocs;
import com.agility.ddp.data.domain.DdpCategorizedDocsService;
import com.agility.ddp.data.domain.DdpDmsDocsDetail;
import com.agility.ddp.data.domain.DdpDmsDocsDetailService;
import com.agility.ddp.data.domain.DdpEmailTriggerSetup;
import com.agility.ddp.data.domain.DdpRuleDetail;
import com.agility.ddp.view.util.CategorizedDocsWrapper;
import com.agility.ddp.view.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@RooWebJson(jsonObject = DdpCategorizedDocs.class)
@Controller
@RequestMapping({"/ddpcategorizeddocses"})
@RooWebScaffold(path = "ddpcategorizeddocses", formBackingObject = DdpCategorizedDocs.class)
public class DdpCategorizedDocsController {

    @Autowired
    DdpCategorizedDocsService ddpCategorizedDocsService;

    @Autowired
    DdpDmsDocsDetailService ddpDmsDocsDetailService;

    @Autowired
    DdpCategorizationTask categorizationTask;
    
    @Autowired
    DdpUpdateMetadataTask updateMetadataTask;
    
    @Autowired
    DdpInitiateProcessTask initiateProcessTask;
    
    @Autowired
    RuleJob ruleJob;
    
    @Autowired
    DdpMultiAedRuleJob multiAedRuleJob;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    Environment env;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "docsmediator")
    public String docDetailsMediator(@RequestParam(value = "docs", required = false) Integer docs) {
        if (docs == null) return "ddpaedrules/categorizedDocs"; else return "ddpaedrules/categorizedDocsDetails";
    }

    /**
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "categorizedDocs", headers="Accept=Application/json", method = RequestMethod.GET)
    @ResponseBody
    public Map listCategorizedDocs(@RequestParam(value = "page", required = false) Integer page) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        List<CategorizedDocsWrapper> categorizedDocsWrappers = null;
        categorizedDocsWrappers = findAllCategorizedDocsWrappers();
        map.put("rows", categorizedDocsWrappers);
        map.put("page", page);
        return map;
    }

    @RequestMapping(value = "docsDetails", headers = "Accept=application/json", method = RequestMethod.GET)
    @ResponseBody
    public Map<Object, Object> listCategorizedDocsDetails(@RequestParam(value = "page", required = false) Integer page) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        ArrayList<Object> records = new ArrayList<Object>();
        List<DdpDmsDocsDetail> result = null;
        result = getDocsDetails();
//        HashMap<Object, Object>[] rowData = new HashMap[result.size()];
//        for (int i = 0; i < result.size(); i++) {
//            rowData[i] = new HashMap<Object, Object>();
//            rowData[i].put("docsDetailsId", result.get(i).getDddId());
//            rowData[i].put("docsDtxId", result.get(i).getDddDtxId().getDtxId());
//            rowData[i].put("docsRObjectId", result.get(i).getDddRObjectId());
//            rowData[i].put("docsControlDocType", result.get(i).getDddControlDocType());
//            rowData[i].put("docsCompanySource", result.get(i).getDddCompanySource());
//            rowData[i].put("docsBranchSource", result.get(i).getDddBranchSource());
//            rowData[i].put("docsCompanyDestination", result.get(i).getDddCompanyDestination());
//            rowData[i].put("docsBranchDestination", result.get(i).getDddBranchDestination());
//            rowData[i].put("docsJobNumber", result.get(i).getDddJobNumber());
//            records.add(rowData[i]);
//        }
        map.put("rows", result);
        map.put("page", page);
        return map;
    }

    /**
     *
     * @param catId
     * @return status
     */
    @RequestMapping(value = "/reProcessAED/{catId}")
    @ResponseBody
    public int reProcess(@PathVariable Integer catId) {
        DdpCategorizedDocs categorizedDocs = ddpCategorizedDocsService.findDdpCategorizedDocs(catId);
        ruleJob.callWorkFlow(new Object[] { categorizedDocs });
        int status = categorizedDocs.getCatStatus();
        return status;
    }
    @RequestMapping(value = "/reProcessMultiAED/{catId}")
    @ResponseBody
    public int reProcessMultiAedRule(@PathVariable Integer catId)
    {
    	DdpCategorizedDocs categorizedDocs = ddpCategorizedDocsService.findDdpCategorizedDocs(catId);
    	List<DdpRuleDetail> ddpRuleDetails = multiAedRuleJob.getAllRuleDetails(categorizedDocs.getCatRulId().getRulId());
    	DdpEmailTriggerSetup ddpEmailTriggerSetup = multiAedRuleJob.getDdpEmailTriggerSetup(categorizedDocs.getCatRulId().getRulId());
    	multiAedRuleJob.executeJob(ddpRuleDetails, ddpEmailTriggerSetup, null, null, 3,null);
    	int status = categorizedDocs.getCatStatus();
    	return status;
    }
    public DdpDmsDocsDetail getDmsDocsDetail(int dtxId) {
        int detailId = jdbcTemplate.queryForObject(Constant.DDPDMSDOCSDETAILBYDTXID, new Object[] { dtxId }, Integer.class);
        return ddpDmsDocsDetailService.findDdpDmsDocsDetail(detailId);
    }
    @RequestMapping(value = "/reProcessSynWithRange/{minSynID}/{maxSynID}")
    @ResponseBody
    public String reprocessSynIdWithRange(@PathVariable(value = "minSynID") int minSynID,@PathVariable(value = "maxSynID") int maxSynID)
    {
    	try{
    	categorizationTask.executeBetweenSynIDs(minSynID, maxSynID);
    	return "success";
    	}catch(Exception ex)
    	{
    		ex.printStackTrace();
    		return "failure";
    	}
    }
    @RequestMapping(value="/reprocessUpdateMetadataJobWithRange/{minChlID}/{maxChlID}")
    @ResponseBody
    public String reprocessChlIDwithRange(@PathVariable(value = "minChlID") int minChlID, @PathVariable(value = "maxChlID") int maxChlID)
    {
    	try{
    		updateMetadataTask.processChlIDRange(minChlID, maxChlID);
    		return "success";
    	}catch (Exception e) {
    		e.printStackTrace();
    		return "failure";
		}
    }
    @RequestMapping(value="/reprocessInitiateJobWithRange/{minChlID}/{maxChlID}")
    @ResponseBody
    public String reprocessCatIDwithRange(@PathVariable(value = "minChlID") int minChlID, @PathVariable(value = "maxChlID") int maxChlID)
    {
    	try{
    		initiateProcessTask.processCatIDRange(minChlID, maxChlID);
    		return "success";
    	}catch (Exception e) {
    		e.printStackTrace();
    		return "failure";
		}
    }
    /**
     *
     * @return
     */
    public List<DdpCategorizedDocs> getCategorizedDocs() {
        List<DdpCategorizedDocs> ddpCategorizedDocs = this.jdbcTemplate.query(Constant.CATEGORIZEDDOCSDESC, new RowMapper<DdpCategorizedDocs>() {

            @Override
            public DdpCategorizedDocs mapRow(ResultSet rs, int rowNum) throws SQLException {
                DdpCategorizedDocs categorizedDocs = ddpCategorizedDocsService.findDdpCategorizedDocs(rs.getInt("CAT_ID"));
                return categorizedDocs;
            }
        });
        return ddpCategorizedDocs;
    }

    /**
     *
     * @return
     */
    public List<DdpDmsDocsDetail> getDocsDetails() {
        List<DdpDmsDocsDetail> dmsDocsDetails = this.jdbcTemplate.query(Constant.DMSDOCSDETAILSDESC, new DdpDmsDocsDetail());
        return dmsDocsDetails;
    }
    
    public List<CategorizedDocsWrapper> findAllCategorizedDocsWrappers()
    {
    	List<CategorizedDocsWrapper> categorizedDocsWrappers = this.jdbcTemplate.query(Constant.CATEGORIZEDDOCSDESC, new CategorizedDocsWrapper());
    	return categorizedDocsWrappers;
    }
    public List<CategorizedDocsWrapper> findCategorizedDocsWrappersByParam(int catID)
    {
    	List<CategorizedDocsWrapper> categorizedDocsWrappers = this.jdbcTemplate.query(Constant.SELECT_CATEGORIZEDDOCS_BY_PARAM,new Object[] {catID}, new CategorizedDocsWrapper());
    	return categorizedDocsWrappers;
    }
}
