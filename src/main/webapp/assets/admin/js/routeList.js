 

layui.define([ 'table','fl'], function(exports) {
 
	const route={
			bind:function(){
				
			},load:function(){
				
				const table=layui.table;
				layui,fl.renderTable({
					elem:'#routeTable',
					  page: { count: 80, limit: 10 }
	                , url: '/admin/api/route/list'
	                , page: true //开启分页
	                , cols: [[ //表头
	                    { field: 'id', sort: true, title: "ID", width: 90 }
	                    , { field: 'pattern', minWidth: 150, title: "标题" }
	                    , { field: 'pv', width: 90, sort: true, title: "阅读量" }
	                    , { field: 'state', width: 90, templet: '#state-tpl', title: "状态" }
	                    , { field: 'identify', title: "访问路径" }
	                    , { templet: '#operation-tpl', title: "操作" }
	                ]]
				})
				
			},pjaxLoad:function(){
				
			}
	};
	
	exports('route',route);
})