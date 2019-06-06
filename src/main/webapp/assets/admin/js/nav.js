layui.use(['form','layer','form','table'], function(){
	
	 form = layui.form;
	 layer = layui.layer;
	 table = layui.table;
	 
	 //监听提交
	  form.on('submit(add)', function(data){
		   add(data.field);
	  	 return false;
	  });
	  form.on('submit(update)', function(data){
		   update(data.field);
	  	 return false;
	  });
	  fl.renderTable({
		elem: '#comments'
	    ,url:'/admin/api/nav/list'
	    ,cellMinWidth: 80 
	    ,cols: [[
	    	{field:'id', sort: true,width:60,title:"id"}
	      ,{field:'title',title:"标题",width:130} 
	      ,{field:'url',title:"URL"}
	      ,{field:'sort',title:"排序"}
	      ,{templet:'#operation-tpl',title:"操作"}
	    ]]
	  });
});


 

function add(data){
	$.ajax({
		url:"/admin/api/nav/add",
		type:"post",
		data:data,
		success:function(data){
			if(fl.isOk(data)){
				fl.alertOkAndReload("添加成功！");
			}
		}
	})

}
function update(data){
	fl.ajax({
		url:"/admin/api/nav/update",
		type:"post",
		data:data,
		success:function(data){
			fl.alertOkAndReload("修改成功！");
		}
	})
}