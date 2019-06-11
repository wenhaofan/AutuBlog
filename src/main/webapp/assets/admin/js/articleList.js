layui.define([
    'jquery',
    'fl',
    'form',
    'layer',
    'table',
    'fl'
], function (exports) {
    let articleList = {

        resetLayerIndex: 0,
        //layui加载完成的回调
        pjaxLoad:function(){
            this.load();
        },
        load: function () {
            this.renderArticles();
            this.initCategorySelect();
        }, bind: function () {
            let that = this;
            const form = layui.form, table = layui.table;
            $("body").on("click", ".article-remove", function () {
                that.removeArticle(this);
            })
            $("body").on("click", ".article-delete", function () {
                that.deleteArticle(this);
            })

            $("body").on("click", ".article-recover", function () {
                that.recoverArticle(this);
            })

            $("body").on("click", ".article-push", function () {
                that.asyncMetaWeblog(this);
            })

            $("body").on("click", ".createIndex", function () {
                that.resetIndex();
            })

            form.on('select(category-select)', function (data) {
                that.querylist($(".layui-form").serializeJson());
            });
            form.on('select(state-select)', function (data) {
                that.querylist($(".layui-form").serializeJson())
            });
        },
        querylist: function (data) {
            layui.table.reload('articles', {
                url: '/admin/api/article/list',
                where: data
                // 设定异步数据接口的额外参数
            });
        },   //渲染文章列表
        renderArticles: function () {
            layui.fl.renderTable({
                page: { count: 80, limit: 10 }
                , url: '/admin/api/article/list'
                , elem: '#articles'
                , page: true //开启分页
                , cols: [[ //表头
                    { field: 'id', sort: true, title: "ID", width: 90 }
                    , { field: 'title', minWidth: 150, title: "标题" }
                    , { field: 'pv', width: 90, sort: true, title: "阅读量" }
                    , { field: 'state', width: 90, templet: '#state-tpl', title: "状态" }
                    , { field: 'identify', title: "访问路径" }
                    , { templet: '#operation-tpl', title: "操作" }
                ]]
            });
        },
        /**
                * 重置lucene所有索引
         * @returns
         */
        resetIndex: function () {
            let that = this;
            const fl = layui.fl, layer = layui.layer;
            resetLayerIndex = layer.msg('重置中', {
                icon: 16
                , shade: 0.01,
                time: false,
                success: function () {

                }
            });
            layui.fl.ajax({
                url: "/admin/api/article/createIndex",
                success: function (data) {
                    layui.fl.alertOk({ title: "重置成功！" });
                    that.layer.close(resetLayerIndex);
                }, error: function () {
                    that.layer.close(resetLayerIndex);
                }
            })
        },
        /**
             * 恢复文章
        * @param that
        * @returns
        */
        recoverArticle: function (obj) {
            var id = $(obj).data("id");
            let that = this;
            layui.fl.alertConfirm({
                title: "是否确认恢复？", then: function () {
                    layui.fl.ajax({
                        url: "/admin/api/article/recover/" + id,
                        success: function (data) {
                            layui.fl.alertOkAndReload(data.msg)
                        }
                    })
                }
            })
        },
        /** 初始化分类多选 */
        initCategorySelect: function () {
            let that = this;
            var initCategorys = {
                type: "category",
                callback: function (categorys) {
                    var html = template("option-tpl", {
                        "categorys": categorys
                    });
                    $("#category-select").html(html);
                    layui.form.render('select');
                }
            }
            this.listMeta(initCategorys);
        },
        /**根据类型查询文章元数据,为空则查询所有*/
        listMeta: function (paras) {
            let that = this;
            $.ajax({
                url: "/api/meta/list/" + paras.type,
                dataType: "json",
                success: function (data) {
                    if (layui.fl.isOk(data)) {
                        paras.callback(data.metas);
                    }
                }
            })
        },
        /**
                * 废弃文章
         * @param that
         * @returns
         */
        removeArticle: function (obj) {
            let that = this;
            var id = $(obj).data("id");
            layui.fl.alertConfirm({
                title: "是否确认废弃？", then: function () {
                    layui.fl.ajax({
                        url: "/admin/api/article/remove/" + id,
                        success: function (data) {
                            layui.fl.alertOk({ title: data.msg });
                            $(obj).parent().parent().parent().remove();
                        }
                    })
                }
            })
            return false;
        },

        /**
                 * 删除文章
         * @param that
         * @returns
         */
        deleteArticle: function (obj) {
            let that = this;
            var id = $(obj).attr("data-id");
            layui.fl.alertConfirm({
                title: "是否确认删除？", text: "注意：删除后将不能恢复！", then: function () {
                    layui.fl.ajax({
                        url: "/admin/api/article/delete/" + id,
                        success: function (data) {
                            layui.fl.alertOk({ title: data.msg });
                            $(that).parent().parent().parent().remove();
                        }
                    })
                }
            })
        },

        /**
                * 调用metaweblog接口同步发送文章
         * @param that
         * @returns
         */
        asyncMetaWeblog: function (obj) {
            let that = this;
            layui.fl.ajax({
                url: "/admin/api/article/asyncMetaWeblog/" + $(obj).data("id"),
                success: function (data) {
                    layui.fl.alertOk({});
                }
            })
        },

    };

    articleList.bind();
    articleList.load();
    exports("articleList", articleList);
});





