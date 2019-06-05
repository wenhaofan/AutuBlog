
define([
    'jquery',
    'fl',
    'layui'
], function ($, fl, layui) {

    const meta = {
        pjaxLoad: function () {
            var initCategory = {
                type: "category",
                callback: function (categorys) {
                    var $metaBlock = $("#category-list");
                    var $meta;
                    $.each(categorys, function (index, item) {
                        $meta = $(template('tpl-meta-list', {
                            meta: item,
                            type: "category"
                        }));
                        $metaBlock.append($meta);
                    })
                }
            }

            this.listMeta(initCategory);

            var initTags = {
                type: "tag",
                callback: function (tags) {
                    var $metaBlock = $("#tag-list");
                    var $meta;
                    $.each(tags, function (index, item) {
                        $meta = $(template('tpl-meta-list', {
                            meta: item,
                            type: "tag"
                        }));
                        $metaBlock.append($meta);
                    })
                }
            }
            this.listMeta(initTags);
        },
        bind: function () {
            var form = layui.form;
            const that=this;
            // 监听提交
            form.on('submit(metaForm)', function (data) {
                var field = data.field;
                if (field.id) {
                    that.doUpdate(field);
                } else {
                     that.add(field);
                }
                return false;
            });

            $("a").hover(function () {
                return false;
            })
            
            $("#reset").click( function () {
                    var type = $('input[name="type"]').val();
                    $("#submit-btn-" + type).text(
                        "添加" + (type == "category" ? "分类" : "标签"));
                    $('input[name="id"]').val(0);
                    $(this).hide();
                })
            $("body").on('click', ".meta-list .layui-nav-item a", function () {
                var left = $(this).position().left
                    + parseFloat($(this).css("marginLeft"));
                var top = $(this).position().top + $(this).height();
                $(this).next().css({
                    opacity: 1,
                    left: left,
                    top: top
                })
                var $next = $(this).next();
                if ($next.is(':hidden')) {
                    $(this).find("span").addClass("layui-nva-mored");
                    $(this).next().show();
                } else {
                    $(this).next().hide();
                    $(this).find("span").removeClass("layui-nva-mored");
                }

            })
            $("body").on("click",".meta-changetype",function(){
                that.changeType($(this).data("type"));
            })
        },
        	/**根据类型查询文章元数据,为空则查询所有*/
		listMeta:function (paras){
			$.ajax({
				url:"/api/meta/list/"+paras.type,
				dataType:"json",
				success:function(data){
					if(fl.isOk(data)){
						paras.callback(data.metas);
					}
				}
			})
		},
        update: function (obj, type, id, name, pri) {
            $('input[name="id"]').val(id);
            $('input[name="mname"]').val(name);
            $('input[name="sort"]').val(pri);
            $('input[name="type"]').val(type);
            if (type == "category") {
                $("#submit-btn-tag").hide();
                $("#submit-btn-category").show();
                $("#submit-btn-category").text("修改分类");
            } else {
                $("#submit-btn-category").hide();
                $("#submit-btn-tag").show();
                $("#submit-btn-tag").text("修改标签");
            }
            $("#reset").show();
            var $ul = $(obj).parent().parent();
            $ul.hide();
        },

        add: function (data) {
            fl.ajax({
                url: "/admin/api/meta/add",
                type: "post",
                data: data,
                dataType: "json",
                success: function (data) {
                    fl.alertOkAndReload(data.text);
                }
            })
        },

        doUpdate: function (data) {
            fl.ajax({
                url: "/admin/api/meta/update",
                data: data,
                dataType: "json",
                success: function (data) {
                    fl.alertOkAndReload(data.text);
                }
            })
        }, remove: function (id) {
            fl.alertConfirm({
                title: '确认删除吗？',
                then: function () {
                    fl.ajax({
                        url: "/admin/api/meta/remove/" + id,
                        dataType: "json",
                        success: function (data) {
                            fl.alertOkAndReload(data.text);
                        }
                    })
                }
            })
        }, changeType:function (type) {
                $("input[name='type']").val(type);
        }
    }

    meta.bind();
    return meta;
});

