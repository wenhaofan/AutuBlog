$(function(){
	
	//避免pjax重复加载js导致事件重复绑定
	if (typeof (adminAdvanceListIsBind) != "undefined") {
	    return;
	}   
	adminAdvanceListIsBind=true;
	
	$("body").on("click",".privacy",function(){
		showPrivacy(this);
	})
	$("body").on("click",".mupdate",function(){
		renderMupdate(this);
	})
	$("body").on("click",".bupdate",function(){
		renderBupdate(this);
	})
	
	$("body").on("click",".madd",function(){
		renderMEdit({});
	})
	$("body").on("click",".badd",function(){
		renderBEdit({});
	})

	$("body").on("click",".mdelete",function(){
		mdelete(that);
	})
	
	$("body").on("click",".bdelete",function(){
		bdelete(this);
	})
})
var deleteMid;
function mdelete(that){
	deleteMid=$(that).data("id");
	fl.alertConfirm({title:"是否确认删除？",then:function(){
		fl.ajax({
			url:"/admin/api/metaConfig/mconfigDelete/"+deleteMid,
			success:function(data){
				fl.alertOkAndReload();
			}
		})
	}})
}

var deleteBid;
function bdelete(that){
	deleteBid=$(that).data("id");
	fl.alertConfirm({title:"是否确认删除？",then:function(){
		fl.ajax({
			url:"/admin/api/baiduConfig/bconfigDelete/"+deleteBid,
			success:function(data){
				fl.alertOkAndReload();
			}
		})

	}});
}
function renderBupdate(that){
	fl.ajax({
		url:"/admin/api/baiduConfig/bget/"+$(that).data("id"),
		dataType:'json',
		success:function(data){
			renderBEdit(data.config);
		}
	}) 
}
function renderMupdate(taht){
	fl.ajax({
		url:"/admin/api/metaConfig/mget/"+$(taht).data("id"),
		dataType:'json',
		success:function(data){
			renderMEdit(data.config);
		}
	})
}
/**
 * 显示隐藏的密码
 * @param that
 * @returns
 */
function showPrivacy(that){
	var content=$(that).data("content");
	var text=$(that).text();
	$(that).text(content);
	$(that).data("content",text);
}
 
function renderMEdit(data){
	var content=template("tpl-medit",data);
	var width=520;
	if($(window).width()<768){
		width=350;
	}
  	layerIndex=layer.open({
  		title:"新增",
  		area: [width+'px', '350px'],
	  	  type: 1, 
	  	  content: content //这里content是一个普通的String
  	});
}
function renderBEdit(data){
	var content=template("tpl-bedit",data);
	var width=520;
	if($(window).width()<768){
		width=350;
	}
  	layerIndex=layer.open({
  		title:"新增",
  		area: [width+'px', '230px'],
	  	  type: 1, 
	  	  content: content //这里content是一个普通的String
  	});
}

function medit(data){
	fl.ajax({
		url:"/admin/api/metaConfig/mconfigEdit",
		data:data,
		type:"post",
		success:function(data){
			fl.alertOkAndReload();
		}
	})
}
function bedit(data){
	fl.ajax({
		url:"/admin/api/baiduConfig/bconfigEdit",
		data:data,
		type:"post",
		success:function(data){
			fl.alertOkAndReload();
		}
	})
}

layui.use(['form','table','laytpl'],function(){
	form=layui.form;
	table=layui.table;
	
	form.render();
	
	table.render({
		elem: '#metaweblog'
	   ,url:'/admin/api/metaConfig/mList'
	   ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
	   ,cols: [[
	   	{field:'id', sort: true,width:60,title:"id"}
	     ,{field:'website',title:"网站",width:100}
	     ,{field:'url',title:"接口路径"}
	     ,{field:'userName',title:"账号",width:100}
	     ,{templet:'#tpl-mprivacy',title:"密码",width:150}
	     ,{templet:'#tpl-moperation',title:"操作",width:150}
	   ]]
	 });
	
	table.render({
		  elem: '#baiduseo'
		   ,url:'/admin/api/baiduConfig/bList'
		   ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
		   ,cols: [[
		   	 {field:'id', sort: true,width:60,title:"id"}
		     ,{field:'site',title:"网站" }
		     ,{templet:'#tpl-bprivacy',title:"token",width:200}
		     ,{templet:'#tpl-boperation',title:"操作",width:150}
		]]
	});
	
	form.on("submit(editMconfig)",function(data){
		medit(data.field);
	})
	form.on("submit(editBconfig)",function(data){
		bedit(data.field);
	})
	
	form.on("submit(pushLinks)",function(data){
		pushLinks(data.field);
	})
}) 

function pushLinks(data){
	fl.ajax({
		data:data,
		url:"/admin/api/baiduConfig/pushBaiduLinks",
		type:"post",
		success:function(data){
			fl.alertOk({title:"提交成功！"});
		}
	})
}