package com.agility.ddp.view.web;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid; 

import com.agility.ddp.data.domain.DdpGroup;
import com.agility.ddp.data.domain.DdpGroupSetup;
import com.agility.ddp.data.domain.DdpUserService;
import com.agility.ddp.view.util.Constant;
import com.agility.ddp.view.util.MetaUtil;
import com.agility.ddp.view.util.UserGroup;
import com.agility.ddp.view.util.UserRole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;


@RooWebJson(jsonObject = DdpGroupSetup.class)
@Controller
@RequestMapping("/ddpgroupsetups/list")
@RooWebScaffold(path = "ddpgroupsetups", formBackingObject = DdpGroupSetup.class)
public class DdpGroupSetupController {
	
	private static final Logger logger = LoggerFactory.getLogger(DdpGroupSetupController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     *
     * @param uiModel
     * @return
     */
    @RequestMapping(params = "searchgroupsetup", produces = "text/html")
    public String searchform(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpGroupSetupController.searchform() Method Invoked and Executed."); 
        return "ddpgroupsetups/searchform";
    }

    /**
     *
     * @param usrName
     * @param usrLoginName
     * @return
     */
    private List getUserIdJdbcTemplate(String usrName, String usrLoginName) {
        List list = this.jdbcTemplate.query(Constant.SELECT_ASSIGNED_GROUP, new Object[] { usrName, usrLoginName }, new RowMapper() {

            public LinkedList<String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                LinkedList<String> usegroup = new LinkedList<String>();
                usegroup.add(rs.getString("usr_id"));
                usegroup.add(rs.getString("usr_login_id"));
                usegroup.add(rs.getString("grp_id"));
                usegroup.add(rs.getString("grp_name"));
                return usegroup;
            }
        });
        return list;
    }

    /**
     *
     * @param userId
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "/list/{userId}", produces = "text/html")
    public String show(@PathVariable("userId") Integer userId, Model uiModel) {
    	 logger.info("DdpGroupSetupController.show() Method Invoked."); 
        addDateTimeFormatPatterns(uiModel);
        List<DdpGroupSetup> ddpGroupSetups1 = new ArrayList<DdpGroupSetup>();
        List<DdpGroupSetup> ddpGroupSetups = getGroupDiplayName(userId.toString());
        for (DdpGroupSetup ddpGroupSetup : ddpGroupSetups) {
            ddpGroupSetups1.add(ddpGroupSetupService.findDdpGroupSetup(ddpGroupSetup.getGrsId()));
        }
        List<List<Object>> lst = getUserDetailForGivenUserIDJdbcTemplate(userId.toString());
        List<Object> userName = new ArrayList<Object>();
        if (!lst.isEmpty()) {
            userName.add(lst.get(0).get(1));
        }
        uiModel.addAttribute("username", userName);
        uiModel.addAttribute("ddpgroupsetup", ddpGroupSetups1);
        uiModel.addAttribute("itemSubId", userId);
        if (ddpGroupSetups1.isEmpty()) {
            return "redirect:/ddpgroupsetups/list?page=1&size=10";
        }
        logger.info("DdpGroupSetupController.show() Executed Successfully."); 
        return "ddpgroupsetups/show";
    }

    /**
     *
     * @param userLogin_Id
     * @return
     */
    public List<DdpGroupSetup> getGroupDiplayName(String userLogin_Id) {
        List<DdpGroupSetup> ddGroupDisplayName = this.jdbcTemplate.query(Constant.SELECT_ASSIGNED_GROUP_DETAIL, new Object[] { userLogin_Id }, new RowMapper<DdpGroupSetup>() {

            public DdpGroupSetup mapRow(ResultSet rs, int rowNum) throws SQLException {
                DdpGroupSetup ddpGroupset = new DdpGroupSetup();
                ddpGroupset.setGrsId(rs.getInt("grs_id"));
                return ddpGroupset;
            }
        });
        return ddGroupDisplayName;
    }

    /**
     *
     * @param userLogin_Id
     * @return
     */
    public List<DdpGroup> getGroupIDDiplayName(String userLogin_Id) {
        List<DdpGroup> ddGroupDisplayName = this.jdbcTemplate.query(Constant.SELECT_GROUP_DISPLAY_NAME, new Object[] { userLogin_Id }, new RowMapper<DdpGroup>() {

            public DdpGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
                DdpGroup ddpGroup = new DdpGroup();
                ddpGroup.setGrpId(rs.getInt("GRP_ID"));
                ddpGroup.setGrpDisplayName(rs.getString("GRP_DISPLAY_NAME"));
                return ddpGroup;
            }
        });
        return ddGroupDisplayName;
    }

    /**
     *
     * @param grsId
     * @param userId
     * @param page
     * @param size
     * @param uiModel
     * @return
     */
    @Transactional
    @RequestMapping(value = "/{grsId}/{userId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("grsId") Integer grsId, @PathVariable("userId") Integer userId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        DdpGroupSetup ddpGroupSetup = ddpGroupSetupService.findDdpGroupSetup(grsId);
        ddpGroupSetupService.deleteDdpGroupSetup(ddpGroupSetup);
        uiModel.asMap().clear();
        //        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        //        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpgroupsetups/list/list/" + userId+"?search";
    }
    @Transactional
    @RequestMapping(value = "/true/{userId}", method = RequestMethod.DELETE, produces = "text/html")
    public String deleteAllGropuForUser(@PathVariable("userId") Integer userId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        List<DdpGroupSetup> ddpGroupSetup = getGroupSetupIds(userId.toString());
        for (DdpGroupSetup groupSetup : ddpGroupSetup) {
            ddpGroupSetupService.deleteDdpGroupSetup(groupSetup);
        }
        uiModel.asMap().clear();
        //        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        //        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/ddpgroupsetups/list/list/" + userId;
    }

    /**
     *
     * @param usrId
     * @param page
     * @param size
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "/{usrId}", params = { "search" }, produces = "text/html")
    public String listUserGroup(@PathVariable("usrId") Integer usrId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    	logger.info("DdpGroupSetupController.listUserGroup() Method Invoked."); 
        List<UserGroup> userGroups = new ArrayList<UserGroup>();
        List<List<Object>> lst = getUserDetailForGivenUserIDJdbcTemplate(usrId.toString());
        List<List<Object>> arrylist = new ArrayList<List<Object>>();
        for (List temp : lst) {
            List<Object> list = new ArrayList<Object>();
            list.add(temp.get(0).toString());
            list.add(temp.get(1).toString());
            arrylist.add(list);
        }
        List<List<Object>> duplicateRemoveuserGroups1 = new ArrayList<List<Object>>(new HashSet(arrylist));
        for (List temp : duplicateRemoveuserGroups1) {
            UserGroup group = new UserGroup();
            group.setUserId(temp.get(0).toString());
            group.setUserLoginId(temp.get(1).toString());
            userGroups.add(group);
        }
        List<UserGroup> duplicateRemoveuserGroups = new ArrayList<UserGroup>(new HashSet(userGroups));
        for (UserGroup group : duplicateRemoveuserGroups) {
            String strUserId = group.getUserId();
            for (List temp1 : lst) {
                if (temp1.get(0).equals(strUserId)) {
                    group.setGrsId((group.getGrsId() != null) ? group.getGrsId() + "," + temp1.get(2).toString() : temp1.get(2).toString());
                    group.setGrsName((group.getGrsName() != null) ? group.getGrsName() + "," + temp1.get(3).toString() : temp1.get(3).toString());
                }
            }
        }
        uiModel.addAttribute("ddpgroupsetups", duplicateRemoveuserGroups);
        addDateTimeFormatPatterns(uiModel);
        //        if (page != null || size != null) {
        //            int sizeNo = size == null ? 10 : size.intValue();
        //            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
        //            uiModel.addAttribute("ddpaedrules", ddpAedRuleService.findDdpAedRuleEntries(firstResult, sizeNo));
        //            float nrOfPages = (float) ddpAedRuleService.countAllDdpAedRules() / sizeNo;
        //            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        //        } else {
        //            uiModel.addAttribute("ddpaedrules", ddpAedRuleService.findAllDdpAedRules());
        //        }
        //        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpGroupSetupController.listUserGroup() Executed Successfully."); 
        return "ddpgroupsetups/list";
    }

    /**
     *
     * @param usrId
     * @return
     */
    private List getUserDetailForGivenUserIDJdbcTemplate(String usrId) {
        List list = this.jdbcTemplate.query(Constant.SELECT_ASSIGNED_GROUP_USERID, new Object[] { usrId }, new RowMapper() {

            public LinkedList<String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                LinkedList<String> usegroup = new LinkedList<String>();
                usegroup.add(rs.getString("usr_id"));
                usegroup.add(rs.getString("usr_login_id"));
                usegroup.add(rs.getString("grp_id"));
                usegroup.add(rs.getString("grp_name"));
                return usegroup;
            }
        });
        return list;
    }

    /**
     *
     * @param usrId
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "/{usrId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("usrId") Integer usrId, Model uiModel) {
    	logger.info("DdpGroupSetupController.updateForm() Method Invoked."); 
        List<DdpGroupSetup> ddpGroupSetups = new ArrayList<DdpGroupSetup>();
        DdpGroupSetup groupSetup = null;
        for (DdpGroupSetup ddpGroupSetup : getGroupSetupIds(usrId.toString())) {
            groupSetup = ddpGroupSetupService.findDdpGroupSetup(ddpGroupSetup.getGrsId());
            //			ddpGroupSetups.add(groupSetup.getGrsGroupId().getGrpDisplayName());
            ddpGroupSetups.add(groupSetup);
        }
        uiModel.addAttribute("groupsetup", groupSetup);
        uiModel.addAttribute("ddpgroupsetup1", ddpGroupSetups);
        uiModel.addAttribute("ddpgroupselectedlist", getGroupIDDiplayName(usrId.toString()));
        populateEditForm(uiModel, groupSetup);
        logger.info("DdpGroupSetupController.updateForm() Executed Successfully."); 
        return "ddpgroupsetups/update";
    }

    /**
     *
     * @param userLogin_Id
     * @return
     */
    public List<DdpGroupSetup> getGroupSetupIds(String userLogin_Id) {
        List<DdpGroupSetup> ddGroupGroupId = this.jdbcTemplate.query(Constant.SELECT_GROUPSETUP_ID, new Object[] { userLogin_Id }, new RowMapper<DdpGroupSetup>() {

            public DdpGroupSetup mapRow(ResultSet rs, int rowNum) throws SQLException {
                DdpGroupSetup ddpGroupSetup = new DdpGroupSetup();
                ddpGroupSetup.setGrsId(rs.getInt("GRS_ID"));
                return ddpGroupSetup;
            }
        });
        return ddGroupGroupId;
    }

    /**
     *
     * @param userLogin_Id
     * @param selectedGrpId
     * @return
     */
    public String deleteGroupByUserId(String userLogin_Id) {
        return jdbcTemplate.queryForObject(Constant.DEL_USERGTOUP, String.class, userLogin_Id);
    }

    /**
     *
     * @param ddpGroupSetup
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
    @Transactional
    @RequestMapping(params = {"update"},method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid DdpGroupSetup ddpGroupSetup, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpGroupSetupController.update() Method Invoked."); 
        uiModel.asMap().clear();
        //Get User id
        String userId = httpServletRequest.getParameter("grsUserId.usrId");
        //Get selected Groups and selected Roles
        String[] groupList = httpServletRequest.getParameterValues("grsUserId");
        MetaUtil metaUtil = new MetaUtil();
        //Get Login User Name
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //Clear UI Model
        uiModel.asMap().clear();
        //Get the Calendar date, created by and it can not be null
        Calendar cal = Calendar.getInstance();
        //Get List of user Group exist
        List<DdpGroup> groups = getGroupIDDiplayName(userId);
        //Add ddpgroup display name into arraylist
        ArrayList dbGroupList = new ArrayList();
        for (DdpGroup ddpGroup : groups) {
            dbGroupList.add(Integer.toString(ddpGroup.getGrpId()));
        }
        if (null == groupList) {
            //Get Group set id to update
            List<DdpGroupSetup> ddpGroupSetups = getGroupSetupIds(userId);
            for (DdpGroupSetup ddpGroupSetup1 : ddpGroupSetups) {
                DdpGroupSetup groupSetup = ddpGroupSetupService.findDdpGroupSetup(ddpGroupSetup1.getGrsId());
                ddpGroupSetupService.deleteDdpGroupSetup(groupSetup);
            }
        } else {
            //Convert string of array into ArrayList.
            List<String> selectedGroupList = Arrays.asList(groupList);
            // Get Update List
            Set updateGroupList = metaUtil.updateList(selectedGroupList, dbGroupList);
            // Save List
            Set saveGroupList = metaUtil.saveList(selectedGroupList, dbGroupList);
            //Delete List
            Set deleteGroupList = metaUtil.delList(selectedGroupList, dbGroupList);
            //Update Records
            for (Object selectedGroupId : updateGroupList) {
                //Get Group set id to update
                String strGrpId = getGroupIdJdbcTemplate(userId, selectedGroupId.toString());
                //Get DDPGroupset Bean for particular id
                DdpGroupSetup ddpGroupSetup1 = ddpGroupSetupService.findDdpGroupSetup(Integer.parseInt(strGrpId));
                ddpGroupSetup1.setGrsUserId(ddpUserService.findDdpUser(Integer.parseInt(userId)));
                ddpGroupSetup1.setGrsModifiedBy(username);
                ddpGroupSetup1.setGrsModifiedDate(cal);
                ddpGroupSetupService.updateDdpGroupSetup(ddpGroupSetup1);
            }
            //Insert records
            for (Object object : saveGroupList) {
                int grsId = Integer.parseInt(object.toString());
                DdpGroup ddpGroup = new DdpGroup();
                DdpGroupSetup ddpGroupSetup2 = new DdpGroupSetup();
                ddpGroup.setGrpId(grsId);
                ddpGroupSetup2.setGrsGroupId(ddpGroup);
                ddpGroupSetup2.setGrsUserId(ddpUserService.findDdpUser(Integer.parseInt(userId)));
                ddpGroupSetup2.setGrsStatus(1);
                ddpGroupSetup2.setGrsCreatedBy(username);
                ddpGroupSetup2.setGrsCreatedDate(cal);
                ddpGroupSetup2.setGrsModifiedBy(username);
                ddpGroupSetup2.setGrsModifiedDate(cal);
                ddpGroupSetupService.saveDdpGroupSetup(ddpGroupSetup2);
            }
            //Delete group-
            for (Object selectedGroupId : deleteGroupList) {
                //Get Group set id to update
                String strGrpId = getGroupIdJdbcTemplate(userId, selectedGroupId.toString());
                //Get DDPGroupset Bean for particular id
                DdpGroupSetup ddpGroupSetup3 = ddpGroupSetupService.findDdpGroupSetup(Integer.parseInt(strGrpId));
                ddpGroupSetupService.deleteDdpGroupSetup(ddpGroupSetup3);
            }
        }
        //        ddpGroupSetupService.updateDdpGroupSetup(ddpGroupSetup);
        logger.info("DdpGroupSetupController.update() Executed Successfully."); 
        return "redirect:/ddpgroupsetups/list/list/" + userId + "?search";
    }

    @RequestMapping(value = "/{grsId}", params = { "showgroupdetail" }, produces = "text/html")
    public String showGroupDetails(@PathVariable("grsId") Integer grsId, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpgroupsetup", ddpGroupSetupService.findDdpGroupSetup(grsId));
        uiModel.addAttribute("itemId", grsId);
        return "ddpgroupsetups/showall";
    }

    /**
     *
     * @param userLogin_Id
     * @param selectedGrpId
     * @return
     */
    public String getGroupIdJdbcTemplate(String userLogin_Id, String selectedGrpId) {
        return jdbcTemplate.queryForObject(Constant.SELECT_GROUP_ID, String.class, userLogin_Id, selectedGrpId);
    }

    @RequestMapping(params="list",produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        //Get Login User Name
        List<UserGroup> userGroups = new ArrayList<UserGroup>();
        List<List<Object>> lst = getUserIdJdbcTemplate();
        List<List<Object>> arrylist = new ArrayList<List<Object>>();
        for (List temp : lst) {
            List<Object> list = new ArrayList<Object>();
            list.add(temp.get(0).toString());
            list.add(temp.get(1).toString());
            arrylist.add(list);
        }
        List<List<Object>> duplicateRemoveuserGroups1 = new ArrayList<List<Object>>(new LinkedHashSet(arrylist));
        for (List temp : duplicateRemoveuserGroups1) {
            UserGroup group = new UserGroup();
            group.setUserId(temp.get(0).toString());
            group.setUserLoginId(temp.get(1).toString());
            userGroups.add(group);
        }
        LinkedList<UserGroup> duplicateRemoveuserGroups = new LinkedList<UserGroup>(new LinkedHashSet(userGroups));
        for (UserGroup group : duplicateRemoveuserGroups) {
            String strUserId = group.getUserId();
            for (List temp1 : lst) {
                if (temp1.get(0).equals(strUserId)) {
                    group.setGrsId((group.getGrsId() != null) ? group.getGrsId() + "," + temp1.get(2).toString() : temp1.get(2).toString());
                    group.setGrsName((group.getGrsName() != null) ? group.getGrsName() + "," + temp1.get(3).toString() : temp1.get(3).toString());
                }
            }
        }
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            LinkedList<UserGroup> dummy = new LinkedList<UserGroup>();
            for(int sIndex = firstResult;sIndex < (sizeNo * page);sIndex++){
            	if(sIndex >= duplicateRemoveuserGroups.size()) break;
            	dummy.add(duplicateRemoveuserGroups.get(sIndex));
            }
            uiModel.addAttribute("ddpgroupsetups", dummy);
            float nrOfPages = (float) duplicateRemoveuserGroups.size() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
        		            uiModel.addAttribute("ddpgroupsetups", duplicateRemoveuserGroups);
        }
        addDateTimeFormatPatterns(uiModel);
        //        uiModel.addAttribute("ddpgroupsetups", duplicateRemoveuserGroups);
        //        if (page != null || size != null) {
        //            int sizeNo = size == null ? 10 : size.intValue();
        //            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
        //            float nrOfPages = (float) duplicateRemoveuserGroups.size() / sizeNo;
        //            int max = (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages);
        //            if (max == page) {
        //                max = duplicateRemoveuserGroups.size();
        //            } else {
        //                max = 0;
        //            }
        //            uiModel.addAttribute("ddpgroupsetups", paginationList(duplicateRemoveuserGroups, firstResult, sizeNo, max));
        //            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        //        } else {
        //            uiModel.addAttribute("ddpgroupsetups", duplicateRemoveuserGroups);
        //        }
        //        addDateTimeFormatPatterns(uiModel);
        return "ddpgroupsetups/list";
    }

    /**
     *
     * @return
     */
    private List<List<Object>> getUserIdJdbcTemplate() {
        List list = this.jdbcTemplate.query(Constant.SELECT_ALL_ASSIGNED_GROUP_USERID, new Object[] {  }, new RowMapper() {

            public LinkedList<String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                LinkedList<String> usegroup = new LinkedList<String>();
                usegroup.add(rs.getString("usr_id"));
                usegroup.add(rs.getString("usr_login_id"));
                usegroup.add(rs.getString("grp_id"));
                usegroup.add(rs.getString("grp_name"));
                return usegroup;
            }
        });
        return list;
    }

    /**
     *
     * @param userWrapper
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(params = { "txtUName", "txtLName", "txtEmailId" }, method = RequestMethod.GET, produces = "text/html")
    public String search(@RequestParam("txtUName") String usrDisplayName, @RequestParam("txtLName") String usrLoginName, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpGroupSetupController.search() Method Invoked."); 
        //Get Login User Name
        List<UserGroup> userGroups = new ArrayList<UserGroup>();
        if (!usrLoginName.equals("")) {
            usrLoginName = "%" + usrLoginName + "%";
        }
        if (!usrDisplayName.equals("")) {
            usrDisplayName = "%" + usrDisplayName + "%";
        }
        List<List<Object>> lst = getUserIdJdbcTemplate(usrDisplayName, usrLoginName);
        List<List<Object>> arrylist = new ArrayList<List<Object>>();
        for (List temp : lst) {
            List<Object> list = new ArrayList<Object>();
            list.add(temp.get(0).toString());
            list.add(temp.get(1).toString());
            arrylist.add(list);
        }
        List<List<Object>> duplicateRemoveuserGroups1 = new ArrayList<List<Object>>(new HashSet(arrylist));
        for (List temp : duplicateRemoveuserGroups1) {
            UserGroup group = new UserGroup();
            group.setUserId(temp.get(0).toString());
            group.setUserLoginId(temp.get(1).toString());
            userGroups.add(group);
        }
        @SuppressWarnings("unchecked") List<UserGroup> duplicateRemoveuserGroups = new ArrayList<UserGroup>(new HashSet(userGroups));
        for (UserGroup group : duplicateRemoveuserGroups) {
            String strUserId = group.getUserId();
            for (List temp1 : lst) {
                if (temp1.get(0).equals(strUserId)) {
                    group.setGrsId((group.getGrsId() != null) ? group.getGrsId() + "," + temp1.get(2).toString() : temp1.get(2).toString());
                    group.setGrsName((group.getGrsName() != null) ? group.getGrsName() + "," + temp1.get(3).toString() : temp1.get(3).toString());
                }
            }
        }
        uiModel.addAttribute("ddpgroupsetups", duplicateRemoveuserGroups);
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpGroupSetupController.search() Executed Successfully."); 
        return "ddpgroupsetups/list";
    }

    /**
     *
     * @param duplicateRemoveuserGroups
     * @param firstResult
     * @param sizeNo
     * @param max
     * @return
     */
    public LinkedList<UserGroup> paginationList(LinkedList<UserGroup> duplicateRemoveuserGroups, int firstResult, int sizeNo, int max) {
        LinkedList<UserGroup> resultList = new LinkedList<UserGroup>();
        if (max != 0) {
            sizeNo = max;
        } else {
            if (firstResult == 0) {
                sizeNo = 10;
            } else if (firstResult == sizeNo) {
                sizeNo = sizeNo + 10;
            } else {
                sizeNo = firstResult + 10;
            }
        }
        for (int i = firstResult; i < sizeNo; i++) {
            resultList.add(duplicateRemoveuserGroups.get(i));
        }
        return resultList;
    }
    
    @Transactional
	@RequestMapping(params = {"create"},method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid DdpGroupSetup ddpGroupSetup, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		logger.info("DdpGroupSetupController.create() Method Invoked."); 
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, ddpGroupSetup);
            return "ddpgroupsetups/create";
        }
        uiModel.asMap().clear();
        ddpGroupSetupService.saveDdpGroupSetup(ddpGroupSetup);
        logger.info("DdpGroupSetupController.create() Executed Successfully."); 
        return "redirect:/ddpgroupsetups/list/list/" + encodeUrlPathSegment(ddpGroupSetup.getGrsId().toString(), httpServletRequest);
    }

/*	@RequestMapping(value = "/{grsId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("grsId") Integer grsId) {
        DdpGroupSetup ddpGroupSetup = ddpGroupSetupService.findDdpGroupSetup(grsId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpGroupSetup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpGroupSetupService.deleteDdpGroupSetup(ddpGroupSetup);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }*/
}
