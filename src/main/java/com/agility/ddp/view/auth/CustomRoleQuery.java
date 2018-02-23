package com.agility.ddp.view.auth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.core.SqlParameter;


public class CustomRoleQuery extends MappingSqlQuery<String> {

	public static final String DEF_SQL_SELECT_ROLES =
			"SELECT u.usr_login_id USERNAME, g.grp_name, r.rol_name ROLENAME FROM ddp_user u, ddp_group_setup gs, ddp_group g, ddp_role_setup rs, ddp_role r WHERE gs.grs_user_id = u.usr_id and rs.rls_child_usr_id = u.usr_id and g.grp_id = gs.grs_group_id and r.rol_id = rs.rls_role_id and u.usr_login_id=? UNION SELECT u.usr_login_id USERNAME, g.grp_name, r.rol_name ROLENAME FROM ddp_user u, ddp_group_setup gs, ddp_group g, ddp_role_setup rs, ddp_role r WHERE gs.grs_user_id = u.usr_id and rs.rls_parent_grp_id = gs.grs_group_id and g.grp_id = gs.grs_group_id and r.rol_id = rs.rls_role_id and u.usr_login_id=?";
	
	
	
	public CustomRoleQuery(DataSource dataSource) 
	{  
		super(dataSource, DEF_SQL_SELECT_ROLES);
		super.declareParameter(new SqlParameter("u.usr_login_id", Types.VARCHAR));  
		compile();
	}
	
	
	public CustomRoleQuery(DataSource dataSource, String sqlQuery) 
	{  
		super(dataSource, sqlQuery); 
		//This part has to be extended in future to read the parameters from applicationContext-security.xml
		super.declareParameter(new SqlParameter("u.usr_login_id", Types.VARCHAR));
		compile();  
	}
	
	@Override
	protected String mapRow(ResultSet rs, int rowNum) throws SQLException 
	{	
		return rs.getString("ROLENAME");
	}
	
}
