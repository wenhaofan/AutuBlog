function editInfo(data){
	data.about=htmlEditor.summernote('code');
	fl.ajax({
		url:"/admin/api/user/editInfo",
		data:data,
		type:"post",
		success:function(data){
				fl.alertOk({title:"修改成功！"});
		}
	})
}
 
 
layui.use(['form','upload'],function(){
	form=layui.form;
	var upload = layui.upload;
	form.render();
	form.on("submit(submitEdit)",function(data){
		editInfo(data.field);
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
	renderSummernote();
})

function renderSummernote(){
	 // 富文本编辑器
    htmlEditor = $('.summernote').summernote({
        lang: 'zh-CN',
        height: 340,
        placeholder: '',
        //上传图片的接口
        callbacks:{
            onImageUpload: function(files) {
            	
            	 var uploadUtil=new $.uploadUtil();
                 uploadUtil.setUploadServerUrl("/admin/api/upload/article");
                 uploadUtil.uploadFile({
                	file:files[0],
                	success:function(data){
            		   if(fl.isOk(data)){
                      	 var url =data.info.url;
                          console.log('url =>' + url);
                          htmlEditor.summernote('insertImage', url);
                      } else {
                          fl.alertError(result.msg || '图片上传失败');
                      }
                	}
                }); 
            }
        }
    });
	$('#html-container .note-editable').empty().html($("#tpl-content").html());
}