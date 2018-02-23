package com.agility.ddp.view.web;

import java.util.ArrayList;
import java.util.List;

import com.agility.ddp.data.domain.DdpGroup;
import com.agility.ddp.data.domain.DdpGroupSetup;
import com.agility.ddp.data.domain.DdpRole;
import com.agility.ddp.data.domain.DdpRoleSetup;
import com.agility.ddp.data.domain.DdpUser;
public class UserWrapper{
	
	DdpUser ddpUser = new DdpUser() ;
	
	DdpGroup ddpGroup = new DdpGroup();
	
	DdpRole ddpRole = new DdpRole();
	
	List<DdpRoleSetup> ddpRoleSetup1 = new ArrayList<DdpRoleSetup>();
	
	List<DdpGroupSetup> ddpGroupSetup1 = new ArrayList<DdpGroupSetup>();
	
	DdpGroupSetup ddpGroupSetup = new DdpGroupSetup();
	
	List<DdpRole> ddpRole1= new ArrayList<DdpRole>();
	
	List<DdpGroup> ddpGroup1 = new ArrayList<DdpGroup>();
	
	DdpRoleSetup ddpRoleSetup = new DdpRoleSetup();
	
	List<StatusBean> ddpStatus=new ArrayList<StatusBean>();
	
	public List<StatusBean> getDdpStatus() {
		StatusBean statusBean = new StatusBean();
		statusBean.setStsId(0);
		statusBean.setStsString("Inline");
		ddpStatus.add(statusBean);
		statusBean = new StatusBean();
		statusBean.setStsId(1);
		statusBean.setStsString("Logistics");
		ddpStatus.add(statusBean);
		return ddpStatus;
	}

	public DdpRole getDdpRole() {
		return ddpRole;
	}


	public void setDdpRole(DdpRole ddpRole) {
		this.ddpRole = ddpRole;
	}

	
	public DdpGroup getDdpGroup() {
		return ddpGroup;
	}


	public void setDdpGroup(DdpGroup ddpGroup) {
		this.ddpGroup = ddpGroup;
	}

	
	
	public List<DdpRoleSetup> getDdpRoleSetup1() {
		return ddpRoleSetup1;
	}


	public void setDdpRoleSetup1(List<DdpRoleSetup> ddpRoleSetup1) {
		this.ddpRoleSetup1 = ddpRoleSetup1;
	}


	public List<DdpGroupSetup> getDdpGroupSetup1() {
		return ddpGroupSetup1;
	}


	public void setDdpGroupSetup1(List<DdpGroupSetup> ddpGroupSetup1) {
		this.ddpGroupSetup1 = ddpGroupSetup1;
	}


	public DdpGroupSetup getDdpGroupSetup() {
		return ddpGroupSetup;
	}


	public void setDdpGroupSetup(DdpGroupSetup ddpGroupSetup) {
		this.ddpGroupSetup = ddpGroupSetup;
	}


	public List<DdpRole> getDdpRole1() {
		return ddpRole1;
	}


	public void setDdpRole1(List<DdpRole> ddpRole1) {
		this.ddpRole1 = ddpRole1;
	}


	public List<DdpGroup> getDdpGroup1() {
		return ddpGroup1;
	}


	public void setDdpGroup1(List<DdpGroup> ddpGroup1) {
		this.ddpGroup1 = ddpGroup1;
	}


	public DdpRoleSetup getDdpRoleSetup() {
		return ddpRoleSetup;
	}


	public void setDdpRoleSetup(DdpRoleSetup ddpRoleSetup) {
		this.ddpRoleSetup = ddpRoleSetup;
	}


	public void setDdpUser(DdpUser ddpUser) {
		this.ddpUser = ddpUser;
	}


	public DdpUser getDdpUser() {
		return ddpUser;
	}


	public UserWrapper() {
	}
}
