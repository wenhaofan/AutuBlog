package com.autu._admin.comment;

import java.util.List;

import com.autu.comment.CommentService;
import com.autu.common.controller.BaseController;
import com.autu.common.model.entity.AgentUser;
import com.autu.common.model.entity.Comment;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

public class AdminCommentService extends BaseController {

	@Inject
	private Comment dao;
	
	@Inject
	private CommentService frontCommentService;
 
	/**
	 * 分页查询评论
	 * @param pageNumber
	 * @param pageSize
	 * @param query
	 * @return
	 */
	public Page<Comment> page(Integer pageNumber,Integer pageSize,QueryComment query) {	
		SqlPara sqlPara=dao.getSqlPara("adminComment.page",Kv.by("query", query));
		Page<Comment> page= dao.paginate(pageNumber, pageSize, sqlPara);
		return setInPageNums(page); 
	}
	/**
	 * 设置多个评论所在页码
	 * @param page
	 * @return
	 */
	private Page<Comment> setInPageNums(Page<Comment> page) {
		for(Comment c:page.getList()) {
			setInPageNum(c);
		 }
		return page;
	}
	/**
	 * 回复评论
	 * @param toId
	 * @param content
	 * @param user
	 * @return
	 */
	public Ret reply(Integer toId,String content,AgentUser user) {
		
		Comment toComment = dao.findById(toId);
		
		Comment comment=new Comment();
		comment.setContent(content);
		comment.setEmail(user.getEmail());
		comment.setIdentify(toComment.getIdentify());
		comment.setName(user.getName());
		comment.setUserId(user.getId());
		comment.setWebsite(user.getWebsite());
 
		Comment parentComment=get(toId);
		if(parentComment.getParentId()!=0) {
			comment.setParentId(parentComment.getParentId());
		}
		
		comment.setToUserId(toComment.getUserId());
		comment.setIsAduit(true);
		comment.save();
	
		new Thread(()-> {
			frontCommentService.sendReplyEmail(comment);
		}).start();
		//向被回复人发送通知邮件

		return Ret.ok();
	}
	
	
	public Ret delete(Integer id) {
		dao.findById(id).delete();
		return Ret.ok();
	}
	/**
	 * 审核评论
	 * @param id
	 * @param aduit
	 * @return
	 */
	public Ret aduit(Integer id,boolean aduit) {
		Comment comment=dao.findById(id);
		comment.setIsAduit(aduit).update();
		
		if(comment.getParentId()==null) {
			return Ret.ok();
		}

		new Thread(()-> {
			frontCommentService.sendReplyEmail(comment);
		}).start();
		
		return Ret.ok();
	}
	
	public void setInPageNum(Comment c) {
		 SqlPara sql=dao.getSqlPara("adminComment.countBefore", Kv.by("identify", c.getIdentify()).set("gmtCreate", c.getGmtCreate()));
		 Integer beforeRow=Db.queryInt(sql.getSql(), sql.getPara());
		 c.setPageNum(beforeRow/6);
	}
	
	public Comment get(Integer id) {
		return dao.findById(id);
	}
	
	public List<Comment>  listRecent(Integer num){
		List<Comment> list= dao.find("select * from comment order by gmtCreate desc limit "+num);
		for(Comment comment:list) {
			setInPageNum(comment);
		}
		return list;
	}
}
