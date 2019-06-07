
layui.define([
    'jquery',
    'fl',
    'form',
    'layer',
    'table'
], function (exports) {

    const nav = {
        bind: function () {
            form = layui.form;

            table = layui.table;
            const that = this;
            const fl = layui.fl;
            //监听提交
            form.on('submit(add)', function (data) {
                that.add(data.field);
                return false;
            });
            form.on('submit(update)', function (data) {
                that.update(data.field);
                return false;
            });
            fl.renderTable({
                elem: '#comments'
                , url: '/admin/api/nav/list'
                , cellMinWidth: 80
                , cols: [[
                    { field: 'id', sort: true, width: 60, title: "id" }
                    , { field: 'title', title: "标题", width: 130 }
                    , { field: 'url', title: "URL" }
                    , { field: 'sort', title: "排序" }
                    , { templet: '#operation-tpl', title: "操作" }
                ]]
            });
        }, load: function () {

        }, pjaxLoad: function () {

        }, add: function (data) {
            const fl = layui.fl;
            $.ajax({
                url: "/admin/api/nav/add",
                type: "post",
                data: data,
                success: function (data) {
                    if (fl.isOk(data)) {
                        fl.alertOkAndReload("添加成功！");
                    }
                }
            })

        },
        update: function (data) {
            const fl = layui.fl;
            fl.ajax({
                url: "/admin/api/nav/update",
                type: "post",
                data: data,
                success: function (data) {
                    fl.alertOkAndReload("修改成功！");
                }
            })
        }
    };

    nav.bind();
    exports("nav", nav);
});



