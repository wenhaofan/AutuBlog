 

 
 
var updatePwdIndex;
layui.use(['form','element','layer'],function(){
	form=layui.form;
	layer=layui.layer;
	var element = layui.element;
	  //监听提交
	  form.on('submit(updatePwd)', function(data){
		   fl.ajax({
			   url:"/admin/api/user/editPwd",
			   data:data.field,
			   success:function(data){
					fl.alertOk({title:"密码修改成功！"});
					layer.close(updatePwdIndex);
			   }
		   })
	  	 return false;
	  });
	  
	  //监听提交
	  form.on('submit(updateInfo)', function(data){
		 
		   fl.ajax({
			   url:"/admin/api/user/editInfo",
			   data:data.field,
			   success:function(data){
				    fl.alertOk({title:"修改成功！"});
					layer.close(updatePwdIndex);
			   }
		   })
	  	 return false;
	  });
})
function updatePwd(){
	updatePwdIndex=layer.open({
  		title:"修改密码",
  		area: ['340px', '215px'],
	  	type: 1, 
	  	content: $("#tpl-update-pwd").html(),
	  	success:function(){
	  		form.render();
	  	}
  	});
}
function updateInfo(){
	updatePwdIndex=layer.open({
  		title:"修改个人信息",
  		area: ['340px', '265px'],
	  	type: 1, 
	  	content: $("#tpl-update-info").html(),
	  	success:function(){
	  		form.render();
	  	}
  	});
}


$(function(){
	$("#updatePwd").click(function(){
		updatePwd();
	});
	$("#updateInfo").click(function(){
		updateInfo();
	});
	$(".layui-layout-right").click(function(){
		$(this).trigger("mouseover");
	})
})