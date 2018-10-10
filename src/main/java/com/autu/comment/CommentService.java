package com.autu.comment;

import java.util.List;

import com.autu._admin.user.AdminUserService;
import com.autu.agentUser.AgentUserService;
import com.autu.article.ArticleService;
import com.autu.common._config.BlogContext;
import com.autu.common.annotation.SysLogInfo;
import com.autu.common.exception.MsgException;
import com.autu.common.interceptor.SysLogInterceptor;
import com.autu.common.kit.EmailKit;
import com.autu.common.model.entity.AgentUser;
import com.autu.common.model.entity.Article;
import com.autu.common.model.entity.Comment;
import com.autu.common.model.entity.User;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

public class CommentService {


	@Inject
	private static  Comment dao;
	@Inject
	private static AgentUserService agentUserService;
	@Inject
	private ArticleService articleService;
	@Inject
	private AdminUserService adminUserService;
	public void save(Comment comment,String cookie) {
		if(cookie==null) {
			throw new MsgException("什么鬼！");
		}
		
		if(StrKit.isBlank(comment.getContent())||comment.getContent().length()>512) {
			throw new MsgException("评论内容需大于0且小于等于512字！");
		}
		
		if(StrKit.isBlank(comment.getEmail())) {
			throw new MsgException("填个邮箱吧！");
		}
		 
		if(StrKit.isBlank(comment.getName())) {
			throw new MsgException("请留下你的姓名！");
		}
		
		if(StrKit.isBlank(comment.getIdentify())) {
			throw new MsgException("你评论的是哪个文章啊！");
		}
		
		if(comment.getParentId()==null) {
			comment.setParentId(0);
		}else if(comment.getParentId()!=0){
			//设置回复的消息所属的父级楼层
			Comment parentComment=get(comment.getParentId());
			if(parentComment.getParentId()!=0) {
				comment.setParentId(parentComment.getParentId());
			}
		}
		
		AgentUser agentUser=agentUserService.get(cookie);

		if(agentUser==null) {
			throw new MsgException("系统繁忙,请刷新后再试！");
		}
		agentUser.setEmail(comment.getEmail());
		agentUser.setName(comment.getName());
		agentUser.setWebsite(comment.getWebsite());
		agentUser.update();
		
		comment.setUserId(agentUser.getId());
		//如果为后台用户则直接设置为已审核
		if(comment.getUserId()==-1) {
			comment.setIsAduit(true);
		}
		if(!BlogContext.config.getIsAuditComment()) {
			comment.setIsAduit(true);
		}
		
		comment.save();
		
		new Thread(()->{
			//发送通知邮件
			sendHintAdminEmail(comment);
			if(comment.getIsAduit()) {
				sendReplyEmail(comment);
			}
		}).start();;
		
		
	}


	@Before(SysLogInterceptor.class)
	@SysLogInfo(value="发送评论提醒邮件",action="push")
	private boolean sendHintAdminEmail(Comment comment) {
		
		//如果等于该值 则表示为管理员则发送回复邮件
		if(comment.getUserId()==-1) {
			sendReplyEmail(comment);
			return true;
		}
		//否则通知管理员
		User adminUser=adminUserService.getAdminUser();
		if(adminUser==null) {
			return true;
		}
		
		AgentUser user=agentUserService.get(comment.getUserId());
		Article article=articleService.getArticle(comment.getIdentify());
		String title=null;
		if(article!=null) {
			title=articleService.getArticle(comment.getIdentify()).getTitle();
		}else {
			if(StrKit.equals(comment.getIdentify(), "links")) {
				title="友情链接";
			}
		}

		return EmailKit.sendEmail(adminUser.getEmail(), "你收到了一条评论 by "+BlogContext.config.getTitle(),getHintEmailContent(user.getName(),title));
	}
	
	public String getHintEmailContent(String userName,String title) {
		String serverUrl=BlogContext.getProjectPath();
		return userName+"评论了《"+title+"》,<a href='"+serverUrl+"/admin'>点此前往后台查看</a>";
	}
	
	public Page<Comment> page(Integer pageNum,Integer pageSize,String identify){
		SqlPara sql=dao.getSqlPara("comment.page", identify);
		return dao.paginate(pageNum, pageSize,sql);
	}
	
	public Comment get(Integer id) {
		return dao.findById(id);
	}
	
	public List<Comment> listRecent(){
		 List<Comment> comments=dao.find("select * from comment where isAduit=1 order by gmtCreate desc limit 6");
		 for(Comment c:comments) {
			 setInPageNum(c);
		 }
		 return comments;
	}
	
	public void setInPageNum(Comment c) {
		 Integer beforeRow=Db.queryInt("select count(id) from comment where identify= ? and gmtCreate > ?",c.getIdentify(),c.getGmtCreate());
		 c.setPageNum(beforeRow/6);
	}
	
	@Before(SysLogInterceptor.class)
	@SysLogInfo(value="发送评论回复邮件",action="push")
	public boolean sendReplyEmail(Comment comment) {
		
		if(comment.getParentId()==null) return true ;
 
		//获取被回复人信息
		AgentUser replayAgentUser=agentUserService.get(comment.getToUserId());
		
		//获取当前用户名称
		String userName=agentUserService.get(comment.getUserId()).getName();
		
		if(replayAgentUser!=null) {
			return EmailKit.sendEmail(replayAgentUser.getEmail(),userName+ "回复了你的评论  by "+BlogContext.config.getTitle(),getHintEmail(userName,comment));
		}
		return false;	 
	}
	
	public String getHintEmail(String name,Comment comment) {
		setInPageNum(comment);
		String serverUrl=BlogContext.getProjectPath();
		String url=serverUrl+"article/"+comment.getIdentify()+"?p="+comment.getPageNum()+"#li-comment-"+comment.getId();
		return name+"回复了你的评论,<a href='"+url+"'>点此查看</a>";
	}
}
