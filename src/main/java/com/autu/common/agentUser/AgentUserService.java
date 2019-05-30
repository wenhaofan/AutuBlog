package com.autu.common.agentUser;

import com.autu.common.model.AgentUser;
import com.jfinal.aop.Inject;

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
	
	public boolean saveOrUpdate(AgentUser agentUser) {
		if(agentUser.getId()==null) {
			return agentUser.save();
		}else {
			return agentUser.update();
		}
	}
}
