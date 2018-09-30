layui.use(['layer','table','form'], function(){
	 layer = layui.layer;
	 table = layui.table;
	 form  = layui.form;

	 renderTable();
	 
	 form.render();
	 //监听提交
	 form.on('submit(reply)', function(data){
	   submitReply(data.field);
	 	 return false;
	 });
	 
	 form.on("select(identify-select)",function(data){
		 renderTable(data.value,$("#state-select option:selected").val());
	 })
	  form.on("select(state-select)",function(data){
		 renderTable($("#identify-select option:selected").val(),data.value);
	 })
});
 
var replyData;
var layerIndex;
function submitReply(info){
	replyData=info;
	fl.alertConfirm({title:"确认回复？",then:function(){
		fl.ajax({
			url:"/admin/api/comment/reply",
			type:"post",
			data:replyData,
			success:function(data){
				fl.alertOk({title:"回复成功！"});
				layer.close(layerIndex);
			}
		})
	}})
}
/**
 * 渲染评论表格
 */
function renderTable(identify,state){
	 table.render({
		 page:{count:80,limit:10},
		 where:{
			 identify:identify,
			 state:state
		 }
	    ,elem: '#comments'
	    ,url:'/admin/api/comment/page'
	    ,cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
	    ,cols: [[
	    	{field:'id', sort: true,width:60,title:"id"}
	      ,{field:'name',title:"名称",width:130} //width 支持：数字、百分比和不填写。你还可以通过 minWidth 参数局部定义当前单元格的最小宽度，layui 2.2.1 新增
	      ,{field:'email',title:"邮箱"}
	      ,{field:'website',title:"网址"}
	      ,{field:'content',title:"评论内容"}
	      ,{templet:'#aduit-tpl',width:100,title:"状态"}
	      ,{templet:'#operation-tpl',title:"操作",width:190}
	    ]]
	  });
}

function showComment(that){
	var pageNum=$(that).data("page");
	var identify=$(that).data("identify");
	var id=$(that).data("id");
	window.open("/article/"+identify+"?p="+pageNum+"#li-comment-"+id);
}

var deleteId;
function deleteComment(that){
	deleteId=$(that).data("id");
	fl.alertConfirm({title:"确认删除！",then:function(){
		fl.ajax({
			url:"/admin/api/comment/delete",
			data:{toId:deleteId},
			success:function(data){
				fl.alertOkAndReload();	
			}
		})
	}})
}

var aduitInfo;
var $aduitEle;
function auditComment(that){
	var toId=$(that).data("id");
	var aduit=$(that).data("val");
	aduitInfo={toId:toId,aduit:aduit}

	$aduitEle=$(that);
	fl.alertConfirm({title:("确认"+(aduit=="0"?"不通过？":"通过？")),then:function(){
		fl.ajax({
			url:"/admin/api/comment/aduit",
			data:aduitInfo,
			success:function(data){
				fl.alertOkAndReload();	
			}
		})
	}})
}


function renderReply(that){
  	var toId=	$(that).data("id");
  	var content=$(that).parent().parent().prev().prev().text();
  	
  	var replyHtml=template("tpl-reply",{content:content,toId:toId});
	var width=520;
	if($(window).width()<768){
		width=350;
	}
  	layerIndex=layer.open({
  		title:"回复",
  		area: [width+'px', '380px'],
	  	  type: 1, 
	  	  content: replyHtml //这里content是一个普通的String
  	});
}
$(function(){
	
	//避免pjax重复加载js导致事件重复绑定
	if (typeof (adminCommentListIsBind) != "undefined") {
	    return;
	}   
	adminCommentListIsBind=true;
	
	$("body").on("click",".reply",function(){
		renderReply(this);
	})
	
	$("body").on("click",".aduit",function(){
		auditComment(this);
	})
	
	
	$("body").on("click",".delete",function(){
		deleteComment(this);
	})
	
	$("body").on("click",".go",function(){
		showComment(this);
	})
})
