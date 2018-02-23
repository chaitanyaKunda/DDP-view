/**
 * 
 */
package com.agility.ddp.view.util;

import java.io.File;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


/**
 * @author dguntha
 *
 */
@Component
@PropertySources({@PropertySource(value="classpath:common.properties"),
	@PropertySource(value="file:///E:/DDPConfig/common.properties",ignoreResourceNotFound=true)})
public class ApplicationUIProperties {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationUIProperties.class);

	private CompositeConfiguration compositeCofiguration;
	
	@Autowired
	private Environment env;
	
	 
	    @PostConstruct
	    private void init() {
	    	String[] fileNames = {"file:///E:/DDPConfig/common.properties"};
	        try {
	            compositeCofiguration = new CompositeConfiguration();
	           
	            for (String fileName : fileNames) {
	            	PropertiesConfiguration configure = constructPropertiesConfiguration(fileName);
	            	if (configure != null)
	            		compositeCofiguration.addConfiguration(configure);
	            }
	           
	            System.out.println("Loading the properties file: " + fileNames);
	            
	          
	           
	        } catch (Exception ex) {
	        	logger.error("ApplicationProperties.init() - Unable construct the file Name :"+fileNames, ex);
	        }
	    }
	 
	    /**
	     * Method used for getting the values from the composite Configuration.
	     * 
	     * @param key
	     * @return
	     */
	    public String getProperty(String key) {
	    	
	    	String value =  compositeCofiguration.getString(key);
	        return ( value  == null? env.getProperty(key) : value);
	    }
	    
	    /**
	     * Method used for getting the values from the composite Configuration.
	     * 
	     * @param key
	     * @return
	     */
	    public String getProperty(String key,String defaultValue) {
	    	
	    	String value =  compositeCofiguration.getString(key);
	       return  ( value  == null? env.getProperty(key,defaultValue) : value);
	        		
	        
	    }
	     
	    public void setProperty(String key, Object value) {
	    	compositeCofiguration.setProperty(key, value);
	    }
	     
	    /**
	     * Method used for construct properties configuration object.
	     * 
	     * @param fileName
	     * @return
	     */
	    private PropertiesConfiguration constructPropertiesConfiguration(String fileName) {
	    	
	    	 PropertiesConfiguration configuration = null;
	    	 
	    	try {
	    		boolean isFileExists = true;
	    		
	    		if (fileName.startsWith("file:///")) {
	    			String fileNam = fileName.substring(8);
	    			File file = new File(fileNam);
	    			isFileExists = file.exists();
	    		}
	    		
	    		if (isFileExists) {
			    	 FileChangedReloadingStrategy ddpfileChangedReloadingStrategy = new FileChangedReloadingStrategy();
				     ddpfileChangedReloadingStrategy.setRefreshDelay(1000);
				     configuration = new PropertiesConfiguration(fileName);
				     configuration.setReloadingStrategy(ddpfileChangedReloadingStrategy);
	    		}
	    	} catch (Exception ex) {
	    		logger.error("ApplicationProperties.constructPropertiesConfiguration() - Unable construct the file Name :"+fileName, ex);
	    	}
		     
		     return configuration;
	    }
	    
	    public static void main(String[] args) {
	    	ApplicationUIProperties properties = new ApplicationUIProperties();
	    	properties.getProperty("export.ruleByQry.clientID.docsSource");
	    	
	    }

}
