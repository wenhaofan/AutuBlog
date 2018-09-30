layui.use(['table','form'],function(){
	table=layui.table;
	form=layui.form;
	
	renderSysLogTable();
	 
  	form.on("select(queryLevel)",function(data){
  		renderSysLogTable({level:data.value})
  	})
  	
  	form.render();
})

function renderSysLogTable(data){
	  table.render({
		where:data?data:{}
	    ,elem: '#sysLogTable'
	    ,url: '/admin/api/sysLog/page' //数据接口
	    ,page: true //开启分页
	    ,cols: [[ //表头
	      {field: 'id', title: 'ID', width:80, sort: true, fixed: 'left'}
	      ,{field: 'content', title: '内容'}
	      ,{field: 'action', title: 'action',width:100}
	      ,{field: 'url', title: '请求路径'}
	      ,{field: 'ip', title: 'IP', width: 120}
	      ,{templet: '#tpl-level', title: '类型', width: 90, sort: true}
	      ,{field: 'data', title: '数据'}
	      ,{field: 'gmtCreate', title: '创建时间', width: 180, sort: true}
	     ]]
	  });
}