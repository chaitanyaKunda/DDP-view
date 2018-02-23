/**
 * 
 */
package com.agility.ddp.view.web;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//import com.agility.ddp.data.domain.DdpCompanyService;

/**
 * @author DGuntha
 *
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"classpath*:META-INF/spring/applicationContext*.xml","file:src/main/webapp/WEB-INF/spring/webmvc-config.xml"})
//@WebAppConfiguration
//@Transactional
public class DdpBranchControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
	private FilterChainProxy springSecurityFilter;
	
	//@Autowired MockHttpSession session;
	//https://myshittycode.com/2013/10/23/how-to-unit-test-spring-mvc-controller/
	//@Autowired
	//private DdpCompanyService ddpCompanyService;
	
	@Autowired
	private WebApplicationContext 	wac;
	
	private List<SimpleGrantedAuthority> authorities = new ArrayList< SimpleGrantedAuthority >();
	
	//@Before
	public void before()
	{
		//org.mockito.MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(this.springSecurityFilter,"/*").build();
		authorities.add(new SimpleGrantedAuthority("admin_role"));
	
	}
	
	
	/**
	 * Test method for {@link com.agility.ddp.view.web.DdpBranchController#mediatorController()}.
	 */
	//@Test
	public void testMediatorController() {

	}

	/**
	 * Test method for {@link com.agility.ddp.view.web.DdpBranchController#listBranches(java.lang.Integer)}.
	 */
	//@Test
	public void testListBranches() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.agility.ddp.view.web.DdpBranchController#create(com.agility.ddp.data.domain.DdpBranch, org.springframework.validation.BindingResult, org.springframework.ui.Model, javax.servlet.http.HttpServletRequest)}.
	 */
	//@Test
	public void testCreate() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.agility.ddp.view.web.DdpBranchController#update(com.agility.ddp.data.domain.DdpBranch, org.springframework.validation.BindingResult, org.springframework.ui.Model, javax.servlet.http.HttpServletRequest)}.
	 */
	//@Test
	public void testUpdate() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.agility.ddp.view.web.DdpBranchController#list(java.lang.Integer, java.lang.Integer, org.springframework.ui.Model)}.
	 */
	//@Test
	public void testList() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.agility.ddp.view.web.DdpBranchController#show(java.lang.String, org.springframework.ui.Model)}.
	 */
	//@Test
	public void testShow() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.agility.ddp.view.web.DdpBranchController#delete(java.lang.String, java.lang.Integer, java.lang.Integer, org.springframework.ui.Model)}.
	 */
	//@Test
	public void testDelete() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.agility.ddp.view.web.DdpBranchController#createForm(org.springframework.ui.Model)}.
	 */
	//@Test
	public void testCreateForm() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.agility.ddp.view.web.DdpBranchController#updateForm(java.lang.String, org.springframework.ui.Model)}.
	 */
	//@Test
	public void testUpdateForm() {
		fail("Not yet implemented"); // TODO
	}

}
