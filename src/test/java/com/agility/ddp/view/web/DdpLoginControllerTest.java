package com.agility.ddp.view.web;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;


import javax.servlet.http.HttpSession;


//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"classpath*:META-INF/spring/applicationContext*.xml"})
//@WebAppConfiguration
public class DdpLoginControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
	private FilterChainProxy springSecurityFilter;
	
	@Autowired
	private WebApplicationContext webAppContext;
	
	@Before
	public void before()
	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).addFilter(this.springSecurityFilter,"/*").build();
	}
	
	//@Test
	public void success_loginInAndLogOutTest() throws Exception
	

	{
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/resources/j_spring_security_check").param("j_username", "dguntha").param("j_password", "Chips@12");
		HttpSession session = mockMvc.perform(formLogin().loginProcessingUrl("/resources/j_spring_security_check").user("dguntha").password("Chips@12"))
											 .andDo(print())
											.andExpect(status().isFound())
											.andExpect(redirectedUrl("/")).andReturn().getRequest().getSession();
		
		assertNotNull(session);
		
		MockHttpServletResponse response = mockMvc.perform(get("/resources/j_spring_security_logout")).andExpect(status().is(HttpStatus.FOUND.value())).andReturn().getResponse();
		assertNotNull(response);
	}
	//@Test
	public void failure_loginTest() throws Exception
	{
		HttpSession session = mockMvc.perform(post("/resources/j_spring_security_check")
											.param("j_username", "admin").param("j_password", "wrongpswd"))
											.andExpect(status().is(HttpStatus.FOUND.value()))
											.andExpect(redirectedUrl("/login?login_error=t"))
											.andReturn().getRequest().getSession();
		assertNotNull(session);
	}
	
//	@Test
//	public void testCreateForm() throws Exception {
//		mockMvc.perform(get("/ddpusers/list").param("txtUName", "chaitanya").param("txtLName", "").param("txtEmailId", "")).andDo(print())
//						.andExpect(handler().handlerType(DdpUserController.class))
//						.andExpect(handler().methodName("search"))
//						.andExpect(forwardedUrl("WEB-INF/tiles/default.jsp"))
//						.andExpect(status().isOk());
//	}
	

}
