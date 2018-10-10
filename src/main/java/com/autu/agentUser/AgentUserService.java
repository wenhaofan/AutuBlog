package com.autu.agentUser;

import com.autu.common.aop.Inject;
import com.autu.common.model.entity.AgentUser;

public class AgentUserService {

	@Inject
	private  AgentUser dao;
	
	public static final String AGENT_USER_COOKIE_KEY="agentUserCookie";
	public static final int AGENT_USER_COOKIE_AGE=356*24*60*60;
	
	public void save(AgentUser agentUser) {
		agentUser.save();
	}
	
	public AgentUser get(Integer userId) {
		return dao.findById(userId);
	}
	
	public AgentUser get(String cookie) {
		return dao.findFirst(dao.getSqlPara("agentUser.findByCookie", cookie));
	}
	
	public void update(AgentUser agentUser) {
		dao.deleteById(agentUser.getId());
		agentUser.save();
	}
	
}
