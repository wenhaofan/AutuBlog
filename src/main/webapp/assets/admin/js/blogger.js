lauyi.define([
    'jquery',
    'fl',
    'ueditor',
    'form',
    'upload'
], function (exports) {
    var blogger = {
        htmlEditor: null,

        bind: function () {
            const form = layui.form;
            const that = this;
            form.on("submit(bloggerEdit)", function (data) {
                that.editInfo(data.field);
            })
        },
        load: function () {
            form = layui.form;
            var upload = layui.upload;
            form.render();
            //执行实例
            upload.render({
                field: "upfile",
                elem: '#uploadHeadImg' //绑定元素
                , url: '/admin/api/upload/headimg' //上传接口
                , done: function (res) {
                    $(".avatar-img").attr("src", res.info.url);
                    $("input[name='headImg']").val(res.info.url);
                }, error: function () {
                    layui.fl.alertErro("上传失败！");
                }
            });
            this.htmlEditor = layui.ueditor.UE.getEditor('blogger-ueditor', {
                initialFrameHeight: 400,
                initialContent: this.getContent()
            });
        }, getContent: function () {

            if (!this.isInit) {
                return $(".blogger #about-content").html();
            }

            return this.htmlEditor.getContent();
        }, editInfo: function (data) {
            data.about = this.getContent();
            fl.ajax({
                url: "/admin/api/user/editInfo",
                data: data,
                type: "post",
                success: function (data) {
                    fl.alertOk({ title: "修改成功！" });
                }
            })
        }
    }
    blogger.bind();
    exports("blogger", blogger);
});


