#key("comment_title")
[#(config.title)评论通知] Re:#(title)
#end

#key("comment")
	#Re:#(title)
	<br>
	#(comment.content)
	<hr>
	#set(commentUrl=projectPath+"/a/"+comment.getIdentify()+"?p="+comment.getPageNum()+"#li-comment-"+comment.getId())
	评论者:  <a href=#(commentUrl) >#(user.name)</a>
	
	#if(config.isAuditComment)
		请前往<a href="#(projectPath)admin/comment">后台</a>审核
	#else
		URL: <a href="#(commentUrl)" >#(commentUrl)</a>
	#end 
#end
 
#key("reply_title")
[#(config.title)回复通知]:Re:#(title)
#end

#key("reply")
	#set(commentUrl=projectPath+"/a/"+comment.getIdentify()+"?p="+comment.getPageNum()+"#li-comment-"+comment.getId())
	#Re:#(re)
	<br>
	#(comment.content)
	<hr>
	回复者:  <a href="#(commentUrl)" >#(user.name)</a>
	URL: <a href="#(commentUrl)" >#(commentUrl)</a>
#end
 