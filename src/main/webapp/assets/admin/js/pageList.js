 

layui.define([ 'table','fl','form','layer'], function(exports) {
 
	const pageList={
			bind:function(){
				const that=this;
				const layer=layui.layer;
				const $=layui.$;
				
				layui.form.on('select(page-state-select)',function(data){
					that.renderTable({isDeleted:data.value});
				})
				
				
				 
				$("body").on('click','.page-edit-data',function(){
					const id=$(this).data("id");
					if(id){
						$.pjax({url: '/admin/article/edit'+id, container: config.pjaxContainer})
					}else{
						layer.msg("请先绑定文章");
					}
				})
				
				$("body").on('click','.page-recover',function(){
					that.recoverPage($(this).data('id'));
				})
	 
				$("body").on('click','.page-remove',function(){
					that.removePage($(this).data('id'));
				})
				
			 
			},load:function(){
				layui.form.render();
				this.renderTable({});
			},renderTable:function(data){
				const table=layui.table;
				layui.fl.renderTable({
					elem:'#routeTable',
					 page: { count: 80, limit: 10 }
	                , url: '/admin/api/page/list',
	                where:data
	                , cols: [[ //表头
	                    { field: 'id', sort: true, title: "ID", width: 90 }
	                    , { field: 'name', minWidth: 150, title: "标题" }
	                    , { templet:function(d){
	                    	return d.article.pv;
	                    }, width: 90, sort: true, title: "阅读量" }
	                    , { field: 'pattern', title: "访问路径" }
	                    , { field: 'remark', title: "备注" }
	                    , {   width: 90, templet: function(d){
	                    	return d.isDeleted?'已删除':'正常';
	                    }, title: "状态" }
	                    , { templet: '#operation-tpl', title: "操作" ,templet:function(d){
	                    	
	                    	let tpl= '<button type="button" class="layui-btn layui-btn-xs page-edit-template"  ><a href="/admin/page/edit/'+d.id+'" pjax >修改模板</a></button>';
	                    	
	                    	if(d.articleId){
	                    		tpl+='<button type="button" class="layui-btn layui-btn-xs page-edit"  ><a href="/admin/article/edit/'+d.articleId+'" pjax >修改文章</button>';;	               
	                    	}
	                    
	                    	if(d.isDeleted){
	                    		tpl+=	'<button type="bbutton" class="layui-btn layui-btn-xs page-recover" data=id="'+d.id+'">恢复</button>';
	                    	}else{
	                    		tpl+=	'<button type="bbutton" class="layui-btn layui-btn-xs page-remove" data=id="'+d.id+'">删除</button>';
	                    	}
	                    	
	                    	return tpl;
	                    }}
	                ]]
				})
			},pjaxLoad:function(){
				this.load();
			},removePage:function(id){
				fl.ajax({
					url:"/admin/api/page/remove/"+id,
					success:function(data){
						fl.alertOkAndReload({title:"删除成功"});
					}
				})
			},recoverPage:function(id){
				fl.ajax({
					url:"/admin/api/page/recover/"+id,
					success:function(data){
						fl.alertOkAndReload({title:"恢复成功"});
					}
			})
			}
	};
	
	pageList.bind();
	pageList.load();
	
	exports('pageList',pageList);
})