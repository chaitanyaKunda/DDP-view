package com.agility.ddp.view.web;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.agility.ddp.data.domain.DdpCompany;
import com.agility.ddp.data.domain.DdpCompanyService;
import com.agility.ddp.view.util.Constant;

//https://docs.spring.io/spring-security/site/docs/current/reference/html/test-mockmvc.html
//http://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#test-mockmvc
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@ContextConfiguration(value={"classpath*:META-INF/spring/applicationContext*.xml","classpath*:WEB-INF/spring/webmvc-config.xml"})

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:META-INF/spring/applicationContext*.xml","file:src/main/webapp/WEB-INF/spring/webmvc-config.xml"})
@WebAppConfiguration
@Transactional
public class DdpCompanyControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
	private FilterChainProxy springSecurityFilter;
	
	//@Autowired MockHttpSession session;
	//https://myshittycode.com/2013/10/23/how-to-unit-test-spring-mvc-controller/
	@Autowired
	private DdpCompanyService ddpCompanyService;
	
	@Autowired
	private WebApplicationContext 	wac;
	
	private List<SimpleGrantedAuthority> authorities = new ArrayList< SimpleGrantedAuthority >();
	
	//@Autowired
	//private DdpCompanyController ddpCompanyController;
	
	@Before
	public void before()
	{
		//org.mockito.MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(this.springSecurityFilter,"/*").build();
		authorities.add(new SimpleGrantedAuthority("admin_role"));
		//this.mockMvc = MockMvcBuilders.standaloneSetup(ddpCompanyController).addFilter(this.springSecurityFilter,"/*").build();
//		List<SimpleGrantedAuthority> allAuthorities = new ArrayList<SimpleGrantedAuthority>();
//		allAuthorities.add(new SimpleGrantedAuthority("admin_role"));
//		User user = new User("admin", "", true, true, true, true, allAuthorities);
//		session.setAttribute("sessionParam",user);
	}
	
	
	@Test
	public void testCompanyMediator() throws Exception {
		
		List<SimpleGrantedAuthority> authorities = new ArrayList< SimpleGrantedAuthority >();
		authorities.add(new SimpleGrantedAuthority("admin_role"));
		
		 mockMvc.perform(get("/ddpcompanys/list/mediator").with( user("dguntha").authorities(authorities)))
		 		 .andDo(print())
				 .andExpect(status().isOk())
				 .andExpect(view().name("ddpcompanys/display"));
		
	}

	@Test
	public void testListCompanys() throws Exception {

		List<SimpleGrantedAuthority> authorities = new ArrayList< SimpleGrantedAuthority >();
		authorities.add(new SimpleGrantedAuthority("admin_role"));
		
		 this.mockMvc.perform(get("/ddpcompanys/list/listCompanys").param("page", "1").with(user("dguntha").authorities(authorities)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	//@Rollback(false)
	public void testCreate() throws Exception {
		
		//BindingResult bindingResult = mock(BindingResult.class);
		//HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		
		
		
		
		this.mockMvc.perform(post("/ddpcompanys/list").param("create", "")
				.param("comCompanyName", "TST")
				.param("comCountryCode", "TST")
				.param("comCountryName", "INDIA")
				.param("comRegion", "APR")
				//.param("comStatus", Constant.ACTIVE+"")
				.param("comCompanyCode","TST")
				.with(user("dguntha").authorities(authorities)))
				.andDo(print())
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/ddpcompanys/list/list/TST"))
				.andExpect(redirectedUrl("/ddpcompanys/list/list/TST"));
	}
//
	@Test
	//@Rollback(false)
	public void testUpdate() throws Exception {
		
		this.mockMvc.perform(put("/ddpcompanys/list").param("update", "")
				.param("comCompanyName", "TST")
				.param("comCountryCode", "TST")
				.param("comCountryName", "INDIA")
				.param("comRegion", "APR")
				.param("comStatus", Constant.ACTIVE+"")
				.param("comCompanyCode","TST")
				.param("status", "0")
				.with(user("dguntha").authorities(authorities)))
				.andDo(print())
				.andExpect(status().isFound());
	}
//
	@Test
	public void testList() throws Exception {
		this.mockMvc.perform(get("/ddpcompanys/list").param("list", "").param("page", "1").param("size", "1").with(user("dguntha").authorities(authorities)))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(view().name("ddpcompanys/list"))
		.andExpect(model().attributeExists("ddpcompanys"));

	}
//
	@Test
	public void testShow() throws Exception {
		
		this.mockMvc.perform(get("/ddpcompanys/list/list/{comCompanyCode}","LPL")
				.with(user("dguntha").authorities(authorities)))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(view().name("ddpcompanys/show"))
		.andExpect(model().attributeExists("ddpcompany"));

	}

	@Test
	public void testUpdateForm() throws Exception {
		
		this.mockMvc.perform(get("/ddpcompanys/list/{comCompanyCode}/form","LPL")
				.with(user("dguntha").authorities(authorities)))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(view().name("ddpcompanys/update"))
		.andExpect(model().attributeExists("ddpCompany"))
		.andExpect(model().attributeExists("ddpruledetails"));
	}

	@Test
	public void testDeletecompany() throws Exception {

		this.mockMvc.perform(delete("/ddpcompanys/list/{comCompanyCode}","TST").param("page", "1").param("size", "1").with(user("dguntha").authorities(authorities)))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void testCreateForm() throws Exception {
		
		
		this.mockMvc.perform(get("/ddpcompanys/list/form")
				.with(user("dguntha").authorities(authorities)))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(view().name("ddpcompanys/create"))
		.andExpect(model().attributeExists("ddpCompany"))
		.andExpect(model().attributeExists("ddpruledetails"));
	}

}
