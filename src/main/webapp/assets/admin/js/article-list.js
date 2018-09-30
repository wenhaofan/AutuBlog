var form;
var table;
layui.use([ 'table', 'form' ], function() {
	table = layui.table;
	laytpl = layui.laytpl;
	form = layui.form;
	
	form.on('select(category-select)', function(data) {
		querylist($(".layui-form").serializeJson());
	});
	form.on('select(state-select)', function(data) {
		querylist($(".layui-form").serializeJson())
	});
	
	renderArticles();
	initCategorySelect();
});

function renderArticles(){
	table.render({
		page:{count:80,limit:10}
		,url:'/admin/api/article/list' 
	    ,elem: '#articles'
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
 
function querylist(data) {
	table.reload('articles', {
		url : '/admin/api/article/list',
		where :data
	// 设定异步数据接口的额外参数
	});
}

/** 初始化分类多选 */
function initCategorySelect() {
	var initCategorys = {
		type:"category",
		callback : function(categorys) {
			var html = template("option-tpl", {
				"categorys" : categorys
			});
			$("#category-select").html(html);
			form.render('select');
		}
	}
	metaUtils.listMeta( initCategorys);
}


$(document).ready(function() {
	
	//避免pjax重复加载js导致事件重复绑定
	if (typeof (adminArticleListIsBind) != "undefined") {
	    return;
	}   
	adminArticleListIsBind=true;
	
 
	$("body").on("click", ".article-remove", function() {
		removeArticle(this);
	})
	$("body").on("click", ".article-delete", function() {
		deleteArticle(this);
	})
	
	$("body").on("click", ".article-recover", function() {
		recoverArticle(this);
	})
	
	$("body").on("click",".article-push",function(){
		asyncMetaWeblog(this);
	})
 
	$("body").on("click",".createIndex",function(){
		resetIndex();
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

/**
 * 调用metaweblog接口同步发送文章
 * @param that
 * @returns
 */
function asyncMetaWeblog(that){
	fl.ajax({
		url:"/admin/api/article/asyncMetaWeblog/"+$(that).data("id"),
		success:function(data){
			fl.alertOk({});
		}
	})
}

/**
 * 恢复文章
 * @param that
 * @returns
 */
function recoverArticle(that){
	var id = $(that).attr("data-id");
	fl.alertConfirm({title:"是否确认恢复？",then:function(){
		fl.ajax({
			url : "/admin/api/article/recover/"+id,
			success : function(data) {
				fl.alertOkAndReload(data.msg)
			}
		})
	}})
}
/**
 * 重置所有索引
 * @returns
 */
var resetLayerIndex;
function resetIndex(){
	resetLayerIndex=layer.msg('重置中', {
			  icon: 16
			  ,shade: 0.01,
			  time: false,
			  success:function(){
					
			  }
			});
	//不知为何只有这样才能弹出加载提示框 
	setTimeout(() => {
		fl.ajax({
			url:"/admin/api/article/createIndex",
			success:function(data){
				fl.alertOk({title:"重置成功！"});
				layer.close(resetLayerIndex);
			},error:function(){
				layer.close(resetLayerIndex);
			}
		})
	}, 1000);
}