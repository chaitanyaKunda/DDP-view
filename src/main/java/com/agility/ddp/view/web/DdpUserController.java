package com.agility.ddp.view.web;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.joda.time.format.DateTimeFormat; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.agility.ddp.core.logger.AuditLog;
import com.agility.ddp.data.domain.DdpGroup;
import com.agility.ddp.data.domain.DdpGroupService;
import com.agility.ddp.data.domain.DdpGroupSetup;
import com.agility.ddp.data.domain.DdpGroupSetupService;
import com.agility.ddp.data.domain.DdpRole;
import com.agility.ddp.data.domain.DdpRoleService;
import com.agility.ddp.data.domain.DdpRoleSetup;
import com.agility.ddp.data.domain.DdpRoleSetupService;
import com.agility.ddp.data.domain.DdpUser;
import com.agility.ddp.data.domain.DdpUserService;
import com.agility.ddp.view.auth.QueryActiveDirectory;
import com.agility.ddp.view.util.Constant;
import com.agility.ddp.view.util.MetaUtil;


@RooWebJson(jsonObject = DdpUser.class)
@Controller
@RequestMapping("/ddpusers/list")
@RooWebScaffold(path = "ddpusers", formBackingObject = DdpUser.class)
public class DdpUserController {

    @Autowired
    DdpRoleSetupService ddpRoleSetupService;

    @Autowired
    DdpRoleService ddpRoleService;

    @Autowired
    DdpGroupService ddpGroupService;

    @Autowired
    DdpUserService ddpUserService;

    @Autowired
    DdpGroupSetupService ddpGroupSetupService;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static final Logger logger = LoggerFactory.getLogger(DdpUserController.class);
    
  //Get Login User Name
  	public String strUserName	= ""; 
  	
  	public Calendar currentDate;
    /**
     * * @param uiModel
     * @return
     */
    @RequestMapping(value = "/form", produces = "text/html")
    public String createForm(Model uiModel) {
    	logger.info("DdpUserController.createForm(Model uiModel) Method Invoked.");
        UserWrapper user = new UserWrapper();
        populateEditForm(uiModel, user, null);
        logger.info("DdpUserController.createForm(Model uiModel) Executed Successfully.");
        return "ddpusers/create";
    }

    /**
     *
     * @param usrId
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "/{usrId}/form", produces = "text/html")
    public String updateForm(@PathVariable("usrId") Integer usrId, Model uiModel) {
    	logger.info("DdpUserController.updateForm() Method Invoked.");
        UserWrapper user = new UserWrapper();
        String userDomin = getUserDomainJdbcTemplate(usrId.toString());
        if (userDomin.equalsIgnoreCase("Logistics")) {
//            uiModel.addAttribute("logistics", "logistics");
        	DdpUser ddpUser = ddpUserService.findDdpUser(usrId);
        	 user.setDdpUser(ddpUser);
             populateEditForm(uiModel, user, usrId);
            logger.info("DdpUserController.updateForm() Executed Successfully.");
            return "ddpusers/custom_update";
//            return "redirect:/ddpusers/list/list/" + usrId;
        } else {
            DdpUser ddpUser = ddpUserService.findDdpUser(usrId);
            user.setDdpUser(ddpUser);
            populateEditForm(uiModel, user, usrId);
            logger.info("DdpUserController.updateForm() Executed Successfully.");
            return "ddpusers/update";
        }
    }

    /**
     *
     * @param usrId
     * @param grsId
     * @param rolsId
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "/list/{usrId}", produces = "text/html")
    public String show(@PathVariable("usrId") Integer usrId, Model uiModel) {
    	logger.info("DdpUserController.show() Method Invoked.");
        addDateTimeFormatPatterns(uiModel);
        List<DdpGroup> ddpGroup3 = getGroupDiplayName(usrId.toString());
        List<DdpRole> ddpRole3 = getRoleDisplayName(usrId.toString());
        uiModel.addAttribute("ddpuser", ddpUserService.findDdpUser(usrId));
        uiModel.addAttribute("ddpusergropusetup", ddpGroup3);
        uiModel.addAttribute("ddpuserrolesetup", ddpRole3);
        uiModel.addAttribute("itemId", usrId);
        logger.info("DdpUserController.show() Executed Successfully.");
        return "ddpusers/show";
    }

    /**
     *
     * @param page
     * @param size
     * @param uiModel
     * @return
     */
    @RequestMapping(params="list",produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    	  logger.info("DdpUserController.list() Method Invoked.");
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("ddpusers", ddpUserService.findDdpUserEntries(firstResult, sizeNo));
            float nrOfPages = (float) ddpUserService.countAllDdpUsers() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("ddpusers", ddpUserService.findAllDdpUsers());
        }
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpUserController.list() Executed Successfully.");
        return "ddpusers/list";
    }

    /**
     *
     * @param page
     * @param size
     * @param uiModel
     * @return
     */
    @RequestMapping(params = "searchform", produces = "text/html")
    public String searchform(Model uiModel) {
    	logger.info("DdpUserController.searchform() Method Invoked and Executed.");
        addDateTimeFormatPatterns(uiModel);
        return "ddpusers/searchform";
    }

    /**
     *
     * @param userWrapper
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
    @AuditLog(message="Searching for User. ")
    @RequestMapping(params = { "txtUName", "txtLName", "txtEmailId" }, method = RequestMethod.GET, produces = "text/html")
    public String search(Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpUserController.search(Model uiModel, HttpServletRequest httpServletRequest) Method Invoked.");
        //Get Login User Name
        String strUName = httpServletRequest.getParameter("txtUName");
        String strLName = httpServletRequest.getParameter("txtLName");
        String strEmailId = httpServletRequest.getParameter("txtEmailId");
        if (!strUName.equals("")) {
            strUName = "%" + strUName + "%";
        }
        if (!strLName.equals("")) {
            strLName = "%" + strLName + "%";
        }
        if (!strEmailId.equals("")) {
            strEmailId = "%" + strEmailId + "%";
        }
        List<Object> userId = getUserIdJdbcTemplate(strLName, strUName, strEmailId);
        int intUserId = 0;
        List<DdpUser> ddpUsers = new ArrayList<DdpUser>();
        for (Object object : userId) {
            DdpUser ddpUser = new DdpUser();
            intUserId = Integer.parseInt(object.toString());
            ddpUser = ddpUserService.findDdpUser(intUserId);
            ddpUsers.add(ddpUser);
        }
        uiModel.addAttribute("ddpusers", ddpUsers);
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpUserController.search(Model uiModel, HttpServletRequest httpServletRequest) Executed Successfully.");
        return "ddpusers/list";
    }

    /**
     *
     * @param usrId
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "/{usrId}", params = { "search" }, produces = "text/html")
    public String showList(@PathVariable("usrId") Integer usrId, Model uiModel) {
    	 logger.info("DdpUserController.showList() Method Invoked.");
        List<DdpUser> ddpUsers = new ArrayList<DdpUser>();
        addDateTimeFormatPatterns(uiModel);
        DdpUser ddpUser = ddpUserService.findDdpUser(usrId);
        ddpUsers.add(ddpUser);
        uiModel.addAttribute("ddpusers", ddpUsers);
        addDateTimeFormatPatterns(uiModel);
        logger.info("DdpUserController.showList() Executed Successfully.");
        return "ddpusers/list";
    }

    /**
     *
     * @param userWrapper
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
    @Transactional
    @AuditLog(message="New User Created. ")
    @RequestMapping(params = {"create"},method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid UserWrapper userWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpUserController.create() Method Invoked.");
        //Get custom value from httpRequest
        String domain = httpServletRequest.getParameter("stsDomain");
        int domainInt = Integer.parseInt(domain);
        if (domainInt == 0) {
            domain = Constant.INLINE;
        } else {
            domain = Constant.LOGISTICS;
        }
        //Get seleceted group and role
        String status = httpServletRequest.getParameter("status");
        String[] groupList = httpServletRequest.getParameterValues("ddpUser.ddpGroupSetups");
        String[] roleList = httpServletRequest.getParameterValues("ddpUser.ddpRoleSetups");
        //Clear the Model
        uiModel.asMap().clear();
        //set logged in user name and today's Date
        strUserName	= SecurityContextHolder.getContext().getAuthentication().getName(); 
        currentDate = Calendar.getInstance();
        userWrapper.getDdpUser().setUsrCreatedBy(strUserName);
        userWrapper.getDdpUser().setUsrModifiedBy(strUserName);
        userWrapper.getDdpUser().setUsrCreatedDate(currentDate);
        userWrapper.getDdpUser().setUsrModifiedDate(currentDate);
        userWrapper.getDdpUser().setUsrStatus(Integer.parseInt(status));
        userWrapper.getDdpUser().setUsrDomain(domain);
        //User Details
        ddpUserService.saveDdpUser(userWrapper.getDdpUser());
        int userLoginId = userWrapper.getDdpUser().getUsrId();
        //Save User Group and Role
        userWrapper.ddpUser.setUsrId(userLoginId);
        for (int i = 0; i < groupList.length; i++) {
            DdpGroup ddpGroup1 = new DdpGroup();
            DdpGroupSetup ddpGroupSetup1 = new DdpGroupSetup();
            int grsId = Integer.parseInt(groupList[i]);
            ddpGroup1.setGrpId(grsId);
            userWrapper.ddpGroup1.add(ddpGroup1);
            ddpGroupSetup1.setGrsGroupId(userWrapper.ddpGroup1.get(i));
            ddpGroupSetup1.setGrsUserId(userWrapper.ddpUser);
            ddpGroupSetup1.setGrsStatus(1);
            ddpGroupSetup1.setGrsCreatedBy(strUserName);
            ddpGroupSetup1.setGrsCreatedDate(currentDate);
            ddpGroupSetup1.setGrsModifiedBy(strUserName);
            ddpGroupSetup1.setGrsModifiedDate(currentDate);
            userWrapper.ddpGroupSetup1.add(ddpGroupSetup1);
            ddpGroupSetupService.saveDdpGroupSetup(userWrapper.getDdpGroupSetup1().get(i));
        }
        //Save User/Group Role
        for (int i = 0; i < roleList.length; i++) {
            DdpRoleSetup ddpRoleSetup1 = new DdpRoleSetup();
            DdpRole ddpRole1 = new DdpRole();
            int rlsId = Integer.parseInt(roleList[i]);
            ddpRole1.setRolId(rlsId);
            userWrapper.ddpRole1.add(ddpRole1);
            ddpRoleSetup1.setRlsRoleId(userWrapper.ddpRole1.get(i));
            ddpRoleSetup1.setRlsParentGrpId(null);
            ddpRoleSetup1.setRlsChildUsrId(userWrapper.ddpUser);
            //1- User 2- Admin group
            ddpRoleSetup1.setRlsClass(1);
            ddpRoleSetup1.setRlsStatus(1);
            ddpRoleSetup1.setRlsCreatedBy(strUserName);
            ddpRoleSetup1.setRlsCreatedDate(currentDate);
            ddpRoleSetup1.setRlsModifiedBy(strUserName);
            ddpRoleSetup1.setRlsModifiedDate(currentDate);
            userWrapper.ddpRoleSetup1.add(ddpRoleSetup1);
            ddpRoleSetupService.saveDdpRoleSetup(userWrapper.getDdpRoleSetup1().get(i));
        }
        logger.info("DdpUserController.create() Excecuted Successfully.");
        return "redirect:/ddpusers/list/list/" + userWrapper.getDdpUser().getUsrId().toString();
    }

    /**
     *
     * @param userWrapper
     * @param bindingResult
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
    @Transactional
    @AuditLog(message="User Details Updated. ")
    @RequestMapping(params = {"update"},method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid UserWrapper userWrapper, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	 logger.info("DdpUserController.update() Method Invoked.");
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, userWrapper, null);
            return "ddpusers/update";
        }
        MetaUtil metaUtil = new MetaUtil();
        //Get selected Groups and selected Roles
        String status = httpServletRequest.getParameter("status");
        String[] groupList = httpServletRequest.getParameterValues("ddpUser.ddpGroupSetups");
        String[] roleList = httpServletRequest.getParameterValues("ddpUser.ddpRoleSetups");
        //Clear UI Model
        uiModel.asMap().clear();
        //set logged in user name and today's Date
        strUserName	= SecurityContextHolder.getContext().getAuthentication().getName(); 
        currentDate = Calendar.getInstance();
        //Get User Id
        Integer ddpUserId = userWrapper.getDdpUser().getUsrId();
        DdpUser ddpUser = ddpUserService.findDdpUser(ddpUserId);
        ddpUser.setUsrModifiedBy(strUserName);
        ddpUser.setUsrModifiedDate(currentDate);
        ddpUser.setUsrBranchCode(userWrapper.getDdpUser().getUsrBranchCode());
        ddpUser.setUsrCompanyCode(userWrapper.getDdpUser().getUsrCompanyCode());
        ddpUser.setUsrCountry(userWrapper.getDdpUser().getUsrCountry());
        ddpUser.setUsrDisplayName(userWrapper.getDdpUser().getUsrDisplayName());
        ddpUser.setUsrDomain(userWrapper.getDdpUser().getUsrDomain());
        ddpUser.setUsrEmailAddress(userWrapper.getDdpUser().getUsrEmailAddress());
        ddpUser.setUsrFirstName(userWrapper.getDdpUser().getUsrFirstName());
        ddpUser.setUsrLastName(userWrapper.getDdpUser().getUsrLastName());
        ddpUser.setUsrHintAnswer(userWrapper.getDdpUser().getUsrHintAnswer());
        ddpUser.setUsrHintQuestion(userWrapper.getDdpUser().getUsrHintQuestion());
        ddpUser.setUsrMiddleName(userWrapper.getDdpUser().getUsrMiddleName());
        ddpUser.setUsrPassword(userWrapper.getDdpUser().getUsrPassword());
        ddpUser.setUsrRegion(userWrapper.getDdpUser().getUsrRegion());
        ddpUser.setUsrStatus(Integer.parseInt(status));
        //Get Saved user auto generated user id
        ddpUserService.updateDdpUser(ddpUser);
        //Get List of user Group exist
        List<DdpGroup> groups = getGroupDiplayName(ddpUserId.toString());
        //Add ddpgroup display name into arraylist
        ArrayList dbGroupList = new ArrayList();
        for (DdpGroup ddpGroup : groups) {
            dbGroupList.add(Integer.toString(ddpGroup.getGrpId()));
        }
        if (null == groupList) {
            //Get Group set id to update
            List<DdpGroupSetup> ddpGroupSetups = getGroupSetupIds(ddpUserId.toString());
            for (DdpGroupSetup ddpGroupSetup : ddpGroupSetups) {
                DdpGroupSetup groupSetup = ddpGroupSetupService.findDdpGroupSetup(ddpGroupSetup.getGrsId());
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
            //Save User Group and Role
            userWrapper.ddpUser.setUsrId(ddpUserId);
            //Update Records
            for (Object selectedGroupId : updateGroupList) {
                //Get Group set id to update
                String strGrpId = getGroupIdJdbcTemplate(userWrapper.getDdpUser().getUsrId().toString(), selectedGroupId.toString());
                //Get DDPGroupset Bean for particular id
                DdpGroupSetup ddpGroupSetup = ddpGroupSetupService.findDdpGroupSetup(Integer.parseInt(strGrpId));
                ddpGroupSetup.setGrsUserId(userWrapper.ddpUser);
                ddpGroupSetup.setGrsModifiedBy(strUserName);
                ddpGroupSetup.setGrsModifiedDate(currentDate);
                ddpGroupSetupService.updateDdpGroupSetup(ddpGroupSetup);
            }
            //Delete group
            for (Object selectedGroupId : deleteGroupList) {
                //Get Group set id to update
                String strGrpId = getGroupIdJdbcTemplate(userWrapper.getDdpUser().getUsrId().toString(), selectedGroupId.toString());
                //Get DDPGroupset Bean for particular id
                DdpGroupSetup ddpGroupSetup = ddpGroupSetupService.findDdpGroupSetup(Integer.parseInt(strGrpId));
                ddpGroupSetupService.deleteDdpGroupSetup(ddpGroupSetup);
            }
            //Insert records
            for (Object object : saveGroupList) {
                int grsId = Integer.parseInt(object.toString());
                DdpGroup ddpGroup = new DdpGroup();
                DdpGroupSetup ddpGroupSetup = new DdpGroupSetup();
                ddpGroup.setGrpId(grsId);
                ddpGroupSetup.setGrsGroupId(ddpGroup);
                ddpGroupSetup.setGrsUserId(userWrapper.ddpUser);
                ddpGroupSetup.setGrsStatus(1);
                ddpGroupSetup.setGrsCreatedBy(strUserName);
                ddpGroupSetup.setGrsCreatedDate(currentDate);
                ddpGroupSetup.setGrsModifiedBy(strUserName);
                ddpGroupSetup.setGrsModifiedDate(currentDate);
                ddpGroupSetupService.saveDdpGroupSetup(ddpGroupSetup);
            }
           
        }
        if (null == roleList) {
            //Get Group set id to update
            List<DdpRoleSetup> ddpRoleSetups = getRoleSetupIds(ddpUserId.toString());
            for (DdpRoleSetup ddpRoleSetup : ddpRoleSetups) {
                ddpRoleSetupService.deleteDdpRoleSetup(ddpRoleSetup);
            }
        } else {
            //Get List of user Group exist
            List<DdpRole> lstRole = getRoleDisplayName(ddpUserId.toString());
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
                String strRoleId = getRoleIdJdbcTemplate(userWrapper.getDdpUser().getUsrId().toString(), selectedRoleId.toString());
                //Get DDPGroupset Bean for particular id
                DdpRoleSetup ddpRoleSetup = ddpRoleSetupService.findDdpRoleSetup(Integer.parseInt(strRoleId));
                ddpRoleSetup.setRlsChildUsrId(userWrapper.ddpUser);
                ddpRoleSetup.setRlsModifiedBy(strUserName);
                ddpRoleSetup.setRlsModifiedDate(currentDate);
                ddpRoleSetupService.updateDdpRoleSetup(ddpRoleSetup);
            }
            //Delete Role
            for (Object selectedRoleId : deleteRoleList) {
                //Get Group set id to update
                String strRoleId = getRoleIdJdbcTemplate(userWrapper.getDdpUser().getUsrId().toString(), selectedRoleId.toString());
                //Get DDPGroupset Bean for particular id
                DdpRoleSetup ddpRoleSetup = ddpRoleSetupService.findDdpRoleSetup(Integer.parseInt(strRoleId));
                ddpRoleSetupService.deleteDdpRoleSetup(ddpRoleSetup);
            }
            //Insert records
            for (Object object : saveRoleList) {
                DdpRoleSetup ddpRoleSetup = new DdpRoleSetup();
                DdpRole ddpRole = new DdpRole();
                int rlsId = Integer.parseInt(object.toString());
                ddpRole.setRolId(rlsId);
                ddpRoleSetup.setRlsRoleId(ddpRole);
                ddpRoleSetup.setRlsParentGrpId(null);
                ddpRoleSetup.setRlsChildUsrId(userWrapper.ddpUser);
                //1- User 2- Admin group
                ddpRoleSetup.setRlsClass(1);
                ddpRoleSetup.setRlsStatus(1);
                ddpRoleSetup.setRlsCreatedBy(strUserName);
                ddpRoleSetup.setRlsCreatedDate(currentDate);
                ddpRoleSetup.setRlsModifiedBy(strUserName);
                ddpRoleSetup.setRlsModifiedDate(currentDate);
                ddpRoleSetupService.saveDdpRoleSetup(ddpRoleSetup);
            }
           
        }
        logger.info("DdpUserController.update() Executed Successfully.");
        return "redirect:/ddpusers/list/list/" + userWrapper.getDdpUser().getUsrId().toString();
    }
    @Transactional
    @AuditLog("User Deleted. ")
    @RequestMapping(value = "/{usrId}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("usrId") Integer usrId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    	logger.info("DdpUserController.delete() Method Invoked.");
        try {
            //Get Group set id to update
            List<DdpGroupSetup> ddpGroupSetups = getGroupSetupIds(usrId.toString());
            for (DdpGroupSetup ddpGroupSetup : ddpGroupSetups) {
                DdpGroupSetup groupSetup = ddpGroupSetupService.findDdpGroupSetup(ddpGroupSetup.getGrsId());
                ddpGroupSetupService.deleteDdpGroupSetup(groupSetup);
            }
            //Get Group set id to update
            List<DdpRoleSetup> ddpRoleSetups = getRoleSetupIds(usrId.toString());
            for (DdpRoleSetup ddpRoleSetup : ddpRoleSetups) {
                DdpRoleSetup groupSetup = ddpRoleSetupService.findDdpRoleSetup(ddpRoleSetup.getRlsId());
                ddpRoleSetupService.deleteDdpRoleSetup(groupSetup);
            }
            DdpUser ddpUser = ddpUserService.findDdpUser(usrId);
            ddpUserService.deleteDdpUser(ddpUser);
        } catch (Exception e) {
        	logger.info("DdpUserController.delete() : Exception while Deleting User.");
            //need to do something for transcation failure
            e.printStackTrace();
        }
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        logger.info("DdpUserController.delete() Executed Successfully.");
        return "redirect:/ddpusers/list?list&page=1&size=10";
    }

    /**
     *
     * @param uiModel
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(params = { "ad", "LoginId" }, method = RequestMethod.GET, produces = "text/html")
    public String getUserDetailFromAD(Model uiModel, HttpServletRequest httpServletRequest) {
    	logger.info("DdpUserController.getUserDetailFromAD() Method Invoked.");
        // Need to write the business.
        String strLoginId = httpServletRequest.getParameter("LoginId");
        System.out.println(strLoginId);
        try {
            Map<Object, Object> adMap = new QueryActiveDirectory().adDetails(strLoginId);
            Integer userId = getUserExitence(strLoginId);
            UserWrapper user = new UserWrapper();
            if (userId > 0) {
                uiModel.addAttribute("UserExist", "User already exists");
                populateEditForm(uiModel, user, null);
                return "ddpusers/create";
            } else if (null == adMap) {
                uiModel.addAttribute("UserExist", "User not in control");
                populateEditForm(uiModel, user, null);
                return "ddpusers/create";
            } else {
                user.ddpUser.setUsrLoginId(adMap.get("sAMAccountName").toString());
                user.ddpUser.setUsrFirstName(adMap.get("givenName").toString());
                user.ddpUser.setUsrLastName(adMap.get("sn").toString());
                user.ddpUser.setUsrDisplayName(adMap.get("displayName").toString());
                user.ddpUser.setUsrEmailAddress(adMap.get("mail").toString());
                user.ddpUser.setUsrCountry(adMap.get("co").toString());
                user.ddpUser.setUsrCompanyCode(adMap.get("company").toString());
                if (adMap.get("l") != null)
                	user.ddpUser.setUsrBranchCode(adMap.get("l").toString()); 
                //			uiModel.addAttribute("activeDirectory",adMap);
                //			populateEditForm(uiModel, user,null);
                //			response.setCharacterEncoding("UTF-8");
                //	        response.setContentType("text/html");
                //	        response.getWriter().write(adMap);
                uiModel.addAttribute("UserExist", "");
                populateEditForm(uiModel, user, null);
            }
        } catch (Exception e) {
        	logger.info("DdpUserController.getUserDetailFromAD() : Exception while getting Logged in User Details.");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("DdpUserController.getUserDetailFromAD() Executed Successfully.");
        return "ddpusers/create";
    }

    /**
     *
     * @param uiModel
     * @param userWrapper
     * @param userId
     */
    void populateEditForm(Model uiModel, UserWrapper userWrapper, Integer userId) {
    	 logger.info("DdpUserController.populateEditForm() Method Invoked.");
        uiModel.addAttribute("userWrapper", userWrapper);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddprolesetups", ddpRoleSetupService.findAllDdpRoleSetups());
        uiModel.addAttribute("ddpgroupsetups", ddpGroupSetupService.findAllDdpGroupSetups());
        //If user if is not null, prepare the list of selected user
        if (userId != null) {
            uiModel.addAttribute("ddpgroupselectedlist", getGroupDiplayName(userId.toString()));
            uiModel.addAttribute("ddproleselectedlist", getRoleDisplayName(userId.toString()));
        } else {
            uiModel.addAttribute("ddpgroupselectedlist", new ArrayList<DdpGroup>());
            uiModel.addAttribute("ddproleselectedlist", new ArrayList<DdpRole>());
        }
        //Prepare list to get the User Display Name
        DdpUser ddpUser = new DdpUser();
        List<DdpUser> displayUserName = new ArrayList<DdpUser>();
        for (DdpUser ddpUser2 : ddpUserService.findAllDdpUsers()) {
            ddpUser = new DdpUser();
            ddpUser.setUsrDisplayName(ddpUser2.getUsrDisplayName());
            ddpUser.setUsrId(ddpUser2.getUsrId());
            displayUserName.add(ddpUser);
        }
        //Prepare list to get the Group Display Name
        DdpGroup lstGroup = new DdpGroup();
        List<DdpGroup> displayGroupName = new ArrayList<DdpGroup>();
        for (DdpGroup t : ddpGroupService.findAllDdpGroups()) {
            lstGroup = new DdpGroup();
            lstGroup.setGrpDisplayName(t.getGrpDisplayName());
            lstGroup.setGrpId(t.getGrpId());
            displayGroupName.add(lstGroup);
        }
        uiModel.addAttribute("ddpgroupsdisname", displayGroupName);
        //Prepare list to get the Role Display Name
        DdpRole ddpRole = new DdpRole();
        List<DdpRole> displayRoleName = new ArrayList<DdpRole>();
        for (DdpRole ddpRole2 : ddpRoleService.findAllDdpRoles()) {
            ddpRole = new DdpRole();
            ddpRole.setRolDisplayName(ddpRole2.getRolDisplayName());
            ddpRole.setRolId(ddpRole2.getRolId());
            ddpRole.setRolDescription("");
            ddpRole.setRolName("");
            displayRoleName.add(ddpRole);
        }
        uiModel.addAttribute("ddproledisplayname", displayRoleName);
        logger.info("DdpUserController.populateEditForm() Executed Successfully.");
    }

    /**
     *
     * @param pathSegment
     * @param httpServletRequest
     * @return
     */
    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }

    /**
     *
     * @param userDomain
     * @return
     */
    public String getUserDomainJdbcTemplate(String userDomain) {
        return jdbcTemplate.queryForObject(Constant.DEF_SQL_SELECT_USER_DOMAIN, String.class, userDomain);
    }

    /**
     *
     * @param userLogin_Id
     * @return
     */
    public List<Object> getUserIdJdbcTemplate(String userLogin_Id, String userDispalyName, String emailId) {
        List<Object> ddUserId = this.jdbcTemplate.query(Constant.SELECT_USER_ID, new Object[] { userLogin_Id, userDispalyName, emailId }, new RowMapper<Object>() {

            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                Object userId;
                userId = rs.getObject("usr_id");
                return userId;
            }
        });
        return ddUserId;
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

    /**
     *
     * @param userLogin_Id
     * @param selectedRoleId
     * @return
     */
    public String getRoleIdJdbcTemplate(String userLogin_Id, String selectedRoleId) {
        return jdbcTemplate.queryForObject(Constant.SELECT_ROLE_ID, String.class, userLogin_Id, selectedRoleId);
    }

    /**
     *
     * @param userLogin_Id
     * @return
     */
    public List<DdpGroup> getGroupDiplayName(String userLogin_Id) {
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
     * @param uiModel
     * @param ddpUser
     * @param ddpRoleSetup
     * @param ddpGroupSetup
     */
    void populateEditForm(Model uiModel, DdpUser ddpUser, DdpRoleSetup ddpRoleSetup, DdpGroupSetup ddpGroupSetup) {
        uiModel.addAttribute("ddpUser", ddpUser);
        uiModel.addAttribute("ddpRoleSetup", ddpRoleSetup);
        uiModel.addAttribute("ddpGroupSetup", ddpGroupSetup);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("ddpusers", ddpUserService.findAllDdpUsers());
        uiModel.addAttribute("ddproles", ddpRoleService.findAllDdpRoles());
    }

    /**
     *
     * @param uiModel
     */
    void addDateTimeFormatPatterns(Model uiModel) {
        //User
        uiModel.addAttribute("ddpUser_usrcreateddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpUser_usrmodifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        //Role setup
        uiModel.addAttribute("ddpRoleSetup_rlscreateddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpRoleSetup_rlsmodifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        //Group Setup
        uiModel.addAttribute("ddpGroupSetup_grscreateddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("ddpGroupSetup_grsmodifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

    //Check user existence with id
    public Integer getUserExitence(String userLogin_Id) {
        return jdbcTemplate.queryForObject(Constant.SQL_USER_COUNT, Integer.class, userLogin_Id);
    }

/*	@RequestMapping(value = "/{usrId}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("usrId") Integer usrId) {
        DdpUser ddpUser = ddpUserService.findDdpUser(usrId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (ddpUser == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ddpUserService.deleteDdpUser(ddpUser);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }*/
}
