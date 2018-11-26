layui.use(['table'],function(){
	table=layui.table;
 
	renderLoginRecordTable();
 
})

function renderLoginRecordTable(data){
	fl.renderTable({
		where:data?data:{}
	    ,elem: '#loginRecordTable'
	    ,url: '/admin/api/loginRecord/page' //数据接口
	    ,page: true //开启分页
	    ,cols: [[ //表头
	      {field: 'id', title: 'ID', width:80, sort: true, fixed: 'left'}
	      ,{field: 'gmtCreate', title: '登录时间'}
	      ,{field: 'ip', title: 'IP', width: 120}
	      ,{templet: '#tpl-device', title: '设备信息'},
	      {field: '', title: '操作'}
	     ]]
	  });
}