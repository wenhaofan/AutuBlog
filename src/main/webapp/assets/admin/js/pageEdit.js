layui.define(['form','ueditor','fl','layer'],function(exports){
	
	const pageEdit={
			bind:function(){
				const form=layui.form,fl=layui.fl,that=this;
				
				
				form.on('submit(pageSubmit)',function(data){
					that.edit(data.field);
				})
				
			},
			load:function(){
				this.initEditor();
			},pjaxLoad:function(){
				
				
				if(layui.pageEdit.htmlEditor){
					layui.ueditor.UE.delEditor('page-ueditor');
				 
				}
				
				
				this.load();
				
			},initEditor:function(){
				const that=this;
			    that.htmlEditor = layui.ueditor.UE.getEditor('page-ueditor', {
	                initialFrameHeight: 400,
	                initialContent:  $("#content").html()
	            });
	            
	            that.htmlEditor.ready(function() {
	                that.htmlEditor.execCommand('serverparam', {
	                    'uploadType': 'page',
	                    'ueditor':true,
	                });
	            });
			},edit:function(fdata){
				fdata['content']=this.htmlEditor.getContent();
				
				layui.fl.ajax({
					url:'/admin/api/page/edit',
					data:fdata,
					success:function(res){
						layui.layer.msg("保存成功");
						$('.page-edit input[name="id"]').val(res.page.id);
					}
				})
			}
	};
	
	pageEdit.bind();
	pageEdit.load();
	exports("pageEdit",pageEdit);
	
})