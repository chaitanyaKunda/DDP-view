package com.agility.ddp.view.util;

import java.util.Calendar;
import org.springframework.security.core.context.SecurityContextHolder;
//constant class
public class Constant {
	
	public static final String SQL_USER_COUNT 					 =   "SELECT COUNT(*) FROM ddp_user where usr_login_id = ?";
	
//	public static final String DEF_SQL_SELECT_USER_ID			 =	 "SELECT usr_id FROM ddp_user where  usr_login_id=?";
	
	public static final String DEF_SQL_SELECT_USER_DOMAIN		 =	 "SELECT USR_DOMAIN FROM ddp_user where usr_id=?";
	
	public static final String DEF_SQL_SELECT_ROLES				 =	 "SELECT u.usr_login_id USERNAME, g.grp_name, r.rol_name ROLENAME FROM ddp_user u, ddp_group_setup gs, ddp_group g, ddp_role_setup rs, ddp_role r WHERE gs.grs_user_id = u.usr_id and gs.grs_group_id = g.grp_id and rs.rls_role_id = r.rol_id and u.usr_login_id=?";
	
	public static final String SELECT_GROUP_DISPLAY_NAME		 =	 "SELECT a.GRP_ID,a.GRP_DISPLAY_NAME FROM DDP_GROUP a INNER JOIN DDP_GROUP_SETUP b ON a.GRP_ID = b.GRS_GROUP_ID where b.GRS_USER_ID =? ";  
	
	public static final String SELECT_ROLE_DISPLAY_NAME			 =	 "SELECT a.ROL_ID,a.ROL_DISPLAY_NAME FROM DDP_ROLE a INNER JOIN DDP_ROLE_SETUP b ON a.ROL_ID = b.RLS_ROLE_ID where b.RLS_CHILD_USR_ID = ?";
	
	public static final String SELECT_GROUP_ID					 =	 "select grs_id from ddp_group_setup where GRS_USER_ID = ? and grs_group_id = ? ";
	
	public static final String SELECT_ROLE_ID					 =	 "select RLS_ID from ddp_role_setup where RLS_CHILD_USR_ID  = ? and RLS_ROLE_ID = ? ";
	
	public static final String SELECT_GROUP_IDS					 = 	 "SELECT grs_id FROM ddp_group_setup where GRS_USER_ID = ?";
	
	public static final String SELECT_ROLE_IDS					 =	 "SELECT RLS_ID FROM ddp_role_setup where RLS_CHILD_USR_ID = ?";
	
//	public static final String SELECT_GROUPSETUP_ID ="select grs_id,GRS_GROUP_ID,GRS_USER_ID,GRS_STATUS,GRS_CREATED_BY,GRS_CREATED_DATE,GRS_MODIFIED_BY ,GRS_MODIFIED_DATE from ddp_group_setup where GRS_USER_ID = ?";
	
	public static final String SELECT_GROUPSETUP_ID				 =	"select GRS_ID from ddp_group_setup where GRS_USER_ID = ?";
	
	public static final String SELECT_ROLESETUP_ID				 =	"select RLS_ID from ddp_role_setup where RLS_CHILD_USR_ID = ?";
	
	public static final String SELECT_USER_ID					 =	"SELECT usr_id FROM ddp_user where usr_login_id LIKE  ?  or USR_DISPLAY_NAME LIKE  ? or USR_EMAIL_ADDRESS LIKE ? ";
	
	public static final String SELECT_ASSIGNED_GROUP			 =	"select du.usr_id, du.usr_login_id, dg.grp_id, dg.grp_name from ddp_group_setup dgs, "
																	+" ddp_group dg, ddp_user du where dgs.grs_group_id = dg.grp_id	"
																	+" and dgs.grs_user_id = du.usr_id and grs_user_id in "
																	+" (select distinct usr_id from ddp_user where  usr_display_name LIKE ?  "
																	+" or  usr_login_id LIKE ? )" ;
	
	public static final String SELECT_ASSIGNED_Role				 =	"select du.usr_id, du.usr_login_id, dr.rol_id, dr.rol_name  from ddp_role_setup drs, ddp_role dr, ddp_user du where drs.rls_child_usr_id = du.usr_id and drs.rls_role_id = dr.rol_id "
																	+ " and (  drs.rls_class = 1 and drs.rls_child_usr_id in ( select distinct usr_id from ddp_user where usr_display_name LIKE ?  or usr_login_id LIKE ? ))  "
																	+ "  or (drs.rls_class = 2 and drs.rls_parent_grp_id in ( select distinct grp_id from ddp_group  where grp_name LIKE ? or grp_display_name LIKE ? )) ";

	public static final String SELECT_ASSIGNED_ROLE_USERID	 	=	"select du.usr_id, du.usr_login_id, dr.rol_id, dr.rol_name  from ddp_role_setup drs, ddp_role dr, ddp_user du where drs.rls_child_usr_id = du.usr_id and drs.rls_role_id = dr.rol_id "
																	+ " and (  drs.rls_class = 1 and drs.rls_child_usr_id in ( select distinct usr_id from ddp_user where usr_id = ?  ))  ";

	public static final String SELECT_ALL_ASSIGNED_ROLE_USERID	=	"select du.usr_id, du.usr_login_id, dr.rol_id, dr.rol_name  from ddp_role_setup drs, ddp_role dr, ddp_user du where drs.rls_child_usr_id = du.usr_id and drs.rls_role_id = dr.rol_id "
																	+ " and (  drs.rls_class = 1 and drs.rls_child_usr_id in ( select distinct usr_id from ddp_user ))  ";

	
	public static final String SELECT_ASSIGNED_GROUP_DETAIL		=	"select a.grs_id from DDP_GROUP_SETUP a  inner join DDP_GROUP b ON b.GRP_ID = a.GRS_GROUP_ID  where GRS_USER_ID = ?";
	
	public static final String SELECT_ASSIGNED_ROLE_DETAIL		= 	"select a.rls_id from DDP_Role_SETUP a inner join DDP_Role b ON "
														   			+ " b.ROL_ID = a.RLS_ROLE_ID  where RLS_CHILD_USR_ID = ? ";
	
	public static final String SELECT_ASSIGNED_GROUP_USERID		=	"select du.usr_id, du.usr_login_id, dg.grp_id, dg.grp_name"
																	+" from ddp_group_setup dgs, "
																	+" ddp_group dg, ddp_user du where dgs.grs_group_id = dg.grp_id	"
																	+" and dgs.grs_user_id = du.usr_id and grs_user_id in "
																	+" (select distinct usr_id from ddp_user where usr_id = ? )" ;
	
	public static final String SELECT_ALL_ASSIGNED_GROUP_USERID	=	"select du.usr_id, du.usr_login_id, dg.grp_id, dg.grp_name"
																	+" from ddp_group_setup dgs, "
																	+" ddp_group dg, ddp_user du where dgs.grs_group_id = dg.grp_id	"
																	+" and dgs.grs_user_id = du.usr_id and grs_user_id in "
																	+" (select distinct usr_id from ddp_user )" ;
	
	public static final String DEL_USERGTOUP					=	"delete from DDP_GROUP_SETUP where GRS_USER_ID = ?";
	
	public static final String SELECT_BRANCH_BY_COMPANY			=	"select * from DDP_BRANCH where BRN_COMPNAY_CODE = ? and BRN_STATUS = 0 ORDER BY BRN_BRANCH_CODE";
	
	public static final String SELECTED_RULE					= 	"select DISTINCT b.RUL_ID,a.RDT_ID,rdt_company,rdt_branch,RDT_PARTY_CODE, rdt_doc_type,c.CEM_EMAIL_FROM,c.CEM_EMAIL_TO,c.CEM_EMAIL_CC,c.CEM_EMAIL_SUBJECT,c.CEM_EMAIL_BODY,d.COP_OPTION"
																	+ " from DDP_RULE_DETAIL a"
																	+ " left join DDP_RULE b on a.RDT_RULE_ID = b.RUL_ID"
																	+ " left join DDP_COMM_EMAIL c on a.RDT_COMMUNICATION_ID = c.CEM_EMAIL_ID"
																	+ " left join DDP_COMM_OPTIONS_SETUP d on a.RDT_ID = d.COP_RDT_ID  where b.rul_id= ?";
	
	public static final String SELECT_RULEDETAILFORRULEID		=	"SELECT DISTINCT RUL_ID,RDT_COMPANY,RDT_BRANCH,RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_COMMUNICATION_ID,CE.CEM_EMAIL_FROM,CE.CEM_EMAIL_TO,CE.CEM_EMAIL_CC,CE.CEM_EMAIL_BODY,CE.CEM_EMAIL_SUBJECT,RDT_ID "
																	+" FROM( "
																	+" SELECT DISTINCT RUL_ID,RDT_COMPANY,RDT_BRANCH,RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_COMMUNICATION_ID,RDT_ID"
																	+" FROM DDP_RULE R"
																	+" LEFT JOIN DDP_RULE_DETAIL RD ON R.RUL_ID=RD.RDT_RULE_ID"
																	+" WHERE RUL_ID= ?"
																	+" )A"
																	+" LEFT JOIN DDP_COMM_EMAIL CE ON A.RDT_COMMUNICATION_ID=CE.CEM_EMAIL_ID ";
	
	public static final String SELECT_OPTION					=	"select * from DDP_COMM_OPTIONS_SETUP  where COP_RDT_ID = ?";
	
	public static final String SELECTEDRULEDETAILBYID			= "select DISTINCT EM.RUL_ID,EM.RDT_COMPANY,EM.RDT_BRANCH,EM.RDT_DOC_TYPE,EM.RDT_PARTY_CODE,EM.RDT_COMMUNICATION_ID,EM.CEM_EMAIL_FROM,EM.CEM_EMAIL_TO,EM.CEM_EMAIL_CC,EM.CEM_EMAIL_BODY,EM.CEM_EMAIL_SUBJECT, "
																  +" EM.RDT_ID,EM.COP_OPTION AS EMAIL,PR.COP_OPTION AS PRINTING " 	
																  +" FROM"
																  +" (SELECT DISTINCT * "
																  +" FROM("
																  +" SELECT DISTINCT RUL_ID,RDT_COMPANY,RDT_BRANCH,RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_COMMUNICATION_ID,CE.CEM_EMAIL_FROM,CE.CEM_EMAIL_TO,CE.CEM_EMAIL_CC,CE.CEM_EMAIL_BODY,RDT_ID,CE.CEM_EMAIL_SUBJECT "
																  +" FROM("
																  +" SELECT DISTINCT RUL_ID,RDT_COMPANY,RDT_BRANCH,RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_COMMUNICATION_ID,RDT_ID"
																  +" FROM DDP_RULE R"
																  +" LEFT JOIN DDP_RULE_DETAIL RD ON R.RUL_ID=RD.RDT_RULE_ID"
																  +" WHERE RUL_ID= ?"
																  +" )A"
																  +" LEFT JOIN (select CEM_EMAIL_ID,CEM_EMAIL_CC,CEM_EMAIL_SUBJECT,CEM_EMAIL_TO,CEM_EMAIL_FROM,CEM_EMAIL_BODY,e.CMS_COMMUNICATION_ID from DDP_COMM_EMAIL r "
																  +" left join DDP_COMMUNICATION_SETUP e on e.CMS_PROTOCOL_SETTINGS_ID = r.CEM_EMAIL_ID) "
																  +" CE ON A.RDT_COMMUNICATION_ID=CE.CMS_COMMUNICATION_ID " 
																  +" )B"
																  +" LEFT JOIN (SELECT COP_OPTION,COP_RDT_ID FROM DDP_COMM_OPTIONS_SETUP where COP_OPTION = 'Emailing') CO ON B.RDT_ID=CO.COP_RDT_ID "
																  +" ) EM"
																  +" left join (SELECT DISTINCT * "
																  +" FROM("
																  +" SELECT DISTINCT RUL_ID,RDT_COMPANY,RDT_BRANCH,RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_COMMUNICATION_ID,CE1.CEM_EMAIL_FROM,CE1.CEM_EMAIL_TO,CE1.CEM_EMAIL_CC,CE1.CEM_EMAIL_BODY,RDT_ID "
																  +" FROM("
																  +" SELECT DISTINCT RUL_ID,RDT_COMPANY,RDT_BRANCH,RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_COMMUNICATION_ID,RDT_ID"
																  +" FROM DDP_RULE R1"
																  +" LEFT JOIN DDP_RULE_DETAIL RD1 ON R1.RUL_ID=RD1.RDT_RULE_ID"
																  +" WHERE RUL_ID= ?"
																  +" )A1"
																  +" LEFT JOIN (select CEM_EMAIL_ID,CEM_EMAIL_CC,CEM_EMAIL_SUBJECT,CEM_EMAIL_TO ,CEM_EMAIL_FROM,CEM_EMAIL_BODY,e.CMS_COMMUNICATION_ID from DDP_COMM_EMAIL r"
																  +" left join DDP_COMMUNICATION_SETUP e on e.CMS_PROTOCOL_SETTINGS_ID = r.CEM_EMAIL_ID)"
																  +" CE1 ON A1.RDT_COMMUNICATION_ID=CE1.CMS_COMMUNICATION_ID"
																  +" )B1"
																  +" LEFT JOIN (SELECT COP_OPTION,COP_RDT_ID FROM DDP_COMM_OPTIONS_SETUP where cop_option = 'Printing') CO1 ON B1.RDT_ID=CO1.COP_RDT_ID "
																  +" ) PR"
																  +" ON PR.RDT_ID = EM.RDT_ID";
	
	public static final String SELECTEDRULEDETAILBYID1			= " select DISTINCT EM.RUL_ID,EM.RDT_COMPANY,EM.RDT_BRANCH,EM.RDT_DOC_TYPE,EM.RDT_PARTY_CODE,"
																  +" EM.RDT_COMMUNICATION_ID,EM.CEM_EMAIL_FROM,EM.CEM_EMAIL_TO,EM.CEM_EMAIL_CC, "
																  +" EM.CEM_EMAIL_BODY,EM.CEM_EMAIL_SUBJECT, "
																  +" EM.RDT_ID,EM.COP_OPTION AS EMAIL,PR.COP_OPTION AS PRINTING  " 	
																  +" FROM ("
																  +" SELECT DISTINCT * " 
																  +" FROM ( "
																  +" SELECT DISTINCT RUL_ID,RDT_COMPANY,RDT_BRANCH, "
																  +" RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_COMMUNICATION_ID,CE.CEM_EMAIL_FROM, "
																  +" CE.CEM_EMAIL_TO,CE.CEM_EMAIL_CC,CE.CEM_EMAIL_BODY,RDT_ID,CE.CEM_EMAIL_SUBJECT " 
																  +" FROM ( "
																  +" select MAX(RUL_ID) as RUL_ID ,max(RDT_COMPANY) as RDT_COMPANY,max(RDT_BRANCH) as RDT_BRANCH, "
																  +" RDT_DOC_TYPE,RDT_PARTY_CODE,"
																  +" max(RDT_COMMUNICATION_ID) as RDT_COMMUNICATION_ID , max(RDT_ID) as RDT_ID from ( "
																  +" SELECT DISTINCT RUL_ID,RDT_COMPANY, "
																  +" ( select distinct ( RDT_BRANCH+',') "
																  +" from DDP_RULE_DETAIL "
																  +" where  RDT_RULE_ID= ? " 
																  +" For XML PATH('') ) " 
																  +" as RDT_BRANCH,RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_COMMUNICATION_ID,RDT_ID"
																  +" FROM DDP_RULE R "
																  +" LEFT JOIN DDP_RULE_DETAIL RD ON R.RUL_ID=RD.RDT_RULE_ID "
																  +" WHERE RUL_ID= ?"
																  +" )dis group by RDT_PARTY_CODE,RDT_DOC_TYPE"
																  +" )A"
																  +" LEFT JOIN (select CEM_EMAIL_ID,CEM_EMAIL_CC,CEM_EMAIL_SUBJECT,CEM_EMAIL_TO,CEM_EMAIL_FROM,CEM_EMAIL_BODY,e.CMS_COMMUNICATION_ID" 
																  +" from DDP_COMM_EMAIL r " 
																  +" left join DDP_COMMUNICATION_SETUP e on e.CMS_PROTOCOL_SETTINGS_ID = r.CEM_EMAIL_ID)" 
																  +" CE ON A.RDT_COMMUNICATION_ID=CE.CMS_COMMUNICATION_ID "  
																  +" )B "
																  +" LEFT JOIN (SELECT COP_OPTION,COP_RDT_ID FROM DDP_COMM_OPTIONS_SETUP where COP_OPTION = 'Emailing') "
																  +" CO ON B.RDT_ID=CO.COP_RDT_ID " 
																  +" ) EM"
																  +" left join ("
																  +" SELECT DISTINCT * " 
																  +" FROM("
																  +" SELECT DISTINCT RUL_ID ,RDT_COMPANY,RDT_BRANCH,RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_COMMUNICATION_ID, "
																  +" CEM_EMAIL_FROM, CEM_EMAIL_TO,CEM_EMAIL_CC,CEM_EMAIL_BODY,RDT_ID"
																  +" FROM("
																  +" select MAX(RUL_ID) as RUL_ID ,max(RDT_COMPANY) as RDT_COMPANY,max(RDT_BRANCH) as RDT_BRANCH, "
																  +" RDT_DOC_TYPE,RDT_PARTY_CODE,"
																  +" max(RDT_COMMUNICATION_ID) as RDT_COMMUNICATION_ID , max(RDT_ID) as RDT_ID from ("
																  +" SELECT DISTINCT   RUL_ID, RDT_COMPANY, "
																  +" ( select distinct ( RDT_BRANCH+',') "
																  +" from DDP_RULE_DETAIL"
																  +"		where  RDT_RULE_ID= ?" 
																  +"			For XML PATH('') )"	
																  +"		as RDT_BRANCH, RDT_DOC_TYPE,RDT_PARTY_CODE, RDT_COMMUNICATION_ID, RDT_ID"
																  +"	  FROM DDP_RULE R1"
																  +"	  LEFT JOIN DDP_RULE_DETAIL RD1 ON R1.RUL_ID=RD1.RDT_RULE_ID"
																  +"	  WHERE RUL_ID= ?" 
																  +" ) dis group by RDT_PARTY_CODE,RDT_DOC_TYPE"
																  +" )A1"  
																  +" LEFT JOIN (select CEM_EMAIL_ID,CEM_EMAIL_CC,CEM_EMAIL_SUBJECT,CEM_EMAIL_TO ,CEM_EMAIL_FROM,CEM_EMAIL_BODY,e.CMS_COMMUNICATION_ID"  
																  +" from DDP_COMM_EMAIL r "
																  +" left join DDP_COMMUNICATION_SETUP e on e.CMS_PROTOCOL_SETTINGS_ID = r.CEM_EMAIL_ID) "
																  +" CE1 ON A1.RDT_COMMUNICATION_ID=CE1.CMS_COMMUNICATION_ID "
																  +" )B1 "
																  +" LEFT JOIN (SELECT COP_OPTION,COP_RDT_ID FROM DDP_COMM_OPTIONS_SETUP where cop_option = 'Printing') CO1 " 
																  +" ON B1.RDT_ID=CO1.COP_RDT_ID  "
																  +" ) PR" 
																  +" ON PR.RDT_ID = EM.RDT_ID";
	
	
	public static final String SELECTEDRULEDETAILBYIDVIEW		= "  select DISTINCT EM.RUL_ID,EM.RDT_COMPANY,EM.RDT_BRANCH,EM.RDT_DOC_TYPE,EM.RDT_PARTY_CODE,EM.RDT_PARTY_ID,"
																   +" EM.RDT_COMMUNICATION_ID,EM.CEM_EMAIL_FROM,EM.CEM_EMAIL_TO,EM.CEM_EMAIL_CC," 
																   +" EM.CEM_EMAIL_BODY,EM.CEM_EMAIL_SUBJECT," 
																   +" EM.RDT_ID,EM.COP_OPTION AS EMAIL,PR.COP_OPTION AS PRINTING"   	
																   +" FROM ("
																   +" SELECT DISTINCT *"  
																   +" FROM (" 
																   +" SELECT DISTINCT RUL_ID,RDT_COMPANY,RDT_BRANCH, RDT_PARTY_ID,"
																   +" RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_COMMUNICATION_ID,CE.CEM_EMAIL_FROM," 
																   +" CE.CEM_EMAIL_TO,CE.CEM_EMAIL_CC,CE.CEM_EMAIL_BODY,RDT_ID,CE.CEM_EMAIL_SUBJECT"  
																   +" FROM (" 
																   +" select MAX(RUL_ID) as RUL_ID ,max(RDT_COMPANY) as RDT_COMPANY,max(RDT_BRANCH) as RDT_BRANCH," 
																   +" RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_PARTY_ID,"
																   +" max(RDT_COMMUNICATION_ID) as RDT_COMMUNICATION_ID , max(RDT_ID) as RDT_ID from (" 
																   +" SELECT DISTINCT RUL_ID,RDT_COMPANY," 
																   +" dbo.Fn_Branches(?,RD.RDT_RULE_ID)" 
																   +" as RDT_BRANCH,"
																   +" RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_PARTY_ID,RDT_COMMUNICATION_ID,RDT_ID"
																   +" FROM DDP_RULE R" 
																   +" LEFT JOIN DDP_RULE_DETAIL RD ON R.RUL_ID=RD.RDT_RULE_ID" 
																   +" WHERE RDT_COMPANY= ?"  
																   +" )dis group by RDT_PARTY_CODE,RDT_DOC_TYPE,RDT_PARTY_ID"
																   +" )A"
																   +" LEFT JOIN (select CEM_EMAIL_ID,CEM_EMAIL_CC,CEM_EMAIL_SUBJECT,CEM_EMAIL_TO,CEM_EMAIL_FROM,CEM_EMAIL_BODY,e.CMS_COMMUNICATION_ID" 
																   +" from DDP_COMM_EMAIL r"  
																   +" left join DDP_COMMUNICATION_SETUP e on e.CMS_PROTOCOL_SETTINGS_ID = r.CEM_EMAIL_ID)" 
																   +" CE ON A.RDT_COMMUNICATION_ID=CE.CMS_COMMUNICATION_ID"   
																   +" )B" 
																   +" LEFT JOIN (SELECT COP_OPTION,COP_RDT_ID FROM DDP_COMM_OPTIONS_SETUP where COP_OPTION = 'Emailing')" 
																   +" CO ON B.RDT_ID=CO.COP_RDT_ID"  
																   +" ) EM"
																   +" left join ("
																   +" SELECT DISTINCT *"  
																   +" FROM("
																   +" SELECT DISTINCT RUL_ID ,RDT_COMPANY,RDT_BRANCH,RDT_PARTY_ID,RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_COMMUNICATION_ID," 
																   +" CEM_EMAIL_FROM, CEM_EMAIL_TO,CEM_EMAIL_CC,CEM_EMAIL_BODY,RDT_ID"
																   +" FROM("
																   +" select MAX(RUL_ID) as RUL_ID ,max(RDT_COMPANY) as RDT_COMPANY,max(RDT_BRANCH) as RDT_BRANCH," 
																   +" RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_PARTY_ID,"
																   +" max(RDT_COMMUNICATION_ID) as RDT_COMMUNICATION_ID , max(RDT_ID) as RDT_ID from ("
																   +" SELECT DISTINCT   RUL_ID, RDT_COMPANY,"
																   +" dbo.Fn_Branches(?,RD1.RDT_RULE_ID)"
																   +" as RDT_BRANCH," 
																   +" RDT_DOC_TYPE,RDT_PARTY_CODE,RDT_PARTY_ID, RDT_COMMUNICATION_ID, RDT_ID"
																   +" FROM DDP_RULE R1"
																   +" LEFT JOIN DDP_RULE_DETAIL RD1 ON R1.RUL_ID=RD1.RDT_RULE_ID"
																   +" WHERE RDT_COMPANY= ?"  
																   +" ) dis group by RDT_PARTY_CODE,RDT_DOC_TYPE,RDT_PARTY_ID"
																   +" )A1"  
																   +" LEFT JOIN (select CEM_EMAIL_ID,CEM_EMAIL_CC,CEM_EMAIL_SUBJECT,CEM_EMAIL_TO ,CEM_EMAIL_FROM,CEM_EMAIL_BODY,e.CMS_COMMUNICATION_ID"  
																   +" from DDP_COMM_EMAIL r" 
																   +" left join DDP_COMMUNICATION_SETUP e on e.CMS_PROTOCOL_SETTINGS_ID = r.CEM_EMAIL_ID)" 
																   +" CE1 ON A1.RDT_COMMUNICATION_ID=CE1.CMS_COMMUNICATION_ID" 
																   +" )B1" 
																   +" LEFT JOIN (SELECT COP_OPTION,COP_RDT_ID FROM DDP_COMM_OPTIONS_SETUP where cop_option = 'Printing') CO1"  
																   +" ON B1.RDT_ID=CO1.COP_RDT_ID"  
																   +" ) PR" 
																   +" ON PR.RDT_ID = EM.RDT_ID";
	
	public static String BRANCHESBYRULEID1						=	"SELECT DISTINCT RDT_BRANCH FROM DDP_RULE_DETAIL WHERE  RDT_RULE_ID= ?";
	
								
	public static String BRANCHESBYRULEID						=	"SELECT DISTINCT RDT_BRANCH FROM DDP_RULE_DETAIL WHERE RDT_COMPANY= ? AND RDT_RULE_ID= ?";
	
	//Get user company by user id
	public static String USERCOMPANY							= "select USR_COMPANY_CODE from DDP_USER where USR_LOGIN_ID = ? and USR_STATUS = 0";
	
	//Get user Group by user id
	public static String USERGROUP								= "select GRP.GRP_NAME from DDP_GROUP GRP,DDP_USER USR,DDP_GROUP_SETUP GRS where GRP.GRP_ID=GRS.GRS_GROUP_ID and USR.USR_ID=GRS.GRS_USER_ID and USR.USR_LOGIN_ID= ?";
		
	//Get user company by user id
	public static String USERREGION								= "select USR_REGION from DDP_USER where USR_LOGIN_ID = ? and USR_STATUS = 0";
		
	/*********************  Querying for DDP USER BASED ON LOGIN ID ***********************/
	public static final String GETUSERBYLOGINID					= "SELECT USR_ID FROM DDP_USER WHERE USR_LOGIN_ID = ? ";
		
	public static String USERCOMPANYBRANCHES					= "select BRN_BRANCH_CODE from DDP_BRANCH where BRN_COMPANY_CODE = ? and BRN_STATUS = 0 ";
	
	
	public static Calendar CURRENTDATE							= Calendar.getInstance();
																  
	//Get Login User Name
	public static  String USERLOGINID							= SecurityContextHolder.getContext().getAuthentication().getName(); 
	
	//Status Active in Inactive Flag ---> 0 - Active / 1 - Inactive
	public static final int ACTIVE								= 0; 
	public static final int INACTIVE							= 1;
	
	//Check the domain value
	public static final String INLINE							= "InLine";
	public static final String LOGISTICS						= "Logistics";
	
	//Creating XML file name suffix, file name should be "country code + emailing"
	public static final String EmailFileName					=	"emailing.xml";

	
	public static final String SELECT_RDT_ID					=	"select * from dbo.DDP_RULE_DETAIL where rdt_rule_id= ?";
	
	public static final String SELECTRULEDETAILBYAEDID			= 	"select * from ddp_rule_detail where rdt_rule_id = ?"	;
	
	public static final String COMPANIESBYREGION				= 	"select * from ddp_COMPANY WHERE COM_REGION= ? AND COM_STATUS=0"	;
	
	public static final String SELECTOPTIONSETUPBYRULEDETILID	= 	"select * from DDP_COMM_OPTIONS_SETUP where Cop_rdt_id = ?"	;
	
	public static final String SELECT_RULE_ID					=	"select rul_id from ddp_rule where rul_name LIKE ? ";
	
	/*********************  Querying DDP_CATEGORIZED_DOCS for Records in last 90days ***********************/
	public static final String CATEGORIZEDDOCSDESC				=	"SELECT TOP 10000 dcd.CAT_ID,dcd.CAT_SYN_ID,dcd.CAT_RULE_TYPE,dcd.CAT_RDT_ID,(SELECT RDT_RELAVANT_TYPE"
																+ " FROM DDP_RULE_DETAIL WHERE RDT_ID=dcd.CAT_RDT_ID) AS RELEVANT_TYPE,"
																+ "ddd.DDD_COMPANY_SOURCE,ddd.DDD_CONTROL_DOC_TYPE,ddd.DDD_CONSIGNMENT_ID,"
																+ "ddd.DDD_JOB_NUMBER,ddd.DDD_DOC_REF,dcd.CAT_RUL_ID,dcd.CAT_CREATED_DATE,"
																+ "dcd.CAT_STATUS,(select detail.RDT_PARTY_CODE from DDP_RULE_DETAIL detail"
																+ " where detail.RDT_ID = dcd.CAT_RDT_ID) AS RDT_PARTY_CODE,(select detail.RDT_PARTY_ID from"
																+ " DDP_RULE_DETAIL detail where detail.RDT_ID = dcd.CAT_RDT_ID) AS RDT_PARTY_ID,"
																+ "ddd.DDD_GENERATING_SYSTEM,ddd.DDD_IS_RATED FROM DDP_CATEGORIZED_DOCS dcd,"
																+ "DDP_DMS_DOCS_DETAIL ddd WHERE dcd.CAT_DTX_ID=ddd.DDD_DTX_ID AND"
																+ " CONVERT(VARCHAR(10),dcd.CAT_CREATED_DATE,120) >= CONVERT(VARCHAR(10),getdate()-90,120)"
																+ "	ORDER BY dcd.CAT_ID DESC";
	
	/*********************  Querying DDP_DMS_DOCS_DETAIL for Records in last 90days ***********************/
	public static final String DMSDOCSDETAILSDESC				=	"SELECT TOP 20000 DDD.* FROM DDP_DMS_DOCS_DETAIL DDD,DDP_CATEGORIZED_DOCS CAT "
																	+"WHERE CAT.CAT_DTX_ID=DDD.DDD_DTX_ID AND " 
																	+"CONVERT(VARCHAR(10),CAT.CAT_CREATED_DATE,120) >= CONVERT(VARCHAR(10),getdate()-90,120) ORDER BY DDD_ID DESC";
	
	/*********************  Querying for last 60days Audit Results ***********************/
	public static final String AUDITTXNDESC						=	"SELECT TOP 20000 * FROM DDP_AUDIT_TXN where CONVERT(VARCHAR(10), ATX_CREATED_DATE, 120) >= CONVERT(VARCHAR(10), getdate() - 60, 120) ORDER BY ATX_ID DESC";
	
	public static final String DDPDMSDOCSDETAILBYDTXID			=	"SELECT DISTINCT DDD_ID FROM DDP_DMS_DOCS_DETAIL WHERE DDD_DTX_ID=?";
	
	/*********************  Querying for list of company codes whose status is Active  ***********************/
	public static final String SELECT_COMPANY_BY_STATUS				=	"SELECT * FROM DDP_COMPANY WHERE COM_STATUS=0";
	
	/*********************  Querying for list of DocTypes whose status is Active ***********************/
	public static final String SELECT_DOCTYPE_BY_STATUS				=	"SELECT * FROM DDP_DOCTYPE WHERE DTY_COMPANY_CODE=? and DTY_STATUS=0";
	
	/*********************  Querying for list of Party whose status is Active ***********************/
	public static final String SELECT_PARTY_BY_STATUS				=	"SELECT PTY_PARTY_CODE FROM DDP_PARTY WHERE PTY_STATUS=0";
	
	/******************** Querying for List of AED rule and Rule Details for Admin User ****************************************/
	public static final String SELECT_ALL_ACTIVE_RULE_DETAILS		= "SELECT * FROM ( SELECT	drd.RDT_ID, drd.RDT_RULE_ID,dr.RUL_DESCRIPTION, drd.RDT_PARTY_ID,"
																	+ " cem.CEM_EMAIL_TO,cem.CEM_EMAIL_CC,drd.RDT_COMPANY, drd.RDT_BRANCH, ROW_NUMBER() OVER(PARTITION BY dr.RUL_ID ORDER BY dr.RUL_ID DESC) rn"
																	+ " FROM DDP_RULE_DETAIL drd,DDP_RULE dr,DDP_COMMUNICATION_SETUP cms,DDP_COMM_EMAIL cem "
																	+ "WHERE dr.RUL_ID=drd.RDT_RULE_ID AND drd.RDT_COMMUNICATION_ID=cms.CMS_COMMUNICATION_ID "
																	+ "AND cms.CMS_PROTOCOL_SETTINGS_ID= cem.CEM_EMAIL_ID AND drd.RDT_RULE_TYPE= ? union "
																	+ "SELECT	drd.RDT_ID, drd.RDT_RULE_ID,dr.RUL_DESCRIPTION, drd.RDT_PARTY_ID, mes.MES_EMAIL_TO,mes.MES_EMAIL_CC,"
																	+ "drd.RDT_COMPANY, drd.RDT_BRANCH, ROW_NUMBER() OVER(PARTITION BY dr.RUL_ID ORDER BY dr.RUL_ID DESC) rn "
																	+ "FROM DDP_RULE_DETAIL drd,DDP_RULE dr,DDP_COMMUNICATION_SETUP cms,DDP_MULTI_EMAILS mes "
																	+ "WHERE dr.RUL_ID=drd.RDT_RULE_ID AND drd.RDT_COMMUNICATION_ID=cms.CMS_COMMUNICATION_ID "
																	+ "AND cms.CMS_COMMUNICATION_ID= mes.MES_CMS_ID AND drd.RDT_RULE_TYPE= ?) a WHERE rn = 1";
	
	/****************** Querying for List of AED Rules and Rule Details for Region User*********************************/
	public static final String SELECT_ACTIVE_RULES_REGEION			= "SELECT *  FROM ( SELECT	drd.RDT_ID, drd.RDT_RULE_ID,dr.RUL_DESCRIPTION, drd.RDT_PARTY_ID,cem.CEM_EMAIL_TO,"
																	+ "cem.CEM_EMAIL_CC,drd.RDT_COMPANY, drd.RDT_BRANCH, ROW_NUMBER() OVER(PARTITION BY dr.RUL_ID ORDER BY dr.RUL_ID DESC) rn "
																	+ "FROM DDP_RULE_DETAIL drd,DDP_RULE dr,DDP_COMPANY dc,DDP_COMMUNICATION_SETUP cms,DDP_COMM_EMAIL cem "
																	+ "WHERE dr.RUL_ID=drd.RDT_RULE_ID AND dc.COM_COMPANY_CODE=drd.RDT_COMPANY AND dc.COM_STATUS=0 "
																	+ "AND drd.RDT_COMMUNICATION_ID=cms.CMS_COMMUNICATION_ID AND cms.CMS_PROTOCOL_SETTINGS_ID= cem.CEM_EMAIL_ID  "
																	+ "AND drd.RDT_RULE_TYPE= ? AND dc.COM_REGION= ? UNION "
																	+ "SELECT	drd.RDT_ID, drd.RDT_RULE_ID,dr.RUL_DESCRIPTION, drd.RDT_PARTY_ID,mes.MES_EMAIL_TO,mes.MES_EMAIL_CC,"
																	+ "drd.RDT_COMPANY, drd.RDT_BRANCH, ROW_NUMBER() OVER(PARTITION BY dr.RUL_ID ORDER BY dr.RUL_ID DESC) rn "
																	+ "FROM DDP_RULE_DETAIL drd,DDP_RULE dr,DDP_COMPANY dc,DDP_COMMUNICATION_SETUP cms,DDP_MULTI_EMAILS mes "
																	+ "WHERE dr.RUL_ID=drd.RDT_RULE_ID AND dc.COM_COMPANY_CODE=drd.RDT_COMPANY AND dc.COM_STATUS=0 "
																	+ "AND drd.RDT_COMMUNICATION_ID=cms.CMS_COMMUNICATION_ID AND cms.CMS_COMMUNICATION_ID= mes.MES_CMS_ID  "
																	+ "AND drd.RDT_RULE_TYPE= ? AND dc.COM_REGION= ?) a WHERE rn = 1";
	
	/***************** Querying for List of AED Rules and Rule Details for Local User *****************************/
	public static final String SELECT_ACTIVE_RULE_LOCAL				= "SELECT *  FROM ( SELECT	drd.RDT_ID, drd.RDT_RULE_ID,dr.RUL_DESCRIPTION, drd.RDT_PARTY_ID, "
																	+ "cem.CEM_EMAIL_TO,cem.CEM_EMAIL_CC,drd.RDT_COMPANY, drd.RDT_BRANCH, ROW_NUMBER() OVER(PARTITION BY dr.RUL_ID ORDER BY dr.RUL_ID DESC) rn "
																	+ "FROM DDP_RULE_DETAIL drd,DDP_RULE dr,DDP_COMPANY dc,DDP_COMMUNICATION_SETUP cms,DDP_COMM_EMAIL cem "
																	+ "WHERE dr.RUL_ID=drd.RDT_RULE_ID AND dc.COM_COMPANY_CODE=drd.RDT_COMPANY "
																	+ "AND dc.COM_STATUS=0 AND drd.RDT_COMMUNICATION_ID=cms.CMS_COMMUNICATION_ID AND cms.CMS_PROTOCOL_SETTINGS_ID= cem.CEM_EMAIL_ID "
																	+ "AND drd.RDT_RULE_TYPE= ? AND dc.COM_COMPANY_CODE= ? UNION "
																	+ "SELECT drd.RDT_ID, drd.RDT_RULE_ID,dr.RUL_DESCRIPTION, drd.RDT_PARTY_ID, mes.MES_EMAIL_TO,mes.MES_EMAIL_CC,drd.RDT_COMPANY, "
																	+ "drd.RDT_BRANCH, ROW_NUMBER() OVER(PARTITION BY dr.RUL_ID ORDER BY dr.RUL_ID DESC) rn "
																	+ "FROM DDP_RULE_DETAIL drd,DDP_RULE dr,DDP_COMPANY dc,DDP_COMMUNICATION_SETUP cms,DDP_MULTI_EMAILS mes "
																	+ "WHERE dr.RUL_ID=drd.RDT_RULE_ID AND dc.COM_COMPANY_CODE=drd.RDT_COMPANY AND dc.COM_STATUS=0 "
																	+ "AND drd.RDT_COMMUNICATION_ID=cms.CMS_COMMUNICATION_ID AND cms.CMS_COMMUNICATION_ID= mes.MES_CMS_ID "
																	+ "AND drd.RDT_RULE_TYPE= ? AND dc.COM_COMPANY_CODE= ?) a WHERE rn = 1";
	
	/***************** Querying for List of AED Rules and Rule Details for User who has multi company Access********************************/
	public static final String SELECT_ACTIVE_RULE_MULTI_COMPANY 	= "SELECT *  FROM ( SELECT	drd.RDT_ID, drd.RDT_RULE_ID,dr.RUL_DESCRIPTION, drd.RDT_PARTY_ID, "
																	+ "cem.CEM_EMAIL_TO,cem.CEM_EMAIL_CC,drd.RDT_COMPANY, drd.RDT_BRANCH, "
																	+ "ROW_NUMBER() OVER(PARTITION BY dr.RUL_ID ORDER BY dr.RUL_ID DESC) rn "
																	+ "FROM DDP_RULE_DETAIL drd,DDP_RULE dr,DDP_COMPANY dc,DDP_COMMUNICATION_SETUP cms,"
																	+ "DDP_COMM_EMAIL cem WHERE dr.RUL_STATUS=0 AND dr.RUL_ID=drd.RDT_RULE_ID "
																	+ "AND dc.COM_COMPANY_CODE=drd.RDT_COMPANY AND dc.COM_STATUS=0 AND drd.RDT_COMMUNICATION_ID=cms.CMS_COMMUNICATION_ID "
																	+ "AND cms.CMS_PROTOCOL_SETTINGS_ID= cem.CEM_EMAIL_ID AND drd.RDT_RULE_TYPE= ? "
																	+ "AND ( dynamiccondition) UNION "
																	+ "SELECT	drd.RDT_ID, drd.RDT_RULE_ID,dr.RUL_DESCRIPTION, drd.RDT_PARTY_ID, mes.MES_EMAIL_TO,"
																	+ "mes.MES_EMAIL_CC,drd.RDT_COMPANY, drd.RDT_BRANCH, ROW_NUMBER() OVER(PARTITION BY dr.RUL_ID ORDER BY dr.RUL_ID DESC) rn "
																	+ "FROM DDP_RULE_DETAIL drd,DDP_RULE dr,DDP_COMPANY dc,DDP_COMMUNICATION_SETUP cms,DDP_MULTI_EMAILS mes "
																	+ "WHERE dr.RUL_STATUS=0 AND dr.RUL_ID=drd.RDT_RULE_ID AND dc.COM_COMPANY_CODE=drd.RDT_COMPANY "
																	+ "AND dc.COM_STATUS=0 AND drd.RDT_COMMUNICATION_ID=cms.CMS_COMMUNICATION_ID "
																	+ "AND cms.CMS_COMMUNICATION_ID= mes.MES_CMS_ID AND drd.RDT_RULE_TYPE= ? "
																	+ "AND ( dynamiccondition)) a WHERE rn = 1";
	
	/***************** Querying for List of EXPORT rule and Rule Details for Admin User ****************************************************/
	public static final String SELECT_ALL_ACTIVE_EXPORT_RULES		= "SELECT *  FROM	( SELECT	drd.RDT_ID, drd.RDT_RULE_ID,dr.RUL_DESCRIPTION, drd.RDT_PARTY_ID, "
																	+ "(select cpny.COM_COUNTRY_CODE from DDP_COMPANY cpny where cpny.COM_COMPANY_CODE = drd.RDT_COMPANY) COM_COUNTRY_CODE, "
																	+ "drd.RDT_COMPANY, drd.RDT_BRANCH,drd.RDT_STATUS, ROW_NUMBER() OVER(PARTITION BY dr.RUL_ID ORDER BY dr.RUL_ID DESC) rn "
																	+ "FROM DDP_RULE_DETAIL drd,DDP_RULE dr,DDP_COMPANY dc WHERE dr.RUL_ID=drd.RDT_RULE_ID AND dr.RUL_STATUS=0 "
																	+ "AND dc.COM_STATUS=0 AND drd.RDT_RULE_TYPE= ? 	) 	a WHERE rn = 1";
	
	/***************** Querying for List of EXPORT rule and Rule Details for Region User ****************************************************/
	public static final String SELECT_ACTIVE_EXPORT_RULES_REGEION	= "SELECT *  FROM ( SELECT	drd.RDT_ID, drd.RDT_RULE_ID,dr.RUL_DESCRIPTION, drd.RDT_PARTY_ID, "
																	+ "dc.COM_COUNTRY_CODE,drd.RDT_COMPANY, drd.RDT_BRANCH,drd.RDT_STATUS, ROW_NUMBER() OVER(PARTITION BY dr.RUL_ID ORDER BY dr.RUL_ID DESC) rn "
																	+ "FROM DDP_RULE_DETAIL drd,DDP_RULE dr,DDP_COMPANY dc WHERE dr.RUL_STATUS=0 AND dr.RUL_ID=drd.RDT_RULE_ID AND "
																	+ "dc.COM_COMPANY_CODE=drd.RDT_COMPANY AND dc.COM_STATUS=0 AND drd.RDT_RULE_TYPE= ? AND dc.COM_REGION= ? ) a WHERE rn = 1";
	
	/***************** Querying for List of EXPORT Rules and Rule Details for Local User *****************************/
	public static final String SELECT_ACTIVE_EXPORT_RULE_LOCAL		= "SELECT *  FROM ( SELECT	drd.RDT_ID, drd.RDT_RULE_ID,dr.RUL_DESCRIPTION, drd.RDT_PARTY_ID, "
																	+ "dc.COM_COUNTRY_CODE,drd.RDT_COMPANY, drd.RDT_BRANCH,drd.RDT_STATUS, ROW_NUMBER() OVER(PARTITION BY dr.RUL_ID ORDER BY dr.RUL_ID DESC) rn "
																	+ "FROM DDP_RULE_DETAIL drd,DDP_RULE dr,DDP_COMPANY dc WHERE dr.RUL_STATUS=0 AND dr.RUL_ID=drd.RDT_RULE_ID "
																	+ "AND dc.COM_COMPANY_CODE=drd.RDT_COMPANY AND dc.COM_STATUS=0 AND drd.RDT_RULE_TYPE= ? AND dc.COM_COMPANY_CODE= ? ) a WHERE rn = 1";
	
	
	/***************** Querying for List of EXPORT Rules and Rule Details for User who has multi company Access********************************/
	public static final String SELECT_ACTIVE_EXPORT_RULE_MULTI_COMPANY 	= "SELECT *  FROM ( SELECT	drd.RDT_ID, drd.RDT_RULE_ID,dr.RUL_DESCRIPTION, drd.RDT_PARTY_ID, dc.COM_COUNTRY_CODE,"
																	+ " drd.RDT_COMPANY, drd.RDT_BRANCH,drd.RDT_STATUS, ROW_NUMBER() OVER(PARTITION BY dr.RUL_ID ORDER BY dr.RUL_ID DESC) rn FROM"
																	+ " DDP_RULE_DETAIL drd,DDP_RULE dr,DDP_COMPANY dc WHERE dr.RUL_ID=drd.RDT_RULE_ID AND dc.COM_COMPANY_CODE=drd.RDT_COMPANY AND"
																	+ " dc.COM_STATUS=0 AND drd.RDT_RULE_TYPE= ? AND ( dynamiccondition)) a WHERE rn = 1";
	
	/***************** Querying for List of EXPORT Rules for Rule by Query *****************************/
	public static final String SELECT_ACTIVE_EXPORT_RULE_BY_QUERY		= "SELECT *  FROM ( SELECT	drd.RDT_ID, drd.RDT_RULE_ID,dr.RUL_DESCRIPTION, drd.RDT_PARTY_ID, "
																	+ "(select cpny.COM_COUNTRY_CODE from DDP_COMPANY cpny where cpny.COM_COMPANY_CODE = drd.RDT_COMPANY) COM_COUNTRY_CODE,"
																	+ "drd.RDT_COMPANY, drd.RDT_BRANCH,drd.RDT_STATUS, ROW_NUMBER() OVER(PARTITION BY dr.RUL_ID ORDER BY dr.RUL_ID DESC) rn "
																	+ "FROM DDP_RULE_DETAIL drd,DDP_RULE dr,DDP_COMPANY dc WHERE dr.RUL_STATUS=0 AND dr.RUL_ID=drd.RDT_RULE_ID "
																	+ "AND drd.RDT_COMPANY IS NULL AND dc.COM_STATUS=0 AND drd.RDT_RULE_TYPE= ?) a WHERE rn = 1";
	
	/***************** Querying for List of EXPORT Rules which are reading from Properties file ********************************/
	public static final String SELECT_ACTIVE_EXPORT_RULE_FROM_PROP 	="SELECT '' AS RDT_ID,rul.RUL_ID AS RDT_RULE_ID,rul.RUL_DESCRIPTION,'' AS RDT_PARTY_ID,"
																	+ "'' AS COM_COUNTRY_CODE,'' AS RDT_COMPANY,'' AS RDT_BRANCH,exprul.EXP_STATUS AS RDT_STATUS FROM DDP_RULE rul,"
																	+ "DDP_EXPORT_RULE exprul,DDP_SCHEDULER sch WHERE rul.RUL_ID=exprul.EXP_RULE_ID AND rul.RUL_STATUS=0 AND "
																	+ "sch.SCH_CHOOSEN_TYPE='1' AND sch.SCH_ID=exprul.EXP_SCHEDULER_ID";
	
	/*****************    Querying for compressionSetupId By Multi Aed Rule Id      ********************************************/
	public static final String SELECT_COMPRESSIONID_BY_MAED_ID		= "SELECT cts.CTS_COMPRESSION_ID FROM DDP_MULTI_AED_RULE maed,DDP_COMPRESSION_SETUP cts where cts.CTS_COMPRESSION_ID = maed.MAED_COMPRESSION_ID AND maed.MAED_RULE_ID= ?";
	
	/*****************    Querying for Gen SourceLkp for Description by its Option       *********************************************/
	public static final String SELECT_GEN_DESC_BY_OPTION			= "SELECT GSL_DESCRIPTION FROM DDP_GEN_SOURCE_LKP WHERE GSL_OPTION=?";
	
	/*****************    Querying for Rate SourceLkp for Description by its Option       *********************************************/
	public static final String SELECT_RATE_DESC_BY_OPTION			= "SELECT RTL_DESCRIPTION FROM DDP_RATE_LKP WHERE RTL_OPTION=?";
	
	/****************   Querying for list of Exp_ids whose has same QueryID(ClientID) for Rule By Query *****************/
	public static final String SELECT_EXP_ID_OF_SAME_CLIENT_ID		="SELECT EXP.EXP_RULE_ID FROM DDP_EXPORT_RULE EXP,DDP_SCHEDULER SCH WHERE EXP.EXP_SCHEDULER_ID=SCH.SCH_ID AND EXP_STATUS=0 AND SCH_RULE_CATEGORY=?";
	
	/******************* Querying for all categorized_docs which are not processed for perticular rule *****************************/
	public static final String SELECT_NOT_PROCESSED_CATEGORY_DOCS	="SELECT cat.CAT_ID FROM DDP_CATEGORIZED_DOCS cat,DDP_DMS_DOCS_DETAIL ddd "
																	+ "WHERE ddd.DDD_DTX_ID=cat.CAT_DTX_ID AND "
																	+ "cat.CAT_RUL_ID=? AND cat.CAT_RULE_TYPE=?";
	/**************** Query for Not Processed Rdt's in Update MetaData job *****************************/
	public static final String SELECT_NOT_PROCESSED_RDT_IDS			= "SELECT CHL_ID FROM DDP_CATEGORIZATION_HOLDER WHERE CHL_RUL_ID IN (dynamiccondition)"
																		+" AND CHL_DTX_ID NOT IN (SELECT DDD_DTX_ID FROM DDP_DMS_DOCS_DETAIL)";
	
	/*************** Query for Not Processed cat_IDs in categorization job *************************/
	public static final String SELECT_NOT_PROCESSED_CAT_IDS			= "SELECT CAT_ID FROM DDP_CATEGORIZED_DOCS WHERE CAT_STATUS=0 AND CAT_RUL_ID=?";	
	
	public static final String SELECT_CATEGORIZEDDOCS_BY_PARAM		="SELECT dcd.CAT_ID,dcd.CAT_SYN_ID,dcd.CAT_RULE_TYPE,dcd.CAT_RDT_ID,"
																	+ "(SELECT RDT_RELAVANT_TYPE FROM DDP_RULE_DETAIL WHERE RDT_ID=dcd.CAT_RDT_ID) AS RELEVANT_TYPE,"
																	+ "ddd.DDD_COMPANY_SOURCE,ddd.DDD_CONTROL_DOC_TYPE,ddd.DDD_CONSIGNMENT_ID,ddd.DDD_JOB_NUMBER,"
																	+ "ddd.DDD_DOC_REF,dcd.CAT_RUL_ID,dcd.CAT_CREATED_DATE,dcd.CAT_STATUS,"
																	+ "(select detail.RDT_PARTY_CODE from DDP_RULE_DETAIL detail where detail.RDT_ID = dcd.CAT_RDT_ID) AS RDT_PARTY_CODE,"
																	+ "(select detail.RDT_PARTY_ID from DDP_RULE_DETAIL detail where detail.RDT_ID = dcd.CAT_RDT_ID) AS RDT_PARTY_ID,"
																	+ "ddd.DDD_GENERATING_SYSTEM,ddd.DDD_IS_RATED FROM DDP_CATEGORIZED_DOCS dcd,DDP_DMS_DOCS_DETAIL ddd "
																	+ "WHERE dcd.CAT_DTX_ID=ddd.DDD_DTX_ID and dcd.CAT_ID=? ORDER BY dcd.CAT_ID DESC";
	
	
}
