 

layui.define([ 'table','fl','form'], function(exports) {
 
	const routeList={
			bind:function(){
				const that=this;
				layui.form.on('select(route-state-select)',function(data){
					that.renderTable({isDeleted:data.value});
				})
			},load:function(){
				this.renderTable({});
			},renderTable:function(data){
				const table=layui.table;
				layui.fl.renderTable({
					elem:'#routeTable',
					 page: { count: 80, limit: 10 }
	                , url: '/admin/api/route/list',
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
	                    	
	                    	let tpl= '<button type="button" class="layui-btn layui-btn-xs  route-bind" data=id="'+d.id+'">绑定文章</button>'+
	                    	'<button type="button" class="layui-btn layui-btn-xs route-edit-template" data=id="'+d.id+'">修改模板</button>'+
	                    	'<button type="bbutton" class="layui-btn layui-btn-xs route-edit-data" data=id="'+d.articleId+'">修改文章</button>';
	                    
	                    	if(d.isDeleted){
	                    		tpl+=	'<button type="bbutton" class="layui-btn layui-btn-xs route-edit-recover" data=id="'+d.id+'">恢复</button>';
	                    	}else{
	                    		tpl+=	'<button type="bbutton" class="layui-btn layui-btn-xs route-edit-data" data=id="'+d.id+'">删除</button>';
	                    	}
	                    	
	                    	return tpl;
	                    }}
	                ]]
				})
			},pjaxLoad:function(){
				this.load();
			}
	};
	
	routeList.bind();
	routeList.load();
	
	exports('routeList',routeList);
})