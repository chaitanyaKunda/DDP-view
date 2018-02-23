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
import com.agility.ddp.data.domain.DdpRole;
import com.agility.ddp.data.domain.DdpRoleSetup;
import com.agility.ddp.view.util.Constant;
import com.agility.ddp.view.util.MetaUtil;
import com.agility.ddp.view.util.UserGroup;
import com.agility.ddp.view.util.UserRole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.beans.factory.annotation.Autowired;
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


@RooWebJson(jsonObject = DdpRoleSetup.class)
@Controller
@RequestMapping("/ddprolesetups/list")
@RooWebScaffold(path = "ddprolesetups", formBackingObject = DdpRoleSetup.class)
public class DdpRoleSetupController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(DdpRoleSetupController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     *
     * @param uiModel
     * @return
     */
    @RequestMapping(params = "searchrolesetup", produces = "text/html")
    public String searchform(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpRoleSetupController.searchform() Method Invoked and Executed."); 
        return "ddprolesetups/searchform";
    }

    private List getUserIdJdbcTemplate(String usrName, String usrLoginName, String grpName, String grpDisplayName) {
        List list = this.jdbcTemplate.query(Constant.SELECT_ASSIGNED_Role, new Object[] { usrName, usrLoginName, grpName, grpDisplayName }, new RowMapper() {

            public LinkedList<String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                LinkedList<String> useRole = new LinkedList<String>();
                useRole.add(rs.getString("usr_id"));
                useRole.add(rs.getString("usr_login_id"));
                useRole.add(rs.getString("rol_id"));
                useRole.add(rs.getString("rol_name"));
                return useRole;
            }
        });
        return list;
    }
    
    @Transactional
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid DdpRoleSetup ddpRoleSetup, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpGroupSetupController.create() Method Invoked."); 
        uiModel.asMap().clear();
        //Get Calendar Date
        Calendar cal = Calendar.getInstance();
        //Get Login User Name
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ddpRoleSetup.setRlsCreatedBy(username);
        ddpRoleSetup.setRlsCreatedDate(cal);
        ddpRoleSetup.setRlsModifiedBy(username);
        ddpRoleSetup.setRlsModifiedDate(cal);
        ddpRoleSetup.setRlsStatus(1);
        //1- User 2- Admin group
        ddpRoleSetup.setRlsClass(1);
        ddpRoleSetupService.saveDdpRoleSetup(ddpRoleSetup);
        logger.info("DdpGroupSetupController.create() Executed Successfully."); 
        return "redirect:/ddprolesetups/list/" + encodeUrlPathSegment(ddpRoleSetup.getRlsId().toString(), httpServletRequest);
    }

    /**
     *
     * @param userLogin_Id
     * @return
     */
    public List<DdpRoleSetup> getRoleDiplayName(String userLogin_Id) {
        List<DdpRoleSetup> ddGroupDisplayName = this.jdbcTemplate.query(Constant.SELECT_ASSIGNED_ROLE_DETAIL, new Object[] { userLogin_Id }, new RowMapper<DdpRoleSetup>() {

            public DdpRoleSetup mapRow(ResultSet rs, int rowNum) throws SQLException {
                DdpRoleSetup ddpRoleset = new DdpRoleSetup();
                ddpRoleset.setRlsId(rs.getInt("RLS_ID"));
                return ddpRoleset;
            }
        });
        return ddGroupDisplayName;
    }

    @RequestMapping(value = "/list/{userId}", produces = "text/html")
    public String show(@PathVariable("userId") Integer userId, Model uiModel) {
    	logger.info("DdpGroupSetupController.show() Method Invoked."); 
        addDateTimeFormatPatterns(uiModel);
        List<DdpRoleSetup> ddpRoleSetups1 = new ArrayList<DdpRoleSetup>();
        List<DdpRoleSetup> ddpGroupSetups = getRoleDiplayName(userId.toString());
        for (DdpRoleSetup ddpGroupSetup : ddpGroupSetups) {
            ddpRoleSetups1.add(ddpRoleSetupService.findDdpRoleSetup(ddpGroupSetup.getRlsId()));
        }
        List<List<Object>> lst = getUserIdJdbcTemplate(userId.toString());
        List<Object> userName = new ArrayList<Object>();
        if (!lst.isEmpty()) {
            userName.add(lst.get(0).get(1));
        }
        uiModel.addAttribute("username", userName);
        uiModel.addAttribute("ddprolesetup", ddpRoleSetups1);
        uiModel.addAttribute("itemId", userId);
        if (ddpRoleSetups1.isEmpty()) {
            return "redirect:/ddprolesetups/list?page=1&size=10";
        }
        logger.info("DdpGroupSetupController.show() Executed Successfully."); 
        return "ddprolesetups/show";
    }
    
    @Transactional
    @RequestMapping(value = "/{rlsId}/{userId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("rlsId") Integer rlsId, @PathVariable("userId") Integer userId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    	logger.info("DdpGroupSetupController.delete() Method Invoked."); 
        DdpRoleSetup ddpRoleSetup = ddpRoleSetupService.findDdpRoleSetup(rlsId);
        ddpRoleSetupService.deleteDdpRoleSetup(ddpRoleSetup);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        logger.info("DdpGroupSetupController.delete() Executed Successfully."); 
        return "redirect:/ddprolesetups/list/list/" + userId+"?search";
    }

    @RequestMapping(value = "/{usrId}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("usrId") Integer usrId, Model uiModel) {
    	logger.info("DdpGroupSetupController.updateForm() Method Invoked."); 
        List<DdpRoleSetup> ddpRoleSetups = new ArrayList<DdpRoleSetup>();
        DdpRoleSetup roleSetup = null;
        for (DdpRoleSetup ddpRoleSetup : getRoleSetupIds(usrId.toString())) {
            roleSetup = ddpRoleSetupService.findDdpRoleSetup(ddpRoleSetup.getRlsId());
            //			ddpGroupSetups.add(groupSetup.getGrsGroupId().getGrpDisplayName());
            ddpRoleSetups.add(roleSetup);
        }
        uiModel.addAttribute("rolesetup", roleSetup);
        uiModel.addAttribute("ddprolesetup1", ddpRoleSetups);
        uiModel.addAttribute("ddproleselectedlist", getRoleIDDiplayName(usrId.toString()));
        populateEditForm(uiModel, roleSetup);
        populateEditForm(uiModel, ddpRoleSetupService.findDdpRoleSetup(ddpRoleSetups.get(0).getRlsId()));
        logger.info("DdpGroupSetupController.updateForm() Executed Successfully."); 
        return "ddprolesetups/update";
    }

    /**
     *
     * @param userLogin_Id
     * @return
     */
    public List<DdpRole> getRoleIDDiplayName(String userLogin_Id) {
        List<DdpRole> ddRoleDisplayName = this.jdbcTemplate.query(Constant.SELECT_ROLE_DISPLAY_NAME, new Object[] { userLogin_Id }, new RowMapper<DdpRole>() {

            public DdpRole mapRow(ResultSet rs, int rowNum) throws SQLException {
                DdpRole ddpRole = new DdpRole();
                ddpRole.setRolId(rs.getInt("ROL_ID"));
                ddpRole.setRolDisplayName(rs.getString("ROL_DISPLAY_NAME"));
                return ddpRole;
            }
        });
        return ddRoleDisplayName;
    }
    
    @Transactional
    @RequestMapping(params = {"update"},method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid DdpRoleSetup ddpRoleSetup, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpGroupSetupController.update() Method Invoked."); 
        uiModel.asMap().clear();
        //Get User id
        String userId = httpServletRequest.getParameter("rlsChildUsrId.usrId");
        MetaUtil metaUtil = new MetaUtil();
        //Get Login User Name
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        //Clear UI Model
        uiModel.asMap().clear();
        //Get the Calendar date, created by and it can not be null
        Calendar cal = Calendar.getInstance();
        //Get selected Role list
        String[] roleList = httpServletRequest.getParameterValues("rlsChildUsrId");
        if (null == roleList) {
            //Get Group set id to update
            List<DdpRoleSetup> ddpRoleSetups = getRoleSetupIds(userId);
            for (DdpRoleSetup ddpRoleSetup1 : ddpRoleSetups) {
                ddpRoleSetupService.deleteDdpRoleSetup(ddpRoleSetup1);
            }
        } else {
            //Get List of user Group exist
            List<DdpRole> lstRole = getRoleDisplayName(userId);
            //Add ddpgroup display name into arraylist
            ArrayList dbRoleList = new ArrayList();
            for (DdpRole ddpRole : lstRole) {
                dbRoleList.add(Integer.toString(ddpRole.getRolId()));
            }
            //Convert string of array into ArrayList.
            List<String> selectedRoleList = Arrays.asList(roleList);
            // Get Update List
            Set updateRoleList = metaUtil.updateList(selectedRoleList, dbRoleList);
            // Save List
            Set saveRoleList = metaUtil.saveList(selectedRoleList, dbRoleList);
            //Delete List
            Set deleteRoleList = metaUtil.delList(selectedRoleList, dbRoleList);
            //Update Records
            for (Object selectedRoleId : updateRoleList) {
                //Get Group set id to update
                String strRoleId = getRoleIdJdbcTemplate(userId, selectedRoleId.toString());
                //Get DDPGroupset Bean for particular id
                DdpRoleSetup ddpRoleSetup2 = ddpRoleSetupService.findDdpRoleSetup(Integer.parseInt(strRoleId));
                ddpRoleSetup2.setRlsChildUsrId(ddpUserService.findDdpUser(Integer.parseInt(userId)));
                ddpRoleSetup2.setRlsModifiedBy(username);
                ddpRoleSetup2.setRlsModifiedDate(cal);
                ddpRoleSetupService.updateDdpRoleSetup(ddpRoleSetup2);
            }
            //Insert records
            for (Object object : saveRoleList) {
                DdpRoleSetup ddpRoleSetup3 = new DdpRoleSetup();
                DdpRole ddpRole = new DdpRole();
                int rlsId = Integer.parseInt(object.toString());
                ddpRole.setRolId(rlsId);
                ddpRoleSetup3.setRlsRoleId(ddpRole);
                ddpRoleSetup3.setRlsParentGrpId(null);
                ddpRoleSetup3.setRlsChildUsrId(ddpUserService.findDdpUser(Integer.parseInt(userId)));
                //1- User 2- Admin group
                ddpRoleSetup3.setRlsClass(1);
                ddpRoleSetup3.setRlsStatus(1);
                ddpRoleSetup3.setRlsCreatedBy(username);
                ddpRoleSetup3.setRlsCreatedDate(cal);
                ddpRoleSetup3.setRlsModifiedBy(username);
                ddpRoleSetup3.setRlsModifiedDate(cal);
                ddpRoleSetupService.saveDdpRoleSetup(ddpRoleSetup3);
            }
            //Delete Role
            for (Object selectedRoleId : deleteRoleList) {
                //Get Group set id to update
                String strRoleId = getRoleIdJdbcTemplate(userId, selectedRoleId.toString());
                //Get DDPGroupset Bean for particular id
                DdpRoleSetup ddpRoleSetup4 = ddpRoleSetupService.findDdpRoleSetup(Integer.parseInt(strRoleId));
                ddpRoleSetupService.deleteDdpRoleSetup(ddpRoleSetup4);
            }
        }
        //        ddpRoleSetupService.updateDdpRoleSetup(ddpRoleSetup);
        //        return "redirect:/ddprolesetups/" + encodeUrlPathSegment(ddpRoleSetup.getRlsId().toString(), httpServletRequest);
        logger.info("DdpGroupSetupController.update() Executed Successfully."); 
        return "redirect:/ddprolesetups/list/" + userId + "?search";
    }

    @RequestMapping(value = "/{usrId}", params = { "search" }, produces = "text/html")
    public String listUserRole(@PathVariable("usrId") Integer usrId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    	logger.info("DdpGroupSetupController.listUserRole() Method Invoked."); 
        List<UserRole> userRoles = new ArrayList<UserRole>();
        List<List<Object>> lst = getUserIdJdbcTemplate(usrId.toString());
        List<List<Object>> arrylist = new ArrayList<List<Object>>();
        for (List temp : lst) {
            List<Object> list = new ArrayList<Object>();
            list.add(temp.get(0).toString());
            list.add(temp.get(1).toString());
            arrylist.add(list);
        }
        List<List<Object>> duplicateRemoveuserGroups1 = new ArrayList<List<Object>>(new HashSet(arrylist));
        for (List temp : duplicateRemoveuserGroups1) {
            UserRole role = new UserRole();
            role.setUsr_id(temp.get(0).toString());
            role.setUsr_login_id(temp.get(1).toString());
            userRoles.add(role);
        }
        List<UserRole> duplicateRemoveuserGroups = new ArrayList<UserRole>(new HashSet(userRoles));
        for (UserRole roles : duplicateRemoveuserGroups) {
            String strUserId = roles.getUsr_id();
            for (List temp1 : lst) {
                if (temp1.get(0).equals(strUserId)) {
                    roles.setRol_id((roles.getRol_id() != null) ? roles.getRol_id() + "," + temp1.get(2).toString() : temp1.get(2).toString());
                    roles.setRol_name((roles.getRol_name() != null) ? roles.getRol_name() + "," + temp1.get(3).toString() : temp1.get(3).toString());
                }
            }
        }
        uiModel.addAttribute("ddprolesetups", duplicateRemoveuserGroups);
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpGroupSetupController.listUserRole() Executed Successfully."); 
        return "ddprolesetups/list";
    }

    private List<List<Object>> getUserIdJdbcTemplate(String string) {
        List list = this.jdbcTemplate.query(Constant.SELECT_ASSIGNED_ROLE_USERID, new Object[] { string }, new RowMapper() {

            public LinkedList<String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                LinkedList<String> useRole = new LinkedList<String>();
                useRole.add(rs.getString("usr_id"));
                useRole.add(rs.getString("usr_login_id"));
                useRole.add(rs.getString("rol_id"));
                useRole.add(rs.getString("rol_name"));
                return useRole;
            }
        });
        return list;
    }

    /**
     *
     * @param userLogin_Id
     * @return
     */
    public List<DdpRole> getRoleDisplayName(String userLogin_Id) {
        List<DdpRole> ddRoleDisplayName = this.jdbcTemplate.query(Constant.SELECT_ROLE_DISPLAY_NAME, new Object[] { userLogin_Id }, new RowMapper<DdpRole>() {

            public DdpRole mapRow(ResultSet rs, int rowNum) throws SQLException {
                DdpRole ddpRole = new DdpRole();
                ddpRole.setRolId(rs.getInt("ROL_ID"));
                ddpRole.setRolDisplayName(rs.getString("ROL_DISPLAY_NAME"));
                return ddpRole;
            }
        });
        return ddRoleDisplayName;
    }

    /**
     *
     * @param userLogin_Id
     * @return
     */
    public List<DdpRoleSetup> getRoleSetupIds(String userLogin_Id) {
        List<DdpRoleSetup> ddGroupGroupId = this.jdbcTemplate.query(Constant.SELECT_ROLESETUP_ID, new Object[] { userLogin_Id }, new RowMapper<DdpRoleSetup>() {

            public DdpRoleSetup mapRow(ResultSet rs, int rowNum) throws SQLException {
                DdpRoleSetup ddpRoleSetup = new DdpRoleSetup();
                ddpRoleSetup.setRlsId(rs.getInt("RLS_ID"));
                return ddpRoleSetup;
            }
        });
        return ddGroupGroupId;
    }

    /**
     *
     * @param userLogin_Id
     * @param selectedRoleId
     * @return
     */
    public String getRoleIdJdbcTemplate(String userLogin_Id, String selectedRoleId) {
        return jdbcTemplate.queryForObject(Constant.SELECT_ROLE_ID, String.class, userLogin_Id, selectedRoleId);
    }

    @RequestMapping(params="list",produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    	logger.info("DdpGroupSetupController.list() Method Invoked."); 
        List<UserRole> userRoles = new ArrayList<UserRole>();
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
            UserRole role = new UserRole();
            role.setUsr_id(temp.get(0).toString());
            role.setUsr_login_id(temp.get(1).toString());
            userRoles.add(role);
        }
        LinkedList<UserRole> duplicateRemoveuserRoles = new LinkedList<UserRole>(new LinkedHashSet(userRoles));
        for (UserRole roles : duplicateRemoveuserRoles) {
            String strUserId = roles.getUsr_id();
            for (List temp1 : lst) {
                if (temp1.get(0).equals(strUserId)) {
                    roles.setRol_id((roles.getRol_id() != null) ? roles.getRol_id() + "," + temp1.get(2).toString() : temp1.get(2).toString());
                    roles.setRol_name((roles.getRol_name() != null) ? roles.getRol_name() + "," + temp1.get(3).toString() : temp1.get(3).toString());
                }
            }
        }
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            LinkedList<UserRole> dummy = new LinkedList<UserRole>();
            for(int sIndex = firstResult;sIndex < (sizeNo * page);sIndex++){
            	if(sIndex >= duplicateRemoveuserRoles.size()) break;
            	dummy.add(duplicateRemoveuserRoles.get(sIndex));
            }
            uiModel.addAttribute("ddprolesetups", dummy);
            float nrOfPages = (float) duplicateRemoveuserRoles.size() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddprolesetups", duplicateRemoveuserRoles);
        }
        addDateTimeFormatPatterns(uiModel);
        //        uiModel.addAttribute("ddprolesetups", duplicateRemoveuserGroups);
        //        addDateTimeFormatPatterns(uiModel);
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
        //            uiModel.addAttribute("ddprolesetups", paginationList(duplicateRemoveuserGroups, firstResult, sizeNo, max));
        //            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        //        } else {
        //            uiModel.addAttribute("ddprolesetups", duplicateRemoveuserGroups);
        //        }
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpGroupSetupController.list() Executed Successfully."); 
        return "ddprolesetups/list";
    }

    private List<List<Object>> getUserIdJdbcTemplate() {
        List list = this.jdbcTemplate.query(Constant.SELECT_ALL_ASSIGNED_ROLE_USERID, new Object[] {  }, new RowMapper() {

            public LinkedList<String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                LinkedList<String> useRole = new LinkedList<String>();
                useRole.add(rs.getString("usr_id"));
                useRole.add(rs.getString("usr_login_id"));
                useRole.add(rs.getString("rol_id"));
                useRole.add(rs.getString("rol_name"));
                return useRole;
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
    @RequestMapping(params = { "txtUName", "txtLName", "txtGName", "txtGDisplayName" }, method = RequestMethod.GET, produces = "text/html")
    public String search(@RequestParam("txtUName") String usrDisplayName, @RequestParam("txtLName") String usrLoginName, @RequestParam("txtGName") String grpName, @RequestParam("txtGDisplayName") String grpDisplayName, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpGroupSetupController.search() Method Invoked."); 
        //Get Login User Name
        List<UserRole> userRoles = new ArrayList<UserRole>();
        if (!usrLoginName.equals("")) {
            usrLoginName = "%" + usrLoginName + "%";
        }
        if (!usrDisplayName.equals("")) {
            usrDisplayName = "%" + usrDisplayName + "%";
        }
        if (!grpName.equals("")) {
            grpName = "%" + grpName + "%";
        }
        if (!grpDisplayName.equals("")) {
            grpDisplayName = "%" + grpDisplayName + "%";
        }
        List<List<Object>> lst = getUserIdJdbcTemplate(usrDisplayName, usrLoginName, grpName, grpDisplayName);
        List<List<Object>> arrylist = new ArrayList<List<Object>>();
        for (List temp : lst) {
            List<Object> list = new ArrayList<Object>();
            list.add(temp.get(0).toString());
            list.add(temp.get(1).toString());
            arrylist.add(list);
        }
        List<List<Object>> duplicateRemoveuserRole = new ArrayList<List<Object>>(new HashSet(arrylist));
        for (List temp : duplicateRemoveuserRole) {
            UserRole role = new UserRole();
            role.setUsr_id(temp.get(0).toString());
            role.setUsr_login_id(temp.get(1).toString());
            userRoles.add(role);
        }
        List<UserRole> duplicateRemoveuserRole1 = new ArrayList<UserRole>(new HashSet(userRoles));
        for (UserRole role : duplicateRemoveuserRole1) {
            String strUserId = role.getUsr_id();
            for (List temp1 : lst) {
                if (temp1.get(0).equals(strUserId)) {
                    role.setRol_id((role.getRol_id() != null) ? role.getRol_id() + "," + temp1.get(2).toString() : temp1.get(2).toString());
                    role.setRol_name((role.getRol_name() != null) ? role.getRol_name() + "," + temp1.get(3).toString() : temp1.get(3).toString());
                }
            }
        }
        uiModel.addAttribute("ddprolesetups", userRoles);
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpGroupSetupController.search() Executed Successfully."); 
        return "ddprolesetups/list";
    }

    public LinkedList<UserRole> paginationList(LinkedList<UserRole> duplicateRemoveuserGroups, int firstResult, int sizeNo, int max) {
        LinkedList<UserRole> resultList = new LinkedList<UserRole>();
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
}
