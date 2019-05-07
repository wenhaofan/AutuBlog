
var blogger={
	htmlEditor:null,
	isInit:false,
	init:function(){
		this.htmlEditor= UE.getEditor('blogger-ueditor',{
			initialFrameHeight:400,
			initialContent :this.getContent()
		});
		this.isInit=true;
	},getContent:function(){
		
		if(!this.isInit){
			return $(".blogger #about-content").html();
		}
		
		return this.htmlEditor.getContent();
	},editInfo:function (data){
		data.about=this.getContent();
		fl.ajax({
			url:"/admin/api/user/editInfo",
			data:data,
			type:"post",
			success:function(data){
				fl.alertOk({title:"修改成功！"});
			}
		})
	}
}

 
layui.use(['form','upload'],function(){
	form=layui.form;
	var upload = layui.upload;
	form.render();
	form.on("submit(submitEdit)",function(data){
		blogger.editInfo(data.field);
	})
	
	 //执行实例
   upload.render({
	field:"upfile",
    elem: '#uploadHeadImg' //绑定元素
    ,url: '/admin/api/upload/headimg' //上传接口
    ,done: function(res){
      	$(".avatar-img").attr("src",res.info.url);
      	$("input[name='headImg']").val(res.info.url);
    },error: function(){
      fl.alertErro("上传失败！");
    }
  });
}) 
$(function(){
	blogger.init();
})

 