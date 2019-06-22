layui.define(['jquery', 'fl', 'form', 'laytpl', 'table', 'upload'], function (exports) {
    const config = {
        bind: function () {
            form = layui.form;
            const that=this;
            form.render();
        
 


            form.on("submit(editConfig)", function (data) {

                var $form = $(data.form);
                var $input = $form.find("div.layui-form-item").find("input[name='isAuditComment']");
                if ($input.length > 0) {
                    if (!data.field.isAuditComment) {
                        data.field.isAuditComment = 0;
                    }
                }
                that.editConfig(data.field);
                return false;
            })

         

        },
        pjaxLoad: function () {
            this.load();
        },
        load: function () {
            upload = layui.upload;
            

            //执行实例
            upload.render({
                acceptMime: 'image/ico'
                , exts: 'ico'
                , field: "upfile",
                elem: '.upload-ico' //绑定元素
                , url: '/admin/api/upload/ico' //上传接口

                , done: function (res) {
                    $(".ico-img").attr("src", res.info.url);
                    $("input[name='ico']").val(res.info.url);
                }, error: function () {
                    layui.fl.alertErro("上传失败！");
                }
            });
            upload.render({
                field: "upfile",
                elem: '.upload-logo' //绑定元素
                , url: '/admin/api/upload/logo' //上传接口
                , done: function (res) {
                    $(".logo-img").attr("src", res.info.url);
                    $("input[name='logo']").val(res.info.url);
                }, error: function () {
                    layui.fl.alertErro("上传失败！");
                }
            });
            this.setBasicForm();
        },
      
        setBasicForm: function () {
            layui.fl.ajax({
                url: "/admin/api/config",
                dataType: "json",
                success: function (data) {
                    var config = data.config;
                    $(".ico-img").attr("src", (config.ico && config.ico.length > 0) ? config.ico : '/favicon.ico')
                    $(".logo-img").attr("src", (config.logo && config.logo.length > 0) ? config.logo : '/logo.png')
                    form.val("editConfig", config)
                }
            })
        },
        editConfig: function (data) {
            layui.fl.ajax({
                url: "/admin/api/config/edit",
                data: data,
                type: "post",
                success: function (data) {
                    layui.fl.alertOk({ title: "修改成功！" });
                }
            })
        },
    };
    config.bind();
    config.load();
    exports("config", config);
});

