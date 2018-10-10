package com.autu.user;

import java.util.Date;

import com.autu.common.model.entity.LoginRecord;
import com.autu.common.model.entity.Session;
import com.autu.common.model.entity.User;
import com.jfinal.aop.Inject;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 *  
 * @author 范文皓
 * @createDate 创建时间：2018年9月6日 下午12:38:51
 */
public class LoginService {
	 
	@Inject
	private User dao;
 
	public static final  String loginUserKey="loginUser";
	public static final String sessionIdName="antuBlogSessionId";
	public static final String sessionCacheKey="loginSessionCacheKey";
	/**
	 * 在session的失效时间小于该时间时访问系统 则失效时间重置为访问时间后的24小时
	 */
	private static final Integer resetSessionExpiredTime=4*60*60*1000;
 
	public Ret  login(String ukAccount,String pwd,boolean isKeep,String ip) {

		
		Kv vals=Kv.by("account",ukAccount);
		SqlPara sql=dao.getSqlPara("login.login",vals);
		
		User loginUser= dao.findFirst(sql);
		
		if(loginUser==null) {
			return Ret.fail("msg", "账号密码错误!");
		}
		
		String dbPwd=loginUser.getPwd();
		
		String loginPwdMd5=HashKit.md5(pwd);
		
		System.out.println("db1"+loginPwdMd5);
		System.out.println("db2"+dbPwd);
		System.out.println("result1:"+StrKit.equals(dbPwd, loginPwdMd5));
		System.out.println("result2"+dbPwd.equals(loginPwdMd5));
		if(!StrKit.equals(dbPwd, loginPwdMd5)) {
			return Ret.fail("msg", "账号密码错误!");
		}

		loginUser.setPwd("");
		
		LoginRecord log=new LoginRecord();
		log.setIp(ip);
		log.setTime(new Date());
		log.setUserId(loginUser.getId());
		log.save();
		Session session=new Session();
		session.setId(StrKit.getRandomUUID());
		session.setUserId(loginUser.getId());
		
		long liveSecond=isKeep?365*24*60*60:24*60*60;
		
		session.setExpireAt(System.currentTimeMillis()+liveSecond*1000);
		
		if(!session.save()) {
			return Ret.fail("msg","session保存失败!");
		}

		CacheKit.put(LoginService.sessionCacheKey, session.getId(), session);
		
		CacheKit.put(LoginService.loginUserKey,session.getId(), loginUser);
		
		return Ret.ok("cookieMaxAge", liveSecond)
				.set(LoginService.loginUserKey, loginUser).set(sessionIdName, session.getId());
	}
	
	
	public User getUserWithSessionId(String sessionId) {
		Session session=	CacheKit.get(LoginService.sessionCacheKey, sessionId);
		
		if(session!=null&&(session.getExpireAt()-System.currentTimeMillis())<=resetSessionExpiredTime) {
			session.setExpireAt(System.currentTimeMillis()+24*60*60*1000);
			session.update();
		}
		
		return CacheKit.get(LoginService.loginUserKey, sessionId);
	}
	
	public User loginWithSessionId(String sessionId) {
		Session session=Session.dao;
		session=session.findById(sessionId);
		if(session==null) {
			return null;
		}
		if(session.isExpired()) {
			session.delete();
			return null;
		}
		Object userId=session.getUserId();
		User loginUser=  dao.findById(userId);
		if(loginUser==null) {
			return null;
		}
		CacheKit.put(LoginService.loginUserKey, session.getId(), loginUser);
		
		return loginUser;
	}
	

	
}
