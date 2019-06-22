layui.define([ 'form', 'ueditor', 'fl', 'layer', 'editormd' ],
		function(exports) {

			const pageEdit = {
				editormd : null,
				bind : function() {
					const form = layui.form, fl = layui.fl, that = this;

					form.on('submit(pageSubmit)', function(data) {
						that.edit(data.field);
					})

					$('body').on('click', '.page-edit-article', function() {
						const id = $('input[name="articleId"]').val();
						$.pjax({
							url : '/admin/article/edit/' + id,
							container : config.pjaxContainer
						})
					});

					$("body").on("click", '.fullscreen', function() {
						that.editormd.fullscreen();
					});

					$('body').on('click', '.page-preview', function() {
						that.preview();
					})
					
					if($('.page-edit input[name="articleId"]').val()){
						$('.page-edit-article').show();
					}
				},
				load : function() {
					 
					ctrlsEvent(function() {
						$('button[lay-filter="pageSubmit"]').trigger('click');
					});
					this.initEditor();
				},
				preview : function() {

				},
				pjaxLoad : function() {

					if (layui.pageEdit.htmlEditor) {
						layui.ueditor.UE.delEditor('page-ueditor');

					}

					this.load();

				},
				initEditor : function() {
					const that = this;

					const pageId = $('input[name="id"]').val();

					if (pageId) {
						layui.fl.ajax({
							url : "/admin/api/page/content/" + pageId,
							success : function(data) {
								that.loadEditor(data.content);
							}
						})
					} else {
						that.loadEditor();
					}

				},
				loadEditor : function(content) {

					this.editormd = editormd("page-editor", {
						width : "100%",
						height : 640,
						watch : false,
						toolbar : false,
						codeFold : true,
 
						placeholder : "Enjoy coding!",
						value : content,
						path : '/assets/plugins/editor/lib/',
						theme : (localStorage.theme) ? localStorage.theme
								: "default",
						mode : (localStorage.mode) ? localStorage.mode
								: "text/html",
					});

				},
				edit : function(fdata) {
					fdata['content'] = this.editormd.getValue();

					layui.fl.ajax({
						url : '/admin/api/page/edit',
						data : fdata,
						success : function(res) {
							layui.layer.msg("保存成功");
							$('.page-edit input[name="id"]').val(res.page.id);
						}
					})
				}
			};

			pageEdit.bind();
			pageEdit.load();
			exports("pageEdit", pageEdit);

		})