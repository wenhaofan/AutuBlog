package com.autu.detail;

import java.util.List;

import com.autu._admin.user.AdminUserService;
import com.autu.common._config.BlogContext;
import com.autu.common.agentUser.AgentUserService;
import com.autu.common.exception.MsgException;
import com.autu.common.keys.KeyKit;
import com.autu.common.kit.EmailKit;
import com.autu.common.model.AgentUser;
import com.autu.common.model.Article;
import com.autu.common.model.Comment;
import com.autu.common.model.User;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
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
	private DetailService articleService;
	@Inject
	private AdminUserService adminUserService;
	public void save(Comment comment,String cookie) {

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
		
		comment.setIsAduit(false);
		comment.setUserId(agentUser.getId());
		//如果为后台用户则直接设置为已审核
		if(comment.getUserId()==-1) {
			comment.setIsAduit(true);
		}
		if(!BlogContext.config.getIsAuditComment()) {
			comment.setIsAduit(true);
		}
		
		comment.save();
		
		Article article=articleService.getArticle(comment.getIdentify());
		
		new Thread(()->{
			//发送通知邮件
			sendHintAdminEmail(comment,article);
			if(comment.getIsAduit()) {
				sendReplyEmail(comment,article);
			}
		}).start();;
		
		
	}

	
	private boolean sendHintAdminEmail(Comment comment,Article article) {
		setInPageNum(comment);
		//如果等于该值 则表示为管理员则仅发送回复邮件
		if(comment.getUserId()==-1) {
			sendReplyEmail(comment,article);
			return true;
		}
		
		//通知管理员,无后台账号则不通知
		User adminUser=adminUserService.getAdminUser();
		if(adminUser==null) {
			return true;
		}
		
		AgentUser user=agentUserService.get(comment.getUserId());
	
		String title=null;
		if(article!=null) {
			title=articleService.getArticle(comment.getIdentify()).getTitle();
		}else {
			if(StrKit.equals(comment.getIdentify(), "links")) {
				title="友情链接";
			}else if(StrKit.equals(comment.getIdentify(),"about")) {
				title="关于我";
			}
		}
		
		String emailTitle=KeyKit.use().getContent(
						"comment.comment_title",
						Kv.by("config", BlogContext.config)
						.set("title", title));
		
		String emailContent=KeyKit.use()
				.getContent(
						"comment.comment",
						Kv.by("projectPath", BlogContext.getProjectPath())
						.set("comment", comment)
						.set("user", user)
						.set("config", BlogContext.config));
	 
		return EmailKit.sendEmail(adminUser.getEmail(), emailTitle,emailContent);
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

	public boolean sendReplyEmail(Comment comment,Article article) {
		
		if(comment.getParentId()==null) return true ;
			
		setInPageNum(comment);
		
		String title=null;
		
		if(article!=null) {
			title=articleService.getArticle(comment.getIdentify()).getTitle();
		}else {
			if(StrKit.equals(comment.getIdentify(), "links")) {
				title="友情链接";
			}else if(StrKit.equals(comment.getIdentify(),"about")) {
				title="关于我";
			}
		}
		
		//获取被回复人信息
		AgentUser replayAgentUser=agentUserService.get(comment.getToUserId());
		
		//获取当前用户名称
		AgentUser user=agentUserService.get(comment.getUserId());
		
		String replyTitle=KeyKit.use()
				.getContent(
						"comment.reply_title",
						Kv.by("title", title)
						.set("config", BlogContext.config));
		
		String replyContent=KeyKit.use().getContent(
				"comment.reply",
				Kv.by("comment", comment)
				.set("projectPath", BlogContext.getProjectPath())
				.set("config", BlogContext.config)
				.set("user", user));
		
		if(replayAgentUser!=null) {
			return EmailKit.sendEmail(replayAgentUser.getEmail(),replyTitle,replyContent);
		}
		
		return false;	 
	}
 
}
