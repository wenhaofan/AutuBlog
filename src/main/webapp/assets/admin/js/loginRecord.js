layui.use(['table'],function(){
	table=layui.table;
	template.defaults.imports.currentSessionId=$("input[name='currentSessionId']").val();
	renderLoginRecordTable();
})

function renderLoginRecordTable(data){
	fl.renderTable({
		where:data?data:{}
	    ,elem: '#loginRecordTable'
	    ,url: '/admin/api/loginRecord/page' //数据接口
	    ,page: true //开启分页
	    ,cols: [[ //表头
	      {field: 'id', title: 'ID', sort: true, fixed: 'left'}
	      ,{field: 'gmtCreate', title: '登录时间'}
	      ,{field: 'ip', title: 'IP'}
	      ,{templet: '#tpl-device', title: '设备信息'},
	      {templet: '#tpl-operation', title: '操作'}
	     ]]
	  });
}

$(function(){
	
	if (typeof (isBindLoginRecord) != "undefined") {
	    return;
	}  

	isBindLoginRecord=true;
	
	$("body").on("click",".login-downline",function(){
		fl.ajax({
			url:"/admin/api/loginRecord/downline/"+$(this).data("id"),
			success:function(data){
				fl.alertOkAndReload("操作成功！");
			}
		})
	})
})