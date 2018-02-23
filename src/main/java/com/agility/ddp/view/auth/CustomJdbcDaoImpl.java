package com.agility.ddp.view.auth;

import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agility.ddp.core.logger.AuditLog;

//@PropertySource("classpath:ddp.properties")
public class CustomJdbcDaoImpl extends JdbcDaoImpl 
{
	private static final Logger logger = LoggerFactory.getLogger(CustomJdbcDaoImpl.class);
	
//	@Autowired
//	Environment env;
//	
	//@Value("${test.Prop2}")
	//private String two;

	public CustomJdbcDaoImpl()
	{
        super();
    }
	
	@AuditLog
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{		
		logger.info("User [{}] trying login.",username);
		//logger.info("#######  PROP VALE ####### : {}",env.getProperty("event_name_com.agility.ddp.view.auth.CustomJdbcDaoImpl", "UNKNOWN"));
		//logger.info("#######  PROP VALE ####### : {}",env.getProperty("test.Prop2", "UNKNOWN"));
		//logger.info("#######  PROP VALE ####### : {}",two);
				
		try
		{
			UserDetails userDetails = super.loadUserByUsername(username);
			Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();
			for(GrantedAuthority authority:authorities)
			{
				logger.debug("User [{}] - assigned role : [{}]",username,authority);
			}
			if(!userDetails.isEnabled())
			{
				throw new UsernameNotFoundException("[InLine] User ["+username+"] is disabled.");
			}
			return userDetails;
		}
		catch(UsernameNotFoundException unfEx)
		{
			logger.warn(unfEx.getMessage()+" : Querying ActiveDirectory to check user logistics/{} exists or not.",username);
			throw new UsernameNotFoundException(unfEx.getMessage()+" : Querying ActiveDirectory to check user logistics/"+username+" exists or not.");
		}
		
	}
	
	@Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }

}
