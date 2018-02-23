package com.agility.ddp.view.auth;

import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import com.agility.ddp.core.logger.AuditLog;

@Configuration
//@PropertySource({"file:///E:/DDPConfig/ddp.properties"})
public class QueryActiveDirectory implements InitializingBean
{
//	@Autowired
//	Environment env;
	
	private static final Logger logger = LoggerFactory.getLogger(QueryActiveDirectory.class);
	
	private static LdapContext ldapContext;
	private Hashtable<String, String> ldapEnv = new Hashtable<String, String>(11);
	
	private String initialContextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
	private String provideUrl = "ldap://logistics.intra:389";
	//private String provideUrl = "ldap://10.201.65.51:389";
	private String securityAuthentication = "simple";
	//private String securityPrincipal = "CN=DMAdmin,OU=Service Accounts,OU=DMS,OU=IT Infrastructure Servers,OU=DataCenter Singapore,DC=logistics,DC=intra";
	private String securityPrincipal = "CN=SVC-DMSAdmin,OU=Service Accounts,OU=DMS,OU=IT Infrastructure Servers,OU=DataCenter Singapore,DC=logistics,DC=intra";
	private String securityCredentials = "agildcmnsy";
	private String securityProtocol = "simple";
	
	private String[] returnAtts = {"name", "sn","givenName", "displayName", "title", "samAccountName", "cn", "company", "department", "l", "streetAddress", "co", "employeeID", "givenName", "ipPhone", "mail", "mobile"};
	private String searchBase = "DC=logistics,DC=intra";
	
	@Override
	public void afterPropertiesSet() throws Exception 
	{
		ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, getInitialContextFactory());
		ldapEnv.put(Context.PROVIDER_URL,  getProvideUrl());
		ldapEnv.put(Context.SECURITY_AUTHENTICATION, getSecurityAuthentication());
		ldapEnv.put(Context.SECURITY_PRINCIPAL, getSecurityPrincipal());
		ldapEnv.put(Context.SECURITY_CREDENTIALS, getSecurityCredentials());
		ldapEnv.put(Context.SECURITY_PROTOCOL, getSecurityProtocol());
		ldapEnv.put("com.sun.jndi.ldap.connect.pool", "true");
		logger.debug("afterPropertiesSet() constructor invoked to initiate InitialLdapContext().");
	}
	
	
	private LdapContext getLdapContext() throws Exception 
	{
		logger.debug("LdapContext() invoked to initiate InitialLdapContext().");
		ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, getInitialContextFactory());
		ldapEnv.put(Context.PROVIDER_URL,  getProvideUrl());
		ldapEnv.put(Context.SECURITY_AUTHENTICATION, getSecurityAuthentication());
		ldapEnv.put(Context.SECURITY_PRINCIPAL, getSecurityPrincipal());
		ldapEnv.put(Context.SECURITY_CREDENTIALS, getSecurityCredentials());
		ldapEnv.put(Context.SECURITY_PROTOCOL, getSecurityProtocol());
		ldapEnv.put("com.sun.jndi.ldap.connect.pool", "true");

		return new InitialLdapContext(ldapEnv, null);
	}
	
	@AuditLog(message="Querying user details from active directory.")
	public Map adDetails(String strUserName) throws Exception
	{
		logger.debug("Method adDetails({}) invoked.",strUserName);
		try
		{
			ldapContext = getLdapContext();
			
			Map attrMap = null;
			SearchControls searchCtls = new SearchControls();
			
			searchCtls.setReturningAttributes(getReturnAtts());
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			
			String searchFilter = "(&(objectClass=*)(sAMAccountName="+strUserName+"))";
			String searchBase = getSearchBase();
			int totalResults = 0;
			
			NamingEnumeration<SearchResult> result = ldapContext.search(searchBase, searchFilter, searchCtls);
			
			//this will overwrite the last matching record to attrMap
			while (result.hasMoreElements())
			{
				SearchResult sr = (SearchResult)result.next();			
	
				totalResults++;
				logger.debug("AD Name : {}",sr.getName());
				Attributes attrs = sr.getAttributes();
				
				if (attrs != null)
				{
					attrMap = new HashMap();
					NamingEnumeration ne = attrs.getAll();
					while (ne.hasMore())
					{
						Attribute attr = (Attribute) ne.next();
						attrMap.put(attr.getID(), attr.get());
						logger.debug("AD Attributes: {} : {}",attr.getID(),attr.get());
					}
					ne.close();
				}
				
			}
			
			return attrMap;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			logger.error(ex.getMessage()+" : Error while Querying ActiveDirectory to get user details logistics/{}.",strUserName);
			throw new Exception(ex.getMessage()+" : Error while Querying ActiveDirectory to get user details logistics/"+strUserName);
		}
		
	}
	
	public String getInitialContextFactory()
	{
		return initialContextFactory;
	}
	
	public void setInitialContextFactory(String strInitialContextFactory) 
	{
		this.initialContextFactory = strInitialContextFactory;
	}
	
	public String getProvideUrl()
	{
		return provideUrl;
	}
	
	public void setProvideUrl(String strProvideUrl) 
	{
		this.provideUrl = strProvideUrl;
	}
	
	public String getSecurityAuthentication()
	{
		return securityAuthentication;
	}
	
	public void setSecurityAuthentication(String strSecurityAuthentication) 
	{
		this.securityAuthentication = strSecurityAuthentication;
	}

	public String getSecurityPrincipal()
	{
		return securityPrincipal;
	}
	
	public void setSecurityPrincipal(String strSecurityPrincipal) 
	{
		this.securityPrincipal = strSecurityPrincipal;
	}
	
	public String getSecurityCredentials()
	{
		return securityCredentials;
	}
	
	public void setSecurityCredentials(String strSecurityCredentials) 
	{
		this.securityCredentials = strSecurityCredentials;
	}
	
	public String getSecurityProtocol()
	{
		return securityProtocol;
	}
	
	public void setSecurityProtocol(String strSecurityProtocol) 
	{
		this.securityProtocol = strSecurityProtocol;
	}
	
	public String[] getReturnAtts()
	{
		return returnAtts;
	}
	
	public void setReturnAtts(String[] strReturnAtts) 
	{
		this.returnAtts = strReturnAtts;
	}
	
	public String getSearchBase()
	{
		return searchBase;
	}
	
	public void setSearchBase(String strSearchBase) 
	{
		this.searchBase = strSearchBase;
	}
}
