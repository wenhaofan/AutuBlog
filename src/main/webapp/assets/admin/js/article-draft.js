var form;
var table;
layui.use([ 'table'], function() {
	table = layui.table;
 
	renderArticles();
});

function renderArticles(){
	table.render({
		where:{state:0}
		,page:{count:80,limit:10}
		,url:'/admin/api/article/list' 
	    ,elem: '#article-drafts'
	    ,page: true //开启分页
	    ,cols: [[ //表头
	    	{field:'id', sort: true,title:"ID", width:90}
	      ,{field:'title',minWidth:150,title:"标题"}
	      ,{field:'pv', width:90,sort: true,title:"阅读量"}
	      ,{field:'state',width:90,templet:'#state-tpl',title:"状态"}
	      ,{field:'identify',title:"访问路径"}
	      ,{templet:'#operation-tpl', title:"操作"}
	    ]]
	 });
}
$(document).ready(function() {
	
	//避免pjax重复加载js导致事件重复绑定
	if (typeof (adminArticleDraftListIsBind) != "undefined") {
	    return;
	}   
	adminArticleDraftListIsBind=true;
	
 
	$("body").on("click", "#article-drafts .article-remove", function() {
		removeArticle(this);
	})
	$("body").on("click", "#article-drafts .article-delete", function() {
		deleteArticle(this);
	})
	
	$("body").on("click", "#article-drafts .article-recover", function() {
		recoverArticle(this);
	})
	
	$("body").on("click","#article-drafts .article-push",function(){
		asyncMetaWeblog(this);
	})
 
})

/**
 * 废弃文章
 * @param that
 * @returns
 */
function removeArticle(that){
	var id = $(that).data("id");
	fl.alertConfirm({title:"是否确认废弃？",then:function(){
		fl.ajax({
			url : "/admin/api/article/remove/"+id,
			success : function(data) {
				fl.alertOk({title:data.msg});
				$(that).parent().parent().parent().remove();
			}
		})
	}})
	return false;
}

/**
 * 删除文章
 * @param that
 * @returns
 */
function deleteArticle(that){
	var id = $(that).attr("data-id");
	fl.alertConfirm({title:"是否确认删除？",text:"注意：删除后将不能恢复！",then:function(){
		fl.ajax({
			url : "/admin/api/article/delete/"+id,
			success : function(data) {
				fl.alertOk({title:data.msg});
				$(that).parent().parent().parent().remove();
			}
		})
	}})
}

 