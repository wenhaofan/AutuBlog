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
	 table.render({
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


var replyData;
var layerIndex;
 
$(function(){
	
	//避免pjax重复加载js导致事件重复绑定
	if (typeof (adminNavListIsBind) != "undefined") {
	    return;
	}   
	adminNavListIsBind=true;
	
	
	$("body").on("click",".add",function(){
	  	var editHtml=template("tpl-edit",{method:"add"});

	  	layerIndex=layer.open({
	  		title:"新增导航",
	  		area: ['420px', '300px'],
		  	  type: 1, 
		  	  content: editHtml, //这里content是一个普通的String
		  	  success:function(){
		  		  form.render();
		  	  }
	  	});
	})
	
	$("body").on("click",".update",function(){
		var id=$(this).data("id");
		var sort=$(this).parent().parent().prev().text();
		var url=$(this).parent().parent().prev().prev().text();
		var title=$(this).parent().parent().prev().prev().prev().text();
		
	  	var editHtml=template("tpl-edit",{method:"update",sort:sort,url:url,title:title,id:id});
	  	layerIndex=layer.open({
	  		title:"修改导航",
	  		area: ['420px', '300px'],
		  	  type: 1, 
		  	  content: editHtml, //这里content是一个普通的String
		  	  success:function(){
		  		  form.render();
		  	  }
	  	});
	})
 
 
	var deleteId;
	$("body").on("click",".delete",function(){
		deleteId=$(this).data("id");
		fl.alertConfirm({title:"确认删除！",then:function(){
			fl.ajax({
				url:"/admin/api/nav/delete",
				data:{toId:deleteId},
				success:function(data){
					fl.alertOkAndReload();	
				}
			})
		}})
	})
 
})

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