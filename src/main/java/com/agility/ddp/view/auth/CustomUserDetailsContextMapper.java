package com.agility.ddp.view.auth;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.core.userdetails.User;

import com.agility.ddp.core.logger.AuditLog;

public class CustomUserDetailsContextMapper implements UserDetailsContextMapper 
{
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsContextMapper.class);

	private CustomRoleQuery customRoleQuery;
	
	private DataSource dataSource;
	private String roleQuery;
	
	private List<SimpleGrantedAuthority> loadRolesFromDatabase(String username) 
	{
		customRoleQuery = new CustomRoleQuery(dataSource,roleQuery);
		List<String> roles = customRoleQuery.execute(username);
		
		List<SimpleGrantedAuthority> authorities = new ArrayList< SimpleGrantedAuthority >();
		
		for( String role:roles ) 
		{
			logger.debug("User [{}] - assigned role : [{}]",username,role);
			authorities.add(new SimpleGrantedAuthority(role));
		}
		     
		return authorities;
	}
	
	@AuditLog(message="User found in Active Directory.")
	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) 
	{
		logger.info("User [{}] found in Active Directory (LOGISTICS domain). Querying Roles for user [{}]",username,username);
		List<SimpleGrantedAuthority> allAuthorities = new ArrayList<SimpleGrantedAuthority>();
		for (GrantedAuthority auth : authorities) 
		{
	        if (auth != null && !auth.getAuthority().isEmpty()) 
	        {
	           allAuthorities.add((SimpleGrantedAuthority) auth);
	        }
		}
	   
		// add additional roles from the database table
		allAuthorities.addAll(loadRolesFromDatabase(username));
		return new User(username, "", true, true, true, true, allAuthorities);
	}

	@Override
	public void mapUserToContext(UserDetails arg0, DirContextAdapter arg1) {
		// TODO Auto-generated method stub

	}
	
	public String getRoleQuery() 
	{
		return roleQuery;
	}
	
	public void setRoleQuery(String strRoleQuery) 
	{
		this.roleQuery = strRoleQuery;
	}
	
	public DataSource getDataSource() 
	{
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource) 
	{
		this.dataSource = dataSource;
	}

}