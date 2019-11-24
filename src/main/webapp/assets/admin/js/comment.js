layui.define(['layer', 'table', 'form', 'jquery'], function (exports) {

    const comment = {
        bind: function () {

            form = layui.form;
            const that = this;


            form.render();
            //监听提交
            form.on('submit(reply)', function (data) {
                that.submitReply(data.field);
                return false;
            });

            form.on("select(identify-select)", function (data) {
                that.renderTable(data.value, $("#state-select option:selected").val());
            })
            form.on("select(state-select)", function (data) {
                that.renderTable($("#identify-select option:selected").val(), data.value);
            })


            $("body").on("click", ".reply", function () {
                that.renderReply(this);
            })

            $("body").on("click", ".aduit", function () {
                that.auditComment(this);
            })


            $("body").on("click", ".delete", function () {
                that.deleteComment(this);
            })

            $("body").on("click", ".go", function () {
                that.showComment(this);
            })
        },
        load: function () {
            this.renderTable();
        },
        pjaxLoad: function () {
            this.load();
        },
        submitReply: function (replyData) {
            fl = layui.fl;
            layer = layui.layer;
            fl.alertConfirm({
                title: "确认回复？", then: function () {
                    fl.ajax({
                        url: "/admin/api/comment/reply",
                        type: "post",
                        data: replyData,
                        success: function (data) {
                            fl.alertOk({ title: "回复成功！" });
                            layer.close(layerIndex);
                        }
                    })
                }
            })
        },
        /**
         * 渲染评论表格
         */
        renderTable: function (identify, state) {
            layui.fl.renderTable({
                page: { count: 80, limit: 10 },
                where: {
                    identify: identify,
                    state: state
                }
                , elem: '#comments'
                , url: '/admin/api/comment/page'
                , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                , cols: [[
                    { field: 'id', sort: true, width: 60, title: "id" }
                    , { field: 'name', title: "名称", width: 130 } //width 支持：数字、百分比和不填写。你还可以通过 minWidth 参数局部定义当前单元格的最小宽度，layui 2.2.1 新增
                    , { field: 'email', title: "邮箱" }
                    , { field: 'website', title: "网址" }
                    , { field: 'content', title: "评论内容" }
                    , { templet: '#aduit-tpl', width: 100, title: "状态" }
                    , { templet: '#operation-tpl', title: "操作", width: 190 }
                ]]
            });
        }, showComment: function (that) {
            var pageNum = $(that).data("page");
            var identify = $(that).data("identify");
            var id = $(that).data("id");
            window.open("/article/" + identify + "?p=" + pageNum + "#li-comment-" + id);
        }, deleteComment: function (obj) {
            deleteId = $(obj).data("id");
            const fl = layui.fl;
            fl.alertConfirm({
                title: "确认删除！", then: function () {
                    fl.ajax({
                        url: "/admin/api/comment/delete",
                        data: { toId: deleteId },
                        success: function (data) {
                            fl.alertOkAndReload();
                        }
                    })
                }
            })
        }, auditComment: function (obj) {
            var toId = $(obj).data("id");
            var aduit = $(obj).data("val");
            const fl = layui.fl;
            fl.alertConfirm({
                title: ("确认" + (aduit == "0" ? "不通过？" : "通过？")), then: function () {
                    fl.ajax({
                        url: "/admin/api/comment/aduit",
                        data: { toId: toId, aduit: aduit },
                        success: function (data) {
                            fl.alertOkAndReload();
                        }
                    })
                }
            })
        }, renderReply: function (obj) {
            const layer = layui.layer;
            var toId = $(obj).data("id");
            var content = $(obj).parent().parent().prev().prev().text();

            var replyHtml = template("tpl-reply", { content: content, toId: toId });
            var width = 520;
            if ($(window).width() < 768) {
                width = 350;
            }
            layerIndex = layer.open({
                title: "回复",
                area: [width + 'px', '380px'],
                type: 1,
                content: replyHtml //这里content是一个普通的String
            });
        }
    }

    comment.bind();
    comment.load();
    exports("comment", comment);
});



